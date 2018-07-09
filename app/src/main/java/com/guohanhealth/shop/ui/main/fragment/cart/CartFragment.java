package com.guohanhealth.shop.ui.main.fragment.cart;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseFragment;
import com.guohanhealth.shop.bean.CartInfo;
import com.guohanhealth.shop.bean.CartNumberInfo;
import com.guohanhealth.shop.custom.EmptyView;
import com.guohanhealth.shop.event.OnFragmentInteractionListener;
import com.guohanhealth.shop.event.RxBus;
import com.guohanhealth.shop.ui.login.LoginActivity;
import com.guohanhealth.shop.utils.Utils;
import com.guohanhealth.shop.utils.engine.GlideEngine;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CartFragment extends BaseFragment<CartPersenter, CartModel> implements CartView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.cart_view_empty)
    EmptyView cartViewEmpty;
    @BindView(R.id.cart_view_recycle)
    RecyclerView cartViewRecycle;
    @BindView(R.id.cart_view_refresh)
    SmartRefreshLayout cartViewRefresh;
    @BindView(R.id.cart_check_all)
    CheckBox cartCheckAll;
    @BindView(R.id.cart_text_money)
    TextView cartTextMoney;
    @BindView(R.id.cart_text_settlement)
    TextView cartTextSettlement;
    Unbinder unbinder;
    @BindView(R.id.cart_view_settlement)
    LinearLayout cartViewSettlement;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private CartAdapter adapter;
    private CartChildAdapter childAdapter;

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

    List<CartInfo.CartListBean> list;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_cart;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        adapter = new CartAdapter(R.layout.cart_list_item, list);
        cartViewRecycle.setLayoutManager(new LinearLayoutManager(mContext));
        cartViewRecycle.setAdapter(adapter);
        cartViewRefresh.setOnRefreshListener(refreshLayout -> {
            mPresenter.getCartList(App.getApp().getKey());
        });
    }

    class CartAdapter extends BaseQuickAdapter<CartInfo.CartListBean, BaseViewHolder> {
        public CartAdapter(int layoutResId, @Nullable List<CartInfo.CartListBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, CartInfo.CartListBean item) {
            helper.setText(R.id.text, item.store_name);
            RecyclerView recyclerView = helper.getView(R.id.recycleview);
            childAdapter = new CartChildAdapter(R.layout.cart_list_child_item, item.goods);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.setAdapter(childAdapter);
        }
    }

    class CartChildAdapter extends BaseQuickAdapter<CartInfo.CartListBean.GoodsBean, BaseViewHolder> {

        public CartChildAdapter(int layoutResId, @Nullable List<CartInfo.CartListBean.GoodsBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, CartInfo.CartListBean.GoodsBean item) {
            helper.setText(R.id.text, item.goods_name);
            helper.setText(R.id.text1, item.goods_price);
            helper.setText(R.id.cart_number, item.goods_num);
            GlideEngine.getInstance().loadImage(mContext,helper.getView(R.id.img),item.goods_image_url);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Utils.isEmpty(App.getApp().getKey()) && App.getApp().isLogin()) {
            mPresenter.getCartList(App.getApp().getKey());
        } else {
            cartViewSettlement.setVisibility(View.GONE);
            cartViewEmpty.setVisibility(View.VISIBLE);
            cartViewEmpty.setEmptyText("您还没有登陆呢");
            cartViewEmpty.setEmptyText1("点击登陆！");
            getView(cartViewEmpty, R.id.text1).setOnClickListener(v -> {
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
        if (Utils.isEmpty(info.cart_list)) {
            cartViewEmpty.setVisibility(View.GONE);
            cartViewSettlement.setVisibility(View.VISIBLE);
            cartViewRecycle.setVisibility(View.VISIBLE);
            list.clear();
            list.addAll(info.cart_list);
            cartTextMoney.setText(Utils.getString(info.sum));
            adapter.notifyDataSetChanged();
            RxBus.getDefault().post(new CartNumberInfo());
        } else {
            cartViewSettlement.setVisibility(View.GONE);
            cartViewRecycle.setVisibility(View.GONE);
            cartViewEmpty.setVisibility(View.VISIBLE);
            cartViewEmpty.setEmptyText("您的购物还是空的");
            cartViewEmpty.setEmptyText1("去挑选一些喜欢的商品吧！");
            getView(cartViewEmpty, R.id.view).setOnClickListener(v -> {
                onButtonPressed(Constants.MAINSELECTNUMBER, 0);
            });

        }
    }
}
