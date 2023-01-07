package vsp.trongame.applicationstub.model.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RESTClient {

    public String getRESTRessource(String address, String route) throws IOException {
        URL url = new URL(address + route);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(1000);

        StringBuilder response = new StringBuilder("");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return new String(response);
    }

    public int putRESTRessource(String address, String route, byte[] ressource) throws IOException {

        URL url = new URL(address + route);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Accpet", "application/json");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setConnectTimeout(1000);
        connection.setDoOutput(true);
        connection.getOutputStream().write(ressource);
        connection.getOutputStream().close();

        int responseCode = connection.getResponseCode();

        connection.disconnect();

        return responseCode;
    }

}
