package com.thewalkingschoolbus.thewalkingschoolbus.api_binding;

import android.util.Log;

import com.google.gson.Gson;
import com.thewalkingschoolbus.thewalkingschoolbus.exceptions.ApiException;
import com.thewalkingschoolbus.thewalkingschoolbus.models.GpsLocation;
import com.thewalkingschoolbus.thewalkingschoolbus.models.User;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.ServerManager.BASE_URL;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.ServerManager.SUCCESSFUL;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.ServerManager.httpRequestDelete;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.ServerManager.httpRequestGet;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.ServerManager.httpRequestPost;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.ServerManager.readJsonIntoString;

public class UserApiBinding {

    private static String LOGIN = "/login";
    private static String CREATE_USER = "/users/signup";
    private static String LIST_USERS = "/users";
    private static String GET_USER_BY_ID = "/users/%s";
    private static String GET_USER_BY_EMAIL = "/users/byEmail?email=%s";
    private static String DELETE_USER = "/users/%s";
    private static String EDIT_USER = "/users/%s";
    private static String GET_GPS_LOCATION = "/users/%s/lastGpsLocation";
    private static String POST_GET_LOCATION = "/users/%s/lastGpsLocation";

    public static String loginRequest(User user, String enteredPassword)throws Exception, ApiException {
        String url = BASE_URL+LOGIN;
        // create json file here
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email",user.getEmail());
        if(enteredPassword == null){
            jsonObject.put("password",user.getPassword());
        }else {
            jsonObject.put("password", enteredPassword);
        }

        HttpURLConnection connection = httpRequestGet(url,jsonObject);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }
        // save token
        String token = connection.getHeaderField("authorization");

        User.setToken("Bearer "+token);
        Log.i("TAG",User.getToken());
        return SUCCESSFUL;
    }

    // take parentUser and enteredPassword ass parameters
    // return null if server returns error:
    //      user already exist ...
    // return user object for with server generated ID
    public static User createUser(User user)throws Exception, ApiException{

        String url = BASE_URL+CREATE_USER;

        String jsonFile = new Gson().toJson(user);
        JSONObject jsonObject = new JSONObject(jsonFile);

        HttpURLConnection connection = httpRequestPost(url,jsonObject);
        // send json file

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        // read json file and save in User
        StringBuffer response = readJsonIntoString(connection);
        user = new Gson().fromJson(response.toString(),User.class);
        return user;

    }

    // Does not need a parameters
    // return null if server returns error:
    //      problem with token, server connection issues
    // return array of user object on approve
    //      monitoring stuff, group stuff for every user
    //      in every user's list of monitoring and monitoring by only conntain user ID
    public static User[] listUsers()throws Exception {
        String url = BASE_URL+LIST_USERS;
        HttpURLConnection connection = httpRequestGet(url,null);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        StringBuffer response = readJsonIntoString(connection);
        User[] allUsers = new Gson().fromJson(response.toString(),User[].class);
        return allUsers;
    }

    // takes parentUser with id that want to be found
    // return null if server returns error:
    //      user not found, connection ...
    // return User Object in detail
    //      monitoring stuff, group stuff for this user
    //      in the list of monitoring user only contain IDs
    public static User getUserById(User user) throws Exception {

        String url = BASE_URL+String.format(GET_USER_BY_ID,user.getId());
        HttpURLConnection connection = httpRequestGet(url,null);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        StringBuffer response = readJsonIntoString(connection);
        JSONObject json = new JSONObject(response.toString());
        String str =(String) json.getString("customJson");

        user = new Gson().fromJson(response.toString(),User.class);
        user.customJsonFromJson(str);

        return user;
    }

    // takes parentUser with email that want to be found
    // return null if server returns error:
    //      user not found, connection ...
    // return User Object in detail
    //      monitoring stuff, group stuff for this user
    //      in the list of monitoring user only contain IDs
    public static User getUserByEmail(User user) throws Exception{
        String url = BASE_URL + String.format(GET_USER_BY_EMAIL,user.getEmail());
        HttpURLConnection connection = httpRequestGet(url,null);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        StringBuffer response = readJsonIntoString(connection);
        JSONObject json = new JSONObject(response.toString());
        String str =(String) json.getString("customJson");
        user = new Gson().fromJson(response.toString(),User.class);
        user.customJsonFromJson(str);
        return user;
    }

    // delete user with id
    // user parentUser as the user class and use their id to delete

    public static String deleteUser(User user) throws Exception{
        String url = BASE_URL + String.format(DELETE_USER,user.getId());
        HttpURLConnection connection = httpRequestDelete(url);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        return SUCCESSFUL;
    }

    // find user using ID and update all user info
    // need to have all the info not just the updated ones
    // throw exception :
    //  user must have unique email addresses. Trying to change the current user to an email
    //  address which is already in use by another user will generate an error.
    public static String editUser(User user)throws Exception{
        String url = BASE_URL + String.format(EDIT_USER,user.getId());
        user.customJsonToJson();
        String jsonFile = new Gson().toJson(user);
        JSONObject jsonObject = new JSONObject(jsonFile);
        HttpURLConnection connection = httpRequestPost(url,jsonObject);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        return SUCCESSFUL;
    }


    public static GpsLocation getGpsLocation(User user)throws Exception{
        String url = BASE_URL + String.format(GET_GPS_LOCATION,user.getId());
        HttpURLConnection connection = httpRequestGet(url,null);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        StringBuffer response = readJsonIntoString(connection);
        return new Gson().fromJson(response.toString(), GpsLocation.class);
    }

    // The time format is sensitive to capitalization of the values. If the values are not correctly
    //capitalized, it returns an HTTP 400 status with no error
    public static GpsLocation postGpsLocation(User user)throws Exception{
        String url = BASE_URL + String.format(POST_GET_LOCATION,user.getId());
        String jsonFile = new Gson().toJson(user.getLastGpsLocation());
        JSONObject jsonObject = new JSONObject(jsonFile);
        HttpURLConnection connection = httpRequestPost(url,jsonObject);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        StringBuffer response = readJsonIntoString(connection);
        return new Gson().fromJson(response.toString(), GpsLocation.class);

    }
}
