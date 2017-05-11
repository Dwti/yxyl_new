package com.yanxiu.gphone.student.homePage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;

public class MainActivity extends YanxiuBaseActivity implements View.OnClickListener{

    private FrameLayout mContentMain;
    private final int INDEX_HOMEWORK = 0;//练习tab
    private final int INDEX_GROUP = 1;//作业tab
    private final int INDEX_MY = 2;//我的tab

    private int mLastSelectIndex = -1;

    private View[] mNavBarViews = new View[3];
    private ImageView[] mNavIconViews = new ImageView[3];
    private TextView[] mNavTextViews = new TextView[3];
    private int mNormalNavTxtColor, mSelNavTxtColor;

    public NaviFragmentFactory mNaviFragmentFactory;
    public FragmentManager mFragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        mContentMain = (FrameLayout) findViewById(R.id.content_main);
        mFragmentManager = getSupportFragmentManager();
        mNaviFragmentFactory = new NaviFragmentFactory();
        initBottomBar();
        showCurrentFragment(0);
    }

    private void initBottomBar() {
        mSelNavTxtColor = getResources().getColor(R.color.color_805500);
        mNormalNavTxtColor = getResources().getColor(R.color.color_006666);
        mNavBarViews[0] = findViewById(R.id.navi_homework);
        mNavBarViews[1] = findViewById(R.id.navi_group);
        mNavBarViews[2] = findViewById(R.id.navi_my);
        for (int i = 0; i < 3; i++) {
            mNavBarViews[i].setOnClickListener(this);
            mNavIconViews[i] = (ImageView) mNavBarViews[i].findViewById(R.id.nav_icon);
            mNavTextViews[i] = (TextView) mNavBarViews[i].findViewById(R.id.nav_txt);
        }
//        Util.setViewTypeface(YanxiuTypefaceTextView.TypefaceType.FANGZHENG, mNavTextViews[0],
//                mNavTextViews[1], mNavTextViews[2]);
        mNavTextViews[0].setText(R.string.exercises);
        mNavTextViews[1].setText(R.string.navi_tbm_group);
        mNavTextViews[2].setText(R.string.navi_tbm_my);
    }

    @Override
    public void onClick(View view) {
        int curItem = INDEX_HOMEWORK;
        switch (view.getId()) {
            case R.id.navi_homework:
                curItem = INDEX_HOMEWORK;
                break;
            case R.id.navi_group:
                curItem = INDEX_GROUP;
                break;
            case R.id.navi_my:
                curItem = INDEX_MY;
                break;
            default:
                break;
        }
        if (mNaviFragmentFactory.getCurrentItem() != curItem) {
            showCurrentFragment(curItem);
        }
    }
    private void checkBottomBarProcess(int index){
        if(index>=0 && index<3) {
            resetBottomBar();
            mNavBarViews[index].setBackgroundResource(R.drawable.home_nav_bar_sel);
            mNavTextViews[index].setTextColor(mSelNavTxtColor);
            mNavTextViews[index].setShadowLayer(2, 0, 2, getResources().getColor(R.color.color_ffff99));
            switch (index) {
                case INDEX_HOMEWORK:
                    mNavIconViews[0].setBackgroundResource(R.drawable.navi_main_selected);
                    break;
                case INDEX_GROUP:
                    mNavIconViews[1].setBackgroundResource(R.drawable.navi_group_selected);
                    break;
                case INDEX_MY:
                    mNavIconViews[2].setBackgroundResource(R.drawable.navi_my_selected);
                    break;
            }
        }
    }
    private void resetBottomBar() {
        for (int i = 0; i < 3; i++) {
            mNavBarViews[i].setBackgroundResource(R.drawable.home_nav_bar_nor);
            mNavTextViews[i].setTextColor(mNormalNavTxtColor);
            mNavTextViews[i].setShadowLayer(2, 0, 2, getResources().getColor(R.color.color_33ffff));
        }
        mNavIconViews[0].setBackgroundResource(R.drawable.navi_main_normal);
        mNavIconViews[1].setBackgroundResource(R.drawable.navi_group_normal);
        mNavIconViews[2].setBackgroundResource(R.drawable.navi_my_normal);

    }

    private void showCurrentFragment(int index) {
        if (index == mLastSelectIndex) {
            return;
        }
        mLastSelectIndex = index;
        checkBottomBarProcess(index);
        if (mNaviFragmentFactory == null) {
            mNaviFragmentFactory = new NaviFragmentFactory();
        }
        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }
        if (index != INDEX_GROUP) {
//            requestGroupHwDotNumTask();
        }
        mNaviFragmentFactory.hideAndShowFragment(mFragmentManager, index);
    }

    /**
     * 退出间隔时间戳
     */
    private long mBackTimestamp = 0;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_DOWN) {

            if (System.currentTimeMillis() - mBackTimestamp <= 2000) {
                //Todo 退出程序

                finish();
            } else {
                mBackTimestamp = System.currentTimeMillis();
                Toast.makeText(this, getString(R.string.app_exit_tip), Toast.LENGTH_SHORT).show();
            }
            return false;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    /**
     * 跳转MainActivity
     *
     * @param activity
     */
    public static void invoke(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }
}
