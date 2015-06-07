package wwckl.projectmiki.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import wwckl.projectmiki.PreferenceKeys;
import wwckl.projectmiki.R;

public class WelcomeActivity extends AppCompatActivity {

    private CheckBox mCheckBoxShowWelcome;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mCheckBoxShowWelcome = (CheckBox) findViewById(R.id.cbShowWelcome);
        loadSavedPreferences();
    }

    /**
     * Loads user preference
     */
    private void loadSavedPreferences () {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);

        boolean checkBoxValue = sharedPreferences.getBoolean(PreferenceKeys.DISPLAY_WELCOME, true);
        mCheckBoxShowWelcome.setChecked(checkBoxValue);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        // Action bar menu.
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Save user preference for boolean type
     * @param key Preference Key
     * @param value The value of the preference
     */
    private void savePreferencesBool(String key, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * Save user preference for string type
     * @param key Preference Key
     * @param value The value of the preference
     */
    private void savePreferencesString(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /***
     * Save user preferred picture mode to SharedPreferences
     * and returns back to the parent activity
     *
     * @param view
     */
    public void savePictureMode (View view){
        // Save preference of check box value.
        savePreferencesBool(PreferenceKeys.DISPLAY_WELCOME, mCheckBoxShowWelcome.isChecked());

        Intent returnIntent = new Intent();
        String pictureMode = "";

        // return the selected input Method to Main activity
        switch (view.getId()) {
            case R.id.btnBatch1:
                pictureMode = getString(R.string.batch);
                break;
            case R.id.btnCamera1:
                pictureMode = getString(R.string.camera);
                break;
            case R.id.btnGallery1:
                pictureMode = getString(R.string.gallery);
                break;
        }

        savePreferencesString(PreferenceKeys.DEFAULT_PICTURE_RETRIEVE_MODE, pictureMode);

        returnIntent.putExtra("result_input_method", pictureMode);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
