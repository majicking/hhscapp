package com.guohanhealth.shop.ui.main.fragment.mine;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseFragment;
import com.guohanhealth.shop.bean.MineInfo;
import com.guohanhealth.shop.custom.CustomPopuWindow;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.ApiService;
import com.guohanhealth.shop.http.HttpErrorCode;
import com.guohanhealth.shop.ui.cart.ChatListActivity;
import com.guohanhealth.shop.ui.login.LoginActivity;
import com.guohanhealth.shop.event.OnFragmentInteractionListener;
import com.guohanhealth.shop.ui.main.fragment.mine.address.AddressListActivity;
import com.guohanhealth.shop.ui.main.fragment.mine.collect.CollectActivity;
import com.guohanhealth.shop.ui.main.fragment.mine.history.HistoryActivity;
import com.guohanhealth.shop.ui.main.fragment.mine.property.accountbalance.PredepositActivity;
import com.guohanhealth.shop.ui.main.fragment.mine.property.PropertyActivity;
import com.guohanhealth.shop.ui.main.fragment.mine.property.point.PointActivity;
import com.guohanhealth.shop.ui.main.fragment.mine.property.rechargecardbalance.RechargeCardActivity;
import com.guohanhealth.shop.ui.main.fragment.mine.property.redpack.RedPackActivity;
import com.guohanhealth.shop.ui.main.fragment.mine.property.voucher.VoucherActivity;
import com.guohanhealth.shop.ui.main.fragment.mine.returngoods.ReturnActivity;
import com.guohanhealth.shop.ui.main.fragment.mine.setting.SettingActivity;
import com.guohanhealth.shop.ui.order.OrderActivity;
import com.guohanhealth.shop.utils.Logutils;
import com.guohanhealth.shop.utils.Utils;
import com.guohanhealth.shop.utils.engine.GlideEngine;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.util.Random;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class MineFragment extends BaseFragment<MinePersenter, MineModel> implements MineView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.mine_banner_view)
    RelativeLayout mineBannerView;
    @BindView(R.id.mine_userheadimg)
    ImageView mineUserheadimg;
    @BindView(R.id.mine_username)
    TextView mineUsername;
    @BindView(R.id.mine_btnviewlogin)
    LinearLayout mineBtnviewlogin;
    @BindView(R.id.mine_setting)
    ImageView mineSetting;
    @BindView(R.id.mine_charting)
    ImageView mineCharting;
    @BindView(R.id.mine_btnview_goodscollect)
    LinearLayout mineBtnviewGoodscollect;
    @BindView(R.id.mine_btnview_storecollect)
    LinearLayout mineBtnviewStorecollect;
    @BindView(R.id.mine_btnview_myhistory)
    LinearLayout mineBtnviewMyhistory;
    @BindView(R.id.mine_btnview_order)
    LinearLayout mineBtnviewOrder;
    @BindView(R.id.mine_order_num_1)
    TextView mineOrderNum1;
    @BindView(R.id.mine_btnview_order1)
    RelativeLayout mineBtnviewOrder1;
    @BindView(R.id.mine_order_num_2)
    TextView mineOrderNum2;
    @BindView(R.id.mine_btnview_order2)
    RelativeLayout mineBtnviewOrder2;
    @BindView(R.id.mine_order_num_3)
    TextView mineOrderNum3;
    @BindView(R.id.mine_btnview_order3)
    RelativeLayout mineBtnviewOrder3;
    @BindView(R.id.mine_order_num_4)
    TextView mineOrderNum4;
    @BindView(R.id.mine_btnview_order4)
    RelativeLayout mineBtnviewOrder4;
    @BindView(R.id.mine_order_num_5)
    TextView mineOrderNum5;
    @BindView(R.id.mine_btnview_order5)
    RelativeLayout mineBtnviewOrder5;
    @BindView(R.id.mine_btnview_mymoney)
    LinearLayout mineBtnviewMymoney;
    @BindView(R.id.mine_btnview_mymoney1)
    RelativeLayout mineBtnviewMymoney1;
    @BindView(R.id.mine_btnview_mymoney2)
    RelativeLayout mineBtnviewMymoney2;
    @BindView(R.id.mine_btnview_mymoney3)
    RelativeLayout mineBtnviewMymoney3;
    @BindView(R.id.mine_btnview_mymoney4)
    RelativeLayout mineBtnviewMymoney4;
    @BindView(R.id.mine_btnview_mymoney5)
    RelativeLayout mineBtnviewMymoney5;
    @BindView(R.id.mine_btnview_distribution)
    LinearLayout mineBtnviewDistribution;
    @BindView(R.id.mine_btnview_distribution1)
    RelativeLayout mineBtnviewDistribution1;
    @BindView(R.id.mine_btnview_distribution2)
    RelativeLayout mineBtnviewDistribution2;
    @BindView(R.id.mine_btnview_distribution3)
    RelativeLayout mineBtnviewDistribution3;
    @BindView(R.id.mine_btnview_distribution4)
    RelativeLayout mineBtnviewDistribution4;
    @BindView(R.id.mine_btnview_distribution5)
    RelativeLayout mineBtnviewDistribution5;
    @BindView(R.id.mine_btnview_address)
    LinearLayout mineBtnviewAddress;
    @BindView(R.id.mine_btnview_setting)
    LinearLayout mineBtnviewSetting;
    @BindView(R.id.mine_btnview_twocode)
    LinearLayout mineBtnviewTwocode;
    @BindView(R.id.mine_member_vip)
    TextView mineMemberVip;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Thread thread;

    public MineFragment() {
    }

    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mineBannerView != null)
                mineBannerView.setBackgroundColor(Constants.BGCOLORS[new Random().nextInt(10)]);
        }
    };

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        /**登陆按钮*/
        mineBtnviewlogin.setOnClickListener(v -> {
            if (!App.getApp().isLogin() || !Utils.isEmpty(App.getApp().getKey())) {
                readyGo(LoginActivity.class);
            }
        });
        /**设置*/
        mineSetting.setOnClickListener(v -> {
            if (Utils.isLogin(mContext))
                readyGo(SettingActivity.class);
//            throw new NullPointerException();
        });
        /**订单*/
        mineBtnviewOrder.setOnClickListener(v -> toOrder(0));
        mineBtnviewOrder1.setOnClickListener(v -> toOrder(1));
        mineBtnviewOrder2.setOnClickListener(v -> toOrder(2));
        mineBtnviewOrder3.setOnClickListener(v -> toOrder(3));
        mineBtnviewOrder4.setOnClickListener(v -> toOrder(4));
        /**退货退款*/
        mineBtnviewOrder5.setOnClickListener(v -> readyGo(ReturnActivity.class));
        /**财产*/
        mineBtnviewMymoney.setOnClickListener(v -> readyGo(PropertyActivity.class));
        mineBtnviewMymoney1.setOnClickListener(v -> readyGo(PredepositActivity.class));
        mineBtnviewMymoney2.setOnClickListener(v -> readyGo(RechargeCardActivity.class));
        mineBtnviewMymoney3.setOnClickListener(v -> readyGo(VoucherActivity.class));
        mineBtnviewMymoney4.setOnClickListener(v -> readyGo(RedPackActivity.class));
        mineBtnviewMymoney5.setOnClickListener(v -> readyGo(PointActivity.class));
        /**地址管理*/
        mineBtnviewAddress.setOnClickListener(v -> readyGo(AddressListActivity.class));

        mineBtnviewTwocode.setOnClickListener(v -> {
            View view = getView(R.layout.twocode);
            AVLoadingIndicatorView loadingIndicatorView = view.findViewById(R.id.loading);

            ImageView imageView = getView(view, R.id.img);
            CustomPopuWindow popupWindow = Utils.getPopuWindown(mContext, view, Gravity.BOTTOM);
            Api.get(ApiService.RECOMMEND_QR + App.getApp().getKey(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    mActivity.runOnUiThread(() -> {
                        showToast(Utils.getErrorString(e));
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = response.body().string();
                    try {
                        if (Utils.getCode(json) == HttpErrorCode.HTTP_NO_ERROR) {
                            mActivity.runOnUiThread(() -> {
                                GlideEngine.getInstance().loadImage(mContext, imageView, Utils.getValue("recommend_qr", Utils.getDatasString(json)));
                                loadingIndicatorView.setVisibility(View.GONE);
                            });
                        } else {
                            mActivity.runOnUiThread(() -> {
                                showToast(Utils.getErrorString(json));
                            });
                        }
                    } catch (Exception e) {
                        mActivity.runOnUiThread(() -> {
                            showToast(Utils.getErrorString(e));
                        });
                    }
                }
            });
        });
        /**下面设置*/
        mineBtnviewSetting.setOnClickListener(v -> readyGo(SettingActivity.class));
        /**收藏*/
        mineBtnviewGoodscollect.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.TYPE, 0);
            readyGo(CollectActivity.class, bundle);
        });
        mineBtnviewStorecollect.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.TYPE, 1);
            readyGo(CollectActivity.class, bundle);
        });
        mineBtnviewMyhistory.setOnClickListener(v -> readyGo(HistoryActivity.class));
        mineCharting.setOnClickListener(v -> readyGo(ChatListActivity.class));
    }

    public void toOrder(int index) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.ORDERINDEX, index);
        bundle.putBoolean(Constants.ORDERTYPE, false);
        readyGo(OrderActivity.class, bundle);
    }


    private void setbgChange() {

        thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                SystemClock.sleep(5000);
                handler.sendEmptyMessage(0);
            }
        });
        thread.start();
    }


    @Override
    public void onResume() {
        super.onResume();
        //背景变色
        setbgChange();
        /**每次进入检测个人信息和订单信息*/
        setLoginInfo();
    }


    /**
     * 检测是否登录
     */
    public void setLoginInfo() {
        String loginKey = App.getApp().getKey();
        if (Utils.isEmpty(loginKey)) {
            mPresenter.getMineInfo(loginKey);
        } else {
            mineUsername.setText("点击登陆");
            mineMemberVip.setVisibility(View.GONE);
            GlideEngine.getInstance().loadCircleImage(mContext, 50, R.mipmap.djk_icon_member, mineUserheadimg, "");
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        thread.interrupt();//中断线程切换背景颜色
    }

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
    public void getMineInfo(MineInfo info) {
        Logutils.i(info.toString());
        App.getApp().setIsLogin(true);
        mineUsername.setText(info.member_info.user_name);
        mineMemberVip.setVisibility(View.VISIBLE);
        mineMemberVip.setText(info.member_info.level_name);
        GlideEngine.getInstance().loadCircleImage(mContext, 80, R.mipmap.djk_icon_member, mineUserheadimg, info.member_info.avatar);
        if (!Utils.getString(info.member_info.order_nopay_count).equals("0")) {
            mineOrderNum1.setVisibility(View.VISIBLE);
            mineOrderNum1.setText(info.member_info.order_nopay_count);
        } else {
            mineOrderNum1.setVisibility(View.GONE);
        }
        if (!Utils.getString(info.member_info.order_noreceipt_count).equals("0")) {
            mineOrderNum2.setVisibility(View.VISIBLE);
            mineOrderNum2.setText(info.member_info.order_noreceipt_count);
        } else {
            mineOrderNum2.setVisibility(View.GONE);
        }
        if (!Utils.getString(info.member_info.order_notakes_count).equals("0")) {
            mineOrderNum3.setVisibility(View.VISIBLE);
            mineOrderNum3.setText(info.member_info.order_notakes_count);
        } else {
            mineOrderNum3.setVisibility(View.GONE);
        }
        if (!Utils.getString(info.member_info.order_noeval_count).equals("0")) {
            mineOrderNum4.setVisibility(View.VISIBLE);
            mineOrderNum4.setText(info.member_info.order_noeval_count);
        } else {
            mineOrderNum4.setVisibility(View.GONE);
        }
        if (!Utils.getString(info.member_info.returns).equals("0")) {
            mineOrderNum5.setVisibility(View.VISIBLE);
            mineOrderNum5.setText(info.member_info.returns);
        } else {
            mineOrderNum5.setVisibility(View.GONE);
        }
    }

    @Override
    public void faild(String msg) {
        showToast(msg);
        App.getApp().setIsLogin(false);
    }
}
