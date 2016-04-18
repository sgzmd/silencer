package info.romankirillov.silencer;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class TimeBasedSilenceActivity extends AppCompatActivity
        implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "TimeBasedSilenceActivity";

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
        setContentView(R.layout.timed_silence_activity);

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
            NotificationHelper.createOrUpdateNotification(this.getApplicationContext());
        }

        seekBar.setProgress(1);
        redrawText();
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
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(
                TimeBasedSilenceActivity.class.getName(), 0);

        SharedPreferences.Editor prefs = sharedPreferences.edit();

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
        NotificationHelper.createOrUpdateNotification(getApplicationContext());
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isCheckboxChecked) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(
                TimeBasedSilenceActivity.class.getName(), 0);

        SharedPreferences.Editor prefs = sharedPreferences.edit();

        switch (compoundButton.getId()) {
            case R.id.sticky_notification: {
                prefs.putBoolean(
                        getString(R.string.sticky_notification),
                        isCheckboxChecked);
                Log.d(TAG, "Sticky checked: " + isCheckboxChecked);

                if (!isCheckboxChecked) {
                    NotificationHelper.cancelNotification(this.getApplicationContext());
                } else {
                    NotificationHelper.createOrUpdateNotification(this.getApplicationContext());
                }

            }
        }
        prefs.commit();
    }
}
