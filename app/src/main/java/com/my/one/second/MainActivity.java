package com.my.one.second;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.leaderboard.Leaderboards;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.my.one.second.Util.PrefManager;
import com.my.one.second.Util.StopWatchHelper;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private StopWatchHelper stopWatchHelper;
    private AlertDialog.Builder google_play_dialog;
    private Intent lowest = new Intent();
    private Boolean cancelledSignIn = false;
    private PrefManager prefManager;
    private Toolbar toolbar;

    private TextView stopwatch_view;
    private TextView attempts_view;
    private LinearLayout bg;
    private Button reset_btn;
    String resultString = "";

    private boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(this) != null;
    }
    GoogleSignInAccount signedInAccount;
    private static final int RC_LEADERBOARD_UI = 9004;
    private static final int RC_ACHIEVEMENT_UI = 9003;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "MV-OS";

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.main);

        prefManager = new PrefManager(this);
        if (prefManager.isFirstTimeLaunch()) {
            Intent i = new Intent(getApplicationContext(), IntroActivity.class);
            startActivity(i);
            finish();
        } else {
            toolbar = findViewById(R.id.customToolbar);
            google_play_dialog = new AlertDialog.Builder(this);
            Button start_stop_btn = findViewById(R.id.start_stop_btn);
            reset_btn = findViewById(R.id.reset_btn);
            Button google_play_btn = findViewById(R.id.google_play_btn);
            stopwatch_view = findViewById(R.id.stopwatch_view);
            attempts_view = findViewById(R.id.attempts_view);
            bg = findViewById(R.id.bg);

            reset_btn.setOnClickListener(this);
            google_play_btn.setOnClickListener(this);
            start_stop_btn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        stopWatchHelper.startStop(0);
                        MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.click1);
                        mp.start();
                    }
                    return true;
                }
            });

            stopWatchHelper = new StopWatchHelper();
            stopWatchHelper.setContext(this);
            stopWatchHelper.setMainActivity(this);

            google_play_dialog.setTitle("Google Play Games");
            google_play_dialog.setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface _dialog, int _which) {
                    prefManager.resetFailedSignins();
                    signInSilently();
                }
            });
            google_play_dialog.setNeutralButton("Achievements", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface _dialog, int _which) {
                    showAchievements();
                }
            });
            google_play_dialog.setNegativeButton("Leaderboard", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface _dialog, int _which) {
                    showLeaderboard();
                }
            });

            lowest.setClass(getApplicationContext(), LowestActivity.class);

            if (prefManager.isSignedIn()) {
                if (prefManager.getFailedSignins() > 12) {
                    Toast.makeText(getApplicationContext(), "Auto sign in disabled due to too many failed attempts. Sign in through the menu to enable auto sign in.", Toast.LENGTH_LONG).show();
                } else {
                    signInSilently();
                }
                if (GoogleSignIn.getLastSignedInAccount(this) != null) {
                    GamesClient gamesClient = Games.getGamesClient(MainActivity.this, GoogleSignIn.getLastSignedInAccount(this));
                    gamesClient.setViewForPopups(findViewById(R.id.gps_popup));
                }
            }

            setSupportActionBar(toolbar);
            stopWatchHelper.setAttempts(prefManager.getAttempts());
            super.onRestart();
        }
    }

    public void fadeButtonOut() {
        try {
            reset_btn.animate().alpha(0.5f).setDuration(200);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void fadeButtonIn() {
        try {
            reset_btn.animate().alpha(1).setDuration(200);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        TextView tv = new TextView(this);
        tv.setText(R.string.mode_colen);
        tv.setTextColor(getResources().getColor(R.color.white));
        menu.add(0, 0, 0, "Mode:").setActionView(tv).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.one_second_btn:
                Toast.makeText(this, R.string.same_gamemode, Toast.LENGTH_LONG).show();
                return true;
            case R.id.lowest_btn:
                startActivity(lowest);
                return true;
            case R.id.statistics_btn:
                Intent stat = new Intent(getApplicationContext(), StatisticsActivity.class);
                stat.putExtra("calling-activity", 0);
                startActivity(stat);
                return true;
            case R.id.help_btn:
                Intent help = new Intent(getApplicationContext(), IntroActivity.class);
                startActivity(help);
                return true;
//            case R.id.discord_btn:
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.discord_link)));
//                startActivity(browserIntent);
//                return true;
        }
        return false;
    }

    public void changeBg(String color) {
        switch (color) {
            case "green":
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bg.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_green));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "transparent":
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bg.setBackgroundColor(Color.TRANSPARENT);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                Toast.makeText(this, "Error: 1x80-100", Toast.LENGTH_LONG).show();
                break;
        }
    }

    public void changeTextViewText(String textViewName, String text) {
        final String newText = text;
        switch (textViewName) {
            case "attempts_view":
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            attempts_view.setText(String.valueOf(newText));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "stopwatch_view":
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            stopwatch_view.setText(newText);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                Toast.makeText(this, "Error 1x83-91", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public String getTextViewText(String textViewName) {
        switch (textViewName) {
            case "attempts_view":
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resultString = String.valueOf(attempts_view.getText());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "stopwatch_view":
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resultString = String.valueOf(stopwatch_view.getText());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                resultString = "Error 1x83-92";
                break;
        }
        return resultString;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reset_btn:
                MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.click2);
                mp.start();
                stopWatchHelper.reset(0);
                break;
            case R.id.google_play_btn:
                google_play_dialog.create().show();
                break;
        }//DON'T FORGET "break;"
    }

    private void signInSilently() {
        final GoogleSignInClient signInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        signInClient.silentSignIn().addOnCompleteListener(this,
                new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        if (task.isSuccessful()) {
                            signedInAccount = task.getResult();
                            signedIn();
                            prefManager.resetFailedSignins();
                        } else {
                            startSignInIntent();
                        }
                    }
                });
    }

    private void signedIn() {
        prefManager.setSignedIn(true);

        google_play_dialog.setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface _dialog, int _which) {
                signOut();
            }
        });
    }

    private void startSignInIntent() {
        GoogleSignInClient signInClient = GoogleSignIn.getClient(this,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        Intent intent = signInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                signedInAccount = result.getSignInAccount();
                prefManager.resetFailedSignins();

                signedIn();
                Toast.makeText(getApplicationContext(), "Signed In", Toast.LENGTH_SHORT).show();
            } else {
                String message = result.getStatus().getStatusMessage();
                if (message == null || message.isEmpty()) {
                    message = "Error";
                }
                Toast.makeText(getApplicationContext(), "Failed signing in, please check internet connection and restart the app to use achievements and leader board.", Toast.LENGTH_LONG).show();
                cancelledSignIn = true;
                prefManager.addFailedSignins();
            }
        }
    }

    private void signOut() {
        GoogleSignInClient signInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        signInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        prefManager.setSignedIn(false);

                        google_play_dialog.setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface _dialog, int _which) {
                                prefManager.resetFailedSignins();
                                signInSilently();
                            }
                        });
                    }
                });
    }

    private void showLeaderboard() {
        if (isSignedIn()) {
            Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                    .getLeaderboardIntent(getString(R.string.leaderboard))
                    .addOnSuccessListener(new OnSuccessListener<Intent>() {
                        @Override
                        public void onSuccess(Intent intent) {
                            startActivityForResult(intent, RC_LEADERBOARD_UI);
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Please sign in to use this feature", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAchievements() {
        if (isSignedIn()) {
            Games.getAchievementsClient(this, signedInAccount)
                    .getAchievementsIntent()
                    .addOnSuccessListener(new OnSuccessListener<Intent>() {
                        @Override
                        public void onSuccess(Intent intent) {
                            startActivityForResult(intent, RC_ACHIEVEMENT_UI);
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Please sign in to use this feature", Toast.LENGTH_SHORT).show();
        }
    }

    public void achieve(String achievement, int type) {
        if (type == 1) {
            prefManager.addTotalWins();
            prefManager.setMostAttemptsToWin();
            prefManager.setLeastAttemptsToWin();
            prefManager.addAttemptsToWin();
        } else if (type == 2) {
            prefManager.addTotalSpecialTimes();
        }

        if (isSignedIn()) {
            try {
                Games.getAchievementsClient(this, signedInAccount).unlock(achievement);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void increment(String achievement, int i) {
        if (isSignedIn()) {
            Games.getAchievementsClient(this, signedInAccount).increment(achievement, i);
        }
    }

    private boolean isScoreResultValid(final Leaderboards.LoadPlayerScoreResult scoreResult) {
        return scoreResult != null && GamesStatusCodes.STATUS_OK == scoreResult.getStatus().getStatusCode() && scoreResult.getScore() != null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!cancelledSignIn) {
            if (prefManager.getFailedSignins() < 12) {
                signInSilently();
            }
        }
    }

    @Override
    protected void onDestroy() {
        //prefManager.setAttempts(attempts);
        super.onDestroy();
    }

    public void saveAttempt(String mili) {
        prefManager.addAttempts();
        int intTime = Integer.parseInt(mili.substring(0, 2) + mili.substring(2 + 1));

        if (intTime == 100) {
            prefManager.addCurrentStreak();
        } else {
            prefManager.resetCurrentStreak();
        }

        String time = String.valueOf(intTime);
        prefManager.addTime(time);
        //Toast.makeText(getApplicationContext(), time, Toast.LENGTH_LONG).show();

        ArrayList<String> times = prefManager.getTimes();
        StringBuilder result = new StringBuilder();
        int arrayLength = times.size() - 1;
        for (int i = 0; i < arrayLength; i++) {
            result.append(times.get(i)).append(", ");
        }
        result.append(times.get(arrayLength));
    }

    public void resetAttempts() {
        prefManager.resetAttempts();
    }
}
