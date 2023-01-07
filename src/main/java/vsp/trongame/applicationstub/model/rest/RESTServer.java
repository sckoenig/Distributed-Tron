package vsp.trongame.applicationstub.model.rest;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import vsp.trongame.applicationstub.model.rest.ressources.Game;
import vsp.trongame.applicationstub.model.rest.ressources.Registration;
import vsp.trongame.applicationstub.model.rest.ressources.Steering;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static vsp.trongame.applicationstub.model.rest.RESTProtocol.*;
import static vsp.trongame.applicationstub.model.rest.RESTStubState.*;

public class RESTServer {

    private final ExecutorService executorService;
    private final RESTStub restAdapter;
    private final Gson gson;
    private HttpServer server;
    private int port;

    public RESTServer(RESTStub restAdapter, ExecutorService executorService) {
        this.executorService = executorService;
        this.restAdapter = restAdapter;
        this.gson = new Gson();
    }

    public void start() {
        try {
            server = HttpServer.create(new InetSocketAddress(0), 0);
            this.port = server.getAddress().getPort();

            createGameRoute();
            createRegistratioRoute();
            createSteeringRoute();

            server.setExecutor(executorService);
            server.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        server.stop(1);
    }

    private void createGameRoute() {
        this.server.createContext(ROUTE_PUT_GAME, exchange -> {
            if (restAdapter.getCurrentState() == REST_REGISTRATION || restAdapter.getCurrentState() == BUILDING_GAME) {
                if (exchange.getRequestMethod().equals(SUPPORTED_METHOD)) {
                    String body = readRequestBody(exchange);
                    Game game = gson.fromJson(body, Game.class);
                    restAdapter.handleRessource(game);
                }
            }
        });
    }

    private void createRegistratioRoute() {
        server.createContext(ROUTE_PUT_REGISTRATION, exchange -> {
            System.out.println("RECEIVED : Registration");
            int responseCode = STATUS_NOT_AVAILABLE;

            if (restAdapter.getCurrentState() == REST_REGISTRATION) { //else game is full or active

                if (exchange.getRequestMethod().equals(SUPPORTED_METHOD)) {
                    String body = readRequestBody(exchange);
                    Registration registration = gson.fromJson(body, Registration.class);
                    boolean success = restAdapter.handleRessource(registration);
                    responseCode = success ? STATUS_OK : STATUS_DENIED;
                }
            }
            exchange.sendResponseHeaders(responseCode, 0);
        });
    }

    private void createSteeringRoute() {
        server.createContext(ROUTE_PUT_STEERING, exchange -> {
            if (restAdapter.getCurrentState() == RUNNING) {
                if (exchange.getRequestMethod().equals(SUPPORTED_METHOD)) {
                    String body = readRequestBody(exchange);
                    Steering steering = gson.fromJson(body, Steering.class);
                    restAdapter.handleRessource(steering);
                }
            }
        });
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


    public int getPort() {
        return this.port;
    }
}
