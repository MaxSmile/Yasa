package com.getyasa.base.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.getyasa.R;


public class DialogHelper {
    private Activity    mActivity;
    private AlertDialog mAlertDialog;
    private Toast       mToast;

    public DialogHelper(Activity activity) {
        mActivity = activity;
    }

    /**
     * @param title
     * @param msg
     * @param positive
     * @param positiveListener
     * @param negative
     * @param negativeListener
     */
    public void alert(final String title, final String msg, final String positive,
                      final DialogInterface.OnClickListener positiveListener,
                      final String negative, final DialogInterface.OnClickListener negativeListener) {
        alert(title, msg, positive, positiveListener, negative, negativeListener, false);
    }

    /**
     * @param title
     * @param msg
     * @param positive
     * @param positiveListener
     * @param negative
     * @param negativeListener
     * @param isCanceledOnTouchOutside
     */
    public void alert(final String title, final String msg, final String positive,
                      final DialogInterface.OnClickListener positiveListener,
                      final String negative,
                      final DialogInterface.OnClickListener negativeListener,
                      final Boolean isCanceledOnTouchOutside) {
        dismissProgressDialog();

        mActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (mActivity == null || mActivity.isFinishing()) {
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                if (title != null) {
                    builder.setTitle(title);
                }
                if (msg != null) {
                    builder.setMessage(msg);
                }
                if (positive != null) {
                    builder.setPositiveButton(positive, positiveListener);
                }
                if (negative != null) {
                    builder.setNegativeButton(negative, negativeListener);
                }
                mAlertDialog = builder.show();
                mAlertDialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
                mAlertDialog.setCancelable(false);
            }
        });
    }

    /**
     * TOAST
     * 
     * @param msg
     * @param period
     */
    public void toast(final String msg, final int period) {
        mActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mToast = new Toast(mActivity);
                View view = LayoutInflater.from(mActivity).inflate(
                    R.layout.view_transient_notification, null);
                TextView tv = (TextView) view.findViewById(android.R.id.message);
                tv.setText(msg);
                mToast.setView(view);
                mToast.setDuration(period);
                mToast.setGravity(Gravity.CENTER, 0, 0);
                mToast.show();
            }
        });
    }

    /**
     * 
     * @param msg
     */
    public void showProgressDialog(final String msg) {
        showProgressDialog(msg, true, null);
    }

    /**
     * 
     * @param msg
     */
    public void showProgressDialog(final String msg, final boolean cancelable,
                                   final OnCancelListener cancelListener) {
        dismissProgressDialog();

        mActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (mActivity == null || mActivity.isFinishing()) {
                    return;
                }

                mAlertDialog = new GenericProgressDialog(mActivity);
                mAlertDialog.setMessage(msg);
                mAlertDialog.setCancelable(cancelable);
                mAlertDialog.setOnCancelListener(cancelListener);

                mAlertDialog.show();

                mAlertDialog.setCanceledOnTouchOutside(false);
            }
        });
    }

    public void dismissProgressDialog() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mAlertDialog != null && mAlertDialog.isShowing() && !mActivity.isFinishing()) {
                    mAlertDialog.dismiss();
                    mAlertDialog = null;
                }
            }
        });
    }

}
