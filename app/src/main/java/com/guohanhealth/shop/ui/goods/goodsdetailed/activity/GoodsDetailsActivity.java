package com.guohanhealth.shop.ui.goods.goodsdetailed.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.bean.GoodsDetailedInfo;
import com.guohanhealth.shop.bean.SpecBean;
import com.guohanhealth.shop.custom.CustomPopuWindow;
import com.guohanhealth.shop.event.GoodsDetailsEvent;
import com.guohanhealth.shop.event.OnFragmentInteractionListener;
import com.guohanhealth.shop.event.RxBus;
import com.guohanhealth.shop.ui.goods.GoodsModel;
import com.guohanhealth.shop.ui.goods.goodsdetailed.fragment.comment.CommentFragment;
import com.guohanhealth.shop.ui.goods.goodsdetailed.fragment.goods.GoodsFragment;
import com.guohanhealth.shop.ui.goods.goodsdetailed.fragment.goodsdetail.GoodsDetailFragment;
import com.guohanhealth.shop.ui.goods.goodsorder.GoodsOrderActivity;
import com.guohanhealth.shop.ui.main.activity.MainActivity;
import com.guohanhealth.shop.utils.JSONParser;
import com.guohanhealth.shop.utils.Logutils;
import com.guohanhealth.shop.utils.Utils;
import com.guohanhealth.shop.utils.engine.GlideEngine;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import co.lujun.androidtagview.TagContainerLayout;

public class GoodsDetailsActivity extends BaseActivity<GoodsDetailsPersenter, GoodsModel> implements GoodsDetailsView, OnFragmentInteractionListener {

    String goods_id;
    @BindView(R.id.goodsdetail_back)
    ImageView goodsdetailBack;
    @BindView(R.id.goodsdetail_view_tablayout)
    TabLayout goodsdetailViewTablayout;
    @BindView(R.id.goodsdetail_more)
    ImageView goodsdetailMore;
    @BindView(R.id.goodsdetail_view_viewpager)
    ViewPager goodsdetailViewViewpager;
    List<String> titles = new ArrayList<>(Arrays.asList("商品", "详细", "评论"));
    @BindView(R.id.goodsdetail_view_title)
    LinearLayout goodsdetailViewTitle;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.goodsdetail_view_customer)
    LinearLayout goodsdetailViewCustomer;
    @BindView(R.id.goodsdetail_view_cart)
    LinearLayout goodsdetailViewCart;
    @BindView(R.id.goodsdetail_text_cartnumber)
    TextView goodsdetailTextCartnumber;
    @BindView(R.id.goodsdetail_text_buy)
    TextView goodsdetailTextBuy;
    @BindView(R.id.goodsdetail_text_addcart)
    TextView goodsdetailTextAddcart;
    @BindView(R.id.goodsdetail_view_order)
    LinearLayout goodsdetailViewOrder;
    private List<Fragment> fragmentList;
    private ViewPagerAdapter adapter;
    private GoodsFragment goodsFragment;
    private GoodsDetailFragment goodsDetailFragment;
    private CommentFragment commentFragment;
    private View orderView;
    private CustomPopuWindow popuWindow;
    private String is_virtual;
    private EditText number;
    private String cart_id;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_goods_details;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        goods_id = getIntent().getStringExtra(Constants.GOODS_ID);
        goodsdetailBack.setOnClickListener(v -> finish());
        fragmentList = new ArrayList<>();
        addFragment();
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        goodsdetailViewViewpager.setAdapter(adapter);
        goodsdetailViewViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(goodsdetailViewTablayout));
        goodsdetailViewTablayout.setupWithViewPager(goodsdetailViewViewpager);
        line.setVisibility(View.GONE);
        goodsdetailViewViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    line.setVisibility(View.GONE);
                    goodsdetailViewTablayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.translucent));
                    goodsdetailViewTablayout.setTabTextColors(getResources().getColor(R.color.white), getResources().getColor(R.color.appColor));
                    goodsdetailViewTitle.setBackgroundColor(getResources().getColor(R.color.translucent));
                    goodsdetailViewTitle.setSelected(false);
                    goodsdetailBack.setSelected(false);
                    goodsdetailMore.setSelected(false);
                } else {
                    goodsdetailViewTablayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.appColor));
                    goodsdetailViewTitle.setBackgroundColor(getResources().getColor(R.color.white));
                    goodsdetailViewTablayout.setTabTextColors(getResources().getColor(R.color.nc_text), getResources().getColor(R.color.appColor));
                    line.setVisibility(View.VISIBLE);
                    goodsdetailViewTablayout.setSelected(true);
                    goodsdetailMore.setSelected(true);
                    goodsdetailBack.setSelected(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        goodsdetailTextBuy.setOnClickListener(v -> {

            showOrder(1);
        });
        goodsdetailTextAddcart.setOnClickListener(v -> {
            showOrder(2);
        });

        setOnChangeGoodsInfoListener((type, data) -> {
            if (type.equals("updata_goods_number")) {
                if (Integer.valueOf(data) <= 0) {
                    goods_number = 1;
                    number.setText(goods_number + "");
                }
            }
        });
        goodsdetailViewCart.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.MAINNUMBER, 2);
            readyGo(MainActivity.class, bundle);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getCardNumber(App.getApp().getKey());
        getData(goods_id);
    }

    public void getData(String goods_id) {
        mPresenter.getGoodsDetails(this, goods_id, App.getApp().getKey());
    }

    private void addFragment() {
        goodsFragment = GoodsFragment.newInstance("", "");
        goodsDetailFragment = GoodsDetailFragment.newInstance("", "");
        commentFragment = CommentFragment.newInstance("", "");
        fragmentList.add(goodsFragment);
        fragmentList.add(goodsDetailFragment);
        fragmentList.add(commentFragment);
    }

    public String goodsdata;

    public String getGoodsdata() {
        return goodsdata;
    }

    public void setGoodsdata(String goodsdata) {
        this.goodsdata = goodsdata;
    }

    @Override
    public Object doSomeThing(String key, Object value) {
        if (Constants.CURRENTITEM.equals(key)) {
            goodsdetailViewViewpager.setCurrentItem((Integer) value);
        } else if (Constants.GOODS_ID.equals(key)) {
            getData((String) value);
        } else if (Constants.GETGOODSID.equals(key)) {
            return getGoods_id();
        } else if (Constants.CARDNUMBER.equals(key)) {
            mPresenter.getCardNumber(App.getApp().getKey());
        } else if (Constants.SHOWORDER.equals(key)) {
            showOrder((Integer) value);
        } else if (Constants.GETGOODSDETAILS.equals(key)) {
            return getGoodsdata();
        }
        return null;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;

        goodsDetailFragment.getData(goods_id);
    }

    public interface OnChangeGoodsInfoListener {
        void updataUI(String type, String data);
    }

    public OnChangeGoodsInfoListener onChangeGoodsInfoListener;

    public void setOnChangeGoodsInfoListener(OnChangeGoodsInfoListener listener) {
        this.onChangeGoodsInfoListener = listener;
    }


    @Override
    public void getCardNumber(String number) {
        if (Utils.isEmpty(App.getApp().getKey())) {
            goodsdetailTextCartnumber.setVisibility(View.VISIBLE);
            goodsdetailTextCartnumber.setText(number);
        }
    }

    boolean isOpenPopwindown;

    public void showOrder(int type) {
        isOpenPopwindown = true;
        orderView = LayoutInflater.from(mContext).inflate(R.layout.goods_view_order, null);
        popuWindow = new CustomPopuWindow.PopupWindowBuilder(mContext)
                .setOnDissmissListener(() -> isOpenPopwindown = false)
                .setView(orderView).setFocusable(true).setOnDissmissListener(() -> backgroundAlpha(1)).create()
                .showAtLocation(orderView, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.2f);
        getView(orderView, R.id.btn).setOnClickListener(v -> {

            if (type == 1) {/**提交订单*/
                if (Utils.isLogin(mContext)) {
                    if (goods_storage == 0) {
                        showToast("没有库存啦！");
                        return;
                    }
                    if (is_virtual.equals("0")) {
                        cart_id = goods_id + "|" + goods_number;
                        mPresenter.buyStep1(this, App.getApp().getKey(), cart_id);
                    } else {
                        mPresenter.buyStep1V(this, App.getApp().getKey(), goods_id, "" + goods_number);
                    }
                }
            } else if (type == 2) {/**加入购物车*/
                if (Utils.isLogin(mContext)) {
                    mPresenter.addCart(App.getApp().getKey(), goods_id, goods_number + "");
                }
            }
            if (popuWindow != null) {
                popuWindow.dissmiss();
            }
        });
        setSpecInfo(goodsdata);
    }

    String[] spectype;/*储存选择规格id*/
    int goods_number = 1; /*商品购买数量*/
    int goods_storage = 0; /*库存*/

    private void updateValue() {/**更新数量*/
        String adderValue = "";
        if (goods_storage == 0) {
            showToast("没有库存啦！");
        } else if (goods_storage > 0 && goods_number > goods_storage) {
            adderValue = "" + goods_storage;
            goods_number = goods_storage;
            showToast("我们小仓库就这么多啦！");
        } else if (goods_number <= 0) {
            goods_number = 1;
        } else {
            adderValue = String.valueOf(goods_number);
        }
        number.setText(goods_number + "");
        if (onChangeGoodsInfoListener != null) {
            onChangeGoodsInfoListener.updataUI("updata_goods_number", adderValue);
        }

    }

    @Override
    public void showContent() {
        super.showContent();
        showToast("添加成功");
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    public List<SpecBean> getSpecList(String data) {
        List<SpecBean> list = new ArrayList<>();
        if (Utils.isEmpty(data)) {
            try {
                JSONObject jsonGoods_spec = new JSONObject(data);
                Iterator<?> itName = jsonGoods_spec.keys();
                while (itName.hasNext()) {
                    String specID = itName.next().toString();
                    String specV = jsonGoods_spec.getString(specID);
                    list.add(new SpecBean(specID, specV));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return list;

    }

    private void setSpecInfo(String goodsdata) {
        GoodsDetailedInfo info = JSONParser.JSON2Object(goodsdata, GoodsDetailedInfo.class);
        if (info != null) {
            String goods_info = JSONParser.getStringFromJsonString("goods_info", goodsdata);
            String spec_value = JSONParser.getStringFromJsonString("spec_value", goods_info);
            String spec_name = JSONParser.getStringFromJsonString("spec_name", goods_info);
            String goods_spec = JSONParser.getStringFromJsonString("goods_spec", goods_info);
            String spec_image = JSONParser.getStringFromJsonString("spec_image", goodsdata);
            String spec_list = JSONParser.getStringFromJsonString("spec_list", goodsdata);
            List<SpecBean> specnamelist = getSpecList(spec_name);
            List<SpecBean> specvaluelist = getSpecList(spec_value);
            List<SpecBean> specimgList = getSpecList(spec_image);
            List<SpecBean> specList = getSpecList(spec_list);
            List<SpecBean> goodsspecList = getSpecList(goods_spec);
            ((TextView) getView(orderView, R.id.goods_storage)).setText("库存：" + Utils.getString(info.goods_info.goods_storage + " 件"));
            ((TextView) getView(orderView, R.id.goods_name)).setText(Utils.getString(info.goods_info.goods_name));
            ((TextView) getView(orderView, R.id.goods_price)).setText(Utils.getString(info.goods_info.goods_price));
            number = getView(orderView, R.id.goods_number);
            try { /*库存*/
                goods_storage = Integer.parseInt(Utils.getString(info.goods_info.goods_storage));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                goods_storage = 0;
            }
            number.setText("" + goods_number);
            /**数据监听*/
            number.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().trim().equals("")) {
                        goods_number = 1;
                    } else {
                        goods_number = Integer.valueOf(s.toString());
                    }
                    if (onChangeGoodsInfoListener != null) {
                        onChangeGoodsInfoListener.updataUI("updata_goods_number", String.valueOf(goods_number));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().isEmpty()) {
                        goods_number = 1;
                    }
                    if (goods_storage > 0 && goods_number > goods_storage) {
                        s.replace(0, number.getText().toString().length(), "" + goods_storage);
                        goods_number = goods_storage;
                        showToast("我们小仓库就这么多啦！");
                    } else if (goods_storage == 0) {
                        showToast("没有库存啦！");
                    }

                    if (onChangeGoodsInfoListener != null) {
                        onChangeGoodsInfoListener.updataUI("updata_goods_number", String.valueOf(goods_number));
                    }


                }
            });
            getView(orderView, R.id.goods_number_up).setOnClickListener(v -> {
                if (goods_storage > 0) {
                    goods_number++;
                }
                updateValue();
            });
            getView(orderView, R.id.goods_number_down).setOnClickListener(v -> {
                if (goods_storage > 0) {
                    if (goods_number > 1) {
                        goods_number--;
                    } else {
                        showToast("亲，至少1件商品哟");
                        goods_number = 1;
                    }
                }
                updateValue();

            });
            LinearLayout layout = getView(orderView, R.id.goods_spec);/**第一层*/

            spectype = new String[specnamelist.size()];
            if (Utils.isEmpty(specnamelist)) {
                layout.removeAllViews();
                for (int i = 0; i < specnamelist.size(); i++) {
                    View specView = getView(R.layout.goods_spec_list_view);/**子类第一层 */
                    TextView specName = getView(specView, R.id.specName);
                    specName.setHint(specnamelist.get(i).key);
                    specName.setText(specnamelist.get(i).value);/**设置规格*/
                    layout.addView(specView);/***/
                    TagContainerLayout speclistLayout = getView(specView, R.id.specList);/**子类第二层*/
                    if (Utils.isEmpty(specvaluelist)) {
                        if (specvaluelist.get(i).key.equals(specnamelist.get(i).key)) {
                            List<SpecBean> list = getSpecList(specvaluelist.get(i).value);
                            if (Utils.isEmpty(list)) {
                                speclistLayout.removeAllViews();
                                for (int j = 0; j < list.size(); j++) {

                                    View view = getView(R.layout.spec_item);
                                    TextView textView = getView(view, R.id.text);
                                    textView.setHint(list.get(j).key);
                                    textView.setText(list.get(j).value);/**设置规格名称*/
                                    if (Utils.isEmpty(goodsspecList)) {
                                        for (int k = 0; k < goodsspecList.size(); k++) {
                                            if (Utils.isEmpty(specimgList)) {/**加載圖片*/
                                                for (int l = 0; l < specimgList.size(); l++) {
                                                    if (specimgList.get(l).key.equals(goodsspecList.get(k).key)) {
                                                        GlideEngine.getInstance().loadImage(mContext, getView(orderView, R.id.goods_image), specimgList.get(l).value);
                                                    }
                                                }
                                            }
                                            if (goodsspecList.get(k).value.equals(list.get(j).value)) {/**设置默认选择*/
                                                textView.setActivated(true);
                                                if (Utils.isEmpty(textView.getHint().toString().trim())) {
                                                    spectype[i] = Utils.getString(textView.getHint().toString().trim());
                                                }
                                            }
                                        }
                                    }

                                    if (list.size() > 1) {/**一种规格以上允许点击*/
                                        int finalI1 = i;
                                        textView.setOnClickListener(v -> {
                                            if (Utils.isEmpty(textView.getHint().toString().trim())) {
                                                spectype[finalI1] = Utils.getString(textView.getHint().toString().trim());
                                            }
                                            Logutils.i(Arrays.toString(spectype));

                                            if (Utils.isEmpty(specList)) {
                                                for (int m = 0; m < specList.size(); m++) {
                                                    String[] spec = specList.get(m).key.split("\\|");
                                                    if (Utils.isEmpty(spec)) {
                                                        Arrays.sort(spectype);
                                                        Arrays.sort(spec);
                                                        if (Arrays.equals(spectype, spec)) {
                                                            goods_id = specList.get(m).value;
                                                            getData(goods_id);
                                                            return;
                                                        }
                                                    }
                                                }
                                            }
                                        });
                                    }

                                    speclistLayout.addView(view);
                                }
                            }
                        }

                    }
                }
            } else {/**没有规格选择默认选项*/
                View view = getView(R.layout.spec_item);
                TextView textView = getView(view, R.id.text);
                textView.setText("默认");
                textView.setActivated(true);
                layout.addView(view);
                GlideEngine.getInstance().loadImage(mContext, getView(orderView, R.id.goods_image), JSONParser.JSON2Array(spec_image, String.class).get(0));
            }
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        Window window = ((GoodsDetailsActivity) mContext).getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        window.setAttributes(lp);
    }


    @Override
    public void getGoodsDetails(String data) {
        try {
            if (onChangeGoodsInfoListener != null) {
                onChangeGoodsInfoListener.updataUI("updata_goods_id", getGoods_id());
            }
            RxBus.getDefault().post(new GoodsDetailsEvent(data));
            setGoodsdata(data);
            if (isOpenPopwindown) {
                setSpecInfo(data);
            }
            GoodsDetailedInfo info = JSONParser.JSON2Object(data, GoodsDetailedInfo.class);
            is_virtual = info.goods_info.is_virtual;
            if (Utils.getString(info.goods_info.cart).equals("0")) {/**是否显示加入购物车*/
                goodsdetailTextAddcart.setVisibility(View.GONE);
            } else {
                goodsdetailTextAddcart.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().unRegister(this);
    }

    @Override
    public void buyStep1Data(String data) {
        goOrder(data);
    }

    @Override
    public void buyStep1VData(String data) {
        goOrder(data);
    }


    private void goOrder(String data) {
        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        bundle.putInt(Constants.IFCART, 0);
        bundle.putString(Constants.IS_VIRTUAL, is_virtual);
        bundle.putString(Constants.CART_ID, is_virtual.equals("1") ? goods_id : goods_id + "|" + goods_number);
        bundle.putString(Constants.GOODS_NUMBER, goods_number + "");
        readyGo(GoodsOrderActivity.class, bundle);
    }


    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int arg0) {

            return fragmentList.get(arg0);
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();//hotIssuesList.size();
        }
    }

    @Override
    public void faild(String msg) {
        showToast(msg);
    }
}
