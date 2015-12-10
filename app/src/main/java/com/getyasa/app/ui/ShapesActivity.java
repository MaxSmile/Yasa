package com.getyasa.app.ui;

import android.os.Bundle;
import android.view.View;

import com.github.skykai.stickercamera.R;


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

        setUpActionBar(false,false,"Choose a shape");
    }

    public void onShapeClick(View v) {
        //Intent cameraIntent = new Intent(this, MakePicsActivity.class);
        //cameraIntent.putExtra("shape_id",v.getTag().toString());
        //startActivity(cameraIntent);
    }



}
