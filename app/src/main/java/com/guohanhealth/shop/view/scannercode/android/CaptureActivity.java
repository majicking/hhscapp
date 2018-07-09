package com.guohanhealth.shop.view.scannercode.android;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.guohanhealth.shop.utils.Logutils;
import com.guohanhealth.shop.view.scannercode.camera.CameraManager;
import com.guohanhealth.shop.view.scannercode.view.ViewfinderView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * 这个activity打开相机，在后台线程做常规的扫描；它绘制了一个结果view来帮助正确地显示条形码，在扫描的时候显示反馈信息，
 * 然后在扫描成功的时候覆盖扫描结果
 */
public final class CaptureActivity extends Activity implements
        SurfaceHolder.Callback, SensorEventListener {

    private static final String TAG = CaptureActivity.class.getSimpleName();

    // 相机控制
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private IntentSource source;
    private Collection<BarcodeFormat> decodeFormats;
    private Map<DecodeHintType, ?> decodeHints;
    private String characterSet;
    // 电量控制
    private InactivityTimer inactivityTimer;
    // 声音、震动控制
    private BeepManager beepManager;

    private ImageButton imageButton_back;
    private SurfaceHolder surfaceHolder;
    private SensorManager mSensorManager;
    private float lux;
    private View views;

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    private Camera camera;

    /**
     * OnCreate中初始化一些辅助类，如InactivityTimer（休眠）、Beep（声音）以及AmbientLight（闪光灯）
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        // 保持Activity处于唤醒状态

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getResources().getIdentifier("captures", "layout", getPackageName()));

        hasSurface = false;

        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        try {
            camera = Camera.open();
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("msg", e.toString());
        }

        imageButton_back = (ImageButton) findViewById(getResources().getIdentifier("capture_imageview_back", "id", getPackageName()));
        imageButton_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(getResources().getIdentifier("photo", "id", getPackageName())).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyStoragePermissions();
            }
        });
        views = findViewById(getResources().getIdentifier("views", "id", getPackageName()));
        views.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashlightUtils();
            }
        });

        mSensorManager = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT); // 获取光线传感器
        if (lightSensor != null) { // 光线传感器存在时
            mSensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL); // 注册事件监听
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            // 获取光线强度
            lux = event.values[0];
            Log.d("msg", "lux : " + lux);
            if (lux < 20) {
                views.setVisibility(View.VISIBLE);

            } else if (lux < 10) {
                if (!isFlashlightOn()) {
                    Closeshoudian();
                }

            } else {
                views.setVisibility(View.GONE);
                if (isFlashlightOn()) {
                    Closeshoudian();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public static final int REQUEST_CODE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 100;

    private void verifyStoragePermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_EXTERNAL_STORAGE);
            } else {
                ChoosePhone();
            }
        } else {
            ChoosePhone();
        }

    }


    private void ChoosePhone() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        Intent wrapperIntent = Intent.createChooser(intent, "选择二维码图片");
        startActivityForResult(wrapperIntent, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ChoosePhone();
                } else {
                    Toast.makeText(this, "相册权限被拒绝", Toast.LENGTH_SHORT).show();
                }
                break;

        }

    }

    //获取地址中的参数 根据key
    private static String getParms(String strURL, String key) {
        try {
            String[] arrSplit = null;
            arrSplit = strURL.split("[?]");
            if (strURL.length() > 1) {
                if (arrSplit.length > 1) {
                    if (arrSplit[1] != null) {
                        arrSplit = arrSplit[1].split("[&]");
                        for (String strSplit : arrSplit) {
                            String[] arrSplitEqual = null;
                            arrSplitEqual = strSplit.split("[=]");
                            if (arrSplitEqual.length > 1) {
                                if (arrSplitEqual[0].equals(key)) {
                                    return arrSplitEqual[1];
                                }
                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private Handler handl = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intents = getIntent();
            String key = (String) msg.obj;
            String rcode = getParms(key, "r_code");
            Logutils.i(key);
            if (msg.what == 1) {
                intents.putExtra("codedContent", key);
                setResult(RESULT_OK, intents);
//                if (key.length() == 13) {
//                    Intent intent = new Intent(CaptureActivity.this, GoodsListFragmentManager.class);
//                    intent.putExtra("barcode", (String) msg.obj);
//                    intent.putExtra("gc_name", (String) msg.obj);
//                    startActivity(intent);
//                } else if (key.indexOf("goods_id") > 0) {
//                    String goodsId = key.substring(key.lastIndexOf("=") + 1);
//                    Intent intent = new Intent(CaptureActivity.this, GoodsDetailsActivity.class);
//                    intent.putExtra("goods_id", goodsId);
//                    startActivity(intent);
//                } else if (!TextUtils.isEmpty(rcode)) {
//                    Intent intent = new Intent(CaptureActivity.this, RegisteredActivity.class);
//                    intent.putExtra("rcode", rcode);
//                    startActivity(intent);
//                } else {
//                    String result = null;
//                    Pattern pattern = Pattern
//                            .compile("(http://|ftp://|https://|www){0,1}[^\u4e00-\u9fa5\\s]*?\\.(com|net|cn|me|tw|fr)[^\u4e00-\u9fa5\\s]*");
//                    // 空格结束
//                    Matcher matcher = pattern
//                            .matcher(key);
//                    while (matcher.find()) {
//                        result = matcher.group(0);
//                    }
//
//                    if (!TextUtils.isEmpty(result) && Uri.parse(result) != null) {
////                        Intent intent = new Intent(Intent.ACTION_VIEW);
////                        intent.setData(Uri.parse(key));
//                        Intent intent = new Intent(CaptureActivity.this, WebViewActivity.class);
//                        intent.putExtra("url", result);
//
//                        startActivity(intent);
//                    } else {
//                        Toast.makeText(CaptureActivity.this, "该平台不支持该二维码", Toast.LENGTH_SHORT).show();
//                    }
//                }
                finish();
            } else if (msg.what == 2) {
                intents.putExtra("error", key);
                setResult(RESULT_OK, intents);
                finish();
            } else {
                intents.putExtra("error", key);
                setResult(RESULT_OK, intents);
                finish();
            }
        }
    };

    //    Handler barHandler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case PARSE_BARCODE_SUC:
//                    //viewfinderView.setRun(false);
//                    String key = (String) msg.obj;
//                    if(key.length() == 13) {
//                        Intent intent = new Intent(com.xinyuangongxiang.shop.bracode.ui.CaptureActivity.this, GoodsListFragmentManager.class);
//                        intent.putExtra("barcode", (String) msg.obj);
//                        intent.putExtra("gc_name", (String) msg.obj);
//                        startActivity(intent);
//                    } else if(key.indexOf("goods_id") > 0) {
//                        String goodsId = key.substring(key.lastIndexOf("=") + 1);
//                        Intent intent = new Intent(com.xinyuangongxiang.shop.bracode.ui.CaptureActivity.this, GoodsDetailsActivity.class);
//                        intent.putExtra("goods_id", goodsId);
//                        startActivity(intent);
//                    } else {
//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        intent.setData(Uri.parse(key));
//                        startActivity(intent);
//                    }
//                    finish();
//                    break;
//                case PARSE_BARCODE_FAIL:
//                    //showDialog((String) msg.obj);
//                    if (mProgress != null && mProgress.isShowing()) {
//                        mProgress.dismiss();
//                    }
//                    new AlertDialog.Builder(com.xinyuangongxiang.shop.bracode.ui.CaptureActivity.this).setTitle("提示").setMessage("扫描失败！").setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    }).show();
//                    break;
//            }
//            super.handleMessage(msg);
//        }
//
//    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE:
                    final Uri uri = data.getData();
                    if (uri != null)
                        try {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Looper.prepare();
                                    new Handler().post(new Runnable() {
                                        @Override
                                        public void run() {

                                            String result = syncDecodeQRCode(getDecodeAbleBitmap(getRealPathFromUri(getApplicationContext(), uri)));
                                            Message ms = new Message();
                                            if (!TextUtils.isEmpty(result) && !"".equals(result)) {
                                                ms.what = 1;
                                                ms.obj = result;
                                                Log.i("msg", result);

                                            } else {
                                                ms.what = 2;
                                                ms.obj = "解析失败，请检查图片格式";
                                            }
                                            handl.sendMessage(ms);

                                        }
                                    });//在子线程中直接去new 一个handler
                                    Looper.loop();

                                }
                            }).start();

                        } catch (Exception e) {
                            e.printStackTrace();
                            Message ms = new Message();
                            ms.what = 2;
                            ms.obj = e.toString();
                            handl.sendMessage(ms);
                        }
                    break;

            }


        }

    }


    /*    *
     * 是否开启了闪光灯
     * @return
     */
    public boolean isFlashlightOn() {
        try {
            Camera.Parameters parameters = camera.getParameters();
            String flashMode = parameters.getFlashMode();
            if (flashMode.equals(Camera.Parameters.FLASH_MODE_TORCH)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 闪光灯开关
     */
    public void flashlightUtils() {
        if (isFlashlightOn()) {
            Closeshoudian();
        } else {
            Openshoudian();
        }
    }

    public void Openshoudian() {
        //异常处理一定要加，否则Camera打开失败的话程序会崩溃
        try {
            camera = Camera.open();
        } catch (Exception e) {
            Log.i("msg", "Camera打开有问题");
        }
        if (camera != null) {
            setIsOpenFlashMode(true);
        }
    }

    public void Closeshoudian() {
        if (camera != null) {
            setIsOpenFlashMode(false);
        }
    }

    public void initCameraParams() {
        try {
            Camera.Parameters parameters = camera.getParameters();
            // 选择合适的预览尺寸
            List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
            if (sizeList.size() > 0) {
                Camera.Size cameraSize = sizeList.get(0);
                //预览图片大小
                parameters.setPreviewSize(cameraSize.width, cameraSize.height);
            }
            //设置生成的图片大小
            sizeList = parameters.getSupportedPictureSizes();
            if (sizeList.size() > 0) {
                Camera.Size cameraSize = sizeList.get(0);
                for (Camera.Size size : sizeList) {
                    //小于100W像素
                    if (size.width * size.height < 100 * 10000) {
                        cameraSize = size;
                        break;
                    }
                }
                parameters.setPictureSize(cameraSize.width, cameraSize.height);
            }
            //设置图片格式
            parameters.setPictureFormat(ImageFormat.JPEG);
            parameters.setJpegQuality(100);
            parameters.setJpegThumbnailQuality(100);
            //自动聚焦模式
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            parameters.setFlashMode(getIsOpenFlashMode());
            //curRotation=getPreviewDegree();
//        screenDirectionListener();
            //Log.e(">>>>>","curRotation:"+curRotation);
            //parameters.setRotation(curRotation);//生成的图片转90°
            //camera.setDisplayOrientation(curRotation);//预览转90°预览图片旋转90°
            //camera.setDisplayOrientation(getPreviewDegree());//预览旋转90°
            camera.setDisplayOrientation(90);
            camera.setParameters(parameters);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "相机出错", Toast.LENGTH_SHORT).show();
        }
    }

    private String isOpenFlashMode = Camera.Parameters.FLASH_MODE_OFF;

    public String getIsOpenFlashMode() {
        return isOpenFlashMode;
    }

    public void setIsOpenFlashMode(boolean mIsOpenFlashMode) {
        if (mIsOpenFlashMode)
            this.isOpenFlashMode = Camera.Parameters.FLASH_MODE_TORCH;
        else
            this.isOpenFlashMode = Camera.Parameters.FLASH_MODE_OFF;
        setCameraPreView();//重新预览相机
    }

    public void setCameraPreView() {
        if (camera != null) {
            camera.stopPreview();
            initCameraParams();
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
            } catch (Exception e) {
                Log.i("msg", "相机转换失败!" + e.getMessage());
            }
        } else {
            Log.i("msg", "相机初始化失败!");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        // CameraManager必须在这里初始化，而不是在onCreate()中。
        // 这是必须的，因为当我们第一次进入时需要显示帮助页，我们并不想打开Camera,测量屏幕大小
        // 当扫描框的尺寸不正确时会出现bug
        cameraManager = new CameraManager(getApplication());
        viewfinderView = (ViewfinderView) findViewById(getResources().getIdentifier("viewfinder_view", "id", getPackageName()));
        viewfinderView.setCameraManager(cameraManager);
        handler = null;
        SurfaceView surfaceView = (SurfaceView) findViewById(getResources().getIdentifier("preview_view", "id", getPackageName()));
        surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            // activity在paused时但不会stopped,因此surface仍旧存在；
            // surfaceCreated()不会调用，因此在这里初始化camera
            initCamera(surfaceHolder);
        } else {
            // 重置callback，等待surfaceCreated()来初始化camera
            surfaceHolder.addCallback(this);
        }

        beepManager.updatePrefs();
        inactivityTimer.onResume();

        source = IntentSource.NONE;
        decodeFormats = null;
        characterSet = null;
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(getResources().getIdentifier("preview_view", "id", getPackageName()));
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    /**
     * 扫描成功，处理反馈信息
     *
     * @param rawResult
     * @param barcode
     * @param scaleFactor
     */
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        inactivityTimer.onActivity();

        boolean fromLiveScan = barcode != null;
        //这里处理解码完成后的结果，此处将参数回传到Activity处理
        if (fromLiveScan) {
            Message message = new Message();
            if (rawResult != null && !TextUtils.isEmpty(rawResult.getText())) {
                beepManager.playBeepSoundAndVibrate();
                message.obj = rawResult.getText();
                message.what = 1;
            } else {
                message.what = 2;
                message.obj = "扫描失败，请重试";
            }
            handl.sendMessage(message);
//            Toast.makeText(this, "扫描成功" + rawResult.getText(), Toast.LENGTH_SHORT).show();
//            Intent intent = getIntent();
//            intent.putExtra("codedContent", rawResult.getText());
//            intent.putExtra("codedBitmap", barcode);
//            setResult(RESULT_OK, intent);
//            finish();
        }

    }

    /**
     * 初始化Camera
     *
     * @param surfaceHolder
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            return;
        }
        try {
            // 打开Camera硬件设备
            cameraManager.openDriver(surfaceHolder);
            // 创建一个handler来打开预览，并抛出一个运行时异常
            if (handler == null) {
                handler = new CaptureActivityHandler(this, decodeFormats,
                        decodeHints, characterSet, cameraManager);
            }
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    /**
     * 显示底层错误信息并退出应用
     */
    private void displayFrameworkBugMessageAndExit() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("提示");
//        builder.setMessage("相机打开失败，请开启权限或重启App");
//        builder.setPositiveButton("OK", new FinishListener(this));
//        builder.setOnCancelListener(new FinishListener(this));
//        builder.show();
    }


    /**
     * 同步解析bitmap二维码。该方法是耗时操作，请在子线程中调用。
     *
     * @param bitmap 要解析的二维码图片
     * @return 返回二维码图片里的内容 或 null
     */
    public static String syncDecodeQRCode(Bitmap bitmap) {
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
            Result result = new MultiFormatReader().decode(new BinaryBitmap(new HybridBinarizer(source)), HINTS);
            return result.getText();
        } catch (Exception e) {
            return null;
        }
    }

    public static final Map<DecodeHintType, Object> HINTS = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);

    {

        List<BarcodeFormat> allFormats = new ArrayList<BarcodeFormat>();
        allFormats.add(BarcodeFormat.AZTEC);
        allFormats.add(BarcodeFormat.CODABAR);
        allFormats.add(BarcodeFormat.CODE_39);
        allFormats.add(BarcodeFormat.CODE_93);
        allFormats.add(BarcodeFormat.CODE_128);
        allFormats.add(BarcodeFormat.DATA_MATRIX);
        allFormats.add(BarcodeFormat.EAN_8);
        allFormats.add(BarcodeFormat.EAN_13);
        allFormats.add(BarcodeFormat.ITF);
        allFormats.add(BarcodeFormat.MAXICODE);
        allFormats.add(BarcodeFormat.PDF_417);
        allFormats.add(BarcodeFormat.QR_CODE);
        allFormats.add(BarcodeFormat.RSS_14);
        allFormats.add(BarcodeFormat.RSS_EXPANDED);
        allFormats.add(BarcodeFormat.UPC_A);
        allFormats.add(BarcodeFormat.UPC_E);
        allFormats.add(BarcodeFormat.UPC_EAN_EXTENSION);
        HINTS.put(DecodeHintType.POSSIBLE_FORMATS, allFormats);
        HINTS.put(DecodeHintType.CHARACTER_SET, "utf-8");
    }


    private static Bitmap getDecodeAbleBitmap(String picturePath) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(picturePath, options);
            int sampleSize = options.outHeight / 400;
            if (sampleSize <= 0)
                sampleSize = 1;
            options.inSampleSize = sampleSize;
            options.inJustDecodeBounds = false;

            return BitmapFactory.decodeFile(picturePath, options);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 根据图片的Uri获取图片的绝对路径(已经适配多种API)
     *
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    public static String getRealPathFromUri(Context context, Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion < 11) {
            // SDK < Api11
            return getRealPathFromUri_BelowApi11(context, uri);
        }
        if (sdkVersion < 19) {
            // SDK > 11 && SDK < 19
            return getRealPathFromUri_Api11To18(context, uri);
        }
        // SDK > 19
        return getRealPathFromUri_AboveApi19(context, uri);
    }

    /**
     * 适配api11-api18,根据uri获取图片的绝对路径
     */
    private static String getRealPathFromUri_Api11To18(Context context, Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};

        CursorLoader loader = new CursorLoader(context, uri, projection, null,
                null, null);
        Cursor cursor = loader.loadInBackground();

        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }

    /**
     * 适配api11以下(不包括api11),根据uri获取图片的绝对路径
     */
    private static String getRealPathFromUri_BelowApi11(Context context, Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection,
                null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }

    /**
     * 适配api19以上,根据uri获取图片的绝对路径
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String getRealPathFromUri_AboveApi19(Context context, Uri uri) {
        String filePath = null;
        String wholeID = DocumentsContract.getDocumentId(uri);

        // 使用':'分割
        String id = wholeID.split(":")[1];

        String[] projection = {MediaStore.Images.Media.DATA};
        String selection = MediaStore.Images.Media._ID + "=?";
        try {
            String[] selectionArgs = {id};

            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                    selection, selectionArgs, null);
            int columnIndex = cursor.getColumnIndex(projection[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }

}
