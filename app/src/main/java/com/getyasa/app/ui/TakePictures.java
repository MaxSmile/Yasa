package com.getyasa.app.ui;

import android.os.Bundle;

import com.getyasa.R;
import com.getyasa.app.camera.ui.CameraActivity;
import com.getyasa.app.camera.util.CameraHelper;

import butterknife.ButterKnife;

/**
 * Created by maxim.vasilkov@gmail.com on 11/12/15.
 */
public class TakePictures extends CameraActivity {

    public static final String TAG = TakePictures.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera1);
        mCameraHelper = new CameraHelper(this);
        ButterKnife.inject(this);
        initView();
        initEvent();

    }

}
