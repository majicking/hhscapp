package com.guohanhealth.shop.ui.main.fragment.mine.returngoods;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.bean.ReturnInfo;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.ApiService;
import com.guohanhealth.shop.http.HttpErrorCode;
import com.guohanhealth.shop.utils.Utils;
import com.guohanhealth.shop.utils.engine.GlideEngine;

import java.io.IOException;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ReturnDetailsActivity extends BaseActivity {


    @BindView(R.id.common_toolbar_title_tv)
    TextView commonToolbarTitleTv;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.text2)
    TextView text2;
    @BindView(R.id.text3)
    TextView text3;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.text4)
    TextView text4;
    @BindView(R.id.text5)
    TextView text5;
    @BindView(R.id.text6)
    TextView text6;
    @BindView(R.id.text7)
    TextView text7;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_return_details;
    }

    String refund_id;

    @Override
    protected void initView(Bundle savedInstanceState) {
        initToolBarNav(commonToolbar, commonToolbarTitleTv, "退款详情");
        refund_id = getIntent().getStringExtra(Constants.REFUND_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    public void getData() {
        Api.get(ApiService.GET_REFUND_INFO + "&key=" + App.getApp().getKey() + "&refund_id=" + refund_id+ "&random=" + new Random().nextInt(10), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    showToast(Utils.getErrorString(e));
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String json = response.body().string();
                    if (Utils.getCode(json) == HttpErrorCode.HTTP_NO_ERROR) {
                        runOnUiThread(() -> {
                            try {
                                ReturnInfo info = Utils.getObject(Utils.getDatasString(json), ReturnInfo.class);
                                /**图片*/
                                if (Utils.isEmpty(info.pic_list)) {
                                    linearLayout.removeAllViews();
                                    for (int i = 0; i < info.pic_list.size(); i++) {
                                        View view = getView(R.layout.image_35);
                                        ImageView imageView = (ImageView) getView(view, R.id.img);
                                        GlideEngine.getInstance().loadImage(mContext, imageView, info.pic_list.get(i));
                                        linearLayout.addView(view);
                                    }
                                }
                                /**我的退款申请*/
                                text.setText(info.refund.refund_sn);
                                text1.setText(info.refund.reason_info);
                                text2.setText(info.refund.refund_amount);
                                text3.setText(info.refund.buyer_message);
                                /**商家退款处理*/
                                text4.setText(info.refund.seller_state);
                                text5.setText(info.refund.seller_message);
                                /**商城退款审核*/
                                text6.setText(info.refund.admin_state);
                                text7.setText(info.refund.admin_state);
                            } catch (Exception e) {
                                runOnUiThread(() -> {
                                    showToast(Utils.getErrorString(e));
                                });
                            }
                        });
                    } else {
                        runOnUiThread(() -> {
                            showToast(Utils.getErrorString(json));
                        });
                    }
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        showToast(Utils.getErrorString(e));
                    });
                }
            }
        });
    }
}
