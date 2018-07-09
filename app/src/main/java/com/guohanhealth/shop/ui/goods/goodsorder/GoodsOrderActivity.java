package com.guohanhealth.shop.ui.goods.goodsorder;

import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.guohanhealth.shop.R;
import com.guohanhealth.shop.adapter.CommonAdapter;
import com.guohanhealth.shop.adapter.ViewHolder;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.bean.AddressInfo;
import com.guohanhealth.shop.bean.GoodsOrderInfo;
import com.guohanhealth.shop.bean.PayWayInfo;
import com.guohanhealth.shop.bean.Step2Info;
import com.guohanhealth.shop.bean.Store_cart_list;
import com.guohanhealth.shop.bean.UpDataAddressInfo;
import com.guohanhealth.shop.custom.CustomPopuWindow;
import com.guohanhealth.shop.event.Functions;
import com.guohanhealth.shop.event.RxBus;
import com.guohanhealth.shop.ui.goods.GoodsModel;
import com.guohanhealth.shop.ui.goods.goodsdetailed.activity.GoodsDetailsActivity;
import com.guohanhealth.shop.ui.order.OrderActivity;
import com.guohanhealth.shop.utils.JSONParser;
import com.guohanhealth.shop.utils.Logutils;
import com.guohanhealth.shop.utils.PayResult;
import com.guohanhealth.shop.utils.Utils;
import com.guohanhealth.shop.utils.engine.GlideEngine;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;

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
import io.reactivex.functions.Consumer;

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
    private int ifcart;
    private String cart_id;
    private String address_id;
    private String vat_hash;
    private String offpay_hash;
    private String offpay_hash_batch;
    private String if_pd_pay = "0";//记录是否充值卡支付  1-使用 0-不使用
    private String if_rcb_pay = "0";//记录是否预存款支付 1-使用 0-不使用
    private String healthbean_pay = "0";//记录是否健康豆支付 1-使用 0-不使用
    private String password;/*支付密码*/
    private String pay_message; /*店铺评论 */
    private String data = ""; /*数据*/
    String pay_name = "online";  /*online(线上付款) offline(货到付款)*/
    private int inv_id;
    private String pay_sn;
    private PopupWindow popuWindow;
    private String is_virtual;
    private String goods_number;
    private String buyer_phone;

    public GoodsOrderActivity() {
        list = new ArrayList<>();
        datalist = new ArrayList<>();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_goods_order;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        initToolBarNav(commonToolbar, commonToolbarTitleTv, "确认订单");
        data = getIntent().getStringExtra("data");
        is_virtual = getIntent().getStringExtra(Constants.IS_VIRTUAL);
        ifcart = getIntent().getIntExtra(Constants.IFCART, 0);
        cart_id = getIntent().getStringExtra(Constants.CART_ID);
        goods_number = getIntent().getStringExtra(Constants.GOODS_NUMBER);

        goodsOrderViewAddress.setOnClickListener(v -> {
            showToast("添加地址正在开发中");
        });
        goodsOrderViewPayway.setOnClickListener(c -> {
            showPayWayWindown();
        });
        goodsOrderViewInvoice.setOnClickListener(v -> {
            shopInvoiceWindown();
        });

        goodsOrderTextApply.setOnClickListener(v -> {

            mPresenter.buyStep2(App.getApp().getKey(), cart_id, "" + ifcart, address_id, vat_hash,
                    offpay_hash, offpay_hash_batch, pay_name,
                    inv_id + "", if_pd_pay, if_rcb_pay, healthbean_pay,
                    password, is_virtual, goods_number, buyer_phone);
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
            freight_hash = Utils.getString(info.freight_hash);
            goodsOrderTextMoney.setText(Utils.getString(Utils.getString(info.order_amount)));
            list.clear();
            if (Utils.getString(info.address_api.allow_offpay).equals("1") && Utils.getString(info.ifshow_offpay).equals("true")) {
                list.add("货到付款");
            }
            list.add("在线支付");
            vat_hash = Utils.getString(info.vat_hash);
            inv_id = info.inv_info.inv_id;
            offpay_hash = Utils.getString(info.address_api.offpay_hash);
            offpay_hash_batch = Utils.getString(info.address_api.offpay_hash_batch);
            goodsOrderTextInvoice.setText(Utils.getString(info.inv_info.content));
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

    @Override
    public void updataAddress(UpDataAddressInfo info) {
        Logutils.i(info);
        offpay_hash = info.offpay_hash;
        offpay_hash_batch = info.offpay_hash_batch;
    }

    @Override
    public void getPayInfo(Step2Info info) {
        mPresenter.getPaymentList(App.getApp().getKey());
        pay_sn = info.pay_sn;
//        if (info != null && info.pay_info.equals("true")) {
//
//        }
    }


    @Override
    public void getPaymentList(PayWayInfo info) {
        Logutils.i(info.payment_list.size());
        popuWindow = Utils.shopPayWindown(mContext, info.payment_list, pay_sn, is_virtual, v -> {
            endOrder();
        });
        RxBus.getDefault().register(this, PayResult.class, payResult -> {
            Logutils.i(payResult.getResult());
            if (payResult.getResultStatus().equals("9000")) {
                showToast("支付成功");
            } else if (payResult.getResultStatus().equals("8000")) {
                showToast("支付结果确认中");
            } else if (payResult.getResultStatus().equals("6001")) {
                showToast("支付取消");
            } else {
                showToast("订单支付失败");
            }
            endOrder();
        });
        RxBus.getDefault().register(this, BaseResp.class, resp -> {
            if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
                if (resp.errCode == 0) {
                    Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
                } else if (resp.errCode == -2) {
                    Toast.makeText(this, "取消交易", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show();
                }
                endOrder();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (popuWindow != null && popuWindow.isShowing()) {
            return false;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (popuWindow != null && popuWindow.isShowing()) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void endOrder() {
        if (popuWindow != null) {
            popuWindow.dismiss();
        }
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.ORDERINDEX, 1);
        readyGoThenKill(OrderActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().unRegister(this);
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
            EditText store_edit = helper.getView(R.id.store_edit);
            pay_message = Utils.getEditViewText(store_edit);
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
        TagContainerLayout tagContainerLayout = getView(view, R.id.tagcontainerlayout);
        List<String> list = Arrays.asList(new String[]{info.inv_info.content});
        tagContainerLayout.setTags(list);
        for (int i = 0; i < tagContainerLayout.getChildCount(); i++) {
            TagView tagView = (TagView) tagContainerLayout.getChildAt(i);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tagView.setLayoutParams(layoutParams);
            tagView.setMinimumWidth(getResources().getDimensionPixelSize(R.dimen.dp_50));
            tagView.setTextSize(getResources().getDimension(R.dimen.sp_12));
            if (tagView.getText().toString().equals(Utils.getTextViewText(goodsOrderTextInvoice))) {
                tagView.setTagBorderColor(getResources().getColor(R.color.appColor));
                tagView.setTagBackgroundColor(getResources().getColor(R.color.appColor));
                tagView.setTagTextColor(getResources().getColor(R.color.white));
            } else {
                tagView.setTagBorderColor(getResources().getColor(R.color.gray));
                tagView.setTagBackgroundColor(getResources().getColor(R.color.translucent));
                tagView.setTagTextColor(getResources().getColor(R.color.nc_text));
            }
        }

        tagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                invoicepopuWindow.dissmiss();
                goodsOrderTextInvoice.setText(text);
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }
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


    /**
     * @param info
     */
    private void setAddress(AddressInfo info) {
        if (info != null) {
            address_id = info.address_id;
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
