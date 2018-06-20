package com.majick.guohanhealth.ui.goods.goodsdetailed.fragment.goods;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.majick.guohanhealth.R;
import com.majick.guohanhealth.adapter.CommonAdapter;
import com.majick.guohanhealth.adapter.ViewHolder;
import com.majick.guohanhealth.adapter.ViewPageAdapter;
import com.majick.guohanhealth.app.App;
import com.majick.guohanhealth.base.BaseFragment;
import com.majick.guohanhealth.bean.GoodsDetailedInfo;
import com.majick.guohanhealth.custom.CustomDialog;
import com.majick.guohanhealth.custom.MyViewPager;
import com.majick.guohanhealth.event.OnFragmentInteractionListener;
import com.majick.guohanhealth.utils.Logutils;
import com.majick.guohanhealth.utils.Utils;
import com.majick.guohanhealth.utils.engine.GlideEngine;
import com.majick.guohanhealth.view.NoScrollGridView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.youth.banner.view.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class GoodsFragment extends BaseFragment<GoodsPersenter, GoodsModel> implements GoodsView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.goods_banner)
    BannerViewPager goodsBanner;
    @BindView(R.id.goods_collect)
    ImageView goodsCollect;
    @BindView(R.id.goods_share)
    ImageView goodsShare;
    @BindView(R.id.goods_name1)
    TextView goodsName1;
    @BindView(R.id.goods_name2)
    TextView goodsName2;
    @BindView(R.id.goods_price)
    TextView goodsPrice;
    @BindView(R.id.goods_solenum)
    TextView goodsSolenum;
    @BindView(R.id.goods_loca)
    TextView goodsLoca;
    @BindView(R.id.goods_havegoods)
    TextView goodsHavegoods;
    @BindView(R.id.goods_runmoney)
    TextView goodsRunmoney;
    @BindView(R.id.goods_view_loca)
    LinearLayout goodsViewLoca;
    @BindView(R.id.goods_view_gridview)
    NoScrollGridView goodsViewGridview;
    @BindView(R.id.goods_view_gridview1)
    NoScrollGridView goodsViewGridview1;
    Unbinder unbinder;
    @BindView(R.id.goods_text_selectclass)
    TextView goodsTextSelectclass;
    @BindView(R.id.goods_text_selectdescribe)
    TextView goodsTextSelectdescribe;
    @BindView(R.id.goods_text_percent)
    TextView goodsTextPercent;
    @BindView(R.id.goods_text_percentnum)
    TextView goodsTextPercentnum;
    @BindView(R.id.goods_text_storename)
    TextView goodsTextStorename;
    @BindView(R.id.goods_text_describe1)
    TextView goodsTextDescribe1;
    @BindView(R.id.goods_text_describecore1)
    TextView goodsTextDescribecore1;
    @BindView(R.id.goods_text_describelevel1)
    TextView goodsTextDescribelevel1;
    @BindView(R.id.goods_text_describe2)
    TextView goodsTextDescribe2;
    @BindView(R.id.goods_text_describecore2)
    TextView goodsTextDescribecore2;
    @BindView(R.id.goods_text_describelevel2)
    TextView goodsTextDescribelevel2;
    @BindView(R.id.goods_text_describe3)
    TextView goodsTextDescribe3;
    @BindView(R.id.goods_text_describecore3)
    TextView goodsTextDescribecore3;
    @BindView(R.id.goods_text_describelevel3)
    TextView goodsTextDescribelevel3;
    @BindView(R.id.goods_img_rating)
    ImageView goodsImgRating;
    @BindView(R.id.goods_view_service)
    LinearLayout goodsViewService;
    List<GoodsDetailedInfo.Goods_commend_list> goods_commend_lists;
    @BindView(R.id.goods_view_recommend)
    LinearLayout goodsViewRecommend;
    @BindView(R.id.goods_view_scrollview)
    ScrollView goodsViewScrollview;
    private String mParam1;
    private String mParam2;
    private List<String> imglist;
    private OnFragmentInteractionListener mListener;
    private CommonAdapter<GoodsDetailedInfo.Goods_commend_list> commonAdapter;
    private PagerAdapter pagerAdapter;
    private ShowPagerAdapter showpageradapter;

    public GoodsFragment() {

    }


    public static GoodsFragment newInstance(String param1, String param2) {
        GoodsFragment fragment = new GoodsFragment();
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
        return R.layout.fragment_goods;
    }

    private boolean isshow;

    @Override
    protected void initView(Bundle savedInstanceState) {
        imglist = new ArrayList<>();
        goods_commend_lists = new ArrayList<>();
        pagerAdapter = new PagerAdapter(imglist);
        showpageradapter = new ShowPagerAdapter(imglist);
        goodsBanner.setAdapter(pagerAdapter);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        MyViewPager pager = new MyViewPager(mContext);
        pager.setLayoutParams(layoutParams);
        pager.setAdapter(showpageradapter);

        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pagerAdapter.setOnPagerViewItemClickListener((v, position) -> {
            dialog.show();
            showpageradapter.updataAdapter(imglist);
            Window window = dialog.getWindow();
            window.getDecorView().setPadding(0, 0, 0, 0);
            window.setGravity(Gravity.CENTER);
            window.setContentView(pager);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.FILL_PARENT;
            lp.height = WindowManager.LayoutParams.FILL_PARENT;
            window.setAttributes(lp);
            pager.setCurrentItem(position);
            isshow = !isshow;
        });
        showpageradapter.setOnPagerViewItemClickListener((v, p) -> {
            if (dialog.isShowing())
                dialog.dismiss();
            isshow = !isshow;
        });

        commonAdapter = new CommonAdapter<GoodsDetailedInfo.Goods_commend_list>(mContext, goods_commend_lists, R.layout.goodsdetail_item_recommend) {
            @Override
            public void convert(ViewHolder viewHolder, GoodsDetailedInfo.Goods_commend_list item, int position, View convertView, ViewGroup parentViewGroup) {
                viewHolder.setText(R.id.text1, item.goods_name);
                viewHolder.setText(R.id.text2, item.goods_promotion_price);
                GlideEngine.getInstance().loadImage(mContext, viewHolder.getView(R.id.img), item.goods_image_url);
            }
        };
        goodsViewGridview1.setAdapter(commonAdapter);
        goodsViewGridview1.setOnItemClickListener((a, v, p, i) -> {
            onButtonPressed("goods_id", goods_commend_lists.get(p).goods_id);
        });
        mPresenter.getGoodsDetails(mParam1, App.getApp().getKey());

    }

    interface OnPagerViewItemClickListener {
        void OnItemClick(View v, int position);
    }

    class PagerAdapter extends android.support.v4.view.PagerAdapter {
        List<String> list;
        OnPagerViewItemClickListener listener;

        public void setOnPagerViewItemClickListener(OnPagerViewItemClickListener listener) {
            this.listener = listener;
        }

        public PagerAdapter(List<String> list) {
            this.list = list;
        }

        public void updataAdapter(List<String> list) {
            if (list != null && list.size() > 0) {
                this.list = list;
            } else {
                this.list.clear();
            }
            notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            if (list != null && list.size() > 0) {
                ImageView imageView = new ImageView(mContext);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(params);
                GlideEngine.getInstance().loadImage(mContext, imageView, list.get(position));
                imageView.setOnClickListener(v -> {
                    listener.OnItemClick(v, position);
                });
                container.addView(imageView);
                return imageView;
            } else {
                View view = LayoutInflater.from(mContext).inflate(R.layout.layout_empty, null);
                TextView textView = view.findViewById(R.id.text);
                textView.setText("没有找到该图片");
                container.addView(view);
                return view;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    class ShowPagerAdapter extends android.support.v4.view.PagerAdapter {
        List<String> list;
        OnPagerViewItemClickListener listener;

        public void setOnPagerViewItemClickListener(OnPagerViewItemClickListener listener) {
            this.listener = listener;
        }

        public ShowPagerAdapter(List<String> list) {
            this.list = list;
        }

        public void updataAdapter(List<String> list) {
            if (list != null && list.size() > 0) {
                this.list = list;
            } else {
                this.list.clear();
            }
            notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(params);
            GlideEngine.getInstance().loadImage(mContext, imageView, list.get(position));
            imageView.setOnClickListener(v -> {
                listener.OnItemClick(v, position);
            });
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
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
    public void faild(String msg) {

    }

    @Override
    public void getGoodsDetails(GoodsDetailedInfo info) {
        if (info != null) {
            try {
                new Handler().postDelayed(() -> goodsViewScrollview.fullScroll(ScrollView.FOCUS_UP), 200);
                /**展示图片banner*/
                imglist.clear();
                imglist.addAll(getImageList(info.goods_image));
                imglist.addAll(getImageList(info.goods_image));
                pagerAdapter.updataAdapter(imglist);
                showpageradapter.updataAdapter(imglist);
                /**商品基本信息*/
                goodsName1.setText(Utils.getString(info.goods_info.goods_name));
                goodsName2.setText(Utils.getString(info.goods_info.goods_jingle));
                goodsPrice.setText("￥ " + Utils.getString(info.goods_info.goods_price));
                goodsSolenum.setText("销量：" + Utils.getString(info.goods_info.goods_salenum));
                if (Utils.isEmpty(info.goods_info.goods_grade)) {
                    GlideEngine.getInstance().loadImage(mContext, goodsImgRating, info.goods_info.goods_grade);
                } else {
                    goodsImgRating.setVisibility(View.GONE);
                }
                /**物流信息*/
                goodsLoca.setText(Utils.getString(info.goods_hair_info.area_name));
                goodsHavegoods.setText(Utils.getString(info.goods_hair_info.if_store_cn));
                goodsRunmoney.setText(Utils.getString(info.goods_hair_info.content));
                goodsTextStorename.setText(Utils.getString(info.store_info.store_name));
                /**店铺信息*/
                goodsTextDescribe1.setText(Utils.getString(info.store_info.store_credit.store_servicecredit.text));
                goodsTextDescribe2.setText(Utils.getString(info.store_info.store_credit.store_deliverycredit.text));
                goodsTextDescribe3.setText(Utils.getString(info.store_info.store_credit.store_desccredit.text));
                goodsTextDescribecore1.setText(Utils.getString(info.store_info.store_credit.store_servicecredit.credit));
                goodsTextDescribecore2.setText(Utils.getString(info.store_info.store_credit.store_deliverycredit.credit));
                goodsTextDescribecore3.setText(Utils.getString(info.store_info.store_credit.store_desccredit.credit));
                goodsTextDescribelevel1.setText(Utils.getString(info.store_info.store_credit.store_servicecredit.percent_text));
                goodsTextDescribelevel2.setText(Utils.getString(info.store_info.store_credit.store_deliverycredit.percent_text));
                goodsTextDescribelevel3.setText(Utils.getString(info.store_info.store_credit.store_desccredit.percent_text));
                /**商品评价*/
                goodsTextPercent.setText("好评率 " + Utils.getString(info.goods_evaluate_info.good_percent) + "%");
                goodsTextPercentnum.setText("(" + Utils.getString(info.goods_evaluate_info.all) + "人评价)");
                if (Utils.isEmpty(info.goods_commend_list)) {
                    goods_commend_lists.clear();
                    goods_commend_lists.addAll(info.goods_commend_list);
                    commonAdapter.updataAdapter(goods_commend_lists);
                } else {
                    goodsViewRecommend.setVisibility(View.GONE);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private List<String> getImageList(String info) {
        List<String> imglist = new ArrayList<>();
        if (Utils.isEmpty(info)) {
            String[] strings = info.split(",");
            if (strings != null && strings.length > 0) {
                for (int i = 0; i < strings.length; i++) {
                    imglist.clear();
                    imglist.add(strings[i]);
                }
            }
        }
        return imglist;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
