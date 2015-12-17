package com.getyasa.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.common.util.FileUtils;
import com.getyasa.R;
import com.getyasa.YasaConstants;
import com.getyasa.activities.Camera.PreviewSurfaceView;
import com.getyasa.activities.Camera.Task.ImageDecodeTask;
import com.getyasa.activities.Camera.Task.SaveImageTask;
import com.getyasa.activities.Camera.Utility.Constant;
import com.getyasa.app.ui.ApplyEffectsActivity;
import com.getyasa.base.YasaBaseActivity;

import java.io.IOException;

/**
 * Created by maxim.vasilkov@gmail.com on 17/12/15.
 */
public class CameraActivity extends YasaBaseActivity {

    private Camera camera;
    private PreviewSurfaceView previewSurfaceView;
    private FrameLayout previewFrame;
    private Button captureButton;
    private ImageView switchCameraButton;
    private ImageView galleryButton;
    String shape_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Boolean front = getIntent().getBooleanExtra("front",false);
        Constant.BACK_CAMERA_IN_USE = !front;
        setContentView(R.layout.activity_camera_surface);
        setUpActionBar(true,false,"");
        shape_id = getIntent().getStringExtra("shape_id");




        previewFrame = (FrameLayout)findViewById(R.id.camera_preview);
        captureButton = (Button)findViewById(R.id.button_capture);
        switchCameraButton = (ImageView)findViewById(R.id.button_switch);
        galleryButton = (ImageView)findViewById(R.id.gallery);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, pictureCallback);
            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, YasaConstants.REQUEST_PICK);
            }
        });

        switchCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.BACK_CAMERA_IN_USE) {
                    showFrontCamera();
                } else {
                    showBackCamera();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent result) {
        if (requestCode == YasaConstants.REQUEST_PICK && resultCode == RESULT_OK) {
            Uri uri = result.getData();

            try {
                Constant.bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                Intent i = new Intent();
                i.setData(uri);
                i.putExtra("image","image");
                setResult(RESULT_OK, i);
                finish();
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
            setResult(RESULT_CANCELED, null);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Constant.BACK_CAMERA_IN_USE) {
            showBackCamera();
        } else {
            showFrontCamera();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        removePreview();
        releaseCamera();
    }

    private void showBackCamera() {
        releaseCamera();
        camera = getCameraInstance(Camera.CameraInfo.CAMERA_FACING_BACK);
        if (camera != null) {
            if (previewSurfaceView != null) {
                removePreview();
            }
            Constant.BACK_CAMERA_IN_USE = true;
            attachCameraToPreview();
        }
    }

    private void showFrontCamera() {
        int numberOfCameras = Camera.getNumberOfCameras();

        if (numberOfCameras > 1) {
            releaseCamera();
            camera = getCameraInstance(Camera.CameraInfo.CAMERA_FACING_FRONT);
            Constant.BACK_CAMERA_IN_USE = false;
            removePreview();
            attachCameraToPreview();
        } else {
            Toast.makeText(this, "Front camera not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void attachCameraToPreview() {
        previewSurfaceView = new PreviewSurfaceView(this, camera);
        previewFrame.addView(previewSurfaceView);
    }

    private void removePreview() {
        previewFrame.removeView(previewSurfaceView);
    }

    private void releaseCamera(){
        if (camera != null){
            camera.release();        // release the camera for other applications
            camera = null;
        }
    }

    public Camera getCameraInstance(int cameraId){
        Camera c = null;
        try {
            c = Camera.open(cameraId); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
            e.printStackTrace();
        }
        return c; // returns null if camera is unavailable
    }

    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            captureButton.setEnabled(false);

            camera.stopPreview();

            new ImageDecodeTask(CameraActivity.this, data, previewFrame.getHeight(), previewSurfaceView.getHeight()).execute();

        }
    };





}
