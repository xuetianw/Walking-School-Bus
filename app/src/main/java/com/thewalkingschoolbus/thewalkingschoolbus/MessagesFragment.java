package com.thewalkingschoolbus.thewalkingschoolbus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.thewalkingschoolbus.thewalkingschoolbus.interfaces.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.models.Message;
import com.thewalkingschoolbus.thewalkingschoolbus.models.User;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;

import java.util.ArrayList;
import java.util.List;

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.GET_READ_MESSAGES_FOR_USER;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.GET_UNREAD_MESSAGES_FOR_USER;

public class MessagesFragment extends android.app.Fragment {

    private static final String TAG = "MessagesFragment";
    private View view;
    private List<String> messageList;
    private List<Message> fullMessageList;
    private Message[] messages;
    private int numMessages;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        view = inflater.inflate(R.layout.fragment_messages, container, false);

        //updateListView();
        setupNewMessageBtn();
        setUpRefresh();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateListView();
    }

    private void updateListView() {
        new GetUserAsyncTask(GET_UNREAD_MESSAGES_FOR_USER, User.getLoginUser(),null, null,null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                messages = (Message[]) result;
                messageList = new ArrayList<>();
                fullMessageList = new ArrayList<>();

                for (numMessages = 0; numMessages < messages.length;numMessages++) {
                    if(messages[numMessages].isEmergency()) {
                        messageList.add(numMessages, "ID: " + messages[numMessages].getId() +
                                " From User: " + messages[numMessages].getFromUser().getName() + " (unread urgent)");
                    }else{
                        messageList.add(numMessages, "ID: " + messages[numMessages].getId() +
                                " From User: " + messages[numMessages].getFromUser().getName() + " (unread)");
                    }
                    fullMessageList.add(numMessages,messages[numMessages]);
                }


                getReadMessageForUser();

            }
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getActivity(),"Unable to update the list", Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }

    private void getReadMessageForUser(){
        new GetUserAsyncTask(GET_READ_MESSAGES_FOR_USER, User.getLoginUser(), null, null, null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                messages = (Message[]) result;
                if(messages.length != 0) {
                    for (int i = 0; i < messages.length;i++) {
                        messageList.add(numMessages,"ID: "+messages[i].getId()+" From User: "+ messages[i].getFromUser().getName());
                        fullMessageList.add(numMessages,messages[i]);
                        numMessages++;
                    }
                }


                populateListView();

            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getActivity(),"error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }

    private void populateListView(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.message_entry, messageList);
        ListView list = view.findViewById(R.id.listViewMessages);
        list.setAdapter(adapter);
        registerClickCallback();

        if(messages.length == 0){
            Toast.makeText(getActivity(),"No message", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerClickCallback() {
        ListView list = view.findViewById(R.id.listViewMessages);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Message messageClicked = fullMessageList.get(position);
                Intent intent = MessageViewActivity.makeIntent(getActivity(), messageClicked);
                startActivity(intent);
            }
        });
    }

    private void setupNewMessageBtn() {
        FloatingActionButton btn = view.findViewById(R.id.btnNewMessage);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MessageNewActivity.class));
            }
        });
    }

    private void setUpRefresh(){
        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshMessages);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        updateListView();
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }
}
