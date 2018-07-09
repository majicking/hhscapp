package com.guohanhealth.shop.ui.main.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.ShapeBadgeItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;
import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.base.BaseAppManager;
import com.guohanhealth.shop.base.BaseFragment;
import com.guohanhealth.shop.bean.CartNumberInfo;
import com.guohanhealth.shop.event.OnFragmentInteractionListener;
import com.guohanhealth.shop.event.RxBus;
import com.guohanhealth.shop.ui.main.fragment.cart.CartFragment;
import com.guohanhealth.shop.ui.main.fragment.classtype.ClassTypeFragment;
import com.guohanhealth.shop.ui.main.fragment.home.HomeFragment;
import com.guohanhealth.shop.ui.main.fragment.mine.MineFragment;
import com.guohanhealth.shop.utils.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.BindView;

import static com.guohanhealth.shop.app.Constants.MAINNUMBER;

public class MainActivity extends BaseActivity<MainPersenter, MainModel> implements OnFragmentInteractionListener, MainView {
    @BindView(R.id.fragment_group)
    FrameLayout fragmentGroup;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private ArrayList<BaseFragment> mFragments;
    private int mLastFgIndex;//最后显示的fragment
    private HomeFragment homeFragment1;
    private ClassTypeFragment homeFragment2;
    private CartFragment homeFragment3;
    private MineFragment homeFragment4;
    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;
    private TextBadgeItem mTextBadgeItem;
    private ShapeBadgeItem mShapeBadgeItem;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        homeFragment1 = HomeFragment.newInstance("0", "");
        homeFragment2 = ClassTypeFragment.newInstance("1", "");
        homeFragment3 = CartFragment.newInstance("2", "");
        homeFragment4 = MineFragment.newInstance("3", "");
        mFragments = new ArrayList<>();
        mFragments.add(homeFragment1);
        mFragments.add(homeFragment2);
        mFragments.add(homeFragment3);
        mFragments.add(homeFragment4);
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

        mTextBadgeItem = new TextBadgeItem()
                .setBorderWidth(1)
                .setBackgroundColorResource(R.color.colorAccent)
                .setAnimationDuration(200)
                .setText("0")
                .setGravity(Gravity.TOP | Gravity.END)
                .setHideOnSelect(true);

        mShapeBadgeItem = new ShapeBadgeItem()
                .setShapeColorResource(R.color.colorPrimary)
                .setGravity(Gravity.TOP | Gravity.END)
                .setHideOnSelect(false);

        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar //值得一提，模式跟背景的设置都要在添加tab前面，不然不会有效果。
                .setActiveColor(R.color.deep_red);//选中颜色 图标和文字
//                .setInActiveColor("#8e8e8e")//默认未选择颜色
//                .setBarBackgroundColor(R.color.white);//默认背景色
        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.main_index_my_home_p, "首页")
                                .setInactiveIcon(ContextCompat.getDrawable(MainActivity.this, R.mipmap.main_index_my_home_n))
//                        .setBadgeItem(mShapeBadgeItem)
                )
                .addItem(new BottomNavigationItem(R.mipmap.main_index_my_class_p, "分类")
                        .setInactiveIcon(ContextCompat.getDrawable(MainActivity.this, R.mipmap.main_index_my_class_n)))
                .addItem(new BottomNavigationItem(R.mipmap.main_index_my_cart_p, "购物车")
                        .setInactiveIcon(ContextCompat.getDrawable(MainActivity.this, R.mipmap.main_index_my_cart_n))
                        .setBadgeItem(mTextBadgeItem))
                .addItem(new BottomNavigationItem(R.mipmap.member_b_2, "我的")
                        .setInactiveIcon(ContextCompat.getDrawable(MainActivity.this, R.mipmap.member_b)))
                .setFirstSelectedPosition(getIntent().getIntExtra(MAINNUMBER, 0))//设置默认选择的按钮
                .initialise();//所有的设置需在调用该方法前完成

        setBottomNavigationItem(mBottomNavigationBar, 8, 20, 13);

        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switchFragment(position);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
        initData();
    }


    //刷新ui
    private void initData() {
        switchFragment(getIntent().getIntExtra(MAINNUMBER, 0));
        RxBus.getDefault().register(this, CartNumberInfo.class, v -> {
            if (!Utils.isEmpty(App.getApp().getKey()) || !App.getApp().isLogin()) {
                if (mTextBadgeItem != null) {
                    mTextBadgeItem.setText(Utils.getString("0"));
                }
            } else {
                getCardNumber();
            }
        });
    }

    public void getCardNumber() {
        mPresenter.getCardNumber(App.getApp().getKey());
    }


    @Override
    protected void onResume() {
        super.onResume();
        getCardNumber();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().unRegister(this);
    }

    /**
     * @param bottomNavigationBar，需要修改的 BottomNavigationBar
     * @param space                     图片与文字之间的间距
     * @param imgLen                    单位：dp，图片大小，应 <= 36dp
     * @param textSize                  单位：dp，文字大小，应 <= 20dp
     *                                  <p>
     *                                  使用方法：直接调用setBottomNavigationItem(bottomNavigationBar, 6, 26, 10);
     *                                  代表将bottomNavigationBar的文字大小设置为10dp，图片大小为26dp，二者间间距为6dp
     **/

    private void setBottomNavigationItem(BottomNavigationBar bottomNavigationBar, int space, int imgLen, int textSize) {
        Class barClass = bottomNavigationBar.getClass();
        Field[] fields = barClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            if (field.getName().equals("mTabContainer")) {
                try {
                    //反射得到 mTabContainer
                    LinearLayout mTabContainer = (LinearLayout) field.get(bottomNavigationBar);
                    for (int j = 0; j < mTabContainer.getChildCount(); j++) {
                        //获取到容器内的各个Tab
                        View view = mTabContainer.getChildAt(j);
                        //获取到Tab内的各个显示控件
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(56));
                        FrameLayout container = (FrameLayout) view.findViewById(R.id.fixed_bottom_navigation_container);
                        container.setLayoutParams(params);
                        container.setPadding(dip2px(12), dip2px(0), dip2px(12), dip2px(0));
                        //获取到Tab内的文字控件
                        TextView labelView = (TextView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_title);
                        //计算文字的高度DP值并设置，setTextSize为设置文字正方形的对角线长度，所以：文字高度（总内容高度减去间距和图片高度）*根号2即为对角线长度，此处用DP值，设置该值即可。
                        labelView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
                        labelView.setIncludeFontPadding(false);
                        labelView.setPadding(0, 0, 0, dip2px(20 - textSize - space / 2));
                        //获取到Tab内的图像控件
                        ImageView iconView = (ImageView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_icon);
                        //设置图片参数，其中，MethodUtils.dip2px()：换算dp值
                        params = new FrameLayout.LayoutParams(dip2px(imgLen), dip2px(imgLen));
                        params.setMargins(0, 0, 0, space / 2);
                        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                        iconView.setLayoutParams(params);

                        TextView badge = (TextView) view.findViewById(R.id.fixed_bottom_navigation_badge);
                        FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                        badge.post(() -> {
//                            params1.setMargins(labelView.getWidth() - labelView.getWidth() / 2, 35, 0, 0);
//                            badge.setLayoutParams(params1);
//                            badge.setTextSize(10);
//
//                        });
                        badge.setTextSize(10);

                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int dip2px(float dpValue) {
        final float scale = getApplication().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void selectItem(int position) {
        if (mBottomNavigationBar != null) {
            mBottomNavigationBar.selectTab(position);
        }
    }


    /**
     * 切换fragment
     *
     * @param position 要显示的fragment的下标
     */
    private void switchFragment(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment targetFg = mFragments.get(position);
        Fragment lastFg = mFragments.get(mLastFgIndex);
//        if (mLastFgIndex != 1) {
//            ft.remove(lastFg);//我的界面 每次移除 下次重新请求
//        } else {
        ft.hide(lastFg);
//        }
        mLastFgIndex = position;
        if (!targetFg.isAdded()) {
            ft.add(R.id.fragment_group, targetFg);
        }
        ft.show(targetFg);
        ft.commitAllowingStateLoss();
    }

    private long mExitTime;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                showToast("再按一次退出程序");
                mExitTime = System.currentTimeMillis();
            } else {
                BaseAppManager.getInstance().clear();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public Object doSomeThing(String key, Object value) {
        if (key.equals(Constants.MAINSELECTNUMBER))
            selectItem((Integer) value);
        else if (key.equals(Constants.CART_COUNT))
            getCardNumber();
        return null;
    }

    @Override
    public void faild(String msg) {
        showToast(msg);
    }


    @Override
    public void setCartNumber(String number) {
        if (mTextBadgeItem != null) {
            mTextBadgeItem.setText(Utils.getString(number));
        }
    }
}
