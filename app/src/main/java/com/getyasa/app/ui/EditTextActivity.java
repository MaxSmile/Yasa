package com.getyasa.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.common.util.StringUtils;
import com.getyasa.YasaConstants;
import com.getyasa.R;
import com.getyasa.base.YasaBaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 编辑文字
 * Created by sky on 2015/7/20.
 * Weibo: http://weibo.com/2030683111
 * Email: 1132234509@qq.com
 */
public class EditTextActivity extends YasaBaseActivity {

    private final static int MAX        = 10;
    private int maxlength               = MAX;

    @InjectView(R.id.text_input)
    EditText contentView;

    @InjectView(R.id.tag_input_tips)
    TextView numberTips;

    public static void openTextEdit(Activity mContext, String defaultStr,int maxLength, int reqCode) {
        Intent i = new Intent(mContext, EditTextActivity.class);
        i.putExtra(YasaConstants.PARAM_EDIT_TEXT, defaultStr);
        if (maxLength != 0) {
            i.putExtra(YasaConstants.PARAM_MAX_SIZE, maxLength);
        }
        mContext.startActivityForResult(i, reqCode);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);
        ButterKnife.inject(this);
        maxlength = getIntent().getIntExtra(YasaConstants.PARAM_MAX_SIZE, MAX);

        String defaultStr = getIntent().getStringExtra(YasaConstants.PARAM_EDIT_TEXT);
        if (StringUtils.isNotEmpty(defaultStr)) {
            contentView.setText(defaultStr);
            if (defaultStr.length() <= maxlength) {
                numberTips.setText("你还可以输入" + (maxlength - defaultStr.length()) + "个字  ("
                                   + defaultStr.length() + "/" + maxlength + ")");
            }
        }
        titleBar.setRightBtnOnclickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String inputTxt = contentView.getText().toString();
                intent.putExtra(YasaConstants.PARAM_EDIT_TEXT, inputTxt);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        contentView.addTextChangedListener(mTextWatcher);
    }

    TextWatcher mTextWatcher = new TextWatcher() {
                                 private int editStart;
                                 private int editEnd;

                                 @Override
                                 public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                                               int arg3) {
                                 }

                                 @Override
                                 public void onTextChanged(CharSequence s, int arg1, int arg2,
                                                           int arg3) {
                                 }

                                 @Override
                                 public void afterTextChanged(Editable s) {
                                     editStart = contentView.getSelectionStart();
                                     editEnd = contentView.getSelectionEnd();
                                     if (s.toString().length() > maxlength) {
                                         toast("你输入的字数已经超过了限制！", Toast.LENGTH_LONG);
                                         s.delete(editStart - 1, editEnd);
                                         int tempSelection = editStart;
                                         contentView.setText(s);
                                         contentView.setSelection(tempSelection);
                                     }
                                     numberTips.setText("你还可以输入"
                                                        + (maxlength - s.toString().length())
                                                        + "个字  (" + s.toString().length() + "/"
                                                        + maxlength + ")");
                                 }
                             };
}
