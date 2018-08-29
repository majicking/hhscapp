package com.guohanhealth.shop.ui.main.fragment.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
import com.guohanhealth.shop.base.BaseFragment;
import com.guohanhealth.shop.bean.Adv_list;
import com.guohanhealth.shop.bean.Goods1Bean;
import com.guohanhealth.shop.bean.Goods2Bean;
import com.guohanhealth.shop.bean.GoodsInfo;
import com.guohanhealth.shop.bean.Home1Info;
import com.guohanhealth.shop.bean.Home2Info;
import com.guohanhealth.shop.bean.Home3Info;
import com.guohanhealth.shop.bean.Home4Info;
import com.guohanhealth.shop.bean.Home5Info;
import com.guohanhealth.shop.bean.HomeMenuBtn;
import com.guohanhealth.shop.custom.MyGridView;
import com.guohanhealth.shop.custom.MyScrollView;
import com.guohanhealth.shop.event.OnFragmentInteractionListener;
import com.guohanhealth.shop.event.PermissionListener;
import com.guohanhealth.shop.ui.cart.ChatListActivity;
import com.guohanhealth.shop.ui.goods.goodsdetailed.activity.GoodsDetailsActivity;
import com.guohanhealth.shop.ui.main.fragment.home.signin.SignActivity;
import com.guohanhealth.shop.ui.main.fragment.mine.history.HistoryActivity;
import com.guohanhealth.shop.ui.main.fragment.mine.property.PropertyActivity;
import com.guohanhealth.shop.ui.order.OrderActivity;
import com.guohanhealth.shop.ui.search.SearchActivity;
import com.guohanhealth.shop.utils.Utils;
import com.guohanhealth.shop.utils.engine.GlideEngine;
import com.guohanhealth.shop.view.NoScrollGridView;
import com.guohanhealth.shop.view.scannercode.android.CaptureActivity;
import com.scwang.smartrefresh.header.DeliveryHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.json.JSONObject;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends BaseFragment<HomePersenter, HomeModel> implements HomeView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.home_scanner)
    ImageView homeScanner;
    @BindView(R.id.home_view_search)
    LinearLayout homeViewSearch;
    @BindView(R.id.home_view_im)
    LinearLayout homeViewIm;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;
    @BindView(R.id.home_view_layout)
    LinearLayout homeViewLayout;
    @BindView(R.id.gridview)
    MyGridView gridview;
    @BindView(R.id.home_hot)
    TextView homeHot;
    @BindView(R.id.home_view_scrollview)
    MyScrollView homeViewScrollview;
    @BindView(R.id.home_view_titleview)
    LinearLayout homeViewTitleview;


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {

    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        smartrefreshlayout.setRefreshHeader(new DeliveryHeader(mContext));
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setDelayTime(2000);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        mPresenter.getHomeData();
        smartrefreshlayout.setOnRefreshListener(refreshLayout -> {
            homeViewLayout.removeAllViews();
            mPresenter.getHomeData();
        });
        gridview.setAdapter(new CommonAdapter<HomeMenuBtn>(mContext, HomeMenuBtn.getHomeBtn(), R.layout.home_menu_item) {
            @Override
            public void convert(ViewHolder viewHolder, HomeMenuBtn item, int position, View convertView, ViewGroup parentViewGroup) {
                viewHolder.setText(R.id.title, item.title);
                GlideEngine.getInstance().loadImage(mContext, viewHolder.getView(R.id.img), item.icon);
//                Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), item.icon);
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                byte[] datas = baos.toByteArray();
//
//                GlideEngine.getInstance().loadCircleImage(mContext, 180, datas, R.mipmap.djk_icon_member, viewHolder.getView(R.id.img));
//                GradientDrawable mm = (GradientDrawable) viewHolder.getView(R.id.view).getBackground();
//                mm.setColor(item.backgroundCoror);
            }
        });
        gridview.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    onButtonPressed(Constants.MAINNUMBER, 1);
                    break;
                case 1:
                    onButtonPressed(Constants.MAINNUMBER, 2);
                    break;
                case 2:

                    break;
                case 3:
                    readyGo(SignActivity.class);
                    break;
                case 4:
                    onButtonPressed(Constants.MAINNUMBER, 3);
                    break;
                case 5:
                    readyGo(OrderActivity.class);
                    break;
                case 6:
                    readyGo(PropertyActivity.class);
                    break;
                case 7:
                    readyGo(HistoryActivity.class);
                    break;
            }
        });
        homeViewSearch.setOnClickListener(v -> {
            readyGo(SearchActivity.class);
        });
        homeScanner.setOnClickListener(v ->
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
                        })

        );
        homeHot.setText(App.getApp().getHotname());
        homeViewScrollview.setOnScrollListener(scrollY -> {
            if (scrollY < 150) {
                homeViewTitleview.setBackgroundColor(getResources().getColor(R.color.translucent));
            } else if (scrollY > 150 && scrollY < 600) {
                float scale = (float) scrollY / 600;
                float alpha = (255 * scale);
                homeViewTitleview.setBackgroundColor(Color.argb((int) alpha, 237, 89, 104));
            } else {
                homeViewTitleview.setBackgroundColor(getResources().getColor(R.color.appColor));

            }
        });

        homeViewIm.setOnClickListener(v -> {
            readyGo(ChatListActivity.class);
        });
    }


    public void onButtonPressed(String key, int value) {
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
    }

    @Override
    public void showHome1(Home1Info jsonobj) {
        View view = getView(R.layout.home_item_home1);
        View linev = getView(view, R.id.linev);
        View titleview = getView(view, R.id.titleview);
        TextView title = getView(view, R.id.title);
        ImageView img = getView(view, R.id.img);
        if (Utils.isEmpty(jsonobj.title)) {
            title.setText(jsonobj.title);
            titleview.setVisibility(View.VISIBLE);
            linev.setBackgroundColor(Constants.BGCOLORS[new Random().nextInt(10)]);
        } else {
            titleview.setVisibility(View.GONE);
        }
        GlideEngine.getInstance().loadImage(mContext, img, jsonobj.image);
        view.setOnClickListener(v -> {
            goToType(jsonobj.type, jsonobj.data);
        });
        homeViewLayout.addView(view);
    }

    @Override
    public void showHome2(Home2Info jsonobj) {
        View view = getView(R.layout.home_item_home2);
        TextView title = getView(view, R.id.title);
        ImageView img1 = getView(view, R.id.img1);
        ImageView img2 = getView(view, R.id.img2);
        ImageView img3 = getView(view, R.id.img3);
        View linev = getView(view, R.id.linev);
        View titleview = getView(view, R.id.titleview);
        if (Utils.isEmpty(jsonobj.title)) {
            title.setText(jsonobj.title);
            titleview.setVisibility(View.VISIBLE);
            linev.setBackgroundColor(Constants.BGCOLORS[new Random().nextInt(10)]);
        } else {
            titleview.setVisibility(View.GONE);
        }
        GlideEngine.getInstance().loadImage(mContext, img1, jsonobj.square_image);
        GlideEngine.getInstance().loadImage(mContext, img2, jsonobj.rectangle1_image);
        GlideEngine.getInstance().loadImage(mContext, img3, jsonobj.rectangle2_image);
        img1.setOnClickListener(v -> goToType(jsonobj.square_type, jsonobj.square_data));
        img2.setOnClickListener(v -> goToType(jsonobj.rectangle1_type, jsonobj.rectangle1_data));
        img3.setOnClickListener(v -> goToType(jsonobj.rectangle2_type, jsonobj.rectangle2_data));
        homeViewLayout.addView(view);
    }

    @Override
    public void showHome3(Home3Info jsonobj) {
        View view = getView(R.layout.home_item_home3);
        TextView title = getView(view, R.id.title);
        View linev = getView(view, R.id.linev);
        View titleview = getView(view, R.id.titleview);
        if (Utils.isEmpty(jsonobj.title)) {
            title.setText(jsonobj.title);
            titleview.setVisibility(View.VISIBLE);
            linev.setBackgroundColor(Constants.BGCOLORS[new Random().nextInt(10)]);
        } else {
            titleview.setVisibility(View.GONE);
        }
        NoScrollGridView gridView = getView(view, R.id.gridview);
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

    @Override
    public void showHome4(Home4Info jsonobj) {
        View view = getView(R.layout.home_item_home4);
        TextView title = getView(view, R.id.title);
        ImageView img1 = getView(view, R.id.img1);
        ImageView img2 = getView(view, R.id.img2);
        ImageView img3 = getView(view, R.id.img3);
        View linev = getView(view, R.id.linev);
        View titleview = getView(view, R.id.titleview);
        if (Utils.isEmpty(jsonobj.title)) {
            title.setText(jsonobj.title);
            titleview.setVisibility(View.VISIBLE);
            linev.setBackgroundColor(Constants.BGCOLORS[new Random().nextInt(10)]);
        } else {
            titleview.setVisibility(View.GONE);
        }
        GlideEngine.getInstance().loadImage(mContext, img1, jsonobj.rectangle1_image);
        GlideEngine.getInstance().loadImage(mContext, img2, jsonobj.rectangle2_image);
        GlideEngine.getInstance().loadImage(mContext, img3, jsonobj.square_image);
        img1.setOnClickListener(v -> goToType(jsonobj.rectangle1_type, jsonobj.rectangle1_data));
        img2.setOnClickListener(v -> goToType(jsonobj.rectangle2_type, jsonobj.rectangle2_data));
        img3.setOnClickListener(v -> goToType(jsonobj.square_type, jsonobj.square_data));
        homeViewLayout.addView(view);
    }

    @Override
    public void showHome5(Home5Info jsonobj) {
        View view = getView(R.layout.home_item_home5);
        View titlelayout = getView(view, R.id.titlelayout);
        TextView title1 = getView(view, R.id.title1);
        TextView title2 = getView(view, R.id.title2);
        ImageView img1 = getView(view, R.id.img1);
        ImageView img2 = getView(view, R.id.img2);
        ImageView img3 = getView(view, R.id.img3);
        ImageView img4 = getView(view, R.id.img4);
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

    //如果你需要考虑更好的体验，可以这么操作
    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }

    @Override
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

    @Override
    public void showVideoView(JSONObject jsonobj) {

    }

    @Override
    public void showGoods(GoodsInfo jsonobj) {
        View view = getView(R.layout.home_item_goods);
        View titlelayout = getView(view, R.id.titleview);
        TextView title = getView(view, R.id.text);
        NoScrollGridView gridView = getView(view, R.id.gridview);
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

    @Override
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
        gridView.setNumColumns(2);
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

    boolean endThread;

    @Override
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

    @Override
    public void stopRefresh() {
        if (smartrefreshlayout != null && smartrefreshlayout.isRefreshing()) {
            smartrefreshlayout.finishRefresh();
        }
    }


    public void goToType(String type, String square_data) {
        Utils.GoToType(mContext, type, square_data);
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
