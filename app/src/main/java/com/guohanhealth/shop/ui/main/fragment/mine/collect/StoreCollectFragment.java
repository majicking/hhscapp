package com.guohanhealth.shop.ui.main.fragment.mine.collect;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.base.BaseFragment;
import com.guohanhealth.shop.bean.GoodsCollectInfo;
import com.guohanhealth.shop.bean.StoreCollectInfo;
import com.guohanhealth.shop.custom.EmptyView;
import com.guohanhealth.shop.event.OnFragmentInteractionListener;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.ApiService;
import com.guohanhealth.shop.http.HttpErrorCode;
import com.guohanhealth.shop.ui.main.activity.MainActivity;
import com.guohanhealth.shop.utils.Utils;
import com.guohanhealth.shop.utils.engine.GlideEngine;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class StoreCollectFragment extends BaseFragment<CollectPersenter, CollectModel> implements CollectView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;
    @BindView(R.id.emptyview)
    EmptyView emptyview;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Adapters mAdapters;

    public StoreCollectFragment() {
        mList = new ArrayList<>();
    }

    public static StoreCollectFragment newInstance(String param1, String param2) {
        StoreCollectFragment fragment = new StoreCollectFragment();
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
        return R.layout.recycleview;
    }

    boolean isLoad;
    public List<StoreCollectInfo.DataBean> mList;

    @Override
    protected void initView(Bundle savedInstanceState) {

        smartrefreshlayout.setRefreshHeader(new ClassicsHeader(mContext));
        smartrefreshlayout.setOnRefreshListener(refreshLayout -> {
            curpage = 1;
            isLoad = false;
            getStoreData();
        });
        smartrefreshlayout.setOnLoadMoreListener(refreshLayout -> {
            if (Boolean.valueOf(App.getApp().Hasmore()) && App.getApp().getPage_total() > curpage) {
                isLoad = true;
                curpage++;
                getStoreData();
            } else {
                stopReOrLoad();
            }
        });
        emptyview.setEmpty("您还没有关注任何商品", "可以去看看哪些商品值得收藏");
        emptyview.setVisibility(View.VISIBLE);
        emptyview.setClickListener(v -> {
            readyGoThenKill(MainActivity.class);
        });
        mAdapters = new Adapters(R.layout.storecollect_item, mList);
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerview.setAdapter(mAdapters);
    }

    private void getStoreData() {
        mPresenter.getStoreCollectList(App.getApp().getKey(), curpage + "");
    }


    private void stopReOrLoad() {
        if (smartrefreshlayout != null) {
            if (smartrefreshlayout.isLoading()) {
                smartrefreshlayout.finishLoadMore();
            }
            if (smartrefreshlayout.isRefreshing()) {
                smartrefreshlayout.finishRefresh();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getStoreData();
    }

    class Adapters extends BaseQuickAdapter<StoreCollectInfo.DataBean, BaseViewHolder> {

        public Adapters(int layoutResId, @Nullable List<StoreCollectInfo.DataBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, StoreCollectInfo.DataBean item) {
            GlideEngine.getInstance().loadImage(mContext, helper.getView(R.id.img1), item.store_avatar_url);
            helper.setText(R.id.text1, item.store_name);
            helper.setText(R.id.text2, "粉丝：" + item.store_collect + "人 ");
            helper.setText(R.id.text3, "商品：" + item.goods_count + "件");
            helper.getView(R.id.img2).setOnClickListener(v -> {
                Api.post(ApiService.STOREFAVORITES_DEL,
                        new FormBody.Builder()
                                .add("key", App.getApp().getKey())
                                .add("store_id", item.store_id)
                                .build(),
                        new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                getActivity().runOnUiThread(() -> {
                                    showToast(Utils.getErrorString(e));
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                try {
                                    String json = response.body().string();
                                    if (Utils.getCode(json) == HttpErrorCode.HTTP_NO_ERROR) {
                                        if (Utils.getDatasString(json).equals("1")) {
                                            getActivity().runOnUiThread(() -> {
                                                showToast("操作成功");
                                                getStoreData();
                                            });
                                        }
                                    } else if (Utils.getCode(json) == HttpErrorCode.ERROR_400) {
                                        getActivity().runOnUiThread(() -> {
                                            showToast(Utils.getErrorString(json));
                                        });
                                    }
                                } catch (Exception e) {
                                    getActivity().runOnUiThread(() -> {
                                        showToast(Utils.getErrorString(e));
                                    });
                                }

                            }
                        });
            });
        }
    }


    int curpage = 1;

    public void onButtonPressed(String key, String value) {
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
        showToast(msg);
        stopReOrLoad();
        recyclerview.setVisibility(View.GONE);
        emptyview.setVisibility(View.VISIBLE);
    }

    @Override
    public void getData(Object data) {
        stopReOrLoad();
        StoreCollectInfo info = (StoreCollectInfo) data;
        if (info != null) {
            if (!isLoad) {
                mList.clear();
            }
            if (Utils.isEmpty(info.favorites_list)) {
                recyclerview.setVisibility(View.VISIBLE);
                emptyview.setVisibility(View.GONE);
                mList.addAll(info.favorites_list);
            } else {
                recyclerview.setVisibility(View.GONE);
                emptyview.setVisibility(View.VISIBLE);
            }
            mAdapters.notifyDataSetChanged();
        }
    }

}
