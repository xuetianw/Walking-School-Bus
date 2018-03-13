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
import android.widget.TextView;

import com.thewalkingschoolbus.thewalkingschoolbus.Interface.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.Group;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.User;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;

import java.util.List;

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.GET_USER_BY_ID;

public class GroupFragment extends android.app.Fragment {

    private static final String TAG = "GroupFragment";
    private View view;
    private Group[] mGroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        view = inflater.inflate(R.layout.fragment_group, container, false);
        setUpRefresh();
        getGroupListAndPopulateList();
        setUpAddButton();
        return view;

        /*
        * How to add content in fragment:
        *
        * Fragments function identical to regular activities, except it does not extend from AppCompatActivity.
        * Hence, some things such as findViewByID or executing context related code works differently.
        *
        * FindViewBYId Example
        * instead   of: Button btn = findViewById(R.id.example);
        *           do: Button btn = view.findViewById(R.id.example);
        *
        * Context Example
        * Instead   of: Toast.makeText(this, "example", Toast.LENGTH_SHORT).show()
        *           do: Toast.makeText(getActivity(), "example.", Toast.LENGTH_SHORT).show()
        *
        * If this is unclear, look at example code in MonitoringFragment.
        */
    }
    private void getGroupListAndPopulateList(){
        new GetUserAsyncTask(GET_USER_BY_ID, User.getLoginUser(), null, null, null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                User returnUser = (User) result;
                List<Group> mGroupList = returnUser.getMemberOfGroups();
                mGroup = new Group[mGroupList.size()];
                mGroupList.toArray(mGroup);
                stringsPrep();
            }

            @Override
            public void onFailure(Exception e) {
            }
        }).execute();
    }

    private void stringsPrep(){
        String[] mGroupDisplay;
        int length;
        if(mGroup == null){
            mGroupDisplay = new String[0];
            length = 0;
        }
            mGroupDisplay = new String[mGroup.length];
            length = mGroup.length;


        for(int i = 0; i < length;i++){
            String str = "id: "+mGroup[i].getId() +" "+"Group Name:"+mGroup[i].getGroupDescription();
            mGroupDisplay[i]=str;
        }

        populateListView(mGroupDisplay);
    }

    private void populateListView(String[] mGroupDisplay){
        // create list of item
        String[] myItems = mGroupDisplay;
        // Build adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.group_entry,myItems);
        // configure the list view
        ListView list = view.findViewById(R.id.groupListViewId);
        list.setAdapter(adapter);

        //registerClickCallback
        registerClickCallback();
    }

    private void registerClickCallback() {
        ListView list = view.findViewById(R.id.groupListViewId);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                Group selectedGroup = mGroup[position];
                Intent intent =  GroupDetailActivity.makeIntent(getActivity(),selectedGroup,User.getLoginUser());
                startActivity(intent);
            }
        });
    }

    private void setUpAddButton(){
        FloatingActionButton btn = view.findViewById(R.id.addGroupBut);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = JoinOrCreateGroupActivity.makeIntent(getActivity());
                startActivity(intent);
            }
        });
    }

    private void setUpRefresh(){
        final SwipeRefreshLayout mySwipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getGroupListAndPopulateList();
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

    }
}
