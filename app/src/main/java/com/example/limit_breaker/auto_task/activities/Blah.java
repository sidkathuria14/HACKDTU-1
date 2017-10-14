package com.example.sidkathuria14.settingscheckself;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;

import java.util.Map;

import static android.R.attr.key;

/**
 * Created by sidkathuria14 on 14/10/17.
 */

public class Blah extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {


    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_general);
    }


    @Override
    protected void onResume() {
        super.onResume();


        sharedPreferences = getPreferenceManager().getSharedPreferences();

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        Map<String, ?> preferencesMap = sharedPreferences.getAll();
        // iterate through the preference entries and update their summary if they are an instance of EditTextPreference
        for (Map.Entry<String, ?> preferenceEntry : preferencesMap.entrySet()) {
            if (preferenceEntry instanceof EditTextPreference) {
                updateSummary((EditTextPreference) preferenceEntry);
            }
        }
    }

    @Override
    protected void onPause() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Map<String, ?> preferencesMap = sharedPreferences.getAll();

        // get the preference that has been changed
        Object changedPreference = preferencesMap.get(key);
        // and if it's an instance of EditTextPreference class, update its summary
        if (preferencesMap.get(key) instanceof EditTextPreference) {
            updateSummary((EditTextPreference) changedPreference);
        }
    }
        private void updateSummary(EditTextPreference preference) {
            // set the EditTextPreference's summary value to its current text
            preference.setSummary(preference.getText());
            Log.d("check", "updateSummary: ");
        }
}
