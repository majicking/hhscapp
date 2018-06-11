package com.majick.guohanhealth.ui.main.fragment.classtype;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.majick.guohanhealth.R;
import com.majick.guohanhealth.adapter.BaseRecyclerAdapter;
import com.majick.guohanhealth.adapter.BaseViewHolder;
import com.majick.guohanhealth.app.Constants;
import com.majick.guohanhealth.base.BaseFragment;
import com.majick.guohanhealth.bean.BrandListInfo;
import com.majick.guohanhealth.bean.GoodsClassChildInfo;
import com.majick.guohanhealth.bean.GoodsClassInfo;
import com.majick.guohanhealth.ui.main.fragment.OnFragmentInteractionListener;
import com.majick.guohanhealth.utils.Utils;
import com.majick.guohanhealth.utils.engine.GlideEngine;
import com.majick.guohanhealth.view.scannercode.android.CaptureActivity;
import com.scwang.smartrefresh.header.DeliveryHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;
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

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private BaseRecyclerAdapter<BrandListInfo.Brand_list> brandAdapter;
    private BaseRecyclerAdapter<GoodsClassChildInfo.Class_list> goodclasschildAdapter;
    private List<GoodsClassInfo.Class_list> goodsClassInfos;
    private List<BrandListInfo.Brand_list> brandListInfos;
    private List<GoodsClassChildInfo.Class_list> goodsClassChildInfos;
    private ClassAdapter classAdapter;
    private ClassChildListAdapter classChildListAdapter;
    private ClassChildAdapter classChildAdapter;
    private BaseRecyclerAdapter<GoodsClassInfo.Class_list> goodsClassAdapter;

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
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CAMERA},
                            Constants.REQUEST_CAMERA);
                    return;
                }
            }
            startActivityForResult(new Intent(mContext, CaptureActivity.class), Constants.REQUEST_CAMERA);
        });
        goodsClassInfos = new ArrayList<>();
        brandListInfos = new ArrayList<>();
        goodsClassChildInfos = new ArrayList<>();
        brandAdapter = new BaseRecyclerAdapter<BrandListInfo.Brand_list>(mContext, R.layout.classtype_item, brandListInfos) {
            @Override
            public void convert(BaseViewHolder holder, BrandListInfo.Brand_list brand_list) {
                holder.setText(R.id.text, brand_list.brand_name);
                GlideEngine.getInstance().loadImage(mContext, (ImageView) holder.getView(R.id.img), brand_list.brand_pic);
                holder.getView(R.id.line).setVisibility(View.GONE);
                holder.itemView.setOnClickListener(v -> {
                    showToast(brand_list.brand_name);
                });
            }
        };
        goodsClassAdapter = new BaseRecyclerAdapter<GoodsClassInfo.Class_list>(mContext, R.layout.classtype_item, goodsClassInfos) {

            @Override
            public void convert(BaseViewHolder holder, GoodsClassInfo.Class_list class_list) {

                holder.setText(R.id.text, class_list.gc_name);
                GlideEngine.getInstance().loadImage(mContext, (ImageView) holder.getView(R.id.img), class_list.image);
                boolean isClick = false;
                if (class_list.gc_id.equals("0")) {
                    isClick = true;
                }
                if (isClick) {
                    selectView = holder.itemView;
                    setSelectView(holder.itemView);
                } else {
                    setResetView(holder.itemView);
                }

                holder.itemView.setOnClickListener(v -> {
                    setResetView(selectView);
                    setSelectView(v);
                    selectView = v;
                    if (class_list.gc_id.equals("0")) {
                        mPresenter.getBrandList();
                    } else {
                        mPresenter.getGoodsChild(class_list.gc_id);
                    }
                });


            }
        };
        classAdapter = new ClassAdapter(R.layout.classtype_item, goodsClassInfos);
        classChildListAdapter = new ClassChildListAdapter(R.layout.classtype_item_child, goodsClassChildInfos);
        classAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        classChildListAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        //设置重复执行动画
        classAdapter.isFirstOnly(false);
        classAdapter.setOnItemClickListener((adapter, view, i) -> {
            setResetView(selectView);
            setSelectView(view);
            selectView = view;
            if (goodsClassInfos.get(i).gc_id.equals("0")) {
                mPresenter.getBrandList();
            } else {
                mPresenter.getGoodsChild(goodsClassInfos.get(i).gc_id);
            }
        });
        goodclasschildAdapter = new BaseRecyclerAdapter<GoodsClassChildInfo.Class_list>(mContext, android.R.layout.simple_list_item_1, goodsClassChildInfos) {

            //            @Override
            public void convert(BaseViewHolder holder, GoodsClassChildInfo.Class_list s) {
                holder.setText(android.R.id.text1, s.gc_name);
            }
        };


        classtypeRecycleOne.setLayoutManager(new LinearLayoutManager(mContext));
        classtypeRecycleOne.setAdapter(goodsClassAdapter);
        classtypeRecycleTwo.setLayoutManager(new GridLayoutManager(mContext, 3));
        classtypeRecycleTwo.setAdapter(brandAdapter);
        classtypeViewSearch.setOnClickListener(v -> {
            showToast("查询");
        });
        initData();
    }

    public void setSelectView(View view) {
        TextView textView = view.findViewById(R.id.text);
        View line = view.findViewById(R.id.line);
        ImageView img = view.findViewById(R.id.img);
        textView.setTextColor(mContext.getResources().getColor(R.color.blue_dark_btn));
        line.setBackgroundColor(mContext.getResources().getColor(R.color.blue_dark_btn));
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                img.setBackground(tintDrawable(img.getDrawable(), mContext.getResources().getColor(R.color.blue_btn)));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void setResetView(View view) {
        TextView textView = view.findViewById(R.id.text);
        View line = view.findViewById(R.id.line);
        ImageView img = view.findViewById(R.id.img);
        textView.setTextColor(mContext.getResources().getColor(R.color.nc_text));
        line.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
        try {
            img.getDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private Drawable tintDrawable(Drawable drawable, int colors) {
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(colors));
        return wrappedDrawable;
    }

    private void initData() {
        mPresenter.getGoodsClass();
        mPresenter.getBrandList();
    }

    class ClassAdapter extends BaseQuickAdapter<GoodsClassInfo.Class_list, com.chad.library.adapter.base.BaseViewHolder> {

        public ClassAdapter(int layoutResId, @Nullable List<GoodsClassInfo.Class_list> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(com.chad.library.adapter.base.BaseViewHolder helper, GoodsClassInfo.Class_list item) {

            helper.setText(R.id.text, item.gc_name);
            GlideEngine.getInstance().loadImage(mContext, (ImageView) helper.getView(R.id.img), item.image);
            boolean isClick = false;
            if (item.gc_id.equals("0")) {
                isClick = true;
            }
            if (isClick) {
                selectView = helper.itemView;
                setSelectView(helper.itemView);
            } else {
                setResetView(helper.itemView);
            }
        }
    }

    class ClassChildListAdapter extends BaseQuickAdapter<GoodsClassChildInfo.Class_list, com.chad.library.adapter.base.BaseViewHolder> {
        public ClassChildListAdapter(int layoutResId, @Nullable List<GoodsClassChildInfo.Class_list> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(com.chad.library.adapter.base.BaseViewHolder helper, GoodsClassChildInfo.Class_list item) {
            helper.setText(R.id.text, item.gc_name);
            helper.getView(R.id.point).setBackgroundColor(Constants.RNDOMCOLOR);
            RecyclerView recyclerView = helper.getView(R.id.recycleview);
            classChildAdapter = new ClassChildAdapter(android.R.layout.simple_list_item_1, item.child);
            classChildAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
            recyclerView.setAdapter(classChildAdapter);
            classChildAdapter.setOnItemClickListener((a, v, p) -> {
                showToast(item.child.get(p).gc_name);
            });
        }
    }

    class ClassChildAdapter extends BaseQuickAdapter<GoodsClassChildInfo.Class_list.Child, com.chad.library.adapter.base.BaseViewHolder> {

        public ClassChildAdapter(int layoutResId, @Nullable List<GoodsClassChildInfo.Class_list.Child> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(com.chad.library.adapter.base.BaseViewHolder helper, GoodsClassChildInfo.Class_list.Child item) {
            TextView textView = helper.getView(android.R.id.text1);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setMaxLines(1);
            textView.setText(item.gc_name);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.REQUEST_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(new Intent(mContext, CaptureActivity.class), Constants.REQUEST_CAMERA);
                } else {
                    showToast("相机权限拒绝");
                }
                break;

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


        goodsClassAdapter.notifyDataSetChanged();
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
    public void fail(String msg) {
        showToast(msg);
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
