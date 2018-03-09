package com.thewalkingschoolbus.thewalkingschoolbus.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackyx on 2018-03-04.
 */

public class User {

    private static String token = null;

    private String id;
    private String name;
    private String email;

    private List<User> monitorsUsers;
    private List<User> monitoredByUsers;
    private List<User> allUsers;
    //private list<Group> Group = new ArrayList<>();

    public User(){
        id = null;
        name = null;
        email = null;
        monitorsUsers = new ArrayList<>();
        monitoredByUsers = new ArrayList<>();

    }

    public User (String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }



    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<User> getMonitoringUser(){
        return monitorsUsers;
    }

    public List<User> getMonitoringByUser(){
        return monitoredByUsers;
    }

    public List<User> getAllUsers(){
        return allUsers;
    }

    public void appendMonitoringByUser(int position,User user){
        monitoredByUsers.add(position,user);
    }

    public void appendMonitoringUser(int position,User user){
        monitorsUsers.add(position,user);
    }

    public void appendAllUsers(int position,User user){
        allUsers.add(position,user);
    }

    public void setId(String id) {
        this.id = id;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setEmail(String email) {
        this.email = email;
    }



    public static String getToken() throws Exception{
        return token;
    }

    public static void setToken(String tokenReceive) {
        token = tokenReceive;
    }
}
