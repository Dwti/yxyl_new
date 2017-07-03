package com.yanxiu.gphone.student.homepage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.HomePageBaseFragment;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.questions.answerframe.ui.activity.AnswerReportActicity;
import com.yanxiu.gphone.student.util.ActivityManger;
import com.yanxiu.gphone.student.util.DataFetcher;
import com.yanxiu.gphone.student.util.UpdateUtil;

import static com.yanxiu.gphone.student.constant.Constants.MAINAVTIVITY_REFRESH;

public class MainActivity extends YanxiuBaseActivity implements View.OnClickListener{

    private final String TAG = MainActivity.this.getClass().getSimpleName();

    public boolean misRefresh;//是否需要刷新

    private FrameLayout mContentMain;
    private final int INDEX_HOMEWORK = 0;//作业tab
    private final int INDEX_EXERCISE = 1;//练习tab
    private final int INDEX_MY = 2;//我的tab

    private int mLastSelectIndex = -1;

    private View mBottomNaviLayout;
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
        UpdateUtil.Initialize(this,false);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        misRefresh = getIntent().getBooleanExtra(MAINAVTIVITY_REFRESH,false);
        refreshFragmentData();
    }

    /**
     * 刷新当前fragment的数据（从loginacticity回来）
     */
    private void refreshFragmentData(){
        try{
            if(misRefresh)
                ((HomePageBaseFragment)mNaviFragmentFactory.getItem(mNaviFragmentFactory.getCurrentItem())).requestData();
        }catch(Exception e){
            e.printStackTrace();
            Log.e(TAG, "刷新失败" + e.getMessage());
        }

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
        DataFetcher.getInstance().destory();
        ActivityManger.destoryAll();
        super.onDestroy();
    }

    private void initView() {
        misRefresh = false;
        mContentMain = (FrameLayout) findViewById(R.id.content_main);
        mBottomNaviLayout = findViewById(R.id.navi_switcher);
        mFragmentManager = getSupportFragmentManager();
        mNaviFragmentFactory = new NaviFragmentFactory();
        initBottomBar();
        showCurrentFragment(0);
    }

    private void initBottomBar() {
        mSelNavTxtColor = getResources().getColor(R.color.color_336600);
        mNormalNavTxtColor = getResources().getColor(R.color.color_999999);
        mNavBarViews[0] = findViewById(R.id.navi_homework);
        mNavBarViews[1] = findViewById(R.id.navi_exercise);
        mNavBarViews[2] = findViewById(R.id.navi_my);
        for (int i = 0; i < 3; i++) {
            mNavBarViews[i].setOnClickListener(this);
            mNavIconViews[i] = (ImageView) mNavBarViews[i].findViewById(R.id.nav_icon);
            mNavTextViews[i] = (TextView) mNavBarViews[i].findViewById(R.id.nav_txt);
        }
//        Util.setViewTypeface(YanxiuTypefaceTextView.TypefaceType.FANGZHENG, mNavTextViews[0],
//                mNavTextViews[1], mNavTextViews[2]);
        mNavTextViews[0].setText(R.string.navi_tbm_group);
        mNavTextViews[1].setText(R.string.exercises);
        mNavTextViews[2].setText(R.string.navi_tbm_my);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.ddddd)
            AnswerReportActicity.invoke(this,"298148");

        int curItem = INDEX_HOMEWORK;
        switch (view.getId()) {
            case R.id.navi_homework:
                curItem = INDEX_HOMEWORK;
                break;
            case R.id.navi_exercise:
                curItem = INDEX_EXERCISE;
                break;
            case R.id.navi_my:
                curItem = INDEX_MY;
                break;
            default:
                break;
        }
        startTabAnimation(mNavBarViews[curItem]);
        if (mNaviFragmentFactory.getCurrentItem() != curItem) {
            showCurrentFragment(curItem);
        }
    }
    private void checkBottomBarProcess(int index){
        if(index>=0 && index<3) {
            resetBottomBar();
//            mNavBarViews[index].setBackgroundResource(R.drawable.home_nav_bar_sel);
            mNavTextViews[index].setTextColor(mSelNavTxtColor);
//            mNavTextViews[index].setShadowLayer(2, 0, 2, getResources().getColor(R.color.color_ffff99));
            switch (index) {
                case INDEX_HOMEWORK:
//                    mNavIconViews[0].setBackgroundResource(R.drawable.navi_homework_selected);
                    break;
                case INDEX_EXERCISE:
//                    mNavIconViews[1].setBackgroundResource(R.drawable.navi_exercise_selected);
                    break;
                case INDEX_MY:
//                    mNavIconViews[2].setBackgroundResource(R.drawable.navi_my_selected);
                    break;
            }
        }
    }
    private void resetBottomBar() {
        for (int i = 0; i < 3; i++) {
//            mNavBarViews[i].setBackgroundResource(R.drawable.home_nav_bar_nor);
            mNavTextViews[i].setTextColor(mNormalNavTxtColor);
//            mNavTextViews[i].setShadowLayer(2, 0, 2, getResources().getColor(R.color.color_33ffff));
        }
//        mNavIconViews[0].setBackgroundResource(R.drawable.navi_homework_normal);
//        mNavIconViews[1].setBackgroundResource(R.drawable.navi_exercise_normal);
//        mNavIconViews[2].setBackgroundResource(R.drawable.navi_my_normal);

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
        if (index != INDEX_HOMEWORK) {
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

    /**
     * 跳转MainActivity:LoginActivity跳转
     *
     * @param activity
     */
    public static void invoke(Activity activity,boolean refresh) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(MAINAVTIVITY_REFRESH,refresh);
        activity.startActivity(intent);
    }

    public void setBottomNaviBarsVisibility(int visibility) {
        mBottomNaviLayout.setVisibility(visibility);
    }

    /**
     * 点击tab时的弹跳动画
     * @param view
     */
    private void startTabAnimation(final View view){
        view.setVisibility(View.VISIBLE);
        Animation anim_step1 = AnimationUtils.loadAnimation(this, R.anim.anim_maintab_scale_1);
        final Animation anim_step2 = AnimationUtils.loadAnimation(this, R.anim.anim_maintab_scale_2);
        final Animation anim_step3 = AnimationUtils.loadAnimation(this, R.anim.anim_maintab_scale_3);
        final Animation anim_step4 = AnimationUtils.loadAnimation(this, R.anim.anim_maintab_scale_4);
        view.startAnimation(anim_step1);
        anim_step1.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {}

            public void onAnimationEnd(Animation animation) {
                view.startAnimation(anim_step2);
            }

            public void onAnimationRepeat(Animation animation) {}
        });
        anim_step2.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {}

            public void onAnimationEnd(Animation animation) {
                view.startAnimation(anim_step3);
            }

            public void onAnimationRepeat(Animation animation) {}
        });
        anim_step3.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {}

            public void onAnimationEnd(Animation animation) {
                view.startAnimation(anim_step4);
            }

            public void onAnimationRepeat(Animation animation) {}
        });
    }
}
