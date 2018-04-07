package com.thewalkingschoolbus.thewalkingschoolbus.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.thewalkingschoolbus.thewalkingschoolbus.R;
import com.thewalkingschoolbus.thewalkingschoolbus.activities.MainActivity;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;
import com.thewalkingschoolbus.thewalkingschoolbus.interfaces.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.models.Customization;
import com.thewalkingschoolbus.thewalkingschoolbus.models.User;
import com.thewalkingschoolbus.thewalkingschoolbus.models.collections.Avatar;
import com.thewalkingschoolbus.thewalkingschoolbus.models.collections.Theme;
import com.thewalkingschoolbus.thewalkingschoolbus.models.collections.Title;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.EDIT_USER;

public class CollectionFragment extends android.app.Fragment {

    private static final String TAG = "CollectionFragment";
    private View view;
    private Avatar[] avatars;
    private Title[] titles;
    private Theme[] themes;
    private ImageButton[] imageButtonAvatars;
    private TextView[] textViewAvatars;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        view = inflater.inflate(R.layout.fragment_collection, container, false);

        // TEST //
        //resetUnlocks();
        setupAddTestPoints();
        // TEST //

        setupCustomizationInfo();
        updateListViewTitles();
        updateListViewThemes();
        updateAvatarSelection();
        updatePointsView();

        return view;
    }

    private void setupAddTestPoints() {
        Button button = view.findViewById(R.id.addTestPoints);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.getLoginUser().addPoints(100);
                updatePointsView();

                // Save to server
                new GetUserAsyncTask(EDIT_USER, User.getLoginUser(), null, null,null, new OnTaskComplete() {
                    @Override
                    public void onSuccess(Object result) {
                        Toast.makeText(getActivity(), "+ 100", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, "Error: "+e.getMessage());
                    }
                }).execute();
            }
        });
    }

    private void setupCustomizationInfo() {
        avatars = Avatar.avatars;
        titles = Title.titles;
        themes = Theme.themes;

        // Reset unlock
        for (Avatar avatar : avatars) {
            avatar.setAvailable(false);
        }
        for (Title title : titles) {
            title.setAvailable(false);
        }
        for (Theme theme : themes) {
            theme.setAvailable(false);
        }

        // Update unlock
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
                    titles[titleOwned[i]].setAvailable(true);
                }
            }
            int[] themeOwned = User.getLoginUser().getCustomization().getThemeOwned();
            if (themeOwned != null) {
                for (int i = 0; i < themeOwned.length; i ++) {
                    themes[themeOwned[i]].setAvailable(true);
                }
            }
        }
    }

    private void updatePointsView() {
        TextView textView = view.findViewById(R.id.userPoints);
        textView.setText(User.getLoginUser().getCurrentPoints() + " PTS / " + User.getLoginUser().getTotalPointsEarned() + " PTS TOTAL");
    }

    private void updateAvatarSelection() {
        imageButtonAvatars = new ImageButton[avatars.length];
        textViewAvatars = new TextView[avatars.length];
        for (int i = 0; i < imageButtonAvatars.length; i++) {
            // Set up image buttons
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

            // Set up text view avatar cost
            textViewAvatars[i] = new TextView(getActivity());
            textViewAvatars[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textViewAvatars[i].setLayoutParams(new LinearLayout.LayoutParams(240, ViewGroup.LayoutParams.WRAP_CONTENT));
            LinearLayout avatarHolderCost = view.findViewById(R.id.AvatarHolderCost);
            avatarHolderCost.addView(textViewAvatars[i]);

            // Darken locked avatar, remove cost for unlocked avatar
            if (!avatars[i].isAvailable()) {
                imageButtonAvatars[i].setColorFilter(Color.BLACK);
                textViewAvatars[i].setText(avatars[i].getCost() + " PTS");
            }
            // Highlight equipped avatar
            if (User.getLoginUser().getCustomization() != null && User.getLoginUser().getCustomization().getAvatarEquipped() == i) {
                imageButtonAvatars[i].setBackgroundColor(Color.LTGRAY);
            }
        }
    }

    private void onClickAvatar(final ImageButton imageButton) {
        final Avatar avatarClicked = avatars[(int) imageButton.getTag()];
        if (avatarClicked.isAvailable()) {

            // Update avatar equipped
            if (User.getLoginUser().getCustomization() == null) {
                User.getLoginUser().setCustomization(new Customization());
            }

            // If already equipped, un-equip. Else equip
            if (User.getLoginUser().getCustomization().getAvatarEquipped() == (int) imageButton.getTag()) {
                User.getLoginUser().getCustomization().setAvatarEquipped(-1);
                imageButton.setBackgroundColor(Color.TRANSPARENT);
            } else {
                Toast.makeText(getActivity(), "Selected!", Toast.LENGTH_SHORT).show();
                User.getLoginUser().getCustomization().setAvatarEquipped((int) imageButton.getTag());
                for (ImageButton button : imageButtonAvatars) {
                    button.setBackgroundColor(Color.TRANSPARENT);
                }
                imageButton.setBackgroundColor(Color.LTGRAY);
            }
        } else {
            if (User.getLoginUser().addPoints(- avatarClicked.getCost())) { // Update points
                updatePointsView();

                // Update avatar purchased
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

                // Update visually
                textViewAvatars[(int) imageButton.getTag()].setText("");
                Toast.makeText(getActivity(), "Purchased", Toast.LENGTH_SHORT).show();
                imageButton.setColorFilter(Color.TRANSPARENT);
                avatarClicked.setAvailable(true);
            } else {
                Toast.makeText(getActivity(), "Too expensive", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Save to server
        new GetUserAsyncTask(EDIT_USER, User.getLoginUser(), null, null,null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {

            }
            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "Error: "+e.getMessage());
            }
        }).execute();
    }

    private void updateListViewTitles() {
        // Build list
        List<String> monitoringList = new ArrayList<>();
        for(Title title: titles){
            if (title.isAvailable()) {
                monitoringList.add(title.getTitle());
            } else {
                monitoringList.add(title.getTitle() + " (LOCKED " + title.getCost() + " PTS)" );
            }
        }

        // Build adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.collection_entry, monitoringList);

        // Configure the list view
        ListView list = view.findViewById(R.id.listViewTitles);
        list.setAdapter(adapter);

        // Update clicks
        registerClickCallbackTitles();

        // Highlight equipped title
        highlightEquippedTitle();
    }

    private void highlightEquippedTitle() {
        if (User.getLoginUser().getCustomization() != null && User.getLoginUser().getCustomization().getTitleEquipped() != -1) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ListView listView = view.findViewById(R.id.listViewTitles);
                    listView.getChildAt(User.getLoginUser().getCustomization().getTitleEquipped()).setBackgroundColor(Color.LTGRAY);
                }
            }, 1);
        }
    }

    private void registerClickCallbackTitles() {
        final ListView listView = view.findViewById(R.id.listViewTitles);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, final int position, long id) {
                if (titles[position].isAvailable()) {

                    // Update title equipped
                    if (User.getLoginUser().getCustomization() == null) {
                        User.getLoginUser().setCustomization(new Customization());
                    }

                    // If already equipped, un-equip. Else equip
                    if (User.getLoginUser().getCustomization().getTitleEquipped() == position) {
                        User.getLoginUser().getCustomization().setTitleEquipped(-1);
                        listView.getChildAt(position).setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        Toast.makeText(getActivity(), "Selected!", Toast.LENGTH_SHORT).show();
                        User.getLoginUser().getCustomization().setTitleEquipped(position);
                        for (int i = 0; i < listView.getChildCount(); i++) {
                            listView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                        }
                        listView.getChildAt(position).setBackgroundColor(Color.LTGRAY);
                    }
                } else {
                    if (User.getLoginUser().addPoints(- titles[position].getCost())) {
                        updatePointsView();

                        // Update title purchased
                        if (User.getLoginUser().getCustomization() == null) {
                            User.getLoginUser().setCustomization(new Customization());
                        }
                        if (User.getLoginUser().getCustomization().getTitleOwned() == null) {
                            int[] titleOwnedUpdate = new int[]{position};
                            User.getLoginUser().getCustomization().setTitleOwned(titleOwnedUpdate);
                        } else {
                            Log.d(TAG, "$$$$ " + Arrays.toString(User.getLoginUser().getCustomization().getTitleOwned()));
                            int[] titleOwned = User.getLoginUser().getCustomization().getTitleOwned();
                            int[] titleOwnedUpdate = new int[titleOwned.length + 1];
                            System.arraycopy(titleOwned, 0, titleOwnedUpdate, 0, titleOwned.length );
                            titleOwnedUpdate[titleOwnedUpdate.length - 1] = position;
                            User.getLoginUser().getCustomization().setTitleOwned(titleOwnedUpdate);
                            Log.d(TAG, "$$$$ " + Arrays.toString(User.getLoginUser().getCustomization().getTitleOwned()));
                        }

                        // Update visually
                        Toast.makeText(getActivity(), "Purchased", Toast.LENGTH_SHORT).show();
                        TextView textView = (TextView) listView.getChildAt(position);
                        textView.setText(titles[position].getTitle());
                        titles[position].setAvailable(true);
                    } else {
                        Toast.makeText(getActivity(), "Too expensive", Toast.LENGTH_SHORT).show();
                    }
                }

                // Save to server
                new GetUserAsyncTask(EDIT_USER, User.getLoginUser(), null, null,null, new OnTaskComplete() {
                    @Override
                    public void onSuccess(Object result) {

                    }
                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, "Error: "+e.getMessage());
                    }
                }).execute();
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
                monitoringList.add(theme.getName() + " (LOCKED " + theme.getCost() + " PTS)" );
            }
        }

        // Build adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.collection_entry, monitoringList);

        // Configure the list view
        ListView list = view.findViewById(R.id.listViewThemes);
        list.setAdapter(adapter);

        // Update clicks
        registerClickCallbackThemes();

        // Highlight equipped title
        highlightEquippedTheme();
    }

    private void highlightEquippedTheme() {
        if (User.getLoginUser().getCustomization() != null && User.getLoginUser().getCustomization().getThemeEquipped() != -1) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ListView listView = view.findViewById(R.id.listViewThemes);
                    listView.getChildAt(User.getLoginUser().getCustomization().getThemeEquipped()).setBackgroundColor(Color.LTGRAY);
                }
            }, 1);
        }
    }

    private void registerClickCallbackThemes() {
        final ListView listView = view.findViewById(R.id.listViewThemes);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, final int position, long id) {
                if (themes[position].isAvailable()) {

                    // Update theme equipped
                    if (User.getLoginUser().getCustomization() == null) {
                        User.getLoginUser().setCustomization(new Customization());
                    }

                    // If already equipped, un-equip. Else equip
                    if (User.getLoginUser().getCustomization().getThemeEquipped() == position) {
                        User.getLoginUser().getCustomization().setThemeEquipped(-1);
                        listView.getChildAt(position).setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        Toast.makeText(getActivity(), "Selected!", Toast.LENGTH_SHORT).show();
                        User.getLoginUser().getCustomization().setThemeEquipped(position);
                        for (int i = 0; i < listView.getChildCount(); i++) {
                            listView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                        }
                        listView.getChildAt(position).setBackgroundColor(Color.LTGRAY);
                    }

                    // Set theme
                    setToolbarTheme();
                } else {
                    if (User.getLoginUser().addPoints(- themes[position].getCost())) {
                        updatePointsView();

                        // Update theme purchased
                        if (User.getLoginUser().getCustomization() == null) {
                            User.getLoginUser().setCustomization(new Customization());
                        }
                        if (User.getLoginUser().getCustomization().getThemeOwned() == null) {
                            int[] themeOwnedUpdate = new int[]{position};
                            User.getLoginUser().getCustomization().setThemeOwned(themeOwnedUpdate);
                        } else {
                            Log.d(TAG, "$$$$ " + Arrays.toString(User.getLoginUser().getCustomization().getThemeOwned()));
                            int[] themeOwned = User.getLoginUser().getCustomization().getThemeOwned();
                            int[] themeOwnedUpdate = new int[themeOwned.length + 1];
                            System.arraycopy(themeOwned, 0, themeOwnedUpdate, 0, themeOwned.length );
                            themeOwnedUpdate[themeOwnedUpdate.length - 1] = position;
                            User.getLoginUser().getCustomization().setThemeOwned(themeOwnedUpdate);
                            Log.d(TAG, "$$$$ " + Arrays.toString(User.getLoginUser().getCustomization().getThemeOwned()));
                        }

                        // Update visually
                        Toast.makeText(getActivity(), "Purchased", Toast.LENGTH_SHORT).show();
                        TextView textView = (TextView) listView.getChildAt(position);
                        textView.setText(themes[position].getName());
                        themes[position].setAvailable(true);
                    } else {
                        Toast.makeText(getActivity(), "Too expensive", Toast.LENGTH_SHORT).show();
                    }
                }

                // Save to server
                new GetUserAsyncTask(EDIT_USER, User.getLoginUser(), null, null,null, new OnTaskComplete() {
                    @Override
                    public void onSuccess(Object result) {

                    }
                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, "Error: "+e.getMessage());
                    }
                }).execute();
            }
        });
    }

    public static void setToolbarTheme() {
        ColorDrawable colorDrawable;
        android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) MainActivity.getContextOfApplication()).getSupportActionBar();
        if (actionBar == null || User.getLoginUser().getCustomization() == null) {
            return;
        }
        int themeEquipped = User.getLoginUser().getCustomization().getThemeEquipped();
        if (themeEquipped == 0) {
            colorDrawable = new ColorDrawable(MainActivity.getContextOfApplication().getResources().getColor(R.color.logoBlue));
        } else if (themeEquipped == 1) {
            colorDrawable = new ColorDrawable(MainActivity.getContextOfApplication().getResources().getColor(R.color.green));
        } else if (themeEquipped == 2) {
            colorDrawable = new ColorDrawable(MainActivity.getContextOfApplication().getResources().getColor(R.color.logoYellow));
        } else {
            colorDrawable = new ColorDrawable(MainActivity.getContextOfApplication().getResources().getColor(R.color.transparent));
        }
        actionBar.setBackgroundDrawable(colorDrawable);
    }

    public static int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }

    // TEST

    private void resetUnlocks() {
        int[] avatarOwnedUpdate = new int[] {};
        User.getLoginUser().getCustomization().setAvatarOwned(avatarOwnedUpdate);
        User.getLoginUser().getCustomization().setAvatarEquipped(-1);
        int[] titleOwnedUpdate = new int[] {};
        User.getLoginUser().getCustomization().setTitleOwned(titleOwnedUpdate);
        User.getLoginUser().getCustomization().setTitleEquipped(-1);
        int[] themeOwnedUpdate = new int[] {};
        User.getLoginUser().getCustomization().setThemeOwned(themeOwnedUpdate);
        User.getLoginUser().getCustomization().setThemeEquipped(-1);

        new GetUserAsyncTask(EDIT_USER, User.getLoginUser(), null, null,null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                Toast.makeText(getActivity(), "Unlocks reset", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "Error: "+e.getMessage());
            }
        }).execute();
    }
}