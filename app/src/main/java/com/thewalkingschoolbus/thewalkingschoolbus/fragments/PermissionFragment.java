package com.thewalkingschoolbus.thewalkingschoolbus.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thewalkingschoolbus.thewalkingschoolbus.R;

public class PermissionFragment extends android.app.Fragment {

    private static final String TAG = "PermissionFragment";
    private View view;

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

    }
}
