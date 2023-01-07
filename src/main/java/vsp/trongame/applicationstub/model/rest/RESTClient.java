package vsp.trongame.applicationstub.model.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

public class RESTClient {

    private HttpClient client;

    public RESTClient(){
        client = HttpClient.newHttpClient();
    }

    public String getRESTRessource(String address, String route) throws IOException {

        String responseBody = "";
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(address + route)).timeout(Duration.of(5, SECONDS)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            responseBody = response.body();
        } catch (HttpTimeoutException  | URISyntaxException | InterruptedException e){
            e.printStackTrace();
            Thread.currentThread().interrupt();
            // Coordinator no longer available
        }

        return responseBody;
    }

    public int putRESTRessource(String address, String route, String ressource) throws IOException {

        int statusCode = -1;

        try {
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(address + route)).
                    headers("Content-Type", "application/json", "accept", "application/json").
                    timeout(Duration.of(1, SECONDS)).
                    PUT(HttpRequest.BodyPublishers.ofString(ressource)).build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
            statusCode = response.statusCode();

        } catch (HttpTimeoutException  | URISyntaxException e){
            // Coordinator no longer available
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
        return statusCode;

    }

}
