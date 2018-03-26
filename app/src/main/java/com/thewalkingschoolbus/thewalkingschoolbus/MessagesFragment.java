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

import com.thewalkingschoolbus.thewalkingschoolbus.Interface.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.Message;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.User;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;

import java.util.ArrayList;
import java.util.List;

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.GET_MESSAGES_FOR_USER;

public class MessagesFragment extends android.app.Fragment {

    private static final String TAG = "MessagesFragment";
    private View view;
    List<String> messageList;
    Message[] messages;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        if (container != null) {
//            container.removeAllViews();
//        }
        view = inflater.inflate(R.layout.fragment_messages, container, false);

        //updateListView();
        setupAddMonitoringBtn();
        setUpRefresh();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateListView();
    }

    private void updateListView() {
        new GetUserAsyncTask(GET_MESSAGES_FOR_USER, User.getLoginUser(),null, null,null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                messageList = new ArrayList<>();
                messages = (Message[]) result;

                for(Message message: messages){
                    messageList.add(message.getText());
                }

                // build adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.message_entry, messageList);

                // configure the list view
                ListView list = view.findViewById(R.id.listViewMessages);
                list.setAdapter(adapter);

                // update clicks
                registerClickCallback();

                if(messages.length == 0){
                    Toast.makeText(getActivity(),"No message", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getActivity(),"Unable to update the list", Toast.LENGTH_SHORT)
                        .show();
                Toast.makeText(getActivity(), "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }

    // Read message
    private void registerClickCallback() {
        ListView list = view.findViewById(R.id.listViewMessages);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Message messageClicked = messages[position];
                Intent intent = MessageViewActivity.makeIntent(getActivity(), messageClicked);
                startActivity(intent);
            }
        });
    }

    // Write message
    private void setupAddMonitoringBtn() {
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
