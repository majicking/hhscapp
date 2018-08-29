package com.guohanhealth.shop.ui.order;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.bean.Adv_list;
import com.guohanhealth.shop.bean.ImageFile;
import com.guohanhealth.shop.bean.ReturnBean;
import com.guohanhealth.shop.bean.ReturnFileInfo;
import com.guohanhealth.shop.event.PermissionListener;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.ApiService;
import com.guohanhealth.shop.http.HttpErrorCode;
import com.guohanhealth.shop.utils.Logutils;
import com.guohanhealth.shop.utils.Utils;
import com.guohanhealth.shop.utils.engine.GlideEngine;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReturnOrderActivity extends BaseActivity {


    @BindView(R.id.common_toolbar_title_tv)
    TextView commonToolbarTitleTv;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.store_img)
    ImageView storeImg;
    @BindView(R.id.store_name)
    TextView storeName;
    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.spinner)
    AppCompatSpinner spinner;
    @BindView(R.id.text2)
    TextView text2;
    @BindView(R.id.edit1)
    AppCompatEditText edit1;
    @BindView(R.id.text3)
    TextView text3;
    @BindView(R.id.numberView)
    View numberView;
    @BindView(R.id.edit2)
    AppCompatEditText edit2;
    @BindView(R.id.text4)
    TextView text4;
    @BindView(R.id.edit3)
    AppCompatEditText edit3;
    @BindView(R.id.text5)
    TextView text5;
    @BindView(R.id.addimg1)
    ImageView addimg1;
    @BindView(R.id.addimg2)
    ImageView addimg2;
    @BindView(R.id.addimg3)
    ImageView addimg3;
    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.goods_img)
    ImageView goodsImg;
    @BindView(R.id.goods_name)
    TextView goodsName;
    @BindView(R.id.goods_price)
    TextView goodsPrice;
    @BindView(R.id.goods_number)
    TextView goodsNumber;
    private File mFile;
    private String mFileName;
    private String mTimeStamp;
    private File mFile1;
    private String mFileName1;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_return_order;
    }

    private int type = 1;
    private String order_id;
    private String rec_id;
    private String reason_id;
    private PopupWindow mPopupWindow;
    private Map<Integer, HashMap<String, File>> fileMap;
    private Map<Integer, ImageView> itemImg;
    private Map<Integer, ReturnFileInfo> mListpath;
    private int count;

    @Override
    protected void initView(Bundle savedInstanceState) {
        type = getIntent().getIntExtra(Constants.TYPE, 1);
        order_id = getIntent().getStringExtra(Constants.ORDER_ID);
        rec_id = getIntent().getStringExtra(Constants.REC_ID);
        if (type == 1)
            initToolBarNav(commonToolbar, commonToolbarTitleTv, "商品退款");
        else if (type == 2)
            initToolBarNav(commonToolbar, commonToolbarTitleTv, "商品退货");
        fileMap = new HashMap<>();
        itemImg = new HashMap<>();
        mListpath = new HashMap<>();
        bean = new ReturnBean();
        btn.setOnClickListener(v -> {
            try {
                if (!Utils.isEmpty(edit1) || (Double.valueOf(Utils.getEditViewText(edit1)) > Double.valueOf(bean.goods.goods_pay_price))) {
                    showToast("退款金额不能为空，或不能超过可退金额");
                    return;
                }
                if (type == 2) {
                    if ((!Utils.isEmpty(edit2)) || Double.valueOf(Utils.getEditViewText(edit2)) == 0 || Double.valueOf(bean.goods.goods_num) < Double.valueOf(Utils.getEditViewText(edit2))) {
                        showToast("退货数据不能为空，或不能超过可退数量");
                        return;
                    }
                }
                if (!Utils.isEmpty(edit3)) {
                    showToast("请输入申请说明");
                    return;
                }
                if (!Utils.isEmpty(reason_id)) {
                    showToast("请选择申请原因");
                    return;
                }

                FormBody.Builder builder = new FormBody.Builder();
                builder.add("key", App.getApp().getKey());
                builder.add("order_id", order_id);
                builder.add("order_goods_id", rec_id);
                builder.add("reason_id", reason_id);
                builder.add("refund_amount", Utils.getEditViewText(edit1));
                builder.add("buyer_message", Utils.getEditViewText(edit3));
                builder.add("goods_num", Utils.getEditViewText(edit2));
                builder.add("refund_type", type + "");
                for (HashMap.Entry<Integer, ReturnFileInfo> map : mListpath.entrySet()) {
                    builder.add("refund_pic[" + map.getKey() + "]",  Utils.getString(map.getValue().file_name));
                }
                FormBody formBody = builder.build();
                Api.post(ApiService.REFUND_POST, formBody, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(() -> {
                            showToast(Utils.getErrorString(e));
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String json = response.body().string();

                        try {
                            if (Utils.getCode(json) == HttpErrorCode.HTTP_NO_ERROR) {
                                if (Utils.getDatasString(json).equals("1")) {
                                    runOnUiThread(() -> {
                                        showToast("申请成功");
                                        finish();
                                    });
                                }
                            } else {
                                runOnUiThread(() -> {
                                    showToast(Utils.getErrorString(json));
                                });
                            }
                        } catch (Exception e) {
                            runOnUiThread(() -> {

                                showToast(Utils.getErrorString(e));
                            });
                        }
                    }
                });
            } catch (Exception e) {
                showToast(Utils.getErrorString(e));
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    public void getData() {
        Api.get(ApiService.REFUND_FORM + "&order_id=" + order_id + "&order_goods_id=" + rec_id + "&key=" + App.getApp().getKey()+ "&random=" + new Random().nextInt(10), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    showToast(Utils.getErrorString(e));
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    if (Utils.getCode(json) == HttpErrorCode.HTTP_NO_ERROR) {
                        ReturnBean info = Utils.getObject(Utils.getDatasString(json), ReturnBean.class);
                        if (info != null) {
                            runOnUiThread(() -> {
                                setData(info);
                            });
                        }
                    } else {
                        runOnUiThread(() -> {
                            showToast(Utils.getErrorString(json));
                        });
                    }
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        showToast(Utils.getErrorString(e));
                    });
                }
            }
        });
    }

    ReturnBean bean;

    public void setData(ReturnBean bean) {

        this.bean = bean;
        /**商店名字*/
        storeName.setText(Utils.getString(bean.order.store_name));
        /**商品信息*/
        goodsName.setText(Utils.getString(bean.goods.goods_name));
        goodsPrice.setText(Utils.getString("￥" + bean.goods.goods_price));
        goodsNumber.setText(Utils.getString("×" + bean.goods.goods_num));
        GlideEngine.getInstance().loadImage(mContext, goodsImg, bean.goods.goods_img_360);
        /**退货信息*/
        edit1.setText(Utils.getString(bean.goods.goods_pay_price));
        if (type == 1) {
            numberView.setVisibility(View.GONE);
            text1.setText("退款原因:");
            text2.setText("退款金额:");
            text4.setText("退款说明:");
            text5.setText("退款凭证:");
        } else if (type == 2) {
            numberView.setVisibility(View.VISIBLE);
            edit2.setText(Utils.getString(bean.goods.goods_num));
            text1.setText("退货原因:");
            text2.setText("退货金额:");
            text3.setText("退货数量:");
            text4.setText("退货说明:");
            text5.setText("退货凭证:");
        }

        //设置 Adapter.  其中layout 就是一个TextView
        ArrayAdapter<ReturnBean.ReasonListBean> adapter = new ArrayAdapter<>(mContext, R.layout.text_item, bean.reason_list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reason_id = bean.reason_list.get(position).reason_id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        itemImg.put(1, addimg1);
        itemImg.put(2, addimg2);
        itemImg.put(3, addimg3);
        for (HashMap.Entry<Integer, ImageView> map : itemImg.entrySet()) {
            mListpath.put(map.getKey(), new ReturnFileInfo());
            setImageFile(map.getValue(), map.getKey());

        }
    }


    public void setImageFile(ImageView imageFile, int num) {
        imageFile.setOnClickListener(v -> {
            count = num;
            View view = getView(R.layout.layout_picture_selector);
            mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setAnimationStyle(R.style.popuStyle);
            mPopupWindow.setOnDismissListener(() -> {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1; //0.0-1.0
                getWindow().setAttributes(lp);
            });
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(false);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            if (!mPopupWindow.isShowing()) {
                mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 0.4f; //0.0-1.0
                getWindow().setAttributes(lp);
            }
            Button btn1 = (Button) getView(view, R.id.btn1);
            Button btn2 = (Button) getView(view, R.id.btn2);
            Button btn3 = (Button) getView(view, R.id.btn3);
            btn1.setOnClickListener(v1 -> {
                requestRuntimePermission(new String[]{Manifest.permission.CAMERA},
                        new PermissionListener() {

                            @Override
                            public void onGranted() {
                                getCamera();
                            }

                            @Override
                            public void onDenied(List<String> deniedPermissions) {
                                showToast("拒绝相机权限");
                            }
                        });
            });
            btn2.setOnClickListener(v1 -> {
                requestRuntimePermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        new PermissionListener() {
                            @Override
                            public void onGranted() {
                                // 使用意图直接调用手机相册
                                Intent intent = new Intent(
                                        Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                // 打开手机相册,设置请求码
                                startActivityForResult(intent, Constants.REQUEST_Album);
                            }

                            @Override
                            public void onDenied(List<String> deniedPermissions) {
                                showToast("读取相册权限");
                            }
                        });
            });
            btn3.setOnClickListener(v1 -> {
                if (mPopupWindow != null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
            });
        });
    }

    private void getCamera() {
        String savePath = "";
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/camera/";
            File savedir = new File(savePath);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        }
        // 没有挂载SD卡，无法保存文件
        if (savePath == null || "".equals(savePath)) {
            showToast("无法保存照片，请检查SD卡是否挂载");
            return;
        }
        mTimeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        //照片命名
        mFileName = mTimeStamp + ".jpg";
        mFile = new File(savePath, mFileName);
        Uri uri = Uri.fromFile(mFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, Constants.REQUEST_CAMERA);
    }

    private void uploadMultiFile() {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM)
                .addFormDataPart("key", App.getApp().getKey());
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), mFile1);
        builder.addFormDataPart("refund_pic", mFileName1, fileBody);
        showLoadingDialog("上传...");
        Api.post(ApiService.UPLOAD_PIC, builder.build(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    hideLoadingDialog();
                    showToast(Utils.getErrorString(e));
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                runOnUiThread(() -> {
                    hideLoadingDialog();
                    if (Utils.getCode(json) == HttpErrorCode.HTTP_NO_ERROR) {
                        mListpath.put(count, Utils.getObject(Utils.getDatasString(json), ReturnFileInfo.class));
                    } else {
                        showToast(Utils.getErrorString(json));
                    }
                });
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == MotionEvent.BUTTON_BACK) {
            if (mPopupWindow != null && mPopupWindow.isShowing()) {
                mPopupWindow.isShowing();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.REQUEST_CAMERA:
                    try {
                        resizeImage(Uri.fromFile(mFile));
                    } catch (Exception e) {
                        showToast(Utils.getErrorString(e));
                    }
                    break;
                case Constants.REQUEST_Album:
                    try {
                        if (data != null) {
                            Uri uri = data.getData();
                            resizeImage(uri);
                        }
                    } catch (Exception e) {
                        showToast(Utils.getErrorString(e));
                    }

                    break;
                case Constants.RESIZE_REQUEST_CODE:
                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                    if (data != null) {
                        showResizeImage(data);
                    }
                    break;
            }
        }
    }

    public void resizeImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, Constants.RESIZE_REQUEST_CODE);
    }

    HashMap<String, File> refundpicmap = new HashMap<>();

    private void showResizeImage(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            //            String path = getExternalCacheDir().getAbsolutePath() + File.separator + IMAGE_FILE_NAME;
            String mTimeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            //照片命名
            mFileName1 = mTimeStamp + ".jpg";
            String path = getFilesDir().getPath() + File.separator + mFileName1;
            saveImage(photo, path);
            new BitmapDrawable();
            Drawable drawable = new BitmapDrawable(photo);
            itemImg.get(count).setPadding(1, 1, 1, 1);
            itemImg.get(count).setImageDrawable(drawable);

            mFile1 = new File(path);
            uploadMultiFile();
        }
    }


    public static boolean saveImage(Bitmap photo, String spath) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(spath, false));
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
