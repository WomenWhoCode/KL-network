package wwckl.projectmiki.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import wwckl.projectmiki.R;

/**
 * Created by Aryn on 5/17/15.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
