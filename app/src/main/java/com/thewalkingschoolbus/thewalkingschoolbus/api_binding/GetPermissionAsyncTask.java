package com.thewalkingschoolbus.thewalkingschoolbus.api_binding;

import android.os.AsyncTask;

import com.thewalkingschoolbus.thewalkingschoolbus.interfaces.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.models.Group;
import com.thewalkingschoolbus.thewalkingschoolbus.models.PermissionRequest;
import com.thewalkingschoolbus.thewalkingschoolbus.models.User;

/**
 * Created by JackyX on 2018-04-03.
 */

public class GetPermissionAsyncTask extends AsyncTask<Void, Void, Object> {

    private OnTaskComplete mlistener;

    private GetPermissionAsyncTask.functionTypeForPermission functionChoice;

    private Object returnObject;
    private User mParentUser;
    private Exception mException;
    private Group mGroup;
    private PermissionRequest mPermissionRequest;

    public GetPermissionAsyncTask(GetPermissionAsyncTask.functionTypeForPermission permissionType,
                                  User parentUser, Group group, PermissionRequest pr, OnTaskComplete listener){
        functionChoice = permissionType;
        mlistener = listener;
        mGroup = group;
        mParentUser = parentUser;
        mPermissionRequest = pr;
    }

    protected Object doInBackground(Void... urls){
        try {
            switch (functionChoice) {
                case GET_ALL_PERMISSION_REQUESTS:
                    returnObject = PermissionsApiBinding.getAllPermissionRequests();
                    break;
                case GET_PERMISSION_REQUESTS_FOR_USER:
                    returnObject = PermissionsApiBinding.getPermissionRequestsForUser(mParentUser);
                    break;
                case GET_PERMISSION_REQUESTS_FOR_USER_WITH_CERTAIN_STATUS:
                    returnObject = PermissionsApiBinding.getPermissionRequestsForUserWithCertainStatus(mParentUser,mPermissionRequest);
                    break;
                case GET_PERMISSION_REQUESTS_FOR_GROUP:
                    returnObject = PermissionsApiBinding.getPermissionRequestsForGroup(mGroup);
                    break;
                case GET_PERMISSION_REQUESTS_WITH_STATUS:
                    returnObject = PermissionsApiBinding.getPermissionRequestsWithStatus(mPermissionRequest);
                    break;
                case GET_PERMISSION_REQUESTS_FOR_USER_IN_GROUP_WITH_CERTAIN_STATUS:
                    returnObject = PermissionsApiBinding.getPermissionRequestsForUserInGroupWithCertainStatus(mParentUser,mGroup,mPermissionRequest);
                    break;
                case GET_PERMISSION_REQUEST_WITH_ID:
                    returnObject = PermissionsApiBinding.getPermissionRequestsWithID(mPermissionRequest);
                    break;
                case POST_PERMISSION_CHANGE_WITH_ID:
                    returnObject = PermissionsApiBinding.postPermissionRequestsChangeWithId(mPermissionRequest);
                    break;
                default:
                    returnObject = null;
                    break;
            }
            return returnObject;

        } catch (Exception e) {
            mException = e;
        }
        return null;
    }

    protected void onPostExecute(Object returnObject) {
        if (mlistener != null) {
            if (mException == null) {
                mlistener.onSuccess(returnObject);
            } else {
                mlistener.onFailure(mException);
            }
        }
    }

    public enum functionTypeForPermission {
        GET_ALL_PERMISSION_REQUESTS,GET_PERMISSION_REQUESTS_FOR_USER,
        GET_PERMISSION_REQUESTS_FOR_USER_WITH_CERTAIN_STATUS,GET_PERMISSION_REQUESTS_FOR_GROUP,
        GET_PERMISSION_REQUESTS_WITH_STATUS,GET_PERMISSION_REQUESTS_FOR_USER_IN_GROUP_WITH_CERTAIN_STATUS,
        GET_PERMISSION_REQUEST_WITH_ID,POST_PERMISSION_CHANGE_WITH_ID
    }
}
