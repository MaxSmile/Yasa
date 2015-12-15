package com.getyasa.yasa.fragmented.camera;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.getyasa.yasa.fragmented.MakePicsActivity;

/**
 * Created by Maxim Vasilkov maxim.vasilkov@gmai.com on 08/12/15.
 */
public class YasaCameraBaseFragment extends Fragment {

    protected MakePicsActivity parentActivity;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentActivity = (MakePicsActivity) getActivity();
    }
}
