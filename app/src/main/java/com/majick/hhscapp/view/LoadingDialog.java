package com.majick.hhscapp.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.TextView;


import com.majick.hhscapp.R;

import butterknife.BindView;

/**
 * Author:  andy.xwt
 * Date:    2018/1/23 13:44
 * Description:
 */


public class LoadingDialog extends BaseDialog {


    @BindView(R.id.tv_text)
    TextView mTvText;
    private String mText;

    public LoadingDialog(@NonNull Context context, String text) {
        super(context,R.style.myDialogTheme);
        this.mText = text;
    }

    @Override
    protected int initDialogLayoutId() {
        return R.layout.dialog_loading;
    }

    @Override
    protected void initView() {
        mTvText.setText(mText);
    }



    @Override
    protected DialogConfig initDialogDisplay() {
        return new DialogConfig(0f, 0, Gravity.CENTER);
    }
}
