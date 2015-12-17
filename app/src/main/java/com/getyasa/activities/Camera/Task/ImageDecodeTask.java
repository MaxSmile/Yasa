package com.getyasa.activities.Camera.Task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.getyasa.activities.Camera.Utility.BitmapHelper;
import com.getyasa.activities.Camera.Utility.Constant;

/**
 * Created by maxim.vasilkov@gmail.com on 17/12/15.
 */
public class ImageDecodeTask extends AsyncTask<Void, Void, Bitmap> {

    private Activity activity;
    private byte[] data;

    private int layoutHeight;
    private int surfaceViewHeight;

//    private Bitmap bitmap;

    public ImageDecodeTask(Activity context, byte[] data, int layoutHeight, int surfaceViewHeight){
        this.activity = context;
        this.data = data;
        this.layoutHeight = layoutHeight;
        this.surfaceViewHeight = surfaceViewHeight;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {

        //make a call to the garbage collected to force a garbage collection cycle.
        System.gc();

        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0 , data.length);

        //for back camera
        if(Constant.BACK_CAMERA_IN_USE) {
            Bitmap rotatedBitmap = BitmapHelper.rotateBitmap(bitmap, Constant.BACK_CAMERA_ROTATION, layoutHeight, surfaceViewHeight);
            bitmap = null;
            bitmap = rotatedBitmap;
        }else{
            //for front camera
            bitmap = BitmapHelper.rotateBitmap(bitmap, Constant.FRONT_CAMERA_ROTATION);
            bitmap = BitmapHelper.flip(bitmap);


            final int bitmapWidth = bitmap.getWidth();
            final int bitmapHeight = bitmap.getHeight();

            int croppedHeight = (int) ((float)(bitmapHeight * layoutHeight) / surfaceViewHeight);

            if(croppedHeight > bitmapHeight){
                //do not crop anything
                croppedHeight = bitmapHeight;
            }

            Bitmap finalBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, croppedHeight);
            bitmap = null;
            bitmap = finalBitmap;
        }

        Log.d("decode", "decode_complete");

        this.data = null;
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        Intent i = new Intent();
        i.setData(null);
        i.putExtra("image",bitmap);
        activity.setResult(Activity.RESULT_OK, i);
        activity.finish();
    }

}
