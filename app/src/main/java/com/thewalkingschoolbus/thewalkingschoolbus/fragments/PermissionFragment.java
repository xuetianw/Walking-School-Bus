package com.thewalkingschoolbus.thewalkingschoolbus.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.thewalkingschoolbus.thewalkingschoolbus.R;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetPermissionAsyncTask;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.ServerManager;
import com.thewalkingschoolbus.thewalkingschoolbus.interfaces.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.models.PermissionRequest;
import com.thewalkingschoolbus.thewalkingschoolbus.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetPermissionAsyncTask.functionTypeForPermission.GET_PERMISSION_REQUESTS_FOR_USER;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetPermissionAsyncTask.functionTypeForPermission.GET_PERMISSION_REQUEST_WITH_ID;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetPermissionAsyncTask.functionTypeForPermission.POST_PERMISSION_CHANGE_WITH_ID;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.ServerManager.PermissionStatus.APPROVED;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.ServerManager.PermissionStatus.DENIED;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.ServerManager.PermissionStatus.PENDING;

public class PermissionFragment extends android.app.Fragment {
    private static final String TAG = "PermissionFragment";
    private View view;

    private List<PermissionRequest> allPermissionRequests;
    private List<PermissionRequest> pendingPermissionRequests;

    private PermissionRequest selectedPermissionRequest;
    private List<String> pendingPermissionRequestsStr;
    private List<String> allPermissionRequestsStr;
    private PermissionRequest.Authorizor[] authorizors;

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
        new GetPermissionAsyncTask(GET_PERMISSION_REQUESTS_FOR_USER, User.getLoginUser(), null, null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                PermissionRequest[] pr = (PermissionRequest[]) result;
                allPermissionRequests = Arrays.asList(pr);

                if(allPermissionRequests.isEmpty()){
                    Toast.makeText(getActivity(),"NO REQUESTS",Toast.LENGTH_SHORT).show();
                    return;
                }

                extractPending();

            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getActivity(),"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }

    private void extractPending(){
        pendingPermissionRequests = new ArrayList<>();

        List<PermissionRequest> toKeep = new ArrayList<>();

        for (PermissionRequest pr: allPermissionRequests) {
            if(!pr.getRequestingUser().getId().equals(User.getLoginUser().getId())) {
                if (pr.getStatus() == PENDING) {
                    pendingPermissionRequests.add(pr);
                } else {
                    toKeep.add(pr);
                }
            }
        }


        if(pendingPermissionRequests.isEmpty()) {
            Toast.makeText(getActivity(), "NO PENDING REQUESTS", Toast.LENGTH_SHORT).show();
            allPermissionRequests = toKeep;
        }
        allPermissionRequests = toKeep;

        stringPrepForPendingList();
        stringPrepForAlll();
    }

    private void stringPrepForPendingList(){
        pendingPermissionRequestsStr = new ArrayList<>();
        for(PermissionRequest pr: pendingPermissionRequests){
            pendingPermissionRequestsStr.add("Permission Id: "+pr.getId().toString()+" from user: "+pr.getRequestingUser().getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.permission_entry, pendingPermissionRequestsStr);
        // configure the list view
        ListView list = view.findViewById(R.id.listViewForPermission);

        list.setAdapter(adapter);

        registerClickCallbackForPending();
    }

    private void stringPrepForAlll(){
        allPermissionRequestsStr = new ArrayList<>();
        for(PermissionRequest pr: allPermissionRequests){
            allPermissionRequestsStr.add("Permission Id: "+pr.getId().toString()+" from user: "+pr.getRequestingUser().getName()+ " ("+pr.getStatus().toString()+")");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.permission_entry, allPermissionRequestsStr);
        // configure the list view
        ListView list = view.findViewById(R.id.listViewForAllPermissions);

        list.setAdapter(adapter);

        registerClickCallbackForAll();
    }

    //call back for pending list
    private void registerClickCallbackForPending(){
        ListView list = view.findViewById(R.id.listViewForPermission);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                selectedPermissionRequest = pendingPermissionRequests.get(position);
                alertDialogForPending();
            }
        });
    }

    //call back for all-list
    private void registerClickCallbackForAll(){
        ListView list = view.findViewById(R.id.listViewForAllPermissions);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                selectedPermissionRequest = allPermissionRequests.get(position);
                alertDialogForAll();
            }
        });
    }
    // set up both altert dialog when list view on item click
    private void alertDialogForAll(){
        Set<PermissionRequest.Authorizor> authorizor = selectedPermissionRequest.getAuthorizors();
        authorizors = authorizor.toArray(new PermissionRequest.Authorizor[0]);
        String infoOnAuthorizer = stringPrepForAuthorizor();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Message");
        builder.setMessage(selectedPermissionRequest.getMessage()+"\n\n Authorizers :\n"+ infoOnAuthorizer);
        AlertDialog alert = builder.create();
        alert.show();
    }

    private String stringPrepForAuthorizor(){
        StringBuilder str = new StringBuilder();

        for(PermissionRequest.Authorizor pa:authorizors){
            str.append(pa.getStatus().toString()+" By User:uhhhh "+pa.getWhoApprovedOrDenied().getName()+"\n");
        }

        return str.toString();
    }

    private void alertDialogForPending(){
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

    // update status for permission
    private void updateStatus(ServerManager.PermissionStatus functionType){
        selectedPermissionRequest.setStatus(functionType);
        new GetPermissionAsyncTask(POST_PERMISSION_CHANGE_WITH_ID, null, null, selectedPermissionRequest, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                Toast.makeText(getActivity(),"ez",Toast.LENGTH_SHORT).show();
                updateUser();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getActivity(),"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }
}


