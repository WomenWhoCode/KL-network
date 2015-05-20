package wwckl.projectmiki.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import wwckl.projectmiki.R;


public class MainActivity extends AppCompatActivity {
    final int SELECT_INPUT_METHOD = 1;
    String inputMethod = "";

    public void testing(String inputString){
        TextView t = (TextView)findViewById(R.id.textView);
        t.append(inputString);
    }

    // returns the selected input method
    public void getDefaultInputMethod() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean displayWelcome = sharedPrefs.getBoolean("pref_display_welcome", true);

        if (displayWelcome) {
            startWelcomeActivity();
        }
        else {
            inputMethod = sharedPrefs.getString("pref_input_method", getString(R.string.camera));
        }
    }

    // display welcome activity and returns with result
    public void startWelcomeActivity(){
        Intent selectInputMethodIntent = new Intent(this, WelcomeActivity.class);
        startActivityForResult(selectInputMethodIntent, SELECT_INPUT_METHOD);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check to run Welcome Activity
        // or retrieve default input method
        if (savedInstanceState == null)
            getDefaultInputMethod();

        testing(inputMethod);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case SELECT_INPUT_METHOD:
                if (resultCode == RESULT_OK) {
                    inputMethod = data.getStringExtra("result_input_method");
                }
                break;
            default:
                // Not the intended intent
                break;
        }
    }

    // onClick of next button
    public void startBillSplitting(View view){
        Intent intent = new Intent(this, BillSplitterActivity.class);
        startActivity(intent);
    }
}
