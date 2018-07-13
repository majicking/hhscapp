package com.guohanhealth.shop.ui.main.fragment.mine;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseFragment;
import com.guohanhealth.shop.bean.MineInfo;
import com.guohanhealth.shop.ui.login.LoginActivity;
import com.guohanhealth.shop.event.OnFragmentInteractionListener;
import com.guohanhealth.shop.ui.main.fragment.mine.setting.SettingActivity;
import com.guohanhealth.shop.ui.order.OrderActivity;
import com.guohanhealth.shop.utils.Logutils;
import com.guohanhealth.shop.utils.Utils;
import com.guohanhealth.shop.utils.engine.GlideEngine;

import butterknife.BindView;


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
                mineBannerView.setBackgroundColor(Constants.RANDOMCOLOR);
        }
    };

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //背景变色
        setbgChange();
        //登陆按钮
        mineBtnviewlogin.setOnClickListener(v -> {
            if (!App.getApp().isLogin() || !Utils.isEmpty(App.getApp().getKey())) {
                readyGo(LoginActivity.class);
            }
        });
        //设置
        mineSetting.setOnClickListener(v -> {
            if (Utils.isLogin(mContext))
                readyGo(SettingActivity.class);
        });
        mineBtnviewOrder.setOnClickListener(v -> toOrder(0));
        mineBtnviewOrder1.setOnClickListener(v -> toOrder(1));
        mineBtnviewOrder2.setOnClickListener(v -> toOrder(2));
        mineBtnviewOrder3.setOnClickListener(v -> toOrder(3));
        mineBtnviewOrder4.setOnClickListener(v -> toOrder(4));
        mineBtnviewOrder5.setOnClickListener(v -> showToast("退货退款"));
    }

    public void toOrder(int index) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.ORDERINDEX, index);
        bundle.putBoolean(Constants.ORDERTYPE, false);
        readyGo(OrderActivity.class,bundle);
    }

    private void setbgChange() {

        thread = new Thread(() -> {
            while (true) {
                SystemClock.sleep(5000);
                handler.sendEmptyMessage(0);
            }
        });
        thread.start();
    }


    @Override
    public void onResume() {
        super.onResume();
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
        GlideEngine.getInstance().loadCircleImage(mContext, 50, R.mipmap.djk_icon_member, mineUserheadimg, info.member_info.avatar);
    }

    @Override
    public void faild(String msg) {
        showToast(msg);
        App.getApp().setIsLogin(false);
    }
}
