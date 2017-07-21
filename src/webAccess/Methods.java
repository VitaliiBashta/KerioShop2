package webAccess;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static logic.Raynet.RAYNET_URL;

public class Methods {

    public static String sendGet(String url) {

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(RAYNET_URL + url);

        // add request header
        request.addHeader("X-Instance-Name", "zebra");
        request.addHeader("Authorization", "Basic dml0YWxpaS5iYXNodGFAemVicmEuY3o6VjE1OTZpdGFsaWk=");

        HttpResponse response;
        StringBuilder result = new StringBuilder();
        try {
            response = client.execute(request);
            BufferedReader rd;

            rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.println("\nSending 'GET' request to URL : " + url);
//        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());


        return result.toString();
    }

    public static String sendGet2(String url) throws IOException {

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        HttpResponse response;
        StringBuilder result = new StringBuilder();

        response = client.execute(request);
        BufferedReader rd;

        rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }


        return result.toString();
    }

    public static String sendPost(String url, List<NameValuePair> urlParameters) {

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(RAYNET_URL + url);

        // add request header
        request.addHeader("X-Instance-Name", "zebra");
        request.addHeader("Authorization", "Basic dml0YWxpaS5iYXNodGFAemVicmEuY3o6VjE1OTZpdGFsaWk=");


        HttpResponse response;
        StringBuilder result = new StringBuilder();
        try {
            if (urlParameters != null) request.setEntity(new UrlEncodedFormEntity(urlParameters));
            response = client.execute(request);
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    public static String sendPost2(String url, String urlParameters) {
        StringBuilder response = new StringBuilder();

        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            String inputLine;
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return response.toString();
    }

    public static String sendPut(String url, HttpEntity entity) {

        HttpClient client = HttpClientBuilder.create().build();
        HttpPut request = new HttpPut(RAYNET_URL + url);

        // add request header
        request.addHeader("X-Instance-Name", "zebra");
        request.addHeader("Authorization", "Basic dml0YWxpaS5iYXNodGFAemVicmEuY3o6VjE1OTZpdGFsaWk=");


        HttpResponse response;
        StringBuilder result = new StringBuilder();
        try {
            request.setEntity(entity);
            response = client.execute(request);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    public static void main(String[] args) throws Exception {
    }
}
