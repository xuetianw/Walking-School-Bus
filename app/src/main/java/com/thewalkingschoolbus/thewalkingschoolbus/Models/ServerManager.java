package com.thewalkingschoolbus.thewalkingschoolbus.Models;

/**
 * Created by Jackyx on 2018-03-04.
 */
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;


public class ServerManager {

    private String GET = "GET";
    private String POST = "POST";
    private String DELETE = "DELETE";

    private String API_KEY = "BB390E20-F40E-40D1-BE2D-2F99AAF8E449";
    private String BASE_URL = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443";
    private String LOGIN = "/login";
    private String CREATE_USER = "/users/signup";
    private String LIST_USER = "/users";
    private String USER_MONITORING_LIST = "/users/";
    private String CREATE_MONITORING = "";
    private String STOP_MONITORING = "";

    private HttpsURLConnection httpRequest(String url,String requestMethod) throws Exception{
        URL obj = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();
        connection.setRequestMethod(requestMethod);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type","application/json");
        connection.setRequestProperty("apiKey",API_KEY);
        return connection;
    }

    private void sendJson(HttpsURLConnection connection,JSONObject jsonObject)throws Exception{
        PrintStream printStream = new PrintStream(connection.getOutputStream());
        printStream.println(jsonObject.toString());
        printStream.close();
    }



    public String loginRequest(String enteredEmail,String enteredPassword)throws Exception{
        String url = BASE_URL+LOGIN;
        HttpsURLConnection connection = httpRequest(url,GET);
        connection.setDoOutput(true);

        // create json file here
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email",enteredEmail);
        jsonObject.put("password",enteredPassword);

        sendJson(connection,jsonObject);

        int responseCode = connection.getResponseCode();
        if(responseCode != 200) {
            return "Login Unsuccessful";
        }
        // save token
        String token = connection.getResponseMessage();
        User user = User.getInstance();
        user.setToken(token);
        return "Login Successful";
    }

    public String createUser(String enteredEmail,String enteredPassword, String enteredName)throws Exception{

        String url = BASE_URL+CREATE_USER;
        HttpsURLConnection connection = httpRequest(url,POST);
        connection.setDoOutput(true);
        // send json file
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name",enteredName);
        jsonObject.put("email",enteredEmail);
        jsonObject.put("password",enteredPassword);

        sendJson(connection,jsonObject);

        if (connection.getResponseCode() != 201) {
            // failed
            return "Error creating user";
        }

        // read json file and save in User
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        StringBuffer response = new StringBuffer();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        User user = User.getInstance();
        JSONObject returnJson = new JSONObject(response.toString());
        user.setId(returnJson.getString("id"));
        user.setName(returnJson.getString("name"));
        user.setEmail(returnJson.getString("email"));

        return "Successfully created User";

    }

    public String getUsers()throws Exception {
        String url = BASE_URL+LIST_USER;
        HttpsURLConnection connection = httpRequest(url,GET);

        if (connection.getResponseCode() != 200) {
            // failed
            return null;
        }
        return null;
    }

    public String getSingleUser(String id) throws Exception {

        String url = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443/users/"+id;
        URL obj = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type","application/json");
        connection.setRequestProperty("apiKey",API_KEY);

        if (connection.getResponseCode() != 200) {
            // failed
            return null;
        }
        return null;
    }

    public String userMonitoring(String id) throws Exception {
        String url = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443/users/"+id+"/monitorsUsers";
        URL obj = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type","application/json");
        connection.setRequestProperty("apiKey",API_KEY);

        if (connection.getResponseCode() != 200) {
            // failed
            return null;
        }
        return null;
    }

    public String CreateMonitoring(String idForMonitoring, String idForMonitoringBy) throws Exception {
        String url = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443/users/"+idForMonitoring+"/monitorsUsers";
        URL obj = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type","application/json");
        connection.setRequestProperty("apiKey",API_KEY);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",idForMonitoringBy);

        PrintStream printStream = new PrintStream(connection.getOutputStream());
        printStream.println(jsonObject.toString());
        printStream.close();

        if (connection.getResponseCode() != 201) {
            // failed
            return null;
        }
        return null;
    }

    public String stopMonitoring (String idForMonitoring, String idForMonitoringBy)throws Exception{
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
            return null;
        }
        return null;
    }

}
