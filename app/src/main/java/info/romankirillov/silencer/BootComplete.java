package info.romankirillov.silencer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by kirillov on 2/13/17.
 */

public class BootComplete extends BroadcastReceiver {

  private static final String TAG = "BootComplete";

  @Override
  public void onReceive(Context context, Intent intent) {

    Log.w(TAG, "BootComplete received");

    boolean isStickyEnabled = PreferenceManager
        .getDefaultSharedPreferences(context)
        .getBoolean(SettingsActivity.KEY_ENABLE_STICKY_NOTIF, false);
    if (isStickyEnabled) {
      Log.d(TAG, "Sticky enabled");
      NotificationHelper.createOrUpdateNotification(context);
    }
  }
}
