package com.tarum.net.http;

import com.tarum.app.Application;
import com.tarum.util.GlueList;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WebClient {

    private Application application;
    private HttpClient client;
    private HttpRequest request;
    private HttpResponse<String> response;

    public WebClient(Application application){
        this.application = application;
    }

    public GlueList<String> sendRequest (String webPageUrl){
        HttpURLConnection urlConnection;

        try {
            urlConnection = (HttpURLConnection) new URL(webPageUrl).openConnection();
            urlConnection.setRequestMethod("GET");

            InputStream inputStream = urlConnection.getInputStream();
            int contentLength = urlConnection.getContentLength();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

//    public String sendRequest (String webPageUrl){
//        GlueList<String> webPageContent = new GlueList<>();
//
//        this.client = HttpClient.newHttpClient();
//        this.request = HttpRequest.newBuilder()
//                .uri(URI.create(webPageURL))
//                .GET() // GET is default
//                .build();
//
//        try {
//            response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        return response.body();
//    }

}
