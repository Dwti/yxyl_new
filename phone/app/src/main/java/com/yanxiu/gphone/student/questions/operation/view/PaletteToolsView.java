package com.yanxiu.gphone.student.questions.operation.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.util.ScreenUtils;

/**
 * Created by sunpeng on 2018/1/12.
 */

public class PaletteToolsView extends FrameLayout {
    private SmallPenView mSmallPenView;
    private BigPenView mBigPenView;
    private ColorFrameRelativeLayout mStraightLineFrame,mDottedLineFrame;
    private ImageView mEraser;
    private CircleView mColorView;
    public static float THIN_STROKE;
    public static float BOLD_STROKE;
    public static float ERASER_STROKE;

    private OnPaintModeChangedListener mOnPaintModeChangedListener;
    private OnLineModeChangedListener mOnLineModeChangedListener;
    public PaletteToolsView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PaletteToolsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PaletteToolsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PaletteToolsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        View root = LayoutInflater.from(context).inflate(R.layout.palette_tools,this,true);
        mSmallPenView = (SmallPenView) root.findViewById(R.id.smallPen);
        mBigPenView = (BigPenView) root.findViewById(R.id.bigPen);
        mStraightLineFrame = (ColorFrameRelativeLayout) root.findViewById(R.id.straightLineLayout);
        mDottedLineFrame = (ColorFrameRelativeLayout) root.findViewById(R.id.dottedLineLayout);
        mEraser = (ImageView) root.findViewById(R.id.iv_eraser);
        mColorView = (CircleView) root.findViewById(R.id.currentColor);

        mSmallPenView.setSelected(true);

        THIN_STROKE = ScreenUtils.dpToPx(getContext(),2);
        BOLD_STROKE = ScreenUtils.dpToPx(getContext(),10);
        ERASER_STROKE = ScreenUtils.dpToPx(getContext(),12);

        initListener();
    }

    private void initListener(){
        mSmallPenView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSmallPenViewSelected(true);
            }
        });

        mBigPenView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setBigPenViewSelected(true);
            }
        });

        mStraightLineFrame.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEraser.isSelected())
                    return;
                mStraightLineFrame.setFrameVisible(!mStraightLineFrame.getFrameVisible());
                mDottedLineFrame.setFrameVisible(false);
                if(mOnLineModeChangedListener != null){
                    if(mStraightLineFrame.getFrameVisible()){
                        mOnLineModeChangedListener.onLineModeChanged(PaletteView.LineMode.STRAIGHT);
                    }else {
                        mOnLineModeChangedListener.onLineModeChanged(PaletteView.LineMode.NONE);
                    }
                }
            }
        });

        mDottedLineFrame.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEraser.isSelected())
                    return;
                mDottedLineFrame.setFrameVisible(!mDottedLineFrame.getFrameVisible());
                mStraightLineFrame.setFrameVisible(false);
                if(mOnLineModeChangedListener != null){
                    if(mDottedLineFrame.getFrameVisible()){
                        mOnLineModeChangedListener.onLineModeChanged(PaletteView.LineMode.DOTTED);
                    }else {
                        mOnLineModeChangedListener.onLineModeChanged(PaletteView.LineMode.NONE);
                    }
                }
            }
        });

        mEraser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setEraserSelected(true);
            }
        });
    }

    private void setSmallPenViewSelected(boolean selected){
        if(mSmallPenView.isSelected() == selected){
            return;
        }else {
            mSmallPenView.setSelected(selected);
            if(selected){
                setBigPenViewSelected(false);
                setEraserSelected(false);
                mSmallPenView.setUseStatus(true);
                if(mOnPaintModeChangedListener != null){
                    mOnPaintModeChangedListener.onPaintModeChanged(PaletteView.PaintMode.DRAW, THIN_STROKE);
                }
            }else {
                mSmallPenView.setUseStatus(false);
            }
        }
    }

    private void setBigPenViewSelected(boolean selected){
        if(mBigPenView.isSelected() == selected){
            return;
        }else {
            mBigPenView.setSelected(selected);
            if(selected){
                setSmallPenViewSelected(false);
                setEraserSelected(false);
                mBigPenView.setUseStatus(true);
                if(mOnPaintModeChangedListener != null){
                    mOnPaintModeChangedListener.onPaintModeChanged(PaletteView.PaintMode.DRAW, BOLD_STROKE);
                }
            }else {
                mBigPenView.setUseStatus(false);
            }
        }
    }


    private void setEraserSelected(boolean selected){
        if(mEraser.isSelected() == selected){
            return;
        }else {
            mEraser.setSelected(selected);
            if(selected){
                setSmallPenViewSelected(false);
                setBigPenViewSelected(false);
                mEraser.setImageResource(R.drawable.eraser_use);
                if(mOnPaintModeChangedListener != null){
                    mOnPaintModeChangedListener.onPaintModeChanged(PaletteView.PaintMode.ERASER,ERASER_STROKE);
                }
            }else {
                mEraser.setImageResource(R.drawable.eraser_unuse);
            }
        }
    }

    public void setColor(int color){
        mSmallPenView.setColor(color);
        mBigPenView.setColor(color);
        mStraightLineFrame.setColor(color);
        mDottedLineFrame.setColor(color);
        mColorView.setCircleColor(color);
    }

    public void setOnColorClickListener(OnClickListener listener){
        mColorView.setOnClickListener(listener);
    }


    public OnPaintModeChangedListener getOnPaintModeChangedListener() {
        return mOnPaintModeChangedListener;
    }

    public void setOnPaintModeChangedListener(OnPaintModeChangedListener onPaintModeChangedListener) {
        mOnPaintModeChangedListener = onPaintModeChangedListener;
    }

    public OnLineModeChangedListener getOnLineModeChangedListener() {
        return mOnLineModeChangedListener;
    }

    public void setOnLineModeChangedListener(OnLineModeChangedListener onLineModeChangedListener) {
        mOnLineModeChangedListener = onLineModeChangedListener;
    }

    public interface OnPaintModeChangedListener{
        void onPaintModeChanged(PaletteView.PaintMode mode,float strokeWidth);
    }

    public interface OnLineModeChangedListener{
        void onLineModeChanged(PaletteView.LineMode mode);
    }
}
