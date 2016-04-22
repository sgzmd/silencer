package info.romankirillov.silencer.fragments;

import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.text.DateFormatSymbols;
import java.util.GregorianCalendar;
import java.util.Locale;

import info.romankirillov.silencer.R;

public class SchedulePickerFragment extends Fragment implements View.OnClickListener {
    private static final int[] DOW_TOGGLES = {
            R.id.toggleSunday,
            R.id.toggleMonday,
            R.id.toggleTuesday,
            R.id.toggleWednesday,
            R.id.toggleThursday,
            R.id.toggleFriday,
            R.id.toggleSaturday,
            R.id.toggleSunday,
    };

    private static final String TAG = "SchedulePickerFragment";

    private int silenceFromHours = 22,
            silenceFromMinutes = 0,
            silenceToHours = 7,
            silenceToMinutes = 0;
    private EditText silenceFrom;
    private EditText silenceTo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_time_picker, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] shortWeekdays = DateFormatSymbols.getInstance().getShortWeekdays();
        Log.d(TAG, "DOW_TOGGLES: " + DOW_TOGGLES.length);
        Log.d(TAG, "shortWeekdays: " + shortWeekdays.length);

        for (int i = 0; i < shortWeekdays.length; ++i) {
            Log.d(TAG, shortWeekdays[i]);
        }

        for (int i = 0; i < DOW_TOGGLES.length; ++i) {
            Log.d(TAG, shortWeekdays[i]);
            ToggleButton toggleButton = (ToggleButton) this.getActivity().findViewById(DOW_TOGGLES[i]);
            toggleButton.setText(shortWeekdays[i]);
            toggleButton.setTextOff(shortWeekdays[i]);
            toggleButton.setTextOn(shortWeekdays[i]);
            toggleButton.setOnClickListener(this);
        }

        this.silenceFrom = (EditText) getActivity().findViewById(R.id.silenceFrom);
        this.silenceTo = (EditText) getActivity().findViewById(R.id.silenceTo);

        silenceFrom.setText("22:00");
        silenceTo.setText("07:00");
        silenceFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(
                        SchedulePickerFragment.this.getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                silenceFromHours = hourOfDay;
                                silenceFromMinutes = minute;



                                redrawFields();
                            }
                        },
                        silenceFromHours,
                        silenceFromMinutes,
                        DateFormat.is24HourFormat(SchedulePickerFragment.this.getActivity()))
                        .show();
            }
        });
        // Disallow manual input
        silenceFrom.setKeyListener(null);


        silenceTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(
                        SchedulePickerFragment.this.getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                silenceToHours = hourOfDay;
                                silenceToMinutes = minute;

                                redrawFields();
                            }
                        },
                        silenceToHours,
                        silenceToMinutes,
                        DateFormat.is24HourFormat(SchedulePickerFragment.this.getActivity()))
                        .show();
            }
        });
        // Disallow manual input
        silenceTo.setKeyListener(null);

        redrawFields();
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "Toggled: " + v.getId());
    }

    @Override
    public void onResume() {
        super.onResume();

        redrawFields();
    }

    private void redrawFields() {
        java.text.DateFormat format = DateFormat.getTimeFormat(this.getActivity());

        GregorianCalendar cal = new GregorianCalendar(Locale.getDefault());

        cal.set(GregorianCalendar.HOUR_OF_DAY, silenceFromHours);
        cal.set(GregorianCalendar.MINUTE, silenceFromMinutes);

//        format.setCalendar(cal);

        silenceFrom.setText(format.format(cal.getTime()));

        Log.d(TAG, silenceFrom.getText().toString());

        cal.set(GregorianCalendar.HOUR_OF_DAY, silenceToHours);
        cal.set(GregorianCalendar.MINUTE, silenceToMinutes);
//        format.setCalendar(cal);

        silenceTo.setText(format.format(cal.getTime()));
    }
}
