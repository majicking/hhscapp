package com.guohanhealth.shop.ui.goods.goodslist;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.adapter.BaseRecyclerAdapter;
import com.guohanhealth.shop.adapter.BaseViewHolder;
import com.guohanhealth.shop.adapter.CommonAdapter;
import com.guohanhealth.shop.adapter.ViewHolder;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.bean.Area_list;
import com.guohanhealth.shop.bean.GoodsListInfo;
import com.guohanhealth.shop.bean.SelectedInfo;
import com.guohanhealth.shop.custom.EmptyView;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.RxHelper;
import com.guohanhealth.shop.http.RxManager;
import com.guohanhealth.shop.ui.goods.GoodsModel;
import com.guohanhealth.shop.ui.goods.goodsdetailed.activity.GoodsDetailsActivity;
import com.guohanhealth.shop.ui.search.SearchActivity;
import com.guohanhealth.shop.utils.Utils;
import com.guohanhealth.shop.utils.engine.GlideEngine;
import com.guohanhealth.shop.view.NoScrollGridView;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class GoodsListActivity extends BaseActivity<GoodsListPersenter, GoodsModel> implements GoodsListView {
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
    private BaseRecyclerAdapter<GoodsListInfo.GoodsListBean, RecyclerView.ViewHolder> adapter;
    private List<Area_list> area_list;
    private List<SelectedInfo.Contract_list> contract_list;

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
    boolean isload;

    @Override
    protected void initView(Bundle savedInstanceState) {
        back.setOnClickListener(v -> finish());
        goodslistViewSearch.setOnClickListener(v -> readyGoThenKill(SearchActivity.class));
        goodslistViewSmartrefresh.setRefreshHeader(new WaterDropHeader(mContext));
        goodslistViewSmartrefresh.setOnRefreshListener((v) -> {
            isload = false;
            curpage = "1";
            getData();
        });
        goodslistViewSmartrefresh.setOnLoadMoreListener((v) -> {
            if (App.getApp().Hasmore()) {
                curpage = "" + App.getApp().getPage_total();
                isload = true;
                getData();
            } else {
                goodslistViewSmartrefresh.finishLoadMore();
            }
        });

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
            showSortPopu();//
            goodslistTextSort.setTextColor(getResources().getColor(R.color.appColor));
            goodslistTextSort.setSelected(true);
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
            goodslistTextSelect.setSelected(true);
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
        View nullview = view.findViewById(R.id.nullview);
        nullview.setOnClickListener(v -> popupWindow.dismiss());
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
        popupWindow.showAsDropDown(goodslistViewSort);
    }

    static SelectedInfo selectedInfo;

    public void showSelectPopu() {
        selectedInfo = new SelectedInfo();
        area_list = new ArrayList<>();
        contract_list = new ArrayList<>();
        selectedInfo.area_list = area_list;
        selectedInfo.contract_list = contract_list;
        View view = LayoutInflater.from(mContext).inflate(R.layout.goodlist_select_pop, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        View nullview = view.findViewById(R.id.nullview);
        nullview.setOnClickListener(v -> popupWindow.dismiss());
        NoScrollGridView gridView = view.findViewById(R.id.gridview);
        Spinner spinner = view.findViewById(R.id.select_pop_spinner);
        TextView location = view.findViewById(R.id.select_pop_location);
        EditText minprice = view.findViewById(R.id.select_pop_minprice);
        EditText maxprice = view.findViewById(R.id.select_pop_maxprice);
        final Button giftbtn = view.findViewById(R.id.select_pop_gift);
        final Button groupbuybtn = view.findViewById(R.id.select_pop_groupbuy);
        final Button timelimitbtn = view.findViewById(R.id.select_pop_timelimit);
        final Button virtulbtn = view.findViewById(R.id.select_pop_virtul);
        final Button onwbtn = view.findViewById(R.id.select_pop_onw);
        final Button selectbtn = view.findViewById(R.id.select_pop_btn);
        final Button resetbtn = view.findViewById(R.id.select_pop_reset);
        selectbtn.setActivated(true);
        resetbtn.setActivated(true);
        selectButton(giftbtn);
        selectButton(groupbuybtn);
        selectButton(timelimitbtn);
        selectButton(virtulbtn);
        selectButton(onwbtn);
        Area_list list = new Area_list();
        list.area_id = "0";
        list.area_name = "不限";
        location.setOnClickListener(v -> {
            spinner.performClick();
        });

        CommonAdapter adapter = new CommonAdapter<SelectedInfo.Contract_list>(mContext, contract_list, R.layout.select_pop_contract) {
            @Override
            public void convert(ViewHolder viewHolder, SelectedInfo.Contract_list item, int position, View convertView, ViewGroup parentViewGroup) {
                Button btn = (Button) viewHolder.getView(R.id.select_btn);
                btn.setText(item.name);
                btn.setHint(item.id);
                btn.setActivated(false);
                selectButton(btn);
            }
        };

        CommonAdapter adapter1 = new CommonAdapter<Area_list>(mContext, area_list, R.layout.item_text) {
            @Override
            public void convert(ViewHolder viewHolder, Area_list item, int position, View convertView, ViewGroup parentViewGroup) {
                TextView text = viewHolder.getView(R.id.text);
                text.setText(item.area_name);
                text.setHint(item.area_id);
            }
        };
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                area_list.clear();
                contract_list.clear();
                if (msg.obj != null) {
                    selectedInfo = ((SelectedInfo) msg.obj);
                }
                area_list = selectedInfo.area_list;
                contract_list = selectedInfo.contract_list;
                location.setText(list.area_name);
                location.setHint(list.area_id);
                minprice.setText("");
                maxprice.setText("");
                resetButton(giftbtn);
                resetButton(groupbuybtn);
                resetButton(timelimitbtn);
                resetButton(virtulbtn);
                resetButton(onwbtn);
                area_list.add(0, list);
                adapter.updataAdapter(contract_list);
                adapter.notifyDataSetChanged();
                adapter1.updataAdapter(area_list);
                adapter1.notifyDataSetChanged();
            }
        };
        gridView.setAdapter(adapter);
        spinner.setAdapter(adapter1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Area_list area_list = (Area_list) spinner.getItemAtPosition(position);
                location.setText(area_list.area_name);
                location.setHint(area_list.area_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getSelectData(handler);
        resetbtn.setOnClickListener(v -> {
            getSelectData(handler);
        });
        selectbtn.setOnClickListener(v -> {
            if (Utils.isEmpty(minprice)) {
                price_from = Utils.getEditViewText(minprice);
            }
            if (Utils.isEmpty(maxprice)) {
                price_to = Utils.getEditViewText(maxprice);
            }

            if (!Utils.getTextViewText(location).equals("不限")) {
                area_id = location.getHint().toString();
            }

            if (giftbtn.isActivated()) {
                gift = "1";
            }
            if (groupbuybtn.isActivated()) {
                groupbuy = "1";
            }
            if (timelimitbtn.isActivated()) {
                xianshi = "1";
            }
            if (virtulbtn.isActivated()) {
                virtual = "1";
            }
            if (onwbtn.isActivated()) {
                own_shop = "1";
            }
            for (int i = 0; i < gridView.getChildCount(); i++) {
                LinearLayout item = (LinearLayout) gridView.getChildAt(i);
                Button button = item.findViewById(R.id.select_btn);
                if (button.isActivated()) {
                    ci += button.getHint().toString() + "_";
                }
            }
            getData();
            popupWindow.dismiss();

        });
        popupWindow.showAsDropDown(goodslistViewSelect);
    }

    private void getSelectData(Handler handler) {
        new RxManager().add(Api.getDefault().getSelectedInfo().compose(RxHelper.handleResult()).subscribe(info -> {
            Message ms = new Message();
            ms.what = 1;
            ms.obj = info;
            handler.sendMessage(ms);
        }, throwable -> {
            showToast(throwable.getMessage());
        }));
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

    public void resetButton(Button btn) {
        btn.setActivated(false);
        btn.setTextColor(getResources().getColor(R.color.nc_text));
    }

    public void Reset() {
        goodslistTextSort.setSelected(false);
        goodslistTextSelect.setSelected(false);
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
        adapter = new BaseRecyclerAdapter<GoodsListInfo.GoodsListBean, RecyclerView.ViewHolder>(mContext, layout, list) {

            @Override
            public void convert(BaseViewHolder holder, GoodsListInfo.GoodsListBean item) {
                holder.itemView.setOnClickListener(v -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("goods_id", item.goods_id);
                    readyGo(GoodsDetailsActivity.class, bundle);
                });
                holder.setText(R.id.goodslist_item_list_name1, Utils.getString(item.goods_name));
                holder.setText(R.id.goodslist_item_list_name2, Utils.getString(item.goods_jingle));
                holder.setText(R.id.goodslist_item_list_salenumber, "销量：" + Utils.getString(item.goods_salenum));
                holder.setText(R.id.goodslist_item_list_storename, Utils.getString(item.store_name));
                holder.setText(R.id.goodslist_item_list_price, "￥ " + Utils.getString(item.goods_price));
                GlideEngine.getInstance().loadImage(mContext, holder.getView(R.id.goodslist_item_list_img), Utils.getString(item.goods_image_url));

                TextView isownshop = holder.getView(R.id.goodslist_item_list_ownstore);
                TextView fcode = holder.getView(R.id.goodslist_item_list_fcode);
                TextView itemtype = holder.getView(R.id.goodslist_item_list_grouppurchase);
                TextView virtual = holder.getView(R.id.goodslist_item_list_virtual);

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

                /**平台自营*/
                if (Utils.getString(item.is_own_shop).equals("1")) {
                    isownshop.setVisibility(View.VISIBLE);
                    isownshop.setBackgroundColor(Constants.RANDOMCOLOR);
                } else {
                    isownshop.setVisibility(View.GONE);
                }
                /**F商品*/
                if (Utils.getString(item.is_fcode).equals("1")) {
                    fcode.setVisibility(View.VISIBLE);
                    fcode.setBackgroundColor(Constants.RANDOMCOLOR);
                } else {
                    fcode.setVisibility(View.GONE);
                }
                /**虚拟*/
                if (Utils.getString(item.is_fcode).equals("1")) {
                    virtual.setVisibility(View.VISIBLE);
                    virtual.setBackgroundColor(Constants.RANDOMCOLOR);
                } else {
                    virtual.setVisibility(View.GONE);
                }
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
                } else if (Utils.getString(item.is_presell).equals("1")) {/**预售*/
                    itemtype.setVisibility(View.VISIBLE);
                    itemtype.setText(getString(R.string.text_presell));
                    itemtype.setBackgroundColor(Constants.RANDOMCOLOR);
                } else {
                    itemtype.setVisibility(View.GONE);
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
        goodslistViewSmartrefresh.finishLoadMore();
        if (!isload) {
            list.clear();
        }
        if (info != null && info.size() > 0) {
            list.addAll(info);
            emptyview.setVisibility(View.GONE);
        } else {
            emptyview.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }


}
