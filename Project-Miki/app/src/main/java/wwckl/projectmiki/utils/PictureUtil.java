package wwckl.projectmiki.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lydialim on 5/31/15.
 */
public class PictureUtil {

    private static final String LOG_TAG = PictureUtil.class.getSimpleName();
    private static final String PICTURE_DIR_NAME = "ProjectMiki";

    /**
     * Creates media file uri to store the picture
     * @return
     * @throws IOException
     */
    public static File createFile () throws IOException {

        // media location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), PICTURE_DIR_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(LOG_TAG, "Oops! Failed create " + PICTURE_DIR_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        Log.v(LOG_TAG, mediaFile.getAbsolutePath());
        return mediaFile;
    }

    /**
     * Creates a bitmap from the picture path given. Will auto-rotate based on exif data
     *
     * @param picturePath The location of the picture
     * @return
     */
    public static Bitmap createBitmap (String picturePath) {

        if (picturePath == null || picturePath.length() < 1) {
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);

        try {
            ExifInterface exif = new ExifInterface(picturePath);
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            Log.v(LOG_TAG, "Orientation: " + exifOrientation);

            int rotate = 0;
            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
            }

            Log.v(LOG_TAG, "Rotation: " + rotate);

            if (rotate != 0) {
                // Getting width & height of the given image.
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();

                // Setting pre rotate
                Matrix matrix = new Matrix();
                matrix.preRotate(rotate);

                // Rotating Bitmap
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
            }

            // Convert to ARGB_8888, required by tess
            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

            return bitmap;

        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Rotate or conversion failed: " + Log.getStackTraceString(e));
        }

        return null;
    }

    /**
     * Rotates the bitmap image
     *
     * @param source The bitmap image that you want to rotate
     * @param angle  Rotation angle
     * @return Rotated image
     */
    public static Bitmap rotateBitmap (Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
