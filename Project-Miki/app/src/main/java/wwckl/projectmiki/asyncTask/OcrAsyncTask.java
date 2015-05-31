package wwckl.projectmiki.asyncTask;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import wwckl.projectmiki.views.LoadingView;

/**
 * Created by lydialim on 5/31/15.
 *
 * Optical character recognition asynchronous task
 * 1. Params, the type of the parameters sent to the task upon execution.
 * 2. Progress, the type of the progress units published during the background computation.
 * 3. Result, the type of the result of the background computation.
 *
 * ref : http://developer.android.com/reference/android/os/AsyncTask.html
 */
public class OcrAsyncTask extends AsyncTask<Bitmap, Void, String> {

    private static final String LOG_TAG = OcrAsyncTask.class.getSimpleName();

    // TODO : Look for a new home
    private static final String DATA_PATH = "tessdata";
    private static final String LANGUAGE  = "eng"; // english only for now

    private Activity mActivity;

    public OcrAsyncTask (Activity activity) {
        mActivity = activity;
    }

    @Override
    protected String doInBackground (Bitmap... params) {

        Bitmap bitmap = params[0];

        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.setDebug(true);
        baseApi.init(DATA_PATH, LANGUAGE);
        baseApi.setImage(bitmap);
        String recognizedText = baseApi.getUTF8Text();
        baseApi.end();

        // clean up
        recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
        Log.d(LOG_TAG, recognizedText);

        return recognizedText;
    }

    @Override
    protected void onPreExecute () {
        if (mActivity != null) {
            LoadingView.show(mActivity);
        }
    }

    @Override
    protected void onPostExecute (String result) {
        if (mActivity != null) {
            LoadingView.hide(mActivity);
        }
    }
}
