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
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.bean.EvaluationAgainBean;
import com.guohanhealth.shop.bean.EvaluationInfo;
import com.guohanhealth.shop.bean.ImageFile;
import com.guohanhealth.shop.custom.RatingBar;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;


public class EvaluationActivity extends BaseActivity {


    @BindView(R.id.common_toolbar_title_tv)
    TextView commonToolbarTitleTv;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;

    @BindView(R.id.ratingbar2)
    RatingBar ratingbar2;
    @BindView(R.id.ratingbar3)
    RatingBar ratingbar3;
    @BindView(R.id.ratingbar4)
    RatingBar ratingbar4;
    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.descriptionView)
    LinearLayout descriptionView;
    @BindView(R.id.goodslistview)
    LinearLayout goodslistview;

    private Map<String, List<ImageView>> itemImg = new HashMap<>();
    private Map<Integer, ImageFile> mListpath;
    private String mTimeStamp;
    private String mFileName;
    private Map<Integer, HashMap<String, File>> fileMap;
    private File mFile;
    private PopupWindow mPopupWindow;
    private float rating2 = 5, rating3 = 5, rating4 = 5;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_evaluation;
    }

    private int type;
    private String order_id;

    private Map<String, String> commentCache = new HashMap<>();
    private Map<String, Integer> rateCache = new HashMap<>();
    private Map<String, String> anonyCache = new HashMap<>();
    //商品图片缓存， 第一层标记商品编号，二层标记照片位置，第三层为图片属性
    private Map<String, Map<Integer, ImageFile>> itemImageBean = new HashMap<>();

    @Override
    protected void initView(Bundle savedInstanceState) {
        order_id = getIntent().getStringExtra(Constants.ORDER_ID);
        mListpath = new HashMap<>();
        fileMap = new HashMap<>();
        type = getIntent().getIntExtra(Constants.TYPE, 0);
        if (type == 0) {
            initToolBarNav(commonToolbar, commonToolbarTitleTv, "订单评价");
            setRating();
        } else {
            initToolBarNav(commonToolbar, commonToolbarTitleTv, "追加评论");
        }
        getData(type);
        btn.setOnClickListener(v -> {
            Logutils.i(commentCache);
            Logutils.i(anonyCache);
            Logutils.i(rateCache);
            Logutils.i(itemImageBean);
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("key", App.getApp().getKey());
            builder.add("order_id", order_id);
            if (type==0){
                if (is_own_shop.equals("0")) {
                    builder.add("store_deliverycredit", Math.round(rating2) + "");
                    builder.add("store_desccredit", Math.round(rating3) + "");
                    builder.add("store_servicecredit", Math.round(rating4) + "");
                }
                if (Utils.isEmpty(anonyCache)) {
                    for (HashMap.Entry<String, String> anony : anonyCache.entrySet()) {
                        builder.add("goods[" + anony.getKey() + "][anony]", anony.getValue());
                    }
                }
                if (Utils.isEmpty(rateCache)) {
                    for (HashMap.Entry<String, Integer> rating : rateCache.entrySet()) {
                        builder.add("goods[" + rating.getKey() + "][score]", "" + rating.getValue());

                    }
                }
            }else{

            }

            if (Utils.isEmpty(commentCache)) {
                for (HashMap.Entry<String, String> commont : commentCache.entrySet()) {
                    if (Utils.isEmpty(commont.getValue())) {
                        if (commont.getValue().length() < 10) {
                            showToast("商品评论不能少于10个字");
                            return;
                        }
                        builder.add("goods[" + commont.getKey() + "][comment]", commont.getValue());
                    } else {
                        showToast("请填写商品评论");
                        return;
                    }
                }
            }


            if (Utils.isEmpty(itemImageBean)) {
                for (HashMap.Entry<String, Map<Integer, ImageFile>> map : itemImageBean.entrySet()) {
                    for (HashMap.Entry<Integer, ImageFile> map1 : map.getValue().entrySet()) {
                        builder.add("goods[" + map.getKey() + "][evaluate_image][" + map1.getKey() + "]", Utils.getString(map1.getValue().file_name));
                    }
                }
            }
            FormBody formBody = builder.build();
            Api.post(type==0?ApiService.SAVE:ApiService.SAVE_AGAIN, formBody, new Callback() {
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
                                    showToast("评论成功");
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
        });

    }

    /**
     * 设置星级值
     */
    private void setRating() {
        ratingbar2.setOnRatingChangeListener(ratingCount -> {
            rating2 = ratingCount;
        });
        ratingbar3.setOnRatingChangeListener(ratingCount -> {
            rating3 = ratingCount;
        });
        ratingbar4.setOnRatingChangeListener(ratingCount -> {
            rating4 = ratingCount;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    int count = 0;
    String rec_id;

    public void checkImg(ImageView imageView, int index, String rec) {
        imageView.setOnClickListener(v -> {
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
                count = index;
                rec_id = rec;
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
                count = index;
                rec_id = rec;
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

    public void getData(int type) {
        Api.get((type == 0 ? ApiService.MEMBER_EVALUATE : ApiService.MEMBER_EVALUATE_AGAIN) + "&key=" + App.getApp().getKey() + "&order_id=" + order_id + "&random=" + new Random().nextInt(10), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    showToast(Utils.getErrorString(e));
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String json = response.body().string();
                    if (Utils.getCode(json) == HttpErrorCode.HTTP_NO_ERROR) {
                        runOnUiThread(() -> {
                            if (type == 0) {
                                EvaluationInfo info = Utils.getObject(Utils.getDatasString(json), EvaluationInfo.class);
                                if (info != null) {
                                    setData(info);
                                }
                            } else if (type == 1) {
                                EvaluationAgainBean againBean = Utils.getObject(Utils.getDatasString(json), EvaluationAgainBean.class);
                                if (againBean != null) {
                                    setAgainData(againBean);
                                }
                            }
                        });
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

    String is_own_shop;

    public void setAgainData(EvaluationAgainBean info) {
        goodslistview.removeAllViews();
        descriptionView.setVisibility(View.GONE);
        if (Utils.isEmpty(info.evaluate_goods)) {
            for (int i = 0; i < info.evaluate_goods.size(); i++) {
                int index = i;
                View view = getView(R.layout.evalua_goods_item);
                ImageView imageView = (ImageView) getView(view, R.id.img);
                TextView textView = (TextView) getView(view, R.id.text1);
                TextView textView1 = (TextView) getView(view, R.id.text2);
                EditText edittext = (EditText) getView(view, R.id.edittext);
                RatingBar ratingBar = (RatingBar) getView(view, R.id.ratingbar);
                LinearLayout checkview = (LinearLayout) getView(view, R.id.checkview);
                /**显示评论*/
                textView1.setText(Utils.getString(info.evaluate_goods.get(i).geval_content));
                ratingBar.setVisibility(View.GONE);
                /**隐藏匿名*/
                checkview.setVisibility(View.GONE);
                /**显示图片*/
                GlideEngine.getInstance().loadImage(mContext, imageView, info.evaluate_goods.get(i).geval_goodsimage);
                /**显示名字*/
                textView.setText(info.evaluate_goods.get(i).geval_goodsname);

                /**默认留言*/
                commentCache.put(info.evaluate_goods.get(i).geval_id, edittext.getText().toString().trim());
                edittext.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        commentCache.put(info.evaluate_goods.get(index).geval_id, edittext.getText().toString().trim());
                    }
                });
                ImageView addimg1 = (ImageView) getView(view, R.id.addimg1);
                ImageView addimg2 = (ImageView) getView(view, R.id.addimg2);
                ImageView addimg3 = (ImageView) getView(view, R.id.addimg3);
                ImageView addimg4 = (ImageView) getView(view, R.id.addimg4);
                ImageView addimg5 = (ImageView) getView(view, R.id.addimg5);
                List<ImageView> mList = new ArrayList<>();
                mList.add(addimg1);
                mList.add(addimg2);
                mList.add(addimg3);
                mList.add(addimg4);
                mList.add(addimg5);
                itemImg.put(info.evaluate_goods.get(index).geval_id, mList);
                for (int j = 0; j < mList.size(); j++) {
                    /**默认图片*/
                    mListpath.put(j, new ImageFile());
                    itemImageBean.put(info.evaluate_goods.get(index).geval_id, mListpath);
                    checkImg(mList.get(j), j, info.evaluate_goods.get(index).geval_id);
                }
                goodslistview.addView(view);
            }
        }
    }

    public void setData(EvaluationInfo info) {
        goodslistview.removeAllViews();
        if (info.store_info != null) {
            is_own_shop = info.store_info.is_own_shop;
            if (info.store_info.is_own_shop.equals("0")) {
                descriptionView.setVisibility(View.VISIBLE);
            } else {
                descriptionView.setVisibility(View.GONE);
            }
        }
        if (Utils.isEmpty(info.order_goods)) {

            for (int i = 0; i < info.order_goods.size(); i++) {
                int index = i;
                View view = getView(R.layout.evalua_goods_item);
                ImageView imageView = (ImageView) getView(view, R.id.img);
                TextView textView = (TextView) getView(view, R.id.text1);
                RatingBar ratingBar = (RatingBar) getView(view, R.id.ratingbar);
                /**默认星级*/
                rateCache.put(info.order_goods.get(index).rec_id, 5);
                ratingBar.setOnRatingChangeListener(ratingCount -> {
                    rateCache.put(info.order_goods.get(index).rec_id, Math.round(ratingCount));
                });
                GlideEngine.getInstance().loadImage(mContext, imageView, info.order_goods.get(i).goods_image_url);
                textView.setText(info.order_goods.get(i).goods_name);
                EditText edittext = (EditText) getView(view, R.id.edittext);
                /**默认留言*/
                commentCache.put(info.order_goods.get(i).rec_id, edittext.getText().toString().trim());
                edittext.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        commentCache.put(info.order_goods.get(index).rec_id, edittext.getText().toString().trim());
                    }
                });
                CheckBox checkbox = (CheckBox) getView(view, R.id.checkbox);
                LinearLayout checkview = (LinearLayout) getView(view, R.id.checkview);
                /**默认匿名*/
                anonyCache.put(info.order_goods.get(index).rec_id, "0");
                checkview.setOnClickListener(v -> {
                    checkbox.setChecked(!checkbox.isChecked());
                });
                checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    anonyCache.put(info.order_goods.get(index).rec_id, isChecked ? "1" : "0");
                });
                ImageView addimg1 = (ImageView) getView(view, R.id.addimg1);
                ImageView addimg2 = (ImageView) getView(view, R.id.addimg2);
                ImageView addimg3 = (ImageView) getView(view, R.id.addimg3);
                ImageView addimg4 = (ImageView) getView(view, R.id.addimg4);
                ImageView addimg5 = (ImageView) getView(view, R.id.addimg5);
                List<ImageView> mList = new ArrayList<>();
                mList.add(addimg1);
                mList.add(addimg2);
                mList.add(addimg3);
                mList.add(addimg4);
                mList.add(addimg5);
                itemImg.put(info.order_goods.get(index).rec_id, mList);
                for (int j = 0; j < mList.size(); j++) {
                    /**默认图片*/
                    mListpath.put(j, new ImageFile());
                    itemImageBean.put(info.order_goods.get(index).rec_id, mListpath);
                    checkImg(mList.get(j), j, info.order_goods.get(index).rec_id);
                }
                goodslistview.addView(view);
            }
        }
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
        if (fileMap != null && fileMap.size() > 0) {
            for (HashMap.Entry<Integer, HashMap<String, File>> map : fileMap.entrySet()) {
                for (HashMap.Entry<String, File> maps : (map.getValue()).entrySet()) {
                    RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), maps.getValue());
                    builder.addFormDataPart("file", maps.getKey(), fileBody);
                }
            }
        }
        showLoadingDialog("上传...");
        Api.post(ApiService.FILE_UPLOAD, builder.build(), new Callback() {
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
                        mListpath.put(count, Utils.getObject(Utils.getDatasString(json), ImageFile.class));
                        itemImageBean.put(rec_id, mListpath);
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
                        /**
                         * 该uri就是照片文件夹对应的uri
                         */
//                        Bitmap bitmapByPath = Utils.getBitmapByPath(imagePath, 100, 100);
//                        if (Utils.isEmpty(mList)) {
//                            mList.get(count).setImageBitmap(bitmapByPath);
//                            mList.get(count).setPadding(0, 0, 0, 0);
//                        }
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

    private void showResizeImage(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            //            String path = getExternalCacheDir().getAbsolutePath() + File.separator + IMAGE_FILE_NAME;
            String mTimeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            //照片命名
            String mFileName = mTimeStamp + ".jpg";
            String path = getFilesDir().getPath() + File.separator + mFileName;
            saveImage(photo, path);
            new BitmapDrawable();
            Drawable drawable = new BitmapDrawable(photo);
            itemImg.get(rec_id).get(count).setPadding(0, 0, 0, 0);
            itemImg.get(rec_id).get(count).setImageDrawable(drawable);
            HashMap<String, File> map = new HashMap<>();
            map.put(mFileName, new File(path));
            fileMap.put(count, map);
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
