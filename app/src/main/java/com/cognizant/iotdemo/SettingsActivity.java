package com.cognizant.iotdemo;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    private EditText urlEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        urlEditText = (EditText) findViewById(R.id.editText);
    }

    public void setURL(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.IOT, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("URL", urlEditText.getText().toString());
        editor.commit();
        finish();
    }
}
