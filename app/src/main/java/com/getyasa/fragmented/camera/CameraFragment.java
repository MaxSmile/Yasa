package com.getyasa.fragmented.camera;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.common.util.DistanceUtil;
import com.getyasa.App;
import com.getyasa.R;

/**
 * Created by Maxim Vasilkov maxim.vasilkov@gmail.com on 25/11/15.
 */
public class CameraFragment extends CameraFragmentExt {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);


        adjustShape();
    }



    void adjustShape() {
        if (parentActivity!=null) {
            switch (parentActivity.shape_id) {
                case "1": {
                    ViewGroup.LayoutParams layout = cover.getLayoutParams();
                    layout.width = App.getApp().getScreenWidth() / 2;
                } break;
                case "2": {
                    ViewGroup.LayoutParams layout = cover.getLayoutParams();
                    layout.height = App.getApp().getScreenWidth() / 2;
                } break;
                case "3": {
                } break;
                case "4": {
                } break;
                case "5": {
                } break;
                case "6": {
                } break;
                case "7": {
                } break;
                case "8": {
                } break;
            }
        }
    }

    @Override
    public void onCoverClick(View v) {

    }
}