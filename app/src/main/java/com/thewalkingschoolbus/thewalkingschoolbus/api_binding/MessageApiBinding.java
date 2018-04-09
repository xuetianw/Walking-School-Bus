package com.thewalkingschoolbus.thewalkingschoolbus.api_binding;

import com.google.gson.Gson;
import com.thewalkingschoolbus.thewalkingschoolbus.exceptions.ApiException;
import com.thewalkingschoolbus.thewalkingschoolbus.models.Group;
import com.thewalkingschoolbus.thewalkingschoolbus.models.Message;
import com.thewalkingschoolbus.thewalkingschoolbus.models.User;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.ServerManager.BASE_URL;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.ServerManager.SUCCESSFUL;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.ServerManager.httpRequestGet;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.ServerManager.httpRequestPost;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.ServerManager.readJsonIntoString;

/*
    this file contain all api binding that are related to messages
    including api:
    get messages : group user emergency (read/unread)
    send messages
    set messages as read or unread
 */

public class MessageApiBinding {

    private static String GET_ALL_MESSAGES = "/messages";
    private static String GET_ALL_EMERGENCY_MESSAGES = "/messages?is-emergency=true";
    private static String GET_MESSAGES_FOR_GROUP = "/messages?togroup=%s";
    private static String GET_EMERGENCY_MESSAGES_FOR_GROUP = "/messages?togroup=%s&is-emergency=true";
    private static String GET_MESSAGES_FOR_USER = "/messages?foruser=%s";
    private static String GET_UNREAD_MESSAGES_FOR_USER = "/messages?foruser=%s&status=unread";
    private static String GET_READ_MESSAGES_FOR_USER = "/messages?foruser=%s&status=read";
    private static String GET_UNREAD_EMERGENCY_MESSAGES_FOR_USER = "/messages?foruser=%s&status=unread&is-emergency=true";

    private static String POST_MESSAGE_TO_GROUP = "/messages/togroup/%s";
    private static String POST_MESSAGE_TO_PARENTS = "/messages/toparentsof/%s";
    private static String GET_ONE_MESSAGE = "/messages/%s";
    private static String SET_MESSAGE_AS_READ_OR_UNREAD =  "/messages/%s/readby/%s";


    public static Message[] getAllMessages()throws Exception{
        String url = BASE_URL+ GET_ALL_MESSAGES;
        HttpURLConnection connection = httpRequestGet(url,null);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }
        StringBuffer result = readJsonIntoString(connection);
        Message[] messages = new Gson().fromJson(result.toString(),Message[].class);
        return messages;
    }

    public static Message[] getAllEmergencyMessages()throws Exception{
        String url = BASE_URL+ GET_ALL_EMERGENCY_MESSAGES;
        HttpURLConnection connection = httpRequestGet(url,null);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }
        StringBuffer result = readJsonIntoString(connection);
        Message[] messages = new Gson().fromJson(result.toString(),Message[].class);
        return messages;
    }


    public static Message[] getMessagesForGroup(Group group)throws Exception{
        String url = BASE_URL+ String.format(GET_MESSAGES_FOR_GROUP,group.getId());
        HttpURLConnection connection = httpRequestGet(url,null);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }
        StringBuffer result = readJsonIntoString(connection);
        Message[] messages = new Gson().fromJson(result.toString(),Message[].class);
        return messages;
    }

    public static Message[] getEmergencyMessagesForGroup(Group group)throws Exception{
        String url = BASE_URL+ String.format(GET_EMERGENCY_MESSAGES_FOR_GROUP,group.getId());
        HttpURLConnection connection = httpRequestGet(url,null);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }
        StringBuffer result = readJsonIntoString(connection);
        Message[] messages = new Gson().fromJson(result.toString(),Message[].class);
        return messages;
    }

    public static Message[] getMessagesForUser(User user)throws Exception{
        String url = BASE_URL+ String.format(GET_MESSAGES_FOR_USER,user.getId());
        HttpURLConnection connection = httpRequestGet(url,null);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }
        StringBuffer result = readJsonIntoString(connection);
        Message[] messages = new Gson().fromJson(result.toString(),Message[].class);
        return messages;
    }


    public static Message[] getUnreadMessagesForUser(User user)throws Exception{
        String url = BASE_URL+ String.format(GET_UNREAD_MESSAGES_FOR_USER,user.getId());
        HttpURLConnection connection = httpRequestGet(url,null);
        connection.setRequestProperty("JSON-DEPTH","1");

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }
        StringBuffer result = readJsonIntoString(connection);
        Message[] messages = new Gson().fromJson(result.toString(),Message[].class);
        return messages;
    }

    public static Message[] getReadMessagesForUser(User user)throws Exception{
        String url = BASE_URL+ String.format(GET_READ_MESSAGES_FOR_USER,user.getId());
        HttpURLConnection connection = httpRequestGet(url,null);
        connection.setRequestProperty("JSON-DEPTH","1");

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }
        StringBuffer result = readJsonIntoString(connection);
        Message[] messages = new Gson().fromJson(result.toString(),Message[].class);
        return messages;
    }

    public static Message[] getUnreadEmergencyMessagesForUser(User user)throws Exception{
        String url = BASE_URL+ String.format(GET_UNREAD_EMERGENCY_MESSAGES_FOR_USER,user.getId());
        HttpURLConnection connection = httpRequestGet(url,null);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }
        StringBuffer result = readJsonIntoString(connection);
        Message[] messages = new Gson().fromJson(result.toString(),Message[].class);
        return messages;
    }

    public static Message postMessageToGroup (Group group, Message message)throws Exception{
        String url = BASE_URL+ String.format(POST_MESSAGE_TO_GROUP,group.getId());
        String str = new Gson().toJson(message);
        JSONObject jsonObject = new JSONObject(str);
        HttpURLConnection connection = httpRequestPost(url,jsonObject);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        StringBuffer result = readJsonIntoString(connection);
        Message messages = new Gson().fromJson(result.toString(),Message.class);
        return messages;
    }

    public static Message postMessageToParents(User user,Message message)throws Exception{
        String url = BASE_URL + String.format(POST_MESSAGE_TO_PARENTS,user.getId());
        String str = new Gson().toJson(message);
        JSONObject jsonObject = new JSONObject(str);
        HttpURLConnection connection = httpRequestPost(url,jsonObject);

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        StringBuffer result = readJsonIntoString(connection);
        Message messages = new Gson().fromJson(result.toString(),Message.class);
        return messages;
    }

    public static Message getOneMessage(Message message)throws Exception{
        String url = BASE_URL + String.format(GET_ONE_MESSAGE,message.getId());
        HttpURLConnection connection = httpRequestGet(url,null);
        //connection.setRequestProperty("JSON-DEPTH","1");

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        StringBuffer result = readJsonIntoString(connection);
        Message messages = new Gson().fromJson(result.toString(),Message.class);
        return messages;
    }

    public static String setMessageAsReadOrUnread(User user, Message message)throws Exception{
        String url = BASE_URL + String.format(SET_MESSAGE_AS_READ_OR_UNREAD,message.getId(),user.getId());
        String str = String.valueOf(message.isMessageRead());
        HttpURLConnection connection = httpRequestPost(url,null);

        PrintStream outStream = new PrintStream(connection.getOutputStream());
        outStream.println(str);
        outStream.close();

        if (connection.getResponseCode() >= 400) {
            // failed
            BufferedReader error = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            throw new Gson().fromJson(error, ApiException.class);
        }

        return SUCCESSFUL;
    }

}
