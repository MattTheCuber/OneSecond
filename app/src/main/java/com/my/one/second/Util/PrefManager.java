package com.my.one.second.Util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "one_second";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_SIGNED_IN = "IsSignedIn";
    private static final String TIMES = "Times";
    private static final String TIMES_LOWEST = "TimesLowest";
    private static final String ATTEMPTS = "Attempts";
    private static final String ATTEMPTS_TO_WIN = "AttemptsToWin";
    private static final String BEST_TIME = "BestTime";
    private static final String FAILED_SIGNINS = "FailedSignins";

    private static final String TOTAL_ATTEMPTS = "TotalAttempts";
    private static final String TOTAL_WINS = "TotalWins";
    private static final String TOTAL_SPECIAL_TIMES = "TotalSpecialTimes";
    private static final String TOTAL_SPECIAL_TIMES_LOWEST = "TotalSpecialTimesLowest";
    private static final String MOST_ATTEMPTS_TO_WIN = "MostAttemptsToWin";
    private static final String LEAST_ATTEMPTS_TO_WIN = "LeastAttemptsToWin";
    private static final String CURRENT_STREAK = "CurrentStreak";
    private static final String BEST_STREAK = "BestStreak";
    //private static final String  = "";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }
    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setSignedIn(boolean isSignedIn) {
        editor.putBoolean(IS_SIGNED_IN, isSignedIn);
        editor.commit();
    }
    public boolean isSignedIn() {
        return pref.getBoolean(IS_SIGNED_IN, true);
    }

    public void addTime(String time) {
        ArrayList<String> times = getTimes();
        times.add(String.valueOf(time));
        StringBuilder result = new StringBuilder();
        int arrayLength = times.size() - 1;
        for (int i = 0; i < arrayLength; i++) {
            result.append(times.get(i)).append(",");
        }
        result.append(times.get(arrayLength));

        editor.putString(TIMES, result.toString());
        editor.commit();
    }
    public void resetTimes() {
        editor.remove(TIMES).commit();
    }
    public ArrayList<String> getTimes() {
        ArrayList<String> times = new ArrayList<String>();
        String timesRaw = pref.getString(TIMES, "");
        for (String s : timesRaw.split(",")) {
            if (!s.isEmpty()) {
                times.add(s);
            }
        }

        return times;
    }

    public void addTimeLowest(String time) {
        ArrayList<String> times = getTimes();
        times.add(String.valueOf(time));
        StringBuilder result = new StringBuilder();
        int arrayLength = times.size() - 1;
        for (int i = 0; i < arrayLength; i++) {
            result.append(times.get(i)).append(",");
        }
        result.append(times.get(arrayLength));

        editor.putString(TIMES_LOWEST, result.toString());
        editor.commit();
    }
    public void resetTimesLowest() {
        editor.remove(TIMES_LOWEST).commit();
    }
    public ArrayList<String> getTimesLowest() {
        ArrayList<String> times = new ArrayList<String>();
        String timesRaw = pref.getString(TIMES_LOWEST, "");
        for (String s : timesRaw.split(",")) {
            if (!s.isEmpty()) {
                times.add(s);
            }
        }

        return times;
    }

    public void addAttemptsToWin() {
        ArrayList<String> attempts = getAttemptsToWin();
        attempts.add(String.valueOf(getAttempts()));
        StringBuilder result = new StringBuilder();
        int arrayLength = attempts.size() - 1;
        for (int i = 0; i < arrayLength; i++) {
            result.append(attempts.get(i)).append(",");
        }
        result.append(attempts.get(arrayLength));

        editor.putString(ATTEMPTS_TO_WIN, result.toString());
        editor.commit();
    }
    public void resetAttemptsToWin() {
        editor.remove(ATTEMPTS_TO_WIN).commit();
    }
    public ArrayList<String> getAttemptsToWin() {
        ArrayList<String> attempts = new ArrayList<String>();
        String attemptsRaw = pref.getString(ATTEMPTS_TO_WIN, "");
        for (String s : attemptsRaw.split(",")) {
            if (!s.isEmpty()) {
                attempts.add(s);
            }
        }

        return attempts;
    }

    public void addAttempts() {
        int currentAttempts = getAttempts() + 1;
        editor.putInt(ATTEMPTS, currentAttempts);
        editor.commit();

        addTotalAttempts();
    }
    public void resetAttempts() {
        editor.remove(ATTEMPTS).commit();
    }
    public int getAttempts() {
        return pref.getInt(ATTEMPTS, 0);
    }

    public void setBestTime(int new_best) {
        editor.putInt(BEST_TIME, new_best);
        editor.commit();
    }
    public void resetBestTime() {
        editor.remove(BEST_TIME).commit();
    }
    public int getBestTime() {
        return pref.getInt(BEST_TIME, 0);
    }

    public void addFailedSignins() {
        int totalFails = getFailedSignins() + 1;
        editor.putInt(FAILED_SIGNINS, totalFails);
        editor.commit();
    }
    public void resetFailedSignins() {
        editor.remove(FAILED_SIGNINS).commit();
    }
    public int getFailedSignins() {
        return pref.getInt(FAILED_SIGNINS, 0);
    }

    public void addTotalAttempts() {
        int totalAttempts = getTotalAttempts() + 1;
        editor.putInt(TOTAL_ATTEMPTS, totalAttempts);
        editor.commit();
    }
    public void resetTotalAttempts() {
        editor.remove(TOTAL_ATTEMPTS).commit();
    }
    public int getTotalAttempts() {
        return pref.getInt(TOTAL_ATTEMPTS, 0);
    }

    public void addTotalWins() {
        int totalWins = getTotalWins() + 1;
        editor.putInt(TOTAL_WINS, totalWins);
        editor.commit();
    }
    public void resetTotalWins() {
        editor.remove(TOTAL_WINS).commit();
    }
    public int getTotalWins() {
        return pref.getInt(TOTAL_WINS, 0);
    }

    public void addTotalSpecialTimes() {
        int totalSpecialTimes = getTotalSpecialTimes() + 1;
        editor.putInt(TOTAL_SPECIAL_TIMES, totalSpecialTimes);
        editor.commit();
    }
    public void resetTotalSpecialTimes() {
        editor.remove(TOTAL_SPECIAL_TIMES).commit();
    }
    public int getTotalSpecialTimes() {
        return pref.getInt(TOTAL_SPECIAL_TIMES, 0);
    }

    public void addTotalSpecialTimesLowest() {
        int totalSpecialTimes = getTotalSpecialTimesLowest() + 1;
        editor.putInt(TOTAL_SPECIAL_TIMES_LOWEST, totalSpecialTimes);
        editor.commit();
    }
    public void resetTotalSpecialTimesLowest() {
        editor.remove(TOTAL_SPECIAL_TIMES_LOWEST).commit();
    }
    public int getTotalSpecialTimesLowest() {
        return pref.getInt(TOTAL_SPECIAL_TIMES_LOWEST, 0);
    }

    public int getAverageTime() {
        ArrayList<String> times = getTimes();
        int average = 0;
        int arrayLength = times.size();
        if (arrayLength > 0) {
            for (int i = 0; i < arrayLength; i++) {
                average += Integer.parseInt(times.get(i));
            }
            average = average / arrayLength;

            return average;
        } else {
            return 0;
        }
    }

    public int getAverageTimeLowest() {
        ArrayList<String> times = getTimesLowest();
        int average = 0;
        int arrayLength = times.size();
        if (arrayLength > 0) {
            for (int i = 0; i < arrayLength; i++) {
                average += Integer.parseInt(times.get(i));
            }
            average = average / arrayLength;

            return average;
        } else {
            return 0;
        }
    }

    public void addCurrentStreak() {
        editor.putInt(CURRENT_STREAK, getCurrentStreak() + 1);
        editor.commit();

        if (getBestStreak() < getCurrentStreak()) {
            setBestStreak(getCurrentStreak());
        }
    }
    public void resetCurrentStreak() {
        editor.remove(CURRENT_STREAK).commit();
    }
    public int getCurrentStreak() {
        return pref.getInt(CURRENT_STREAK, 0);
    }

    public void setBestStreak(int streak) {
        editor.putInt(BEST_STREAK, streak);
        editor.commit();
    }
    public void resetBestStreak() {
        editor.remove(BEST_STREAK).commit();
    }
    public int getBestStreak() {
        return pref.getInt(BEST_STREAK, 0);
    }

    public int getAverageAverageAttemptsToWin() {
        ArrayList<String> attempts = getAttemptsToWin();
        int average = 0;
        int arrayLength = attempts.size();
        if (arrayLength > 0) {
            for (int i = 0; i < arrayLength; i++) {
                average += Integer.parseInt(attempts.get(i));
            }
            average = average / arrayLength;

            return average;
        } else {
            return 0;
        }
    }

    public void setMostAttemptsToWin() {
        if (getAttempts() > getMostAttemptsToWin()) {
            editor.putInt(MOST_ATTEMPTS_TO_WIN, getAttempts());
            editor.commit();
        }
    }
    public void resetMostAttemptsToWin() {
        editor.remove(MOST_ATTEMPTS_TO_WIN).commit();
    }
    public int getMostAttemptsToWin() {
        return pref.getInt(MOST_ATTEMPTS_TO_WIN, 0);
    }

    public void setLeastAttemptsToWin() {
        if ((getAttempts() < getLeastAttemptsToWin()) || (getLeastAttemptsToWin() == 0)) {
            editor.putInt(LEAST_ATTEMPTS_TO_WIN, getAttempts());
            editor.commit();
        }
    }
    public void resetLeastAttemptsToWin() {
        editor.remove(LEAST_ATTEMPTS_TO_WIN).commit();
    }

    public int getLeastAttemptsToWin() {
        return pref.getInt(LEAST_ATTEMPTS_TO_WIN, 0);
    }

    public double getAttemptsDeviation() {
        ArrayList<String> attemptsString = getAttemptsToWin();
        ArrayList<Integer> attemptsInt = new ArrayList<>();
        for (int i = 0; i < attemptsString.size(); i++) {
            attemptsInt.add(Integer.parseInt(attemptsString.get(i)));
        }
        return calculateStandardDeviation(attemptsInt);
    }

    public double getTimeDeviation() {
        ArrayList<String> timesString = getTimes();
        ArrayList<Integer> timesInt = new ArrayList<>();
        for (int i = 0; i < timesString.size(); i++) {
            timesInt.add(Integer.parseInt(timesString.get(i)));
        }
        return calculateStandardDeviation(timesInt);
    }

    public double getTimeDeviationLowest() {
        ArrayList<String> timesString = getTimesLowest();
        ArrayList<Integer> timesInt = new ArrayList<>();
        for (int i = 0; i < timesString.size(); i++) {
            timesInt.add(Integer.parseInt(timesString.get(i)));
        }
        return calculateStandardDeviation(timesInt);
    }

    public int getAverageAverageAttemptsToWinOf3() {
        ArrayList<String> attempts = getAttemptsToWin();
        int average = 0;
        int arrayLength = attempts.size();
        if (arrayLength > 2) {
            for (int i = arrayLength - 3; i < arrayLength; i++) {
                average += Integer.parseInt(attempts.get(i));
            }
            average = average / 3;

            return average;
        } else {
            return 0;
        }
    }

    public int getAverageAverageAttemptsToWinOf5() {
        ArrayList<String> attempts = getAttemptsToWin();
        int average = 0;
        int arrayLength = attempts.size();
        if (arrayLength > 4) {
            for (int i = arrayLength - 5; i < arrayLength; i++) {
                average += Integer.parseInt(attempts.get(i));
            }
            average = average / 5;

            return average;
        } else {
            return 0;
        }
    }

    public int getAverageAverageAttemptsToWinOf12() {
        ArrayList<String> attempts = getAttemptsToWin();
        int average = 0;
        int arrayLength = attempts.size();
        if (arrayLength > 11) {
            for (int i = arrayLength - 12; i < arrayLength; i++) {
                average += Integer.parseInt(attempts.get(i));
            }
            average = average / 12;

            return average;
        } else {
            return 0;
        }
    }

    public int getAverageAverageAttemptsToWinOf50() {
        ArrayList<String> attempts = getAttemptsToWin();
        int average = 0;
        int arrayLength = attempts.size();
        if (arrayLength > 49) {
            for (int i = arrayLength - 50; i < arrayLength; i++) {
                average += Integer.parseInt(attempts.get(i));
            }
            average = average / 50;

            return average;
        } else {
            return 0;
        }
    }

    public int getAverageAverageAttemptsToWinOf100() {
        ArrayList<String> attempts = getAttemptsToWin();
        int average = 0;
        int arrayLength = attempts.size();
        if (arrayLength > 99) {
            for (int i = arrayLength - 100; i < arrayLength; i++) {
                average += Integer.parseInt(attempts.get(i));
            }
            average = average / 100;

            return average;
        } else {
            return 0;
        }
    }

    public static double calculateStandardDeviation(ArrayList<Integer> sd) {
        double sum = 0;
        double newSum = 0;

        for (int i = 0; i < sd.size(); i++) {
            sum = sum + sd.get(i);
        }
        double mean = (sum) / (sd.size());

        for (int j = 0; j < sd.size(); j++) {
            newSum = newSum + ((sd.get(j) - mean) * (sd.get(j) - mean));
        }
        double squaredDiffMean = (newSum) / (sd.size());
        double standardDev = (Math.sqrt(squaredDiffMean));

        return standardDev;
    }
}