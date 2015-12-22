package com.getyasa.activities.Camera;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.getyasa.activities.Camera.Utility.Constant;

import java.io.IOException;
import java.util.List;

/**
 * Created by maxim.vasilkov@gmail.com on 17/12/15.
 */
public class PreviewSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private Context context;

    private Camera.Size previewSize;
    private Camera.Size pictureSize;
    private List<Camera.Size> supportedPreviewSizes;

    public PreviewSurfaceView(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        this.context = context;
        supportedPreviewSizes = this.camera.getParameters().getSupportedPreviewSizes();
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        // deprecated setting, but required on Android versions prior to 3.0
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try{
            camera.setPreviewDisplay(holder);
        }catch (IOException e){
            camera.release();
            camera = null;
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (surfaceHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        try{
            Camera.Parameters parameters = camera.getParameters();
            parameters.setPreviewSize(previewSize.width, previewSize.height);
            parameters.setPictureSize(pictureSize.width, pictureSize.height);
            camera.setParameters(parameters);
            camera.setDisplayOrientation(Constant.CAMERA_ORIENTATION);
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);

        if (supportedPreviewSizes != null) {

            previewSize = getOptimalPreviewSize(supportedPreviewSizes, width, width);

            setCameraPictureSize();

            Constant.CAMERA_PICTURE_WIDTH = pictureSize.width;
            Constant.CAMERA_PICTURE_HEIGHT = pictureSize.height;
        }

        float ratio = 0f;

        if(previewSize != null) {
            if (previewSize.height >= previewSize.width) {
                ratio = (float) previewSize.height / previewSize.width;
            } else {
                ratio = (float) previewSize.width / previewSize.height;
            }
        }

        Constant.CAMERA_PREVIEW_WIDTH = previewSize.width;
        Constant.CAMERA_PREVIEW_HEIGHT = previewSize.height;

        int newWidth =  width;
        int newHeight = (int)(width * ratio);

        setMeasuredDimension(newWidth, newHeight);
    }

    //calculate the aspect ratio of the preview and set the aspect ratio of the captured image the same as the preview
    private void setCameraPictureSize() {

        List<Camera.Size> pictureSizes = camera.getParameters().getSupportedPictureSizes();

        float previewAspectRatio = (float) previewSize.width / previewSize.height;

        for(Camera.Size size : pictureSizes){
            float pictureAspectRatio = (float) size.width / size.height;
            double downsize = (double)  previewSize.width/size.width;
            if (downsize > MAX_DOWNSIZE) {
                //if the preview is a lot larger than our display surface ignore it
                //reason - on some phones there is not enough heap available to show the larger preview sizes
                continue;
            }
            if(previewAspectRatio == pictureAspectRatio){
                pictureSize = size;
                break;
            }
        }

        if(pictureSize == null) {
            //get the largest picture size
            pictureSize = getOptimalPreviewSize(pictureSizes,previewSize.width, previewSize.height); //pictureSizes.get(0);
        }
    }

    final double MAX_DOWNSIZE = 1.2;
    final double ASPECT_TOLERANCE = 0.25;
    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int width, int height) {

        double targetRatio = (double) width / height;
        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = height;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width * size.height;
            double downsize = (double) size.width / width;
            if (downsize > MAX_DOWNSIZE) {
                //if the preview is a lot larger than our display surface ignore it
                //reason - on some phones there is not enough heap available to show the larger preview sizes
                continue;
            }
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        //keep the max_downsize requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                double downsize = (double) size.width / width;
                if (downsize > MAX_DOWNSIZE) {
                    continue;
                }
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }

        //everything else failed, just take the closest match
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

}
