package com.majick.guohanhealth.ui.main.fragment.classtype;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.majick.guohanhealth.R;
import com.majick.guohanhealth.adapter.BaseRecyclerAdapter;
import com.majick.guohanhealth.adapter.BaseViewHolder;
import com.majick.guohanhealth.app.Constants;
import com.majick.guohanhealth.base.BaseFragment;
import com.majick.guohanhealth.bean.BrandListInfo;
import com.majick.guohanhealth.bean.GoodsClassChildInfo;
import com.majick.guohanhealth.bean.GoodsClassInfo;
import com.majick.guohanhealth.ui.main.fragment.OnFragmentInteractionListener;
import com.majick.guohanhealth.view.scannercode.android.CaptureActivity;
import com.scwang.smartrefresh.header.DeliveryHeader;
import com.scwang.smartrefresh.header.FlyRefreshHeader;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.header.TaurusHeader;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.header.WaveSwipeHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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
    private BaseRecyclerAdapter<GoodsClassInfo.Class_list> goodclassAdapter;
    private BaseRecyclerAdapter<BrandListInfo.Brand_list> brandAdapter;
    private BaseRecyclerAdapter<GoodsClassChildInfo.Class_list> goodclasschildAdapter;
    private List<GoodsClassInfo.Class_list> goodsClassInfos;
    private List<BrandListInfo.Brand_list> brandListInfos;
    private List<GoodsClassChildInfo.Class_list> goodsClassChildInfos;

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
        classtypeViewRefresh.setOnRefreshListener(refreshLayout -> {
            classtypeViewRefresh.finishRefresh();
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
        goodclassAdapter = new BaseRecyclerAdapter<GoodsClassInfo.Class_list>(mContext, R.layout.classtype_item_goodsclass, goodsClassInfos) {
            //            @Override
            public void convert(BaseViewHolder holder, GoodsClassInfo.Class_list s) {
                holder.setText(R.id.text, s.gc_name);
                holder.setImageByUrl(R.id.img,s.image);
            }
        };
        brandAdapter = new BaseRecyclerAdapter<BrandListInfo.Brand_list>(mContext, android.R.layout.simple_list_item_1, brandListInfos) {
            //            @Override
            public void convert(BaseViewHolder holder, BrandListInfo.Brand_list s) {
                holder.setText(android.R.id.text1, s.brand_name);

            }
        };
        goodclasschildAdapter = new BaseRecyclerAdapter<GoodsClassChildInfo.Class_list>(mContext, android.R.layout.simple_list_item_1, goodsClassChildInfos) {
            //            @Override
            public void convert(BaseViewHolder holder, GoodsClassChildInfo.Class_list s) {
                holder.setText(android.R.id.text1, s.gc_name);
            }
        };
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        GridLayoutManager manager1 = new GridLayoutManager(mContext, 3);
        classtypeRecycleOne.setLayoutManager(manager);
        classtypeRecycleOne.setAdapter(goodclassAdapter);
        classtypeRecycleTwo.setLayoutManager(manager1);
        classtypeRecycleTwo.setAdapter(brandAdapter);
        initData();
    }

    private void initData() {
        mPresenter.getGoodsClass();
        mPresenter.getBrandList();
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

    @Override
    public void getGoodsClass(List<GoodsClassInfo.Class_list> info) {
        goodsClassInfos.addAll(info);
        goodclassAdapter.notifyDataSetChanged();
    }

    @Override
    public void getBrandList(List<BrandListInfo.Brand_list> info) {
        brandListInfos.addAll(info);
        brandAdapter.notifyDataSetChanged();
    }

    @Override
    public void getGoodsChild(List<GoodsClassChildInfo.Class_list> info) {
        goodsClassChildInfos.addAll(info);
        goodclasschildAdapter.notifyDataSetChanged();
    }

    @Override
    public void fail(String msg) {
        showToast(msg);
    }
}
