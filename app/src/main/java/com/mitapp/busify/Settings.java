package com.mitapp.busify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
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
    LinearLayout settings_changeTheme_option_view;
    String selectedTheme;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar my_toolbar = findViewById(R.id.action_bar);
        my_toolbar.setTitle("");
        setSupportActionBar(my_toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        settings_changeTheme_option_view = findViewById(R.id.settings_changetheme_option_view);

        final Button changeTheme = findViewById(R.id.settings_changetheme_button);
        changeTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        radioGroup = findViewById(R.id.settings_changetheme_radioGroup);

        RadioButton settings_theme_light = findViewById(R.id.settings_theme_light);
        settings_theme_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_theme();
            }
        });

        RadioButton settings_theme_dark = findViewById(R.id.settings_theme_dark);
        settings_theme_dark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_theme();
            }
        });

        RadioButton settings_theme_white = findViewById(R.id.settings_theme_white);
        settings_theme_white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_theme();
            }
        });

        RadioButton settings_theme_black = findViewById(R.id.settings_theme_black);
        settings_theme_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_theme();
            }
        });

    }

    private void update_theme(){
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);
        selectedTheme = radioButton.getText().toString();
        //TODO: String selectedTheme stores the value on the selected theme. Link it with DB
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
