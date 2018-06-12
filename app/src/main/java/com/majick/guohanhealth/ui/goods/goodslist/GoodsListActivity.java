package com.majick.guohanhealth.ui.goods.goodslist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.majick.guohanhealth.R;
import com.majick.guohanhealth.adapter.BaseRecyclerAdapter;
import com.majick.guohanhealth.adapter.BaseViewHolder;
import com.majick.guohanhealth.app.Constants;
import com.majick.guohanhealth.base.BaseActivity;
import com.majick.guohanhealth.bean.GoodsListInfo;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodsListActivity extends BaseActivity<GoodsListPersenter, GoodsListModel> implements GoodsListView {
    @BindView(R.id.search_edit)
    EditText searchEdit;
    @BindView(R.id.search_btn)
    ImageView searchBtn;
    @BindView(R.id.goodslist_sort_text)
    TextView goodslistSortText;
    @BindView(R.id.goodslist_view_sort)
    LinearLayout goodslistViewSort;
    @BindView(R.id.goodslist_view_advance)
    LinearLayout goodslistViewAdvance;
    @BindView(R.id.goodslist_view_select)
    LinearLayout goodslistViewSelect;
    @BindView(R.id.goodslist_img_change)
    ImageView goodslistImgChange;
    @BindView(R.id.goodslist_recycleview)
    RecyclerView goodslistRecycleview;
    @BindView(R.id.goodslist_view_smartrefresh)
    SmartRefreshLayout goodslistViewSmartrefresh;
    @BindView(R.id.goodslist_flaybtn)
    FloatingActionButton goodslistFlaybtn;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_goods_list;
    }

    //key 排序方式 1-销量 2-浏览量(人气排序) 3-价格 空-最新发布(综合排序)
    //order 排序方式 1-升序 2-降序
    List<GoodsListInfo> list;
    String keyWords = "";//关键字

    @Override
    protected void initView(Bundle savedInstanceState) {
        keyWords = getIntent().getStringExtra(Constants.KEYWORD);
        mPresenter.getGoodsList("1", "10", keyWords, "", "", "", "", "", "", "", "", "", "", "");
        list = new ArrayList<>();
        goodslistRecycleview.setLayoutManager(new LinearLayoutManager(mContext));
        goodslistRecycleview.setAdapter(new BaseRecyclerAdapter<GoodsListInfo>(mContext, R.layout.goodslist_item_list, list) {

            @Override
            public void convert(BaseViewHolder holder, GoodsListInfo o) {
//                    holder.setText(R.id.goodslist_item_list_name1,)
            }

        });


    }

    @Override
    public void faild(String msg) {

    }


    @Override
    public void getGoodsListData(GoodsListInfo info) {

    }
}
