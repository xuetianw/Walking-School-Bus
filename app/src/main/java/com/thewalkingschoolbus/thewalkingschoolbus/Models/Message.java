package com.thewalkingschoolbus.thewalkingschoolbus.Models;

/**
 * Created by Jackyx on 2018-03-19.
 */

public class Message {
    private String id;
    private String timestamp;
    private String text;
    private User fromUser;
    private Group toGroup;
    private boolean emergency;
    private String href;

    public Message(){
        id = null;
        timestamp = null;
        text = null;
        fromUser = null;
        toGroup = null;
        emergency = false;
        href = null;
    }

    public Message(String mText, boolean mEmergency){
        text = mText;
        emergency = mEmergency;

        id = null;
        timestamp = null;
        fromUser = null;
        toGroup = null;
        href = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public Group getToGroup() {
        return toGroup;
    }

    public void setToGroup(Group toGroup) {
        this.toGroup = toGroup;
    }

    public boolean isEmergency() {
        return emergency;
    }

    public void setEmergency(boolean emergency) {
        this.emergency = emergency;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
