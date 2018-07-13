package com.guohanhealth.shop.ui.main.fragment.cart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseFragment;
import com.guohanhealth.shop.bean.CartInfo;
import com.guohanhealth.shop.bean.CartNumberInfo;
import com.guohanhealth.shop.custom.CustomDialog;
import com.guohanhealth.shop.custom.EmptyView;
import com.guohanhealth.shop.event.OnFragmentInteractionListener;
import com.guohanhealth.shop.event.RxBus;
import com.guohanhealth.shop.ui.goods.goodsdetailed.activity.GoodsDetailsActivity;
import com.guohanhealth.shop.ui.goods.goodsorder.GoodsOrderActivity;
import com.guohanhealth.shop.ui.login.LoginActivity;
import com.guohanhealth.shop.utils.Utils;
import com.guohanhealth.shop.utils.engine.GlideEngine;
import com.scwang.smartrefresh.header.DeliveryHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CartFragment extends BaseFragment<CartPersenter, CartModel> implements CartView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.cart_view_empty)
    EmptyView cartViewEmpty;
    @BindView(R.id.cart_view_refresh)
    SmartRefreshLayout cartViewRefresh;
    @BindView(R.id.cart_check_all)
    ImageButton cartCheckAll;
    @BindView(R.id.cart_text_money)
    TextView cartTextMoney;
    @BindView(R.id.cart_text_settlement)
    TextView cartTextSettlement;
    Unbinder unbinder;
    @BindView(R.id.cart_view_settlement)
    LinearLayout cartViewSettlement;
    @BindView(R.id.linearlayout)
    LinearLayout linearlayout;
    @BindView(R.id.cart_view_nestedscrollview)
    NestedScrollView cartViewNestedscrollview;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private List<CartInfo.CartListBean> list;


    public CartFragment() {
        list = new ArrayList<>();
    }

    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private String cartIdString = "";

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_cart;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        cartViewRefresh.setRefreshHeader(new DeliveryHeader(mContext));
        cartViewRefresh.setOnRefreshListener(refreshLayout -> {
            mPresenter.getCartList(App.getApp().getKey());
        });
        cartCheckAll.setSelected(true);
        cartCheckAll.setOnClickListener((v) -> {
            for (Map.Entry e : allGoodsMap.entrySet()) {
                ((GoodsItem) e.getValue()).imageButton.setSelected(!cartCheckAll.isSelected());
            }
            for (int i = 0; i < storeCheckList.size(); i++) {
                ((ImageButton) storeCheckList.get(i)).setSelected(!cartCheckAll.isSelected());
            }
            cartCheckAll.setSelected(!cartCheckAll.isSelected());
            updataPrice();
        });
        cartViewNestedscrollview.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            int index = mContext.getResources().getDimensionPixelSize(R.dimen.dp_50);
            if (scrollY > index) {
                cartViewSettlement.setVisibility(View.GONE);
            } else {
                cartViewSettlement.setVisibility(View.VISIBLE);
            }
        });

        cartTextSettlement.setOnClickListener(v -> {
            cartIdString = "";
            for (Map.Entry entry : allGoodsMap.entrySet()) {
                String cartId = (String) entry.getKey();
                GoodsItem item = (GoodsItem) entry.getValue();

                if (item.imageButton.isSelected()) {
                    allGoodsMap.remove(item);
                    cartIdString += cartId + "|" + item.number + ",";
                }
            }

            if (cartIdString.equals("")) {
                showToast("请选择想要购买的商品");
            } else {
                mPresenter.buyStep1(getActivity(), App.getApp().getKey(), cartIdString.substring(0, cartIdString.length() - 1), "1");
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        if (Utils.isEmpty(App.getApp().getKey()) && App.getApp().isLogin()) {
            mPresenter.getCartList(App.getApp().getKey());
        } else {
            linearlayout.removeAllViews();
            cartViewSettlement.setVisibility(View.GONE);
            cartViewEmpty.setVisibility(View.VISIBLE);
            cartViewEmpty.setEmpty("您还没有登陆呢","点击登陆！");
            getView(cartViewEmpty, R.id.view).setOnClickListener(v -> {
                readyGo(LoginActivity.class);
            });
        }
    }

    public void onButtonPressed(String key, Object value) {
        if (mListener != null) {
            mListener.doSomeThing(key, value);
        }
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
    public void faild(String msg) {
        if (cartViewRefresh != null) {
            cartViewRefresh.finishRefresh();
        }
        showToast(msg);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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


    @Override
    public void getData(CartInfo info) {
        if (cartViewRefresh != null) {
            cartViewRefresh.finishRefresh();
        }
        linearlayout.removeAllViews();
        if (Utils.isEmpty(info.cart_list)) {
            linearlayout.setVisibility(View.VISIBLE);
            cartViewEmpty.setVisibility(View.GONE);
            cartViewSettlement.setVisibility(View.VISIBLE);
            list.clear();
            list.addAll(info.cart_list);
            cartTextMoney.setText(Utils.getString(info.sum));
            setListData();
            RxBus.getDefault().post(new CartNumberInfo());
        } else {
            cartViewSettlement.setVisibility(View.GONE);
            cartViewEmpty.setVisibility(View.VISIBLE);
            cartViewEmpty.setEmptyText("您的购物还是空的");
            cartViewEmpty.setEmptyText1("去挑选一些喜欢的商品吧！");
            getView(cartViewEmpty, R.id.view).setOnClickListener(v -> {
                onButtonPressed(Constants.MAINNUMBER, 0);
            });

        }
    }

    @Override
    public void buyStep1Data(String data) {
        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        bundle.putInt(Constants.IFCART, 1);
        bundle.putString(Constants.IS_VIRTUAL, "0");
        bundle.putString(Constants.CART_ID, cartIdString.substring(0, cartIdString.length() - 1));
        readyGo(GoodsOrderActivity.class, bundle);
    }


    Map<String, GoodsItem> allGoodsMap;
    List<ImageButton> storeCheckList;

    private void setListData() {
        allGoodsMap = new HashMap<>();
        storeCheckList = new ArrayList<>();
        cartCheckAll.setSelected(true);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mContext.getResources().getDimensionPixelSize(R.dimen.dp_80));
        if (Utils.isEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                View view = getView(R.layout.cart_list_item);
                ((TextView) getView(view, R.id.text)).setText(list.get(i).store_name);
                LinearLayout layout = getView(view, R.id.linearlayout);
                layout.removeAllViews();
                ImageButton checkBox = getView(view, R.id.check);
                List<CartInfo.CartListBean.GoodsBean> goodsBeanList = list.get(i).goods;
                final List<String> childGoodsList = new ArrayList<>();
                for (int j = 0; j < goodsBeanList.size(); j++) {
                    int index = j;
                    View childView = getView(R.layout.cart_list_child_item);
                    childView.setLayoutParams(layoutParams);
                    GlideEngine.getInstance().loadImage(mContext, (ImageView) getView(childView, R.id.img), goodsBeanList.get(j).goods_image_url);
                    ((TextView) getView(childView, R.id.text)).setText(goodsBeanList.get(j).goods_name);
                    ((TextView) getView(childView, R.id.text1)).setText(goodsBeanList.get(j).goods_price);
                    //---------------删除商品-------------------------------------------------------------------------
                    ((ImageView) getView(childView, R.id.del)).setOnClickListener(v -> {
                        delCarts(goodsBeanList, index);

                    });
                    //----------------跳转详细------------------------------------------------------------------------
                    getView(childView, R.id.linearLayout).setOnClickListener(v -> {
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.GOODS_ID, goodsBeanList.get(index).goods_id);
                        readyGo(GoodsDetailsActivity.class, bundle);
                    });
                    //-----------------选择商品-----------------------------------------------------------------------
                    childGoodsList.add(goodsBeanList.get(j).cart_id);
                    ImageButton childcheckBox = getView(childView, R.id.check);
                    childcheckBox.setSelected(true);
                    childcheckBox.setOnClickListener(v -> {
                        GoodsItem item = allGoodsMap.get(goodsBeanList.get(index).cart_id);
                        item.imageButton.setSelected(!item.imageButton.isSelected());
                        //计算单个商铺是否全部勾选
                        int number = 0;
                        for (int k = 0; k < childGoodsList.size(); k++) {
                            if (allGoodsMap.get(childGoodsList.get(k)).imageButton.isSelected()) {
                                number++;
                            }
                        }
                        checkBox.setSelected(number == childGoodsList.size());
                        isAllCheck();
                        updataPrice();
                    });
                    //------------------数量加减----------------------------------------------------------------------
                    TextView add = getView(childView, R.id.cart_number_up);
                    TextView reduce = getView(childView, R.id.cart_number_down);
                    TextView number = getView(childView, R.id.cart_number);
                    number.setText(goodsBeanList.get(j).goods_num);

                    add.setOnClickListener(v -> {
                        int newNum = Integer.valueOf(number.getText().toString());
                        newNum += 1;
                        if (newNum > Integer.valueOf(goodsBeanList.get(index).goods_storage)) {
                            showToast("小库只有这么多啦！");
                            return;
                        }
                        mPresenter.upCartNumber(App.getApp().getKey(), goodsBeanList.get(index).cart_id, newNum + "");
                    });
                    reduce.setOnClickListener(v -> {
                        int newNum = Integer.valueOf(number.getText().toString());
                        newNum -= 1;
                        if (newNum <= 0) {
                            delCarts(goodsBeanList, index);
                            return;
                        }
                        mPresenter.upCartNumber(App.getApp().getKey(), goodsBeanList.get(index).cart_id, newNum + "");
                    });
                    //-----------------加入集合-----------------------------------------------------------------------
                    allGoodsMap.put(goodsBeanList.get(j).cart_id, new GoodsItem(goodsBeanList.get(j).goods_price, goodsBeanList.get(j).goods_num, goodsBeanList.get(j).goods_total, childcheckBox));
                    layout.addView(childView);
                }

                storeCheckList.add(checkBox);
                checkBox.setSelected(true);
                checkBox.setOnClickListener((v) -> {
                    checkBox.setSelected(!checkBox.isSelected());
                    for (String cartid : childGoodsList) {
                        allGoodsMap.get(cartid).imageButton.setSelected(checkBox.isSelected());
                    }
                    isAllCheck();
                    updataPrice();
                });


                linearlayout.addView(view);
            }
        }
        updataPrice();
    }

    private void delCarts(List<CartInfo.CartListBean.GoodsBean> goodsBeanList, int index) {
        new CustomDialog.Builder(mContext)
                .setTitle("温馨提示")
                .setMessage("确认删除该商品？")
                .setPositiveButton("删除", (dialog1, which) -> {
                    mPresenter.delCart(App.getApp().getKey(), goodsBeanList.get(index).cart_id);
                    dialog1.dismiss();
                })
                .setNegativeButton("取消", (dialog1, which) -> {
                    dialog1.dismiss();
                }).create().show();
    }


    /**
     * 全选按钮监听
     */
    private void isAllCheck() {
        int num = 0;
        for (int l = 0; l < storeCheckList.size(); l++) {
            if (storeCheckList.get(l).isSelected()) {
                num++;
            }
        }
        cartCheckAll.setSelected(num == storeCheckList.size());
    }

    /**
     * 计算购物车总价
     */
    public void updataPrice() {
        double total = 0.0;
        int num = 0;
        for (Map.Entry<String, GoodsItem> entry : allGoodsMap.entrySet()) {
            if (entry.getValue().imageButton.isSelected()) {
                total += Double.valueOf(entry.getValue().goodsTotal);
                num += Integer.valueOf(entry.getValue().number);
            }
        }
        cartTextSettlement.setText("去结算(" + num + ")");
        cartTextMoney.setText(total + "");
    }


    class GoodsItem {
        public String price;
        public String number;
        public String goodsTotal;
        public ImageButton imageButton;

        public GoodsItem(String price, String number, String goodsTotal, ImageButton imageButton) {
            this.price = price;
            this.number = number;
            this.goodsTotal = goodsTotal;
            this.imageButton = imageButton;
        }

    }
}
