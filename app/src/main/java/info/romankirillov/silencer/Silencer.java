package info.romankirillov.silencer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class Silencer {
    private static final String TAG = "Silencer";

    public static void silence(Context appContext, int silenceForSeconds, int ringerMode) {

        Log.i(TAG, String.format(
                "Silencing for: period=%d seconds, mode=%d", silenceForSeconds, ringerMode));

        AudioManager audio = (AudioManager) appContext
                .getSystemService(Context.AUDIO_SERVICE);

        audio.setRingerMode(ringerMode);

        if (silenceForSeconds == NotificationHelper.SNOOZE_INDEF) {
            Toast.makeText(appContext, "Sound snoozed indefinitely", Toast.LENGTH_LONG);
            return;
        }

        AlarmManager am = (AlarmManager) appContext
                .getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(appContext, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                appContext, 0, intent, 0);

        Calendar alertTime = Calendar.getInstance();
        alertTime.add(Calendar.SECOND, silenceForSeconds);
        alertTime.add(Calendar.SECOND, 3);

        am.setExact(
                AlarmManager.RTC_WAKEUP,
                alertTime.getTimeInMillis(),
                pendingIntent);

        Toast.makeText(
                appContext,
                String.format(
                        appContext.getString(R.string.toast_text),
                        makeSilenceForText(silenceForSeconds)),
                Toast.LENGTH_LONG).show();
    }

    public static void unsilence(Context context) {
        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }

    public static int getCurrentRingerMode(Context context) {
        return ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE)).getRingerMode();
    }

    static String makeSilenceForText(int seconds) {
        return DateUtils.formatElapsedTime(seconds);
    }
}
