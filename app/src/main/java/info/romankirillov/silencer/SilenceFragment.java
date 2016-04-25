package info.romankirillov.silencer;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public class SilenceFragment extends Fragment implements TimePickerDialog.OnTimeSetListener {
    private static final String MODE_PREFERENCE_KEY =
            SilenceFragment.class.getCanonicalName() + ".mode";
    private static final String DURATION_PREFERENCE_KEY =
            SilenceFragment.class.getCanonicalName() + ".duration";
    private static final String CUSTOM_DURATION_PREFERENCE_KEY =
            SilenceFragment.class.getCanonicalName() + ".custom_durtion";
    private static final String CUSTOM_DURATION_HOURS_PREF_KEY =
            SilenceFragment.class.getCanonicalName() + ".custom_durtion.hours";
    private static final String CUSTOM_DURATION_MINUTES_PREF_KEY =
            SilenceFragment.class.getCanonicalName() + ".custom_durtion.minutes";

    public static final String PREF_KEY = SilenceFragment.class.getCanonicalName();

    private Spinner modeSpinner;
    private Spinner durationSpinner;
    private SharedPreferences preferences;
    private TextView customDurationText;
    private int durationHours = 0;
    private int durationMinutes = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.silencer_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        modeSpinner = (Spinner) getActivity().findViewById(R.id.silenceModeSpinner);
        durationSpinner = (Spinner) getActivity().findViewById(R.id.durationSpinner);
        customDurationText = (TextView) getActivity().findViewById(R.id.customDurationText);

        ArrayAdapter<String> modeAdapter = new ArrayAdapter<>(
                this.getActivity(),
                R.layout.silence_mode_spinner_item,
                new String[] {
                        getString(R.string.vibro_mode),
                        getString(R.string.silent_mode),
                });
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSpinner.setAdapter(modeAdapter);

        final ArrayAdapter<String> durationAdapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.silence_duration_spinner_item,
                new String[] {
                        getString(R.string.min_15),
                        getString(R.string.min_30),
                        getString(R.string.min_60),
                        getString(R.string.min_75),
                        getString(R.string.min_90),
                        getString(R.string.min_120),
                        getString(R.string.min_custom)
                });

        durationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        durationSpinner.setAdapter(durationAdapter);

        preferences = getActivity().getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);

        modeSpinner.setSelection(preferences.getInt(MODE_PREFERENCE_KEY, 0));
        durationSpinner.setSelection(preferences.getInt(DURATION_PREFERENCE_KEY, 0));

        durationHours = preferences.getInt(CUSTOM_DURATION_HOURS_PREF_KEY, 0);
        durationMinutes = preferences.getInt(CUSTOM_DURATION_MINUTES_PREF_KEY, 0);

        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                preferences
                        .edit()
                        .putInt(MODE_PREFERENCE_KEY, position)
                        .commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        durationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                preferences
                        .edit()
                        .putInt(DURATION_PREFERENCE_KEY, position)
                        .commit();

                if (position == durationAdapter.getCount() - 1) {
                    // Last element is Custom
                    preferences.edit().putBoolean(CUSTOM_DURATION_PREFERENCE_KEY, true).commit();
                    customDurationText.setVisibility(View.VISIBLE);
                    startTimePickerDialogue();
                } else {
                    preferences.edit().putBoolean(CUSTOM_DURATION_PREFERENCE_KEY, false).commit();
                    customDurationText.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        durationSpinner.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    private void startTimePickerDialogue() {
        new TimePickerDialog(
                this.getActivity(),
                this,
                durationHours,
                durationMinutes,
                true).show();
    }

    private void redrawCustomText() {
        // TODO: Localize me
        customDurationText.setText(
                durationHours + " hours " + durationMinutes + " minutes");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        durationHours = hourOfDay;
        durationMinutes = minute;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(CUSTOM_DURATION_HOURS_PREF_KEY, durationHours);
        editor.putInt(CUSTOM_DURATION_MINUTES_PREF_KEY, durationMinutes);
        editor.commit();

        redrawCustomText();
    }
}
