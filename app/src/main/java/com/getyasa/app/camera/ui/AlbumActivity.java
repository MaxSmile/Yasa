package com.getyasa.app.camera.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.common.util.FileUtils;
import com.common.util.ImageUtils;
import com.common.util.StringUtils;
import com.customview.PagerSlidingTabStrip;
import com.getyasa.YasaConstants;
import com.getyasa.R;
import com.getyasa.app.camera.CameraBaseActivity;
import com.getyasa.app.camera.fragment.AlbumFragment;
import com.getyasa.app.model.Album;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class AlbumActivity extends CameraBaseActivity {

    private Map<String, Album> albums;
    private List<String> paths = new ArrayList<String>();

    @InjectView(R.id.indicator)
    PagerSlidingTabStrip tab;
    @InjectView(R.id.pager)
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        ButterKnife.inject(this);
        albums = ImageUtils.findGalleries(this, paths, 0);
        //ViewPager的adapter
        FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tab.setViewPager(pager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent result) {
        if (requestCode == YasaConstants.REQUEST_CROP && resultCode == RESULT_OK) {

        }
    }


    class TabPageIndicatorAdapter extends FragmentPagerAdapter {
        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //New to display the contents of a Fragment ViewPager item and pass parameters
            return AlbumFragment.newInstance(albums.get(paths.get(position)).getPhotos());
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Album album = albums.get(paths.get(position % paths.size()));
            if (StringUtils.equalsIgnoreCase(FileUtils.getInst().getSystemPhotoPath(),
                    album.getAlbumUri())) {
                return getString(R.string.tab_gallery);
            } else if (album.getTitle().length() > 13) {
                return album.getTitle().substring(0, 11) + "...";
            }
            return album.getTitle();
        }

        @Override
        public int getCount() {
            return paths.size();
        }
    }


}
