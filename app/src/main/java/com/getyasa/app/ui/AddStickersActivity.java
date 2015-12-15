package com.getyasa.app.ui;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.common.util.FileUtils;
import com.common.util.ImageUtils;
import com.common.util.TimeUtils;
import com.customview.LabelView;
import com.customview.MyImageViewDrawableOverlay;
import com.getyasa.App;
import com.getyasa.R;
import com.getyasa.app.camera.CameraBaseActivity;
import com.getyasa.app.camera.adapter.StickerToolAdapter;
import com.getyasa.app.camera.util.EffectUtil;
import com.getyasa.app.model.Addon;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import it.sephiroth.android.library.widget.HListView;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * Created by Maxim Vasilkov maxim.vasilkov@gmail.com on 25/11/15.
 * Over the collage on this screen you are able to place stickers.
 * Stickers are of two type: text and pics.
 * The list of the pics stickers going to be similar to those which are free in Moldiv application.
 * To remove a sticker you have to move it out of the collage edit area.
 * And finally the last and most important is Share function.
 * This going to work as a standard Android Share action which will automatically propagate all
 * available options of pics sharing on the device with that difference that saving to the device
 * storage option will be added.
 */
public class AddStickersActivity extends CameraBaseActivity {

    @InjectView(R.id.gpuimage)
    GPUImageView mGPUImageView;

    @InjectView(R.id.drawing_view_container)
    ViewGroup drawArea;


    @InjectView(R.id.list_tools)
    HListView bottomToolBar;

    @InjectView(R.id.toolbar_area)
    ViewGroup toolArea;

    private MyImageViewDrawableOverlay mImageView;


    //当前图片
    private Bitmap currentBitmap;

    //小白点标签
    private LabelView emptyLabelView;



    //标签区域
    private View commonLabelArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stickers);
        ButterKnife.inject(this);
        EffectUtil.clear();
        initView();
        initStickerToolBar();

        ImageUtils.asyncLoadImage(this, getIntent().getData(), new ImageUtils.LoadImageCallback() {
            @Override
            public void callback(Bitmap result) {
                currentBitmap = result;
                mGPUImageView.setImage(currentBitmap);
            }
        });


    }

    private ShareActionProvider mShareActionProvider;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.menu_share, menu);

        MenuItem shareItem = menu.findItem(R.id.action_share);
        mShareActionProvider =  (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        mShareActionProvider.setShareIntent(createShareIntent());
        mShareActionProvider.setOnShareTargetSelectedListener(new ShareActionProvider.OnShareTargetSelectedListener() {
            @Override
            public boolean onShareTargetSelected(ShareActionProvider source, Intent intent) {
                try {
                    Uri uri = getIntent().getData();
                    Bitmap bitmap = applyChangesBitmap();
                    ImageUtils.saveToFile(uri.getPath(), false, bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mShareActionProvider.setShareIntent(createShareIntent());
                return false;
            }
        });
        // Return true to display menu
        return true;
    }





    // Create and return the Share Intent
    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, getIntent().getData());
        return shareIntent;
    }

    private void initView() {

        setUpActionBar(true,false,"Add Stickers & Share");

        View overlay = LayoutInflater.from(AddStickersActivity.this).inflate(
                R.layout.view_drawable_overlay, null);
        mImageView = (MyImageViewDrawableOverlay) overlay.findViewById(R.id.drawable_overlay);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(App.getApp().getScreenWidth(),
                App.getApp().getScreenWidth());
        mImageView.setLayoutParams(params);
        overlay.setLayoutParams(params);
        drawArea.addView(overlay);

        RelativeLayout.LayoutParams rparams = new RelativeLayout.LayoutParams(App.getApp().getScreenWidth(), App.getApp().getScreenWidth());


        mGPUImageView.setLayoutParams(rparams);


        //Initialization blank label
        emptyLabelView = new LabelView(this);
        emptyLabelView.setEmpty();
        EffectUtil.addLabelEditable(mImageView, drawArea, emptyLabelView,
                mImageView.getWidth() / 2, mImageView.getWidth() / 2);
        emptyLabelView.setVisibility(View.INVISIBLE);

        //初始化推荐标签栏
        commonLabelArea = LayoutInflater.from(AddStickersActivity.this).inflate(
                R.layout.view_label_bottom,null);
        commonLabelArea.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        toolArea.addView(commonLabelArea);
        commonLabelArea.setVisibility(View.GONE);
    }

    @Override
    public void onForward(View v) {
        super.onForward(v);
        savePicture();
    }


    private Bitmap applyChangesBitmap() {
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

        EffectUtil.applyOnSave(cv, mImageView);
        return newBitmap;
    }

    private void savePicture(){
        new SavePicToFileTask().execute(applyChangesBitmap());
    }

    private class SavePicToFileTask extends AsyncTask<Bitmap,Void,String>{
        Bitmap bitmap;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Bitmap... params) {
            String fileName = null;
            try {
                bitmap = params[0];

                 String picName = TimeUtils.dtFormat(new Date(), "yyyyMMddHHmmss");
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
            // TODO: on file saved!
        }
    }




    private void initStickerToolBar(){

        bottomToolBar.setAdapter(new StickerToolAdapter(AddStickersActivity.this, EffectUtil.addonList));
        bottomToolBar.setOnItemClickListener(new it.sephiroth.android.library.widget.AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(it.sephiroth.android.library.widget.AdapterView<?> arg0,
                                    View arg1, int arg2, long arg3) {

                Addon sticker = EffectUtil.addonList.get(arg2);
                EffectUtil.addStickerImage(mImageView, AddStickersActivity.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {

                            }
                        });
            }
        });
    }



}
