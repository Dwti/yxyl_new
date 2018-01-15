package com.yanxiu.gphone.student.questions.operation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.questions.operation.view.PaletteToolsView;
import com.yanxiu.gphone.student.questions.operation.view.PaletteView;
import com.yanxiu.gphone.student.questions.operation.view.SelectColorView;

/**
 * Created by sunpeng on 2018/1/10.
 */

public class PaletteActivity extends YanxiuBaseActivity implements View.OnClickListener {

    private ImageView iv_undo,iv_redo,iv_back,iv_save;
    private Button btn_reset;
    private PaletteView mPaletteView;
    private PaletteToolsView mPaletteToolsView;
    private SelectColorView mSelectColorView;

    public static void invoke(Context context){
        Intent intent = new Intent(context,PaletteActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);
        initView();
        initListener();
    }

    private void initView(){
        btn_reset = (Button) findViewById(R.id.btn_reset);
        iv_undo = (ImageView) findViewById(R.id.iv_undo);
        iv_redo= (ImageView) findViewById(R.id.iv_redo);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_save = (ImageView) findViewById(R.id.iv_save);
        mPaletteView = (PaletteView) findViewById(R.id.palette);
        mPaletteToolsView = (PaletteToolsView) findViewById(R.id.paletteTools);
        mSelectColorView = (SelectColorView) findViewById(R.id.selectColorView);

        iv_undo.setEnabled(false);
        iv_redo.setEnabled(false);
    }

    private void initListener(){
        iv_back.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
        iv_undo.setOnClickListener(this);
        iv_redo.setOnClickListener(this);
        iv_save.setOnClickListener(this);

        mPaletteView.setUndoStatusChangedListener(new PaletteView.UndoStatusChangedListener() {
            @Override
            public void onStatusChanged(boolean canUndo) {
                if(canUndo){
                    iv_undo.setEnabled(true);
                }else {
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
                if(canRedo){
                    iv_redo.setEnabled(true);
                }else {
                    iv_redo.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                break;
            case R.id.iv_save:
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
}
