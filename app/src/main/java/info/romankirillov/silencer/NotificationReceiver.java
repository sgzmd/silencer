package info.romankirillov.silencer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String TAG = NotificationReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "Snooze for: " + intent.getExtras().get(MainActivity.SNOOZE_FOR));


    }
}
