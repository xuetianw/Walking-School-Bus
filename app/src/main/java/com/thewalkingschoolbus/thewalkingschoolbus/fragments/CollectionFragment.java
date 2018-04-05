package com.thewalkingschoolbus.thewalkingschoolbus.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.thewalkingschoolbus.thewalkingschoolbus.R;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;
import com.thewalkingschoolbus.thewalkingschoolbus.interfaces.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.models.Customization;
import com.thewalkingschoolbus.thewalkingschoolbus.models.User;
import com.thewalkingschoolbus.thewalkingschoolbus.models.collections.Avatar;
import com.thewalkingschoolbus.thewalkingschoolbus.models.collections.Theme;
import com.thewalkingschoolbus.thewalkingschoolbus.models.collections.Title;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.EDIT_USER;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.GET_USER_BY_ID;

public class CollectionFragment extends android.app.Fragment {

    private static final String TAG = "CollectionFragment";
    private View view;
    private Avatar[] avatars;
    private Title[] titles;
    private Theme[] themes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        view = inflater.inflate(R.layout.fragment_collection, container, false);


        // TEST
        User.getLoginUser().addPoints(50);
        new GetUserAsyncTask(EDIT_USER, User.getLoginUser(), null, null,null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
            }
            @Override
            public void onFailure(Exception e) {
            }
        }).execute();


        setupCustomizationInfo();

        updateListViewTitles();
        updateListViewThemes();
        updateAvatarSelection();
        updatePointsView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void setupCustomizationInfo() {
        avatars = Avatar.avatars;
        titles = Title.titles;
        themes = Theme.themes;

        if (User.getLoginUser().getCustomization() != null) {
            int[] avatarOwned = User.getLoginUser().getCustomization().getAvatarOwned();
            if (avatarOwned != null) {
                for (int i = 0; i < avatarOwned.length; i ++) {
                    avatars[avatarOwned[i]].setAvailable(true);
                }
            }
            int[] titleOwned = User.getLoginUser().getCustomization().getTitleOwned();
            if (titleOwned != null) {
                for (int i = 0; i < titleOwned.length; i ++) {
                    themes[titleOwned[i]].setAvailable(true);
                }
            }
            int[] themeOwned = User.getLoginUser().getCustomization().getThemeOwned();
            if (themeOwned != null) {
                for (int i = 0; i < themeOwned.length; i ++) {
                    avatars[themeOwned[i]].setAvailable(true);
                }
            }
        }
    }

    private void updatePointsView() {
        TextView textView = view.findViewById(R.id.userPoints);
        textView.setText(User.getLoginUser().getCurrentPoints() + " Point(s)"); // TODO: does not call to server
    }

    private ImageButton[] imageButtonAvatars;

    private void updateAvatarSelection() {
        imageButtonAvatars = new ImageButton[avatars.length];
        for (int i = 0; i < imageButtonAvatars.length; i++) {
            imageButtonAvatars[i] = new ImageButton(getActivity());
            imageButtonAvatars[i].setImageResource(getImageId(getActivity(), avatars[i].getName()));
            imageButtonAvatars[i].setAdjustViewBounds(true);
            imageButtonAvatars[i].setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageButtonAvatars[i].setBackgroundColor(Color.TRANSPARENT);
            imageButtonAvatars[i].setTag(i);
            imageButtonAvatars[i].setLayoutParams(new LinearLayout.LayoutParams(240, ViewGroup.LayoutParams.WRAP_CONTENT));
            imageButtonAvatars[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickAvatar((ImageButton) view);
                }
            });
            LinearLayout avatarHolder = view.findViewById(R.id.AvatarHolder);
            avatarHolder.addView(imageButtonAvatars[i]);

            if (!avatars[i].isAvailable()) {
                imageButtonAvatars[i].setColorFilter(Color.BLACK);
            }
            if (User.getLoginUser().getCustomization() != null && User.getLoginUser().getCustomization().getAvatarEquipped() == i) {
                imageButtonAvatars[i].setBackgroundColor(Color.LTGRAY);
            }
        }
    }

    private void onClickAvatar(final ImageButton imageButton) {
        final Avatar avatarClicked = avatars[(int) imageButton.getTag()];
        if (!avatarClicked.isAvailable()) {
            if (User.getLoginUser().addPoints(- avatarClicked.getCost())) { // Set new current points
                // Update avatar owned
                if (User.getLoginUser().getCustomization() == null) {
                    User.getLoginUser().setCustomization(new Customization());
                }
                if (User.getLoginUser().getCustomization().getAvatarOwned() == null) {
                    int[] avatarOwnedUpdate = new int[]{(int) imageButton.getTag()};
                    User.getLoginUser().getCustomization().setAvatarOwned(avatarOwnedUpdate);
                } else {
                    Log.d(TAG, "$$$$ " + Arrays.toString(User.getLoginUser().getCustomization().getAvatarOwned()));
                    int[] avatarOwned = User.getLoginUser().getCustomization().getAvatarOwned();
                    int[] avatarOwnedUpdate = new int[avatarOwned.length + 1];
                    System.arraycopy(avatarOwned, 0, avatarOwnedUpdate, 0, avatarOwned.length );
                    avatarOwnedUpdate[avatarOwnedUpdate.length - 1] = (int) imageButton.getTag();
                    User.getLoginUser().getCustomization().setAvatarOwned(avatarOwnedUpdate);
                    Log.d(TAG, "$$$$ " + Arrays.toString(User.getLoginUser().getCustomization().getAvatarOwned()));
                }

                new GetUserAsyncTask(EDIT_USER, User.getLoginUser(), null, null,null, new OnTaskComplete() {
                    @Override
                    public void onSuccess(Object result) {
                        Toast.makeText(getActivity(), "Purchased", Toast.LENGTH_SHORT).show();
                        imageButton.setColorFilter(Color.TRANSPARENT);
                        avatarClicked.setAvailable(true);
                        updatePointsView();
                    }
                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, "Error: "+e.getMessage());
                    }
                }).execute();
            } else {
                Toast.makeText(getActivity(), "Too expensive", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Update avatar equipped
            if (User.getLoginUser().getCustomization() == null) {
                User.getLoginUser().setCustomization(new Customization());
            }
            User.getLoginUser().getCustomization().setAvatarEquipped((int) imageButton.getTag());

            new GetUserAsyncTask(EDIT_USER, User.getLoginUser(), null, null,null, new OnTaskComplete() {
                @Override
                public void onSuccess(Object result) {
                    Toast.makeText(getActivity(), "Selected!", Toast.LENGTH_SHORT).show();
                    for (ImageButton button : imageButtonAvatars) {
                        button.setBackgroundColor(Color.TRANSPARENT);
                    }
                    imageButton.setBackgroundColor(Color.LTGRAY);
                }
                @Override
                public void onFailure(Exception e) {
                    Log.d(TAG, "Error: "+e.getMessage());
                }
            }).execute();
        }
    }

    private void updateListViewTitles() {
        // Build list
        List<String> monitoringList = new ArrayList<>();
        for(Title title: titles){
            if (title.isAvailable()) {
                monitoringList.add(title.getTitle());
            } else {
                monitoringList.add(title.getTitle() + " (LOCKED " + title.getCost() + "PTS)" );
            }
        }

        // Build adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.collection_entry, monitoringList);

        // Configure the list view
        ListView list = view.findViewById(R.id.listViewTitles);
        list.setAdapter(adapter);

        // Update clicks
        registerClickCallbackTitles();
    }

    private void registerClickCallbackTitles() {
        final ListView listview = view.findViewById(R.id.listViewTitles);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, final int position, long id) {
                if (titles[position].isAvailable()) {
                    for (int i = 0; i < listview.getChildCount(); i++) {
                        listview.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    }
                    listview.getChildAt(position).setBackgroundColor(Color.LTGRAY);
                    Toast.makeText(getActivity(), "Selected!", Toast.LENGTH_SHORT).show();
                } else {
                    new GetUserAsyncTask(GET_USER_BY_ID, User.getLoginUser(), null, null,null, new OnTaskComplete() {
                        @Override
                        public void onSuccess(Object result) {
                            final User userDetailed = (User) result;
                            userDetailed.addPoints(50); // TODO: for testing only. remove later
                            if (userDetailed.addPoints(- titles[position].getCost())) {
                                new GetUserAsyncTask(EDIT_USER, userDetailed, null, null,null, new OnTaskComplete() {
                                    @Override
                                    public void onSuccess(Object result) {
                                        Toast.makeText(getActivity(), "Purchased", Toast.LENGTH_SHORT).show();
                                        TextView textView = (TextView) listview.getChildAt(position);
                                        textView.setText(titles[position].getTitle());
                                        titles[position].setAvailable(true);
                                        updatePointsView();
                                        // TODO: user has purchased this item. remember it this
                                    }
                                    @Override
                                    public void onFailure(Exception e) {
                                        Log.d(TAG, "Error: "+e.getMessage());
                                    }
                                }).execute();
                            } else {
                                Toast.makeText(getActivity(), "Too expensive", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Exception e) {
                            Log.d(TAG, "Error: "+e.getMessage());
                        }
                    }).execute();
                }
            }
        });
    }

    private void updateListViewThemes() {
        // Build list
        List<String> monitoringList = new ArrayList<>();
        for(Theme theme: themes){
            if (theme.isAvailable()) {
                monitoringList.add(theme.getName());
            } else {
                monitoringList.add(theme.getName() + " (LOCKED " + theme.getCost() + "PTS)" );
            }
        }

        // Build adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.collection_entry, monitoringList);

        // Configure the list view
        ListView list = view.findViewById(R.id.listViewThemes);
        list.setAdapter(adapter);

        // Update clicks
        registerClickCallbackThemes();
    }

    private void registerClickCallbackThemes() {
        final ListView listview = view.findViewById(R.id.listViewThemes);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, final int position, long id) {
                if (themes[position].isAvailable()) {
                    for (int i = 0; i < listview.getChildCount(); i++) {
                        listview.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    }
                    listview.getChildAt(position).setBackgroundColor(Color.LTGRAY);
                    Toast.makeText(getActivity(), "Selected!", Toast.LENGTH_SHORT).show();
                } else {
                    new GetUserAsyncTask(GET_USER_BY_ID, User.getLoginUser(), null, null,null, new OnTaskComplete() {
                        @Override
                        public void onSuccess(Object result) {
                            final User userDetailed = (User) result;
                            userDetailed.addPoints(50); // TODO: for testing only. remove later
                            if (userDetailed.addPoints(- themes[position].getCost())) {
                                new GetUserAsyncTask(EDIT_USER, userDetailed, null, null,null, new OnTaskComplete() {
                                    @Override
                                    public void onSuccess(Object result) {
                                        Toast.makeText(getActivity(), "Purchased", Toast.LENGTH_SHORT).show();
                                        TextView textView = (TextView) listview.getChildAt(position);
                                        textView.setText(themes[position].getName());
                                        themes[position].setAvailable(true);
                                        updatePointsView();
                                        // TODO: user has purchased this item. remember it this
                                    }
                                    @Override
                                    public void onFailure(Exception e) {
                                        Log.d(TAG, "Error: "+e.getMessage());
                                    }
                                }).execute();
                            } else {
                                Toast.makeText(getActivity(), "Too expensive", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Exception e) {
                            Log.d(TAG, "Error: "+e.getMessage());
                        }
                    }).execute();
                }
            }
        });
    }

    public static int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }

//    private void updateDropdownTitles() {
//        Spinner dropdown = view.findViewById(R.id.spinnerTitles);
//
//        List<String> items = new ArrayList<>();
//        for (Title title : titles) {
//            if (title.isAvailable()) {
//                items.add(title.getTitle());
//            } else {
//                items.add(title.getTitle() + " (LOCKED " + title.getCost() + "PTS)" );
//            }
//        }
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), com.thewalkingschoolbus.thewalkingschoolbus.R.layout.support_simple_spinner_dropdown_item, items);
//        adapter.setDropDownViewResource(com.thewalkingschoolbus.thewalkingschoolbus.R.layout.support_simple_spinner_dropdown_item);
//        dropdown.setAdapter(adapter);
//        dropdown.setOnItemSelectedListener(this);
//    }
//
//    private void updateDropdownThemes() {
//        Spinner dropdown = view.findViewById(R.id.spinnerThemes);
//
//        List<String> items = new ArrayList<>();
//        for (Theme theme: themes) {
//            items.add(theme.getName());
//        }
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), com.thewalkingschoolbus.thewalkingschoolbus.R.layout.support_simple_spinner_dropdown_item, items);
//        adapter.setDropDownViewResource(com.thewalkingschoolbus.thewalkingschoolbus.R.layout.support_simple_spinner_dropdown_item);
//        dropdown.setAdapter(adapter);
//        dropdown.setOnItemSelectedListener(this);
//    }
//
//    @Override
//    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//        switch(adapterView.getId()){
//            case R.id.spinnerTitles:
//                Title titleSelected = titles[position];
//
//                if (titleSelected.isAvailable()) {
//                    Toast.makeText(getActivity(), titleSelected.getTitle() + " selected!", Toast.LENGTH_SHORT).show();
//                    // TODO: make this the user's equipped image
//                } else {
//
//                }
//
//
//                break;
//            case R.id.spinnerThemes:
//                Theme themeSelected = themes[position];
//                Toast.makeText(getActivity(), themeSelected.getName() + " selected!", Toast.LENGTH_SHORT).show();
//                // TODO: make this the user's equipped image
//                break;
//            default:
//                break;
//        }
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> adapterView) {
//
//    }
}
