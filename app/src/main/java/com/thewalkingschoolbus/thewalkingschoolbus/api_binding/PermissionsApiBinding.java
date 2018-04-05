package com.thewalkingschoolbus.thewalkingschoolbus.api_binding;

import android.util.Log;

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


public class PermissionsApiBinding {
    private static String GET_ALL_PERMISSION_REQUESTS = "/permissions";
    private static String GET_PERMISSION_REQUESTS_FOR_USER = "/permissions?userId=%s";
    private static String GET_PERMISSION_REQUESTS_FOR_USER_WITH_CERTAIN_STATUS ="/permissions?userId=%s%statusForUser=%s";

    private static String GET_PERMISSION_REQUESTS_FOR_GROUP = "/permissions?groupId=%s";
    private static String GET_PERMISSION_REQUESTS_WITH_STATUS = "/permissions?status=%s";
    private static String GET_PERMISSION_REQUESTS_FOR_USER_IN_GROUP_WITH_CERTAIN_STATUS = " /permissions?status=%s&groupId=%s&userId=%s";

    private static String GET_PERMISSION_REQUEST_WITH_ID = "/permissions/%s";
    private static String POST_PERMISSION_CHANGE_WITH_ID = "/permissions/%s";

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

    public static PermissionRequest[] getPermissionRequestsForUser(User user)throws Exception{
        String url = BASE_URL+ String.format(GET_PERMISSION_REQUESTS_FOR_USER,user.getId());
        HttpURLConnection connection = httpRequestGet(url,null);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        StringBuffer result = readJsonIntoString(connection);

        return new Gson().fromJson(result.toString(),PermissionRequest[].class);
    }

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
        connection.setDoOutput(true);

        PrintStream outStream = new PrintStream(connection.getOutputStream());
        outStream.println(str);
        outStream.close();

        if (connection.getResponseCode() >= 400) {
            Log.e("TAG","responseCode: "+connection.getResponseCode());
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        StringBuffer result = readJsonIntoString(connection);
        return new Gson().fromJson(result.toString(),PermissionRequest.class);
    }


}
