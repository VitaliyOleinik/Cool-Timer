package com.example.cooltimer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.CheckBoxPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.timer_preferences);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        int count = preferenceScreen.getPreferenceCount();

        for(int i = 0; i < count; i++){
            Preference preference = preferenceScreen.getPreference(i);

            if(!(preference instanceof CheckBoxPreference)){
                String value = sharedPreferences.getString(preference.getKey(), "");
                setPreferenceLabael(preference, value);
            }
        }

        Preference preference = findPreference("default_interval");
        preference.setOnPreferenceChangeListener(this);
    }

    private void setPreferenceLabael(Preference preference, String value){
        if(preference instanceof ListPreference){ // check for class
            ListPreference listPreference = (ListPreference) preference; // new object of class ListPreference
            int index = listPreference.findIndexOfValue(value); // index = preference timer values
            if(index >= 0){
                listPreference.setSummary(listPreference.getEntries()[index]);
            }
        } else if (preference instanceof EditTextPreference){
            preference.setSummary(value);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Preference preference = findPreference(s);
        if(!(preference instanceof CheckBoxPreference)){
            String value = sharedPreferences.getString(preference.getKey(), "");
            setPreferenceLabael(preference,value);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        // working when the settings of pref are changed
        // validation data
        Toast toast = Toast.makeText(getContext(), "Please enter an integer number", Toast.LENGTH_SHORT);
        if(preference.getKey().equals("default_interval")){
            String defaultIntervalString = (String) newValue;
            try{
                int defaultInterval = Integer.parseInt(defaultIntervalString);
            }catch (NumberFormatException e){
                toast.show();
                return false;
            }
        }
        return true;
    }
}
