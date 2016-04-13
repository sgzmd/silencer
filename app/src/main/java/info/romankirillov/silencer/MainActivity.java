package info.romankirillov.silencer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "MainActivity";
    public static final int NOTIFICATION_ID = 123;

    static final String SNOOZE_FOR = "info.romankirillov.silencer.snoozefor";
    static final int SNOOZE_15 = 15;
    static final int SNOOZE_30 = 30;

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
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_silent)
                .setContentTitle(getString(R.string.notification_title))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOngoing(true);

        RemoteViews rv = new RemoteViews(getPackageName(), R.layout.notification);
        rv.setImageViewResource(R.id.notification_icon, R.drawable.ic_silent);

        rv.setOnClickPendingIntent(R.id.btn_notification_30min, makeSnoozeIntent(SNOOZE_30));
        rv.setOnClickPendingIntent(R.id.btn_notification_15min, makeSnoozeIntent(SNOOZE_15));
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Notification n = notification.build();
        n.contentView = rv;

        mNotificationManager.notify(NOTIFICATION_ID, n);
    }

    private PendingIntent makeSnoozeIntent(int snoozeFor) {
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra(SNOOZE_FOR, snoozeFor);
        intent.setAction(Integer.toString(snoozeFor));
        return PendingIntent.getBroadcast(
                this.getApplicationContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
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

        new Silencer(
                this.getApplicationContext(),
                getSilencePeriodSeconds(),
                this.radioVibration.isChecked()
                        ? AudioManager.RINGER_MODE_VIBRATE : AudioManager.RINGER_MODE_SILENT)
                .silence();
    }

    private int getSilencePeriodSeconds() {
        return FIVE_MINUTES * silencePeriod;
    }


    private void redrawText() {
        this.silenceFor.setText(String.format(
                getString(R.string.silence_for_text),
                Silencer.makeSilenceForText(getSilencePeriodSeconds())));
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
