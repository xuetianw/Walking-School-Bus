package com.thewalkingschoolbus.thewalkingschoolbus.models;

/*
    this class is mainly use for the permission api binding which allow us to retrieve permission
    object from server and manipulate them
 */
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.ServerManager;

import java.util.Set;


public class PermissionRequest {
    protected Long id;
    protected String href;
    private String action;
    private ServerManager.PermissionStatus status;
    private User userA;
    private User userB;
    private Group groupG;
    private User requestingUser;
    private Set<Authorizor> authorizors;
    private String message;


    public static class Authorizor {
        private Long id;
        private Set<User> users;
        private ServerManager.PermissionStatus status;
        private User whoApprovedOrDenied;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Set<User> getUsers() {
            return users;
        }

        public void setUsers(Set<User> users) {
            this.users = users;
        }

        public ServerManager.PermissionStatus getStatus() {
            return status;
        }

        public void setStatus(ServerManager.PermissionStatus status) {
            this.status = status;
        }

        public User getWhoApprovedOrDenied() {
            return whoApprovedOrDenied;
        }

        public void setWhoApprovedOrDenied(User whoApprovedOrDenied) {
            this.whoApprovedOrDenied = whoApprovedOrDenied;
        }
    }

    public PermissionRequest(){
        id = null;
        href = null;
        action = null;
        status = null;
        userA = null;
        userB = null;
        groupG = null;
        requestingUser = null;
        authorizors = null;
        message = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public User getUserA() {
        return userA;
    }

    public void setUserA(User userA) {
        this.userA = userA;
    }

    public User getUserB() {
        return userB;
    }

    public void setUserB(User userB) {
        this.userB = userB;
    }

    public Group getGroupG() {
        return groupG;
    }

    public void setGroupG(Group groupG) {
        this.groupG = groupG;
    }

    public User getRequestingUser() {
        return requestingUser;
    }

    public void setRequestingUser(User requestingUser) {
        this.requestingUser = requestingUser;
    }

    public Set<Authorizor> getAuthorizors() {
        return authorizors;
    }

    public void setAuthorizors(Set<Authorizor> authorizors) {
        this.authorizors = authorizors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ServerManager.PermissionStatus getStatus() {
        return status;
    }

    public void setStatus(ServerManager.PermissionStatus status) {
        this.status = status;
    }

    public User getter(){
        return  userA;
    }
}
