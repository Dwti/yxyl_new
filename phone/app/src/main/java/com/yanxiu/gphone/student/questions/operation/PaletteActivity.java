package com.yanxiu.gphone.student.questions.operation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.customviews.LoadingView;
import com.yanxiu.gphone.student.questions.operation.view.PaletteToolsView;
import com.yanxiu.gphone.student.questions.operation.view.PaletteView;
import com.yanxiu.gphone.student.questions.operation.view.SelectColorView;
import com.yanxiu.gphone.student.questions.spoken.SpokenErrorDialog;
import com.yanxiu.gphone.student.util.FileUtil;
import com.yanxiu.gphone.student.util.anim.AlphaAnimationUtil;

import java.io.File;

import de.greenrobot.event.EventBus;

/**
 * Created by sunpeng on 2018/1/10.
 */

public class PaletteActivity extends YanxiuBaseActivity implements View.OnClickListener {

    private ImageView iv_undo, iv_redo, iv_back, iv_save;
    private View mOverlay;
    private LoadingView mLoadingView;
    private Button btn_reset;
    private PaletteView mPaletteView;
    private PaletteToolsView mPaletteToolsView;
    private SelectColorView mSelectColorView;
    private PopupWindow mPopupWindow;
    private String mImgUrl;
    private String mStoredFilePath;
    public static final String IMAGE_URL = "IMAGE_URL";
    public static final String FILE_PATH = "FILE_PATH";  //保存涂画的path的文件名字
    public static final String SUFFIX = ".jpg";

    public static void invoke(Context context,String filePath,String imgUrl) {
        Intent intent = new Intent(context, PaletteActivity.class);
        intent.putExtra(FILE_PATH,filePath);
        intent.putExtra(IMAGE_URL,imgUrl);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);
        initData();
        initView();
        initListener();
    }

    private void initView() {
        mOverlay = findViewById(R.id.overlay);
        mLoadingView = (LoadingView) findViewById(R.id.loading);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        iv_undo = (ImageView) findViewById(R.id.iv_undo);
        iv_redo = (ImageView) findViewById(R.id.iv_redo);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_save = (ImageView) findViewById(R.id.iv_save);
        mPaletteView = (PaletteView) findViewById(R.id.palette);
        mPaletteToolsView = (PaletteToolsView) findViewById(R.id.paletteTools);
        mSelectColorView = (SelectColorView) findViewById(R.id.selectColorView);

        iv_undo.setEnabled(false);
        iv_redo.setEnabled(false);

        if(!TextUtils.isEmpty(mImgUrl)){
            Glide.with(this).load(mImgUrl).into(new SimpleTarget<GlideDrawable>() {

                @Override
                public void onLoadStarted(Drawable placeholder) {
                    super.onLoadStarted(placeholder);
                    mLoadingView.showLoadingView();
                }

                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    mPaletteView.setResourceDrawable(resource);
                    mLoadingView.hiddenLoadingView();
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    mLoadingView.hiddenLoadingView();
                    SpokenErrorDialog errorDialog = new SpokenErrorDialog(PaletteActivity.this);
                    errorDialog.setTitle("图片加载失败");
                    errorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            PaletteActivity.this.finish();
                        }
                    });
                    errorDialog.show();
                }
            });
        }

        if(OperationUtils.hasStoredBitmap(mStoredFilePath)){
            mPaletteView.post(new Runnable() {
                @Override
                public void run() {
                    mPaletteView.restoreLocalBitmap(OperationUtils.getStoredBitmap(mStoredFilePath).copy(Bitmap.Config.ARGB_8888,true));
                }
            });
        }


    }

    private void initData(){
        mImgUrl = getIntent().getStringExtra(IMAGE_URL);
        mStoredFilePath = getIntent().getStringExtra(FILE_PATH);
    }

    private void initListener() {
        iv_back.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
        iv_undo.setOnClickListener(this);
        iv_redo.setOnClickListener(this);
        iv_save.setOnClickListener(this);

        mPaletteView.setUndoStatusChangedListener(new PaletteView.UndoStatusChangedListener() {
            @Override
            public void onStatusChanged(boolean canUndo) {
                if (canUndo) {
                    iv_undo.setEnabled(true);
                } else {
                    iv_undo.setEnabled(false);
                }
            }
        });

        mSelectColorView.setOnSelectedColorChangedListener(new SelectColorView.OnSelectedColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                mPaletteToolsView.setColor(color);
                mPaletteView.setPaintColor(color);
            }
        });

        mSelectColorView.setOnColorBoardDismissListener(new SelectColorView.OnColorBoardDismissListener() {
            @Override
            public void onDismiss() {
                mPaletteToolsView.setVisibility(View.VISIBLE);
            }
        });

        mPaletteToolsView.setOnColorClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectColorView.setVisibility(View.VISIBLE);
                mPaletteToolsView.setVisibility(View.INVISIBLE);
            }
        });

        mPaletteToolsView.setOnPaintModeChangedListener(new PaletteToolsView.OnPaintModeChangedListener() {
            @Override
            public void onPaintModeChanged(PaletteView.PaintMode mode, float strokeWidth) {
                mPaletteView.setPaintMode(mode);
                mPaletteView.setStrokeWidth(strokeWidth);
            }
        });

        mPaletteToolsView.setOnLineModeChangedListener(new PaletteToolsView.OnLineModeChangedListener() {
            @Override
            public void onLineModeChanged(PaletteView.LineMode mode) {
                mPaletteView.setLineMode(mode);
            }
        });

        mPaletteView.setRedoStatusChangedListener(new PaletteView.RedoStatusChangedListener() {
            @Override
            public void onStatusChanged(boolean canRedo) {
                if (canRedo) {
                    iv_redo.setEnabled(true);
                } else {
                    iv_redo.setEnabled(false);
                }
            }
        });

        mPaletteView.setOnResetListener(new PaletteView.OnResetListener() {
            @Override
            public void onReset() {
                String bmpPath = mStoredFilePath;
                String picPath = mStoredFilePath + SUFFIX;
                File bmpFile = new File(bmpPath);
                File picFile = new File(picPath);
                if(bmpFile.exists()){
                    bmpFile.delete();
                }
                if(picFile.exists()){
                    picFile.delete();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if(mPaletteView.hasModified()){
                    showPop();
                }else {
                    finish();
                }
                break;
            case R.id.iv_save:
                if(mPaletteView.hasModified()){
                    saveDrawPath();
                }
                finish();
                break;
            case R.id.btn_reset:
                mPaletteView.reset();
                break;
            case R.id.iv_undo:
                mPaletteView.undo();
                break;
            case R.id.iv_redo:
                mPaletteView.redo();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(mPaletteView.hasModified()){
            showPop();
        }else {
            finish();
        }
    }

    private void showPop() {
        if (mPopupWindow == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.pop_operation_tips, null);
            mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mPopupWindow.setAnimationStyle(R.style.pop_anim);

            View pop_bg = view.findViewById(R.id.pop_bg);
            View tv_save = view.findViewById(R.id.tv_save);
            View tv_ignore = view.findViewById(R.id.tv_ignore);
            Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

            pop_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissPop();
                }
            });

            tv_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveDrawPath();
                    dismissPop();
                    PaletteActivity.this.finish();
                }
            });

            tv_ignore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissPop();
                    PaletteActivity.this.finish();
                }
            });

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissPop();
                }
            });

        }
        if(mPopupWindow.isShowing()){
            return;
        }
        mOverlay.setVisibility(View.VISIBLE);
        AlphaAnimationUtil.startPopBgAnimIn(mOverlay);
        mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);

    }

    private void saveDrawPath() {
        //保存画的路径
        String bmpPath = mStoredFilePath;
        FileUtil.saveBitmapToFile(mPaletteView.getBufferedBitmap(),mStoredFilePath);
        //合成背景图跟画的图
        Bitmap bgBmp = mPaletteView.getBgBitmap();
        Bitmap buffBmp = mPaletteView.getBufferedBitmap();
        Bitmap newBmp = Bitmap.createBitmap(mPaletteView.getWidth(),mPaletteView.getHeight(), Bitmap.Config.ARGB_8888);
        newBmp.eraseColor(Color.WHITE);
        Canvas canvas = new Canvas(newBmp);
        float startX =  ( newBmp.getWidth() - bgBmp.getWidth() ) / 2;
        float startY = (newBmp.getHeight() - bgBmp.getHeight() ) / 2;
        //背景图需要居中
        canvas.drawBitmap(bgBmp,startX,startY,null);
        canvas.drawBitmap(buffBmp,0,0,null);
        //保存合成后的图片
        String picPath = bmpPath + SUFFIX;
        FileUtil.saveBitmapToFile(newBmp,picPath);

        //通知图片被修改
        EventBus.getDefault().post(new PictureModifiedMessage());
    }

    private void dismissPop() {
        AlphaAnimationUtil.startPopBgAnimExit(mOverlay);
        mOverlay.setVisibility(View.GONE);
        if (mPopupWindow != null && mPopupWindow.isShowing())
            mPopupWindow.dismiss();
    }

}
