package com.getyasa.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.common.util.FileUtils;
import com.common.util.ImageUtils;
import com.common.util.TimeUtils;
import com.customview.MyImageViewDrawableOverlay;
import com.getyasa.App;
import com.getyasa.R;
import com.getyasa.app.camera.CameraBaseActivity;
import com.getyasa.app.camera.EffectService;
import com.getyasa.app.camera.adapter.FiltersAdapter;
import com.getyasa.app.camera.effect.FilterEffect;
import com.getyasa.app.camera.util.EffectUtil;
import com.getyasa.app.camera.util.GPUImageFilterTools;

import java.io.File;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import it.sephiroth.android.library.widget.HListView;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * Created by Maxim Vasilkov maxim.vasilkov@gmail.com on 25/11/15.
 * To the collage you got you can apply a filter/effect.
 * This functionality is very simple to the one in Instagram.
 * Almost identical. With the difference of may be just having different names for the effects.
 * The number of effects: 5-10.
 */
public class ApplyEffectsActivity extends CameraBaseActivity {

    @InjectView(R.id.gpuimage)
    GPUImageView mGPUImageView;

    @InjectView(R.id.drawing_view_container)
    ViewGroup drawArea;

    @InjectView(R.id.list_tools)
    HListView bottomToolBar;


    // TODO: optimize overlay?
    private MyImageViewDrawableOverlay mImageView;


    //Current picture
    private Bitmap currentBitmap;

    //Small pictures for Filters preview
    private Bitmap smallImageBackgroud;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_effects);
        ButterKnife.inject(this);
        EffectUtil.clear();
        initView();


        ImageUtils.asyncLoadImage(this, getIntent().getData(), new ImageUtils.LoadImageCallback() {
            @Override
            public void callback(Bitmap result) {
                currentBitmap = result;
                mGPUImageView.setImage(currentBitmap);
            }
        });

        ImageUtils.asyncLoadSmallImage(this, getIntent().getData(), new ImageUtils.LoadImageCallback() {
            @Override
            public void callback(Bitmap result) {
                smallImageBackgroud = result;//getResizedBitmap(result,80,80);
                initFilterToolBar();
            }
        });

    }


    private void initView() {
        setUpActionBar(true,true,"");
        View overlay = LayoutInflater.from(ApplyEffectsActivity.this).inflate(
                R.layout.view_drawable_overlay, null);

        mImageView = (MyImageViewDrawableOverlay) overlay.findViewById(R.id.drawable_overlay);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(App.getApp().getScreenWidth(),
                App.getApp().getScreenWidth());
        mImageView.setLayoutParams(params);
        overlay.setLayoutParams(params);
        drawArea.addView(overlay);


        RelativeLayout.LayoutParams rparams = new RelativeLayout.LayoutParams(App.getApp().getScreenWidth(), App.getApp().getScreenWidth());


        //Initializes the filter image
        mGPUImageView.setLayoutParams(rparams);


    }

    @Override
    public void onForward(View v) {
        super.onForward(v);
        savePicture();
    }


    //Save Image
    private void savePicture(){
        //Add filters
        final Bitmap newBitmap = Bitmap.createBitmap(mImageView.getWidth(), mImageView.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newBitmap);
        RectF dst = new RectF(0, 0, mImageView.getWidth(), mImageView.getHeight());
        try {
            cv.drawBitmap(mGPUImageView.capture(), null, dst, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
            cv.drawBitmap(currentBitmap, null, dst, null);
        }

        //Add watermark Stickers
        EffectUtil.applyOnSave(cv, mImageView);

        new SavePicToFileTask().execute(newBitmap);
    }

    private class SavePicToFileTask extends AsyncTask<Bitmap,Void,String>{
        Bitmap bitmap;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(getString(R.string.save_picture));
        }

        @Override
        protected String doInBackground(Bitmap... params) {
            String fileName = null;
            try {
                bitmap = params[0];

                String picName = TimeUtils.dtFormat(new Date(), "yyyyMMddHHmmss") + ".jpg";
                fileName = ImageUtils.saveToFile(FileUtils.getInst().getPhotoSavedPath() + "/"+ picName, false, bitmap);

            } catch (Exception e) {
                e.printStackTrace();
                toast(getString(R.string.image_processing_error), Toast.LENGTH_LONG);
            }
            return fileName;
        }

        @Override
        protected void onPostExecute(String fileName) {
            super.onPostExecute(fileName);
            dismissProgressDialog();
            navigateToNextActivity(Uri.fromFile(new File(fileName)));
        }
    }

    private void navigateToNextActivity(Uri uri) {
        Intent newIntent = new Intent(this, AddStickersActivity.class);
        newIntent.setData(uri);
        this.startActivity(newIntent);
    }



    private void initFilterToolBar(){
        final List<FilterEffect> filters = EffectService.getInst().getLocalFilters(this);
        final FiltersAdapter adapter = new FiltersAdapter(ApplyEffectsActivity.this, filters,smallImageBackgroud);
        bottomToolBar.setAdapter(adapter);
        bottomToolBar.setOnItemClickListener(new it.sephiroth.android.library.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(it.sephiroth.android.library.widget.AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                if (adapter.getSelectFilter() != arg2) {
                    adapter.setSelectFilter(arg2);
                    GPUImageFilter filter = GPUImageFilterTools.createFilterForType(
                            ApplyEffectsActivity.this, filters.get(arg2).getType());
                    mGPUImageView.setFilter(filter);
                    GPUImageFilterTools.FilterAdjuster mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(filter);
                    //Adjustable color filters
                    if (mFilterAdjuster.canAdjust()) {
                        //mFilterAdjuster.adjust(100);//Adjustable filters to choose an appropriate value
                    }
                }
            }
        });
    }



}
