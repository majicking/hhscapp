package com.majick.guohanhealth.ui.goods.goodsorder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.JsonParser;
import com.majick.guohanhealth.R;
import com.majick.guohanhealth.adapter.CommonAdapter;
import com.majick.guohanhealth.adapter.ViewHolder;
import com.majick.guohanhealth.app.App;
import com.majick.guohanhealth.base.BaseActivity;
import com.majick.guohanhealth.bean.AddressInfo;
import com.majick.guohanhealth.bean.GoodsInfo;
import com.majick.guohanhealth.bean.GoodsOrderInfo;
import com.majick.guohanhealth.bean.SpecBean;
import com.majick.guohanhealth.bean.Store_cart_list;
import com.majick.guohanhealth.custom.CustomPopuWindow;
import com.majick.guohanhealth.ui.goods.GoodsModel;
import com.majick.guohanhealth.utils.JSONParser;
import com.majick.guohanhealth.utils.Logutils;
import com.majick.guohanhealth.utils.Utils;
import com.majick.guohanhealth.utils.engine.GlideEngine;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodsOrderActivity extends BaseActivity<GoodsOrderPercenter, GoodsModel> implements GoodsOrderView {


    @BindView(R.id.common_toolbar_title_tv)
    TextView commonToolbarTitleTv;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.goods_order_view_recycleview)
    RecyclerView goodsOrderViewRecycleview;
    @BindView(R.id.goods_order_view_address)
    LinearLayout goodsOrderViewAddress;
    @BindView(R.id.goods_order_view_payway)
    LinearLayout goodsOrderViewPayway;
    @BindView(R.id.goods_order_view_invoice)
    LinearLayout goodsOrderViewInvoice;
    @BindView(R.id.goods_order_text_money)
    TextView goodsOrderTextMoney;
    @BindView(R.id.goods_order_text_apply)
    TextView goodsOrderTextApply;
    @BindView(R.id.goods_order_text_name)
    TextView goodsOrderTextName;
    @BindView(R.id.goods_order_text_mobile)
    TextView goodsOrderTextMobile;
    @BindView(R.id.goods_order_text_address)
    TextView goodsOrderTextAddress;
    @BindView(R.id.goods_order_view_address_default)
    LinearLayout goodsOrderViewAddressDefault;
    @BindView(R.id.goods_order_view_address_null)
    LinearLayout goodsOrderViewAddressNull;

    String freight_hash = "";
    @BindView(R.id.goods_order_text_payway)
    TextView goodsOrderTextPayway;
    @BindView(R.id.goods_order_text_invoice)
    TextView goodsOrderTextInvoice;
    private GoodsOrderInfo info;
    private CustomPopuWindow payWaypopuWindow;
    private CustomPopuWindow invoicepopuWindow;
    List<String> list;

    public GoodsOrderActivity() {
        list = new ArrayList<>();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_goods_order;
    }

    /**
     * 数据
     */
    String data = "";
    /**
     * online(线上付款) offline(货到付款)
     */
    String pay_name = "online";

    @Override
    protected void initView(Bundle savedInstanceState) {
        initToolBarNav(commonToolbar, commonToolbarTitleTv, "确认订单");
        data = getIntent().getStringExtra("data");
        goodsOrderViewAddress.setOnClickListener(v -> {
            showToast("添加地址正在开发中");
        });
        goodsOrderViewPayway.setOnClickListener(c -> {
            showPayWayWindown();
        });
        goodsOrderViewInvoice.setOnClickListener(v -> {
            shopInvoiceWindown();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.isEmpty(data)) {
            setData(data);
        }
    }

    public void setData(String data) {
        info = JSONParser.JSON2Object(data, GoodsOrderInfo.class);
        if (info != null) {
            freight_hash = info.freight_hash;
            goodsOrderTextMoney.setText(Utils.getString(info.order_amount));
            if (info.address_api.allow_offpay.equals("1") && info.ifshow_offpay.equals("true")) {
                list.add("货到付款");
            }
            list.add("在线支付");
            setAddress(info.address_info);
            setOrderData(JSONParser.getStringFromJsonString("store_cart_list", data));
        }

    }

    public void setOrderData(String data) {
        List<String> lists = getSpecList(data);
        Logutils.i(Utils.isEmpty(lists));
        List<Store_cart_list> lists1 = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {
            Store_cart_list in = JSONParser.JSON2Object(lists.get(i), Store_cart_list.class);
            lists1.add(in);
        }
        OrderAdapter adapter = new OrderAdapter(R.layout.goods_order_list_item, lists1);
        goodsOrderViewRecycleview.setLayoutManager(new LinearLayoutManager(mContext));
        goodsOrderViewRecycleview.setAdapter(adapter);
    }


    public List<String> getSpecList(String data) {
        List<String> list = new ArrayList<>();
        if (Utils.isEmpty(data)) {
            try {
                JSONObject jsonGoods_spec = new JSONObject(data);
                Iterator<?> itName = jsonGoods_spec.keys();
                while (itName.hasNext()) {
                    String specID = itName.next().toString();
                    String specV = jsonGoods_spec.getString(specID);
                    list.add(specV);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return list;

    }

    class OrderAdapter extends BaseQuickAdapter<Store_cart_list, BaseViewHolder> {
        public OrderAdapter(int layoutResId, List<Store_cart_list> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Store_cart_list item) {

            helper.setText(R.id.store_name, item.getStore_name());
            helper.setText(R.id.store_money, item.getStore_goods_total());

            RecyclerView recycleview = helper.getView(R.id.recycleview);
            recycleview.setLayoutManager(new LinearLayoutManager(mContext));
            OrderDetailAdapter detailAdapter = new OrderDetailAdapter(R.layout.goods_orderdetail_list_item, item.getGoods_list());
            recycleview.setAdapter(detailAdapter);

        }
    }

    class OrderDetailAdapter extends BaseQuickAdapter<Store_cart_list.GoodsListBean, BaseViewHolder> {
        public OrderDetailAdapter(int layoutResId, @Nullable List<Store_cart_list.GoodsListBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Store_cart_list.GoodsListBean item) {
            GlideEngine.getInstance().loadImage(mContext, helper.getView(R.id.img), item.getGoods_image_url());
            helper.setText(R.id.text, item.getGoods_name());
            helper.setText(R.id.number, item.getGoods_num() + "件");
            helper.setText(R.id.money, item.getGoods_price());


        }
    }

    public void showPayWayWindown() {
        View view = getView(R.layout.goods_order_payway);
        payWaypopuWindow = Utils.getPopuWindown(mContext, view, Gravity.BOTTOM);
        GridView gridView = getView(view, R.id.gridview);
        gridView.setAdapter(new CommonAdapter<String>(mContext, list, R.layout.spec_item) {
            @Override
            public void convert(ViewHolder viewHolder, String item, int position, View convertView, ViewGroup parentViewGroup) {
                TextView textView = viewHolder.getView(R.id.text);
                textView.setText(item);
                if (item.equals(Utils.getTextViewText(goodsOrderTextPayway))) {
                    textView.setActivated(true);
                } else {
                    textView.setActivated(false);
                }
            }
        });
        gridView.setOnItemClickListener((parent, view1, position, id) -> {
            for (int i = 0; i < gridView.getChildCount(); i++) {
                LinearLayout linearLayout = (LinearLayout) parent.getChildAt(i);
                TextView textView = linearLayout.findViewById(R.id.text);
                if (textView.getText().toString().equals(list.get(position))) {
                    textView.setActivated(true);
                } else {
                    textView.setActivated(false);
                }
            }
            switch (position) {
                case 1:
                    pay_name = "offline";
                    break;
                default:
                    pay_name = "online";
                    break;
            }
            goodsOrderTextPayway.setText(list.get(position));
        });

        TextView ok = getView(view, R.id.ok);
        ImageView close = getView(view, R.id.close);
        ok.setOnClickListener(v -> {
            payWaypopuWindow.dissmiss();
        });
        close.setOnClickListener(v -> {
            payWaypopuWindow.dissmiss();
        });
    }

    public void shopInvoiceWindown() {
        View view = getView(R.layout.goods_order_invoice);
        invoicepopuWindow = Utils.getPopuWindown(mContext, view, Gravity.BOTTOM);
        RecyclerView recycleview = getView(view, R.id.recycleview);
        recycleview.setLayoutManager(new GridLayoutManager(mContext, 5));
        LinearLayout layout = (LinearLayout) getView(R.layout.item_text);
        TextView textView = getView(layout, R.id.text);
        textView.setText("不使用发票");
        List<String> list = Arrays.asList(new String[]{"不使用发票"});
        MAdapter adapter = new MAdapter(R.layout.spec_item, list);
        recycleview.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                invoicepopuWindow.dissmiss();
                goodsOrderTextInvoice.setText(list.get(position));
            }
        });
        textView.setOnClickListener(v -> {
            invoicepopuWindow.dissmiss();
            goodsOrderTextInvoice.setText(Utils.getTextViewText(textView));
        });
        TextView ok = getView(view, R.id.ok);
        ImageView close = getView(view, R.id.close);
        ok.setOnClickListener(v -> {
            showToast("新增发票正在开发");
        });
        close.setOnClickListener(v -> {
            invoicepopuWindow.dissmiss();
        });
    }

    private class MAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public MAdapter(int layoutResId, @Nullable List data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.text, item);
        }
    }

    /**
     * @param info
     */
    private void setAddress(AddressInfo info) {
        if (info != null) {
            goodsOrderViewAddressDefault.setVisibility(View.VISIBLE);
            goodsOrderViewAddressNull.setVisibility(View.GONE);
            goodsOrderTextName.setText(Utils.getString(info.true_name));
            goodsOrderTextAddress.setText(Utils.getString(info.area_info) + " " + Utils.getString(info.address));
            goodsOrderTextMobile.setText(Utils.getString(info.mob_phone));
            updataPayWay(info.city_id, info.area_id);
        } else {
            goodsOrderViewAddressDefault.setVisibility(View.GONE);
            goodsOrderViewAddressNull.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 根据地址判断是否支持货到付款
     *
     * @param city_id
     * @param area_id
     */
    private void updataPayWay(String city_id, String area_id) {
        mPresenter.updataAdress(App.getApp().getKey(), city_id, area_id, freight_hash);
    }

    @Override
    public void faild(String msg) {
        showToast(msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
