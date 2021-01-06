package com.pj.urlrefreshthis

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle

import android.view.View
import android.widget.EditText
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.pj.urlrefreshthis.Utils.Variables
import com.pj.urlrefreshthis.databinding.ActivityMyPreferencesBinding

class MyPreferencesActivity : AppCompatActivity(), OnSeekBarChangeListener {


     private lateinit var binding:ActivityMyPreferencesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMyPreferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Define seekbar with minutes
        binding.seekBarMinutes.setOnSeekBarChangeListener(this)

        // Get Saved Preferences
        val sharedPreferences =
            getSharedPreferences(packageName + Constants.PREF_FILE_NAME, MODE_PRIVATE)
        binding.tbURL.setText(sharedPreferences.getString(Constants.PREF_URL, "http://"))
        binding.showTimer.isChecked = sharedPreferences.getBoolean(Variables.SHARED_PREFERENCES_SHOW_TIMER, true)
        binding.showTitle.isChecked = sharedPreferences.getBoolean(Variables.SHARED_PREFERENCES_SHOW_TITLE, true)
        binding.seekBarMinutes.progress =
            sharedPreferences.getInt(
                Constants.PREF_REFRESH_MINUTES,
                6
            ) - 1 // Reduce 1 to correct value seekbar starts with 0 !!
        supportActionBar?.hide()
    }

    override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
        when (seekBar.id) {
            R.id.seekBarMinutes -> {
                val NewRefreshMinutes = i + 1 // add 1 as seekbar lowest number is 0
                binding.tbRefreshMinutes.setText(NewRefreshMinutes.toString())
            }
            else -> {
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}
    fun btnSave(view: View?) {
        val sharedPreferences =
            getSharedPreferences(packageName + Constants.PREF_FILE_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(Constants.PREF_URL, binding.tbURL.text.toString())
        editor.putInt(Constants.PREF_REFRESH_MINUTES,binding.tbRefreshMinutes.text.toString().toInt() )
        editor.putBoolean(Variables.SHARED_PREFERENCES_SHOW_TIMER,binding.showTimer.isChecked)
        editor.putBoolean(Variables.SHARED_PREFERENCES_SHOW_TITLE,binding.showTitle.isChecked)
        editor.apply()
        // return value to calling activity
        val intent = Intent()
//        intent.putExtra(Variables.KEY_DESCRIPTION, description.description)
        setResult(RESULT_OK, intent)

        finish()
    }

    fun btnCancel(view: View?) {
        finish()
    }
}