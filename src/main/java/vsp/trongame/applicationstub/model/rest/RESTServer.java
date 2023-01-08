package vsp.trongame.applicationstub.model.rest;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
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

import static vsp.trongame.applicationstub.model.rest.RESTProtocol.*;
import static vsp.trongame.applicationstub.model.rest.RESTStubState.*;

/**
 * Simple Server that receives REST ressources over http. Creates routes as defined in {@link RESTProtocol}.
 */
public class RESTServer {

    private final ExecutorService executorService;
    private final RESTStub restStub;
    private final Gson gson;
    private HttpServer server;
    private int port;

    public RESTServer(RESTStub restStub, ExecutorService executorService) {
        this.executorService = executorService;
        this.restStub = restStub;
        this.gson = new Gson();
    }

    /**
     * Starts the server by creating a http server and the necessary routes.
     */
    public void start() {
        try {
            server = HttpServer.create(new InetSocketAddress(0), 0);
            this.port = server.getAddress().getPort();

            createGameRoute();
            createRegistrationRoute();
            createSteeringRoute();

            server.setExecutor(executorService);
            server.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops this server by stopping the underlying http server.
     */
    public void stop() {
        server.stop(1);
    }

    /**
     * Route for {@link Game} ressource.
     */
    private void createGameRoute() {
        this.server.createContext(ROUTE_PUT_GAME, exchange -> {
            if (exchange.getRequestMethod().equals(SUPPORTED_METHOD) && (restStub.getCurrentState() == REST_REGISTRATION)) {
                String body = readRequestBody(exchange);
                System.out.println("REST: received game ressource: " + body);

                Game game = readRessource(body, Game.class);
                restStub.handleRessource(game);
            }
        });
    }

    /**
     * Route for {@link Registration} ressource.
     */
    private void createRegistrationRoute() {
        server.createContext(ROUTE_PUT_REGISTRATION, exchange -> {
            int responseCode = STATUS_NOT_AVAILABLE;

            if (exchange.getRequestMethod().equals(SUPPORTED_METHOD) && restStub.getCurrentState() == REST_REGISTRATION) { //else game is full or active
                String body = readRequestBody(exchange);
                System.out.println("REST: received registration ressource: " + body);

                Registration registration = readRessource(body, Registration.class);
                boolean success = restStub.handleRessource(registration, exchange.getRemoteAddress().getAddress().getHostAddress());
                responseCode = success ? STATUS_OK : STATUS_DENIED;
            }

            exchange.sendResponseHeaders(responseCode, -1);
            restStub.startGameIfFull();
        });
    }

    /**
     * Route for {@link Steering} ressource.
     */
    private void createSteeringRoute() {
        server.createContext(ROUTE_PUT_STEERING, exchange -> {
            if (exchange.getRequestMethod().equals(SUPPORTED_METHOD) && restStub.getCurrentState() == RUNNING) {
                String body = readRequestBody(exchange);
                System.out.println("REST: received steering ressource: " + body);

                Steering steering = readRessource(body, Steering.class);
                restStub.handleRessource(steering);
            }
        });
    }

    /**
     * Reads the request body from the http request.
     *
     * @param exchange the http request
     * @return the Body as a String
     * @throws IOException on I/O Error
     */
    private String readRequestBody(HttpExchange exchange) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        StringBuilder requestBody = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            requestBody.append(inputLine);
        }
        return new String(requestBody);
    }

    public int getPort() {
        return this.port;
    }

    /**
     * Reads a Ressource from json String.
     * @param json json string
     * @param ressourceClass the ressource's class
     * @return the ressource or null, if json and class don't match.
     * @param <T> the ressource's class
     */
    private <T> T readRessource(String json, Class<T> ressourceClass) {
        T ressource = null;
        try {
            ressource = gson.fromJson(json, ressourceClass);
        } catch (JsonSyntaxException e) {
            System.err.println("REST: message protocol violation: " + json);
            // ignore message if it doesnt follow protocol
        }
        return ressource;
    }
}
