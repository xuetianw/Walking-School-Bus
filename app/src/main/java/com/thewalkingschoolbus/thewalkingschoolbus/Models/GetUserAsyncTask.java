package com.thewalkingschoolbus.thewalkingschoolbus.Models;

import android.os.AsyncTask;

import com.thewalkingschoolbus.thewalkingschoolbus.Interface.OnTaskComplete;

public class GetUserAsyncTask extends AsyncTask<Void, Void, String>{

    private OnTaskComplete mlistener;
    private String returnMessage;
    private int functionNum;
    private User mainUser;
    private String userWithID;
    private String passwordEntered;

    //private OnErrorListener mOnErrorListener;
    //private OnSuccessListener mOnSuccessListener;

    private Exception mException;


    public GetUserAsyncTask(int task, String userTwo,String password,OnTaskComplete listener){
        mlistener= listener;
        functionNum = task;
        mainUser = User.getInstance();
        userWithID = userTwo;
        passwordEntered = password;
    }

    protected String doInBackground(Void... urls){
        try {
            ServerManager server = new ServerManager();
            switch (functionNum) {
                case 1:
                    returnMessage = server.loginRequest(mainUser.getEmail(),passwordEntered);
                    break;
                case 2:
                    returnMessage = server.createUser(mainUser.getEmail(),passwordEntered,mainUser.getName());
                    break;
                case 3:
                    returnMessage = server.getUsers();
                    break;
                case 4:
                    returnMessage = server.getSingleUser(userWithID);
                    break;
                case 5:
                    returnMessage = server.userMonitoring(userWithID);
                    break;
                case 6:
                    returnMessage = server.CreateMonitoring(mainUser.getId(),userWithID);
                    break;
                case 7:
                    returnMessage = server.stopMonitoring(mainUser.getId(),userWithID);
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

}
