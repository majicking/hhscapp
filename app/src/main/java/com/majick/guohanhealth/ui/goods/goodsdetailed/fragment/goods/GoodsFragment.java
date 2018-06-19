package com.majick.guohanhealth.ui.goods.goodsdetailed.fragment.goods;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.majick.guohanhealth.R;
import com.majick.guohanhealth.app.App;
import com.majick.guohanhealth.base.BaseFragment;
import com.majick.guohanhealth.bean.Adv_list;
import com.majick.guohanhealth.bean.GoodsDetailedInfo;
import com.majick.guohanhealth.event.OnFragmentInteractionListener;
import com.majick.guohanhealth.utils.Utils;
import com.majick.guohanhealth.utils.engine.GlideEngine;
import com.majick.guohanhealth.view.NoScrollGridView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class GoodsFragment extends BaseFragment<GoodsPersenter, GoodsModel> implements GoodsView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.goods_banner)
    Banner goodsBanner;
    @BindView(R.id.goods_collect)
    ImageView goodsCollect;
    @BindView(R.id.goods_share)
    ImageView goodsShare;
    @BindView(R.id.goods_name1)
    TextView goodsName1;
    @BindView(R.id.goods_name2)
    TextView goodsName2;
    @BindView(R.id.goods_price)
    TextView goodsPrice;
    @BindView(R.id.goods_solenum)
    TextView goodsSolenum;
    @BindView(R.id.goods_loca)
    TextView goodsLoca;
    @BindView(R.id.goods_havegoods)
    TextView goodsHavegoods;
    @BindView(R.id.goods_runmoney)
    TextView goodsRunmoney;
    @BindView(R.id.goods_view_loca)
    LinearLayout goodsViewLoca;
    @BindView(R.id.goods_view_gridview)
    NoScrollGridView goodsViewGridview;
    @BindView(R.id.goods_view_gridview1)
    NoScrollGridView goodsViewGridview1;
    Unbinder unbinder;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public GoodsFragment() {
    }

    public static GoodsFragment newInstance(String param1, String param2) {
        GoodsFragment fragment = new GoodsFragment();
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


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_goods;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        goodsBanner.isAutoPlay(true);
        goodsBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        goodsBanner.setDelayTime(2000);
        goodsBanner.setIndicatorGravity(BannerConfig.CENTER);
        mPresenter.getGoodsDetails(mParam1, App.getApp().getKey());
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

    }

    @Override
    public void getGoodsDetails(GoodsDetailedInfo info) {
        if (info != null) {
            goodsName1.setText(Utils.getString(info.goods_info.goods_name));
            goodsName2.setText(Utils.getString(info.goods_info.goods_jingle));
            goodsBanner.setImages(getImageList(info.goods_image));
            goodsBanner.setImageLoader(new ImageLoader() {
                @Override
                public void displayImage(Context context, Object path, ImageView imageView) {
                    GlideEngine.getInstance().loadImage(context, imageView, (String) path);
                }
            });
            goodsBanner.start();
            goodsPrice.setText("￥ " + Utils.getString(info.goods_info.goods_price));
            goodsSolenum.setText("销量：" + Utils.getString(info.goods_info.goods_salenum));
            goodsLoca.setText(Utils.getString(info.goods_hair_info.area_name));
            goodsHavegoods.setText(Utils.getString(info.goods_hair_info.if_store_cn));
            goodsRunmoney.setText(Utils.getString(info.goods_hair_info.content));
        }
    }

    private List<String> getImageList(String info) {
        List<String> imglist = new ArrayList<>();
        if (Utils.isEmpty(info)) {
            String[] strings = info.split(",");
            if (strings != null && strings.length > 0) {
                for (int i = 0; i < strings.length; i++) {
                    imglist.clear();
                    imglist.add(strings[i]);
                }
            }
        }
        return imglist;
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
}
