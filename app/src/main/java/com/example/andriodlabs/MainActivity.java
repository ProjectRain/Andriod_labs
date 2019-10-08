package com.example.andriodlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String ACTIVITY_NAME = "MAIN_ACTIVITY";
    EditText emailTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(ACTIVITY_NAME, "In function:" + "onCreate");
        setContentView(R.layout.activity_main);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        emailTextField = findViewById(R.id.email);
        emailTextField.setText(pref.getString("email", ""));

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent profileActivityIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                profileActivityIntent.putExtra("email", emailTextField.getText().toString());
                startActivity(profileActivityIntent);
            }

        });
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.e(ACTIVITY_NAME, "In function:" + "onPause");
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("email", emailTextField.getText().toString());
        editor.commit();
    }
}
