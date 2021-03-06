package com.guohanhealth.shop.ui.goods.goodsdetailed.fragment.goods;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.adapter.BaseRecyclerAdapter;
import com.guohanhealth.shop.adapter.BaseViewHolder;
import com.guohanhealth.shop.adapter.CommonAdapter;
import com.guohanhealth.shop.adapter.ViewHolder;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseFragment;
import com.guohanhealth.shop.bean.Area_list;
import com.guohanhealth.shop.bean.Contractlist;
import com.guohanhealth.shop.bean.GoodsDetailedInfo;
import com.guohanhealth.shop.bean.Goods_hair_info;
import com.guohanhealth.shop.bean.SpecBean;
import com.guohanhealth.shop.custom.CustomPopuWindow;
import com.guohanhealth.shop.custom.MyScrollView;
import com.guohanhealth.shop.custom.MyViewPager;
import com.guohanhealth.shop.event.GoodsDetailsEvent;
import com.guohanhealth.shop.event.OnFragmentInteractionListener;
import com.guohanhealth.shop.event.RxBus;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.RxHelper;
import com.guohanhealth.shop.http.RxManager;
import com.guohanhealth.shop.ui.goods.GoodsModel;
import com.guohanhealth.shop.ui.goods.goodsdetailed.activity.GoodsDetailsActivity;
import com.guohanhealth.shop.utils.Utils;
import com.guohanhealth.shop.utils.engine.GlideEngine;
import com.guohanhealth.shop.view.NoScrollGridView;
import com.youth.banner.view.BannerViewPager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
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
    @BindView(R.id.goods_text_selectclass)
    LinearLayout goodsTextSelectclass;
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
    MyScrollView goodsViewScrollview;
    @BindView(R.id.goods_view_selectclass)
    LinearLayout goodsViewSelectclass;
    @BindView(R.id.goods_view_percent)
    LinearLayout goodsViewPercent;
    Unbinder unbinder1;
    @BindView(R.id.goods_text_service)
    TextView goodsTextService;
    @BindView(R.id.goods_view_gridview_promotion)
    NoScrollGridView goodsViewGridviewPromotion;
    @BindView(R.id.goods_view_promotion)
    LinearLayout goodsViewPromotion;
    private String mParam1;
    private String mParam2;
    private List<String> imglist;
    private OnFragmentInteractionListener mListener;
    private CommonAdapter<GoodsDetailedInfo.Goods_commend_list> commonAdapter;
    private PagerAdapter pagerAdapter;
    private ShowPagerAdapter showpageradapter;
    private BaseRecyclerAdapter recyclerAdapter;
    private TextView areaText1;
    private View orderView;
    private CustomPopuWindow popuWindow;
    private EditText number;
    private String goods_id;
    private String is_virtual;

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
    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    private float downX;    //按下时 的X坐标
    private float downY;    //按下时 的Y坐标
    boolean isButton = false;

    @Override
    protected void initView(Bundle savedInstanceState) {
        goods_id = (String) onButtonPressed(Constants.GETGOODSID, "");
        imglist = new ArrayList<>();
        goods_commend_lists = new ArrayList<>();
        pagerAdapter = new PagerAdapter(imglist);
        showpageradapter = new ShowPagerAdapter(imglist);
        goodsBanner.setAdapter(pagerAdapter);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, ViewGroup.LayoutParams.WRAP_CONTENT);
        MyViewPager pager = new MyViewPager(mContext);
        pager.setLayoutParams(layoutParams);
        pager.setAdapter(showpageradapter);

        final AlertDialog dialog = new AlertDialog.Builder(mContext,R.style.transparentBgDialog).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pagerAdapter.setOnPagerViewItemClickListener((v, position) -> {
            dialog.show();
            showpageradapter.updataAdapter(imglist);
            Window window = dialog.getWindow();
            window.getDecorView().setPadding(0, 0, 0, 0);
            window.setGravity(Gravity.CENTER);
            window.setContentView(pager);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
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
            goods_id = goods_commend_lists.get(p).goods_id;
            onButtonPressed(Constants.GOODS_ID, goods_id);
        });
        goodsViewPercent.setOnClickListener(v -> {
            onButtonPressed(Constants.CURRENTITEM, 2);
        });
        goodsViewLoca.setOnClickListener(v -> {
            showLocationWindow();
        });
        goodsViewSelectclass.setOnClickListener(v -> {
            onButtonPressed(Constants.SHOWORDER, 0);
        });
        ((GoodsDetailsActivity) mContext).setOnChangeGoodsInfoListener((type, data) -> {
            if (type.equals("updata_goods_number")) {
                goodsTextSelectdescribe.setText(Utils.getString(data));
            }
        });
        RxBus.getDefault().register(this, GoodsDetailsEvent.class, info -> {
            goodsdata = info.data;
            getGoodsDetails(Utils.getString(info.data));
        });
        goodsViewScrollview.setOnBottomReachedListener(() -> {
            isButton = true;
        });
        goodsViewScrollview.setOnScrollChangedListener((v, l, t, oldl, oldt) -> {
//            Logutils.i("l=" + l + " t=" + t + " oldl=" + oldl + " oldt=" + oldl);
        });
        goodsViewScrollview.setOnTouchListener((v, event) -> {
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //将按下时的坐标存储
                    downX = x;
                    downY = y;
                    break;
                case MotionEvent.ACTION_UP:

                    //获取到距离差
                    float dx = x - downX;
                    float dy = y - downY;
                    //防止是按下也判断
                    if (Math.abs(dx) > 8 && Math.abs(dy) > 8) {
                        //通过距离差判断方向
                        int orientation = getOrientation(dx, dy);
                        switch (orientation) {
                            case 't':
                                if (isButton) {
                                    onButtonPressed(Constants.CURRENTITEM, 1);
                                }
                                break;
                        }
                    }
            }
            return false;
        });
    }

    /**
     * 根据距离差判断 滑动方向
     *
     * @param dx X轴的距离差
     * @param dy Y轴的距离差
     * @return 滑动的方向
     */
    private int getOrientation(float dx, float dy) {
        if (Math.abs(dx) > Math.abs(dy)) {
            //X轴移动
            return dx > 0 ? 'r' : 'l';
        } else {
            //Y轴移动
            return dy > 0 ? 'b' : 't';
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Utils.isEmpty(goodsdata)) {
            getGoodsDetails(goodsdata);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    int areaNumber;
    StringBuffer areaText;

    /**
     * 地区选择
     */
    public void showLocationWindow() {
        areaNumber = 1;
        areaText = new StringBuffer("");
        View view = LayoutInflater.from(mContext).inflate(R.layout.area_list, null);
        areaText1 = view.findViewById(R.id.text1);
        Button reset = view.findViewById(R.id.reset);
        View nullview = view.findViewById(R.id.nullview);
        RecyclerView recyclerView = view.findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        CustomPopuWindow s = new CustomPopuWindow.PopupWindowBuilder(mContext).setView(view).size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT).create();

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 3:
                        if (msg.obj != null && ((List<Area_list>) msg.obj).size() > 0) {
                            recyclerAdapter.upDataAdapter((List<Area_list>) msg.obj);
                            break;
                        }
                        getAreaInfo(goods_id, areaText1.getHint().toString(), new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                if (msg.what == 4) {
                                    setLocationInfo(msg);
                                }
                            }
                        }, 4);
                        s.dissmiss();
                        break;
                    case 4:
                        setLocationInfo(msg);
                        break;
                    default:
                        if (msg.obj == null) {
                            s.dissmiss();
                            areaText1.setText(areaText);
                        }
                        recyclerAdapter.upDataAdapter((List<Area_list>) msg.obj);
                        break;
                }
            }
        };
        reset.setOnClickListener(v -> {
            areaText1.setText("请选择");
            areaText.delete(0, areaText.length());
            areaNumber = 1;
            getAreaData("0", handler, areaNumber);
        });
        nullview.setOnClickListener(v -> {
            s.dissmiss();
        });

        recyclerAdapter = new BaseRecyclerAdapter<Area_list, RecyclerView.ViewHolder>(mContext, R.layout.area_list_item, new ArrayList<>()) {
            @Override
            public void convert(BaseViewHolder holder, Area_list o) {
                TextView t = holder.getView(R.id.text);
                t.setText(o.area_name);
                t.setHint(o.area_id);
                t.setOnClickListener(v -> {
                    switch (areaNumber) {
                        default:
                            areaText.append(o.area_name);
                            areaText1.setText(areaText);
                            areaText1.setHint(o.area_id);
                            areaNumber++;
                            getAreaData(o.area_id, handler, areaNumber);
                            break;
                        case 3:
                            areaText.append(o.area_name + " ");
                            areaText1.setText(areaText);
                            getAreaInfo(goods_id, o.area_id, handler, 4);
                            areaNumber = 1;
                            s.dissmiss();
                            break;
                    }


                });
            }
        };
        recyclerView.setAdapter(recyclerAdapter);
        s.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null))
                .showAtLocation(view, Gravity.BOTTOM, 0, 0);
        getAreaData("0", handler, areaNumber);
    }

    private void setLocationInfo(Message msg) {
        Goods_hair_info info = (Goods_hair_info) msg.obj;
        goodsLoca.setText(areaText);
        goodsHavegoods.setText(Utils.getString(info.if_store_cn));
        goodsRunmoney.setText(Utils.getString(info.content));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder1 = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder1.unbind();
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

    public Object onButtonPressed(String key, Object value) {
        if (mListener != null) {
            return mListener.doSomeThing(key, value);
        }
        return null;
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

    }

    @Override
    public void faild(String msg) {

    }

    public String goodsdata;

    public void getGoodsDetails(String data) {
        GoodsDetailedInfo info = Utils.getObject(data, GoodsDetailedInfo.class);
        goodsdata = data;
        if (info != null) {
            try {
                goodsViewScrollview.post(() -> {
                    goodsViewScrollview.scrollTo(0, 0);
                });

                is_virtual = Utils.getString(info.goods_info == null ? "0" : Utils.getString(info.goods_info.is_virtual));
                /**展示图片banner*/
                imglist.clear();
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
                /**店铺信息*/
                goodsTextStorename.setText(Utils.getString(info.store_info.store_name));
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

                /**商品服务保证*/
                List<Contractlist> contractlist = new ArrayList<>();
                String ContractStr = Utils.getValue("contractlist", Utils.getValue("goods_info", data));
                if (Utils.isEmpty(ContractStr)) {
                    JSONObject jsonGoods_spec = new JSONObject(ContractStr);
                    Iterator<?> itName = jsonGoods_spec.keys();
                    while (itName.hasNext()) {
                        String specID = itName.next().toString();
                        String specV = jsonGoods_spec.getString(specID);
                        if (info.goods_info.contract_1.equals("1") && specID.equals("1")) {
                            contractlist.add(Utils.getObject(specV, Contractlist.class));
                            continue;
                        }
                        if (info.goods_info.contract_2.equals("1") && specID.equals("2")) {
                            contractlist.add(Utils.getObject(specV, Contractlist.class));
                            continue;
                        }
                        if (info.goods_info.contract_3.equals("1") && specID.equals("3")) {
                            contractlist.add(Utils.getObject(specV, Contractlist.class));
                            continue;
                        }
                        if (info.goods_info.contract_4.equals("1") && specID.equals("4")) {
                            contractlist.add(Utils.getObject(specV, Contractlist.class));
                            continue;
                        }
                        if (info.goods_info.contract_5.equals("1") && specID.equals("5")) {
                            contractlist.add(Utils.getObject(specV, Contractlist.class));
                            continue;
                        }
                        if (info.goods_info.contract_6.equals("1") && specID.equals("6")) {
                            contractlist.add(Utils.getObject(specV, Contractlist.class));
                            continue;
                        }
                        if (info.goods_info.contract_7.equals("1") && specID.equals("7")) {
                            contractlist.add(Utils.getObject(specV, Contractlist.class));
                            continue;
                        }
                        if (info.goods_info.contract_8.equals("1") && specID.equals("8")) {
                            contractlist.add(Utils.getObject(specV, Contractlist.class));
                            continue;
                        }
                        if (info.goods_info.contract_9.equals("1") && specID.equals("9")) {
                            contractlist.add(Utils.getObject(specV, Contractlist.class));
                            continue;
                        }
                        if (info.goods_info.contract_10.equals("1") && specID.equals("10")) {
                            contractlist.add(Utils.getObject(specV, Contractlist.class));
                            continue;
                        }
                    }
                }
                if (Utils.isEmpty(contractlist)) {
                    goodsViewService.setVisibility(View.VISIBLE);
                    goodsTextService.setText("由" + Utils.getString(info.store_info.store_name) + "销售和发货，并享受售后服务");
                    goodsViewGridview.setAdapter(new CommonAdapter<Contractlist>(mContext, contractlist, R.layout.goods_service_item) {
                        @Override
                        public void convert(ViewHolder viewHolder, Contractlist item, int position, View convertView, ViewGroup parentViewGroup) {
                            viewHolder.setText(R.id.text, item.cti_name);
                            GlideEngine.getInstance().loadImage(mContext, viewHolder.getView(R.id.img), item.cti_icon_url_60);
                        }
                    });
                } else {
                    goodsViewService.setVisibility(View.GONE);
                }

                /**选择默认选项*/
                String goods_info = Utils.getValue("goods_info", data);
                String goods_spec = Utils.getValue("goods_spec", goods_info);
                List<SpecBean> defaultList = ((GoodsDetailsActivity) mContext).getSpecList(goods_spec);
                goodsTextSelectclass.removeAllViews();
                if (defaultList != null && defaultList.size() > 0) {
                    for (int i = 0; i < defaultList.size(); i++) {
                        View view = getView(R.layout.spec_item);
                        TextView textView = getView(view, R.id.text);
                        textView.setText(defaultList.get(i).value);
                        textView.setActivated(true);
                        goodsTextSelectclass.addView(view);
                    }
                } else {
                    View view = getView(R.layout.spec_item);
                    TextView textView = getView(view, R.id.text);
                    textView.setText("默认");
                    textView.setActivated(true);
                    goodsTextSelectclass.addView(view);
                }
                /**活动促销*/
                List<GoodsDetailedInfo.Gift_array> gift_arrayList = info.gift_array;

                if (Utils.isEmpty(gift_arrayList)) {
                    goodsViewPromotion.setVisibility(View.VISIBLE);
                    goodsViewGridviewPromotion.setAdapter(new CommonAdapter<GoodsDetailedInfo.Gift_array>(mContext, gift_arrayList, R.layout.goods_order_gifarray_item) {
                        @Override
                        public void convert(ViewHolder viewHolder, GoodsDetailedInfo.Gift_array item, int position, View convertView, ViewGroup parentViewGroup) {
                            TextView textView = viewHolder.getView(R.id.text);
                            textView.setText(item.gift_goodsname + " x" + item.gift_amount);
                            textView.setHint(item.goods_id);
                            GlideEngine.getInstance().loadImage(mContext, viewHolder.getView(R.id.img), item.gift_goodsimage);
                        }
                    });
                    goodsViewGridviewPromotion.setOnItemClickListener((parent, view, position, id) -> {
//                        onButtonPressed(Constants.GOODS_ID, gift_arrayList.get(position).gift_goodsid);
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.GOODS_ID, gift_arrayList.get(position).gift_goodsid);
                        readyGo(GoodsDetailsActivity.class, bundle);
                    });
                } else {
                    goodsViewPromotion.setVisibility(View.GONE);
                }


            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private void getAreaData(String id, Handler handler, int what) {
        new RxManager().add(Api.getDefault().getAreaList(id).compose(RxHelper.handleResult()).subscribe(info -> {

            Message ms = new Message();
            ms.what = what;
            ms.obj = info.area_list;
            handler.sendMessage(ms);
        }, throwable -> {
            showToast(throwable.getMessage());
        }));
    }

    private void getAreaInfo(String goods_id, String id, Handler handler, int what) {
        new RxManager().add(Api.getDefault().getAreaInfo(goods_id, id).compose(RxHelper.handleResult()).subscribe(info -> {
            Message ms = new Message();
            ms.what = what;
            ms.obj = info;
            handler.sendMessage(ms);
        }, throwable -> {
            showToast(throwable.getMessage());
        }));
    }

    private List<String> getImageList(String info) {
        List<String> imglist = new ArrayList<>();
        if (Utils.isEmpty(info)) {
            String[] strings = info.split(",");
            if (strings != null && strings.length > 0) {
                imglist.clear();
                for (int i = 0; i < strings.length; i++) {
                    imglist.add(strings[i]);
                }
            }
        }
        return imglist;
    }


}
