package com.yanxiu.gphone.student.questions.operation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.questions.operation.view.PaletteToolsView;
import com.yanxiu.gphone.student.questions.operation.view.PaletteView;
import com.yanxiu.gphone.student.questions.operation.view.PathDrawingInfo;
import com.yanxiu.gphone.student.questions.operation.view.SelectColorView;
import com.yanxiu.gphone.student.util.FileUtil;
import com.yanxiu.gphone.student.util.anim.AlphaAnimationUtil;

import java.util.List;

/**
 * Created by sunpeng on 2018/1/10.
 */

public class PaletteActivity extends YanxiuBaseActivity implements View.OnClickListener {

    private ImageView iv_undo, iv_redo, iv_back, iv_save;
    private View mOverlay;
    private Button btn_reset;
    private PaletteView mPaletteView;
    private PaletteToolsView mPaletteToolsView;
    private SelectColorView mSelectColorView;
    private PopupWindow mPopupWindow;

    public static void invoke(Context context) {
        Intent intent = new Intent(context, PaletteActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);
        initView();
        initListener();
    }

    private void initView() {
        mOverlay = findViewById(R.id.overlay);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                //TODO 如果有修改就弹窗，否则直接退出
                showPop();
                break;
            case R.id.iv_save:
                String filePath = FileUtil.getSavePicturePath("222");
                mPaletteView.restoreBuffedBitmap(FileUtil.readBitmapFromFile(filePath).copy(Bitmap.Config.ARGB_8888,true));
                break;
            case R.id.btn_reset:
                mPaletteView.clear();
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
        showPop();
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
                    //保存和读取的时候最好开启一个线程（因为时间原因，再一个这个bitmap本身比较小的缘故所以没用，后期优化）
                    String filePath = FileUtil.getSavePicturePath("222");
                    FileUtil.saveBitmapToFile(mPaletteView.getBufferedBitmap(),filePath);
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

    private void dismissPop() {
        AlphaAnimationUtil.startPopBgAnimExit(mOverlay);
        mOverlay.setVisibility(View.GONE);
        if (mPopupWindow != null && mPopupWindow.isShowing())
            mPopupWindow.dismiss();
    }

}
