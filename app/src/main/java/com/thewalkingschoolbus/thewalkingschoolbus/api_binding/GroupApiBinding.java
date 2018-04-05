package com.thewalkingschoolbus.thewalkingschoolbus.api_binding;

import android.util.Log;

import com.google.gson.Gson;
import com.thewalkingschoolbus.thewalkingschoolbus.exceptions.ApiException;
import com.thewalkingschoolbus.thewalkingschoolbus.models.Group;
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
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.ServerManager.requiresPermission;

public class GroupApiBinding {
    private static String LIST_GROUPS = "/groups";
    private static String CREATE_GROUP = "/groups";
    private static String GET_ONE_GROUP = "/groups/%s";
    private static String UPDATE_EXISTING_GROUP = "/groups/%s";
    private static String DELETE_GROUP = "/groups/%s";
    private static String GET_MEMBERS_OF_GROUP = "/groups/%s/memberUsers";
    private static String ADD_MEMBERS_TO_GROUP = "/groups/%s/memberUsers";
    private static String REMOVE_MEMBER_OF_GROUP = "/groups/%s/memberUsers/%s";


    // take mGroup as parameter
    // return null
    //      authentication problem...
    // return Array of group object
    public static Group[] listGroups()throws Exception{
        String url = BASE_URL+LIST_GROUPS;
        HttpURLConnection connection = httpRequestGet(url,null);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        StringBuffer response = readJsonIntoString(connection);
        Group [] groups = new Gson().fromJson(response.toString(),Group[].class);
        return groups;
    }

    // take mGroup as paramemter
    // return null
    //      group already exist
    // return group object if group is created
    public static Group createGroup(Group group)throws Exception{
        String url = BASE_URL+CREATE_GROUP;
        String string = new Gson().toJson(group);
        JSONObject jsonObject = new JSONObject(string);
        requiresPermission = true;
        HttpURLConnection connection = httpRequestPost(url,jsonObject);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        StringBuffer response = readJsonIntoString(connection);
        requiresPermission = false;
        group = new Gson().fromJson(response.toString(),Group.class);
        return group;
    }

    // take mGroup as parameter
    // return null
    //      group already exist
    // return group object if group is created
    public static Group createGroupWithDetail(Group group)throws Exception{
        String url = BASE_URL+CREATE_GROUP;
        String string = new Gson().toJson(group);
        JSONObject jsonObject = new JSONObject(string);
        HttpURLConnection connection = httpRequestPost(url,jsonObject);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        StringBuffer response = readJsonIntoString(connection);
        group = new Gson().fromJson(response.toString(),Group.class);
        return group;
    }

    // take mGroup as parameter and using its id
    // return null
    //      group id does not exist
    // return group object if group is found
    public static Group getOneGroup(Group group)throws Exception{
        String url = BASE_URL+String.format(GET_ONE_GROUP,group.getId());
        HttpURLConnection connection = httpRequestGet(url,null);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        StringBuffer response = readJsonIntoString(connection);
        Group mGroup = new Gson().fromJson(response.toString(),Group.class);
        return mGroup;
    }

    // take mGroup as parameter to be used as updated group info
    // return null
    //      group id not found, group with same description...
    // return new Group object is successfully updated
    public static Group updateExistingGroup(Group group)throws Exception{
        String url = BASE_URL+String.format(UPDATE_EXISTING_GROUP,group.getId());
        String string = new Gson().toJson(group);
        JSONObject jsonObject = new JSONObject(string);
        requiresPermission = true;
        HttpURLConnection connection = httpRequestPost(url, jsonObject);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        StringBuffer response = readJsonIntoString(connection);
        requiresPermission = false;
        group = new Gson().fromJson(response.toString(),Group.class);
        return group;
    }

    // take mGroup as the group wanted to be deleted
    // return null
    // return SUCCESSFUL
    public static String deleteGroup(Group group)throws Exception{
        String url = BASE_URL + String.format(DELETE_GROUP,group.getId());
        HttpURLConnection connection = httpRequestDelete(url);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        return SUCCESSFUL;
    }

    // take mGroup with id to retrieve info about group memebr
    // return null
    //      group not found
    // return array of User object for all the member in the group
    public static User[] getMembersOfGroup (Group group) throws Exception{
        String url = BASE_URL+ String.format(GET_MEMBERS_OF_GROUP,group.getId());
        HttpURLConnection connection = httpRequestGet(url,null);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        StringBuffer response = readJsonIntoString(connection);
        User[] listMembers = new Gson().fromJson(response.toString(),User[].class);
        return listMembers;
    }

    // take user with id and mGroup with id to identify the group wanted to add new member
    // return null
    //      user not found, grounp not found...
    // return array of user object as group member
    public static User[] addMemberToGroup(User user,Group group) throws Exception{
        String url = BASE_URL+ String.format(ADD_MEMBERS_TO_GROUP,group.getId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",user.getId());
        HttpURLConnection connection = httpRequestPost(url,jsonObject);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        StringBuffer response = readJsonIntoString(connection);
        User[] listMembers = new Gson().fromJson(response.toString(),User[].class);
        return listMembers;
    }

    // take user with id and group with id to identify the group and user
    // return null
    // return SUCCESSFUL if successfully removed
    public static String removeMemberOfGroup(User user, Group group)throws Exception{
        String url = BASE_URL+ String.format(REMOVE_MEMBER_OF_GROUP,group.getId(),user.getId());
        HttpURLConnection connection = httpRequestDelete(url);
        Log.i("TAG",url);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        return SUCCESSFUL;
    }
}
