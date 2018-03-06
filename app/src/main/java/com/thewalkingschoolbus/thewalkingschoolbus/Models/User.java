package com.thewalkingschoolbus.thewalkingschoolbus.Models;

/**
 * Created by Jackyx on 2018-03-04.
 */

public class User {

    static private User instance;

    private String token;

    private String id;
    private String name;
    private String email;



    private User(){
        id = "";
        name = "";
        email = "";
        token = "";
    }

    public static User getInstance(){
        if(instance == null){
            instance = new User();
        }
        return instance;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
