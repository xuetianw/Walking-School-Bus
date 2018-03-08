package com.thewalkingschoolbus.thewalkingschoolbus.Models;

/**
 * Created by Jackyx on 2018-03-04.
 */
import android.util.Log;

import com.thewalkingschoolbus.thewalkingschoolbus.MainActivity;

import org.json.JSONObject;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;

import static android.content.ContentValues.TAG;


public class ServerManager {

    private String GET = "GET";
    private String POST = "POST";
    private String DELETE = "DELETE";

    private String API_KEY = "BB390E20-F40E-40D1-BE2D-2F99AAF8E449"; //api key for flame group
    private String BASE_URL = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443";
    private String LOGIN = "/login";
    private String CREATE_USER = "/users/signup";
    private String LIST_USER = "/users";
    private String GET_USER = "/users/";
    private String USER_MONITORING_LIST = "/monitorsUsers";

    private String SUCCESS = "SUCCESSFUL";
    private String UNSUCCESSFUL = "UNSUCCESSFUL";

    private HttpsURLConnection httpRequest(String url,String requestMethod) throws Exception{
        URL obj = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod(requestMethod);
        connection.setRequestProperty("Content-Type","application/json");
        connection.setRequestProperty("apiKey",API_KEY);
        return connection;
    }

    private void sendJson(HttpsURLConnection connection,JSONObject jsonObject)throws Exception{
        PrintStream printStream = new PrintStream(connection.getOutputStream());
        printStream.println(jsonObject.toString());
        printStream.close();
    }

    private void authorizationWithOutBody(HttpsURLConnection connection, User user) throws Exception{
        connection.setRequestProperty("Bearer <token>",user.getToken());
        PrintStream printStream = new PrintStream(connection.getOutputStream());
        printStream.close();
    }

    private StringBuffer readJsonIntoString (HttpsURLConnection connection) throws Exception{
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        StringBuffer response = new StringBuffer();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response;
    }

    public String loginRequest(User user,String enteredPassword)throws Exception{
        String url = BASE_URL+LOGIN;
        HttpsURLConnection connection = httpRequest(url,GET);
        // create json file here
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email",user.getEmail());
        jsonObject.put("password",enteredPassword);

        sendJson(connection,jsonObject);

        int responseCode = connection.getResponseCode();
        Log.i(TAG, "loginRequest: "+responseCode );

        if(responseCode != 200) {
            return UNSUCCESSFUL;
        }
        // save token
        String token = connection.getResponseMessage();
        user.setToken(token);
        return SUCCESS;
    }

    public String createUser(User user,String enteredPassword)throws Exception{

        String url = BASE_URL+CREATE_USER;
        HttpsURLConnection connection = httpRequest(url,POST);
        // send json file
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name",user.getName());
        jsonObject.put("email",user.getEmail());
        jsonObject.put("password",enteredPassword);

        sendJson(connection,jsonObject);

        if (connection.getResponseCode() != 201) {
            // failed
            return UNSUCCESSFUL;
        }

        // read json file and save in User
        StringBuffer response = readJsonIntoString(connection);
        JSONObject returnJson = new JSONObject(response.toString());

        user.setId(returnJson.getString("id"));

        return SUCCESS;

    }

    public String getUsers(User user)throws Exception {
        String url = BASE_URL+LIST_USER;
        HttpsURLConnection connection = httpRequest(url,GET);
        authorizationWithOutBody(connection,user);

        if (connection.getResponseCode() != 200) {
            // failed
            return UNSUCCESSFUL;
        }



        return SUCCESS;
    }

    public String getSingleUser(User user) throws Exception {

        String url = BASE_URL+GET_USER+user.getId();
        HttpsURLConnection connection = httpRequest(url,GET);
        authorizationWithOutBody(connection,user);

        if (connection.getResponseCode() != 200) {
            // failed
            return UNSUCCESSFUL;
        }
        return SUCCESS;
    }

    public String userMonitoring(User user) throws Exception {
        String url = BASE_URL+GET_USER+user.getId()+USER_MONITORING_LIST;
        HttpsURLConnection connection = httpRequest(url,GET);
        authorizationWithOutBody(connection,user);

        if (connection.getResponseCode() != 200) {
            // failed
            return UNSUCCESSFUL;
        }
        return SUCCESS;
    }

    public String CreateMonitoring(User mainUser, User interactUser) throws Exception {
        String url = BASE_URL+GET_USER+mainUser.getId()+USER_MONITORING_LIST;
        HttpsURLConnection connection = httpRequest(url,POST);
        connection.setRequestProperty("Bearer <token>", mainUser.getToken());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",interactUser.getId());

        PrintStream printStream = new PrintStream(connection.getOutputStream());
        printStream.println(jsonObject.toString());
        printStream.close();

        if (connection.getResponseCode() != 201) {
            // failed
            return UNSUCCESSFUL;
        }


        return SUCCESS;
    }

    public String stopMonitoring (User mainUser, User interactUser)throws Exception{
        String url = BASE_URL+GET_USER+mainUser.getId()+
                USER_MONITORING_LIST+
                interactUser.getId();
        HttpsURLConnection connection = httpRequest(url,DELETE);
        authorizationWithOutBody(connection,mainUser);

        if (connection.getResponseCode() != 204) {
            // failed
            return UNSUCCESSFUL;
        }
        return SUCCESS;
    }

}
