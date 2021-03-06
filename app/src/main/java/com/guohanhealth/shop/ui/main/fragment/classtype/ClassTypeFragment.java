package com.guohanhealth.shop.ui.main.fragment.classtype;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.guohanhealth.shop.R;
import com.guohanhealth.shop.adapter.BaseRecyclerAdapter;
import com.guohanhealth.shop.adapter.BaseViewHolder;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseFragment;
import com.guohanhealth.shop.bean.BrandListInfo;
import com.guohanhealth.shop.bean.GoodsClassChildInfo;
import com.guohanhealth.shop.bean.GoodsClassInfo;
import com.guohanhealth.shop.event.OnFragmentInteractionListener;
import com.guohanhealth.shop.event.PermissionListener;
import com.guohanhealth.shop.ui.goods.goodsdetailed.activity.GoodsDetailsActivity;
import com.guohanhealth.shop.ui.goods.goodslist.GoodsListActivity;
import com.guohanhealth.shop.ui.search.SearchActivity;
import com.guohanhealth.shop.utils.Utils;
import com.guohanhealth.shop.utils.engine.GlideEngine;
import com.guohanhealth.shop.view.scannercode.android.CaptureActivity;
import com.scwang.smartrefresh.header.DeliveryHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;


public class ClassTypeFragment extends BaseFragment<ClassTypePersenter, ClassTypeModel> implements ClassTypeView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.classtype_scanner)
    ImageView classtypeScanner;
    @BindView(R.id.classtype_view_search)
    LinearLayout classtypeViewSearch;
    @BindView(R.id.classtype_im)
    ImageView classtypeIm;
    @BindView(R.id.classtype_recycle_one)
    RecyclerView classtypeRecycleOne;
    @BindView(R.id.classtype_recycle_two)
    RecyclerView classtypeRecycleTwo;
    @BindView(R.id.classtype_view_refresh)
    SmartRefreshLayout classtypeViewRefresh;
    @BindView(R.id.hottext)
    TextView hottext;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private BaseRecyclerAdapter<BrandListInfo.Brand_list, RecyclerView.ViewHolder> brandAdapter;
    private BaseRecyclerAdapter<GoodsClassChildInfo.Class_list, RecyclerView.ViewHolder> goodclasschildAdapter;
    private List<GoodsClassInfo.Class_list> goodsClassInfos;
    private List<BrandListInfo.Brand_list> brandListInfos;
    private List<GoodsClassChildInfo.Class_list> goodsClassChildInfos;
    private ClassAdapter classAdapter;
    private ClassChildListAdapter classChildListAdapter;
    private ClassChildAdapter classChildAdapter;
    private BaseRecyclerAdapter<GoodsClassInfo.Class_list, RecyclerView.ViewHolder> goodsClassAdapter;

    public ClassTypeFragment() {
    }

    public static ClassTypeFragment newInstance(String param1, String param2) {
        ClassTypeFragment fragment = new ClassTypeFragment();
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
        return R.layout.fragment_class_type;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //设置 Header 为 Material风格
        classtypeViewRefresh.setRefreshHeader(new DeliveryHeader(mContext));
        classtypeViewRefresh.setEnableLoadMore(false);
        classtypeViewRefresh.setOnRefreshListener(refreshLayout -> {
            mPresenter.getBrandList();
            mPresenter.getGoodsClass();
        });
        classtypeScanner.setOnClickListener(v -> {
            requestRuntimePermission(new String[]{Manifest.permission.CAMERA},
                    new PermissionListener() {
                        @Override
                        public void onGranted() {
                            readyGoForResult(CaptureActivity.class, Constants.REQUEST_CAMERA);
                        }

                        @Override
                        public void onDenied(List<String> deniedPermissions) {
                            showToast("拒绝相机权限");
                        }
                    });
        });
        goodsClassInfos = new ArrayList<>();
        brandListInfos = new ArrayList<>();
        goodsClassChildInfos = new ArrayList<>();
        brandAdapter = new BaseRecyclerAdapter<BrandListInfo.Brand_list, RecyclerView.ViewHolder>(mContext, R.layout.classtype_item, brandListInfos) {
            @Override
            public void convert(BaseViewHolder holder, BrandListInfo.Brand_list brand_list) {
                holder.setText(R.id.text, brand_list.brand_name);
                GlideEngine.getInstance().loadImage(mContext, (ImageView) holder.getView(R.id.img), brand_list.brand_pic);
                holder.getView(R.id.line).setVisibility(View.GONE);
                holder.itemView.setOnClickListener(v -> {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.KEYWORD, brand_list.brand_id);
                    readyGo(GoodsListActivity.class, bundle);
                });
            }
        };
        classAdapter = new ClassAdapter(R.layout.classtype_item, goodsClassInfos);
        classtypeRecycleOne.setLayoutManager(new LinearLayoutManager(mContext));
        classtypeRecycleOne.setAdapter(classAdapter);
        classtypeRecycleTwo.setLayoutManager(new GridLayoutManager(mContext, 3));
        classtypeRecycleTwo.setAdapter(brandAdapter);
        classChildListAdapter = new ClassChildListAdapter(R.layout.classtype_item_child, goodsClassChildInfos);
        classAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        classChildListAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        //设置重复执行动画
        classAdapter.isFirstOnly(false);

        goodclasschildAdapter = new BaseRecyclerAdapter<GoodsClassChildInfo.Class_list, RecyclerView.ViewHolder>(mContext, android.R.layout.simple_list_item_1, goodsClassChildInfos) {

            //            @Override
            public void convert(BaseViewHolder holder, GoodsClassChildInfo.Class_list s) {
                holder.setText(android.R.id.text1, s.gc_name);
            }
        };
        classAdapter.setOnItemClickListener((adapter, view, position) -> {

            if (goodsClassInfos.get(position).gc_id.equals("0")) {
                mPresenter.getBrandList();
            } else {
                mPresenter.getGoodsChild(goodsClassInfos.get(position).gc_id);
            }
            for (Map.Entry<Integer, com.chad.library.adapter.base.BaseViewHolder> m : map.entrySet()) {
                com.chad.library.adapter.base.BaseViewHolder holder = m.getValue();
                TextView textView = holder.getView(R.id.text);
                ImageView imageView = holder.getView(R.id.img);
                if (m.getKey() == position) {
                    imageView.setColorFilter(mContext.getResources().getColor(R.color.blue_btn));
                    textView.setTextColor(mContext.getResources().getColor(R.color.blue_dark_btn));
                } else {
                    textView.setTextColor(mContext.getResources().getColor(R.color.nc_text));
                    imageView.setColorFilter(mContext.getResources().getColor(R.color.appColor));
                }
            }
        });
        classtypeViewSearch.setOnClickListener(v -> {
            readyGo(SearchActivity.class);
        });
        initData();
    }


    private void initData() {
        mPresenter.getGoodsClass();
        mPresenter.getBrandList();
        hottext.setText(App.getApp().getHotname());
    }

    Map<Integer, com.chad.library.adapter.base.BaseViewHolder> map = new HashMap<>();

    class ClassAdapter extends BaseQuickAdapter<GoodsClassInfo.Class_list, com.chad.library.adapter.base.BaseViewHolder> {

        public ClassAdapter(int layoutResId, @Nullable List<GoodsClassInfo.Class_list> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(com.chad.library.adapter.base.BaseViewHolder helper, GoodsClassInfo.Class_list item) {

            helper.setText(R.id.text, item.gc_name);
            GlideEngine.getInstance().loadImage(mContext, (ImageView) helper.getView(R.id.img), item.image);
            map.put(helper.getPosition(), helper);
        }
    }

    class ClassChildListAdapter extends BaseQuickAdapter<GoodsClassChildInfo.Class_list, com.chad.library.adapter.base.BaseViewHolder> {
        public ClassChildListAdapter(int layoutResId, @Nullable List<GoodsClassChildInfo.Class_list> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(com.chad.library.adapter.base.BaseViewHolder helper, GoodsClassChildInfo.Class_list item) {
            TextView textView = helper.getView(R.id.text);
            textView.setText(item.gc_name);
            textView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.GC_ID, item.gc_id);
                readyGo(GoodsListActivity.class, bundle);
            });

            helper.getView(R.id.point).setBackgroundColor(Constants.BGCOLORS[new Random().nextInt(10)]);
            RecyclerView recyclerView = helper.getView(R.id.recycleview);
            classChildAdapter = new ClassChildAdapter(R.layout.text_item, item.child);
            classChildAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
            recyclerView.setAdapter(classChildAdapter);
            classChildAdapter.setOnItemClickListener((a, v, p) -> {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.GC_ID, item.child.get(p).gc_id);
                readyGo(GoodsListActivity.class, bundle);
            });
        }
    }

    class ClassChildAdapter extends BaseQuickAdapter<GoodsClassChildInfo.Class_list.Child, com.chad.library.adapter.base.BaseViewHolder> {

        public ClassChildAdapter(int layoutResId, @Nullable List<GoodsClassChildInfo.Class_list.Child> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(com.chad.library.adapter.base.BaseViewHolder helper, GoodsClassChildInfo.Class_list.Child item) {
            helper.setText(R.id.text, item.gc_name);
        }
    }


    public Object onButtonPressed(String key, Object value) {
        if (mListener != null) {
          return   mListener.doSomeThing(key, value);
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

    View selectView;

    @Override
    public void getGoodsClass(List<GoodsClassInfo.Class_list> info) {
        if (classtypeViewRefresh != null)
            classtypeViewRefresh.finishRefresh();
        goodsClassInfos.clear();
        goodsClassInfos.addAll(info);
        GoodsClassInfo.Class_list list = new GoodsClassInfo().new Class_list();
        list.gc_id = "0";
        list.gc_name = "品牌推荐";
        list.image = Constants.WAP_BRAND_ICON;
        goodsClassInfos.add(0, list);
        classAdapter.notifyDataSetChanged();
    }

    @Override
    public void getBrandList(List<BrandListInfo.Brand_list> info) {
        if (classtypeViewRefresh != null)
            classtypeViewRefresh.finishRefresh();
        brandListInfos.clear();
        brandListInfos.addAll(info);
        classtypeRecycleTwo.setLayoutManager(new GridLayoutManager(mContext, 3));
        classtypeRecycleTwo.setAdapter(brandAdapter);
        brandAdapter.notifyDataSetChanged();
    }

    @Override
    public void getGoodsChild(List<GoodsClassChildInfo.Class_list> info) {
        goodsClassChildInfos.clear();
        goodsClassChildInfos.addAll(info);
        classtypeRecycleTwo.setAdapter(classChildListAdapter);
        classtypeRecycleTwo.setLayoutManager(new LinearLayoutManager(mContext));
        classChildListAdapter.notifyDataSetChanged();
    }

    @Override
    public void faild(String msg) {
        if (!msg.equals("请登陆")) {
            showToast(msg);
        }
        if (classtypeViewRefresh != null)
            classtypeViewRefresh.finishRefresh();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.REQUEST_CAMERA:
                    showToast(data.getStringExtra("codedContent"));
                    break;
            }
        }
    }
}
