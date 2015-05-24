package wwckl.projectmiki.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import wwckl.projectmiki.R;


public class MainActivity extends AppCompatActivity {
    final int SELECT_INPUT_METHOD = 1;
    final int RESULT_LOAD_IMAGE = 2;
    String inputMethod = "";
    String picturePath = "";
    ActionMode mActionMode = null;
    Bitmap receiptImage = null;

    // retrieves the selected input method
    public void getDefaultInputMethod() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean displayWelcome = sharedPrefs.getBoolean("pref_display_welcome", true);

        if (displayWelcome) {
            startWelcomeActivity();
        }
        else {
            inputMethod = sharedPrefs.getString("pref_input_method", "");
        }
    }

    // retrieves the receipt image
    public void getReceiptImage() {
        // Retrieve image
        if (inputMethod.equalsIgnoreCase(getString(R.string.gallery))){
            startSelectFromGallery();
        }else {
            testing(" getReceiptImage not gallery");
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

    // onClick of next button
    public void startBillSplitting(View view){
        Intent intent = new Intent(this, BillSplitterActivity.class);
        startActivity(intent);
    }

    public void testing(String inputString){
        TextView t = (TextView)findViewById(R.id.textView);
        t.append(inputString);
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up listener for longClick menu for Image
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            // Called when the user long-clicks on someView
            public boolean onLongClick(View view) {
                if (mActionMode != null) {
                    return false;
                }

                // Start the CAB using the ActionMode.Callback defined above
                mActionMode = MainActivity.this.startActionMode(mActionModeCallback);
                view.setSelected(true);
                return true;
            }
        });

        // Check to run Welcome Activity
        // or retrieve default input method
        if (savedInstanceState == null) {
            getDefaultInputMethod();
        }

        testing(" onCreate");
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        // Visible/ Active activities.
        if(picturePath.isEmpty())
            getReceiptImage();
        else
          testing(picturePath);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            // Retrieve Result from Welcome Screen
            case SELECT_INPUT_METHOD:
                if (resultCode == RESULT_OK) {
                    inputMethod = data.getStringExtra("result_input_method");
                }else {
                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
                    inputMethod = sharedPrefs.getString("pref_input_method", "default3");
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

                    receiptImage = BitmapFactory.decodeFile(picturePath);
                    ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    imageView.setImageBitmap(receiptImage);
                }else{
                    testing(" result_load_image");
                }
                break;

            default:
                // Not the intended intent
                break;
        }
    }

    // Setting up call backs for Action Bar that will
    // overlay existing when long click on image
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_image, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            ImageView imageView = (ImageView) findViewById(R.id.imageView);

            switch (item.getItemId()) {
                case R.id.rotate_left:
                    receiptImage = RotateBitmap(receiptImage, 270);
                    imageView.setImageBitmap(receiptImage);
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                case R.id.rotate_right:
                    receiptImage = RotateBitmap(receiptImage, 90);
                    imageView.setImageBitmap(receiptImage);
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };
}