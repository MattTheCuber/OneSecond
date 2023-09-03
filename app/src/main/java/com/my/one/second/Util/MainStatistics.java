package com.my.one.second.Util;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.my.one.second.R;

import androidx.appcompat.widget.TooltipCompat;
import androidx.fragment.app.Fragment;

public class MainStatistics extends Fragment implements View.OnClickListener {
    private TextView aatw_text;
    private TextView aatw3_text;
    private TextView aatw5_text;
    private TextView aatw12_text;
    private TextView aatw50_text;
    private TextView aatw100_text;

    private TextView current_attempts;
    private TextView total_attempts;
    private TextView total_wins;
    private TextView total_special_times;
    private TextView average_time;
    private TextView current_streak;
    private TextView best_streak;
    private TextView aatw;
    private TextView matw;
    private TextView latw;
    private TextView attempts_deviation;
    private TextView time_deviation;
    private TextView aatw3;
    private TextView aatw5;
    private TextView aatw12;
    private TextView aatw50;
    private TextView aatw100;

    private PrefManager prefManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_statistics, container, false);

        prefManager = new PrefManager(getContext());

        aatw_text = view.findViewById(R.id.stat_aatw_text);
        aatw3_text = view.findViewById(R.id.stat_aatw3_text);
        aatw5_text = view.findViewById(R.id.stat_aatw5_text);
        aatw12_text = view.findViewById(R.id.stat_aatw12_text);
        aatw50_text = view.findViewById(R.id.stat_aatw50_text);
        aatw100_text = view.findViewById(R.id.stat_aatw100_text);

        TooltipCompat.setTooltipText(aatw_text,"Average Attempts to Win");
        TooltipCompat.setTooltipText(aatw3_text,"Average Attempts to Win of 3");
        TooltipCompat.setTooltipText(aatw5_text,"Average Attempts to Win of 5");
        TooltipCompat.setTooltipText(aatw12_text,"Average Attempts to Win of 12");
        TooltipCompat.setTooltipText(aatw50_text,"Average Attempts to Win of 50");
        TooltipCompat.setTooltipText(aatw100_text,"Average Attempts to Win of 100");

        current_attempts = view.findViewById(R.id.stat_current_attempts);
        total_attempts = view.findViewById(R.id.stat_total_attempts);
        total_wins = view.findViewById(R.id.stat_total_wins);
        total_special_times = view.findViewById(R.id.stat_total_special_times);
        average_time = view.findViewById(R.id.stat_average_time);
        current_streak = view.findViewById(R.id.stat_current_streak);
        best_streak = view.findViewById(R.id.stat_best_streak);
        aatw = view.findViewById(R.id.stat_aatw);
        matw = view.findViewById(R.id.stat_matw);
        latw = view.findViewById(R.id.stat_latw);
        attempts_deviation = view.findViewById(R.id.stat_attempts_deviation);
        time_deviation = view.findViewById(R.id.stat_time_deviation);
        aatw3 = view.findViewById(R.id.stat_aatw3);
        aatw5 = view.findViewById(R.id.stat_aatw5);
        aatw12 = view.findViewById(R.id.stat_aatw12);
        aatw50 = view.findViewById(R.id.stat_aatw50);
        aatw100 = view.findViewById(R.id.stat_aatw100);

        /*current_attempts.setOnClickListener(this);
        total_attempts.setOnClickListener(this);
        total_wins.setOnClickListener(this);
        total_special_times.setOnClickListener(this);
        average_time.setOnClickListener(this);
        current_streak.setOnClickListener(this);
        best_streak.setOnClickListener(this);
        aatw.setOnClickListener(this);
        matw.setOnClickListener(this);
        latw.setOnClickListener(this);
        attempts_deviation.setOnClickListener(this);
        time_deviation.setOnClickListener(this);
        aatw3.setOnClickListener(this);
        aatw5.setOnClickListener(this);
        aatw12.setOnClickListener(this);
        aatw50.setOnClickListener(this);
        aatw100.setOnClickListener(this);*/

        refreshViews();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stat_current_attempts:
                prefManager.resetAttempts();
                break;
            case R.id.stat_total_attempts:
                prefManager.resetTotalAttempts();
                break;
            case R.id.stat_total_wins:
                prefManager.resetTotalWins();
                break;
            case R.id.stat_total_special_times:
                prefManager.resetTotalSpecialTimes();
                break;
            case R.id.stat_current_streak:
                prefManager.resetCurrentStreak();
                break;
            case R.id.stat_best_streak:
                prefManager.resetBestStreak();
                break;
            case R.id.stat_average_time:
            case R.id.stat_time_deviation:
            case R.id.stat_matw:
            case R.id.stat_latw:
                prefManager.resetTimes();
                break;
            case R.id.stat_aatw:
            case R.id.stat_attempts_deviation:
            case R.id.stat_aatw3:
            case R.id.stat_aatw5:
            case R.id.stat_aatw12:
            case R.id.stat_aatw50:
            case R.id.stat_aatw100:
                prefManager.resetTotalAttempts();
                prefManager.resetTotalWins();
                break;
        }

        refreshViews();
    }

    private void refreshViews() {
        current_attempts.setText(String.valueOf(prefManager.getAttempts()));
        total_attempts.setText(String.valueOf(prefManager.getTotalAttempts()));
        total_wins.setText(String.valueOf(prefManager.getTotalWins()));
        total_special_times.setText(String.valueOf(prefManager.getTotalSpecialTimes()));
        if (prefManager.getTotalAttempts() > 0) {
            String mili = String.valueOf(prefManager.getAverageTime());
            String result = "---";
            if (mili.length() == 1) {
                result = "00.0" + mili;
            } else if (mili.length() == 2) {
                result = "00." + mili;
            } else if (mili.length() == 3) {
                result = "0" + mili.substring(0, 1) + "." + mili.substring(1,3);
            } else if (mili.length() == 4) {
                result = mili.substring(0, 2) + "." + mili.substring(2, 4);
            } else {
                result = "---";
            }
            average_time.setText(result);
        } else {
            average_time.setText("---");
        }
        current_streak.setText(String.valueOf(prefManager.getCurrentStreak()));
        best_streak.setText(String.valueOf(prefManager.getBestStreak()));
        if (prefManager.getAverageAverageAttemptsToWin() > 0) aatw.setText(String.valueOf(prefManager.getAverageAverageAttemptsToWin()));
        if (prefManager.getMostAttemptsToWin() > 0) matw.setText(String.valueOf(prefManager.getMostAttemptsToWin()));
        if (prefManager.getLeastAttemptsToWin() > 0) latw.setText(String.valueOf(prefManager.getLeastAttemptsToWin()));
        if (prefManager.getAttemptsDeviation() > 0) attempts_deviation.setText(String.format("%.2f", prefManager.getAttemptsDeviation()));
        if (prefManager.getTimeDeviation() > 0) time_deviation.setText(String.format("%.2f", prefManager.getTimeDeviation()));
        if (prefManager.getAverageAverageAttemptsToWinOf3() > 0) aatw3.setText(String.valueOf(prefManager.getAverageAverageAttemptsToWinOf3()));
        if (prefManager.getAverageAverageAttemptsToWinOf5() > 0) aatw5.setText(String.valueOf(prefManager.getAverageAverageAttemptsToWinOf5()));
        if (prefManager.getAverageAverageAttemptsToWinOf12() > 0) aatw12.setText(String.valueOf(prefManager.getAverageAverageAttemptsToWinOf12()));
        if (prefManager.getAverageAverageAttemptsToWinOf50() > 0) aatw50.setText(String.valueOf(prefManager.getAverageAverageAttemptsToWinOf50()));
        if (prefManager.getAverageAverageAttemptsToWinOf100() > 0) aatw100.setText(String.valueOf(prefManager.getAverageAverageAttemptsToWinOf100()));
    }
}