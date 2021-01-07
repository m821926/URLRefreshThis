package com.pj.urlrefreshthis

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.pj.urlrefreshthis.Utils.Variables
import com.pj.urlrefreshthis.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private var showTitle=true
    private var showTimer=true
    private var myURL = ""
    private var myRefreshMinutes = 0
    private var TimerWebViewRefresh: Timer? = null
    private var progresBarTimer:Timer?=null
    private var newX=0f
    private var newY=0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.floatingActionButton.setOnClickListener(this)

        //  this fixes size based on screen size.
        val webSettings = binding.myWebView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false
        webSettings.setSupportZoom(true)
        webSettings.defaultTextEncodingName = "utf-8"

        binding.myWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
        Get_SharedPreferences()
        Configure_ProgressBar()
        Configure_WebView()
        configureTitle()


    }

    override fun onResumeFragments() {
    }
    private fun configureTitle() {
        if (showTitle) {
            supportActionBar?.title=myURL
            supportActionBar?.show()
            binding.floatingActionButton.visibility=View.GONE
        } else {
            supportActionBar?.hide()
            binding.floatingActionButton.visibility=View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        Get_SharedPreferences()
        setFloatingButtonPosition()
    }

    override fun onPause() {
        super.onPause()

    }

    override fun onDestroy() {
        super.onDestroy()
        saveFloatingActionButtonPosition()
    }

    override fun onStart() {
        super.onStart()
        setFloatingButtonPosition()
    }

    private fun saveFloatingActionButtonPosition() {
        val sharedPreferences =
            getSharedPreferences(packageName + Constants.PREF_FILE_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val value = this.getResources().getConfiguration().orientation;
        if (value == Configuration.ORIENTATION_PORTRAIT) {
            editor.putFloat(Variables.SHARED_PREFERENCES_FLOATING_BUTTON_Y_PORTRAIT,
                binding.floatingActionButton.posX)
            editor.putFloat(Variables.SHARED_PREFERENCES_FLOATING_BUTTON_Y_PORTRAIT,
                binding.floatingActionButton.posY
            )
        }
        else{
            editor.putFloat(Variables.SHARED_PREFERENCES_FLOATING_BUTTON_Y_LANDSCAPE,
                binding.floatingActionButton.posX)
            editor.putFloat(Variables.SHARED_PREFERENCES_FLOATING_BUTTON_Y_LANDSCAPE,
                binding.floatingActionButton.posY
            )
        }

        editor.apply()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_settings -> {
                GoToPreferences()
                true
            }
            R.id.action_about -> {
                GoToAbout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun Get_SharedPreferences() {
        val sharedPreferences =
            getSharedPreferences(packageName + Constants.PREF_FILE_NAME, MODE_PRIVATE)
        myURL = sharedPreferences.getString(Constants.PREF_URL, Constants.NOT_APLICABLE)!!
        myRefreshMinutes = sharedPreferences.getInt(Constants.PREF_REFRESH_MINUTES, 5)
        showTimer=sharedPreferences.getBoolean(Variables.SHARED_PREFERENCES_SHOW_TIMER,true)
        showTitle=sharedPreferences.getBoolean(Variables.SHARED_PREFERENCES_SHOW_TITLE,true)

        val value = this.getResources().getConfiguration().orientation;
        if (value == Configuration.ORIENTATION_PORTRAIT) {
            newX = sharedPreferences.getFloat(Variables.SHARED_PREFERENCES_FLOATING_BUTTON_X_PORTRAIT, 0f)
            newY = sharedPreferences.getFloat(Variables.SHARED_PREFERENCES_FLOATING_BUTTON_Y_PORTRAIT, 0f)
        }
        else{
            newX = sharedPreferences.getFloat(Variables.SHARED_PREFERENCES_FLOATING_BUTTON_X_LANDSCAPE, 0f)
            newY = sharedPreferences.getFloat(Variables.SHARED_PREFERENCES_FLOATING_BUTTON_Y_LANDSCAPE, 0f)
        }
        // If the Preference settings are not defined then redirect the user to the preferences screen
        if (myURL == Constants.NOT_APLICABLE) {
            GoToPreferences()
        }
    }

    fun setFloatingButtonPosition()
    {
      //      binding.floatingActionButton.setPosition(newX,newY)
    }
    private fun Configure_WebView() {
        // MyTimerTask myTask = new MyTimerTask();
        val myTask: TimerTask = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    binding.myWebView.loadUrl(myURL)
                    binding.progressBar.progress = 0
                }
            }
        }
        TimerWebViewRefresh = Timer()
        TimerWebViewRefresh!!.scheduleAtFixedRate(myTask, 0, (myRefreshMinutes * 60000).toLong())
    }

    private fun Configure_ProgressBar() {
        TimerWebViewRefresh?.cancel()
        TimerWebViewRefresh?.purge()
        TimerWebViewRefresh=null
        progresBarTimer?.cancel()
        progresBarTimer?.purge()
        progresBarTimer=null

        // Start Progressbar to start counting
        binding.progressBar.max = myRefreshMinutes * 60
        val ProgressBarTask: TimerTask = object : TimerTask() {
            override fun run() {
                runOnUiThread { binding.progressBar.incrementProgressBy(1) }
            }
        }
        progresBarTimer = Timer()
        progresBarTimer!!.scheduleAtFixedRate(ProgressBarTask, 1000, 1000)

        if(showTimer) {
            binding.progressBar.visibility = View.VISIBLE
        }
        else{
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    fun onClick_Preferences(view: View?) {
        GoToPreferences()
    }

    private fun GoToPreferences() {
        saveFloatingActionButtonPosition()

        val intent = Intent(this, MyPreferencesActivity::class.java)
        startActivityForResult(intent, Variables.PREFERENCES_RESULT)
    }

    private fun GoToAbout() {
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)

    }

    fun onClick_FloatingActionButton(view: View?) {
        GoToPreferences()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {

                Variables.PREFERENCES_RESULT -> {
                    Get_SharedPreferences()
                    Configure_ProgressBar()
                    Configure_WebView()
                    configureTitle()
                }
                else ->{
                    Get_SharedPreferences()
                    Configure_ProgressBar()
                    Configure_WebView()
                    configureTitle()
                }
             }
        }
    }
    override fun onClick(view: View?) {
        if (view != null) {
            when(view.id){
                R.id.floatingActionButton->{
                    GoToPreferences()
                }
            }
        }
    }

}