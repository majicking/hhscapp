package com.guohanhealth.shop.ui.main.spec;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.adapter.CommonAdapter;
import com.guohanhealth.shop.adapter.ViewHolder;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.bean.Adv_list;
import com.guohanhealth.shop.bean.Goods1Bean;
import com.guohanhealth.shop.bean.Goods2Bean;
import com.guohanhealth.shop.bean.GoodsInfo;
import com.guohanhealth.shop.bean.Home1Info;
import com.guohanhealth.shop.bean.Home2Info;
import com.guohanhealth.shop.bean.Home3Info;
import com.guohanhealth.shop.bean.Home4Info;
import com.guohanhealth.shop.bean.Home5Info;
import com.guohanhealth.shop.custom.MyGridView;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.ApiService;
import com.guohanhealth.shop.http.HttpErrorCode;
import com.guohanhealth.shop.ui.goods.goodsdetailed.activity.GoodsDetailsActivity;
import com.guohanhealth.shop.ui.main.fragment.home.HomeView;
import com.guohanhealth.shop.ui.main.fragment.mine.address.AddressAddActivity;
import com.guohanhealth.shop.utils.JSONParser;
import com.guohanhealth.shop.utils.Utils;
import com.guohanhealth.shop.utils.engine.GlideEngine;
import com.guohanhealth.shop.view.NoScrollGridView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SpecActivity extends BaseActivity {

    @BindView(R.id.common_toolbar_title_tv)
    TextView commonToolbarTitleTv;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.view_layout)
    LinearLayout homeViewLayout;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;
    private String special_id;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_spec;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initToolBarNav(commonToolbar, commonToolbarTitleTv, "专题活动");
        special_id = getIntent().getStringExtra("special_id");
        smartrefreshlayout.setRefreshHeader(new ClassicsHeader(mContext));
        smartrefreshlayout.setOnRefreshListener(view -> {
            getData();
        });
    }

    //如果你需要考虑更好的体验，可以这么操作
    @Override
    protected void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    public void getData() {
        Api.get( ApiService.SPECIAL + "&special_id=" + special_id, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    if (smartrefreshlayout != null && smartrefreshlayout.isRefreshing()) {
                        smartrefreshlayout.finishRefresh();
                    }
                    showToast(Utils.getErrorString(e));
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (smartrefreshlayout != null && smartrefreshlayout.isRefreshing()) {
                    smartrefreshlayout.finishRefresh();
                }
                try {
                    String json = response.body().string();
                    if (Utils.getCode(json) == HttpErrorCode.HTTP_NO_ERROR) {
                        runOnUiThread(() -> {
                            try {

                                String spectitle = Utils.getValue("special_desc", Utils.getDatasString(json));
                                initToolBarNav(commonToolbar, commonToolbarTitleTv, spectitle);
                                homeViewLayout.removeAllViews();
                                String Object = JSONParser.getStringFromJsonString("list", Utils.getDatasString(json));
                                JSONArray arr = new JSONArray(Object);
                                int size = null == arr ? 0 : arr.length();
                                for (int i = 0; i < size; i++) {
                                    JSONObject obj = arr.getJSONObject(i);
                                    JSONObject JsonObj = new JSONObject(obj.toString());
                                    if (!JsonObj.isNull("home1")) {
                                        showHome1(JSONParser.JSON2Object(JsonObj.getString("home1"), Home1Info.class));
                                    } else if (!JsonObj.isNull("home2")) {
                                        showHome2(JSONParser.JSON2Object(JsonObj.getString("home2"), Home2Info.class));
                                    } else if (!JsonObj.isNull("home3")) {
                                        showHome3(JSONParser.JSON2Object(JsonObj.getString("home3"), Home3Info.class));
                                    } else if (!JsonObj.isNull("home4")) {
                                        showHome4(JSONParser.JSON2Object(JsonObj.getString("home4"), Home4Info.class));
                                    } else if (!JsonObj.isNull("home5")) {
                                        showHome5(JSONParser.JSON2Object(JsonObj.getString("home5"), Home5Info.class));
                                    } else if (!JsonObj.isNull("adv_list")) {//banner
                                        showAdvList(JSONParser.JSON2Object(JsonObj.getString("adv_list"), Adv_list.class).item);
                                    } else if (!JsonObj.isNull("video_list")) {     //视频接口
                                        showVideoView(JsonObj);
                                    } else if (!JsonObj.isNull("goods")) {//商品版块
                                        showGoods(JSONParser.JSON2Object(JsonObj.getString("goods"), GoodsInfo.class));
                                    } else if (!JsonObj.isNull("goods1")) {    //限时商品
                                        showGoods1(JSONParser.JSON2Object(JsonObj.getString("goods1"), Goods1Bean.class));
                                    } else if (!JsonObj.isNull("goods2")) {     //抢购商品
                                        showGoods2(JSONParser.JSON2Object(JsonObj.getString("goods2"), Goods2Bean.class));
                                    }
                                }
                            } catch (Exception e) {
                                runOnUiThread(() -> {
                                    showToast(Utils.getErrorString(json));
                                });
                            }
                        });
                    } else {
                        runOnUiThread(() -> {
                            showToast(Utils.getErrorString(json));
                        });
                    }
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        showToast(Utils.getErrorString(e));
                    });
                }
            }
        });
    }


    public void showHome1(Home1Info jsonobj) {
        View view = getView(R.layout.home_item_home1);
        TextView title = (TextView) getView(view, R.id.title);
        ImageView img = (ImageView) getView(view, R.id.img);
        if (Utils.isEmpty(jsonobj.title)) {
            title.setText(jsonobj.title);
        } else {
            title.setVisibility(View.GONE);
        }
        GlideEngine.getInstance().loadImage(mContext, img, jsonobj.image);
        view.setOnClickListener(v -> {
            showToast(jsonobj.title);
        });
        homeViewLayout.addView(view);
    }

    public void showHome2(Home2Info jsonobj) {
        View view = getView(R.layout.home_item_home2);
        TextView title = (TextView) getView(view, R.id.title);
        ImageView img1 = (ImageView) getView(view, R.id.img1);
        ImageView img2 = (ImageView) getView(view, R.id.img2);
        ImageView img3 = (ImageView) getView(view, R.id.img3);
        if (Utils.isEmpty(jsonobj.title)) {
            title.setText(jsonobj.title);
        } else {
            title.setVisibility(View.GONE);
        }
        GlideEngine.getInstance().loadImage(mContext, img1, jsonobj.square_image);
        GlideEngine.getInstance().loadImage(mContext, img2, jsonobj.rectangle1_image);
        GlideEngine.getInstance().loadImage(mContext, img3, jsonobj.rectangle2_image);
        img1.setOnClickListener(v -> goToType(jsonobj.square_type, jsonobj.square_data));
        img2.setOnClickListener(v -> goToType(jsonobj.rectangle1_type, jsonobj.rectangle1_data));
        img3.setOnClickListener(v -> goToType(jsonobj.rectangle2_type, jsonobj.rectangle2_data));
        homeViewLayout.addView(view);
    }

    public void showHome3(Home3Info jsonobj) {
        View view = getView(R.layout.home_item_home3);
        TextView title = (TextView) getView(view, R.id.title);
        if (Utils.isEmpty(jsonobj.title)) {
            title.setText(jsonobj.title);
            title.setVisibility(View.VISIBLE);
        } else {
            title.setVisibility(View.GONE);
        }
        NoScrollGridView gridView = (NoScrollGridView) getView(view, R.id.gridview);
        CommonAdapter<Home3Info.Item> adapter = new CommonAdapter<Home3Info.Item>(mContext, jsonobj.item, R.layout.home_item_home3_adapteritem) {
            @Override
            public void convert(ViewHolder viewHolder, Home3Info.Item item, int position, View convertView, ViewGroup parentViewGroup) {
                GlideEngine.getInstance().loadImage(mContext, viewHolder.getView(R.id.img), item.image);
            }
        };
        gridView.setOnItemClickListener((a, v, p, i) -> {
            goToType(jsonobj.item.get(p).type, jsonobj.item.get(p).data);
        });
        gridView.setAdapter(adapter);
        homeViewLayout.addView(view);
    }

    public void showHome4(Home4Info jsonobj) {
        View view = getView(R.layout.home_item_home4);
        TextView title = (TextView) getView(view, R.id.title);
        ImageView img1 = (ImageView) getView(view, R.id.img1);
        ImageView img2 = (ImageView) getView(view, R.id.img2);
        ImageView img3 = (ImageView) getView(view, R.id.img3);
        if (Utils.isEmpty(jsonobj.title)) {
            title.setText(jsonobj.title);
            title.setVisibility(View.VISIBLE);
        } else {
            title.setVisibility(View.GONE);
        }
        GlideEngine.getInstance().loadImage(mContext, img1, jsonobj.rectangle1_image);
        GlideEngine.getInstance().loadImage(mContext, img2, jsonobj.rectangle2_image);
        GlideEngine.getInstance().loadImage(mContext, img3, jsonobj.square_image);
        img1.setOnClickListener(v -> goToType(jsonobj.rectangle1_type, jsonobj.rectangle1_data));
        img2.setOnClickListener(v -> goToType(jsonobj.rectangle2_type, jsonobj.rectangle2_data));
        img3.setOnClickListener(v -> goToType(jsonobj.square_type, jsonobj.square_data));
        homeViewLayout.addView(view);
    }

    public void showHome5(Home5Info jsonobj) {
        View view = getView(R.layout.home_item_home5);
        View titlelayout = getView(view, R.id.titlelayout);
        TextView title1 = (TextView) getView(view, R.id.title1);
        TextView title2 = (TextView) getView(view, R.id.title2);
        ImageView img1 = (ImageView) getView(view, R.id.img1);
        ImageView img2 = (ImageView) getView(view, R.id.img2);
        ImageView img3 = (ImageView) getView(view, R.id.img3);
        ImageView img4 = (ImageView) getView(view, R.id.img4);
        GlideEngine.getInstance().loadImage(mContext, img1, jsonobj.square_image);
        GlideEngine.getInstance().loadImage(mContext, img2, jsonobj.rectangle1_image);
        GlideEngine.getInstance().loadImage(mContext, img3, jsonobj.rectangle2_image);
        GlideEngine.getInstance().loadImage(mContext, img4, jsonobj.rectangle3_image);
        img1.setOnClickListener(v -> goToType(jsonobj.square_type, jsonobj.square_data));
        img2.setOnClickListener(v -> goToType(jsonobj.rectangle1_type, jsonobj.rectangle1_data));
        img3.setOnClickListener(v -> goToType(jsonobj.rectangle2_type, jsonobj.rectangle2_data));
        img4.setOnClickListener(v -> goToType(jsonobj.rectangle3_type, jsonobj.rectangle3_data));
        String titlemain = jsonobj.title;
        String titlemain1 = jsonobj.stitle;
        if (!TextUtils.isEmpty(titlemain) || TextUtils.isEmpty(titlemain1)) {
            titlelayout.setVisibility(View.VISIBLE);
            title1.setText(TextUtils.isEmpty(titlemain) ? "" : titlemain);
            title2.setText(TextUtils.isEmpty(titlemain1) ? "" : titlemain1);
        } else {
            titlelayout.setVisibility(View.GONE);
        }
        homeViewLayout.addView(view);
    }


    public void showAdvList(List<Adv_list.Item> jsonobj) {
        if (Utils.isEmpty(jsonobj)) {
            banner.setVisibility(View.VISIBLE);
//                    initHeadImage(advertList);
            //设置banner样式
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            banner.setImages(jsonobj);
            //设置图片加载器
            banner.setImageLoader(new com.youth.banner.loader.ImageLoader() {
                @Override
                public void displayImage(Context context, Object path, ImageView imageView) {
                    GlideEngine.getInstance().loadImage(mContext, imageView, ((Adv_list.Item) path).image);
                }
            });
            //设置banner动画效果
            banner.setBannerAnimation(Transformer.DepthPage);
//                    //设置标题集合（当banner样式有显示title时）
//                    banner.setBannerTitles(titles);
            //设置自动轮播，默认为true
            banner.isAutoPlay(true);
            //设置轮播时间
            banner.setDelayTime(1500);
            //设置指示器位置（当banner模式中有指示器时）
            banner.setIndicatorGravity(BannerConfig.CENTER);
//                    //banner设置方法全部调用完毕时最后调用
            banner.start();
            banner.setOnBannerListener(position -> {
                goToType(jsonobj.get(position).type, jsonobj.get(position).data);
            });
        } else {
            banner.setVisibility(View.GONE);
        }

    }

    public void showVideoView(JSONObject jsonobj) {

    }

    public void showGoods(GoodsInfo jsonobj) {
        View view = getView(R.layout.home_item_goods);
        View titlelayout = getView(view, R.id.titleview);
        TextView title = (TextView) getView(view, R.id.text);
        NoScrollGridView gridView = (NoScrollGridView) getView(view, R.id.gridview);
        if (Utils.isEmpty(jsonobj.title)) {
            title.setText(jsonobj.title);
            titlelayout.setVisibility(View.VISIBLE);
        } else {
            titlelayout.setVisibility(View.GONE);
        }
        gridView.setAdapter(new CommonAdapter<GoodsInfo.Item>(mContext, jsonobj.item, R.layout.home_item_goods_gridview_item) {
            @Override
            public void convert(ViewHolder viewHolder, GoodsInfo.Item item, int position, View convertView, ViewGroup parentViewGroup) {
                GlideEngine.getInstance().loadImage(mContext, viewHolder.getView(R.id.img), item.goods_image);
                viewHolder.setText(R.id.text, item.goods_name);
                viewHolder.setText(R.id.text1, item.goods_promotion_price);
            }
        });
        gridView.setOnItemClickListener((a, v, p, i) -> {
            try {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.GOODS_ID, Utils.getString(jsonobj.item.get(p).goods_id));
                readyGo(GoodsDetailsActivity.class, bundle);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        homeViewLayout.addView(view);
    }

    boolean endThread;

    public void showGoods1(Goods1Bean jsonobj) {
        View view = getView(R.layout.home_item_goods);
        View titlelayout = getView(view, R.id.titleview);
        TextView title = (TextView) getView(view, R.id.text);
        NoScrollGridView gridView = (NoScrollGridView) getView(view, R.id.gridview);
        if (Utils.isEmpty(jsonobj.title)) {
            title.setText(jsonobj.title);
            titlelayout.setVisibility(View.VISIBLE);
        } else {
            titlelayout.setVisibility(View.GONE);
        }
        gridView.setNumColumns(1);
        CommonAdapter<Goods1Bean.Item> adapter = new CommonAdapter<Goods1Bean.Item>(mContext, jsonobj.item, R.layout.home_item_goods1_adapter) {
            @Override
            public void convert(ViewHolder viewHolder, Goods1Bean.Item item, int position, View convertView, ViewGroup parentViewGroup) {
                viewHolder.setText(R.id.title, item.goods_name);
                viewHolder.setImageBitmap(R.id.img, item.goods_image);
                viewHolder.setText(R.id.newprize, "￥" + item.xianshi_price);
                TextView oldprice = viewHolder.getView(R.id.oldprice);
                oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                oldprice.setText("" + item.goods_price);
                viewHolder.setText(R.id.endtime, item.activtime);
            }
        };
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };
        new Thread(() -> {
            while (!endThread) {
                try {
                    Thread.sleep(1000);
                    for (int i = 0; i < jsonobj.item.size(); i++) {
                        //拿到每件商品的时间差，转化为具体的多少天多少小时多少分多少秒
                        //并保存在商品time这个属性内
                        long counttime = jsonobj.item.get(i).time;
                        long days = counttime / (60 * 60 * 24);
                        long hours = (counttime - days * (60 * 60 * 24)) / (60 * 60);
                        long minutes = (counttime - days * (60 * 60 * 24) - hours * (60 * 60)) / (60);
                        long second = (counttime - days * (60 * 60 * 24) - hours * (60 * 60) - minutes * (60));
                        //并保存在商品time这个属性内
                        String finaltime = days + "天" + hours + "时" + minutes + "分" + second + "秒";
                        jsonobj.item.get(i).setActivtime(finaltime);
                        //如果时间差大于1秒钟，将每件商品的时间差减去一秒钟，
                        // 并保存在每件商品的counttime属性内
                        if (counttime > 1) {
                            jsonobj.item.get(i).setTime(counttime - 1);
                        }
                    }
                    Message message = new Message();
                    message.what = 1;
                    //发送信息给handler
                    handler.sendMessage(message);
                } catch (Exception e) {

                }
            }
        }).start();
        gridView.setOnItemClickListener((parent, viewa, position, id) -> {
            Intent intent = new Intent(mContext, GoodsDetailsActivity.class);
            intent.putExtra("goods_id", jsonobj.item.get(position).goods_id);
            startActivity(intent);
        });
        homeViewLayout.addView(view);
    }

    public void showGoods2(Goods2Bean jsonobj) {
        View view = getView(R.layout.home_item_goods);
        View titlelayout = getView(view, R.id.titleview);
        TextView title = (TextView) getView(view, R.id.text);
        NoScrollGridView gridView = (NoScrollGridView) getView(view, R.id.gridview);
        if (Utils.isEmpty(jsonobj.title)) {
            title.setText(jsonobj.title);
            titlelayout.setVisibility(View.VISIBLE);
        } else {
            titlelayout.setVisibility(View.GONE);
        }
        gridView.setNumColumns(1);
        CommonAdapter<Goods2Bean.Item> adapter = new CommonAdapter<Goods2Bean.Item>(mContext, jsonobj.item, R.layout.home_item_goods2_adapter) {
            @Override
            public void convert(ViewHolder viewHolder, Goods2Bean.Item item, int position, View convertView, ViewGroup parentViewGroup) {
                viewHolder.setText(R.id.title, item.goods_name);
                viewHolder.setImageBitmap(R.id.img, item.goods_image);
                viewHolder.setText(R.id.money, "￥" + item.goods_promotion_price);
            }
        };
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        gridView.setOnItemClickListener((parent, views, position, id) -> {
            Intent intent = new Intent(mContext, GoodsDetailsActivity.class);
            intent.putExtra("goods_id", jsonobj.item.get(position).goods_id);
            startActivity(intent);
        });

        homeViewLayout.addView(view);
    }

    public void goToType(String type, String square_data) {
        Utils.GoToType(mContext, type, square_data);
    }

}
