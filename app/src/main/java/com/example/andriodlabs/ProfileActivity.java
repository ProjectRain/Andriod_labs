package com.example.andriodlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";
    ImageButton squareButton;
    Button chatButton;
    Button weatherForecast;
    EditText emailEditText;
    ImageButton mImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(ACTIVITY_NAME, "In function:" + "onCreate");
        setContentView(R.layout.profile_activity);
        Intent intent = getIntent();
        emailEditText = findViewById(R.id.email);
        emailEditText.setText(intent.getStringExtra("email"));
        squareButton = findViewById(R.id.squareButton);
        chatButton = findViewById(R.id.chatButton);
        mImageButton = findViewById(R.id.squareButton);
        weatherForecast = findViewById(R.id.weatherForecast);
        squareButton.setOnClickListener(this);
        chatButton.setOnClickListener(this);
        weatherForecast.setOnClickListener(this);

    }
    @Override
    public void onClick(View v){

        switch (v.getId()) {

            case R.id.squareButton:
                dispatchTakePictureIntent();

                break;

            case R.id.chatButton:
                Intent chatRoomActivityIntent = new Intent(getApplicationContext(), ChatRoomActivity.class);
                startActivity(chatRoomActivityIntent);
                break;

            case R.id.weatherForecast:
                Intent weatherForecastIntent = new Intent(getApplicationContext(), WeatherForecast.class);
                startActivity(weatherForecastIntent);
                break;

        }
    }
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Log.e(ACTIVITY_NAME, "In function:" + "dispatchTakePictureIntent");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(ACTIVITY_NAME, "In function:" + "onActivityResult");

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageButton.setImageBitmap(imageBitmap);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.e(ACTIVITY_NAME, "In function:" + "onStart");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.e(ACTIVITY_NAME, "In function:" + "onResume");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.e(ACTIVITY_NAME, "In function:" + "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(ACTIVITY_NAME, "In function:" + "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(ACTIVITY_NAME, "In function:" + "onDestroy");
    }
}
