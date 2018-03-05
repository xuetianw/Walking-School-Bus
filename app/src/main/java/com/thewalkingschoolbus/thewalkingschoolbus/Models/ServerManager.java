package com.thewalkingschoolbus.thewalkingschoolbus.Models;

/**
 * Created by Jackyx on 2018-03-04.
 */
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;


public class ServerManager {

    private String API_KEY = "BB390E20-F40E-40D1-BE2D-2F99AAF8E449";

    public String loginRequest(String enteredEmail,String enteredPassword)throws Exception{

        String url = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443/login";
        URL obj = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type","application/json");
        connection.setRequestProperty("apiKey",API_KEY);
        // create json file here
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email",enteredEmail);
        jsonObject.put("password",enteredPassword);

        OutputStream os = connection.getOutputStream();
        os.write(jsonObject.toString().getBytes());
        os.flush();

        int responseCode = connection.getResponseCode();
        if(responseCode != 200) {
            // failed
            return null;
        }
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            return null;
    }

    public String createUser(String enteredEmail,String enteredPassword, String enteredName)throws Exception{

        String url = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443/users/signup";
        URL obj = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("apiKey",API_KEY);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name",enteredName);
        jsonObject.put("email",enteredEmail);
        jsonObject.put("password",enteredPassword);

        OutputStream os = connection.getOutputStream();
        os.write(jsonObject.toString().getBytes());
        os.flush();

        if (connection.getResponseCode() != 201) {
            // failed
            return null;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(
                (connection.getInputStream())));

        String output;
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            System.out.println(output);
        }
        return null;

    }
    public void getUsers()throws Exception {
        String url = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443/users";
        URL obj = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type","application/json");
        connection.setRequestProperty("apiKey",API_KEY);

        if (connection.getResponseCode() != 200) {
            // failed
            return;
        }

    }

    public void getSingleUser(String id) throws Exception {

        String url = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443/users/"+id;
        URL obj = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type","application/json");
        connection.setRequestProperty("apiKey",API_KEY);

        if (connection.getResponseCode() != 200) {
            // failed
            return;
        }

    }

    public void userMonitoring(String id) throws Exception {
        String url = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443/users/"+id+"/monitorsUsers";
        URL obj = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type","application/json");
        connection.setRequestProperty("apiKey",API_KEY);

        if (connection.getResponseCode() != 200) {
            // failed
            return;
        }
    }

    public void CreateMonitoring(String idForMonitoring, String idForMonitoringBy) throws Exception {
        String url = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443/users/"+idForMonitoring+"/monitorsUsers";
        URL obj = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type","application/json");
        connection.setRequestProperty("apiKey",API_KEY);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",idForMonitoringBy);

        OutputStream os = connection.getOutputStream();
        os.write(jsonObject.toString().getBytes());
        os.flush();

        if (connection.getResponseCode() != 201) {
            // failed
            return;
        }
    }

    public void stopMonitoring (String idForMonitoring, String idForMonitoringBy)throws Exception{
        String url = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443/users/"+idForMonitoring+
                "/monitorsUsers/"+
                idForMonitoringBy;
        URL obj = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Content-Type","application/json");
        connection.setRequestProperty("apiKey",API_KEY);

        if (connection.getResponseCode() != 204) {
            // failed
            return;
        }
    }

}
