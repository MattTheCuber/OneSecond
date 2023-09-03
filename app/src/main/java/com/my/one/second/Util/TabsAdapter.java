package com.my.one.second.Util;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabsAdapter extends FragmentStateAdapter {

    public TabsAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MainStatistics();
            case 1:
                return new LowestStatistics();
            default:
                return new MainStatistics();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}