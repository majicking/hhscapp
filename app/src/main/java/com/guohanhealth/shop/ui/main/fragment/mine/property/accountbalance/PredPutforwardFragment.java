package com.guohanhealth.shop.ui.main.fragment.mine.property.accountbalance;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseFragment;
import com.guohanhealth.shop.bean.MyAssectInfo;
import com.guohanhealth.shop.bean.PayWayInfo;
import com.guohanhealth.shop.bean.PredepositInfo;
import com.guohanhealth.shop.event.OnFragmentInteractionListener;
import com.guohanhealth.shop.ui.main.fragment.mine.property.PredepositModel;
import com.guohanhealth.shop.ui.main.fragment.mine.property.PredepositPersenter;
import com.guohanhealth.shop.ui.main.fragment.mine.property.PredepositView;
import com.guohanhealth.shop.utils.Logutils;
import com.guohanhealth.shop.utils.Utils;

import butterknife.BindView;

public class PredPutforwardFragment extends BaseFragment<PredepositPersenter, PredepositModel> implements PredepositView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.money)
    EditText money;
    @BindView(R.id.usernumber)
    EditText usernumber;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.phonenumber)
    EditText phonenumber;
    @BindView(R.id.paypwd)
    EditText paypwd;
    @BindView(R.id.apply)
    Button apply;
    @BindView(R.id.payway)
    EditText payway;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    public PredPutforwardFragment() {
    }

    public static PredPutforwardFragment newInstance(String param1, String param2) {
        PredPutforwardFragment fragment = new PredPutforwardFragment();
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
        return R.layout.fragment_pred_putforward;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Utils.isEmpty(money) &&
                        Utils.isEmpty(paypwd) &&
                        Utils.isEmpty(usernumber) &&
                        Utils.isEmpty(username) &&
                        Utils.isEmpty(phonenumber) &&
                        Utils.isEmpty(payway)) {
                    apply.setActivated(true);
                } else {
                    apply.setActivated(false);
                }
            }
        };
        money.addTextChangedListener(watcher);
        payway.addTextChangedListener(watcher);
        paypwd.addTextChangedListener(watcher);
        username.addTextChangedListener(watcher);
        phonenumber.addTextChangedListener(watcher);
        usernumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Utils.getEditViewText(money).length() == 1 && Utils.getEditViewText(money).equals(".")) {
                    money.setText("");
                }
                if (s.toString().indexOf(".") >= 0) {
                    if (Utils.getEditViewText(money).indexOf(".", Utils.getEditViewText(money).indexOf(".") + 1) > 0) {
                        money.setText(Utils.getEditViewText(money).substring(0, Utils.getEditViewText(money).length() - 1));
                        money.setSelection(Utils.getEditViewText(money).length());
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Utils.isEmpty(money) &&
                        Utils.isEmpty(paypwd) &&
                        Utils.isEmpty(usernumber) &&
                        Utils.isEmpty(username) &&
                        Utils.isEmpty(phonenumber) &&
                        Utils.isEmpty(payway)) {
                    apply.setActivated(true);
                } else {
                    apply.setActivated(false);
                }
            }
        });
        apply.setOnClickListener(v -> {
            if (!Utils.isEmpty(Utils.getEditViewText(money))) {
                showToast("请输入提现金额");
                return;
            }
            if (!Utils.isEmpty(Utils.getEditViewText(payway))) {
                showToast("请输入收款方式");
                return;
            }
            if (!Utils.isEmpty(Utils.getEditViewText(usernumber))) {
                showToast("请输入收款账号");
                return;
            }
            if (!Utils.isEmpty(Utils.getEditViewText(username))) {
                showToast("请输入收款人");
                return;
            }
            if (!Utils.isEmpty(Utils.getEditViewText(phonenumber))) {
                showToast("请输入手机号码");
                return;
            }
            if (!Utils.isEmpty(Utils.getEditViewText(paypwd))) {
                showToast("请输入支付密码");
                return;
            }
            Double mPredepoit = 0.0;
            try {
                mPredepoit = Double.valueOf(((PredepositActivity) mContext).predepoit);
            } catch (Exception e) {
                e.printStackTrace();
                Logutils.i(Utils.getErrorString(e));
            }
            if ((Double.valueOf(Utils.getEditViewText(money))) > mPredepoit) {
                showToast("提现金额不能大于账户余额");
                return;
            }

            mPresenter.pdcashAdd(App.getApp().getKey(),
                    Utils.getEditViewText(money),
                    Utils.getEditViewText(payway),
                    Utils.getEditViewText(usernumber),
                    Utils.getEditViewText(username),
                    Utils.getEditViewText(phonenumber),
                    Utils.getEditViewText(paypwd));
        });
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
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void faild(String msg) {
        showToast(msg);
    }


    @Override
    public void getMyAssect(MyAssectInfo info) {

    }

    @Override
    public void getPaymentList(PayWayInfo info) {

    }

    @Override
    public void getData(Object result) {
        PredepositInfo info = (PredepositInfo) result;

        if (info != null && info.status.equals("ok")) {
            showToast("提交成功，请耐心等待");
            onButtonPressed(Constants.BALANCENUMBER, "3");
            onButtonPressed(Constants.UPDATAMONEY, "3");
            money.setText("");
            usernumber.setText("");
            username.setText("");
            payway.setText("");
            paypwd.setText("");
            phonenumber.setText("");

        }
    }


}
