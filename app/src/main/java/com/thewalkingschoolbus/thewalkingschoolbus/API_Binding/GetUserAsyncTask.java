package com.thewalkingschoolbus.thewalkingschoolbus.API_Binding;

import android.os.AsyncTask;

import com.thewalkingschoolbus.thewalkingschoolbus.Interface.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.Group;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.User;

public class GetUserAsyncTask extends AsyncTask<Void, Void, Object>{

    private OnTaskComplete mlistener;

    private functionType functionChoice;

    private Object returnObject;
    private User mainUser;
    private User interactUser;
    private String passwordEntered;
    private Exception mException;
    private Group mGroup;

    public GetUserAsyncTask(functionType functionType, User userA, User userB, Group group, String password, OnTaskComplete listener){
        functionChoice = functionType;
        mlistener = listener;
        mGroup = group;
        mainUser = userA;
        interactUser = userB;
        passwordEntered = password;
    }

    protected Object doInBackground(Void... urls){
        try {
            ServerManager server = new ServerManager();
            switch (functionChoice) {
                case LOGIN_REQUEST:
                    returnObject = server.loginRequest(mainUser,passwordEntered);
                    break;
                case CREATE_USER:
                    returnObject = server.createUser(mainUser,passwordEntered);
                    break;
                case ALL_USERS:
                    returnObject = server.getUsers(mainUser);
                    break;
                case SINGLE_USER:
                    returnObject = server.getSingleUser(mainUser);
                    break;
                case LIST_MONITORING:
                    returnObject = server.userMonitoring(mainUser);
                    break;
                case CREATE_MONITORING:
                    returnObject = server.createMonitoring(mainUser,interactUser);
                    break;
                case STOP_MONITORING:
                    returnObject = server.stopMonitoring(mainUser,interactUser);
                    break;
                case LIST_GROUPS:
                    returnObject = server.listGroups(mGroup);
                    break;
                case CREATE_GROUP:
                    returnObject = server.createGroup(mGroup);
                    break;
                case GET_ONE_GROUP:
                    returnObject = server.getOneGroup(mGroup);
                    break;
                case UPDATE_GROUP:
                    returnObject = server.updateExistingGroup(mGroup);
                    break;
                case DELETE_GROUP:
                    returnObject = server.deleteGroup(mGroup);
                default:
                    returnObject = "Function Not Found";
            }
            return returnObject;

        } catch (Exception e) {
            mException = e;
        }
        return "Function Not Found";
    }


    //protected String onProgressUpdate(Void... progress) {
    //}

    protected void onPostExecute(Object returnObject) {
        if (mlistener != null) {
            if (mException == null) {
                mlistener.onSuccess(returnObject);
            } else {
                mlistener.onFailure(mException);
            }
        }
    }
    public enum functionType {
        LOGIN_REQUEST, CREATE_USER, ALL_USERS, SINGLE_USER,
        LIST_MONITORING, CREATE_MONITORING, STOP_MONITORING,
        LIST_GROUPS,CREATE_GROUP,GET_ONE_GROUP,UPDATE_GROUP,
        DELETE_GROUP
    }

}
