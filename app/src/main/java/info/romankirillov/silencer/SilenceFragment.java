package info.romankirillov.silencer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SilenceFragment extends Fragment {
    private static final String MODE_PREFERENCE_KEY =
            SilenceFragment.class.getCanonicalName() + ".mode";
    private static final String DURATION_PREFERENCE_KEY =
            SilenceFragment.class.getCanonicalName() + ".duration";
    public static final String PREF_KEY = SilenceFragment.class.getCanonicalName();

    private Spinner modeSpinner;
    private Spinner durationSpinner;
    private SharedPreferences preferences;

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

        ArrayAdapter<String> modeAdapter = new ArrayAdapter<>(
                this.getActivity(),
                R.layout.silence_mode_spinner_item,
                new String[] {
                        getString(R.string.vibro_mode),
                        getString(R.string.silent_mode),
                });
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSpinner.setAdapter(modeAdapter);

        ArrayAdapter<String> durationAdapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.silence_duration_spinner_item,
                new String[] {
                        getString(R.string.min_15),
                        getString(R.string.min_30),
                        getString(R.string.min_60)
                });

        durationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        durationSpinner.setAdapter(durationAdapter);

        preferences = getActivity().getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);

        modeSpinner.setSelection(preferences.getInt(MODE_PREFERENCE_KEY, 0));
        durationSpinner.setSelection(preferences.getInt(DURATION_PREFERENCE_KEY, 0));

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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
