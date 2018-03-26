package com.thewalkingschoolbus.thewalkingschoolbus.api_binding;

import android.os.AsyncTask;

import com.thewalkingschoolbus.thewalkingschoolbus.Interface.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.Group;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.Message;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.User;

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
            ServerManager server = new ServerManager();
            switch (functionChoice) {
                case LOGIN_REQUEST:
                    returnObject = server.loginRequest(mParentUser,passwordEntered);
                    break;
                case CREATE_USER:
                    returnObject = server.createUser(mParentUser);
                    break;
                case LIST_USERS:
                    returnObject = server.listUsers();
                    break;
                case GET_USER_BY_ID:
                    returnObject = server.getUserById(mParentUser);
                    break;
                case GET_USER_BY_EMAIL:
                    returnObject = server.getUserByEmail(mParentUser);
                    break;
                case USR_MONITORING_LIST:
                    returnObject = server.userMonitoringList(mParentUser);
                    break;
                case USER_MONITORING_BY_LIST:
                    returnObject = server.userMonitoringByList(mParentUser);
                    break;
                case CREATE_MONITORING:
                    returnObject = server.createMonitoring(mParentUser, mChildUser);
                    break;
                case DELETE_MONITORING:
                    returnObject = server.deleteMonitoring(mParentUser, mChildUser);
                    break;
                case LIST_GROUPS:
                    returnObject = server.listGroups();
                    break;
                case CREATE_GROUP:
                    returnObject = server.createGroup(mGroup);
                    break;
                case CREATE_GROUP_WITH_DETAIL:
                    returnObject = server.createGroupWithDetail(mGroup);
                    break;
                case GET_ONE_GROUP:
                    returnObject = server.getOneGroup(mGroup);
                    break;
                case UPDATE_EXISTING_GROUP:
                    returnObject = server.updateExistingGroup(mGroup);
                    break;
                case DELETE_GROUP:
                    returnObject = server.deleteGroup(mGroup);
                    break;
                case GET_MEMBERS_OF_GROUP:
                    returnObject = server.getMembersOfGroup(mGroup);
                    break;
                case ADD_MEMBER_TO_GROUP:
                    returnObject = server.addMemberToGroup(mParentUser,mGroup);
                    break;
                case REMOVE_MEMBER_OF_GROUP:
                    returnObject = server.removeMemberOfGroup(mParentUser,mGroup);
                    break;
                case DELETE_USER:
                    returnObject = server.deleteUser(mParentUser);
                    break;
                case EDIT_USER:
                    returnObject = server.editUser(mParentUser);
                    break;
                case GET_GPS_LOCATION:
                    returnObject = server.getGpsLocation(mParentUser);
                    break;
                case POST_GPS_LOCATION:
                    returnObject = server.postGpsLocation(mParentUser);
                    break;
                case GET_ALL_MESSAGES:
                    returnObject = server.getAllMessages();
                    break;
                case GET_ALL_EMERGENCY_MESSAGES:
                    returnObject = server.getAllEmergencyMessages();
                    break;
                case GET_MESSAGES_FOR_GROUP:
                    returnObject = server.getMessagesForGroup(mGroup);
                    break;
                case GET_EMERGENCY_MESSAGES_FOR_GROUP:
                    returnObject = server.getEmergencyMessagesForGroup(mGroup);
                    break;
                case GET_MESSAGES_FOR_USER:
                    returnObject = server.getMessagesForUser(mParentUser);
                    break;
                case GET_UNREAD_MESSAGES_FOR_USER:
                    returnObject = server.getUnreadMessagesForUser(mParentUser);
                    break;
                case GET_READ_MESSAGES_FOR_USER:
                    returnObject = server.getReadMessagesForUser(mParentUser);
                    break;
                case GET_UNREAD_EMERGENCY_MESSAGES_FOR_USER:
                    returnObject = server.getUnreadEmergencyMessagesForUser(mParentUser);
                    break;
                case POST_MESSAGE_TO_GROUP:
                    returnObject = server.postMessageToGroup(mGroup,mMessage);
                    break;
                case POST_MESSAGE_TO_PARENTS:
                    returnObject = server.postMessageToParents(mParentUser,mMessage);
                    break;
                case GET_ONE_MESSAGE:
                    returnObject = server.getOneMessage(mMessage);
                    break;
                case SET_MESSAGE_AS_READ_OR_UNREAD:
                    returnObject = server.setMessageAsReadOrUnread(mParentUser,mMessage);
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
