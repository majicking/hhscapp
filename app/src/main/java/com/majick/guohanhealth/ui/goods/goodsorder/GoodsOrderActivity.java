package com.majick.guohanhealth.ui.goods.goodsorder;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.majick.guohanhealth.R;
import com.majick.guohanhealth.adapter.CommonAdapter;
import com.majick.guohanhealth.adapter.ViewHolder;
import com.majick.guohanhealth.app.App;
import com.majick.guohanhealth.app.Constants;
import com.majick.guohanhealth.base.BaseActivity;
import com.majick.guohanhealth.bean.AddressInfo;
import com.majick.guohanhealth.bean.GoodsOrderInfo;
import com.majick.guohanhealth.bean.Store_cart_list;
import com.majick.guohanhealth.custom.CustomPopuWindow;
import com.majick.guohanhealth.ui.goods.GoodsModel;
import com.majick.guohanhealth.ui.goods.goodsdetailed.activity.GoodsDetailsActivity;
import com.majick.guohanhealth.utils.JSONParser;
import com.majick.guohanhealth.utils.Logutils;
import com.majick.guohanhealth.utils.Utils;
import com.majick.guohanhealth.utils.engine.GlideEngine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

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
    private List<String> datalist;

    public GoodsOrderActivity() {
        list = new ArrayList<>();
        datalist = new ArrayList<>();
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
            list.clear();
            if (info.address_api.allow_offpay.equals("1") && info.ifshow_offpay.equals("true")) {
                list.add("货到付款");
            }
            list.add("在线支付");
            setAddress(info.address_info);
            setOrderData(JSONParser.getStringFromJsonString("store_cart_list", data));
        }

    }

    public void setOrderData(String data) {
        datalist = getDataList(data);
        Logutils.i(Utils.isEmpty(datalist));
        List<Store_cart_list> lists1 = new ArrayList<>();
        for (int i = 0; i < datalist.size(); i++) {
            Store_cart_list in = JSONParser.JSON2Object(datalist.get(i), Store_cart_list.class);
            lists1.add(in);
        }
        OrderAdapter adapter = new OrderAdapter(R.layout.goods_order_list_item, lists1, datalist);
        goodsOrderViewRecycleview.setLayoutManager(new LinearLayoutManager(mContext));
        goodsOrderViewRecycleview.setAdapter(adapter);
    }


    public List<String> getDataList(String data) {
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;

    }

    /**
     * 循环店铺适配器
     */
    class OrderAdapter extends BaseQuickAdapter<Store_cart_list, BaseViewHolder> {
        List<String> storelist;

        public OrderAdapter(int layoutResId, List<Store_cart_list> data, List<String> datalist) {
            super(layoutResId, data);
            storelist = new ArrayList<>();
            storelist = datalist;
        }

        @Override
        protected void convert(BaseViewHolder helper, Store_cart_list item) {
            helper.setText(R.id.store_name, Utils.getString(item.getStore_name()));
            helper.setText(R.id.store_money, Utils.getString(item.getStore_goods_total()));
            RecyclerView recycleview = helper.getView(R.id.recycleview);
            recycleview.setLayoutManager(new LinearLayoutManager(mContext));
            OrderDetailAdapter detailAdapter = new OrderDetailAdapter(R.layout.goods_orderdetail_list_item, item.getGoods_list(), storelist.get(helper.getLayoutPosition()));
            detailAdapter.setOnItemClickListener((adapter, view, position) -> {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.GOODS_ID, "" + item.getGoods_list().get(position).getGoods_id());
                readyGo(GoodsDetailsActivity.class, bundle);
            });
            recycleview.setAdapter(detailAdapter);
        }
    }

    /**
     * 循环店铺里面得商品适配器
     */
    class OrderDetailAdapter extends BaseQuickAdapter<Store_cart_list.GoodsListBean, BaseViewHolder> {
        String giftlist;

        public OrderDetailAdapter(int layoutResId, @Nullable List<Store_cart_list.GoodsListBean> data, String giftlist) {
            super(layoutResId, data);
            this.giftlist = giftlist;
        }

        @Override
        protected void convert(BaseViewHolder helper, Store_cart_list.GoodsListBean item) {

            GlideEngine.getInstance().loadImage(mContext, helper.getView(R.id.img), item.getGoods_image_url());
            helper.setText(R.id.text, Utils.getString(item.getGoods_name()));
            helper.setText(R.id.number, item.getGoods_num() + "件");
            helper.setText(R.id.money, Utils.getString(item.getGoods_price()));
            ListView listView = helper.getView(R.id.listview);
            List<String> mlist = new ArrayList<>();
            String goods_list = JSONParser.getStringFromJsonString("goods_list", giftlist);
            if (Utils.isEmpty(goods_list)) {
                try {
                    JSONArray jsonArray = new JSONArray(goods_list);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String json = JSONParser.jsonToFormat(jsonObject.toString());
                        mlist.add(json);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                List<Store_cart_list.GoodsListBean.Gift_list> datalist = new ArrayList<>();

                for (int i = 0; i < mlist.size(); i++) {
                    try {
                        String json = JSONParser.getStringFromJsonString("gift_list", mlist.get(i));
                        if (Utils.isEmpty(json)) {
                            try {
                                com.alibaba.fastjson.JSONObject.parseObject(json);
                                JSONObject object = new JSONObject(json);
                                Iterator<?> itName = object.keys();
                                while (itName.hasNext()) {
                                    String specID = itName.next().toString();
                                    String specV = object.getString(specID);
                                    datalist.add(JSONParser.JSON2Object(specV, Store_cart_list.GoodsListBean.Gift_list.class));
                                }
                            } catch (com.alibaba.fastjson.JSONException e) {
                                try {
                                    com.alibaba.fastjson.JSONObject.parseArray(json);
                                    datalist = JSONParser.JSON2Array(json, Store_cart_list.GoodsListBean.Gift_list.class);
                                } catch (com.alibaba.fastjson.JSONException e1) {
                                    e.printStackTrace();
                                    e1.printStackTrace();
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (Utils.isEmpty(datalist)) {
                    /**循环店铺里面的商品的赠品适配器*/
                    listView.setAdapter(new CommonAdapter<Store_cart_list.GoodsListBean.Gift_list>(mContext, datalist, R.layout.goods_order_gifarray_item) {
                        @Override
                        public void convert(ViewHolder viewHolder, Store_cart_list.GoodsListBean.Gift_list item, int position, View convertView, ViewGroup parentViewGroup) {
                            GlideEngine.getInstance().loadImage(mContext, viewHolder.getView(R.id.img), Utils.getString(item.gift_goodsimage));
                            viewHolder.setText(R.id.text, Utils.getString(item.gift_goodsname) + " x" + Utils.getString(item.gift_amount));
                        }
                    });
                }
            }

        }
    }


    public List<Store_cart_list.GoodsListBean.Gift_list> getGiftArrayList(String data) {
        List<Store_cart_list.GoodsListBean.Gift_list> lists = new ArrayList<>();
        try {
            JSONObject jsonGoods_spec = new JSONObject(data);
            Iterator<?> itName = jsonGoods_spec.keys();
            while (itName.hasNext()) {
                String specID = itName.next().toString();
                String specV = jsonGoods_spec.getString(specID);
                lists.add(JSONParser.JSON2Object(specV, Store_cart_list.GoodsListBean.Gift_list.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lists;
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
        TagContainerLayout tagContainerLayout = getView(view, R.id.tagcontainerlayout);
        recycleview.setLayoutManager(new GridLayoutManager(mContext, 4));
        LinearLayout layout = (LinearLayout) getView(R.layout.item_text);
        TextView textView = getView(layout, R.id.text);
        textView.setText("不使用发票");

        List<String> list = Arrays.asList(new String[]{"不使用发票"});
        tagContainerLayout.setTags(list);
        for (int i = 0; i < tagContainerLayout.getChildCount(); i++) {
            tagContainerLayout.getChildAt(i).setOnClickListener(c->{
                showToast("11111111111111");
            });
        }

        tagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                showToast(text);
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }
        });
        MAdapter adapter = new MAdapter(R.layout.spec_item, list);
        recycleview.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view1, position) -> {
            invoicepopuWindow.dissmiss();
            goodsOrderTextInvoice.setText(list.get(position));
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
