package info.romankirillov.silencer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

/**
 * Created by kirillov on 2/13/17.
 */

public class BootComplete extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    boolean isStickyEnabled = PreferenceManager
        .getDefaultSharedPreferences(context)
        .getBoolean(SettingsActivity.KEY_ENABLE_STICKY_NOTIF, false);
    if (isStickyEnabled) {
      NotificationHelper.createOrUpdateNotification(context);
    }
  }
}
