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

    private List<User> monitoringUser;
    private List<User> monitoringByUser;
    // private list<Group> Group = new ArrayList<>();

    public User(){
        id = null;
        name = null;
        email = null;
        monitoringUser = new ArrayList<>();
        monitoringByUser = new ArrayList<>();
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
        return monitoringUser;
    }

    public void appendMonitoringUser(User user){
        monitoringUser.add(user);
    }

    public List<User> getMonitoringByUser(){
        return monitoringByUser;
    }

    public void appendMonitoringByUser(int position,User user){
        monitoringByUser.add(position,user);
    }

    public void appendMonitoringUser(int position,User user){
        monitoringUser.add(position,user);

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

    public static void setToken(String tokenRecived) {
        token = tokenRecived;
    }
}
