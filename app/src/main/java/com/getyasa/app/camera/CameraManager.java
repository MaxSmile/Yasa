package com.getyasa.app.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.getyasa.app.ui.ApplyEffectsActivity;
import com.getyasa.app.ui.CameraActivity;
import com.getyasa.app.model.PhotoItem;

import java.util.Stack;

/**
 * 相机管理类
 * Created by sky on 15/7/6.
 * Weibo: http://weibo.com/2030683111
 * Email: 1132234509@qq.com
 */
public class CameraManager {

    private static CameraManager mInstance;
    private Stack<Activity> cameras = new Stack<Activity>();

    public static CameraManager getInst() {
        if (mInstance == null) {
            synchronized (CameraManager.class) {
                if (mInstance == null)
                    mInstance = new CameraManager();
            }
        }
        return mInstance;
    }

    //打开照相界面
    public void openCamera(Context context) {
        Intent intent = new Intent(context, CameraActivity.class);
        context.startActivity(intent);
    }


    public void processPhotoItem(Activity activity, PhotoItem photo) {
        Uri uri = photo.getImageUri().startsWith("file:") ? Uri.parse(photo
                .getImageUri()) : Uri.parse("file://" + photo.getImageUri());
        Intent newIntent = new Intent(activity, ApplyEffectsActivity.class);
        newIntent.setData(uri);
        activity.startActivity(newIntent);

//        Uri uri = photo.getImageUri().startsWith("file:") ? Uri.parse(photo
//                .getImageUri()) : Uri.parse("file://" + photo.getImageUri());
//        if (ImageUtils.isSquare(photo.getImageUri())) {
//            Intent newIntent = new Intent(activity, ApplyEffectsActivity.class);
//            newIntent.setData(uri);
//            activity.startActivity(newIntent);
//        } else {
//            Intent i = new Intent(activity, CropPhotoActivity.class);
//            i.setData(uri);
//            //TODO稍后添加
//            activity.startActivityForResult(i, AppConstants.REQUEST_CROP);
//        }
    }

    public void close() {
        for (Activity act : cameras) {
            try {
                act.finish();
            } catch (Exception e) {

            }
        }
        cameras.clear();
    }

    public void addActivity(Activity act) {
        cameras.add(act);
    }

    public void removeActivity(Activity act) {
        cameras.remove(act);
    }



}
