package com.thewalkingschoolbus.thewalkingschoolbus.api_binding;

import com.thewalkingschoolbus.thewalkingschoolbus.models.User;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerManager {

    public static boolean requiresPermission = false;

    private static String GET = "GET";
    private static String POST = "POST";
    private static String DELETE = "DELETE";

    private static String API_KEY = "BB390E20-F40E-40D1-BE2D-2F99AAF8E449"; //api key for flame group
    // Debug: Proxy server for plaintext debugging purpose
     //public static String BASE_URL = "http://walkgroup.api.tabjy.com/https://cmpt276-1177-bf.cmpt.sfu.ca:8443";
    public static String BASE_URL="https://cmpt276-1177-bf.cmpt.sfu.ca:8443";

    public static String SUCCESSFUL = "SUCCESSFUL";

    public enum PermissionStatus {
        PENDING,
        APPROVED,
        DENIED
    }

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

        if(requiresPermission == true){
            connection.setRequestProperty("permissions-enabled","true");
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
