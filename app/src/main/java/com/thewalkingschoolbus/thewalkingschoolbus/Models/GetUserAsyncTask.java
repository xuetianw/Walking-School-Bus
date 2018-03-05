package com.thewalkingschoolbus.thewalkingschoolbus.Models;

import android.os.AsyncTask;

import java.net.URL;

/**
 * Created by Jackyx on 2018-03-04.
 */

public class GetUserAsyncTask extends AsyncTask<String, Void, Void>{

    private int functionNum;
    private String name;
    private String email;
    private String password;

    public GetUserAsyncTask(int functionNumEntered,
                            String nameEntered,
                            String emailEntered,
                            String passwordEntered){
        if(functionNumEntered > 0) {
            functionNum = functionNumEntered;
            name = nameEntered;
            email = emailEntered;
            password = passwordEntered;
        }

    }

    protected String doInBackground(Void... urls){
        try {
            switch (functionNum) {
                case 1:
                    return ServerManager.loginRequest(email,password);
                    break;
                case 2:
                    return ServerManager.createUser(email,password,name);
                    break;
            }
            } catch (Exception e) {
            //mException = e;
        }
        return null;
    }


    protected void onProgressUpdate(Void... progress) {

    }

    protected void onPostExecute(long result) {
        // for result
    }

}
