package com.majick.guohanhealth.ui.main.fragment.classtype;

import android.content.Context;
import android.os.Bundle;

import com.majick.guohanhealth.R;
import com.majick.guohanhealth.base.BaseFragment;
import com.majick.guohanhealth.ui.main.fragment.OnFragmentInteractionListener;


public class ClassTypeFragment extends BaseFragment<ClassTypePersenter,ClassTypeModel>implements ClassTypeView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ClassTypeFragment() {
    }

    public static ClassTypeFragment newInstance(String param1, String param2) {
        ClassTypeFragment fragment = new ClassTypeFragment();
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
        return R.layout.fragment_class_type;
    }
    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    public void onButtonPressed(String key, Object value) {
        if (mListener != null) {
            mListener.doSomeThing(key,value);
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

}
