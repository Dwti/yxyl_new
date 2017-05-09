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

    private FrameLayout contentMain;

    private final int INDEX_HOMEWORK = 0;//练习tab
    private final int INDEX_GROUP = 1;//作业tab
    private final int INDEX_MY = 2;//我的tab

    private int lastSelectIndex = -1;

    private View[] navBarViews = new View[3];
    private ImageView[] navIconViews = new ImageView[3];
    private TextView[] navTextViews = new TextView[3];
    private int normalNavTxtColor, selNavTxtColor;

    public NaviFragmentFactory mNaviFragmentFactory;
    public FragmentManager fragmentManager;

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
        contentMain = (FrameLayout) findViewById(R.id.content_main);
        fragmentManager = getSupportFragmentManager();
        mNaviFragmentFactory = new NaviFragmentFactory();
        initBottomBar();
        showCurrentFragment(0);
    }

    private void initBottomBar() {
        selNavTxtColor = getResources().getColor(R.color.color_805500);
        normalNavTxtColor = getResources().getColor(R.color.color_006666);
        navBarViews[0] = findViewById(R.id.navi_homework);
        navBarViews[1] = findViewById(R.id.navi_group);
        navBarViews[2] = findViewById(R.id.navi_my);
        for (int i = 0; i < 3; i++) {
            navBarViews[i].setOnClickListener(this);
            navIconViews[i] = (ImageView) navBarViews[i].findViewById(R.id.nav_icon);
            navTextViews[i] = (TextView) navBarViews[i].findViewById(R.id.nav_txt);
        }
//        Util.setViewTypeface(YanxiuTypefaceTextView.TypefaceType.FANGZHENG, navTextViews[0],
//                navTextViews[1], navTextViews[2]);
        navTextViews[0].setText(R.string.exercises);
        navTextViews[1].setText(R.string.navi_tbm_group);
        navTextViews[2].setText(R.string.navi_tbm_my);
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
            navBarViews[index].setBackgroundResource(R.drawable.home_nav_bar_sel);
            navTextViews[index].setTextColor(selNavTxtColor);
            navTextViews[index].setShadowLayer(2, 0, 2, getResources().getColor(R.color.color_ffff99));
            switch (index) {
                case INDEX_HOMEWORK:
                    navIconViews[0].setBackgroundResource(R.drawable.navi_main_selected);
                    break;
                case INDEX_GROUP:
                    navIconViews[1].setBackgroundResource(R.drawable.navi_group_selected);
                    break;
                case INDEX_MY:
                    navIconViews[2].setBackgroundResource(R.drawable.navi_my_selected);
                    break;
            }
        }
    }
    private void resetBottomBar() {
        for (int i = 0; i < 3; i++) {
            navBarViews[i].setBackgroundResource(R.drawable.home_nav_bar_nor);
            navTextViews[i].setTextColor(normalNavTxtColor);
            navTextViews[i].setShadowLayer(2, 0, 2, getResources().getColor(R.color.color_33ffff));
        }
        navIconViews[0].setBackgroundResource(R.drawable.navi_main_normal);
        navIconViews[1].setBackgroundResource(R.drawable.navi_group_normal);
        navIconViews[2].setBackgroundResource(R.drawable.navi_my_normal);

    }

    private void showCurrentFragment(int index) {
        if (index == lastSelectIndex) {
            return;
        }
        lastSelectIndex = index;
        checkBottomBarProcess(index);
        if (mNaviFragmentFactory == null) {
            mNaviFragmentFactory = new NaviFragmentFactory();
        }
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        if (index != INDEX_GROUP) {
//            requestGroupHwDotNumTask();
        }
        mNaviFragmentFactory.hideAndShowFragment(fragmentManager, index);
    }

    /**
     * 退出间隔时间戳
     */
    private long backTimestamp = 0;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_DOWN) {

            if (System.currentTimeMillis() - backTimestamp <= 2000) {
                //Todo 退出程序

                finish();
            } else {
                backTimestamp = System.currentTimeMillis();
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
