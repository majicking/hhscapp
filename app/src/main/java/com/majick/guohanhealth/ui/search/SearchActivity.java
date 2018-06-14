package com.majick.guohanhealth.ui.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.majick.guohanhealth.R;
import com.majick.guohanhealth.adapter.CommonAdapter;
import com.majick.guohanhealth.adapter.ViewHolder;
import com.majick.guohanhealth.app.App;
import com.majick.guohanhealth.app.Constants;
import com.majick.guohanhealth.base.BaseActivity;
import com.majick.guohanhealth.bean.SearchInfo;
import com.majick.guohanhealth.ui.goods.goodslist.GoodsListActivity;
import com.majick.guohanhealth.utils.Utils;
import com.majick.guohanhealth.view.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

public class SearchActivity extends BaseActivity<SearchPercenter, SearchModel> implements SearchView {


    @BindView(R.id.search_back)
    RelativeLayout searchBack;
    @BindView(R.id.search_edit)
    EditText searchEdit;
    @BindView(R.id.search_btn)
    Button searchBtn;
    @BindView(R.id.search_recycleview_hot)
    RecyclerView searchRecycleviewHot;
    @BindView(R.id.search_recycleview_history)
    RecyclerView searchRecycleviewHistory;
    @BindView(R.id.search_del)
    Button searchDel;
    @BindView(R.id.search_view_history)
    LinearLayout searchViewHistory;
    @BindView(R.id.gridview)
    NoScrollGridView gridview;
    private List<String> hotlist;
    private List<String> historylist;
    private HotAdapter adapter;
    private HotAdapter adapter1;
    private CommonAdapter<String> adapter2;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        searchBack.setOnClickListener(v -> finish());
        searchBtn.setOnClickListener(v -> {
            doSomeThing(Utils.getEditViewText(searchEdit));
        });
        hotlist = new ArrayList<>();
        historylist = new ArrayList<>();
        searchRecycleviewHistory.setLayoutManager(new GridLayoutManager(mContext, 3));
        searchRecycleviewHot.setLayoutManager(new GridLayoutManager(mContext, 3));
        adapter = new HotAdapter(android.R.layout.simple_list_item_1, hotlist);
        adapter1 = new HotAdapter(android.R.layout.simple_list_item_1, historylist);
        searchRecycleviewHot.setAdapter(adapter);
        searchRecycleviewHistory.setAdapter(adapter1);
        adapter2 = new CommonAdapter<String>(mContext, hotlist, R.layout.search_list_item) {
            @Override
            public void convert(ViewHolder viewHolder, String item, int position, View convertView, ViewGroup parentViewGroup) {
                viewHolder.setText(R.id.text, item);
                viewHolder.getView(R.id.text).setBackgroundColor(Constants.BGCOLORS[new Random().nextInt(10)]);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                //拖拽
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
                //滑出屏幕
                int swipeFlags = ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                return makeMovementFlags(dragFlags, 0);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int position_target = target.getLayoutPosition();
                int position = viewHolder.getLayoutPosition();
                return true;
            }

            //标签动画持续时间，默认是250
            @Override
            public long getAnimationDuration(RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {

                return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy);
            }

            /**
             * 是否可以长按拖拽，默认是true
             *
             * @return
             */
            @Override
            public boolean isLongPressDragEnabled() {
                return super.isLongPressDragEnabled();
            }

            /**
             * 标签划出去的回调，direction是滑动的方向
             *
             * @return
             */

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            }
        });
        itemTouchHelper.attachToRecyclerView(searchRecycleviewHot);
        gridview.setAdapter(adapter2);
        gridview.setOnItemClickListener((a, v, p, i) -> {
            doSomeThing(hotlist.get(p));
        });
        searchEdit.setHint(App.getApp().getHotname());
        searchEdit.setText(App.getApp().getHotvalue());
    }

    public void doSomeThing(String value) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEYWORD, value);
        readyGoThenKill(GoodsListActivity.class, bundle);
    }

    class HotAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public HotAdapter(int layoutResId, @Nullable List<String> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(android.R.id.text1, item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getSearchDataList();
    }


    @Override
    public void faild(String msg) {
        showToast(msg);
    }

    @Override
    public void getSearchList(SearchInfo info) {
        hotlist = info.list;
//        adapter.notifyDataSetChanged();
        adapter2.updataAdapter(hotlist);

        if (info.his_list != null && info.his_list.size() > 0) {
            historylist = info.his_list;
            adapter1.notifyDataSetChanged();
            searchDel.setActivated(true);
        } else {
            TextView t = new TextView(mContext);
            t.setText("暂无历史记录");
            t.setPadding(25, 15, 0, 15);
            searchDel.setActivated(false);
            adapter1.setEmptyView(t);
        }
        searchDel.setOnClickListener(v -> {
            if (searchDel.isActivated()) {

            } else {
                showToast("暂无数据");
            }
        });
    }


}
