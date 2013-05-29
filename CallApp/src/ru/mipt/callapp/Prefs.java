package ru.mipt.callapp;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class Prefs extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	private static final String LOG_TAG = "myLogs";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
		sp.registerOnSharedPreferenceChangeListener(this);
		sp = PreferenceManager.getDefaultSharedPreferences(Prefs.this);
		Preference pref = findPreference(getString(R.string.billing));
		pref.setSummary(((ListPreference) pref).getEntry());
		//pref = findPreference(getString(R.string.number));
		//pref.setSummary(sp.getString(getString(R.string.number), "+79176035165"));
		
		//TODO EditTextPreference 
		//TODO First Open Summary
		pref = findPreference(getString(R.string.type));
		pref.setSummary(((ListPreference) pref).getEntry());
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String key) {
		Log.d(LOG_TAG, "Changed Preferences");
		@SuppressWarnings("deprecation")
		Preference pref = findPreference(key);
		if (pref instanceof EditTextPreference) {
			EditTextPreference etp = (EditTextPreference) pref;
			pref.setSummary(etp.getText());
		} else {
			ListPreference lp = (ListPreference) pref;
			pref.setSummary(lp.getEntry());
		}
	}
}
