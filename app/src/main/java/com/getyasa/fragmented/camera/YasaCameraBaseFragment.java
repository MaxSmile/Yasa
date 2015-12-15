package com.getyasa.fragmented.camera;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.getyasa.app.ui.TakePictures;


/**
 * Created by Maxim Vasilkov maxim.vasilkov@gmai.com on 08/12/15.
 */
public abstract class YasaCameraBaseFragment extends Fragment {

    protected TakePictures parentActivity;



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentActivity = (TakePictures) getActivity();
    }
}
