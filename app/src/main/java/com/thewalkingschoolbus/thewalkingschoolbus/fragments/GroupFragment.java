package com.thewalkingschoolbus.thewalkingschoolbus.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thewalkingschoolbus.thewalkingschoolbus.R;

import java.util.ArrayList;
import java.util.List;

public class GroupFragment extends Fragment {

    private static final String TAG = "GroupFragment";
    private View view;

    /**
     * SectionsPageAdapter
     * Used to build tabs.
     * From "Android Tab Tutorial" by "CodingWithMitch"
     */
    private class SectionsPageAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        public SectionsPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        view = inflater.inflate(R.layout.fragment_group, container, false);

        // Set up the viewPager with the sections adapter.
        ViewPager viewPager = view.findViewById(R.id.containerGroup);
        setupViewPager(viewPager);

        TabLayout tabs = view.findViewById(R.id.tabsGroup);
        tabs.setupWithViewPager(viewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getChildFragmentManager());
        adapter.addFragment(new GroupMemberFragment(), "Member");
        adapter.addFragment(new GroupLeaderFragment(), "Leader");
        viewPager.setAdapter(adapter);
    }
}
