package com.getyasa.app.camera;

import android.os.Bundle;

import com.getyasa.base.YasaBaseActivity;


public class CameraBaseActivity extends YasaBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CameraManager.getInst().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CameraManager.getInst().removeActivity(this);
    }
}
