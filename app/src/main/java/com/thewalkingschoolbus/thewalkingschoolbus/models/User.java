package com.thewalkingschoolbus.thewalkingschoolbus.models;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.util.List;

public class User implements Comparable<User> {

    private static String token = null;

    private static User loginUser;

    private String id;
    private String  name;
    private String email;
    private String password;
    private String birthYear;
    private String birthMonth;
    private String address;
    private String cellPhone;
    private String homePhone;
    private String grade;
    private String teacherName;
    private String emergencyContactInfo;
    private GpsLocation lastGpsLocation;

    private List<Message> unreadMessages;
    private List<Message> readMessages;

    private List<User> monitoredByUsers;
    private List<User> monitorsUsers;

    private List<Group> memberOfGroups;
    private List<Group> leadsGroups;

    // Gamification Support
    // - - - - - - - - - - - - - - - - - - - - - - - - - - -
    private Integer currentPoints;
    private Integer totalPointsEarned;
    private String customJson;

    private Customization customization;

    // Permissions
    // - - - - - - - - - - - - - - - - - - - - - - - - - - -
    private List<PermissionRequest> pendingPermissionRequests;

    private String href;

    public User(){
        id = null;
        name = null;
        email = null;
        password = null;
        birthYear = null;
        birthMonth = null;
        address = null;
        cellPhone = null;
        homePhone = null;
        grade = null;
        teacherName = null;
        emergencyContactInfo = null;
        lastGpsLocation = null;

        monitorsUsers = null;
        monitoredByUsers = null;

        memberOfGroups = null;
        leadsGroups = null;

        currentPoints = 0;
        totalPointsEarned = 0;
        customization = null;
    }

    public User (String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        password = null;
        birthYear = null;
        birthMonth = null;
        address = null;
        cellPhone = null;
        homePhone = null;
        grade = null;
        teacherName = null;
        emergencyContactInfo = null;
        lastGpsLocation = null;

        monitorsUsers = null;
        monitoredByUsers = null;

        memberOfGroups = null;
        leadsGroups = null;

        customization = new Customization();
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getBirthMonth() {
        return birthMonth;
    }

    public void setBirthMonth(String birthMonth) {
        this.birthMonth = birthMonth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getEmergencyContactInfo() {
        return emergencyContactInfo;
    }

    public void setEmergencyContactInfo(String emergencyContactInfo) {
        this.emergencyContactInfo = emergencyContactInfo;
    }

    public GpsLocation getLastGpsLocation() {
        return lastGpsLocation;
    }

    public void setLastGpsLocation(GpsLocation lastGpsLocation) {
        this.lastGpsLocation = lastGpsLocation;
    }

    public void setMonitoredByUsers(List<User> monitoredByUsers) {
        this.monitoredByUsers = monitoredByUsers;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public List<Message> getUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessages(List<Message> unreadMessages) {
        this.unreadMessages = unreadMessages;
    }

    public List<Message> getReadMessages() {
        return readMessages;
    }

    public void setReadMessages(List<Message> readMessages) {
        this.readMessages = readMessages;
    }

    public int getCurrentPoints() {
        if (currentPoints == null) {
            currentPoints = 0;
        }
        return currentPoints;
    }

    public boolean addPoints(int points) {
        if (currentPoints == null) {
            currentPoints = 0;
        }
        if (totalPointsEarned == null) {
            totalPointsEarned = 0;
        }
        if (currentPoints + points < 0) {
            return false;
        } else {
            if (points > 0) {
                totalPointsEarned += points;
            }
            currentPoints += points;
            return true;
        }
    }

    public int getTotalPointsEarned() {
        if (totalPointsEarned == null) {
            totalPointsEarned = 0;
        }
        return totalPointsEarned;
    }

    public String getCustomJson() {
        return customJson;
    }

    public void setCustomJson(String customJson) {
        this.customJson = customJson;
    }

    public List<PermissionRequest> getPendingPermissionRequests() {
        return pendingPermissionRequests;
    }

    public void setPendingPermissionRequests(List<PermissionRequest> pendingPermissionRequests) {
        this.pendingPermissionRequests = pendingPermissionRequests;
    }

    public void customJsonToJson(){
        String str = new Gson().toJson(customization);
        customJson = str;
    }

    public void customJsonFromJson(String str){
        customization = new Gson().fromJson(str, Customization.class);
    }


    public Customization getCustomization() {
        return customization;
    }

    public void setCustomization(Customization customization) {
        this.customization = customization;
    }

    public static String getToken() throws Exception{
        return token;
    }

    public static void setToken(String tokenReceive) {
        token = tokenReceive;
    }

    @Override
    public int compareTo(@NonNull User user) {
        if (totalPointsEarned == null){
            totalPointsEarned = 0;
        }

        return user.totalPointsEarned.compareTo(totalPointsEarned);
    }
}
