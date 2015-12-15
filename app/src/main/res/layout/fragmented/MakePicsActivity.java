package com.getyasa.yasa.fragmented;



import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.getyasa.yasa.EditShapeActivity;
import com.getyasa.yasa.R;
import com.getyasa.yasa.YasaBaseActivity;
import com.getyasa.yasa.fragmented.camera.CameraFragment;
import com.getyasa.yasa.fragmented.camera.EditSavePhotoFragment;


public class MakePicsActivity extends YasaBaseActivity {
    public static final String TAG = com.getyasa.yasa.fragmented.MakePicsActivity.class.getSimpleName();
    private static final int REQUEST_CAMERA_PERMISSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_pics);
        setUpActionBar(true,false,"Make pics");
        requestForCameraPermission();
    }

    public void launchCameraFragment() {
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, CameraFragment.newInstance(), CameraFragment.TAG)
                    .commit();
        }
    }

    public EditSavePhotoFragment fragmentSavePhoto;

    @Override
    public void onForward(View v) {
        // accept Image and pass it later

        fragmentSavePhoto.savePicture();
        super.onForward(v);


    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                final int numOfRequest = grantResults.length;
                final boolean isGranted = numOfRequest == 1
                        && PackageManager.PERMISSION_GRANTED == grantResults[numOfRequest - 1];
                if (isGranted) {
                    launchCameraFragment();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void requestForCameraPermission() {
        final String permission = Manifest.permission.CAMERA;
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                showPermissionRationaleDialog("Test", permission);
            } else {
                requestForPermission(permission);
            }
        } else {
            launchCameraFragment();
        }
    }

    private void showPermissionRationaleDialog(final String message, final String permission) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        com.getyasa.yasa.fragmented.MakePicsActivity.this.requestForPermission(permission);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

    private void requestForPermission(final String permission) {
        ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_CAMERA_PERMISSION);
    }

    public void navigateWithPhotoUri(Uri uri) {
        Intent data = new Intent(this, EditShapeActivity.class);
        data.putExtra("picture",uri);

        startActivity(data);
    }



}
