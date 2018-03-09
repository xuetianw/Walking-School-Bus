package com.thewalkingschoolbus.thewalkingschoolbus.Api_Binding;

import android.os.AsyncTask;

import com.thewalkingschoolbus.thewalkingschoolbus.Interface.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.User;

public class GetUserAsyncTask extends AsyncTask<Void, Void, String>{

    private OnTaskComplete mlistener;

    private functionType functionChoice;

    private String returnMessage;
    private User mainUser;
    private User interactUser;
    private String passwordEntered;

    private Exception mException;


    public GetUserAsyncTask(functionType functionType, User userA, User userB,String password,OnTaskComplete listener){
        functionChoice = functionType;
        mlistener= listener;
        mainUser = userA;
        interactUser = userB;
        passwordEntered = password;
    }

    protected String doInBackground(Void... urls){
        try {
            ServerManager server = new ServerManager();
            switch (functionChoice) {
                case LOGIN_REQUEST:
                    returnMessage = server.loginRequest(mainUser,passwordEntered);
                    break;
                case CREATE_USER:
                    returnMessage = server.createUser(mainUser,passwordEntered);
                    break;
                case ALL_USERS:
                    returnMessage = server.getUsers(mainUser);
                    break;
                case SINGLE_USER:
                    returnMessage = server.getSingleUser(mainUser);
                    break;
                case LIST_MONITORING:
                    returnMessage = server.userMonitoring(mainUser);
                    break;
                case CREATE_MONITORING:
                    returnMessage = server.createMonitoring(mainUser,interactUser);
                    break;
                case STOP_MONITORING:
                    returnMessage = server.stopMonitoring(mainUser,interactUser);
                    break;
                default:
                    returnMessage = "Function Not Found";
            }
            return returnMessage;

        } catch (Exception e) {
            mException = e;
        }
        return "Function Not Found";
    }


    //protected String onProgressUpdate(Void... progress) {
    //}

    protected void onPostExecute(String returnMessage) {
        if (mlistener != null) {
            if (mException == null) {
                mlistener.onSuccess(returnMessage);
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
