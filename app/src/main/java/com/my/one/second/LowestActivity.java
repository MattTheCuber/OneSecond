package com.my.one.second;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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
import com.google.android.gms.games.AnnotatedData;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.my.one.second.Util.PrefManager;
import com.my.one.second.Util.StopWatchHelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class LowestActivity extends AppCompatActivity implements View.OnClickListener {

    private StopWatchHelper stopWatchHelper;
	private Intent main = new Intent();
    private AlertDialog.Builder google_play_dialog;
	private Boolean cancelledSignIn = false;
    private PrefManager prefManager;
    private Toolbar toolbar;

	private Button reset_btn;
    private LinearLayout bg;
    private TextView stopwatch_view;
    private TextView best_view;
    private String bestScore;
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
		setContentView(R.layout.lowest);

        toolbar = findViewById(R.id.customToolbar);
        prefManager = new PrefManager(this);
        google_play_dialog = new AlertDialog.Builder(this);
        reset_btn = findViewById(R.id.reset_btn);
        Button google_play_btn = findViewById(R.id.google_play_btn);
        Button start_stop_btn = findViewById(R.id.start_stop_btn);
        stopwatch_view = findViewById(R.id.stopwatch_view);
        best_view = findViewById(R.id.best_view);
        bg = findViewById(R.id.bg);

        reset_btn.setOnClickListener(this);
        google_play_btn.setOnClickListener(this);
        start_stop_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    stopWatchHelper.startStop(1);
                    MediaPlayer mp = MediaPlayer.create(LowestActivity.this, R.raw.click1);
                    mp.start();
                }
                return true;
            }
        });

        stopWatchHelper = new StopWatchHelper();
        stopWatchHelper.setContext(this);
        stopWatchHelper.setLowestActivity(this);

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

        main.setClass(getApplicationContext(), MainActivity.class);

        if (prefManager.isSignedIn()) {
            if (prefManager.getFailedSignins() > 12) {
                Toast.makeText(getApplicationContext(), "Auto sign in disabled due to too many failed attempts. Sign in through the menu to enable auto sign in.", Toast.LENGTH_LONG).show();
            } else {
                signInSilently();
            }
            if (GoogleSignIn.getLastSignedInAccount(this) != null) {
                GamesClient gamesClient = Games.getGamesClient(LowestActivity.this, GoogleSignIn.getLastSignedInAccount(this));
                gamesClient.setViewForPopups(findViewById(R.id.gps_popup));
            }
            getBest();
        }

        setSupportActionBar(toolbar);
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
        MenuInflater inflater = getMenuInflater();
        TextView tv = new TextView(this);
        tv.setText(getText(R.string.mode_colen));
        tv.setTextColor(getResources().getColor(R.color.white));
        menu.add(0, 0, 0, "Mode:").setActionView(tv).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.one_second_btn:
                startActivity(main);
                return true;
            case R.id.lowest_btn:
                Toast.makeText(this, R.string.same_gamemode, Toast.LENGTH_LONG).show();
                return true;
            case R.id.statistics_btn:
                Intent stat = new Intent(getApplicationContext(), StatisticsActivity.class);
                stat.putExtra("calling-activity", 1);
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
            case "red":
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bg.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
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
            case "best_view":
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            best_view.setText(String.valueOf(newText));
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
            case "best_view":
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resultString = String.valueOf(best_view.getText());
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
            /*case R.id.reset_record_btn:
                if (!stopWatchHelper.isRunning()) {
                    new AlertDialog.Builder(this)
                            .setTitle("Reset Record")
                            .setMessage("Do you really want to reset the record?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    setBest("0");
                                    best_view.setText("---");
                                    stopWatchHelper.setBest(Integer.parseInt(bestScore));
                                    resetBest();
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                } else {
                    Toast.makeText(getApplicationContext(), "You can't reset record while the stopwatch is running", Toast.LENGTH_LONG).show();
                }
                break;*/
            case R.id.reset_btn:
                MediaPlayer mp = MediaPlayer.create(LowestActivity.this, R.raw.click2);
                mp.start();
                stopWatchHelper.reset(1);
                break;
            case R.id.google_play_btn:
                google_play_dialog.create().show();
                break;
        }
    }

    private void signInSilently() {
        GoogleSignInClient signInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        signInClient.silentSignIn().addOnCompleteListener(this,
                new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        if (task.isSuccessful()) {
                            prefManager.resetFailedSignins();
                            signedInAccount = task.getResult();
                            signedIn();
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
        if (type == 3) {
            prefManager.addTotalSpecialTimesLowest();
        }

        if (isSignedIn()) {
            try {
                Games.getAchievementsClient(this, signedInAccount).unlock(achievement);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void updateBestTime(int time, int i) {
        if (isSignedIn()) {
            Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this)).submitScore(getString(R.string.leaderboard), time);
        }
    }

    public void setBest(String best) {
        prefManager.setBestTime(Integer.parseInt(best));
        bestScore = best;
        if (best.equals("0")) {
            best_view.setText("---");
        } else {
            best_view.setText(bestScore);
        }
    }

    public void saveScore(String time) {
        prefManager.addTimeLowest(time);
    }

    private void getBest() {
        if (isSignedIn()) {
            Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this)).loadCurrentPlayerLeaderboardScore(getString(R.string.leaderboard), LeaderboardVariant.TIME_SPAN_ALL_TIME, LeaderboardVariant.COLLECTION_PUBLIC)
                    .addOnSuccessListener(this, new OnSuccessListener<AnnotatedData<LeaderboardScore>>() {
                        @Override
                        public void onSuccess(AnnotatedData<LeaderboardScore> leaderboardScoreAnnotatedData) {
                            long score = 0L;
                            if (leaderboardScoreAnnotatedData != null) {
                                if (leaderboardScoreAnnotatedData.get() != null) {
                                    score = leaderboardScoreAnnotatedData.get().getRawScore();
                                    bestScore = String.valueOf(score).split("\\.")[0];
                                    Log.d(TAG, "LeaderBoard: " + Long.toString(score));
                                    getBestFinish();
                                } else {
                                    Log.d(TAG, "LeaderBoard: .get() is null");
                                }
                            } else {
                                Log.d(TAG, "LeaderBoard: " + Long.toString(score));
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "LeaderBoard: FAILURE");
                        }
                    });
        } else { getBestFinish(); }
    }

    private void getBestFinish() {
        String GPG = bestScore;
        String SP = String.valueOf(prefManager.getBestTime());
        String FINAL = "0";
        if (GPG == null) {
            GPG = "";
        }

        if (!GPG.equals("")) {
            if (SP.equals("")) {
                FINAL = GPG;
                Toast.makeText(LowestActivity.this, "Previous record found! " + FINAL + "ms", Toast.LENGTH_SHORT).show();
            } else if (Integer.parseInt(GPG) < Integer.parseInt(SP)) {
                FINAL = GPG;
                Toast.makeText(LowestActivity.this, "Previous Record Found! " + FINAL + "ms", Toast.LENGTH_SHORT).show();
            } else {//SP<GPG
                FINAL = SP;
                if (!FINAL.equals("")) {
                    updateBestTime(Integer.parseInt(FINAL), 1);
                }
            }
        } else if (!SP.equals("")) {
            FINAL = SP;
        }
        setBest(FINAL);
        if (!FINAL.equals("")) {
            stopWatchHelper.setBest(Integer.parseInt(FINAL));
        } else {
            Toast.makeText(LowestActivity.this, "Error GBF: " + FINAL, Toast.LENGTH_SHORT).show();
        }
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
}
