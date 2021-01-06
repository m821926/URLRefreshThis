package com.pj.urlrefreshthis;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class MyPreferencesActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private SeekBar mySeekBarMinutes;
    private TextView mytbRefreshMinutes;
    private EditText mytbURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_preferences);

        // Define seekbar with minutes
        mySeekBarMinutes = (SeekBar) findViewById(R.id.seekBarMinutes);
        mySeekBarMinutes.setOnSeekBarChangeListener( this);

        // Define Text field to show minutes
        mytbRefreshMinutes = (TextView) findViewById(R.id.tbRefreshMinutes);

        // Define URL field where user can enter web page
        mytbURL = (EditText) findViewById(R.id.tbURL);

        // Get Saved Preferences
        SharedPreferences sharedPreferences = getSharedPreferences( getPackageName() + Constants.PREF_FILE_NAME ,MODE_PRIVATE);
        mytbURL.setText(sharedPreferences.getString(Constants.PREF_URL,"http://"));
        mySeekBarMinutes.setProgress(sharedPreferences.getInt(Constants.PREF_REFRESH_MINUTES,6)-1); // Reduce 1 to correct value seekbar starts with 0 !!

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId())
        {
            case R.id.seekBarMinutes:
                int NewRefreshMinutes = i+1; // add 1 as seekbar lowest number is 0
                mytbRefreshMinutes.setText( Integer.toString(NewRefreshMinutes) );
                break;
            default:
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void btnSave(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences( getPackageName() + Constants.PREF_FILE_NAME ,MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.PREF_URL, mytbURL.getText().toString());
        editor.putInt(Constants.PREF_REFRESH_MINUTES, Integer.parseInt( (String) mytbRefreshMinutes.getText() ) );
        editor.apply();

        finish();
    }

    public void btnCancel(View view) {
        finish();
    }
}
