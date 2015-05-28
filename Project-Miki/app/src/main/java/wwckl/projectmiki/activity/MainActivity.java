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
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import wwckl.projectmiki.PreferenceKeys;
import wwckl.projectmiki.R;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_IMAGE_SELECTOR     = 1;
    private static final int REQUEST_IMAGE_FROM_GALLERY = 2;
    private static final int REQUEST_TAKE_PICTURE = 3;

    /* Layout controls */
    private ImageView mImageView;
    private TextView  mTextView;

    private String     mImageSelector = "";
    private String     mPicturePath   = "";
    private ActionMode mActionMode    = null;
    private Bitmap     mReceiptImage  = null;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load layout for this activity
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.textView);

        // Set up listener for longClick menu for Image
        mImageView = (ImageView) findViewById(R.id.imageView);
        mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            // Called when the user long-clicks on someView
            public boolean onLongClick (View view) {
                if (mActionMode != null) {
                    return false;
                }

                // Start the CAB using the ActionMode.Callback defined above
                mActionMode = startActionMode(mActionModeCallback);
                view.setSelected(true);
                return true;
            }
        });

        // Check to run Welcome Activity
        // or retrieve default input method
        if (savedInstanceState == null) {
            initImageSelector();
        }
    }

    private void initImageSelector () {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean displayWelcome = sharedPrefs.getBoolean(PreferenceKeys.DISPLAY_WELCOME, true);

        if (displayWelcome) {
            startWelcomeActivity();
            return;
        }

        mImageSelector = sharedPrefs.getString(PreferenceKeys.DEFAULT_IMAGE_SELECTOR, getString(R.string.gallery));

        // Get receipt image based on selected/default input method.
        showImageSelector();
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        if (!mPicturePath.isEmpty()) {
            mTextView.setVisibility(View.GONE);
            return;
        }

        // Prompt user to Get image of receipt
        mTextView.setVisibility(View.VISIBLE);
        mTextView.setText(getString(R.string.take_a_photo_receipt)
                + "\n or \n"
                + getString(R.string.select_image_from_gallery));
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
            case R.id.action_gallery:
                startGalleryPicker();
                return true;
            case R.id.action_camera:
                startCamera();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            Log.w(LOG_TAG, "ResultCode: " + resultCode + " RequestCode : " + requestCode);

            // exit, nothing else to do here
            return;
        }

        switch (requestCode) {
            // Retrieve Result from Welcome Screen
            case REQUEST_IMAGE_SELECTOR:
                mImageSelector = data.getStringExtra("result_input_method");
                showImageSelector();
                return;

            // retrieve image from camera or gallery
            case REQUEST_TAKE_PICTURE:
            case REQUEST_IMAGE_FROM_GALLERY:
                if (data == null) {
                    return;
                }

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                                                           filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mPicturePath = cursor.getString(columnIndex);
                cursor.close();

                mReceiptImage = BitmapFactory.decodeFile(mPicturePath);
                mImageView.setImageBitmap(mReceiptImage);
                return;

            default:
                // Not the intended intent
        }
    }

    /**
     * Show either gallery or camera to select/capture the receipt image
     * This is based on what user has chosen as their default image selector
     */
    public void showImageSelector () {
        // Retrieve image
        if (mImageSelector.equalsIgnoreCase(getString(R.string.gallery))) {
            startGalleryPicker();
            return;
        }

        if (mImageSelector.equalsIgnoreCase(getString(R.string.camera))) {
            startCamera();
            return;
        }

        Log.w(LOG_TAG, "Image selector value does not match any path");
    }

    /**
     * Displays the welcome activity so that user can select the default image selector
     * on the next startup
     */
    public void startWelcomeActivity() {
        Intent intentWelcomeActivity = new Intent(MainActivity.this, WelcomeActivity.class);
        startActivityForResult(intentWelcomeActivity, REQUEST_IMAGE_SELECTOR);
    }

    /**
     * Show available gallery picker on user device to select the receipt image
     */
    private void startGalleryPicker () {
        Intent intentGallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intentGallery, REQUEST_IMAGE_FROM_GALLERY);
    }

    /**
     * Fires up the device camera so user can take the receipt picture
     */
    private void startCamera () {
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intentCamera, REQUEST_TAKE_PICTURE);
    }

    /**
     * Display the bill splitting screen where all the maths happens
     */
    public void startBillSplitting(View view){
//        Receipt.receiptBitmap = receiptImage;
        Intent intent = new Intent(this, BillSplitterActivity.class);
        startActivity(intent);
    }

    /**
     * Rotates the image
     * @param source The bitmap image that you want to rotate
     * @param angle Rotation angle
     * @return Rotated image
     */
    private Bitmap rotateBitmap (Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    // Setting up call backs for Action Bar that will
    // overlay existing when long click on image
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode (ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_image, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode (ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked (ActionMode mode, MenuItem item) {

            switch (item.getItemId()) {
                case R.id.rotate_left:
                    mReceiptImage = rotateBitmap(mReceiptImage, 270);
                    mImageView.setImageBitmap(mReceiptImage);
                    mode.finish(); // Action picked, so close the CAB
                    return true;

                case R.id.rotate_right:
                    mReceiptImage = rotateBitmap(mReceiptImage, 90);
                    mImageView.setImageBitmap(mReceiptImage);
                    mode.finish(); // Action picked, so close the CAB
                    return true;

                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode (ActionMode mode) {
            mActionMode = null;
        }
    };
}