package com.getyasa.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.common.util.FileUtils;
import com.common.util.ImageUtils;
import com.common.util.TimeUtils;
import com.getyasa.R;
import com.getyasa.activities.Camera.Utility.Constant;
import com.getyasa.base.YasaBaseActivity;
import com.getyasa.collage.MultiTouchListener;

import java.io.File;
import java.util.Date;

/**
 * Created by maxim.vasilkov@gmail.com on 16/12/15.
 */
public class ShapeEditorActivity extends YasaBaseActivity {

    private static final int CAMERA_REQUEST = 1888;
    private static final int CAMERA_SURFACE = 1889;


    ImageView image1, image2;

    int counter = 0;
    String shape_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shape_id = getIntent().getStringExtra("shape_id");
        switch (shape_id) {
            case "1": {
                setContentView(R.layout._shape_activity_1);
            } break;
            case "2": {
                setContentView(R.layout._shape_activity_2);
            } break;
            case "3": {
                setContentView(R.layout._shape_activity_3);
            } break;
            case "4": {
                setContentView(R.layout._shape_activity_4);
            } break;
            case "5": {
                setContentView(R.layout._shape_activity_5);
            } break;
            case "6": {
                setContentView(R.layout._shape_activity_6);
            } break;
            case "7": {
                setContentView(R.layout._shape_activity_7);
            } break;
            case "8": {
                setContentView(R.layout._shape_activity_8);
            } break;
            default: {
                setContentView(R.layout._shape_activity_1);
            }
        }



        image1 = (ImageView)findViewById(R.id.image1);
        image2 = (ImageView)findViewById(R.id.image2);
        image1.setOnTouchListener(new MultiTouchListener());
        image2.setOnTouchListener(new MultiTouchListener());
        setUpActionBar(true,true,"");
        if(counter==0) {
            startCamera(false);
        }
    }

    @Override
    public void onForward(View v) {
        super.onForward(v);
        Bitmap bitmap;
        View v1 = findViewById(R.id.squareRoot);
        v1.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);
        new SavePicToFileTask().execute(bitmap);
    }

    private class SavePicToFileTask extends AsyncTask<Bitmap,Void,String> {
        Bitmap bitmap;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(getString(R.string.save_picture));
        }

        @Override
        protected String doInBackground(Bitmap... params) {
            String fileName = null;
            try {
                bitmap = params[0];

                String picName = TimeUtils.dtFormat(new Date(), "yyyyMMddHHmmss") + ".jpg";
                fileName = ImageUtils.saveToFile(FileUtils.getInst().getPhotoSavedPath() + "/"+ picName, false, bitmap);

            } catch (Exception e) {
                e.printStackTrace();
                toast(getString(R.string.image_processing_error), Toast.LENGTH_LONG);
            }
            return fileName;
        }

        @Override
        protected void onPostExecute(String fileName) {
            super.onPostExecute(fileName);
            dismissProgressDialog();
            navigateToNextActivity(Uri.fromFile(new File(fileName)));
        }
    }


    private void navigateToNextActivity(Uri uri) {
        Intent newIntent = new Intent(this, ApplyEffectsActivity.class);
        newIntent.setData(uri);
        this.startActivity(newIntent);
    }

    void startCamera(boolean front) {
//
//        ContentValues values = new ContentValues();
//        values.put(MediaStore.Images.Media.TITLE, "New Picture");
//        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
//        imageUri = getContentResolver().insert(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//
//        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        if (front) {
//            cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
//        }
//        startActivityForResult(cameraIntent, CAMERA_REQUEST);

        Intent cameraIntent = new Intent(this,CameraActivity.class);
        cameraIntent.putExtra("shape_id",shape_id);
        cameraIntent.putExtra("front",front);
        startActivityForResult(cameraIntent,CAMERA_SURFACE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_SURFACE  ) {
                switch (counter) {
                    case 0: {
                        image1.setImageBitmap(Constant.bitmap);

                        counter++;
                        startCamera(true);
                    }
                    break;
                    case 1: {

                        image2.setImageBitmap(Constant.bitmap);

                        counter++;
                    }
                    break;
                }
            }
        } else {
            finish();
        }
    }



}
