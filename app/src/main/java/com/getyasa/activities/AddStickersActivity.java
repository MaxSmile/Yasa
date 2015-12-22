package com.getyasa.activities;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.customview.MyHighlightView;
import com.customview.MyImageViewDrawableOverlay;
import com.getyasa.App;
import com.getyasa.FiltersAdapter;
import com.getyasa.R;
import com.getyasa.app.camera.CameraBaseActivity;
import com.getyasa.app.camera.util.EffectUtil;
import com.getyasa.app.model.Addon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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


    @InjectView(R.id.toolbar_area)
    ViewGroup toolArea;

    @InjectView(R.id.my_recycler_view)
    RecyclerView mRecyclerView;

    private RecyclerView.LayoutManager mLayoutManager;

    private MyImageViewDrawableOverlay mImageView;


    //Current picture
    private Bitmap currentBitmap;

    //White Point Labels
    private LabelView emptyLabelView;



    //The label area
    private View commonLabelArea;


    int[] stickers = {
            R.drawable.sticker1,
            R.drawable.sticker2,
            R.drawable.sticker3,
            R.drawable.sticker4,
//            R.drawable.sticker5,
            R.drawable.sticker6,
            R.drawable.sticker7,
            R.drawable.sticker8,
            R.drawable.sticker9,
            R.drawable.sticker11,
            R.drawable.sticker12,
            R.drawable.sticker14,
            R.drawable.sticker15,

//            R.drawable.sticker20,
//            R.drawable.sticker21,
//            R.drawable.sticker22,
//            R.drawable.sticker23,
//            R.drawable.sticker24,
            R.drawable.sticker25,
            R.drawable.sticker26,
            R.drawable.sticker27,
           // R.drawable.sticker28,
            R.drawable.sticker29,


//            R.drawable.sticker30,
            R.drawable.sticker31,
            R.drawable.sticker32,
//            R.drawable.sticker33,
//            R.drawable.sticker34,
//            R.drawable.sticker35,
//            R.drawable.sticker36,
//            R.drawable.sticker37,
//            R.drawable.sticker38,
//            R.drawable.sticker39,
//            R.drawable.sticker40
                            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stickers);
        ButterKnife.inject(this);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(new FiltersAdapter(stickers,stickerAddHandler));
        initView();

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

        setUpActionBar(true,false,"");

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

        //Recommended Initialization tab bar
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

                 String picName = TimeUtils.dtFormat(new Date(), "yyyyMMddHHmmss")+".jpg";
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

    private void startDeselectTimer() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                for (MyHighlightView v:EffectUtil.hightlistViews) {
                    v.clearState();
                }
            }
        }, 3000);
    }


    private Handler stickerAddHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Addon sticker = new Addon(msg.arg1);
            EffectUtil.addStickerImage(mImageView, AddStickersActivity.this, sticker,
                    new EffectUtil.StickerCallback() {
                        @Override
                        public void onRemoveSticker(Addon sticker) {

                        }
                    });
                    startDeselectTimer();


            return false;

        }
    });


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_save:
                try {
                    String picName = TimeUtils.dtFormat(new Date(), "yyyyMMddHHmmss")+".jpg";
                    Bitmap bitmap = applyChangesBitmap();
                    saveToInternalSorage(bitmap,picName);
                    toast(getString(R.string.camera_saved),Toast.LENGTH_SHORT);
                } catch (Exception e) {
                    e.printStackTrace();
                    toast(getString(R.string.camera_failed),Toast.LENGTH_LONG);
                }
                return true;
            case R.id.action_logo:
                // TODO: ask if sure?
                Intent intent = new Intent(this, ShapesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }
        return false;
    }


    private String saveToInternalSorage(Bitmap bitmapImage, String fileName) throws IOException {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);




        // Create imageDir
        File mypath=new File(directory,fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 80, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        MediaStore.Images.Media.insertImage(this.getContentResolver(), mypath.getAbsolutePath(), mypath.getName(), fileName);
        return mypath.getAbsolutePath();
    }

}
