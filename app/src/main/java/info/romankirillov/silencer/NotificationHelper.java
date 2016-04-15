package info.romankirillov.silencer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

public class NotificationHelper {
    public static final int NOTIFICATION_ID = 123;

    static final int UNSNOOZE = -1;
    static final int SNOOZE_15 = 15;
    static final int SNOOZE_30 = 30;
    static final int SNOOZE_INDEF = 0;

    static final String SNOOZE_FOR = "info.romankirillov.silencer.snoozefor";
    static final String DESIRED_SILENT_MODE = "info.romankirillov.silencer.silentmode";

    private static final String TAG = "NotificationHelper";

    static void createOrUpdateNotification(Context context) {
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentTitle(context.getString(R.string.notification_title))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOngoing(true);

        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int currentRingerMode = audio.getRingerMode();

        Intent mainActivityIntent = new Intent(context, MainActivity.class);
        PendingIntent activityIntent = PendingIntent.getActivity(context, 0, mainActivityIntent, 0);

        notification.setContentIntent(activityIntent);

        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.notification);
        rv.setImageViewResource(R.id.notification_icon, getNotificationIconId(currentRingerMode));

        int iconClickSnoozePeriod = SNOOZE_INDEF;
        if (currentRingerMode != AudioManager.RINGER_MODE_NORMAL) {
            iconClickSnoozePeriod = UNSNOOZE;
        }

        rv.setOnClickPendingIntent(
                R.id.btn_notification_30min,
                makeSnoozeIntent(context, SNOOZE_30));
        rv.setOnClickPendingIntent(
                R.id.btn_notification_15min,
                makeSnoozeIntent(context, SNOOZE_15));
        rv.setOnClickPendingIntent(
                R.id.notification_icon,
                makeToggleSoundIntent(context, currentRingerMode));

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notification.setContent(rv);

        mNotificationManager.notify(NOTIFICATION_ID, notification.build());
    }

    static void cancelNotification(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NotificationHelper.NOTIFICATION_ID);
    }

    private static int getNotificationIconId(int ringerMode) {
        switch (ringerMode) {
            case AudioManager.RINGER_MODE_SILENT:
                return R.mipmap.ic_flat_silent;
            case AudioManager.RINGER_MODE_VIBRATE:
                return R.mipmap.ic_flat_vibro;
            default:
                return R.mipmap.ic_flat_sound;
        }
    }

    private static int getMutedRingerMode(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                MainActivity.class.getName(), 0);

        String key = context.getString(R.string.silence_mode_dnd);
        if (sharedPreferences.getBoolean(key, false)) {
            return AudioManager.RINGER_MODE_SILENT;
        } else {
            return AudioManager.RINGER_MODE_VIBRATE;
        }
    }

    private static PendingIntent makeToggleSoundIntent(Context context, int currentRingerMode) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra(SNOOZE_FOR, SNOOZE_INDEF);

        int desiredSilentMode = AudioManager.RINGER_MODE_NORMAL;

        switch (currentRingerMode) {
            case AudioManager.RINGER_MODE_NORMAL:
                desiredSilentMode = AudioManager.RINGER_MODE_VIBRATE;
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                desiredSilentMode = AudioManager.RINGER_MODE_SILENT;
                break;
            case AudioManager.RINGER_MODE_SILENT:
                desiredSilentMode = AudioManager.RINGER_MODE_NORMAL;
                break;
        }

        intent.putExtra(DESIRED_SILENT_MODE, desiredSilentMode);
        intent.setAction(Integer.toString(SNOOZE_INDEF));

        return PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static PendingIntent makeSnoozeIntent(Context context, int snoozeFor) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra(SNOOZE_FOR, snoozeFor);
        intent.putExtra(DESIRED_SILENT_MODE, getMutedRingerMode(context));
        intent.setAction(Integer.toString(snoozeFor));
        return PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
