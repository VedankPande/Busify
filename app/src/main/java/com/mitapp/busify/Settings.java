package com.mitapp.busify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.util.Objects;

public class Settings extends AppCompatActivity {

    boolean isChangeThemeExpanded = false;
    LinearLayout settings_theme_followSystem_view;
    int choice, dataFromFile;
    Button changeTheme;
    RadioButton settings_theme_light_followSystem, settings_theme_dark_followSystem, settings_theme_light,settings_theme_dark, settings_theme_white, settings_theme_black,settings_theme_followSystem,settings_theme_white_followSystem,settings_theme_black_followSystem;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

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
        changeTheme = findViewById(R.id.settings_changetheme_button);

        settings_theme_light = findViewById(R.id.settings_theme_light);
        settings_theme_dark = findViewById(R.id.settings_theme_dark);
        settings_theme_white = findViewById(R.id.settings_theme_white);
        settings_theme_black = findViewById(R.id.settings_theme_black);

        settings_theme_followSystem_view = findViewById(R.id.settings_theme_followSystem_view);
        settings_theme_followSystem = findViewById(R.id.settings_theme_followSystem);
        settings_theme_light_followSystem = findViewById(R.id.settings_theme_light_followSystem);
        settings_theme_dark_followSystem = findViewById(R.id.settings_theme_dark_followSystem);
        settings_theme_white_followSystem = findViewById(R.id.settings_theme_white_followSystem);
        settings_theme_black_followSystem = findViewById(R.id.settings_theme_black_followSystem);

        sharedPreferences = getSharedPreferences("Map Themes",MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    private void initializeRadioButtons(){

        dataFromFile = sharedPreferences.getInt("theme", 0);
        if (dataFromFile == 1){
            settings_theme_light.setChecked(true);
            settings_theme_followSystem_view.setVisibility(View.GONE);
        }
        else if (dataFromFile == 2){
            settings_theme_dark.setChecked(true);
            settings_theme_followSystem_view.setVisibility(View.GONE);
        }
        else if (dataFromFile ==3){
            settings_theme_white.setChecked(true);
            settings_theme_followSystem_view.setVisibility(View.GONE);
        }
        else if (dataFromFile == 4){
            settings_theme_black.setChecked(true);
            settings_theme_followSystem_view.setVisibility(View.GONE);
        }
        else if (dataFromFile/100 == 5){
            settings_theme_followSystem.setChecked(true);
            settings_theme_followSystem_view.setVisibility(View.VISIBLE);
            dataFromFile%=100;
            if(dataFromFile/10 == 1) settings_theme_light_followSystem.setChecked(true);
            else if(dataFromFile/10 == 3) settings_theme_light_followSystem.setChecked(true);
            dataFromFile%=10;
            if(dataFromFile == 2) settings_theme_dark_followSystem.setChecked(true);
            else if(dataFromFile == 4) settings_theme_black_followSystem.setChecked(true);
        }
    }

    private void initializeOnClickListeners(){

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


        settings_theme_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_theme_specific();
            }
        });

        settings_theme_dark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_theme_specific();
            }
        });

        settings_theme_white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_theme_specific();
            }
        });

        settings_theme_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_theme_specific();
            }
        });
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
        });
    }

    private void update_theme_specific(){
        RadioGroup radioGroup = findViewById(R.id.settings_changetheme_radioGroup);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);
        String selectedTheme = radioButton.getText().toString();

        switch (selectedTheme) {
            case "Light":
                choice = 1;
                break;
            case "Dark":
                choice = 2;
                break;
            case "White":
                choice = 3;
                break;
            case "Black":
                choice = 4;
                break;
        }

        Toast.makeText(getApplicationContext(), String.valueOf(choice), Toast.LENGTH_SHORT).show();

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
        switch (selectedThemeLight) {
            case "Light":
                choice += 1;
                break;
            case "White":
                choice += 3;
                break;
        }
        RadioGroup radioGroupDark = findViewById(R.id.settings_changetheme_radioGroup_followSystem_dark);
        int selectedIdDark = radioGroupDark.getCheckedRadioButtonId();
        RadioButton radioButtonDark = findViewById(selectedIdDark);
        String selectedThemeDark = radioButtonDark.getText().toString();
        choice*=10;
        switch (selectedThemeDark) {
            case "Dark":
                choice += 2;
                break;
            case "Black":
                choice += 4;
                break;
        }
        editor.putInt("theme",choice);
        editor.apply();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}