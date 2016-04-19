package info.romankirillov.silencer.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import info.romankirillov.silencer.R;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
