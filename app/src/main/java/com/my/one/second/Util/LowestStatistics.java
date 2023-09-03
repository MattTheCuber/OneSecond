package com.my.one.second.Util;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.my.one.second.R;

import androidx.fragment.app.Fragment;

public class LowestStatistics extends Fragment implements View.OnClickListener {
    private TextView lowest_time;
    private TextView average_time;
    private TextView time_deviation;
    private TextView total_special_time;

    private PrefManager prefManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lowest_statistics, container, false);

        prefManager = new PrefManager(getContext());

        lowest_time = view.findViewById(R.id.stat2_lowest_time);
        average_time = view.findViewById(R.id.stat2_average_time);
        time_deviation = view.findViewById(R.id.stat2_time_deviation);
        total_special_time = view.findViewById(R.id.stat2_total_special_times);

        /*lowest_time.setOnClickListener(this);
        average_time.setOnClickListener(this);
        time_deviation.setOnClickListener(this);
        total_special_time.setOnClickListener(this);*/

        refreshViews();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stat2_lowest_time:
                prefManager.resetBestTime();
                break;
            case R.id.stat2_average_time:
            case R.id.stat2_time_deviation:
                prefManager.resetTimesLowest();
                break;
            case R.id.stat2_total_special_times:
                prefManager.resetTotalSpecialTimesLowest();
                break;
        }

        refreshViews();
    }

    private void refreshViews() {
        lowest_time.setText(String.valueOf(prefManager.getBestTime()));
        if (prefManager.getAverageTimeLowest() > 0) average_time.setText(String.valueOf(prefManager.getAverageTimeLowest()));
        if (prefManager.getTimeDeviationLowest() > 0) time_deviation.setText(String.format("%.2f", prefManager.getTimeDeviationLowest()));
        total_special_time.setText(String.valueOf(prefManager.getTotalSpecialTimesLowest()));
    }
}