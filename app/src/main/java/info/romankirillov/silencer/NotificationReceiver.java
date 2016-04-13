package info.romankirillov.silencer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String TAG = NotificationReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "Snooze for: " + intent.getExtras().get(MainActivity.SNOOZE_FOR));

        int silenceForMinutes = intent.getExtras().getInt(MainActivity.SNOOZE_FOR);

        // TODO: pass over ringer mode
        new Silencer(context, silenceForMinutes * 60, AudioManager.RINGER_MODE_VIBRATE).silence();
    }
}
