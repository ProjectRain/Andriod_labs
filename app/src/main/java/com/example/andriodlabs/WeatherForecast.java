package com.example.andriodlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import static org.xmlpull.v1.XmlPullParser.TEXT;

public class WeatherForecast extends AppCompatActivity {
    private ProgressBar loadingImage;
    private TextView currentWeather2;
    private TextView minWeather;
    private TextView maxWeather;
    private TextView uvRatingView;
    private ImageView currentWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        loadingImage = findViewById(R.id.simpleProgressBar);
        currentWeather2 = findViewById(R.id.currentWeather2);
        minWeather = findViewById(R.id.minWeather);
        maxWeather = findViewById(R.id.maxWeather);
        currentWeather = findViewById(R.id.currentWeather);
        uvRatingView = findViewById(R.id.uvRating);
        loadingImage.setVisibility(View.VISIBLE);
        ForecastQuery theQuery = new ForecastQuery();
        theQuery.execute( );
    }
    private class ForecastQuery extends AsyncTask <String, Integer, String> {
        float minTemp;
        float maxTemp;
        float uvRating;
        float currentTemp;
        Bitmap bitmap;
        String weather;

        @Override                       //Type 1
        protected String doInBackground(String... strings) {
            String ret = null;
            String queryURL = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric";

            try {       // Connect to the server:
                URL url = new URL(queryURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();

                //Set up the XML parser:
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(inStream, "UTF-8");

                //Iterate over the XML tags:
                int EVENT_TYPE;         //While not the end of the document:
                while ((EVENT_TYPE = xpp.getEventType()) != XmlPullParser.END_DOCUMENT) {
                    switch (EVENT_TYPE) {
                        case START_TAG:         //This is a start tag < ... >
                            String tagName = xpp.getName(); // What kind of tag?
                            if (tagName.equals("temperature")) {
                                currentTemp = Float.parseFloat(xpp.getAttributeValue(null, "value"));
                                publishProgress(25);
                                minTemp = Float.parseFloat(xpp.getAttributeValue(null, "min"));
                                publishProgress(50);
                                maxTemp = Float.parseFloat(xpp.getAttributeValue(null, "max"));
                                publishProgress(75);
                            }
                            if (tagName.equals("weather")) {
                                weather = xpp.getAttributeValue(null,"icon");
                                String fileName = weather + ".png";

                                Log.i("Search file", "Looking for file: " + fileName);
                                if(!fileExistance(fileName)) {
                                    Log.i("Search file", "Not found locally, downloading: " + fileName);
                                    queryURL = "http://openweathermap.org/img/w/" + fileName;
                                    url = new URL(queryURL);
                                    urlConnection = (HttpURLConnection) url.openConnection();
                                    urlConnection.connect();
                                    int responseCode = urlConnection.getResponseCode();
                                    if (responseCode == 200) {
                                        bitmap = BitmapFactory.decodeStream(urlConnection.getInputStream());
                                        FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                        outputStream.flush();
                                        outputStream.close();
                                    }
                                }else{
                                    Log.i("Search file", "Found locally: " + fileName);
                                    FileInputStream fis = null;
                                    try {    fis = openFileInput(fileName);   }
                                    catch (FileNotFoundException e) {    e.printStackTrace();  }
                                    bitmap = BitmapFactory.decodeStream(fis);
                                }
                                publishProgress(100);

                                queryURL = "http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389";
                                url = new URL(queryURL);
                                urlConnection = (HttpURLConnection) url.openConnection();
                                inStream = urlConnection.getInputStream();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                                StringBuilder sb = new StringBuilder();

                                String line;
                                while ((line = reader.readLine()) != null){
                                    sb.append(line + "\n");
                                }
                                String result = sb.toString();
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    uvRating = Float.parseFloat(jsonObject.getString("value"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case END_TAG:           //This is an end tag: </ ... >
                            break;
                        case TEXT:              //This is text between tags < ... > Hello world </ ... >
                            break;
                    }
                    xpp.next(); // move the pointer to next XML element
                }
            } catch (MalformedURLException mfe) {
                ret = "Malformed URL exception";
            } catch (IOException ioe) {
                ret = "IO Exception. Is the Wifi connected?";
            } catch (XmlPullParserException pe) {
                ret = "XML Pull exception. The XML is not properly formed";
            }

            //What is returned here will be passed as a parameter to onPostExecute:
            return ret;
        }

        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            loadingImage.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            currentWeather2.setText(Float.toString(currentTemp));
            minWeather.setText(Float.toString(minTemp));
            maxWeather.setText(Float.toString(maxTemp));
            currentWeather.setImageBitmap(bitmap);
            uvRatingView.setText(Float.toString(uvRating));
            loadingImage.setVisibility(View.INVISIBLE);
        }
    }
}
