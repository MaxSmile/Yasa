package com.getyasa.activities;

import android.content.Intent;
import android.graphics.AvoidXfermode;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.getyasa.R;
import com.getyasa.YasaConstants;
import com.getyasa.activities.Camera.PreviewSurfaceView;
import com.getyasa.activities.Camera.Task.ImageDecodeTask;
import com.getyasa.activities.Camera.Utility.Constant;
import com.getyasa.base.YasaBaseActivity;

import java.io.IOException;
import java.util.List;

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

    MenuItem flashBtn;
    boolean hasFlash;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.menu_camera_flash, menu);

        flashBtn = menu.findItem(R.id.action_flash);

        hasFlash = isHasFlash();
        flashBtn.setVisible(hasFlash);

        // Return true to display menu
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Boolean front = getIntent().getBooleanExtra("front",false);
        Constant.BACK_CAMERA_IN_USE = !front;

        shape_id = getIntent().getStringExtra("shape_id");
        switch (shape_id) {

            case "3": {
                if (front) {
                    setContentView(R.layout.activity_camera_surface3_);
                } else {
                    setContentView(R.layout.activity_camera_surface);
                }
            } break;
            case "4": {
                if (front) {
                    setContentView(R.layout.activity_camera_surface4_);
                } else {
                    setContentView(R.layout.activity_camera_surface);
                }
            } break;
            case "5": {
                if (front) {
                    setContentView(R.layout.activity_camera_surface5_);
                } else {
                    setContentView(R.layout.activity_camera_surface);
                }
            } break;
            case "6": {
                if (front) {
                    setContentView(R.layout.activity_camera_surface6_);
                } else {
                    setContentView(R.layout.activity_camera_surface);
                }
            } break;
            case "7": {
                if (front) {
                    setContentView(R.layout.activity_camera_surface7_);
                } else {
                    setContentView(R.layout.activity_camera_surface);
                }
            } break;
            case "8": {
                if (front) {
                    setContentView(R.layout.activity_camera_surface8_);
                } else {
                    setContentView(R.layout.activity_camera_surface);
                }
            } break;
            default: {
                setContentView(R.layout.activity_camera_surface);
            }
        }





        setUpActionBar(true,false,"");



        previewFrame = (FrameLayout)findViewById(R.id.camera_preview);
        captureButton = (Button)findViewById(R.id.button_capture);
        switchCameraButton = (ImageView)findViewById(R.id.button_switch);
        galleryButton = (ImageView)findViewById(R.id.gallery);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    camera.takePicture(null, null, pictureCallback);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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



    /**
     * Flash switch On -> Off -> Automatic
     *
     * @param mCamera
     */
    private void turnLight(Camera mCamera) {
        if (mCamera == null || mCamera.getParameters() == null
                || mCamera.getParameters().getSupportedFlashModes() == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        String flashMode = mCamera.getParameters().getFlashMode();
        List<String> supportedModes = mCamera.getParameters().getSupportedFlashModes();
        if (Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)
                && supportedModes.contains(Camera.Parameters.FLASH_MODE_ON)) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            mCamera.setParameters(parameters);
            flashBtn.setIcon(R.drawable.camera_flash_on);
        } else if (Camera.Parameters.FLASH_MODE_ON.equals(flashMode)) {
            if (supportedModes.contains(Camera.Parameters.FLASH_MODE_AUTO)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                flashBtn.setIcon(R.drawable.camera_flash_auto);
                mCamera.setParameters(parameters);
            } else if (supportedModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                flashBtn.setIcon(R.drawable.camera_flash_off);
                mCamera.setParameters(parameters);
            }
        } else if (Camera.Parameters.FLASH_MODE_AUTO.equals(flashMode)
                && supportedModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(parameters);
            flashBtn.setIcon(R.drawable.camera_flash_off);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_flash:
                turnLight(camera);
                return true;
        }
        return false;
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

    boolean isHasFlash() {
        if (camera == null || camera.getParameters() == null
                || camera.getParameters().getSupportedFlashModes() == null) return false;
        List<String> supportedModes = camera.getParameters().getSupportedFlashModes();
        return supportedModes.size()>1;
    }

    private void attachCameraToPreview() {
        previewSurfaceView = new PreviewSurfaceView(this, camera);
        previewFrame.addView(previewSurfaceView);
        hasFlash = isHasFlash();
        if(flashBtn!=null)flashBtn.setVisible(hasFlash);
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


    private Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);

        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);

        // Reset the drawing cache background color to fully transparent
        // for the duration of this operation
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);

        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            //Log.e(TAG, "failed getViewBitmap(" + v + ")", new RuntimeException());
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        // Restore the view
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);

        return bitmap;
    }

    Bitmap composite(Bitmap bitmap1,Bitmap bitmap2) {

        Bitmap resultingImage=Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), bitmap1.getConfig());

        Canvas canvas = new Canvas(resultingImage);

        // Drawing first image on Canvas
        Paint paint = new Paint();
        canvas.drawBitmap(bitmap1, 0, 0, paint);

        // Drawing second image on the Canvas, with Xfermode set to XOR
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        canvas.drawBitmap(bitmap2, 0, 0, paint);

        return resultingImage;
    }

}
