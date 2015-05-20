package wwckl.projectmiki.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import wwckl.projectmiki.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
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
        Intent returnIntent = new Intent();

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
