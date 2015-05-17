package wwckl.projectmiki.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import wwckl.projectmiki.fragment.SettingsFragment;

/**
 * Created by Aryn on 5/17/15.
 * To accomodate user preferences.
 */
public class SettingsActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
