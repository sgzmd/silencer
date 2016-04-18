package info.romankirillov.silencer.fragments;

import android.app.Fragment;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;

import info.romankirillov.silencer.R;
import info.romankirillov.silencer.Silencer;

public class TimedSilenceFragment extends Fragment implements NumberPicker.OnValueChangeListener, View.OnClickListener {
    private NumberPicker numberPickerHours;
    private NumberPicker numberPickerMinutes;
    private ImageView buttonStart;

    private int selectedSilenceMinutes = 0;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        numberPickerHours = (NumberPicker) getActivity().findViewById(R.id.number_picker_hours);
        numberPickerMinutes = (NumberPicker) getActivity().findViewById(R.id.number_picker_minutes);
        buttonStart = (ImageView) getActivity().findViewById(R.id.timed_silence_start);

        numberPickerHours.setMinValue(0);
        numberPickerHours.setMaxValue(24);

        numberPickerMinutes.setMinValue(0);
        numberPickerMinutes.setMaxValue(59);

        numberPickerHours.setOnValueChangedListener(this);
        numberPickerMinutes.setOnValueChangedListener(this);
        buttonStart.setOnClickListener(this);

        numberPickerMinutes.setValue(15);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timed_silence, container, false);
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
        recalculateSilence();
    }

    private void recalculateSilence() {
        selectedSilenceMinutes = numberPickerHours.getValue() * 60 + numberPickerMinutes.getValue();
    }

    @Override
    public void onClick(View view) {
        new Silencer(
                this.getActivity().getApplicationContext(),
                selectedSilenceMinutes * 60,
                AudioManager.RINGER_MODE_VIBRATE).silence();
    }
}
