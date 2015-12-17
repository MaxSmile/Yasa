package com.getyasa.activities.Camera.Utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;

import java.io.ByteArrayOutputStream;

/**
 * Created by maxim.vasilkov@gmail.com on 17/12/15.
 */
public class BitmapHelper {

    /**
     * Tells the decoder to sub sample the image, loading a smaller version into memory.
     *
     * @param path The path of the image to decode
     * @param reqWidth Required width of the final bitmap
     * @param reqHeight Required height of the final bitmap
     * @return Bitmap scaled to the required width and height
     */
    public static Bitmap decodeSampledBitmapFromPath(String path, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        // Uncomment this if you want to reuse already existing bitmaps. Make sure that the images are of same size if you are using this method.
//        if (Utils.hasHoneycomb()) {
//            addInBitmapOptions(options, memoryCache);
//        }

        return BitmapFactory.decodeFile(path, options);
    }



    /**
     * Tells the decoder to sub sample the image, loading a smaller version into memory.
     *
     * @param data The byte array containing the image to decode
     * @param reqWidth Required width of the final bitmap
     * @param reqHeight Required height of the final bitmap
     * @return Bitmap scaled to the required width and height
     */
    public static Bitmap decodeSampledBitmapFromByteArray(byte[] data, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    /**
     * Calculates the sample size needed to scale the bitmap
     * For example, an image with resolution 2048x1536 that is decoded with an inSampleSize of 4 produces a bitmap of approximately 512x384.
     *
     * @param options Options object to get the width and height of bitmap.
     * @param reqWidth Required width of the final bitmap
     * @param reqHeight Required height of the final bitmap
     * @return The calculated sample size.
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static byte[] getByteArrayFromBitmap(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        BitmapHelper.recycleBitmap(bitmap);
        return byteArray;
    }


    public static Bitmap rotateBitmap(Bitmap source, int angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap bitmap =  Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        recycleBitmap(source);
        return bitmap;
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle, int layoutHeight, int surfaceViewHeight)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        int croppedWidth = (int) ((float)(source.getWidth() * layoutHeight) / surfaceViewHeight);

        int bitmapWidth = source.getWidth();

        if(croppedWidth > bitmapWidth){
            //do not crop anything
            croppedWidth = bitmapWidth;
        }
        Bitmap bitmap =  Bitmap.createBitmap(source, 0, 0, croppedWidth, source.getHeight(), matrix, true);
        recycleBitmap(source);
        return bitmap;
    }

    public static Bitmap flip(Bitmap src)
    {
        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);
        Bitmap dst = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, false);
        dst.setDensity(DisplayMetrics.DENSITY_DEFAULT);
        recycleBitmap(src);
        return dst;
    }

    public static void recycleBitmap(Bitmap bitmap){
        bitmap.recycle();
        bitmap = null;
    }

}
