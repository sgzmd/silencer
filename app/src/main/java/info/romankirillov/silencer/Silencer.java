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
    private final int silenceForSeconds;
    private final int ringerMode;
    private final Context appContext;

    private static final String TAG = "Silencer";

    public Silencer(Context appContext, int silenceForSeconds, int ringerMode) {
        this.appContext = appContext;
        this.silenceForSeconds = silenceForSeconds;
        this.ringerMode = ringerMode;

        Log.i(TAG, String.format(
                "Silencer constructed: period=%d seconds, mode=%d", silenceForSeconds, ringerMode));
    }

    public void silence() {
        AudioManager audio = (AudioManager) appContext
                .getSystemService(Context.AUDIO_SERVICE);

        audio.setRingerMode(ringerMode);

        AlarmManager am = (AlarmManager) appContext
                .getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(appContext, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                appContext, 0, intent, 0);

        Calendar alertTime = Calendar.getInstance();
        alertTime.add(Calendar.SECOND, this.silenceForSeconds);
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

    static String makeSilenceForText(int seconds) {
        return DateUtils.formatElapsedTime(seconds);
    }
}
