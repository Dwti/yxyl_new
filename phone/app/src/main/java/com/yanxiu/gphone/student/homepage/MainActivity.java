package com.yanxiu.gphone.student.homepage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.igexin.sdk.PushManager;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.homework.HomeworkFragment;
import com.yanxiu.gphone.student.homework.homeworkdetail.HomeworkDetailActivity;
import com.yanxiu.gphone.student.homework.response.PaperResponse;
import com.yanxiu.gphone.student.push.PushMsgBean;
import com.yanxiu.gphone.student.push.YanxiuPushService;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
import com.yanxiu.gphone.student.questions.answerframe.http.request.AnswerReportRequest;
import com.yanxiu.gphone.student.questions.answerframe.ui.activity.AnswerReportActicity;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.util.ActivityManger;
import com.yanxiu.gphone.student.util.DESBodyDealer;
import com.yanxiu.gphone.student.util.DataFetcher;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.util.UpdateUtil;

import static com.yanxiu.gphone.student.constant.Constants.MAINAVTIVITY_PUSHMSGBEAN;
import static com.yanxiu.gphone.student.constant.Constants.MAINAVTIVITY_REFRESH;

public class MainActivity extends YanxiuBaseActivity implements View.OnClickListener{

    private final String TAG = MainActivity.this.getClass().getSimpleName();
    private final String PUSH_TAG = "pushTag";

    private static MainActivity mainInstance;
    private int msg_type = 0;//推送消息类型

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

    private AnswerReportRequest mRequest;//答题报告请求

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainInstance = this;
        PushManager.getInstance().initialize(this.getApplicationContext(), YanxiuPushService.class);
        initView();
        UpdateUtil.Initialize(this,false);
        judgeToJump(getIntent());
        PushManager.getInstance().bindAlias(this.getApplicationContext(), LoginInfo.getUID());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        showCurrentFragment(0);
        judgeToJump(getIntent());
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
        if (mRequest != null) {
            mRequest.cancelRequest();
            mRequest = null;
        }
        super.onDestroy();
        mainInstance = null;
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

        // Todo 移除icon背景色 后期美工出切图后，这行代码可以去掉
        mNavIconViews[2].setBackground(null);

        Glide.with(this).load(LoginInfo.getHeadIcon()).asBitmap().into(new BitmapImageViewTarget(mNavIconViews[2]){
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable bitmapDrawable= RoundedBitmapDrawableFactory.create(view.getContext().getResources(),resource);
                bitmapDrawable.setCircular(true);
                view.setImageDrawable(bitmapDrawable);
            }
        });
    }

    @Override
    public void onClick(View view) {
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
                ActivityManger.destoryAll();
            } else {
                mBackTimestamp = System.currentTimeMillis();
                Toast.makeText(this, getString(R.string.app_exit_tip), Toast.LENGTH_SHORT).show();
            }
            return false;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    public static MainActivity getInstance() {
        return mainInstance;
    }

    /**
     * 推送跳转逻辑
     * @param intent
     */
    public void judgeToJump(Intent intent) {
        if (intent != null) {
            int currIndex = mNaviFragmentFactory.getCurrentItem();
            PushMsgBean mPushMsgBean = (PushMsgBean) intent.getSerializableExtra(MAINAVTIVITY_PUSHMSGBEAN);
            if(mPushMsgBean != null) {
                Log.e(TAG, "-----------MainActivity----push----");
                msg_type = mPushMsgBean.getMsg_type();//msg_type：0为作业报告，1为学科作业列表，2为作业首页；
                Log.e(PUSH_TAG, "msg_type =" + msg_type);
                switch (msg_type) {
                    case Constants.NOTIFICATION_ACTION_HOMEWORK_CORRECTING:
                        questReportData(String.valueOf(mPushMsgBean.getId()));
                        break;
                    case Constants.NOTIFICATION_ACTION_ASSIGN_HOMEWORK:
                        HomeworkDetailActivity.invoke(MainActivity.this, String.valueOf(mPushMsgBean.getId()), mPushMsgBean.getName());
                        break;
                    case Constants.NOTIFICATION_ACTION_JOIN_THE_CLASS:
                        //进入作业首页
                        if (currIndex == INDEX_HOMEWORK) { //作业
                            if (mNaviFragmentFactory.getCurrentItem() == 0) {
                                Log.e(PUSH_TAG, "mNaviFragmentFactory");
                                ((HomeworkFragment) mNaviFragmentFactory.getItem(0)).loadSubject();
                            }
                        } else {
                            ActivityManger.destoryAllActivity();
                            showCurrentFragment(INDEX_HOMEWORK);
                        }
                        break;
                    case Constants.NOTIFICATION_ACTION_OPEN_WEBVIEW:
//                        ActivityManager.destoryWebviewActivity();
//                        WebViewActivity.launch(MainActivity.this, mPushMsgBean.getName(), getString(R.string.app_name));
                        break;
                    default:
                        break;
                }
            }
//            int type=intent.getIntExtra("type",0);
//            if (type==1){
//                showCurrentFragment(0);
//            }
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

    public static void invoke(Context context, PushMsgBean mPushMsgBean) {
        Intent intent = new Intent();
        if (MainActivity.mainInstance != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }
        intent.setClass(context, MainActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra(MAINAVTIVITY_PUSHMSGBEAN, mPushMsgBean);
        context.startActivity(intent);
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


    /**
     * 请求答题报告
     */
    private void questReportData(String paperId) {
        if (TextUtils.isEmpty(paperId)) {
            return;
        }
        if (mRequest != null) {
            mRequest.cancelRequest();
            mRequest = null;
        }
        mRequest = new AnswerReportRequest(paperId);
        mRequest.bodyDealer = new DESBodyDealer();
        mRequest.startRequest(PaperResponse.class, new EXueELianBaseCallback<PaperResponse>() {

            @Override
            protected void onResponse(RequestBase request, PaperResponse response) {
                if (response.getStatus().getCode() == 0) {
                    if (response.getData().size() > 0) {
                        Paper paper_report = new Paper(response.getData().get(0), QuestionShowType.ANALYSIS);
                        if (paper_report != null && paper_report.getQuestions() != null && paper_report.getQuestions().size() > 0) {
                            String key = this.hashCode() + paper_report.getId();
                            DataFetcher.getInstance().save(key, paper_report);
                            AnswerReportActicity.invoke(MainActivity.this, key);
                        }
                    }
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
//                ToastManager.showMsg("提交成功");
            }
        });
    }
}
