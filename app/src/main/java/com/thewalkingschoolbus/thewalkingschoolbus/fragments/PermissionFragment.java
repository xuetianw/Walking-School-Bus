package com.thewalkingschoolbus.thewalkingschoolbus.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.thewalkingschoolbus.thewalkingschoolbus.R;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetPermissionAsyncTask;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.ServerManager;
import com.thewalkingschoolbus.thewalkingschoolbus.interfaces.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.models.PermissionRequest;
import com.thewalkingschoolbus.thewalkingschoolbus.models.User;

import java.util.ArrayList;
import java.util.List;

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetPermissionAsyncTask.functionTypeForPermission.GET_PERMISSION_REQUEST_WITH_ID;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetPermissionAsyncTask.functionTypeForPermission.POST_PERMISSION_CHANGE_WITH_ID;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.GET_USER_BY_ID;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.ServerManager.PermissionStatus.APPROVED;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.ServerManager.PermissionStatus.DENIED;

public class PermissionFragment extends android.app.Fragment {
    private static final String TAG = "PermissionFragment";
    private View view;

    private List<PermissionRequest> permissionRequests;
    private PermissionRequest selectedPermissionRequest;
    private List<String> permissionRequestsStr;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        view = inflater.inflate(R.layout.fragment_permission, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUser();

    }

    private void updateUser(){
        new GetUserAsyncTask(GET_USER_BY_ID, User.getLoginUser(), null, null, null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                permissionRequests = User.getLoginUser().getPendingPermissionRequests();
                if(permissionRequests.isEmpty()){
                    Toast.makeText(getActivity(),"NO REQUESTS",Toast.LENGTH_SHORT).show();
                    return;
                }
                stringPrep();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getActivity(),"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }

    private void stringPrep(){
        permissionRequestsStr = new ArrayList<>();
        for(PermissionRequest pr: permissionRequests){
            permissionRequestsStr.add(pr.getId().toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.permission_entry, permissionRequestsStr);
        // configure the list view
        ListView list = view.findViewById(R.id.listViewForPermission);

        list.setAdapter(adapter);

        registerClickCallback();
    }
    private void registerClickCallback(){
        ListView list = view.findViewById(R.id.listViewForPermission);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                selectedPermissionRequest = permissionRequests.get(position);
                getPermissionDetail();
            }
        });
    }
    private void getPermissionDetail(){
        new GetPermissionAsyncTask(GET_PERMISSION_REQUEST_WITH_ID, null, null, selectedPermissionRequest, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                selectedPermissionRequest = (PermissionRequest) result;
                alertDialog();
            }

            @Override
            public void onFailure(Exception e) {

            }
        }).execute();
    }

    private void alertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Confirm");
        builder.setMessage(selectedPermissionRequest.getMessage());

        builder.setPositiveButton("APPROVED", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                updateStatus(APPROVED);
                // Do nothing but close the dialog
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("DENIED", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateStatus(DENIED);
                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void updateStatus(ServerManager.PermissionStatus functionType){
        selectedPermissionRequest.setStatus(functionType);
        new GetPermissionAsyncTask(POST_PERMISSION_CHANGE_WITH_ID, null, null, selectedPermissionRequest, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                Toast.makeText(getActivity(),"ez",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getActivity(),"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }
}


