package com.guohanhealth.shop.ui.main.fragment.mine.property.rechargecardbalance;

import android.content.Context;
import android.content.UriMatcher;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseFragment;
import com.guohanhealth.shop.bean.PreInfo;
import com.guohanhealth.shop.event.OnFragmentInteractionListener;
import com.guohanhealth.shop.ui.main.fragment.mine.property.PredepositModel;
import com.guohanhealth.shop.utils.Utils;

import butterknife.BindView;

public class RechargeCardAddFragment extends BaseFragment<RechargeCardPersenter, PredepositModel> implements RechargeCardView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.idcard)
    EditText idcard;
    @BindView(R.id.editcode)
    EditText editcode;
    @BindView(R.id.imgcode)
    ImageView imgcode;
    @BindView(R.id.apply)
    Button apply;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private String code;

    public RechargeCardAddFragment() {
    }

    public static RechargeCardAddFragment newInstance(String param1, String param2) {
        RechargeCardAddFragment fragment = new RechargeCardAddFragment();
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
        return R.layout.fragment_recharge_card_add;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        imgcode.setOnClickListener(v -> mPresenter.getImgCode());
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Utils.isEmpty(idcard) && Utils.isEmpty(editcode)) {
                    apply.setActivated(true);
                } else {
                    apply.setActivated(false);

                }
            }
        };
        idcard.addTextChangedListener(textWatcher);
        editcode.addTextChangedListener(textWatcher);
        apply.setOnClickListener(v -> {
            if (!Utils.isEmpty(idcard)) {
                showToast("请填写充值卡号");
                return;
            }
            if (!Utils.isEmpty(editcode)) {
                showToast("请填写验证码");
                return;
            }
            mPresenter.rechargecardAdd(App.getApp().getKey(), Utils.getEditViewText(idcard), Utils.getEditViewText(editcode), code);
        });
    }

    public void onButtonPressed(String key, String value) {
        if (mListener != null) {
            mListener.doSomeThing(key, value);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getImgCode();
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
        mPresenter.getImgCode();
    }

    @Override
    public void getData(Object info) {
        this.code = (String) info;
        Glide.with(mContext)
                .load(Constants.KEYCODEURL + code)
                .transition(new DrawableTransitionOptions().crossFade(500))
                .into(imgcode);
    }

    @Override
    public void getPreData(PreInfo info) {

    }

    @Override
    public void getResult(String s) {
        if (s.equals("1")) {
            showToast("充值成功！");
            onButtonPressed(Constants.UPDATA, "0");
            onButtonPressed(Constants.UPDATAMONEY, "");
            idcard.setText("");
            editcode.setText("");
        }

    }
}
