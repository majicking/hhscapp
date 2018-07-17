package com.guohanhealth.shop.ui.order;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.bean.OrderDetailInfo;
import com.guohanhealth.shop.utils.Utils;
import com.guohanhealth.shop.utils.engine.GlideEngine;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetailActivity extends BaseActivity {

    @BindView(R.id.common_toolbar_title_tv)
    TextView commonToolbarTitleTv;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.order_detail_stutas)
    TextView orderDetailStutas;
    @BindView(R.id.order_detail_name)
    TextView orderDetailName;
    @BindView(R.id.order_detail_phonenumber)
    TextView orderDetailPhonenumber;
    @BindView(R.id.order_detail_address)
    TextView orderDetailAddress;
    @BindView(R.id.order_detail_payway)
    TextView orderDetailPayway;
    @BindView(R.id.order_detail_freight)
    TextView orderDetailFreight;
    @BindView(R.id.order_detail_money)
    TextView orderDetailMoney;
    @BindView(R.id.order_detail_contact_customer)
    LinearLayout orderDetailContactCustomer;
    @BindView(R.id.order_detail_call_custonmer)
    LinearLayout orderDetailCallCustonmer;
    @BindView(R.id.order_detail_orderid)
    TextView orderDetailOrderid;
    @BindView(R.id.order_detail_creattime)
    TextView orderDetailCreattime;
    @BindView(R.id.order_detail_paytime)
    TextView orderDetailPaytime;
    @BindView(R.id.order_detail_view_paytime)
    LinearLayout orderDetailViewPaytime;
    @BindView(R.id.order_detail_sendtime)
    TextView orderDetailSendtime;
    @BindView(R.id.order_detail_view_sendtime)
    LinearLayout orderDetailViewSendtime;
    @BindView(R.id.order_detail_overtime)
    TextView orderDetailOvertime;
    @BindView(R.id.order_detail_view_obertime)
    LinearLayout orderDetailViewObertime;
    @BindView(R.id.order_detail_oktime)
    TextView orderDetailOktime;
    @BindView(R.id.order_detail_view_oktime)
    LinearLayout orderDetailViewOktime;
    @BindView(R.id.order_detail_btn1)
    TextView orderDetailBtn1;
    @BindView(R.id.order_detail_btn2)
    TextView orderDetailBtn2;
    @BindView(R.id.order_detail_storename)
    TextView orderDetailStorename;
    @BindView(R.id.order_detail_view_goodslist)
    LinearLayout orderDetailViewGoodslist;
    private String mData;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_oder_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initToolBarNav(commonToolbar, commonToolbarTitleTv, "订单详情");
        mData = getIntent().getStringExtra("data");
        OrderDetailInfo info = Utils.getObject(mData, OrderDetailInfo.class);
        if (info.order_info != null) {
            orderDetailStutas.setText(Utils.getString(info.order_info.state_desc));
            orderDetailName.setText(Utils.getString(info.order_info.reciver_name));
            orderDetailPhonenumber.setText(Utils.getString(info.order_info.reciver_phone));
            orderDetailAddress.setText(Utils.getString(info.order_info.reciver_addr));
            orderDetailPayway.setText(Utils.getString(info.order_info.payment_name));
            orderDetailMoney.setText(Utils.getString(info.order_info.real_pay_amount));
            orderDetailFreight.setText(Utils.getString(info.order_info.shipping_fee));
            orderDetailOrderid.setText(Utils.getString(info.order_info.order_sn));
            orderDetailCreattime.setText(Utils.getString(info.order_info.add_time));
            if (Utils.isEmpty(info.order_info.payment_time)) {
                orderDetailPaytime.setVisibility(View.VISIBLE);
                orderDetailPaytime.setText(Utils.getString(info.order_info.payment_time));
            }
            if (Utils.isEmpty(info.order_info.shipping_time)) {
                orderDetailSendtime.setVisibility(View.VISIBLE);
                orderDetailSendtime.setText(Utils.getString(info.order_info.shipping_time));
            }
            if (Utils.isEmpty(info.order_info.finnshed_time)) {
                orderDetailOvertime.setVisibility(View.VISIBLE);
                orderDetailOvertime.setText(Utils.getString(info.order_info.finnshed_time));
            }
            if (Utils.isEmpty(info.order_info.again_time)) {
                orderDetailOktime.setVisibility(View.VISIBLE);
                orderDetailOktime.setText(Utils.getString(info.order_info.again_time));
            }
            orderDetailStorename.setText(Utils.getString(info.order_info.store_name));
            orderDetailViewGoodslist.removeAllViews();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            orderDetailViewGoodslist.setLayoutParams(layoutParams);
            if (Utils.isEmpty(info.order_info.goods_list)) {
                for (int i = 0; i < info.order_info.goods_list.size(); i++) {
                    View view = getView(R.layout.order_list_goods_item);
                    TextView name = (TextView) getView(view, R.id.goods_name);
                    TextView price = (TextView) getView(view, R.id.goods_price);
                    TextView number = (TextView) getView(view, R.id.goods_number);
                    ImageView img = (ImageView) getView(view, R.id.goods_img);
                    name.setText(info.order_info.goods_list.get(i).goods_name);
                    price.setText(info.order_info.goods_list.get(i).goods_price);
                    number.setText(info.order_info.goods_list.get(i).goods_num);
                    GlideEngine.getInstance().loadImage(mContext, img, info.order_info.goods_list.get(i).image_url);
                    orderDetailViewGoodslist.addView(view);
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
