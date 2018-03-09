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
                case LIST_USERS:
                    returnObject = server.listUsers(mainUser);
                    break;
                case GET_SINGLE_USER:
                    returnObject = server.getSingleUser(mainUser);
                    break;
                case USR_MONITORING_LIST:
                    returnObject = server.userMonitoringList(mainUser);
                    break;
                case CREATE_MONITORING:
                    returnObject = server.createMonitoring(mainUser,interactUser);
                    break;
                case DELETE_MONITORING:
                    returnObject = server.deleteMonitoring(mainUser,interactUser);
                    break;
                case LIST_GROUPS:
                    returnObject = server.listGroups();
                    break;
                case CREATE_GROUP:
                    returnObject = server.createGroup(mGroup);
                    break;
                case GET_ONE_GROUP:
                    returnObject = server.getOneGroup(mGroup);
                    break;
                case UPDATE_EXISTING_GROUP:
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
        LOGIN_REQUEST, CREATE_USER, LIST_USERS, GET_SINGLE_USER,
        USR_MONITORING_LIST, CREATE_MONITORING, DELETE_MONITORING,
        LIST_GROUPS,CREATE_GROUP,GET_ONE_GROUP,UPDATE_EXISTING_GROUP,
        DELETE_GROUP
    }

}
