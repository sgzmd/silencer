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

        Log.d(TAG, "Snooze for: " + intent.getExtras().get(NotificationHelper.SNOOZE_FOR));

        int silenceForMinutes = intent.getExtras().getInt(NotificationHelper.SNOOZE_FOR);
        int desiredSilentMode = intent.getExtras().getInt(NotificationHelper.DESIRED_SILENT_MODE);

        switch (silenceForMinutes) {
            case NotificationHelper.SNOOZE_INDEF:
                Silencer.silence(context, NotificationHelper.SNOOZE_INDEF, desiredSilentMode);
                break;
            case NotificationHelper.UNSNOOZE:
                Silencer.unsilence(context);
                break;
            default:
                Silencer.silence(context, silenceForMinutes * 60, desiredSilentMode);
        }
    }
}
