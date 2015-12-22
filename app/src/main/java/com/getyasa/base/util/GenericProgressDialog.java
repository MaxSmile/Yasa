package com.getyasa.base.util;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.getyasa.R;


public class GenericProgressDialog extends AlertDialog {
    private ProgressBar  mProgress;
    private TextView     mMessageView;
    private CharSequence mMessage;
    private boolean      mIndeterminate;

    public GenericProgressDialog(Context context) {
        super(context/*,R.style.Float*/);
    }

    public GenericProgressDialog(Context context, int theme) {
        super(context,/*, R.style.Float*/theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_progress_dialog);
        mProgress = (ProgressBar) findViewById(android.R.id.progress);
        mMessageView = (TextView) findViewById(R.id.message);

        ImageView img = (ImageView) findViewById(R.id.yaicon1p);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        img.startAnimation(rotateAnimation);

        setMessageAndView();
        setIndeterminate(mIndeterminate);
    }

    private void setMessageAndView() {
        mMessageView.setText(mMessage);

        if (mMessage == null || "".equals(mMessage)) {
            mMessageView.setVisibility(View.GONE);
        }

    }

    @Override
    public void setMessage(CharSequence message) {
        mMessage = message;
    }



    public void setIndeterminate(boolean indeterminate) {
        if (mProgress != null) {
            mProgress.setIndeterminate(indeterminate);
        } else {
            mIndeterminate = indeterminate;
        }
    }
}
