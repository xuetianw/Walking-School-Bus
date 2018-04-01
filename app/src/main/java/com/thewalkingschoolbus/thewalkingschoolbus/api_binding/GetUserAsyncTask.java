package com.thewalkingschoolbus.thewalkingschoolbus.api_binding;

import android.os.AsyncTask;

import com.thewalkingschoolbus.thewalkingschoolbus.interfaces.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.models.Group;
import com.thewalkingschoolbus.thewalkingschoolbus.models.Message;
import com.thewalkingschoolbus.thewalkingschoolbus.models.User;

public class GetUserAsyncTask extends AsyncTask<Void, Void, Object>{

    private OnTaskComplete mlistener;

    private functionType functionChoice;

    private Object returnObject;
    private User mParentUser;
    private User mChildUser;
    private String passwordEntered;
    private Exception mException;
    private Group mGroup;
    private Message mMessage;

    public GetUserAsyncTask(functionType functionType, User parentUser, User childUser, Group group, Message message,OnTaskComplete listener){
        functionChoice = functionType;
        mlistener = listener;
        mGroup = group;
        mParentUser = parentUser;
        mChildUser = childUser;
        if(parentUser != null) {
            passwordEntered = parentUser.getPassword();
        }else{
            passwordEntered = null;
        }
        mMessage = message;
    }

    protected Object doInBackground(Void... urls){
        try {
            //ServerManager server = new ServerManager();
            switch (functionChoice) {
                case LOGIN_REQUEST:
                    returnObject = UserApiBinding.loginRequest(mParentUser,passwordEntered);
                    break;
                case CREATE_USER:
                    returnObject = UserApiBinding.createUser(mParentUser);
                    break;
                case LIST_USERS:
                    returnObject = UserApiBinding.listUsers();
                    break;
                case GET_USER_BY_ID:
                    returnObject = UserApiBinding.getUserById(mParentUser);
                    break;
                case GET_USER_BY_EMAIL:
                    returnObject = UserApiBinding.getUserByEmail(mParentUser);
                    break;
                case USR_MONITORING_LIST:
                    returnObject = MonitoringApiBinding.userMonitoringList(mParentUser);
                    break;
                case USER_MONITORING_BY_LIST:
                    returnObject = MonitoringApiBinding.userMonitoringByList(mParentUser);
                    break;
                case CREATE_MONITORING:
                    returnObject = MonitoringApiBinding.createMonitoring(mParentUser, mChildUser);
                    break;
                case DELETE_MONITORING:
                    returnObject = MonitoringApiBinding.deleteMonitoring(mParentUser, mChildUser);
                    break;
                case LIST_GROUPS:
                    returnObject = GroupApiBinding.listGroups();
                    break;
                case CREATE_GROUP:
                    returnObject = GroupApiBinding.createGroup(mGroup);
                    break;
                case CREATE_GROUP_WITH_DETAIL:
                    returnObject = GroupApiBinding.createGroupWithDetail(mGroup);
                    break;
                case GET_ONE_GROUP:
                    returnObject = GroupApiBinding.getOneGroup(mGroup);
                    break;
                case UPDATE_EXISTING_GROUP:
                    returnObject = GroupApiBinding.updateExistingGroup(mGroup);
                    break;
                case DELETE_GROUP:
                    returnObject = GroupApiBinding.deleteGroup(mGroup);
                    break;
                case GET_MEMBERS_OF_GROUP:
                    returnObject = GroupApiBinding.getMembersOfGroup(mGroup);
                    break;
                case ADD_MEMBER_TO_GROUP:
                    returnObject = GroupApiBinding.addMemberToGroup(mParentUser,mGroup);
                    break;
                case REMOVE_MEMBER_OF_GROUP:
                    returnObject = GroupApiBinding.removeMemberOfGroup(mParentUser,mGroup);
                    break;
                case DELETE_USER:
                    returnObject = UserApiBinding.deleteUser(mParentUser);
                    break;
                case EDIT_USER:
                    returnObject = UserApiBinding.editUser(mParentUser);
                    break;
                case GET_GPS_LOCATION:
                    returnObject = UserApiBinding.getGpsLocation(mParentUser);
                    break;
                case POST_GPS_LOCATION:
                    returnObject = UserApiBinding.postGpsLocation(mParentUser);
                    break;
                case GET_ALL_MESSAGES:
                    returnObject = MessageApiBinding.getAllMessages();
                    break;
                case GET_ALL_EMERGENCY_MESSAGES:
                    returnObject = MessageApiBinding.getAllEmergencyMessages();
                    break;
                case GET_MESSAGES_FOR_GROUP:
                    returnObject = MessageApiBinding.getMessagesForGroup(mGroup);
                    break;
                case GET_EMERGENCY_MESSAGES_FOR_GROUP:
                    returnObject = MessageApiBinding.getEmergencyMessagesForGroup(mGroup);
                    break;
                case GET_MESSAGES_FOR_USER:
                    returnObject = MessageApiBinding.getMessagesForUser(mParentUser);
                    break;
                case GET_UNREAD_MESSAGES_FOR_USER:
                    returnObject = MessageApiBinding.getUnreadMessagesForUser(mParentUser);
                    break;
                case GET_READ_MESSAGES_FOR_USER:
                    returnObject = MessageApiBinding.getReadMessagesForUser(mParentUser);
                    break;
                case GET_UNREAD_EMERGENCY_MESSAGES_FOR_USER:
                    returnObject = MessageApiBinding.getUnreadEmergencyMessagesForUser(mParentUser);
                    break;
                case POST_MESSAGE_TO_GROUP:
                    returnObject = MessageApiBinding.postMessageToGroup(mGroup,mMessage);
                    break;
                case POST_MESSAGE_TO_PARENTS:
                    returnObject = MessageApiBinding.postMessageToParents(mParentUser,mMessage);
                    break;
                case GET_ONE_MESSAGE:
                    returnObject = MessageApiBinding.getOneMessage(mMessage);
                    break;
                case SET_MESSAGE_AS_READ_OR_UNREAD:
                    returnObject = MessageApiBinding.setMessageAsReadOrUnread(mParentUser,mMessage);
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
        LOGIN_REQUEST, CREATE_USER, LIST_USERS, GET_USER_BY_ID,GET_USER_BY_EMAIL,
        DELETE_USER,EDIT_USER,GET_GPS_LOCATION,POST_GPS_LOCATION,

        USR_MONITORING_LIST,USER_MONITORING_BY_LIST, CREATE_MONITORING, DELETE_MONITORING,

        LIST_GROUPS,CREATE_GROUP,CREATE_GROUP_WITH_DETAIL,GET_ONE_GROUP,UPDATE_EXISTING_GROUP,
        DELETE_GROUP, GET_MEMBERS_OF_GROUP,ADD_MEMBER_TO_GROUP,REMOVE_MEMBER_OF_GROUP,

        GET_ALL_MESSAGES,GET_ALL_EMERGENCY_MESSAGES,GET_MESSAGES_FOR_GROUP,
        GET_EMERGENCY_MESSAGES_FOR_GROUP,GET_MESSAGES_FOR_USER,GET_UNREAD_MESSAGES_FOR_USER,
        GET_READ_MESSAGES_FOR_USER,GET_UNREAD_EMERGENCY_MESSAGES_FOR_USER,POST_MESSAGE_TO_GROUP,
        POST_MESSAGE_TO_PARENTS,GET_ONE_MESSAGE,SET_MESSAGE_AS_READ_OR_UNREAD
    }

}
