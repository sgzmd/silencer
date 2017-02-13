package info.romankirillov.silencer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    static final String KEY_ENABLE_STICKY_NOTIF = "pref_enable_sticky_notification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (KEY_ENABLE_STICKY_NOTIF.equals(key)) {
            if (sharedPreferences.getBoolean(key, false)) {
                NotificationHelper.createOrUpdateNotification(this.getApplicationContext());
            } else {
                NotificationHelper.cancelNotification(this.getApplicationContext());
            }
        }
    }
}
