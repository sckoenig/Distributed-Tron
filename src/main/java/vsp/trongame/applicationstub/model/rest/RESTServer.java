package vsp.trongame.applicationstub.model.rest;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import vsp.trongame.applicationstub.model.rest.ressources.Game;
import vsp.trongame.applicationstub.model.rest.ressources.Registration;
import vsp.trongame.applicationstub.model.rest.ressources.Steering;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static vsp.trongame.applicationstub.model.rest.AdapterState.*;

public class RESTServer {

    public static final String ROUTE_PUT_REGISTRATION = "/registration";
    public static final String ROUTE_PUT_GAME = "/game";
    public static final String ROUTE_PUT_STEERING = "/steering";
    private static final int MIN_PORT = 5555;
    private static final int MAX_PORT = 49152;

    private final ExecutorService executorService;

    private final RESTAdapter restAdapter;
    private HttpServer server;
    private Random rand;
    private Gson gson;

    public RESTServer(RESTAdapter restAdapter){
        this.executorService = Executors.newSingleThreadExecutor();
        this.restAdapter = restAdapter;
        this.rand = new Random();
        this.gson = new Gson();
    }

    public void stop(){
        server.stop(1);
        executorService.shutdownNow();
    }

    public void start() {
        String ip = "192.168.193.57";
        int port;
        boolean portAvailable = false;

        while (!portAvailable) {
            port = rand.nextInt(MIN_PORT, MAX_PORT + 1);

            try {
                server = HttpServer.create(new InetSocketAddress(ip, port), 0);
                portAvailable = true;
                restAdapter.setLocalAddress(String.format("http://%s:%s", ip, port));

                server.createContext(ROUTE_PUT_GAME, new HttpHandler() {
                    @Override
                    public void handle(HttpExchange exchange) throws IOException {
                        if (restAdapter.getCurrentState() == REST_REGISTRATION || restAdapter.getCurrentState() == BUILDING_GAME) {
                            if (exchange.getRequestMethod().equals("PUT")) {
                                String body = readRequestBody(exchange);
                                Game game = gson.fromJson(body, Game.class);
                                restAdapter.handleRessource(game);
                            }
                        }
                    }
                });

                server.createContext(ROUTE_PUT_REGISTRATION, new HttpHandler() {
                    @Override
                    public void handle(HttpExchange exchange) throws IOException {

                        if (restAdapter.getCurrentState() == REST_REGISTRATION) { //else game is full or active

                            if (exchange.getRequestMethod().equals("PUT")) {
                                String body = readRequestBody(exchange);
                                Registration registration = gson.fromJson(body, Registration.class);
                                boolean success = restAdapter.handleRessource(registration);
                                int responseCode = success? 200 : 406;
                                //success == true -> 200, else 406
                                exchange.sendResponseHeaders(responseCode, 0);
                            }
                        } else {
                            exchange.sendResponseHeaders(410, 0);
                        } // send 410
                    }
                });

                server.createContext(ROUTE_PUT_STEERING, new HttpHandler() {
                    @Override
                    public void handle(HttpExchange exchange) throws IOException {
                        if (restAdapter.getCurrentState() == RUNNING) {
                            if (exchange.getRequestMethod().equals("PUT")) {
                                String body = readRequestBody(exchange);
                                Steering steering = gson.fromJson(body, Steering.class);
                                restAdapter.handleRessource(steering);
                            }
                        }
                    }
                });


            } catch (IOException e) {
                // catch and roll another port
            }
        }


    }

    private String readRequestBody(HttpExchange exchange) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        StringBuilder requestBody = new StringBuilder("");
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            requestBody.append(inputLine);
        }
        return new String(requestBody);
    }

}
