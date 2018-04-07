package com.thewalkingschoolbus.thewalkingschoolbus.api_binding;

import com.google.gson.Gson;
import com.thewalkingschoolbus.thewalkingschoolbus.exceptions.ApiException;
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
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.UserApiBinding.getUserByEmail;

public class MonitoringApiBinding {
    private static String USER_MONITORING_LIST = "/users/%s/monitorsUsers";
    private static String USER_MONITORING_BY_LIST = "/users/%s/monitoredByUsers";
    private static String CREATE_MONITORING = "/users/%s/monitorsUsers";
    private static String DELETE_MONITORING = "/users/%s/monitorsUsers/%s";


    // take parentUser as the one that want to return list
    // throw exception : if server returns error
    //      id not found+ more
    // return a array of User if need can be convert to list
    public static User[] userMonitoringList(User user) throws Exception {

        if(user.getId() == null) {
            user = getUserByEmail(user);
        }

        String url = BASE_URL+String.format(USER_MONITORING_LIST, user.getId());
        HttpURLConnection connection = httpRequestGet(url,null);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        StringBuffer response = readJsonIntoString(connection);
        User[] listMonitoring = new Gson().fromJson(response.toString(), User[].class);
        return listMonitoring;

    }

    // take parentUser as parameter
    // return null if server returns error
    //      id not found+ more
    // return a array of User if need can be convert to list
    public static User[] userMonitoringByList(User user) throws Exception {
        if(user.getId() == null) {
            user = getUserByEmail(user);
        }
        String url = BASE_URL+String.format(USER_MONITORING_BY_LIST,user.getId());
        HttpURLConnection connection = httpRequestGet(url,null);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        StringBuffer response = readJsonIntoString(connection);
        User[] listMonitoringBy = new Gson().fromJson(response.toString(), User[].class);
        return listMonitoringBy;
    }

    // take both parentUser and childUser as parameters
    // return null if server return error
    //      parent not found, child not found, already exist(maybe)
    // return Arrary of User monitoring by parent User
    public static User[] createMonitoring(User parentUser, User childUser) throws Exception {
        if(parentUser.getId() == null) {
            parentUser = getUserByEmail(parentUser);
        }
        if(childUser.getId() == null) {
            childUser = getUserByEmail(childUser);
        }
        String url = BASE_URL+String.format(CREATE_MONITORING,parentUser.getId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",childUser.getId());
        HttpURLConnection connection = httpRequestPost(url,jsonObject);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        StringBuffer response = readJsonIntoString(connection);
        User [] listMonitoringByParentUser = new Gson().fromJson(response.toString(),User[].class);
        return listMonitoringByParentUser;
    }
    // take both parentUser and childUser as parameters
    // return null if server return error
    //      parent not found, child not found, child not in the list
    // return SUCCESSFUL if deleted
    public static String deleteMonitoring (User parentUser, User childUser)throws Exception{

        if(parentUser.getId() == null) {
            parentUser = getUserByEmail(parentUser);
        }

        if(childUser.getId() == null) {
            childUser = getUserByEmail(childUser);
        }

        String url = BASE_URL+ String.format(DELETE_MONITORING, parentUser.getId(), childUser.getId());
        HttpURLConnection connection = httpRequestDelete(url);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        return SUCCESSFUL;
    }
}
