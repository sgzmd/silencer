package info.romankirillov.silencer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SilenceFragment extends Fragment {
    private Spinner modeSpinner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.silencer_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        modeSpinner = (Spinner) getActivity().findViewById(R.id.silenceModeSpinner);
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<>(
                this.getActivity(),
                R.layout.silence_mode_spinner_item,
                new String[] {
                        getString(R.string.vibro_mode),
                        getString(R.string.silent_mode),
                });
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSpinner.setAdapter(modeAdapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
