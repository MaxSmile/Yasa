package com.getyasa.activities.Camera.Utility;

/**
 * Created by maxim.vasilkov@gmail.com on 17/12/15.
 */
public class Constant {

    public static final int CAMERA_ORIENTATION = 90;
    public static final int FRONT_CAMERA_ROTATION = 270;
    public static final int BACK_CAMERA_ROTATION = 90;
    public static final boolean FLAG_DECODE_BITMAP = true;
    public static final boolean FLAG_SAVE_IMAGE = true;

    public static int CAMERA_PREVIEW_WIDTH = 0;
    public static int CAMERA_PREVIEW_HEIGHT = 0;

    public static int CAMERA_PICTURE_WIDTH = 0;
    public static int CAMERA_PICTURE_HEIGHT = 0;

    public static boolean BACK_CAMERA_IN_USE = true;

    //for saving the image
    public static final String DIRECTORY_NAME = "CameraDemo";
    public static final String DATE_PATTERN = "yyyyMMdd_HHmmss";
    public static final String IMAGE_POSTFIX = "IMG_";
    public static final String IMAGE_EXTENSION = ".jpg";
    public static final String VIDEO_POSTFIX = "VID_";
    public static final String VIDEO_EXTENSION = ".mp4";
    public static final String IMAGE_SAVE_SUCCESS_MESSAGE = "Image saved successfully";
    public static final String IMAGE_SAVE_FAILURE_MESSAGE = "Failed to save image";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    public enum FileSaveStatus {
        SUCCESS, FAILED
    }
}
