package info.romankirillov.silencer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private SeekBar seekBar;
    private TextView silenceFor;
    private int silencePeriod = 10;

    private final int FIVE_MINUTES = 5 * 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.findViewById(R.id.silence_btn).setOnClickListener(this);
        this.seekBar = (SeekBar) this.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
        this.silenceFor = ((TextView) this.findViewById(R.id.silence_for_value));

        seekBar.setProgress(1);
        redrawText();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.silence_btn) {
            AudioManager audio = (AudioManager) this.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            audio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

            AlarmManager am = (AlarmManager) this.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this.getApplicationContext(), Alarm.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intent, 0);
            am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + FIVE_MINUTES * silencePeriod * 1000 + 3000, pendingIntent);
            Toast.makeText(view.getContext(), String.format(getString(R.string.toast_text), makeSilenceForText()), Toast.LENGTH_LONG).show();
        }
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
}
