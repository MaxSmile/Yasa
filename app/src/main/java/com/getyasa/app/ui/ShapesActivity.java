package com.getyasa.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.getyasa.R;
import com.getyasa.app.camera.ui.CameraActivity;
import com.getyasa.base.YasaBaseActivity;


/**
 * Created by Maxim Vasilkov maxim.vasilkov@gmail.com on 25/11/15.
 * First activity of the app
 * Shows a grid of shapes
 * Choose one and go to the next step (activity) where you make pics with a device cameras
 */
public class ShapesActivity extends YasaBaseActivity {

    public static final String TAG = ShapesActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shapes);

    }

    public void onShapeClick(View v) {
        Intent cameraIntent = new Intent(this, TakePictures.class);
        cameraIntent.putExtra("shape_id",v.getTag().toString());
        startActivity(cameraIntent);
    }



}
