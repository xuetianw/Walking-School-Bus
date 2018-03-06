package com.thewalkingschoolbus.thewalkingschoolbus.Models;

import android.os.AsyncTask;

public class GetUserAsyncTask extends AsyncTask<Void, Void, String>{

    private int functionNum;
    User mainUser;
    String userWithID;
    String passwordEntered;

    private OnErrorListener mOnErrorListener;
    private OnSuccessListener mOnSuccessListener;

    private Exception mException;


    public GetUserAsyncTask(int task, String userTwo,String password){
        functionNum = task;
        mainUser = User.getInstance();
        userWithID = userTwo;
        passwordEntered = password;
    }

    protected String doInBackground(Void... urls){
        try {
            ServerManager server = new ServerManager();
            String returnMessage;
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
        if (mException != null && mOnErrorListener != null) {
            mOnErrorListener.onError(mException);
            return;
        }

        if (mOnSuccessListener != null) {
            mOnSuccessListener.onSuccess();
        }
        // for result
    }

    public interface OnTaskCompleted{
        void onTaskCompleted();
    }

    public void setOnErrorListener(OnErrorListener l) {
        mOnErrorListener = l;
    }

    public void setOnSuccessListener(OnSuccessListener l) {
        mOnSuccessListener = l;
    }

    public interface OnErrorListener {
        void onError(Exception e);
    }

    public interface OnSuccessListener {
        void onSuccess();
    }

}
