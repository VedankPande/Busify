package com.mitapp.busify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Objects;

public class Settings extends AppCompatActivity {

    LinearLayout settings_changeTheme_option_view, settings_theme_followSystem_view;
    int fileData;
    RadioGroup settings_changetheme_radioGroup, settings_changetheme_radioGroup_followSystem_light,settings_changetheme_radioGroup_followSystem_dark;
    RadioButton settings_theme_light_followSystem, settings_theme_dark_followSystem, settings_theme_light,settings_theme_dark, settings_theme_white, settings_theme_black,settings_theme_followSystem,settings_theme_white_followSystem,settings_theme_black_followSystem;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor fileEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar my_toolbar = findViewById(R.id.action_bar);
        my_toolbar.setTitle("");
        setSupportActionBar(my_toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        initializeVariables();
        initializeRadioButtons();
        initializeOnClickListeners();
    }

    private void initializeVariables(){

        settings_changeTheme_option_view = findViewById(R.id.settings_changeTheme_option_view);
        settings_theme_followSystem_view = findViewById(R.id.settings_theme_followSystem_view);

        settings_changetheme_radioGroup = findViewById(R.id.settings_changetheme_radioGroup);
        settings_changetheme_radioGroup_followSystem_light = findViewById(R.id.settings_changetheme_radioGroup_followSystem_light);
        settings_changetheme_radioGroup_followSystem_dark = findViewById(R.id.settings_changetheme_radioGroup_followSystem_dark);

        settings_theme_light = findViewById(R.id.settings_theme_light);
        settings_theme_dark = findViewById(R.id.settings_theme_dark);
        settings_theme_white = findViewById(R.id.settings_theme_white);
        settings_theme_black = findViewById(R.id.settings_theme_black);
        settings_theme_followSystem = findViewById(R.id.settings_theme_followSystem);

        settings_theme_light_followSystem = findViewById(R.id.settings_theme_light_followSystem);
        settings_theme_dark_followSystem = findViewById(R.id.settings_theme_dark_followSystem);
        settings_theme_white_followSystem = findViewById(R.id.settings_theme_white_followSystem);
        settings_theme_black_followSystem = findViewById(R.id.settings_theme_black_followSystem);

        sharedPreferences = getSharedPreferences("Map Themes",MODE_PRIVATE);
        fileEditor = sharedPreferences.edit();
        fileData = sharedPreferences.getInt("theme", 512);
    }

    private void initializeRadioButtons(){
        if (fileData == 1){
            settings_theme_light.setChecked(true);
        }
        else if (fileData == 2){
            settings_theme_dark.setChecked(true);
        }
        else if (fileData ==3){
            settings_theme_white.setChecked(true);
        }
        else if (fileData == 4){
            settings_theme_black.setChecked(true);
        }
        else if (fileData /100 == 5){
            settings_theme_followSystem.setChecked(true);
            settings_theme_followSystem_view.setVisibility(View.VISIBLE);
            if(fileData %100/10 == 1) settings_theme_light_followSystem.setChecked(true);
            else if(fileData %100/10 == 3) settings_theme_white_followSystem.setChecked(true);
            if(fileData %10 == 2) settings_theme_dark_followSystem.setChecked(true);
            else if(fileData %10 == 4) settings_theme_black_followSystem.setChecked(true);
        }
    }

    private void initializeOnClickListeners(){

        settings_theme_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_theme_specific();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        settings_theme_dark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_theme_specific();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        });

        settings_theme_white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_theme_specific();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        settings_theme_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_theme_specific();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        });

        settings_theme_followSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fileData/100 != 5){
                    fileData = 512;
                    initializeRadioButtons();
                }
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                update_theme_followSystem();
                settings_theme_followSystem_view.setVisibility(View.VISIBLE);
            }
        });

        settings_theme_light_followSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_theme_followSystem();
            }
        });

        settings_theme_white_followSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_theme_followSystem();
            }
        });

        settings_theme_dark_followSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_theme_followSystem();
            }
        });

        settings_theme_black_followSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_theme_followSystem();
            }
        });
    }

    private void update_theme_specific(){
        int selectedId = settings_changetheme_radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);
        String selectedTheme = radioButton.getText().toString();

        switch (selectedTheme) {
            case "Light":
                fileData = 1;
                break;
            case "Dark":
                fileData = 2;
                break;
            case "White":
                fileData = 3;
                break;
            case "Black":
                fileData = 4;
                break;
        }

        fileEditor.putInt("theme", fileData);
        fileEditor.apply();

        settings_theme_followSystem_view.setVisibility(View.GONE);
    }

    private void update_theme_followSystem(){
        int selectedIdLight = settings_changetheme_radioGroup_followSystem_light.getCheckedRadioButtonId();
        RadioButton radioButtonLight = findViewById(selectedIdLight);
        String selectedThemeLight = radioButtonLight.getText().toString();
        fileData = 50;
        switch (selectedThemeLight) {
            case "Light":
                fileData += 1;
                break;
            case "White":
                fileData += 3;
                break;
        }
        int selectedIdDark = settings_changetheme_radioGroup_followSystem_dark.getCheckedRadioButtonId();
        RadioButton radioButtonDark = findViewById(selectedIdDark);
        String selectedThemeDark = radioButtonDark.getText().toString();
        fileData *= 10;
        switch (selectedThemeDark) {
            case "Dark":
                fileData += 2;
                break;
            case "Black":
                fileData += 4;
                break;
        }
        fileEditor.putInt("theme", fileData);
        fileEditor.apply();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}