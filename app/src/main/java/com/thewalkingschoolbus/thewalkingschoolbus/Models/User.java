package com.thewalkingschoolbus.thewalkingschoolbus.Models;

//import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackyx on 2018-03-04.
 */


public class User {

    private static String token = null;

    private static User loginUser;

    private String id;
    private String name;
    private String email;

    private List<User> monitoredByUsers;
    private List<User> monitorsUsers;

    private List<Group> memberOfGroups;
    private List<Group> leadsGroups;

    private String href;

    public User(){
        id = null;
        name = null;
        email = null;
        monitorsUsers = null;
        monitoredByUsers = null;
        memberOfGroups = null;
    }

    public User (String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }



    public static User getLoginUser() {
        return loginUser;
    }

    public static void setLoginUser(User loginUser) {
        User.loginUser = loginUser;
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

    public List<User> getMonitorsUsers() {
        return monitorsUsers;
    }

    public List<User> getMonitoredByUsers() {
        return monitoredByUsers;
    }

    public List<Group> getMemberOfGroups() {
        return memberOfGroups;
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

    public void setMonitorsUsers(List<User> monitorsUsers) {
        this.monitorsUsers = monitorsUsers;
    }

    public void setMemberOfGroups(List<Group> memberOfGroups) {
        this.memberOfGroups = memberOfGroups;
    }

    public List<Group> getLeadsGroups() {
        return leadsGroups;
    }

    public void setLeadsGroups(List<Group> leadsGroups) {
        this.leadsGroups = leadsGroups;
    }

    public static String getToken() throws Exception{
        return token;
    }

    public static void setToken(String tokenReceive) {
        token = tokenReceive;
    }
}
