package com.pj.urlrefreshthis;

import android.webkit.WebView;

import java.util.TimerTask;

public class MyTimerTask extends TimerTask {
    private WebView myWebView;

    public void run(){
        myWebView.reload();
    }

    public void setWebView(WebView thiswebView)
    {
        myWebView = thiswebView;
    }
}
