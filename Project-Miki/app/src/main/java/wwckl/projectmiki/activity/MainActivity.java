package wwckl.projectmiki.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import wwckl.projectmiki.R;


public class MainActivity extends AppCompatActivity {
    final int SELECT_INPUT_METHOD = 1;
    final int RESULT_LOAD_IMAGE = 2;
    String inputMethod = "";
    String picturePath = "";

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
        Intent intentInputMethod = new Intent(MainActivity.this, WelcomeActivity.class);
        startActivityForResult(intentInputMethod, SELECT_INPUT_METHOD);
    }

    // Select Image from gallery
    public void startSelectFromGallery(){
        Intent intentGallery = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intentGallery, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check to run Welcome Activity
        // or retrieve default input method
        if (savedInstanceState == null)
            getDefaultInputMethod();

        if(inputMethod.equalsIgnoreCase(getString(R.string.gallery))){
            startSelectFromGallery();
        }
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
            // Retrieve Result from Welcome Screen
            case SELECT_INPUT_METHOD:
                if (resultCode == RESULT_OK) {
                    inputMethod = data.getStringExtra("result_input_method");
                }
                break;

            // Retrieve Image from Gallery
            case RESULT_LOAD_IMAGE:
                if (resultCode == RESULT_OK && data != null) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                }else{
                    // no image, prompt for input method.
                    startWelcomeActivity();
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
