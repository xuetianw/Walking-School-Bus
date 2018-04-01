package com.thewalkingschoolbus.thewalkingschoolbus.api_binding;

import android.util.Log;

import com.thewalkingschoolbus.thewalkingschoolbus.models.ApiException;
import com.thewalkingschoolbus.thewalkingschoolbus.models.GpsLocation;
import com.thewalkingschoolbus.thewalkingschoolbus.models.Group;
import com.thewalkingschoolbus.thewalkingschoolbus.models.Message;
import com.thewalkingschoolbus.thewalkingschoolbus.models.User;

import com.google.gson.Gson;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerManager {

    private static String GET = "GET";
    private static String POST = "POST";
    private static String DELETE = "DELETE";

    private static String API_KEY = "BB390E20-F40E-40D1-BE2D-2F99AAF8E449"; //api key for flame group
    // Debug: Proxy server for plaintext debugging purpose
    // private String BASE_URL = "http://walkgroup.api.tabjy.com/https://cmpt276-1177-bf.cmpt.sfu.ca:8443";
    public static String BASE_URL="https://cmpt276-1177-bf.cmpt.sfu.ca:8443";

    /*
    private String LOGIN = "/login";
    private String CREATE_USER = "/users/signup";
    private String LIST_USERS = "/users";
    private String GET_USER_BY_ID = "/users/%s";
    private String GET_USER_BY_EMAIL = "/users/byEmail?email=%s";
    private String DELETE_USER = "/users/%s";
    private String EDIT_USER = "/users/%s";
    private String GET_GPS_LOCATION = "/users/%s/lastGpsLocation";
    private String POST_GET_LOCATION = "/users/%s/lastGpsLocation";

    private String USER_MONITORING_LIST = "/users/%s/monitorsUsers";
    private String USER_MONITORING_BY_LIST = "/users/%s/monitoredByUsers";
    private String CREATE_MONITORING = "/users/%s/monitorsUsers";
    private String DELETE_MONITORING = "/users/%s/monitorsUsers/%s";

    private String LIST_GROUPS = "/groups";
    private String CREATE_GROUP = "/groups";
    private String GET_ONE_GROUP = "/groups/%s";
    private String UPDATE_EXISTING_GROUP = "/groups/%s";
    private String DELETE_GROUP = "/groups/%s";
    private String GET_MEMBERS_OF_GROUP = "/groups/%s/memberUsers";
    private String ADD_MEMBERS_TO_GROUP = "/groups/%s/memberUsers";
    private String REMOVE_MEMBER_OF_GROUP = "/groups/%s/memberUsers/%s";

    private String GET_ALL_MESSAGES = "/messages";
    private String GET_ALL_EMERGENCY_MESSAGES = "/messages?is-emergency=true";
    private String GET_MESSAGES_FOR_GROUP = "/messages?togroup=%s";
    private String GET_EMERGENCY_MESSAGES_FOR_GROUP = "/messages?togroup=%s&is-emergency=true";
    private String GET_MESSAGES_FOR_USER = "/messages?foruser=%s";
    private String GET_UNREAD_MESSAGES_FOR_USER = "/messages?foruser=%s&status=unread";
    private String GET_READ_MESSAGES_FOR_USER = "/messages?foruser=%s&status=read";
    private String GET_UNREAD_EMERGENCY_MESSAGES_FOR_USER = "/messages?foruser=%s&status=unread&is-emergency=true";

    private String POST_MESSAGE_TO_GROUP = "/messages/togroup/%s";
    private String POST_MESSAGE_TO_PARENTS = "/messages/toparentsof/%s";
    private String GET_ONE_MESSAGE = "/messages/%s";
    private String SET_MESSAGE_AS_READ_OR_UNREAD =  "/messages/%s/readby/%s";
*/

    public static String SUCCESSFUL = "SUCCESSFUL";

    // for any type of post request, this does the initial connection and sending json file
    // Such as: create User, create Group, create monitoring
    // return httpURLconnection which is used to getResponseCode from server side
    public static HttpURLConnection httpRequestPost(String url, JSONObject jsonObject)throws Exception{
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod(POST);
        connection.setRequestProperty("Content-Type","application/json");
        connection.setRequestProperty("apiKey",API_KEY);

        if(User.getToken()!= null){
            connection.setRequestProperty("Authorization", User.getToken());
        }

        if(jsonObject != null){
            connection.setDoOutput(true);
            PrintStream printStream = new PrintStream(connection.getOutputStream());
            printStream.println(jsonObject .toString());
            printStream.close();
        }
        return connection;
    }

    // for any type of Get request, this does the initial connection and sending json file(for login)
    // Such as: login User, list Group, list monitoring
    // return httpURLconnection which is used to getResponseCode from server side
    public static HttpURLConnection httpRequestGet(String url,JSONObject jsonObject)throws Exception{
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod(GET);
        connection.setRequestProperty("Content-Type","application/json");
        connection.setRequestProperty("apiKey",API_KEY);

        if(User.getToken()!= null){
            connection.setRequestProperty("Authorization", User.getToken());
        }

        if(jsonObject != null){
            connection.setDoOutput(true);
            PrintStream printStream = new PrintStream(connection.getOutputStream());
            printStream.println(jsonObject .toString());
            printStream.close();
        }

        return connection;
    }

    // for any type of Delete request, this does the initial connection
    // Such as: login User, list Group, list monitoring
    // return httpURLconnection which is used to getResponseCode from server side
    public static HttpURLConnection httpRequestDelete(String url)throws Exception{
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        //connection.setDoOutput(true);
        connection.setRequestMethod(DELETE);
        connection.setRequestProperty("Content-Type","application/json");
        connection.setRequestProperty("apiKey",API_KEY);

        if(User.getToken()!= null){
            connection.setRequestProperty("Authorization", User.getToken());
        }

        return connection;
    }

    // turns json file in to string.. as name suggest return a StringBuffer well organize
    // preparation to map Json file into each object
    public static StringBuffer readJsonIntoString (HttpURLConnection connection) throws Exception{
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
}
