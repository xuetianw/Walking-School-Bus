package com.thewalkingschoolbus.thewalkingschoolbus.api_binding;

import android.util.Log;

import com.thewalkingschoolbus.thewalkingschoolbus.Models.Group;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.User;

import com.google.gson.Gson;

import org.json.JSONObject;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.util.Arrays;

import static android.content.ContentValues.TAG;


public class ServerManager {

    private String GET = "GET";
    private String POST = "POST";
    private String DELETE = "DELETE";

    private String API_KEY = "BB390E20-F40E-40D1-BE2D-2F99AAF8E449"; //api key for flame group
    private String BASE_URL = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443";
    private String LOGIN = "/login";
    private String CREATE_USER = "/users/signup";
    private String LIST_USERS = "/users";
    private String GET_SINGLE_USER = "/users/%s";
    private String USER_MONITORING_LIST = "/users/%s/monitorsUsers";
    private String CREATE_MONITORING = "/users/%s/monitorsUsers";
    private String DELETE_MONITORING = "/users/%s/monitorsUsers/%s";
    private String LIST_GROUPS = "/groups";
    private String CREATE_GROUP = "/groups";
    private String GET_ONE_GROUP = "/groups/%s";

    private String SUCCESS = "SUCCESSFUL";

    private HttpsURLConnection httpRequestPost(String url,JSONObject jsonObject)throws Exception{
        URL obj = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod(POST);
        connection.setRequestProperty("Content-Type","application/json");
        connection.setRequestProperty("apiKey",API_KEY);

        if(User.getToken()!= null){
            connection.setRequestProperty("Authorization", User.getToken());
        }

        if(jsonObject != null){
            PrintStream printStream = new PrintStream(connection.getOutputStream());
            printStream.println(jsonObject .toString());
            printStream.close();
        }
        return connection;
    }

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

    private void authorizationWithOutBody(HttpsURLConnection connection) throws Exception{
        connection.setRequestProperty("Authorization", User.getToken());
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

    public User loginRequest(User user,String enteredPassword)throws Exception{
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
            return null;
        }
        // save token
        String token = connection.getResponseMessage();
        Log.i(TAG, "token: "+responseCode );

        User.setToken("Bearer "+token);
        return user;
    }

    public User createUser(User user,String enteredPassword)throws Exception{

        String url = BASE_URL+CREATE_USER;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name",user.getName());
        jsonObject.put("email",user.getEmail());
        jsonObject.put("password",enteredPassword);

        HttpsURLConnection connection = httpRequestPost(url,jsonObject);
        // send json file

        if (connection.getResponseCode() != 201) {
            // failed
            return null;
        }

        // read json file and save in User
        StringBuffer response = readJsonIntoString(connection);
        user = new Gson().fromJson(response.toString(),User.class);
        return user;

    }

    public User[] listUsers(User user)throws Exception {
        String url = BASE_URL+LIST_USERS;
        HttpsURLConnection connection = httpRequest(url,GET);
        authorizationWithOutBody(connection);

        if (connection.getResponseCode() != 200) {
            // failed
            return null;
        }

        StringBuffer response = readJsonIntoString(connection);
        User[] allUsers = new Gson().fromJson(response.toString(),User[].class);
        return allUsers;
    }

    public User getSingleUser(User user) throws Exception {

        String url = BASE_URL+String.format(GET_SINGLE_USER,user.getId());
        HttpsURLConnection connection = httpRequest(url,GET);
        authorizationWithOutBody(connection);

        if (connection.getResponseCode() != 200) {
            // failed
            return null;
        }

        StringBuffer response = readJsonIntoString(connection);
        //JSONObject returnJson = new JSONObject(response.toString());
        //saveJsonArraysWithIds(returnJson,user);
        user = new Gson().fromJson(response.toString(),User.class);
        return user;
    }

    public User userMonitoringList(User user) throws Exception {
        String url = BASE_URL+String.format(USER_MONITORING_LIST,user.getId());
        HttpsURLConnection connection = httpRequest(url,GET);
        authorizationWithOutBody(connection);

        if (connection.getResponseCode() != 200) {
            // failed
            return null;
        }

        StringBuffer response = readJsonIntoString(connection);
        //JSONArray responseJson = new JSONArray(response.toString());
        //saveJsonArrays(responseJson,user,0);
        User[] listMonitoring = new Gson().fromJson(response.toString(), User[].class);
        user.setMonitorsUsers(Arrays.asList(listMonitoring));
        return user;
    }

    public User createMonitoring(User mainUser, User interactUser) throws Exception {
        String url = BASE_URL+String.format(CREATE_MONITORING,mainUser.getId());
        HttpsURLConnection connection = httpRequest(url,POST);
        connection.setRequestProperty("Authorization", User.getToken());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",interactUser.getId());

        sendJson(connection,jsonObject);

        if (connection.getResponseCode() != 201) {
            return null;
        }

        StringBuffer response = readJsonIntoString(connection);
        mainUser = new Gson().fromJson(response.toString(),User.class);
        return mainUser;
    }

    public String deleteMonitoring (User mainUser, User interactUser)throws Exception{
        String url = BASE_URL+ String.format(DELETE_MONITORING,mainUser.getId(),interactUser.getId());
        HttpsURLConnection connection = httpRequest(url,DELETE);
        authorizationWithOutBody(connection);

        if (connection.getResponseCode() != 204) {
            // failed
            return null;
        }
        return SUCCESS;
    }

    public Group[] listGroups()throws Exception{
        String url = BASE_URL+LIST_GROUPS;
        HttpsURLConnection connection = httpRequest(url,GET);
        authorizationWithOutBody(connection);

        if(connection.getResponseCode() != 200){
            return null;
        }
        StringBuffer response = readJsonIntoString(connection);
        Group [] groups = new Gson().fromJson(response.toString(),Group[].class);
        return groups;
    }

    public Group createGroup(Group group)throws Exception{
        String url = BASE_URL+CREATE_GROUP;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("groupDescription",group.getGroupDescription());
        HttpsURLConnection connection = httpRequestPost(url,jsonObject);

        if(connection.getResponseCode() != 200) {
            return null;
        }
        StringBuffer response = readJsonIntoString(connection);
        group = new Gson().fromJson(response.toString(),Group.class);
        return group;
    }

    public String getOneGroup(Group group)throws Exception{

        return null;
    }

    public String updateExistingGroup(Group group)throws Exception{
        return null;
    }

    public String deleteGroup(Group group)throws Exception{
        return null;
    }


}



/*
    garbage for now... i do hope this stay as garbage...
    private void saveJsonArraysWithIds(JSONObject returnJson,User user)throws Exception{
        JSONArray monitorsUsers = returnJson.getJSONArray("monitorsUsers");
        JSONArray monitoredByUsers = returnJson.getJSONArray("monitoredByUsers");
        // save monitors Users
        for (int i = 0;i < monitorsUsers.length(); i++){
            JSONObject tmpJsonObject = monitorsUsers.getJSONObject(i);
            User tmpUser = new User();
            tmpUser.setId(tmpJsonObject.getString("id"));
            user.appendMonitoringUser(i,tmpUser);
        }

        for (int i = 0;i < monitoredByUsers.length(); i++){
            JSONObject tmpJsonObject = monitoredByUsers.getJSONObject(i);
            User tmpUser = new User();
            tmpUser.setId(tmpJsonObject.getString("id"));
            user.appendMonitoringByUser(i,tmpUser);
        }
    }

    private void saveJsonArrays(JSONArray responseJson,User user,int flag)throws Exception{
        for (int i = 0; i < responseJson.length();i++){
            JSONObject tmpJsonObject = responseJson.getJSONObject(i);
            User tmpUser = new User();
            tmpUser.setId(tmpJsonObject.getString("id"));
            tmpUser.setName(tmpJsonObject.getString("name"));
            tmpUser.setId(tmpJsonObject.getString("email"));
            saveJsonArraysWithIds(tmpJsonObject,tmpUser);
            if(flag == 0) {
                user.appendMonitoringUser(i, tmpUser);
            }else if(flag == 1){
                user.appendAllUsers(i,tmpUser);
            }
        }
    }
    */