package com.guohanhealth.shop.ui.main.fragment.mine.address;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.adapter.BaseRecyclerAdapter;
import com.guohanhealth.shop.adapter.BaseViewHolder;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.bean.AddressInfo;
import com.guohanhealth.shop.bean.Area_list;
import com.guohanhealth.shop.custom.CustomPopuWindow;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.ApiService;
import com.guohanhealth.shop.http.RxHelper;
import com.guohanhealth.shop.http.RxManager;
import com.guohanhealth.shop.utils.Utils;
import com.zcw.togglebutton.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AddressAddActivity extends BaseActivity<AddressPersenter, AddressModel> implements AddressView {


    @BindView(R.id.common_toolbar_title_tv)
    TextView commonToolbarTitleTv;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.phonenumber)
    EditText phonenumber;
    @BindView(R.id.area)
    TextView area;
    @BindView(R.id.address)
    EditText address;
    @BindView(R.id.isdefault)
    ToggleButton isdefault;
    @BindView(R.id.saveAddress)
    Button saveAddress;
    private int areaNumber;
    private StringBuffer areaText;
    private TextView areaText1;
    private BaseRecyclerAdapter<Area_list, RecyclerView.ViewHolder> recyclerAdapter;
    String area_id = "-1";
    String city_id = "-1";
    String addressId = "";
    boolean isCheckDefault = false;
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_address_add;
    }

    int type = 0;

    @Override
    protected void initView(Bundle savedInstanceState) {
        type = getIntent().getIntExtra("type", 0);

        if (type == 1) {
            initToolBarNav(commonToolbar, commonToolbarTitleTv, "修改收货地址");
            AddressInfo mAddressInfo = (AddressInfo) getIntent().getSerializableExtra(Constants.DATA);
            if (mAddressInfo != null) {
                name.setText(mAddressInfo.true_name);
                phonenumber.setText(mAddressInfo.mob_phone);
                area.setText(mAddressInfo.area_info);
                area_id = mAddressInfo.area_id;
                city_id = mAddressInfo.city_id;
                addressId = mAddressInfo.address_id;
                address.setText(mAddressInfo.address);
                if (mAddressInfo.is_default.equals("0")) {
                    isdefault.setToggleOff();
                } else {
                    isdefault.setToggleOn();
                }
            }
        } else {
            initToolBarNav(commonToolbar, commonToolbarTitleTv, "新增收货地址");
        }
        area.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
            if (isOpen) {
                imm.hideSoftInputFromWindow(getWindow().getDecorView().findViewById(android.R.id.content).getWindowToken(), 0); //强制隐藏键盘
            }
            showLocationWindow();
        });
        isdefault.setOnToggleChanged(on -> {
            isCheckDefault = on;
        });
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Utils.isEmpty(name)
                        && Utils.isEmpty(phonenumber)
                        && Utils.isEmpty(address)
                        && !area_id.equals("-1")
                        && !city_id.equals("-1")) {
                    saveAddress.setActivated(true);
                } else {
                    saveAddress.setActivated(false);
                }
            }
        };

        saveAddress.setOnClickListener(v -> {
            if (!Utils.isEmpty(name)) {
                showToast("收件人不能为空");
                return;
            }
            if (!Utils.isEmpty(phonenumber)) {
                showToast("联系电话不能为空");
                return;
            }
            if (!Utils.isEmpty(address)) {
                showToast("详细地址不能为空");
                return;
            }
            if (area_id.equals("-1") || city_id.equals("-1")) {
                showToast("请选择地区");
                return;
            }
            mPresenter.editAddressResult(type == 1 ? ApiService.ADDRESS_EDIT : ApiService.ADDRESS_ADD,
                    App.getApp().getKey(),
                    Utils.getEditViewText(name),
                    area_id,
                    city_id,
                    Utils.getTextViewText(area),
                    Utils.getEditViewText(address),
                    Utils.getEditViewText(phonenumber),
                    isCheckDefault ? "1" : "0",
                    addressId
            );
        });
    }


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
                switch (msg.what) {//特别行政区只有2级别
                    case 3:
                        if (msg.obj != null && ((List<Area_list>) msg.obj).size() > 0) {
                            recyclerAdapter.upDataAdapter((List<Area_list>) msg.obj);
                            break;
                        }
                        area.setText(areaText);
                        s.dissmiss();
                        break;
                    default:
                        if (msg.obj == null) {
                            s.dissmiss();
                            area.setText(areaText);
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
                            areaText.append(o.area_name + " ");
                            areaText1.setText(areaText);
                            areaText1.setHint(o.area_id);
                            areaNumber++;
                            getAreaData(o.area_id, handler, areaNumber);
                            area_id = o.area_id;
                            city_id = o.area_id;
                            break;
                        case 3:
                            areaText.append(o.area_name + " ");
                            areaNumber = 1;
                            area.setText(areaText);
                            area.setHint(o.area_id);
                            city_id = o.area_id;
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


    private void setLocationInfo(Message msg) {
        area.setText(areaText);
    }

    @Override
    public void faild(String msg) {
        showToast(msg);
    }

    @Override
    public void getData(Object data) {

    }

    @Override
    public void editAddressResult(Object data) {
        showToast(data);
        finish();
    }
}
