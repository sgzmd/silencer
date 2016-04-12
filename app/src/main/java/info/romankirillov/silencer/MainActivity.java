package info.romankirillov.silencer;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "MainActivity";
    public static final int NOTIFICATION_ID = 123;

    private SeekBar seekBar;
    private TextView silenceFor;
    private int silencePeriod = 10;

    private final int FIVE_MINUTES = 5 * 60;
    private RadioButton radioVibration;
    private RadioButton radioSilent;
    private CheckBox stickyNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.findViewById(R.id.silence_btn).setOnClickListener(this);
        this.seekBar = (SeekBar) this.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
        this.silenceFor = ((TextView) this.findViewById(R.id.silence_for_value));

        this.findViewById(R.id.silence_15_min).setOnClickListener(this);
        this.findViewById(R.id.silence_30_min).setOnClickListener(this);
        this.findViewById(R.id.silence_60_min).setOnClickListener(this);

        this.radioVibration = (RadioButton) this.findViewById(R.id.radio_vibro);
        this.radioSilent = (RadioButton) this.findViewById(R.id.radio_full_silent);

        boolean isDnd = getPreferences(Context.MODE_PRIVATE).getBoolean(
                getString(R.string.silence_mode_dnd), false);

        this.radioVibration.setChecked(!isDnd);
        this.radioSilent.setChecked(isDnd);

        this.stickyNotification = (CheckBox) this.findViewById(R.id.sticky_notification);
        stickyNotification.setOnCheckedChangeListener(this);
        boolean isStickyEnabled = getPreferences(Context.MODE_PRIVATE)
                .getBoolean(getString(R.string.sticky_notification), false);
        stickyNotification.setChecked(isStickyEnabled);

        if (isStickyEnabled) {
            createNotification();
        }

        seekBar.setProgress(1);
        redrawText();
    }


    private void createNotification() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Silence notifications for 30 minutes")
//                .setContentText("Click to silence notifications for 15 minutes")
                .addAction(R.mipmap.ic_launcher, "15 minutes", null)
                .addAction(R.mipmap.ic_launcher, "1 hour", null)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Click to silence notifications for 30 minutes " +
                                "or choose one of the options below"))
                .setOngoing(true);

        // Also tried this
//        notification.contentView = remoteViews;

//        notification.bigContentView = remoteViews;

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(NOTIFICATION_ID, notification.build());
    }

    private void createStickyNotification() {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//
//        builder
//                .setSmallIcon(R.drawable.notification_template_icon_bg)
////                .setContentTitle(getString(R.string.notification_title))
//                .setContentText(getString(R.string.notification_text))
//                .setContent(new RemoteViews(getPackageName(), R.layout.notification_layout))
////                .setStyle(new NotificationCompat.BigTextStyle().bigText("HELLO WORLD!!!"))
//                .setOngoing(true);
//
//        // TODO: get proper icon here
//        builder.addAction(R.drawable.abc_ic_menu_paste_mtrl_am_alpha, getString(R.string.min_30), null);
//        builder.addAction(R.drawable.abc_ic_menu_paste_mtrl_am_alpha, getString(R.string.min_60), null);



        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

//        Notification build = builder.build();
//        build.bigContentView = rv;


        // Creates an explicit intent for an ResultActivity to receive.
        Intent resultIntent = new Intent(this, MainActivity.class);

        // This ensures that the back button follows the recommended convention for the back key.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack.
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);
        remoteViews.setTextViewText(R.id.text_view, "OHHHOO");

        Notification notification = new Notification.Builder(this)
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Custom")
                .setContentIntent(resultPendingIntent)
                .build();

        notification.contentView = remoteViews;

        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.silence_btn: {
                break;
            }
            case R.id.silence_15_min: {
                silencePeriod = 3;
                break;
            }
            case R.id.silence_30_min: {
                silencePeriod = 6;
                break;
            }
            case R.id.silence_60_min: {
                silencePeriod = 12;
                break;
            }
        }

        this.seekBar.setProgress(silencePeriod);
        redrawText();
        silenceFor(view, silencePeriod);
    }

    private void silenceFor(View view, int period) {
        AudioManager audio = (AudioManager) this
                .getApplicationContext()
                .getSystemService(Context.AUDIO_SERVICE);

        audio.setRingerMode(this.radioVibration.isChecked()
                ? AudioManager.RINGER_MODE_VIBRATE : AudioManager.RINGER_MODE_SILENT);

        AlarmManager am = (AlarmManager) this
                .getApplicationContext()
                .getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this.getApplicationContext(), Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 0, intent, 0);
        am.setExact(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + FIVE_MINUTES * period * 1000 + 3000,
                pendingIntent);

        Toast.makeText(
                view.getContext(),
                String.format(getString(R.string.toast_text), makeSilenceForText()),
                Toast.LENGTH_LONG).show();
    }

    private void redrawText() {
        this.silenceFor.setText(String.format(getString(R.string.silence_for_text), makeSilenceForText()));
    }

    private String makeSilenceForText() {
        return DateUtils.formatElapsedTime(FIVE_MINUTES * silencePeriod);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        this.silencePeriod = seekBar.getProgress();
        redrawText();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void onCheckedChanged(View view) {
        SharedPreferences.Editor prefs = getPreferences(Context.MODE_PRIVATE).edit();
        switch (view.getId()) {
            case R.id.radio_vibro: {
                prefs.putBoolean(
                        getString(R.string.silence_mode_dnd),
                        false);
                break;
            }
            case R.id.radio_full_silent: {
                prefs.putBoolean(
                        getString(R.string.silence_mode_dnd),
                        true);
                break;
            }

        }
        prefs.commit();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        SharedPreferences.Editor prefs = getPreferences(Context.MODE_PRIVATE).edit();
        switch (compoundButton.getId()) {
            case R.id.sticky_notification: {
                prefs.putBoolean(
                        getString(R.string.sticky_notification),
                        b);
                Log.d(TAG, "Sticky checked: " + b);

                if (!b) {
                    NotificationManager mNotificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.cancel(NOTIFICATION_ID);
                } else {
                    createNotification();
                }

            }
        }
        prefs.commit();
    }
}
