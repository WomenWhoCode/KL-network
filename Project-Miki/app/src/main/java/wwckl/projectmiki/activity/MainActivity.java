package wwckl.projectmiki.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
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

import java.io.File;
import java.io.IOException;

import wwckl.projectmiki.PreferenceKeys;
import wwckl.projectmiki.R;
import wwckl.projectmiki.asyncTask.OcrAsyncTask;
import wwckl.projectmiki.utils.PictureUtil;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_PICTURE_RETRIEVAL_PREF = 1;
    private static final int REQUEST_GALLERY                = 2;
    private static final int REQUEST_TAKE_PICTURE           = 3;

    /* Layout controls */
    private ImageView mImageView;
    private TextView  mTextView;

    private ActionMode                      mActionMode;
    private Bitmap                          mReceiptImage;
    private RotatePictureActionModeCallback mActionModeCallback;

    private String mPictureRetrievalPref = "";
    private String mPicturePath          = "";

    private Uri mPictureUri; // file url to store image/video

    private OcrAsyncTask mOcrAsyncTask;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load layout for this activity
        setContentView(R.layout.activity_main);

        mActionModeCallback = new RotatePictureActionModeCallback();

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
            getDefaultOrRetrievePicture();
        }
    }

    private void getDefaultOrRetrievePicture () {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean displayWelcome = sharedPrefs.getBoolean(PreferenceKeys.DISPLAY_WELCOME, true);

        if (displayWelcome) {
            startWelcomeActivity();
            return;
        }

        mPictureRetrievalPref = sharedPrefs.getString(PreferenceKeys.DEFAULT_PICTURE_RETRIEVE_MODE, getString(R.string.gallery));
        // Get receipt picture based on selected/default input method.
        retrievePicture();
    }

    @Override
    public void onResume () {
        super.onResume();  // Always call the superclass method first

        if (mPictureUri != null) {
            mTextView.setVisibility(View.GONE);
            return;
        }

        // Prompt user to Get picture of receipt
        mTextView.setVisibility(View.VISIBLE);
        mTextView.setText(getString(R.string.take_a_photo_receipt)
                          + "\n or \n"
                          + getString(R.string.select_image_from_gallery));
    }

    @Override
    protected void onDestroy () {

        // terminate any running tasks
        if (mOcrAsyncTask != null && mOcrAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            mOcrAsyncTask.cancel(true);
        }

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            case R.id.action_gallery:
                startGallery();
                return true;
            case R.id.action_camera:
                startCamera();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation changes
        outState.putParcelable("picture_uri", mPictureUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        mPictureUri = savedInstanceState.getParcelable("picture_uri");
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            Log.w(LOG_TAG, "ResultCode: " + resultCode + " RequestCode : " + requestCode);

            // exit, nothing else to do here
            return;
        }

        switch (requestCode) {
            // Retrieve Result from Welcome Screen
            case REQUEST_PICTURE_RETRIEVAL_PREF:
                mPictureRetrievalPref = data.getStringExtra("result_input_method");
                retrievePicture();
                return;

            // retrieve image from camera or gallery
            case REQUEST_TAKE_PICTURE:
            case REQUEST_GALLERY:
                if (mPictureUri == null) {
                    showAlert("Picture path not found");
                    return;
                }

                mReceiptImage = PictureUtil.createBitmap(mPictureUri.getPath());
                mImageView.setImageBitmap(mReceiptImage);

                mOcrAsyncTask = new OcrAsyncTask(this);
                mOcrAsyncTask.execute(mReceiptImage);

                return;

            default:
                // Not the intended intent
        }
    }

    /**
     * Show either gallery or camera to select/capture the receipt image
     * This is based on what user has chosen as their default image selector
     */
    public void retrievePicture () {
        // Retrieve image
        if (mPictureRetrievalPref.equalsIgnoreCase(getString(R.string.gallery))) {
            startGallery();
            return;
        }

        if (mPictureRetrievalPref.equalsIgnoreCase(getString(R.string.camera))) {
            startCamera();
            return;
        }

        Log.d(LOG_TAG, "Image selector value does not match any path");
    }

    /**
     * Displays the welcome activity so that user can select the default image selector
     * on the next startup
     */
    public void startWelcomeActivity () {
        Intent intentWelcomeActivity = new Intent(MainActivity.this, WelcomeActivity.class);
        startActivityForResult(intentWelcomeActivity, REQUEST_PICTURE_RETRIEVAL_PREF);
    }

    /**
     * Show available gallery picker on user device to select the receipt image
     */
    private void startGallery () {
        Intent intentGallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intentGallery, REQUEST_GALLERY);
    }

    /**
     * Fires up the device camera so user can take the receipt picture
     */
    private void startCamera () {

        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Create the File where the photo should go
        File pictureFile = null;
        try {
            pictureFile = PictureUtil.createFile();
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        mPictureUri = Uri.fromFile(pictureFile);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, mPictureUri);
        startActivityForResult(intentCamera, REQUEST_TAKE_PICTURE);
    }

    /**
     * Display the bill splitting screen where all the maths happens
     */
    public void startBillSplitting (View view) {
        Intent intent = new Intent(this, BillSplitterActivity.class);
        startActivity(intent);
    }

    /**
     * Displays an alert dialog
     *
     * @param alertMessage The alert message to be shown to the user
     */
    private void showAlert (String alertMessage) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage(alertMessage);
        alertBuilder.setCancelable(true);
        alertBuilder.setPositiveButton("Okay",
                                       new DialogInterface.OnClickListener() {
                                           public void onClick (DialogInterface dialog, int id) {
                                               dialog.cancel();
                                           }
                                       });

        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    /**
     * Setting up call backs for Action Bar that will
     * overlay existing when long click on image
     */
    private class RotatePictureActionModeCallback implements ActionMode.Callback {
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
                    mReceiptImage = PictureUtil.rotateBitmap(mReceiptImage, 270);
                    mImageView.setImageBitmap(mReceiptImage);
                    mode.finish(); // Action picked, so close the CAB
                    return true;

                case R.id.rotate_right:
                    mReceiptImage = PictureUtil.rotateBitmap(mReceiptImage, 90);
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
    }
}