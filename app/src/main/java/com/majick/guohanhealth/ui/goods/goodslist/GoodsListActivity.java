package com.majick.guohanhealth.ui.goods.goodslist;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.majick.guohanhealth.R;
import com.majick.guohanhealth.adapter.BaseRecyclerAdapter;
import com.majick.guohanhealth.adapter.BaseViewHolder;
import com.majick.guohanhealth.adapter.CommonAdapter;
import com.majick.guohanhealth.adapter.ViewHolder;
import com.majick.guohanhealth.app.Constants;
import com.majick.guohanhealth.base.BaseActivity;
import com.majick.guohanhealth.bean.GoodsListInfo;
import com.majick.guohanhealth.bean.SelectedInfo;
import com.majick.guohanhealth.custom.EmptyView;
import com.majick.guohanhealth.http.Api;
import com.majick.guohanhealth.http.RxHelper;
import com.majick.guohanhealth.http.RxManager;
import com.majick.guohanhealth.ui.search.SearchActivity;
import com.majick.guohanhealth.utils.Logutils;
import com.majick.guohanhealth.utils.Utils;
import com.majick.guohanhealth.utils.engine.GlideEngine;
import com.majick.guohanhealth.view.NoScrollGridView;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class GoodsListActivity extends BaseActivity<GoodsListPersenter, GoodsListModel> implements GoodsListView {
    @BindView(R.id.search_btn)
    ImageView searchBtn;
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
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.emptyview)
    EmptyView emptyview;
    @BindView(R.id.goodslist_hot_text)
    TextView goodslistHotText;
    @BindView(R.id.goodslist_view_search)
    LinearLayout goodslistViewSearch;
    @BindView(R.id.goodslist_text_advance)
    TextView goodslistTextAdvance;
    @BindView(R.id.goodslist_text_select)
    TextView goodslistTextSelect;
    @BindView(R.id.goodslist_text_sort)
    TextView goodslistTextSort;
    private BaseRecyclerAdapter<GoodsListInfo.GoodsListBean> adapter;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_goods_list;
    }

    //key 排序方式 1-销量 2-浏览量(人气排序) 3-价格 空-最新发布(综合排序)
    //order 排序方式 1-升序 2-降序
    List<GoodsListInfo.GoodsListBean> list;
    boolean isGrideOrList = false;
    String curpage = "1";
    String page = "10";
    String keyword;//关键字
    String key;
    String order;
    String price_from;
    String price_to;
    String area_id;
    String gift;
    String groupbuy;
    String xianshi;
    String virtual;
    String own_shop;
    String ci;

    @Override
    protected void initView(Bundle savedInstanceState) {
        back.setOnClickListener(v -> finish());
        goodslistViewSearch.setOnClickListener(v -> readyGoThenKill(SearchActivity.class));
        goodslistViewSmartrefresh.setRefreshHeader(new WaterDropHeader(mContext));
        goodslistViewSmartrefresh.setOnRefreshListener((v) -> getData());
        emptyview.setEmptyText1("点击重新加载").setOnClickListener(v -> {
            goodslistViewSmartrefresh.finishRefresh(false);
            getData();
        });
        keyword = getIntent().getStringExtra(Constants.KEYWORD);
        goodslistHotText.setText(keyword);
        list = new ArrayList<>();
        goodslistRecycleview.setLayoutManager(new LinearLayoutManager(mContext));
        setAdapter(R.layout.goodslist_item_list);
        goodslistImgChange.setSelected(true);
        goodslistImgChange.setOnClickListener(v -> {
            if (isGrideOrList) {
                goodslistImgChange.setSelected(isGrideOrList);
                goodslistRecycleview.setLayoutManager(new LinearLayoutManager(mContext));
                setAdapter(R.layout.goodslist_item_list);
            } else {
                goodslistImgChange.setSelected(isGrideOrList);
                goodslistRecycleview.setLayoutManager(new GridLayoutManager(mContext, 2));
                setAdapter(R.layout.goodslist_item_gride);
            }
            isGrideOrList = !isGrideOrList;
        });
        goodslistViewSort.setOnClickListener(v -> {
            Reset();
            showSortPopu();
            goodslistTextSort.setTextColor(getResources().getColor(R.color.appColor));
        });
        goodslistViewAdvance.setOnClickListener(v -> {
            Reset();
            key = "1";
            order = "2";
            goodslistTextAdvance.setTextColor(getResources().getColor(R.color.appColor));
            getData();
        });
        goodslistViewSelect.setOnClickListener(v -> {
            Reset();
            showSelectPopu();
            goodslistTextSelect.setTextColor(getResources().getColor(R.color.appColor));

        });
        getData();
    }

    PopupWindow popupWindow;
    int count = 0;

    public void showSortPopu() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.goodlist_sort_pop, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        ListView listView = view.findViewById(R.id.pop_recycle);
        List<String> list = new ArrayList<>(Arrays.asList("综合排序", "价格降序", "价格升序", "人气排行"));
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(goodslistTextSort.getText().toString())) {
                count = i;
                break;
            }
        }
        listView.setAdapter(new CommonAdapter<String>(mContext, list, R.layout.goodlist_sort_pop_item) {
            @Override
            public void convert(ViewHolder viewHolder, String item, int position, View convertView, ViewGroup parentViewGroup) {
                TextView text = viewHolder.getView(R.id.text);
                text.setText(item);
                if (position == count) {
                    text.setSelected(true);//这里设置状态
                    text.setTextColor(getResources().getColor(R.color.appColor));
                } else {
                    text.setSelected(false);//这里设置状态
                    text.setTextColor(getResources().getColor(R.color.nc_text));
                }
            }
        });

        listView.setOnItemClickListener((a, v, p, i) -> {
            switch (p) {
                case 0:
                    key = "";
                    order = "";
                    break;
                case 1:
                    key = "3";
                    order = "2";
                    break;
                case 2:
                    key = "3";
                    order = "1";
                    break;
                case 3:
                    key = "2";
                    order = "2";
                    break;
            }
            goodslistTextSort.setText(list.get(p));
            getData();
            popupWindow.dismiss();
        });
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        popupWindow.showAsDropDown(goodslistViewSort);
        popupWindow.setOnDismissListener(() -> {
            lp.alpha = 1f;
            getWindow().setAttributes(lp);
        });
    }

    public void showSelectPopu() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.goodlist_select_pop, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        NoScrollGridView gridView=view.findViewById(R.id.gridview);
        new RxManager().add(Api.getDefault().getSelectedInfo().compose(RxHelper.handleResult()).subscribe(info -> {
            Logutils.i(info);
            gridView.setAdapter(new CommonAdapter<SelectedInfo.Contract_list>(mContext, info.contract_list, R.layout.select_pop_contract) {
                @Override
                public void convert(ViewHolder viewHolder, SelectedInfo.Contract_list item, int position, View convertView, ViewGroup parentViewGroup) {
                    viewHolder.setText(R.id.select_btn,item.name);
                    ((TextView)viewHolder.getView(R.id.select_btn)).setHint(item.id);
                }
            });
            gridView.setOnItemClickListener((a,v,p,i)->{
                LinearLayout relativeLayout=(LinearLayout) gridView.getAdapter().getView(p,view,null);
                Button button=relativeLayout.findViewById(R.id.select_btn);
                if (v.isActivated()){
                    button.setHintTextColor(getResources().getColor(R.color.nc_text));
                }else{
                    button.setHintTextColor(getResources().getColor(R.color.white));
                }
                v.setActivated(!v.isActivated());
            });
        }, throwable -> {
            showToast(throwable.getMessage());
        }));
        EditText minprice = view.findViewById(R.id.select_pop_minprice);
        EditText maxprice = view.findViewById(R.id.select_pop_maxprice);
        TextView location = view.findViewById(R.id.select_pop_location);
        Button gift = view.findViewById(R.id.select_pop_gift);
        Button groupbuy = view.findViewById(R.id.select_pop_groupbuy);
        Button timelimit = view.findViewById(R.id.select_pop_timelimit);
        Button virtul = view.findViewById(R.id.select_pop_virtul);
        Button onw = view.findViewById(R.id.select_pop_onw);
        Button sevendayreturn = view.findViewById(R.id.select_pop_sevendayreturn);
        Button quality = view.findViewById(R.id.select_pop_quality);
        Button mailing = view.findViewById(R.id.select_pop_mailing);
        Button rapid = view.findViewById(R.id.select_pop_rapid);
        Button btn = view.findViewById(R.id.select_pop_btn);
        selectButton(gift);
        selectButton(groupbuy);
        selectButton(timelimit);
        selectButton(virtul);
        selectButton(onw);
        selectButton(sevendayreturn);
        selectButton(quality);
        selectButton(mailing);
        selectButton(rapid);
        selectButton(btn);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.8f;
        getWindow().setAttributes(lp);
        popupWindow.showAsDropDown(goodslistViewSelect);
        popupWindow.setOnDismissListener(() -> {
            lp.alpha = 1f;
            getWindow().setAttributes(lp);
        });
    }

    public void selectButton(Button btn) {
        btn.setOnClickListener(v -> {
            if (btn.isActivated()) {
                btn.setTextColor(getResources().getColor(R.color.nc_text));
            } else {
                btn.setTextColor(getResources().getColor(R.color.white));
            }
            btn.setActivated(!btn.isActivated());
        });
    }

    public void Reset() {
        goodslistTextSort.setTextColor(getResources().getColor(R.color.nc_text));
        goodslistTextAdvance.setTextColor(getResources().getColor(R.color.nc_text));
        goodslistTextSelect.setTextColor(getResources().getColor(R.color.nc_text));
        curpage = "1";
        page = "10";
        keyword = "";
        key = "";
        order = "";
        price_from = "";
        price_to = "";
        area_id = "";
        gift = "";
        groupbuy = "";
        xianshi = "";
        virtual = "";
        own_shop = "";
        ci = "";

    }

    private void setAdapter(int layout) {
        adapter = new BaseRecyclerAdapter<GoodsListInfo.GoodsListBean>(mContext, layout, list) {

            @Override
            public void convert(BaseViewHolder holder, GoodsListInfo.GoodsListBean item) {
                holder.setText(R.id.goodslist_item_list_name1, Utils.getString(item.goods_name));
                holder.setText(R.id.goodslist_item_list_name2, Utils.getString(item.goods_jingle));
                holder.setText(R.id.goodslist_item_list_salenumber, "销量：" + Utils.getString(item.goods_salenum));
                holder.setText(R.id.goodslist_item_list_storename, Utils.getString(item.store_name));
                holder.setText(R.id.goodslist_item_list_price, "￥ " + Utils.getString(item.goods_price));
                GlideEngine.getInstance().loadImage(mContext, holder.getView(R.id.goodslist_item_list_img), Utils.getString(item.goods_image_url));
                /**星级*/
                if (Utils.isEmpty(item.evaluation_good_star) && !item.evaluation_good_star.equals("0")) {
                    GlideEngine.getInstance().loadImage(mContext, holder.getView(R.id.goodslist_item_list_rating), Utils.getString(item.goods_grade));
                } else {
                    holder.getView(R.id.goodslist_item_list_rating).setVisibility(View.GONE);
                }
                /**赠品*/
                if (Utils.getString(item.have_gift).equals("1")) {
                    holder.getView(R.id.goodslist_item_list_gift).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.goodslist_item_list_gift).setVisibility(View.GONE);
                }
                TextView itemtype = holder.getView(R.id.goodslist_item_list_grouppurchase);

                if (item.group_flag) { /**团购*/
                    itemtype.setVisibility(View.VISIBLE);
                    itemtype.setText(getString(R.string.text_groupbuy));
                    itemtype.setBackgroundColor(Constants.RANDOMCOLOR);
                } else if (item.sole_flag) {/**手机专享*/
                    itemtype.setVisibility(View.VISIBLE);
                    itemtype.setBackgroundResource(R.mipmap.mobile_price);
                } else if (item.xianshi_flag) {/**限时*/
                    itemtype.setVisibility(View.VISIBLE);
                    itemtype.setText(getString(R.string.text_xianshi));
                    itemtype.setBackgroundColor(Constants.RANDOMCOLOR);
                } else if (Utils.getString(item.is_appoint).equals("1")) {/**预约*/
                    itemtype.setVisibility(View.VISIBLE);
                    itemtype.setText(getString(R.string.text_appoint));
                    itemtype.setBackgroundColor(Constants.RANDOMCOLOR);
                } else if (Utils.getString(item.is_fcode).equals("1")) {/**F码*/
                    itemtype.setVisibility(View.VISIBLE);
                    itemtype.setText(getString(R.string.text_fcode));
                    itemtype.setBackgroundColor(Constants.RANDOMCOLOR);
                } else if (Utils.getString(item.is_presell).equals("1")) {/**预售*/
                    itemtype.setVisibility(View.VISIBLE);
                    itemtype.setText(getString(R.string.text_presell));
                    itemtype.setBackgroundColor(Constants.RANDOMCOLOR);
                } else if (Utils.getString(item.is_virtual).equals("1")) {/**虚拟*/
                    itemtype.setVisibility(View.VISIBLE);
                    itemtype.setText(getString(R.string.text_virtual));
                    itemtype.setBackgroundColor(Constants.RANDOMCOLOR);
                } else {
                    holder.getView(R.id.goodslist_item_list_grouppurchase).setVisibility(View.GONE);
                }

            }

        };
        goodslistRecycleview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void getData() {
        keyword = getIntent().getStringExtra(Constants.KEYWORD);
        mPresenter.getGoodsList(curpage, page, keyword, key, order, price_from, price_to, area_id,
                gift, groupbuy, xianshi, virtual, own_shop, ci);
    }


    @Override
    public void faild(String msg) {
        showToast(msg);
    }


    @Override
    public void getGoodsListData(List<GoodsListInfo.GoodsListBean> info) {
        goodslistViewSmartrefresh.finishRefresh();
        list.clear();
        if (info != null && info.size() > 0) {
            list.addAll(info);
            adapter.notifyDataSetChanged();
            emptyview.setVisibility(View.GONE);
        } else {
            emptyview.setVisibility(View.VISIBLE);
        }
    }


}
