package com.getyasa.app.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.getyasa.R;


/**
 * Created by Maxim Vasilkov maxim.vasilkov@gmail.com on 08/12/15.
 */
public class YasaBaseActivity extends AppCompatActivity {
    protected Bundle savedInstanceState;
    private ImageButton back_btn,forward_btn;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        this.savedInstanceState = savedInstanceState;
    }


    protected void setUpActionBar(boolean back,boolean forward, String label) {
        // Inflate your custom layout
        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.action_bar,
                null);

        // Set up your ActionBar
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
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
}
