package com.thewalkingschoolbus.thewalkingschoolbus.api_binding;

import com.google.gson.Gson;
import com.thewalkingschoolbus.thewalkingschoolbus.exceptions.ApiException;
import com.thewalkingschoolbus.thewalkingschoolbus.models.Group;
import com.thewalkingschoolbus.thewalkingschoolbus.models.Message;
import com.thewalkingschoolbus.thewalkingschoolbus.models.PermissionRequest;
import com.thewalkingschoolbus.thewalkingschoolbus.models.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.ServerManager.BASE_URL;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.ServerManager.httpRequestGet;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.ServerManager.httpRequestPost;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.ServerManager.readJsonIntoString;

/*
    api binding for permission

    include api:
    getting type of request for user or group
    or with a certain permission status
    change permission status
 */

public class PermissionsApiBinding {



    private static String GET_ALL_PERMISSION_REQUESTS = "/permissions";
    private static String GET_PERMISSION_REQUESTS_FOR_USER = "/permissions?userId=%s";
    private static String GET_PERMISSION_REQUESTS_FOR_USER_WITH_CERTAIN_STATUS ="/permissions?userId=%s%statusForUser=%s";

    private static String GET_PERMISSION_REQUESTS_FOR_GROUP = "/permissions?groupId=%s";
    private static String GET_PERMISSION_REQUESTS_WITH_STATUS = "/permissions?status=%s";
    private static String GET_PERMISSION_REQUESTS_FOR_USER_IN_GROUP_WITH_CERTAIN_STATUS = " /permissions?status=%s&groupId=%s&userId=%s";

    private static String GET_PERMISSION_REQUEST_WITH_ID = "/permissions/%s";
    private static String POST_PERMISSION_CHANGE_WITH_ID = "/permissions/%s";

    // get all permission request every initated by any user
    // no parameter required
    public static PermissionRequest[] getAllPermissionRequests()throws Exception{
        String url = BASE_URL+ GET_ALL_PERMISSION_REQUESTS;
        HttpURLConnection connection = httpRequestGet(url,null);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        StringBuffer result = readJsonIntoString(connection);

         return new Gson().fromJson(result.toString(),PermissionRequest[].class);
    }

    // get permission requests for a specific user
    // required a user with id
    public static PermissionRequest[] getPermissionRequestsForUser(User user)throws Exception{
        String url = BASE_URL+ String.format(GET_PERMISSION_REQUESTS_FOR_USER,user.getId());
        HttpURLConnection connection = httpRequestGet(url,null);
        connection.setRequestProperty("JSON-DEPTH","1");

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        StringBuffer result = readJsonIntoString(connection);

        return new Gson().fromJson(result.toString(),PermissionRequest[].class);
    }

    // get permission requests for specific user with a statuts
    // required a user wiht id and a permission request class with status
    public static PermissionRequest[] getPermissionRequestsForUserWithCertainStatus(User user,
                                                                                    PermissionRequest pr)throws Exception{
        String url = BASE_URL+ String.format(GET_PERMISSION_REQUESTS_FOR_USER_WITH_CERTAIN_STATUS
                ,user.getId(),pr.getStatus().toString());
        HttpURLConnection connection = httpRequestGet(url,null);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        StringBuffer result = readJsonIntoString(connection);

        return new Gson().fromJson(result.toString(),PermissionRequest[].class);
    }

    // get permission requests for specific group
    // required group id
    public static PermissionRequest[] getPermissionRequestsForGroup(Group group)throws Exception{
        String url = BASE_URL+ String.format(GET_PERMISSION_REQUESTS_FOR_GROUP,group.getId());
        HttpURLConnection connection = httpRequestGet(url,null);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        StringBuffer result = readJsonIntoString(connection);

        return new Gson().fromJson(result.toString(),PermissionRequest[].class);
    }

    // get all the Permission Requests With Status (pending, deny, approve)
    // required a permissionRequest object with a status
    public static PermissionRequest[] getPermissionRequestsWithStatus(PermissionRequest pr)throws Exception{
        String url = BASE_URL+ String.format(GET_PERMISSION_REQUESTS_WITH_STATUS,pr.getStatus().toString());
        HttpURLConnection connection = httpRequestGet(url,null);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        StringBuffer result = readJsonIntoString(connection);

        return new Gson().fromJson(result.toString(),PermissionRequest[].class);
    }

    //
    public static PermissionRequest[] getPermissionRequestsForUserInGroupWithCertainStatus(User user, Group group,
                                                                                           PermissionRequest pr)throws Exception{
        String url = BASE_URL+ String.format(GET_PERMISSION_REQUESTS_FOR_USER_IN_GROUP_WITH_CERTAIN_STATUS
                ,pr.getStatus().toString(),group.getId(),user.getId());
        HttpURLConnection connection = httpRequestGet(url,null);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        StringBuffer result = readJsonIntoString(connection);

        return new Gson().fromJson(result.toString(),PermissionRequest[].class);
    }

    public static PermissionRequest getPermissionRequestsWithID(PermissionRequest pr)throws Exception{
        String url = BASE_URL+ String.format(GET_PERMISSION_REQUEST_WITH_ID,pr.getId());
        HttpURLConnection connection = httpRequestGet(url,null);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        StringBuffer result = readJsonIntoString(connection);

        return new Gson().fromJson(result.toString(),PermissionRequest.class);
    }

    public static PermissionRequest postPermissionRequestsChangeWithId(PermissionRequest pr)throws Exception{
        String url = BASE_URL+ String.format(POST_PERMISSION_CHANGE_WITH_ID,pr.getId());

        String str =  "\""+pr.getStatus().toString()+"\"";
        HttpURLConnection connection = httpRequestPost(url,null);

        PrintStream outStream = new PrintStream(connection.getOutputStream());
        outStream.println(str);
        outStream.close();

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        StringBuffer result = readJsonIntoString(connection);
        return new Gson().fromJson(result.toString(),PermissionRequest.class);
    }


}
