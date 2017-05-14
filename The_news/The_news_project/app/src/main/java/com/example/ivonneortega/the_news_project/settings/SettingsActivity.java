package com.example.ivonneortega.the_news_project.settings;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.example.ivonneortega.the_news_project.R;
import com.example.ivonneortega.the_news_project.mainActivity.MainActivity;


/**
 * Settings activity where user can set the theme and notifications settings
 */
public class SettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "Settings Activity" ;
    private Switch mSwitch_theme, mSwitch_notification;

    public static final String THEME = "theme";
    public static final String THEME_HAS_CHANGED = "theme_changed";
    public static final String NOTIFICATION = "notification";
    public static final int TRUE = 0;
    public static final int FALSE = 1;
    public ImageView mBack;
    private boolean mStartActivity;

    /**
     * Creating the activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();

        mSwitch_theme = (Switch) findViewById(R.id.switch_theme);
        mSwitch_notification = (Switch) findViewById(R.id.switch_notification);
        mSwitch_theme.setOnCheckedChangeListener(this);
        mSwitch_notification.setOnCheckedChangeListener(this);
        mBack = (ImageView) findViewById(R.id.back_toolbar);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("com.example.ivonneortega.the_news_project.Settings", Context.MODE_PRIVATE);
        String str = sharedPreferences.getString(SettingsActivity.THEME,"DEFAULT");
        //Setting the view depending on the theme
        if(str.equals("dark"))
        {
            mSwitch_theme.setChecked(true);
        }

        int notification = sharedPreferences.getInt(NOTIFICATION,TRUE);
        if(notification==TRUE)
            mSwitch_notification.setChecked(true);
        else
            mSwitch_notification.setChecked(false);
    }

    /**
     * Setting the theme when the activity launches
     */
    public void setTheme()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.ivonneortega.the_news_project.Settings", Context.MODE_PRIVATE);
        String str = sharedPreferences.getString(SettingsActivity.THEME,"DEFAULT");
        if(str.equals("dark"))
        {
            //if the theme is dark change the theme color
            setTheme(R.style.DarkTheme);
            setContentView(R.layout.activity_settings);
            findViewById(R.id.root_toolbar).setBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkTheme));
        }
        else
        {
            //if the theme is not dark just setContentView
            setContentView(R.layout.activity_settings);
        }
        mStartActivity=true;

    }

    /**
     * On checked change for each Switch button
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId())
        {
            // If the theme changes add the change to a shared preferences
            case R.id.switch_theme:
                SharedPreferences sharedPreferences = getSharedPreferences("com.example.ivonneortega.the_news_project.Settings", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(isChecked)
                {
                    editor.putString(THEME,"dark");
                }
                else
                {
                    editor.putString(THEME,"light");
                }
                editor.putBoolean(THEME_HAS_CHANGED,true);
                editor.apply();
                break;

            // If the notification setting changes, add the change to a shared preferences
            case R.id.switch_notification:
                SharedPreferences sharedPreferences1 = getSharedPreferences("com.example.ivonneortega.the_news_project.Settings", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                if(isChecked)
                {
                    editor1.putInt(NOTIFICATION,TRUE);
                    editor1.apply();
                }
                else
                {
                    editor1.putInt(NOTIFICATION,FALSE);
                    editor1.apply();
                }
                break;
        }
    }
}
