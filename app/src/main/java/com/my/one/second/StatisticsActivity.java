package com.my.one.second;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.TooltipCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.my.one.second.Util.PrefManager;
import com.my.one.second.Util.TabsAdapter;

public class StatisticsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private TabLayoutMediator tabLayoutMediator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        toolbar = findViewById(R.id.customToolbar);

        viewPager2 = findViewById(R.id.viewPager2);
        viewPager2.setAdapter(new TabsAdapter(this));
        tabLayout = findViewById(R.id.tabLayout);
        tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0: {
                        tab.setText("One Second Stats");
                        //tab.setIcon(R.drawable.baseline_looks_one_white_48dp);
                        break;
                    }
                    case 1: {
                        tab.setText("Lowest Stats");
                        //tab.setIcon(R.drawable.baseline_touch_app_white_48dp);
                        break;
                    }
                }
            }
        });

        int callingActivity = getIntent().getIntExtra("calling-activity", 0);
        tabLayout.setScrollPosition(callingActivity,0f,true);
        viewPager2.setCurrentItem(callingActivity);

        tabLayoutMediator.attach();
        setSupportActionBar(toolbar);
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
                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(main);
                return true;
            case R.id.lowest_btn:
                Intent lowest = new Intent(getApplicationContext(), LowestActivity.class);
                startActivity(lowest);
                return true;
            case R.id.statistics_btn:
                Toast.makeText(this, R.string.you_are_here, Toast.LENGTH_LONG).show();
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
}
