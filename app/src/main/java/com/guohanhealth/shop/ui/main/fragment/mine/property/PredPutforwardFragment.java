package com.guohanhealth.shop.ui.main.fragment.mine.property;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.event.OnFragmentInteractionListener;


import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PredPutforwardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PredPutforwardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.money)
    EditText money;
    @BindView(R.id.type)
    EditText type;
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
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PredPutforwardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PredPutforwardFragment.
     */
    // TODO: Rename and change types and number of parameters
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pred_putforward, container, false);
        unbinder = ButterKnife.bind(this, view);
        money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(money.getText().toString().length() == 1 && money.getText().toString().equals(".")){
                    money.setText("");
                }
                if (s.toString().indexOf(".") >= 0) {
                    if (money.getText().toString().indexOf(".", money.getText().toString().indexOf(".") + 1) > 0) {
                        money.setText(money.getText().toString().substring(0, money.getText().toString().length() - 1));
                        money.setSelection(money.getText().toString().length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }


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

//    @OnClick({R.id.apply})
//    public void apply() {
//        if (TextUtils.isEmpty(money.getText().toString().trim())) {
//            Toast.makeText(context, "请输入提现金额", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(type.getText().toString().trim())) {
//            Toast.makeText(context, "请输入收款方式", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(usernumber.getText().toString().trim())) {
//            Toast.makeText(context, "请输入收款账号", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(username.getText().toString().trim())) {
//            Toast.makeText(context, "请输入收款人", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(phonenumber.getText().toString().trim())) {
//            Toast.makeText(context, "请输入手机号码", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(paypwd.getText().toString().trim())) {
//            Toast.makeText(context, "请输入支付密码", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        String predepoit = ((PredepositActivity) context).predepoit;
//        if (TextUtils.isEmpty(predepoit) || (Double.parseDouble(money.getText().toString().trim())) > Double.parseDouble(predepoit)) {
//            Toast.makeText(context, "提现金额不能大于账户余额", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("key", MyShopApplication.getInstance().getLoginKey());
//        params.put("pdc_amount", money.getText().toString().trim());
//        params.put("pdc_bank_name", type.getText().toString().trim());
//        params.put("pdc_bank_no", usernumber.getText().toString().trim());
//        params.put("pdc_bank_user", username.getText().toString().trim());
//        params.put("mobilenum", phonenumber.getText().toString().trim());
//        params.put("password", paypwd.getText().toString().trim());
//        params.put("key", MyShopApplication.getInstance().getLoginKey());
//        params.put("client", "android");
//        Dialog dialog = ProgressDialog.showLoadingProgress(context, "正在提现申请...");
//        dialog.show();
//        RemoteDataHandler.asyncLoginPostDataString(Constants.PUTFORWARD, params, MyShopApplication.getInstance(), data -> {
//            ProgressDialog.dismissDialog(dialog);
//            String json = data.getJson();
//            if (!TextUtils.isEmpty(json)) {
//                if (data.getCode() == HttpStatus.SC_OK) {
//                    onButtonPressed("success", 3);
//
//                } else {
//                    ShopHelper.showApiError(context, json);
//                }
//            }
//        });
//
//    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
