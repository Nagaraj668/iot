package com.cognizant.iotdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public static final String IOT = "iot";
    private ToggleButton toggleButton;

    private EditText greenEditText;
    private EditText blueEditText;
    private EditText redEditText;
    private EditText whiteEditText;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(IOT, MODE_PRIVATE);

        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        greenEditText = (EditText) findViewById(R.id.green);
        blueEditText = (EditText) findViewById(R.id.blue);
        redEditText = (EditText) findViewById(R.id.red);
        whiteEditText = (EditText) findViewById(R.id.white);

    }

    public void onSubmit(View view) {
        try {
            int green = Integer.parseInt(greenEditText.getText().toString());
            int blue = Integer.parseInt(blueEditText.getText().toString());
            int red = Integer.parseInt(redEditText.getText().toString());
            int white = Integer.parseInt(whiteEditText.getText().toString());

            int switchStatus = 0;
            String toggleStatusText = toggleButton.getText().toString();

            if (toggleStatusText.equals("ON")) {
                switchStatus = 1;
            }

            String request = constructRequest(switchStatus, green, blue, red, white);

            BackgroundTask backgroundTask = new BackgroundTask();

            String url = sharedPreferences.getString("URL",
                    "http://192.168.43.128/iot.php");

            backgroundTask.execute(url, "POST", request);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Please enter all fields", Toast.LENGTH_SHORT).show();
        }
    }

    private String constructRequest(int switchStatus, int green, int blue, int red, int white) {
        String request = "led=" + switchStatus + "&ledval1=" + green + "&ledval2=" + blue
                + "&ledval3=" + red + "&ledval4=" + white;
        return request;
    }


    private class BackgroundTask extends AsyncTask<Object, String, String> {
        @Override
        protected String doInBackground(Object... objects) {
            String response = null;
            try {
                response = sendRequest((String) objects[0], (String) objects[1], (String) objects[2]);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            // process response
        }
    }

    // HTTP GET request
    private String sendRequest(String url, String method, String request) {
        StringBuffer response = new StringBuffer();
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod(method);

            if (request != null) {
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(request);
                wr.flush();
                wr.close();
            }

            int responseCode = con.getResponseCode();
            System.out.println("Sending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println(method + " response is : " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.settings: {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            }
        }

        return true;
    }
}
