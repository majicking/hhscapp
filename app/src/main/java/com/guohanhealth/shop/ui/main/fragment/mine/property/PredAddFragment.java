package com.guohanhealth.shop.ui.main.fragment.mine.property;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseFragment;
import com.guohanhealth.shop.bean.MyAssectInfo;
import com.guohanhealth.shop.bean.PayWayInfo;
import com.guohanhealth.shop.bean.RechargeOrderInfo;
import com.guohanhealth.shop.event.OnFragmentInteractionListener;
import com.guohanhealth.shop.event.RxBus;
import com.guohanhealth.shop.utils.Logutils;
import com.guohanhealth.shop.utils.PayResult;
import com.guohanhealth.shop.utils.Utils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PredAddFragment extends BaseFragment<PredepositPersenter, PredepositModel> implements PredepositView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.rechargenumber)
    EditText rechargenumber;
    @BindView(R.id.recharge)
    Button recharge;
    private String mParam1;
    private String mParam2;
    private PopupWindow payPopupWindow;
    private PopupWindow popuWindow;

    public PredAddFragment() {
    }

    public static PredAddFragment newInstance(String param1, String param2) {
        PredAddFragment fragment = new PredAddFragment();
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
        return R.layout.fragment_pred_add;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        rechargenumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (rechargenumber.getText().toString().length() == 1 && rechargenumber.getText().toString().equals(".")) {
                    rechargenumber.setText("");
                    showToast("前缀不能为小数点");
                }
                if (s.toString().indexOf(".") >= 0) {
                    if (rechargenumber.getText().toString().indexOf(".", rechargenumber.getText().toString().indexOf(".") + 1) > 0) {
                        rechargenumber.setText(rechargenumber.getText().toString().substring(0, rechargenumber.getText().toString().length() - 1));
                        rechargenumber.setSelection(rechargenumber.getText().toString().length());
                        showToast("只能输入一个小数点");
                    }
                }
                if (s.length() > 0) {
                    recharge.setActivated(true);
                } else {
                    recharge.setActivated(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        recharge.setOnClickListener(v -> {
            if (!Utils.isEmpty(rechargenumber)) {
                showToast("请输入充值金额！");
            }
            mPresenter.recharge(App.getApp().getKey(), Utils.getEditViewText(rechargenumber));
        });
    }

    String rechargetext;


    public void onButtonPressed(String key, String value) {
        if (mListener != null) {
            mListener.doSomeThing(key, value);
        }
    }

    Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private OnFragmentInteractionListener mListener;

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void faild(String msg) {
        showToast(msg);
    }

    public void getMyAssect(MyAssectInfo info) {
    }


    @Override
    public void getPaymentList(PayWayInfo info) {
        Logutils.i(info.payment_list.size());

        popuWindow = Utils.shopPayWindown(mContext, info.payment_list, App.getApp().getPay_sn(), "2", v -> {
            popuWindow.dismiss();
            onButtonPressed(Constants.BALANCENUMBER, "2");
        });
        RxBus.getDefault().register(this, PayResult.class, payResult -> {
            Logutils.i(payResult.getResult());
            popuWindow.dismiss();
            if (payResult.getResultStatus().equals("9000")) {
                showToast("支付成功");
                onButtonPressed(Constants.BALANCENUMBER, "1");
                onButtonPressed(Constants.UPDATAMONEY, "");
                return;
            } else if (payResult.getResultStatus().equals("8000")) {
                showToast("支付结果确认中");
            } else if (payResult.getResultStatus().equals("6001")) {
                showToast("支付取消");
            } else {
                showToast("订单支付失败");
            }
            onButtonPressed(Constants.BALANCENUMBER, "2");
        });
        RxBus.getDefault().register(this, BaseResp.class, resp -> {
            if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
                popuWindow.dismiss();
                if (resp.errCode == 0) {
                    showToast("支付成功");
                    onButtonPressed(Constants.BALANCENUMBER, "1");
                    onButtonPressed(Constants.UPDATAMONEY, "");
                    return;
                } else if (resp.errCode == -2) {
                    showToast("取消交易");
                    showToast("支付失败");
                }
                onButtonPressed(Constants.BALANCENUMBER, "2");
            }
        });
    }

    @Override
    public void getData(Object result) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().unRegister(this);
    }


}
