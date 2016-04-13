package info.romankirillov.silencer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

public class RingerStatusChangeReceiver extends BroadcastReceiver {
    private static final String TAG = "RingerStatusChangeReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        int ringerMode = intent.getIntExtra(AudioManager.EXTRA_RINGER_MODE, 0);
        switch (ringerMode) {
            case AudioManager.RINGER_MODE_NORMAL: {
                Log.d(TAG, "Normal ringer mode enabled");
                NotificationHelper.createOrUpdateNotification(context);
                break;
            }
            case AudioManager.RINGER_MODE_VIBRATE: {
                Log.d(TAG, "Vibrate mode enabled");
                NotificationHelper.createOrUpdateNotification(context);
                break;
            }
            case AudioManager.RINGER_MODE_SILENT: {
                Log.d(TAG, "Silent mode enabled");
                NotificationHelper.createOrUpdateNotification(context);
                break;
            }
            default: {
                Log.d(TAG, "This mode is not handled: " + ringerMode);
            }

        }
    }
}
