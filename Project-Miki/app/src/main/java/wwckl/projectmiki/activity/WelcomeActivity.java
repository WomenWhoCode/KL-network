package wwckl.projectmiki.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import wwckl.projectmiki.R;

public class WelcomeActivity extends AppCompatActivity {
    CheckBox checkBoxShowWelcome;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        checkBoxShowWelcome = (CheckBox) findViewById(R.id.cbShowWelcome);
        loadSavedPreferences();
    }

    private void loadSavedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);

        boolean checkBoxValue = sharedPreferences.getBoolean("pref_display_welcome", true);

        if (checkBoxValue) {
            checkBoxShowWelcome.setChecked(true);
        } else {
            checkBoxShowWelcome.setChecked(false);
        }
    }

    private void savePreferences(String key, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void resultInputMethodSelected(View view){
        // Save preference of check box value.
        savePreferences("pref_display_welcome", checkBoxShowWelcome.isChecked());

        Intent returnIntent = new Intent();

        // return the selected input Method to Main activity
        switch (view.getId()) {
            case R.id.btnBatch1:
                returnIntent.putExtra("result_input_method", getString(R.string.batch));
                break;
            case R.id.btnCamera1:
                returnIntent.putExtra("result_input_method", getString(R.string.camera));
                break;
            case R.id.btnGallery1:
                returnIntent.putExtra("result_input_method", getString(R.string.gallery));
                break;
        }

        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
