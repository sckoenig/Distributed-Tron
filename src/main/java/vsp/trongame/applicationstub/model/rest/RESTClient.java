package vsp.trongame.applicationstub.model.rest;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

/**
 * Sends REST ressources over http.
 */
public class RESTClient {

    private final HttpClient client;

    public RESTClient(){
        client = HttpClient.newHttpClient();
    }

    /**
     * Http Request with "GET" method.
     * @param address the receiver's address
     * @param route the route
     * @return response body as json string or an empty string, if there is no body
     * @throws HttpTimeoutException on time out
     */
    public String getRESTRessource(String address, String route) throws IOException {

        String responseBody = "";
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(address + route)).timeout(Duration.of(5, SECONDS)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            responseBody = response.body();
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        } catch (URISyntaxException e) {
            //
        }
        return responseBody;
    }

    /**
     * Http Request with "PUT" method.
     * @param address the receiver's address
     * @param route the route
     * @param ressource the ressource to put as a json string
     * @return the http response code
     * @throws IOException on I/O Error
     */
    public int putRESTRessource(String address, String route, String ressource) throws IOException {

        int statusCode = -1;

        try {
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(address + route)).
                    headers("Content-Type", "application/json", "accept", "application/json").
                    timeout(Duration.of(1, SECONDS)).
                    PUT(HttpRequest.BodyPublishers.ofString(ressource)).build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            statusCode = response.statusCode();

        } catch (HttpTimeoutException | ConnectException | URISyntaxException e){
            System.err.println("Connecting to Address not possible.");
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
        return statusCode;
    }

}
