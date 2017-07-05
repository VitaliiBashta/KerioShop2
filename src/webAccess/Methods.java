package webAccess;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static logic.Raynet.RAYNET_URL;

public class Methods {

    public static String sendGet(String url) {

        HttpClient client = new DefaultHttpClient();
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


}
