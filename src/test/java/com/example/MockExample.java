package com.example;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class MockExample {
    public static void main(String[] args) {
        // Start WireMock server
        WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(8080));
        wireMockServer.start();

        // Set up a stub
        configureFor("localhost", 8080);
        stubFor(get(urlEqualTo("/hello"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody("Hello, WireMock!")));

        // Make a request to the mocked API
        try {
            String response = makeHttpRequest("http://localhost:8080/hello");
            System.out.println("Response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Stop WireMock server
        wireMockServer.stop();
    }

    private static String makeHttpRequest(String url) throws Exception {
        java.net.HttpURLConnection connection = (java.net.HttpURLConnection) new java.net.URL(url).openConnection();
        connection.setRequestMethod("GET");
        java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
