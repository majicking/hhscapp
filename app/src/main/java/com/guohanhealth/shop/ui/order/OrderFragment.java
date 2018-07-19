package com.guohanhealth.shop.ui.order;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseFragment;
import com.guohanhealth.shop.bean.LogisticsInfo;
import com.guohanhealth.shop.bean.OrderInfo;
import com.guohanhealth.shop.bean.PayWayInfo;
import com.guohanhealth.shop.custom.CustomDialog;
import com.guohanhealth.shop.custom.EmptyView;
import com.guohanhealth.shop.event.ObjectEvent;
import com.guohanhealth.shop.event.OnFragmentInteractionListener;
import com.guohanhealth.shop.event.RxBus;
import com.guohanhealth.shop.http.Result;
import com.guohanhealth.shop.ui.main.activity.MainActivity;
import com.guohanhealth.shop.utils.Logutils;
import com.guohanhealth.shop.utils.PayResult;
import com.guohanhealth.shop.utils.Utils;
import com.guohanhealth.shop.utils.engine.GlideEngine;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.guohanhealth.shop.app.Constants.MAINNUMBER;

public class OrderFragment extends BaseFragment<OrderPersenter, OrderModel> implements OrderView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    Unbinder unbinder;
    @BindView(R.id.emptyview)
    EmptyView emptyview;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    private int position;
    private boolean selecttype;
    private String searchtext;
    private OnFragmentInteractionListener mListener;
    @BindView(R.id.order_views_refresh)
    SmartRefreshLayout orderViewsRefresh;
    boolean isLoading;
    private PopupWindow popuWindow;

    public OrderFragment() {
        list = new ArrayList<>();
    }

    boolean hasmore;
    int page_total = 1;

    public static OrderFragment newInstance(int position, boolean selecttype, String searchtext) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, position);
        args.putBoolean(ARG_PARAM2, selecttype);
        args.putString(ARG_PARAM3, searchtext);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_PARAM1, 0);
            selecttype = getArguments().getBoolean(ARG_PARAM2);
            searchtext = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_order;
    }

    public String[] realtype = {"", "state_new", "state_send", "state_notakes", "state_noeval"};
    public String[] viltype = {"", "state_new", "state_pay"};
    List<OrderInfo.OrderGroupListBean> list;
    int page = 1;


    @Override
    protected void initView(Bundle savedInstanceState) {
        orderViewsRefresh.setOnRefreshListener(refreshLayout -> {
            page = 1;
            isLoading = false;
            getData();
        });
        orderViewsRefresh.setOnLoadMoreListener(refreshLayout -> {
            if (hasmore && page < page_total) {
                page++;
                getData();
                isLoading = true;
            } else {
                orderViewsRefresh.finishLoadMore();
            }
        });
        emptyview.setEmpty("没有找到订单", "去挑选商品").setClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(MAINNUMBER, 0);
            readyGoThenKill(MainActivity.class, bundle);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    public void getData(String searchtext) {
        mPresenter.getOrderData(selecttype, App.getApp().getKey(), page, selecttype ? viltype[position] : realtype[position], searchtext);
    }

    public void getData() {
        mPresenter.getOrderData(selecttype, App.getApp().getKey(), page, selecttype ? viltype[position] : realtype[position], searchtext);
    }

    @Override
    public void getData(Result result) {
        if (orderViewsRefresh != null) {
            if (orderViewsRefresh.isRefreshing())
                orderViewsRefresh.finishRefresh();
            if (orderViewsRefresh.isLoading())
                orderViewsRefresh.finishLoadMore();
        }
        if (!isLoading) {
            list.clear();
        }

        OrderInfo info = (OrderInfo) result.datas;
        hasmore = result.hasmore;
        page_total = result.page_total;
        if (Utils.isEmpty(info.order_group_list)) {
            emptyview.setVisibility(View.GONE);
            list.addAll(info.order_group_list);
            if (Utils.isEmpty(list)) {
                setData();
            }

        } else {
            emptyview.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void orderOperation(String msg) {
        showToast(msg);
    }


    String pay_sn;

    @Override
    public void getPayWayList(PayWayInfo info) {
//        Logutils.i(info.payment_list.size());
        popuWindow = Utils.shopPayWindown(mContext, info.payment_list, pay_sn, selecttype ? "1" : "0", v -> {

            popuWindow.dismiss();
        });
        popuWindow.setFocusable(true);
        popuWindow.setOutsideTouchable(false);
        popuWindow.setOutsideTouchable(false);
        RxBus.getDefault().register(this, PayResult.class, payResult -> {
            Logutils.i(payResult.getResult());
            if (payResult.getResultStatus().equals("9000")) {
                showToast("支付成功");
                getData();
            } else if (payResult.getResultStatus().equals("8000")) {
                showToast("支付结果确认中");
            } else if (payResult.getResultStatus().equals("6001")) {
                showToast("支付取消");
            } else {
                showToast("订单支付失败");
            }
            popuWindow.dismiss();
        });
        RxBus.getDefault().register(this, BaseResp.class, resp -> {

            if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
                if (resp.errCode == 0) {
                    showToast("支付成功");
                    getData();
                } else if (resp.errCode == -2) {
                    showToast("取消交易");
                } else {
                    showToast("支付失败");
                }
            }
            popuWindow.dismiss();
        });
        RxBus.getDefault().register(this, ObjectEvent.class, message -> {
            showToast(message.msg);
            popuWindow.dismiss();
        });
    }

    @Override
    public void lookOrderInfo(String info) {
        if (info != null) {
            Bundle bundle = new Bundle();
            bundle.putString("data", info);
            readyGo(OrderDetailActivity.class, bundle);
        }
    }

    @Override
    public void geExpressInfo(LogisticsInfo data) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.DATA, data);
        readyGo(LogisticsActivity.class, bundle);

    }

    private void setData() {
        //最外层布局
        linearLayout.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 5, 0, 0);
        for (int i = 0; i < list.size(); i++) {//获取每一层订单
            if (Utils.isEmpty(list.get(i).order_list)) {
                for (int j = 0; j < list.get(i).order_list.size(); j++) {//每个订单内部有多个店铺
                    LinearLayout item = (LinearLayout) getView(R.layout.order_list_item);//将要添加得布局

                    item.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_circle_white_5));
                    item.setLayoutParams(layoutParams);
                    //主view
                    TextView storename = getView(item, R.id.store_name);
                    TextView storesatus = getView(item, R.id.store_stauts);
                    TextView goodscount = getView(item, R.id.goods_count);
                    TextView goodsmoney = getView(item, R.id.goods_money);
                    TextView goodsfreight = getView(item, R.id.goods_freight);
                    TextView order_backmoneyorgoods = getView(item, R.id.order_backmoneyorgoods);
                    //操作view
                    LinearLayout itembtnview = getView(item, R.id.itembtnview);
                    View orderdel = getView(item, R.id.order_del);
                    TextView btn1 = getView(item, R.id.order_btn1);//默认隐藏
                    TextView btn2 = getView(item, R.id.order_btn2);//默认隐藏
                    TextView btn3 = getView(item, R.id.order_btn3);//默认隐藏
                    //装同一个商店得商品列表
                    LinearLayout goodslistview = getView(item, R.id.goodslistview);
                    goodslistview.removeAllViews();
                    //计算该店铺总共购买数量
                    int storeallgoodsnumber = 0;
                    if (Utils.isEmpty(list.get(i).order_list.get(j).extend_order_goods)) {
                        for (int k = 0; k < list.get(i).order_list.get(j).extend_order_goods.size(); k++) {//每个店铺有多个商品
//                            Logutils.i(list.get(i).order_list.get(j).extend_order_goods.get(k));
                            LinearLayout gooditemlayout = (LinearLayout) getView(R.layout.order_list_goods_item);
                            ImageView goodsimg = getView(gooditemlayout, R.id.goods_img);
                            TextView goodsname = getView(gooditemlayout, R.id.goods_name);
                            TextView goodsprice = getView(gooditemlayout, R.id.goods_price);
                            TextView goodsnumber = getView(gooditemlayout, R.id.goods_number);
                            GlideEngine.getInstance().loadImage(mContext, goodsimg, Utils.getString(list.get(i).order_list.get(j).extend_order_goods.get(k).goods_image_url));
                            goodsname.setText(Utils.getString(list.get(i).order_list.get(j).extend_order_goods.get(k).goods_name));
                            goodsprice.setText(Utils.getString(list.get(i).order_list.get(j).extend_order_goods.get(k).goods_price));
                            goodsnumber.setText(Utils.isEmpty(list.get(i).order_list.get(j).extend_order_goods.get(k).goods_num) ? Utils.getString(list.get(i).order_list.get(j).extend_order_goods.get(k).goods_num) : "0");
                            //计算该店铺总共购买数量
                            storeallgoodsnumber += Double.valueOf(Utils.isEmpty(list.get(i).order_list.get(j).extend_order_goods.get(k).goods_num) ? Utils.getString(list.get(i).order_list.get(j).extend_order_goods.get(k).goods_num) : "0");
                            goodslistview.addView(gooditemlayout);
                        }
                    }
                    int indexi = i;
                    int indexj = j;


                    //显示店铺名字
                    storename.setText(Utils.getString(list.get(i).order_list.get(j).store_name));
                    //显示状态
                    storesatus.setText(Utils.getString(list.get(i).order_list.get(j).state_desc));
                    //总数
                    goodscount.setText("" + storeallgoodsnumber);
                    //总钱
                    goodsmoney.setText(list.get(i).order_list.get(j).goods_amount);
                    //邮费
                    goodslistview.setOnClickListener(v -> {
                        mPresenter.orderInfo("order_info", App.getApp().getKey(), list.get(indexi).order_list.get(indexj).order_id);
                    });
                    goodsfreight.setText(list.get(i).order_list.get(j).shipping_fee);
                    if (list.get(i).order_list.get(j).if_again) {
                        btn3.setVisibility(View.VISIBLE);
                        btn3.setText("再次确认");

                        btn3.setOnClickListener(v -> {
                            showWrinDialog("再次确认订单？", "order_receive", list.get(indexi).order_list.get(indexj).order_id);
                        });
                    }
                    if (list.get(i).order_list.get(j).if_delete) {
                        orderdel.setVisibility(View.VISIBLE);
                        orderdel.setOnClickListener(v -> {
                            showWrinDialog("确认删除订单？", "order_delete", list.get(indexi).order_list.get(indexj).order_id);
                        });
                    }
                    if (list.get(i).order_list.get(j).if_cancel) {
                        btn1.setVisibility(View.VISIBLE);
                        btn2.setVisibility(View.VISIBLE);
                        btn1.setText("取消订单");
                        btn1.setOnClickListener(v -> {
                            showWrinDialog("确认取消订单？", "order_cancel", list.get(indexi).order_list.get(indexj).order_id);
                        });
                        btn2.setOnClickListener(v -> {
                            pay_sn = list.get(indexi).order_list.get(indexj).pay_sn;
                            mPresenter.getPaymentList(App.getApp().getKey());
                        });
                    }
                    if (list.get(i).order_list.get(j).if_receive) {
                        btn3.setVisibility(View.VISIBLE);
                        btn3.setText("确认收货");
                        btn3.setOnClickListener(v -> {
                            showWrinDialog("确认收到货物", "order_receive", list.get(indexi).order_list.get(indexj).order_id);
                        });
                    }
                    if (list.get(i).order_list.get(j).if_lock) {
                        order_backmoneyorgoods.setVisibility(View.VISIBLE);
                    }
                    if (list.get(i).order_list.get(j).if_evaluation) {
                        btn3.setVisibility(View.VISIBLE);
                        btn3.setText("订单评价");
                    }
                    if (list.get(i).order_list.get(j).if_evaluation_again) {
                        btn3.setVisibility(View.VISIBLE);
                        btn3.setText("追加评价");
                    }
                    if (list.get(i).order_list.get(j).if_refund_cancel) {
                        btn1.setVisibility(View.VISIBLE);
                        btn1.setText("退款");
                    }
                    if (list.get(i).order_list.get(j).if_deliver) {
                        btn1.setVisibility(View.VISIBLE);
                        btn1.setText("查看物流");
                        btn1.setOnClickListener(v -> {
                            mPresenter.searchLogistics(App.getApp().getKey(), list.get(indexi).order_list.get(indexj).order_id);
                        });
                    }
                    linearLayout.addView(item);
                }


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
                    mPresenter.orderOperation(url, App.getApp().getKey(), order_id, selecttype, page, selecttype ? viltype[position] : realtype[position], searchtext);
                    d.dismiss();
                }).create();
        dialog.show();
    }

    @Override
    public void faild(String msg) {

        if (orderViewsRefresh != null) {
            if (orderViewsRefresh.isRefreshing())
                orderViewsRefresh.finishRefresh();
            if (orderViewsRefresh.isLoading())
                orderViewsRefresh.finishLoadMore();
        }
        showToast(msg);
    }


    public Object onButtonPressed(String key, Object value) {
        if (mListener != null) {
            return mListener.doSomeThing(key, value);
        }
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
