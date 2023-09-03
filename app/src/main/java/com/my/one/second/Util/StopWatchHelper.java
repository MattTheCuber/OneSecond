package com.my.one.second.Util;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.widget.Toast;

import com.my.one.second.LowestActivity;
import com.my.one.second.MainActivity;
import com.my.one.second.R;

import java.util.Timer;
import java.util.TimerTask;

public class StopWatchHelper {
    private Timer timer = new Timer();
    private TimerTask timerTask;
    private Toast toastMessage;
    private StopWatch stopwatch = new StopWatch();

    private MainActivity mainActivity;
    private LowestActivity lowestActivity;
    private Context context;

    private int best;
    private int attempts = 0;
    private boolean just_won = false;

    public void setMainActivity(MainActivity newMainActivity) {
        mainActivity = newMainActivity;
    }

    public void setLowestActivity(LowestActivity newLowestActivity) {
        lowestActivity = newLowestActivity;
    }

    public void setBest(int newBest) {
        best = newBest;
    }

    public void setContext(Context newContext) {
        context = newContext;
    }
    public void setAttempts(int newAttempts) {
        attempts = newAttempts;
        mainActivity.changeTextViewText("attempts_view", String.valueOf(attempts));
    }

    public void startStop(int code) {
        if (stopwatch.isPaused()) {
            if (code == 0) {
                if (!mainActivity.getTextViewText("stopwatch_view").equals("60:00")) {
                    resume(code);
                    mainActivity.fadeButtonOut();
                } else {
                    showToast("Too high, please reset", "long", code);
                }
            } else if (code == 1) {
                if (!lowestActivity.getTextViewText("stopwatch_view").equals("5000")) {
                    resume(code);
                    lowestActivity.fadeButtonOut();
                } else {
                    showToast("Too high, please reset", "long", code);
                }
            }
        } else {
            if (stopwatch.isRunning()) {
                if (code == 0) {
                    mainActivity.fadeButtonIn();
                } else if (code == 1) {
                    lowestActivity.fadeButtonIn();
                }
                timerTask.cancel();
                String activity = "";
                String current = "";
                if (code == 0) {
                    current = stopwatch.getSecondsAndMili();
                    stopwatch.pause();
                    activity = pause(code, current);
                } else if (code == 1) {
                    current = String.valueOf(stopwatch.getElapsedTimeMili());
                    stopwatch.pause();
                    activity = pause(code, current);
                }
                updateView(activity, current);
            } else {
                if (code == 0) {
                    mainActivity.fadeButtonOut();
                } else if (code == 1) {
                    lowestActivity.fadeButtonOut();
                }
                stopwatch.start();
                start(code);
            }
        }
    }

    private void updateView(String activity, final String current) {
        String currant = "01.00";
        String curent = "6";
        if (activity.equals("main")) {
            TimerTask newTimerTask = new TimerTask() {
                @Override
                public void run() {
                    mainActivity.changeTextViewText("stopwatch_view", current);//currant
                }
            };
            Timer newTimer = new Timer();
            newTimer.schedule(newTimerTask, 7);
        } else if (activity.equals("lowest")) {
            TimerTask newTimerTask = new TimerTask() {
                @Override
                public void run() {
                    lowestActivity.changeTextViewText("stopwatch_view", current);//curent
                }
            };
            Timer newTimer = new Timer();
            newTimer.schedule(newTimerTask, 4);
        }
    }

    private String pause(int code, String current) {
        String currant = "01.00";
        String curent = "18";
        if (code == 0) {
            attempts++;
            saveAttempts(code, current);

            switch (current) {
                case "01.00":
                    if (attempts == 1) {
                        achieve(mainActivity.getString(R.string.achievement_first_try), code, 0);
                        showToast("First Try!", "long", code);
                    } else if (attempts > 49) {
                        achieve(mainActivity.getString(R.string.achievement_never_give_up), code, 0);
                        showToast("Persistence is the key to success", "long", code);
                    } else {
                        showToast("You win!", "long", code);
                    }
                    just_won = true;
                    achieve(mainActivity.getString(R.string.achievement_1), code, 1);
                    mainActivity.changeBg("green");
                    mainActivity.increment(mainActivity.getString(R.string.achievement_beginner), 1);
                    mainActivity.increment(mainActivity.getString(R.string.achievement_proficient), 1);
                    mainActivity.increment(mainActivity.getString(R.string.achievement_expert), 1);
                    playWin(code);
                    break;
                case "05.00":
                    achieve(mainActivity.getString(R.string.achievement_5), code, 2);
                    showToast("You win! Wait, nevermind.", "long", code);
                    playWin(code);
                    break;
                case "10.00":
                    achieve(mainActivity.getString(R.string.achievement_10), code, 2);
                    showToast("You win! Wait, nevermind.", "long", code);
                    playWin(code);
                    break;
                case "30.00":
                    achieve(mainActivity.getString(R.string.achievement_30), code, 2);
                    showToast("You win! Wait, nevermind.", "long", code);
                    playWin(code);
                    break;
                case "12.34":
                    achieve(mainActivity.getString(R.string.achievement_1234), code, 2);
                    showToast("You win! Wait, nevermind.", "long", code);
                    playWin(code);
                    break;
                case "03.14":
                    achieve(mainActivity.getString(R.string.achievement_pi), code, 2);
                    showToast("Happy pi day!", "long", code);
                    playWin(code);
                    break;
                case "02.71":
                    achieve(mainActivity.getString(R.string.achievement_eulers_number), code, 2);
                    showToast("Try to get 1 second, not Euler’s Number", "long", code);
                    playWin(code);
                    break;
                case "01.61":
                    achieve(mainActivity.getString(R.string.achievement_golden_ratio), code, 2);
                    showToast("Wow! You found the golden ratio!", "long", code);
                    playWin(code);
                    break;
                case "03.16":
                    achieve(mainActivity.getString(R.string.achievement_john_316), code, 2);
                    showToast("For God so loved the world, that he gave his only begotten Son, that whosoever believeth in him should not perish, but have everlasting life. - John 3:16", "long", code);
                    new CountDownTimer(9000, 1000) {
                        public void onTick(long millisUntilFinished) {toastMessage.show();}
                        public void onFinish() {toastMessage.show();}
                    }.start();
                    playWin(code);
            }

            mainActivity.changeTextViewText("attempts_view", String.valueOf(attempts));
            mainActivity.changeTextViewText("stopwatch_view", current);

            return "main";
        } else if (code == 1) {
            saveAttempts(code, current);
            if ((Integer.parseInt(current) < best) || (best == 0)) {
                if (Integer.parseInt(current) > 5) {
                    showToast("New record: " + current, "long", code);
                    just_won = true;
                    best = Integer.parseInt(current);

                    lowestActivity.setBest(current);
                    lowestActivity.changeBg("green");
                } else {
                    current = "6";
                    showToast("New record: " + current, "long", code);
                    just_won = true;
                    best = Integer.parseInt(current);

                    lowestActivity.setBest(current);
                    lowestActivity.changeBg("green");
                }
                playWin(code);
            }
            if (Integer.parseInt(current) == 1234) {
                achieve(lowestActivity.getString(R.string.achievement_1234), code, 3);
                showToast("1234, nice!", "long", code);
                playWin(code);
            } else if (Integer.parseInt(current) == 666) {
                achieve(lowestActivity.getString(R.string.achievement_666), code, 3);
                showToast("Number of the Beast!", "long", code);
                just_won = true;
                lowestActivity.changeBg("red");
                playWin(code);
            } else if (Integer.parseInt(current) == 911) {
                achieve(lowestActivity.getString(R.string.achievement_911), code, 3);
                showToast("911, what's your emergency?", "long", code);
                playWin(code);
            }
            if (Integer.parseInt(current) < 201) {
                achieve(lowestActivity.getString(R.string.achievement_under_200), code, 0);
                if (Integer.parseInt(current) < 101) {
                    achieve(lowestActivity.getString(R.string.achievement_under_100), code, 0);
                    if (Integer.parseInt(current) < 76) {
                        achieve(lowestActivity.getString(R.string.achievement_under_75), code, 0);
                        if (Integer.parseInt(current) < 51) {
                            achieve(lowestActivity.getString(R.string.achievement_under_50), code, 0);
                            if (Integer.parseInt(current) < 26) {
                                achieve(lowestActivity.getString(R.string.achievement_under_25), code, 0);
                            }
                        }
                    }
                }
            }

            lowestActivity.changeTextViewText("stopwatch_view", current);
            lowestActivity.updateBestTime((Integer.parseInt(current)), code);

            return "lowest";
        }
        return "Error";
    }

    private void saveAttempts(int code, String time) {
        if (code == 0) {
            mainActivity.saveAttempt(time);
        } else if (code == 1) {
            lowestActivity.saveScore(time);
        }
    }

    private void resetAttempts() {
        mainActivity.resetAttempts();
    }

    private void achieve(String achievement, int code, int type) {
        if (code == 0) {
            mainActivity.achieve(achievement, type);
        } else if (code == 1) {
            lowestActivity.achieve(achievement, type);
        }
    }

    private void resume(int code) {
        stopwatch.resume();
        start(code);
    }

    public void reset(int code) {
        if (stopwatch.hasRun()) {
            if (!stopwatch.isRunning()) {
                timerTask.cancel();
                stopwatch.stop();

                if (code == 0) {
                    if (just_won) {
                        attempts = 0;
                        resetAttempts();
                        mainActivity.changeTextViewText("attempts_view", String.valueOf(attempts));
                        mainActivity.changeBg("transparent");
                        just_won = false;
                    }
                    mainActivity.changeTextViewText("stopwatch_view", "00.00");
                } else if (code == 1) {
                    if (just_won) {
                        lowestActivity.changeTextViewText("best_view", String.valueOf(best));
                        lowestActivity.changeBg("transparent");
                        just_won = false;
                    }
                    lowestActivity.changeTextViewText("stopwatch_view", "00");
                }

            } else {
                if (code == 0) {
                    showToast("You need to stop the timer first.", "short", code);
                } else if (code == 1) {
                    showToast("You need to stop the timer first.", "short", code);
                }
            }
        } else {
            if (code == 0) {
                showToast("You need to start the timer first.", "short", code);
            } else if (code == 1) {
                showToast("You need to start the timer first.", "short", code);
            }
        }
    }

    private void start(final int code) {
        if (code == 0) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    String current = stopwatch.getSecondsAndMili();
                    if (stopwatch.getElapsedTimeMili() > 59989) {
                        timerTask.cancel();
                        stopwatch.pause();
                        pause(code, current);

                        showToast("Too high, please reset", "short", code);
                        mainActivity.changeTextViewText("stopwatch_view", "60.00");
                        mainActivity.fadeButtonIn();
                        achieve(mainActivity.getString(R.string.achievement_you_forgot_to_finish_1), code, 0);

                        return;
                    }

                    if (stopwatch.isRunning()) {
                        mainActivity.changeTextViewText("stopwatch_view", current);
                    }
                }
            };
            timer.scheduleAtFixedRate(timerTask, 0, 10);
            mainActivity.changeBg("transparent" );
        } else if (code == 1) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    String current = String.valueOf(stopwatch.getElapsedTimeMili());
                    if (stopwatch.getElapsedTimeMili() > 4999) {
                        timerTask.cancel();
                        stopwatch.pause();
                        pause(code, current);

                        showToast("Too high, please reset", "short", code);
                        lowestActivity.changeTextViewText("stopwatch_view", "5000");
                        lowestActivity.fadeButtonIn();
                        achieve(lowestActivity.getString(R.string.achievement_you_forgot_to_finish_2), code, 0);

                        return;
                    }

                    if (stopwatch.isRunning()) {
                        lowestActivity.changeTextViewText("stopwatch_view", current);
                    }
                }
            };
            timer.scheduleAtFixedRate(timerTask, 0, 1);
            lowestActivity.changeBg("transparent");
        }
    }

    private void showToast(String st, String length, int code){
        final String message = st;
        if (code == 0) {
            if (length.equals("long")) {
                mainActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        toastMessage = Toast.makeText(mainActivity, message, Toast.LENGTH_LONG);
                        toastMessage.show();
                    }
                });
            } else {
                mainActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        toastMessage = Toast.makeText(mainActivity, message, Toast.LENGTH_SHORT);
                        toastMessage.show();
                    }
                });
            }
            MediaPlayer mp = MediaPlayer.create(mainActivity, R.raw.grunz_success);
            mp.start();
            //mainActivity.showToast(st, length);
        } else if (code == 1) {
            if (length.equals("long")) {
                lowestActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        toastMessage = Toast.makeText(lowestActivity, message, Toast.LENGTH_LONG);
                        toastMessage.show();
                    }
                });
            } else {
                lowestActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        toastMessage = Toast.makeText(lowestActivity, message, Toast.LENGTH_SHORT);
                        toastMessage.show();
                    }
                });
            }
            //lowestActivity.showToast(st, length);
        }
    }

    private void playWin(int code) {
        MediaPlayer mp;
        if (code == 0) {
            mp = MediaPlayer.create(mainActivity, R.raw.grunz_success);
        } else {
            mp = MediaPlayer.create(lowestActivity, R.raw.grunz_success);
        }
        if(mp.isPlaying()) {
            mp.pause();
        }
        mp.start();
    }

    public boolean isRunning() { return stopwatch.isRunning(); }
}
