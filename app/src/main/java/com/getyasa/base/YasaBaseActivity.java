package com.getyasa.base;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.customview.CommonTitleBar;
import com.getyasa.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by sky on 15/7/6.
 */
public class YasaBaseActivity extends AppCompatActivity implements ActivityResponsable {
    protected Bundle savedInstanceState;
    private ImageButton back_btn,forward_btn;
    private TextView title;

    protected CommonTitleBar titleBar;

    protected ActivityHelper  mActivityHelper;

    protected int transition_in = R.anim.slide_in;
    protected int transition_out = R.anim.slide_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(transition_in, transition_out);
        actionBar = getSupportActionBar();
        mActivityHelper = new ActivityHelper(this);
        this.savedInstanceState = savedInstanceState;
    }


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        titleBar = (CommonTitleBar) findViewById(R.id.title_layout);
        if (titleBar != null)
            titleBar.setLeftBtnOnclickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
    }

    /**
     * @param title
     * @param msg
     * @param positive
     * @param positiveListener
     * @param negative
     * @param negativeListener
     */
    @Override
    public void alert(String title, String msg, String positive,
                      DialogInterface.OnClickListener positiveListener, String negative,
                      DialogInterface.OnClickListener negativeListener) {
        mActivityHelper.alert(title, msg, positive, positiveListener, negative, negativeListener);
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
    @Override
    public void alert(String title, String msg, String positive,
                      DialogInterface.OnClickListener positiveListener, String negative,
                      DialogInterface.OnClickListener negativeListener,
                      Boolean isCanceledOnTouchOutside) {
        mActivityHelper.alert(title, msg, positive, positiveListener, negative, negativeListener,
                isCanceledOnTouchOutside);
    }

    /**
     * TOAST
     *
     * @param msg
     * @param period
     */
    @Override
    public void toast(String msg, int period) {
        mActivityHelper.toast(msg, period);
    }

    /**
     * @param msg
     */
    @Override
    public void showProgressDialog(String msg) {
        mActivityHelper.showProgressDialog(msg);
    }

    /**
     * @param msg
     */
    public void showProgressDialog(final String msg, final boolean cancelable,
                                   final DialogInterface.OnCancelListener cancelListener) {
        mActivityHelper.showProgressDialog(msg, cancelable, cancelListener);
    }

    @Override
    public void dismissProgressDialog() {
        mActivityHelper.dismissProgressDialog();
    }


    protected android.support.v7.app.ActionBar actionBar=null;
    protected void setUpActionBar(boolean back,boolean forward, String label) {
        // Inflate your custom layout
        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.action_bar,
                null);

        // Set up your ActionBar

        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);
        Toolbar parent = (Toolbar) actionBarLayout.getParent();
        parent.setContentInsetsAbsolute(0,0);
        back_btn = (ImageButton)actionBarLayout.findViewById(R.id.back);
        forward_btn = (ImageButton)actionBarLayout.findViewById(R.id.forward);
        title = (TextView) actionBarLayout.findViewById(R.id.title);
        if(!back){
            hideBackButton();
        }
        if(!forward) {
            hideForwardButton();
        }
        setActionBarTitle(label);
    }

    public void showForwardButton() {
        if(forward_btn!=null)
            forward_btn.setVisibility(View.VISIBLE);
    }

    public void hideForwardButton() {
        if(forward_btn!=null) {
            forward_btn.setVisibility(View.GONE);
        }
    }

    public void showBackButton() {
        if(back_btn!=null)
            back_btn.setVisibility(View.VISIBLE);
    }

    public void hideBackButton() {
        if(back_btn!=null) {
            back_btn.setVisibility(View.GONE);
        }
    }

    public void setActionBarTitle(String label) {
        if(title!=null) {
            title.setText(label);
        }
    }

    public String getActionBarTitle() {
        if(title!=null) {
            title.getText().toString();
        }
        return null;
    }

    public void onBack(View v) {
        onBackPressed();
    }

    public void onForward(View v) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_back, R.anim.slide_in_back);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    protected Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);

        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);

        // Reset the drawing cache background color to fully transparent
        // for the duration of this operation
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);

        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            //Log.e(TAG, "failed getViewBitmap(" + v + ")", new RuntimeException());
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        // Restore the view
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);

        return bitmap;
    }
}
