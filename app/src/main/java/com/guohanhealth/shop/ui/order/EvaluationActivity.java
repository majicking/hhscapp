package com.guohanhealth.shop.ui.order;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.custom.CustomPopuWindow;
import com.guohanhealth.shop.custom.RatingBar;
import com.guohanhealth.shop.event.PermissionListener;
import com.guohanhealth.shop.utils.Logutils;
import com.guohanhealth.shop.utils.Utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;


public class EvaluationActivity extends BaseActivity {


    @BindView(R.id.common_toolbar_title_tv)
    TextView commonToolbarTitleTv;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.ratingbar1)
    RatingBar ratingbar1;
    @BindView(R.id.edittext)
    EditText edittext;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.checkview)
    LinearLayout checkview;
    @BindView(R.id.addimg1)
    ImageView addimg1;
    @BindView(R.id.addimg2)
    ImageView addimg2;
    @BindView(R.id.addimg3)
    ImageView addimg3;
    @BindView(R.id.addimg4)
    ImageView addimg4;
    @BindView(R.id.addimg5)
    ImageView addimg5;
    @BindView(R.id.ratingbar2)
    RatingBar ratingbar2;
    @BindView(R.id.ratingbar3)
    RatingBar ratingbar3;
    @BindView(R.id.ratingbar4)
    RatingBar ratingbar4;
    @BindView(R.id.btn)
    Button btn;
    private File output;
    private Uri imageUri;
    private String imagePath;
    private List<ImageView> mList;
    private CustomPopuWindow mPopuWindown;
    private Map<Integer, String> mListpath;
    private String mTimeStamp;
    private String mFileName;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_evaluation;
    }

    private int type;

    @Override
    protected void initView(Bundle savedInstanceState) {
        mList = new ArrayList<>();
        mListpath = new HashMap<>();
        type = getIntent().getIntExtra(Constants.TYPE, 0);
        if (type == 0)
            initToolBarNav(commonToolbar, commonToolbarTitleTv, "订单评价");
        else
            initToolBarNav(commonToolbar, commonToolbarTitleTv, "追加评论");
        checkview.setOnClickListener(v -> {
            checkbox.setChecked(!checkbox.isChecked());
        });
        mList.add(addimg1);
        mList.add(addimg2);
        mList.add(addimg3);
        mList.add(addimg4);
        mList.add(addimg5);
        for (int i = 0; i < mList.size(); i++) {
            checkImg(mList.get(i), i);
        }
        btn.setOnClickListener(v -> {
            if (!Utils.isEmpty(edittext)) {
                showToast("请评论点什么吧！");
                return;
            }
            Logutils.i(ratingbar1.getStarStep());
        });
    }

    int count = 0;

    public void checkImg(ImageView imageView, int index) {
        imageView.setOnClickListener(v -> {
            View view = getView(R.layout.pop_photo);
            mPopuWindown = Utils.getPopuWindown(mContext, view, Gravity.BOTTOM);
            TextView textView = (TextView) getView(view, R.id.text1);
            TextView textView1 = (TextView) getView(view, R.id.text2);
            TextView close = (TextView) getView(view, R.id.close);
            close.setOnClickListener(v1 -> {
                mPopuWindown.dissmiss();
            });
            textView.setOnClickListener(view1 -> {
                count = index;
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
            textView1.setOnClickListener(view2 -> {
                count = index;
                requestRuntimePermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        new PermissionListener() {
                            @Override
                            public void onGranted() {
                                // 使用意图直接调用手机相册
                                Intent intent = new Intent(
                                        Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                // 打开手机相册,设置请求码
                                startActivityForResult(intent, Constants.REQUEST_Album);
                            }

                            @Override
                            public void onDenied(List<String> deniedPermissions) {
                                showToast("读取相册权限");
                            }
                        });
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
        File out = new File(savePath, mFileName);
        Uri uri = Uri.fromFile(out);
        //该照片的绝对路径
        imagePath = savePath + mFileName;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, Constants.REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPopuWindown.dissmiss();
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.REQUEST_CAMERA:
                    try {
                        /**
                         * 该uri就是照片文件夹对应的uri
                         */
                        Bitmap bitmapByPath = Utils.getBitmapByPath(imagePath, 100, 100);
                        if (Utils.isEmpty(mList)) {
                            mList.get(count).setImageBitmap(bitmapByPath);
                            mList.get(count).setPadding(0, 0, 0, 0);
                        }
                        mListpath.put(count, imagePath);
                    } catch (Exception e) {
                        showToast(Utils.getErrorString(e));
                    }
                    Logutils.i(mListpath);
                    break;
                case Constants.REQUEST_Album:
                    try {
//                        /**
//                         * 该uri是上一个Activity返回的
//                         */
                        if (data != null) {
                            Uri uri = data.getData();
                            resizeImage(uri);
                        }
//                        Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
//                        addimg1.setImageBitmap(bit);
//                        mListpath.add(count, uri.getPath());
                    } catch (Exception e) {
                        showToast(Utils.getErrorString(e));
                    }

                    break;
                case Constants.RESIZE_REQUEST_CODE:
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
            if (Utils.isEmpty(mList)) {
                mList.get(count).setPadding(0, 0, 0, 0);
                mList.get(count).setImageDrawable(drawable);
            }
            mListpath.put(count, path);
        }
        Logutils.i(mListpath);
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
