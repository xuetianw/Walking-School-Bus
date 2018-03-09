package com.thewalkingschoolbus.thewalkingschoolbus.Interface;

/**
 * Created by Jackyx on 2018-03-05.
 */

public interface OnTaskComplete {
    public void onSuccess(Object result);
    public void onFailure(Exception e);
}
