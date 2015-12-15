package com.common.util;

import com.getyasa.App;

public class DistanceUtil {

    public static int getCameraAlbumWidth() {
        return (App.getApp().getScreenWidth() - App.getApp().dp2px(10)) / 4 - App.getApp().dp2px(4);
    }
    
    // Camera photo list height calculation
    public static int getCameraPhotoAreaHeight() {
        return getCameraPhotoWidth() + App.getApp().dp2px(4);
    }
    
    public static int getCameraPhotoWidth() {
        return App.getApp().getScreenWidth() / 4 - App.getApp().dp2px(2);
    }

    //Events tab grid Image Height
    public static int getActivityHeight() {
        return (App.getApp().getScreenWidth() - App.getApp().dp2px(24)) / 3;
    }
}
