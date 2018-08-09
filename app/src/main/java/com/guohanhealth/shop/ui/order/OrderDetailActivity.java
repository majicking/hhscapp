package com.guohanhealth.shop.ui.order;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.bean.LogisticsInfo;
import com.guohanhealth.shop.bean.OrderDetailInfo;
import com.guohanhealth.shop.custom.CustomDialog;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.ApiService;
import com.guohanhealth.shop.http.HttpErrorCode;
import com.guohanhealth.shop.utils.Utils;
import com.guohanhealth.shop.utils.engine.GlideEngine;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

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
    @BindView(R.id.order_detail_view_overtime)
    LinearLayout orderDetailViewOvertime;
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
    @BindView(R.id.order_detail_view_operate)
    LinearLayout orderDetailViewOperate;
    @BindView(R.id.order_detail_order_tips)
    TextView orderDetailOrderTips;
    @BindView(R.id.order_return_btn1)
    TextView orderReturnBtn1;
    @BindView(R.id.order_return_btn2)
    TextView orderReturnBtn2;
    @BindView(R.id.order_view_return)
    LinearLayout orderViewReturn;
    @BindView(R.id.order_detail_btn3)
    TextView orderDetailBtn3;
    @BindView(R.id.returnOrder)
    TextView returnOrder;
    private String mData;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_oder_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initToolBarNav(commonToolbar, commonToolbarTitleTv, "订单详情");
        mData = getIntent().getStringExtra("data");
        getData(Utils.getObject(mData, OrderDetailInfo.class));
    }

    public void getData(OrderDetailInfo info) {

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


            if (Utils.isEmpty(info.order_info.order_tips)) { //状态说明
                orderDetailOrderTips.setVisibility(View.VISIBLE);
                orderDetailOrderTips.setText(Utils.getString(info.order_info.order_tips));
            }

            if (Utils.isEmpty(info.order_info.payment_time)) {//支付时间
                orderDetailViewPaytime.setVisibility(View.VISIBLE);
                orderDetailPaytime.setText(Utils.getString(info.order_info.payment_time));
            }
            if (Utils.isEmpty(info.order_info.shipping_time)) {//发货时间
                orderDetailViewSendtime.setVisibility(View.VISIBLE);
                orderDetailSendtime.setText(Utils.getString(info.order_info.shipping_time));
            }
            if (Utils.isEmpty(info.order_info.finnshed_time)) {//完成时间
                orderDetailViewOvertime.setVisibility(View.VISIBLE);
                orderDetailOvertime.setText(Utils.getString(info.order_info.finnshed_time));
            }
            if (Utils.isEmpty(info.order_info.again_time)) {//再次确认时间
                orderDetailViewOktime.setVisibility(View.VISIBLE);
                orderDetailOktime.setText(Utils.getString(info.order_info.again_time));
            }
            orderDetailStorename.setText(Utils.getString(info.order_info.store_name));//店铺名称
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
            if (info.order_info.if_again ||
                    info.order_info.if_buyer_cancel ||
                    info.order_info.if_cancel ||
                    info.order_info.if_delete ||
                    info.order_info.if_deliver ||
                    info.order_info.if_evaluation ||
                    info.order_info.if_refund_cancel) {
                orderDetailViewOperate.setVisibility(View.VISIBLE);
            } else {
                orderDetailViewOperate.setVisibility(View.GONE);
            }

            if (info.order_info.if_again) {
                orderDetailBtn3.setVisibility(View.VISIBLE);
                orderDetailBtn3.setText("再次确认");

                orderDetailBtn3.setOnClickListener(v -> {
                    showWrinDialog("再次确认订单？", "sureOrderAgain", info.order_info.order_id);
                });
            }

            if (info.order_info.if_buyer_cancel) {
                orderDetailBtn1.setVisibility(View.VISIBLE);
                orderDetailBtn1.setText("取消订单");
                orderDetailBtn1.setOnClickListener(v -> {
                    showWrinDialog("确认取消订单？", "order_cancel", info.order_info.order_id);
                });
            }

            if (info.order_info.if_receive) {
                orderDetailBtn3.setVisibility(View.VISIBLE);
                orderDetailBtn3.setText("确认收货");
                orderDetailBtn3.setOnClickListener(v -> {
                    showWrinDialog("确认收到货物", "order_receive", info.order_info.order_id);
                });
            }

            if (info.order_info.if_lock) {
                returnOrder.setVisibility(View.VISIBLE);
                returnOrder.setText("退货/退款中");
            }

            if (info.order_info.if_evaluation) {
                orderDetailBtn2.setVisibility(View.VISIBLE);
                orderDetailBtn2.setText("订单评价");
                orderDetailBtn2.setOnClickListener(v -> {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.ORDER_ID, info.order_info.order_id);
                    readyGo(EvaluationActivity.class, bundle);
                });
            }
            if (info.order_info.if_evaluation_again) {
                orderDetailBtn2.setVisibility(View.VISIBLE);
                orderDetailBtn2.setText("追加评价");
                Bundle bundle = new Bundle();
                bundle.putString(Constants.ORDER_ID, info.order_info.order_id);
                bundle.putInt(Constants.TYPE, 1);
                readyGo(EvaluationActivity.class, bundle);
            }
            if (info.order_info.if_refund_cancel) {
                orderDetailBtn1.setVisibility(View.VISIBLE);
                orderDetailBtn1.setText("退款");
                Bundle bundle = new Bundle();
                bundle.putString(Constants.ORDER_ID, info.order_info.order_id);
                bundle.putInt(Constants.TYPE, 1);
                readyGo(ReturnOrderActivity.class, bundle);
            }
            if (info.order_info.if_deliver) {
                orderDetailBtn1.setVisibility(View.VISIBLE);
                orderDetailBtn1.setText("查看物流");
                orderDetailBtn1.setOnClickListener(v -> {
                    Api.post(ApiService.SEARCH_DELIVER, new FormBody.Builder()
                                    .add("key", App.getApp().getKey())
                                    .add("order_id", info.order_info.order_id)
                                    .build(), new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    runOnUiThread(() -> {
                                        showToast(Utils.getErrorString(e));
                                    });
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String json = response.body().string();
                                    if (Utils.getCode(json) == HttpErrorCode.HTTP_NO_ERROR) {
                                        runOnUiThread(() -> {
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable(Constants.DATA, Utils.getObject(Utils.getDatasString(json), LogisticsInfo.class));
                                            readyGo(LogisticsActivity.class, bundle);
                                        });
                                    } else if (Utils.getCode(json) == HttpErrorCode.ERROR_400) {
                                        runOnUiThread(() -> {
                                            showToast(Utils.getErrorString(json));

                                        });
                                    }
                                }
                            }
                    );
                });
            }


        }
    }

    public void showWrinDialog(String message, String url, String order_id) {
        Dialog dialog = new CustomDialog.Builder(mContext)
                .setTitle("操作警告")
                .setMessage(message)
                .setPositiveButton("取消", (d, v) -> {
                    d.dismiss();
                })
                .setNegativeButton("确定", (d, v) -> {
                    Api.post(ApiService.ORDER_OPERATION + "&op=" + url, new FormBody.Builder()
                            .add("key", App.getApp().getKey())
                            .add("order_id", order_id)
                            .build(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(() -> {
                                d.dismiss();
                                showToast(Utils.getErrorString(e));
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try {
                                String json = response.body().string();
                                runOnUiThread(() -> {
                                    d.dismiss();
                                    if (Utils.getCode(json) == HttpErrorCode.HTTP_NO_ERROR) {
                                        if (Utils.getDatasString(json).equals("1")) {
                                            Api.get(ApiService.ORDER_INFO + "&key=" + App.getApp().getKey() + "&order_id=" + order_id, new Callback() {
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

                                                                OrderDetailInfo info = Utils.getObject(Utils.getDatasString(json), OrderDetailInfo.class);
                                                                if (info != null) {
                                                                    getData(info);
                                                                } else {
                                                                    finish();
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
                                        } else {
                                            showToast(Utils.getDatasString(json));
                                        }
                                    } else if (Utils.getCode(json) == HttpErrorCode.ERROR_400) {
                                        showToast(Utils.getErrorString(json));
                                    }
                                });
                            } catch (Exception e) {
                                showToast(Utils.getErrorString(e));
                            }
                        }
                    });
                }).create();
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
