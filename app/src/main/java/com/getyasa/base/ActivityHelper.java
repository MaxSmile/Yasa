package com.getyasa.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

import com.getyasa.base.util.DialogHelper;


public class ActivityHelper {
    final static String TAG = ActivityHelper.class.getSimpleName();

    private Activity mActivity;


    private DialogHelper mDialogHelper;

    public ActivityHelper(Activity activity) {
        mActivity = activity;
        mDialogHelper = new DialogHelper(mActivity);
    }

    public void finish() {
        mDialogHelper.dismissProgressDialog();
    }

    /**
     *
     * @param title
     * @param msg
     * @param positive
     * @param positiveListener
     * @param negative
     * @param negativeListener
     */
    public void alert(String title, String msg, String positive,
                      DialogInterface.OnClickListener positiveListener, String negative,
                      DialogInterface.OnClickListener negativeListener) {
        mDialogHelper.alert(title, msg, positive, positiveListener, negative, negativeListener);
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
    public void alert(String title, String msg, String positive,
                      DialogInterface.OnClickListener positiveListener, String negative,
                      DialogInterface.OnClickListener negativeListener,
                      Boolean isCanceledOnTouchOutside) {
        mDialogHelper.alert(title, msg, positive, positiveListener, negative, negativeListener,
                isCanceledOnTouchOutside);
    }

    /**
     * TOAST
     *
     * @param msg
     * @param period
     */
    public void toast(String msg, int period) {
        mDialogHelper.toast(msg, period);
    }

    /**
     * Show progress dialog
     *
     * @param msg
     */
    public void showProgressDialog(String msg) {
        mDialogHelper.showProgressDialog(msg);
    }

    /**
     * Show progress dialog
     *
     * @param msg
     */
    public void showProgressDialog(final String msg, final boolean cancelable,
                                   final OnCancelListener cancelListener) {
        mDialogHelper.showProgressDialog(msg, cancelable, cancelListener);
    }

    public void dismissProgressDialog() {
        mDialogHelper.dismissProgressDialog();
    }

}
