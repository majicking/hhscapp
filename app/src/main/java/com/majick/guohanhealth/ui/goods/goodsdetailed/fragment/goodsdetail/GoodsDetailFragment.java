package com.majick.guohanhealth.ui.goods.goodsdetailed.fragment.goodsdetail;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.majick.guohanhealth.R;
import com.majick.guohanhealth.app.Constants;
import com.majick.guohanhealth.base.BaseFragment;
import com.majick.guohanhealth.custom.MyWebView;
import com.majick.guohanhealth.event.OnFragmentInteractionListener;
import com.majick.guohanhealth.http.Api;
import com.majick.guohanhealth.http.ApiService;
import com.majick.guohanhealth.http.HttpErrorCode;
import com.majick.guohanhealth.ui.goods.goodsdetailed.activity.GoodsDetailsActivity;
import com.majick.guohanhealth.utils.JSONParser;
import com.majick.guohanhealth.utils.Logutils;
import com.majick.guohanhealth.utils.Utils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class GoodsDetailFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.webView)
    WebView webView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public GoodsDetailFragment() {


    }

    public static GoodsDetailFragment newInstance(String param1, String param2) {
        GoodsDetailFragment fragment = new GoodsDetailFragment();
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
        return R.layout.fragment_goods_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        ((GoodsDetailsActivity) mContext).setOnChangeGoodsInfoListener((type, data) -> {
            if (type.equals("updata_goods_id")) {
//                getData(((GoodsDetailsActivity)mContext).getGoods_id());
                getData(data);
            }

        });
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
    public void onResume() {
        super.onResume();
        Logutils.i("onResume");
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getData((String) onButtonPressed(Constants.GETGOODSID, ""));
        }
    }

    public void getData(String goods_id) {
        Logutils.i("刷新UI");
        try {
            Api.get(ApiService.GOODS_BODY + "&goods_id=" + goods_id, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Logutils.i(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.code() == HttpErrorCode.HTTP_NO_ERROR) {
                        String data = response.body().string();
                        ((GoodsDetailsActivity) mContext).runOnUiThread(() -> initWebView(data));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initWebView(String goodsBody) {
        String s = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "\t<style type=\"text/css\">\n" +
                "\t\t.img-ks-lazyload{\n" +
                "\t\t\ttext-align: center;\n" +
                "\t\t}\n" +
                "\t\t.img-ks-lazyload img{\n" +
                "\t\t\twidth: 100% !important;\n" +
                "\t\t}\n" +
                "\t</style>\n" +
                "</head>\n" +
                "<body>" +
                "<div class=\"img-ks-lazyload\">" +
                goodsBody +
                "</div>" +
                "</body>\n" +
                "</html>";
        webView.loadDataWithBaseURL(null, s, "text/html", "utf-8", null);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void faild(String msg) {

    }

}
