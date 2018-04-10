package com.thewalkingschoolbus.thewalkingschoolbus.interfaces;

/*
    interface that is used in aysnctask to determine if a task is complete in the separate thread
 */

public interface OnTaskComplete {
    public void onSuccess(Object result);
    public void onFailure(Exception e);
}
