package wwckl.projectmiki.asyncTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import wwckl.projectmiki.R;
import wwckl.projectmiki.views.LoadingView;

/**
 * Created by lydialim on 5/31/15.
 *
 * Optical character recognition asynchronous task.
 * Install the language data required for OCR, initialize the OCR engine
 * and process the image using a background thread
 *
 * AsyncTask parameters:
 * 1. Params, the type of the parameters sent to the task upon execution.
 * 2. Progress, the type of the progress units published during the background computation.
 * 3. Result, the type of the result of the background computation.
 *
 * Ref : http://developer.android.com/reference/android/os/AsyncTask.html
 *
 * The code for this class was partially adapted from https://github.com/rmtheis/android-ocr/
 */
public class OcrAsyncTask extends AsyncTask<Bitmap, String, String> {

    private static final String LOG_TAG = OcrAsyncTask.class.getSimpleName();

    // Default language - English only for now
    private static final String DEFAULT_LANG = "eng";

    private Activity mActivity;
    private String   mTessdataLangFileName;
    private File     mTessdataDir;
    private int mEngineMode = TessBaseAPI.OEM_DEFAULT;

    /**
     * AsyncTask to asynchronously copy data, initialize Tesseract and process the image
     *
     * @param activity The calling activity
     */
    public OcrAsyncTask (Activity activity) {
        mActivity = activity;

        // change this to try a different engine
        mEngineMode = TessBaseAPI.OEM_TESSERACT_CUBE_COMBINED;

        mTessdataLangFileName = "tesseract-ocr-3.02." + DEFAULT_LANG + ".zip";

        // FIXME : Look into alternative for lower API
        mTessdataDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "projectmiki");
    }

    @Override
    protected String doInBackground (Bitmap... params) {

        String progressMsg = "Checking for data installation...";
        publishProgress(progressMsg);

        if (!mTessdataDir.exists() && !mTessdataDir.mkdirs()) {
            Log.e(LOG_TAG, "Couldn't make directory " + mTessdataDir);
            return null;
        }

        // check if the traineddata file has been copy
        File trainedDataFile = new File(mTessdataDir + "/tessdata", DEFAULT_LANG + ".traineddata");
        if (!trainedDataFile.exists()) {
            // File does not exist, lets copy from the assets
            installZipFromAssets(mActivity, mTessdataLangFileName, mTessdataDir);
        }

        // init engine
        progressMsg = "Initializing " + getOcrEngineModeName(mEngineMode) + " OCR engine for " + DEFAULT_LANG + "...";
        publishProgress(progressMsg);

        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.setDebug(true);
        baseApi.init(mTessdataDir.getPath(), DEFAULT_LANG, mEngineMode);

        progressMsg = "Processing receipt...";
        publishProgress(progressMsg);

        Bitmap bitmap = params[0];
        baseApi.setImage(bitmap);
        String recognizedText = baseApi.getUTF8Text();
        baseApi.end();

        // clean text
        recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
        Log.d(LOG_TAG, recognizedText);

        return recognizedText;
    }

    @Override
    protected void onProgressUpdate (String... values) {
        LoadingView.updateText(mActivity, values[0]);
    }

    @Override
    protected void onPreExecute () {
        LoadingView.show(mActivity);
    }

    @Override
    protected void onPostExecute (String result) {
        LoadingView.dismiss(mActivity);
    }

    /**
     * Unzip the given Zip file, located in application assets, into the given
     * destination file.
     *
     * @param assetFileName Name of the file in assets
     * @param destinationDir Directory to save the destination file in
     * @return
     */
    private boolean installZipFromAssets (Context context, String assetFileName, File destinationDir) {

        ZipInputStream inputStream = null;

        try {
            // Attempt to open the zip archive
            inputStream = new ZipInputStream(context.getAssets().open(assetFileName));

            // Loop through all the files and folders in the zip archive (but there should just be one)
            for (ZipEntry entry = inputStream.getNextEntry(); entry != null; entry = inputStream.getNextEntry()) {
                File destinationFile = new File(destinationDir, entry.getName());

                if (entry.isDirectory()) {
                    destinationFile.mkdirs();
                    continue;
                }

                // Note getSize() returns -1 when the zipfile does not have the size set
                long zippedFileSize = entry.getSize();

                // Create a file output stream
                FileOutputStream outputStream = new FileOutputStream(destinationFile);
                final int BUFFER = 8192;

                // Buffer the output to the file
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, BUFFER);
                int unzippedSize = 0;

                // Write the contents
                int count = 0;
                Integer percentComplete = 0;
                Integer percentCompleteLast = 0;
                byte[] data = new byte[BUFFER];
                while ((count = inputStream.read(data, 0, BUFFER)) != -1) {
                    bufferedOutputStream.write(data, 0, count);
                    unzippedSize += count;
                    percentComplete = (int) ((unzippedSize / (long) zippedFileSize) * 100);
                    if (percentComplete > percentCompleteLast) {
                        publishProgress("Uncompressing data for " + DEFAULT_LANG + "...", percentComplete.toString(), "0");
                        percentCompleteLast = percentComplete;
                    }
                }
                bufferedOutputStream.close();
            }

            // clean up
            inputStream.closeEntry();
            inputStream.close();
            inputStream = null;
            return true;
        }
        catch (IOException ioEx) {
            Log.e("ERROR", "Failed to unzipped data file: " + ioEx);
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (IOException ex) {
                    // gulp
                }
            }
        }

        return false;
    }

    /**
     * Returns a string that represents which OCR engine(s) are currently set to be run.
     * <p/>
     * There should only be one for now as we don't show these options to the user.
     * Just good to know that there are multiple engines
     *
     * @return OCR engine mode
     */
    private String getOcrEngineModeName (int ocrEngineMode) {
        String[] ocrEngineModes = mActivity.getResources().getStringArray(R.array.ocrEngineModes);
        if (ocrEngineMode == TessBaseAPI.OEM_TESSERACT_ONLY) {
            return ocrEngineModes[0];
        }

        if (ocrEngineMode == TessBaseAPI.OEM_CUBE_ONLY) {
            return ocrEngineModes[1];
        }

        if (ocrEngineMode == TessBaseAPI.OEM_TESSERACT_CUBE_COMBINED) {
            return ocrEngineModes[2];
        }

        return "default";
    }
}