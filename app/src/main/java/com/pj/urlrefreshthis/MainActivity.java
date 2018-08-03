package com.pj.urlrefreshthis;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private String myURL;
    private int myRefreshMinutes;
    private ProgressBar myProgressBar;
    private WebView myWebView;
    private Timer TimerWebViewRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       myWebView = (WebView) findViewById(R.id.myWebView);
        //  this fixes size based on screen size.
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("utf-8");




        myProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        myWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }
        });

        Get_SharedPreferences();
        Configure_ProgressBar();

        Configure_WebView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Get_SharedPreferences();
        //myWebView.loadUrl(myURL);

        // Reconfigure Progressbar
        myProgressBar.setMax(myRefreshMinutes * 60);
        myProgressBar.setProgress(0);

        TimerWebViewRefresh.cancel();

        Configure_WebView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                GoToPreferences();
                return true;
            case R.id.action_about:
                GoToAbout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void Get_SharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences( getPackageName() + Constants.PREF_FILE_NAME ,MODE_PRIVATE);
        myURL = sharedPreferences.getString(Constants.PREF_URL,Constants.NOT_APLICABLE);
        myRefreshMinutes = sharedPreferences.getInt(Constants.PREF_REFRESH_MINUTES, 5);
        // If the Preference settings are not defined then redirect the user to the preferences screen
        if( myURL.equals(Constants.NOT_APLICABLE) ) {
            GoToPreferences();
        }
    }

    private void Configure_WebView() {
        // MyTimerTask myTask = new MyTimerTask();
        TimerTask myTask = new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        myWebView.loadUrl(myURL);
                        myProgressBar.setProgress(0);

                    }

                });
            }
        };

        TimerWebViewRefresh = new Timer();

        TimerWebViewRefresh.scheduleAtFixedRate(myTask,0, myRefreshMinutes * 60000 );
    }

    private void Configure_ProgressBar() {
        // Start Progressbar to start counting
        myProgressBar.setMax(myRefreshMinutes * 60);
        TimerTask ProgressBarTask = new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        myProgressBar.incrementProgressBy(1);

                    }

                });
            }
        };
        Timer ProgresBarTimer = new Timer();

        ProgresBarTimer.scheduleAtFixedRate(ProgressBarTask,1000, 1000 );
    }

    public void onClick_Preferences(View view) {
        GoToPreferences();
    }

    private void GoToPreferences() {
        Intent intent = new Intent(this, MyPreferencesActivity.class);
        startActivity(intent);
    }

    private void GoToAbout() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void onClick_FloatingActionButton(View view) {
        GoToPreferences();
    }
}
