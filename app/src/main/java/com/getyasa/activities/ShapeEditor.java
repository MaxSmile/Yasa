package com.getyasa.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.common.util.FileUtils;
import com.common.util.ImageUtils;
import com.common.util.TimeUtils;
import com.getyasa.App;
import com.getyasa.R;
import com.getyasa.app.ui.AddStickersActivity;
import com.getyasa.app.ui.ApplyEffectsActivity;
import com.getyasa.base.YasaBaseActivity;
import com.getyasa.collage.MultiTouchListener;

import java.io.File;
import java.util.Date;

/**
 * Created by maxim.vasilkov@gmail.com on 16/12/15.
 */
public class ShapeEditor extends YasaBaseActivity {

    private static final int CAMERA_REQUEST = 1888;


    ImageView image1, image2;

    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String shape_id = getIntent().getStringExtra("shape_id");
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
        setUpActionBar(true,true,"Good job!");
        if(counter==0) {
            startCamera();
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
        //image1.setImageBitmap(bitmap);
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

    void startCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            switch (counter) {
                case 0: {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");

                    image1.setImageBitmap(photo);
                    counter++;
                    startCamera();
                } break;
                case 1: {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    image2.setImageBitmap(photo);
                    counter++;
                    //startCamera()
                } break;
            }

        }
    }
}
