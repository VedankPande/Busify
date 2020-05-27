package com.mitapp.busify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Objects;
import java.util.Set;

public class Settings extends AppCompatActivity {

    boolean isChangeThemeExpanded = false;
    LinearLayout settings_theme_followSystem_view;
    int choice;
    RadioButton settings_theme_light_followSystem, settings_theme_dark_followSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar my_toolbar = findViewById(R.id.action_bar);
        my_toolbar.setTitle("");
        setSupportActionBar(my_toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        final Button changeTheme = findViewById(R.id.settings_changetheme_button);
        changeTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout settings_changeTheme_option_view = findViewById(R.id.settings_changeTheme_option_view);
                if (isChangeThemeExpanded){
                    settings_changeTheme_option_view.setVisibility(View.GONE);
                    isChangeThemeExpanded = false;
                }
                else{
                    settings_changeTheme_option_view.setVisibility(View.VISIBLE);
                    isChangeThemeExpanded = true;
                }
            }
        });

        settings_theme_followSystem_view = findViewById(R.id.settings_theme_followSystem_view);

        RadioButton settings_theme_light = findViewById(R.id.settings_theme_light);
        settings_theme_light.setChecked(true);
        settings_theme_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_theme_specific();
            }
        });

        RadioButton settings_theme_dark = findViewById(R.id.settings_theme_dark);
        settings_theme_dark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_theme_specific();
            }
        });

        RadioButton settings_theme_white = findViewById(R.id.settings_theme_white);
        settings_theme_white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_theme_specific();
            }
        });

        RadioButton settings_theme_black = findViewById(R.id.settings_theme_black);
        settings_theme_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_theme_specific();
            }
        });

        settings_theme_light_followSystem = findViewById(R.id.settings_theme_light_followSystem);
        settings_theme_dark_followSystem = findViewById(R.id.settings_theme_dark_followSystem);
        settings_theme_light_followSystem.setChecked(true);
        settings_theme_dark_followSystem.setChecked(true);
        RadioButton settings_theme_followSystem = findViewById(R.id.settings_theme_followSystem);
        settings_theme_followSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_theme_followSystem();
                settings_theme_followSystem_view.setVisibility(View.VISIBLE);
                settings_theme_light_followSystem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        update_theme_followSystem();
                    }
                });

                RadioButton settings_theme_white_followSystem = findViewById(R.id.settings_theme_white_followSystem);
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

                RadioButton settings_theme_black_followSystem = findViewById(R.id.settings_theme_black_followSystem);
                settings_theme_black_followSystem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        update_theme_followSystem();
                    }
                });
            }
        });
    }

    private void update_theme_specific(){
        RadioGroup radioGroup = findViewById(R.id.settings_changetheme_radioGroup);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);
        String selectedTheme = radioButton.getText().toString();

        if (selectedTheme.equals("Light")) choice = 1;
        else if (selectedTheme.equals("Dark")) choice = 2;
        else if (selectedTheme.equals("White")) choice = 3;
        else if (selectedTheme.equals("Black")) choice = 4;

        Toast.makeText(getApplicationContext(), String.valueOf(choice), Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPreferences = getSharedPreferences("Map Themes",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("theme",choice);
        editor.apply();

        settings_theme_followSystem_view.setVisibility(View.GONE);
    }

    private void update_theme_followSystem(){
        choice = 5;
        RadioGroup radioGroupLight = findViewById(R.id.settings_changetheme_radioGroup_followSystem_light);
        int selectedIdLight = radioGroupLight.getCheckedRadioButtonId();
        RadioButton radioButtonLight = findViewById(selectedIdLight);
        String selectedThemeLight = radioButtonLight.getText().toString();
        choice*=10;
        if (selectedThemeLight.equals("Light")) choice+=1;
        else if (selectedThemeLight.equals("White")) choice+=3;

        RadioGroup radioGroupDark = findViewById(R.id.settings_changetheme_radioGroup_followSystem_dark);
        int selectedIdDark = radioGroupDark.getCheckedRadioButtonId();
        RadioButton radioButtonDark = findViewById(selectedIdDark);
        String selectedThemeDark = radioButtonDark.getText().toString();
        choice*=10;
        if (selectedThemeDark.equals("Dark")) choice+=2;
        else if (selectedThemeDark.equals("Black")) choice+=4;

        Toast.makeText(getApplicationContext(), String.valueOf(choice), Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPreferences = getSharedPreferences("Map Themes",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("theme",choice);
        editor.apply();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}