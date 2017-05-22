package com.yanxiu.gphone.student.user;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.yanxiu.gphone.student.R;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/19 11:20.
 * Function :
 */
public class ChooseView extends View {

    private static final int DEFAULT_TEXT_SIZE = 28;
    private static final int DEFAULT_TEXT_COLOR = Color.BLACK;
    private static final int DEFAULT_TEXT_COLOR_ALPHA_START = 255;
    private static final int DEFAULT_TEXT_COLOR_ALPHA_END = 100;
    private static final int DEFAULT_LINES_COLOR = Color.BLUE;

    private static final int MOVE_TYPE_DEFAULT=0x0000;
    private static final int MOVE_TYPE_UP=0x0001;
    private static final int MOVE_TYPE_DOWN=0x0002;

    private int mTextSize = DEFAULT_TEXT_SIZE;
    private int mTextColor = DEFAULT_TEXT_COLOR;
    private int mTextColorAlphaStart = DEFAULT_TEXT_COLOR_ALPHA_START;
    private int mTextColorAlphaEnd = DEFAULT_TEXT_COLOR_ALPHA_END;
    private int mCenterTextColor = DEFAULT_TEXT_COLOR;
    private int mCenterTextSize = DEFAULT_TEXT_SIZE;
    private int mLinesColor = DEFAULT_LINES_COLOR;

    private Paint paint;
    private int move_type=MOVE_TYPE_DEFAULT;

    private static final int mLinesHeight=4;
    private static final int mCenterRectHeight=200;
    private static final int mTextMarginHeight=40;

    private String[] str = new String[]{"","","","0", "1", "2", "3", "4", "5", "6", "7", "8", "9","","",""};

    private float draw_center_x;
    private float draw_center_y;

    private float text_height;
    private float text_height1;

    private int location=3;

    private float move;

    public ChooseView(Context context) {
        super(context);
        init(context, null);
    }

    public ChooseView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChooseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }
        if (attrs != null) {
            initTypedArray(context, attrs);
        }
        initPaint();
    }

    private void initTypedArray(Context context, AttributeSet attrs) {
//        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ChooseView);
//        mTextSize = array.getDimensionPixelSize(R.styleable.ChooseView_choose_textsize, DEFAULT_TEXT_SIZE);
//        mTextColor = array.getColor(R.styleable.ChooseView_choose_lines_color, DEFAULT_LINES_COLOR);
//        mTextColorAlphaStart = array.getInteger(R.styleable.ChooseView_choose_textcolor_alpha_start, DEFAULT_TEXT_COLOR_ALPHA_START);
//        mTextColorAlphaEnd = array.getInteger(R.styleable.ChooseView_choose_textcolor_alpha_end, DEFAULT_TEXT_COLOR_ALPHA_END);
//        mCenterTextColor = array.getColor(R.styleable.ChooseView_choose_center_textcolor, DEFAULT_TEXT_COLOR);
//        mCenterTextSize = array.getDimensionPixelSize(R.styleable.ChooseView_choose_center_textsize, DEFAULT_TEXT_SIZE);
//        array.recycle();
    }

    private void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(mTextColor);
        paint.setTextSize(mTextSize);
        paint.setAntiAlias(true);
        Paint.FontMetrics metrics = paint.getFontMetrics();
        text_height = metrics.descent - metrics.ascent;

        paint.setTextSize(32);
        Paint.FontMetrics metrics1 = paint.getFontMetrics();
        text_height1 = metrics1.descent - metrics1.ascent;

        paint.setTextSize(88);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        draw_center_x = 0;
        draw_center_y = h / 2;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float y=draw_center_y+move;
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        float baseline = y-(fontMetrics.bottom + fontMetrics.top) / 2.0f;
        String text=str[location];
        canvas.drawText(text, draw_center_x, baseline, paint);
        drawTop(canvas);
        drawButtom(canvas);
        drawLines(canvas);
    }
    private void drawLines(Canvas canvas){
        float  height=(100-text_height1)/2;
        paint.setStrokeWidth(mLinesHeight);

        canvas.drawLine(0,draw_center_y+mCenterRectHeight/2+mLinesHeight/2,200,draw_center_y+mCenterRectHeight/2+mLinesHeight/2,paint);
        canvas.drawLine(0,draw_center_y-(mCenterRectHeight/2+mLinesHeight/2),200,draw_center_y-(mCenterRectHeight/2+mLinesHeight/2),paint);
    }
    private void drawTop(Canvas canvas) {
        float draw_y=mCenterRectHeight/2+mLinesHeight+mTextMarginHeight+text_height/2;

        if (move_type==MOVE_TYPE_UP){
            draw_y+=move;
            if (draw_y<mTextMarginHeight+text_height){
                draw_y=mTextMarginHeight+text_height;
            }
        }

        for (int i=0;i<3;i++) {
            float y=(draw_center_y+move)-draw_y;
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            float baseline = y-(fontMetrics.bottom + fontMetrics.top) / 2.0f;
            String text=str[location-i-1];
            canvas.drawText(text, draw_center_x, baseline, paint);
            draw_y+=mTextMarginHeight+text_height;
        }
    }

    private float xx;

    private void drawButtom(Canvas canvas) {
        float draw_y=mCenterRectHeight/2+mLinesHeight+mTextMarginHeight+text_height/2;

        if (move_type==MOVE_TYPE_UP){
            draw_y=mTextMarginHeight+text_height;
            if (move>text_height/2-(mLinesHeight+mCenterRectHeight/2)){
                draw_y+=move-(text_height/2-(mLinesHeight+mCenterRectHeight/2));
                if (draw_y>mCenterRectHeight/2+mLinesHeight+mTextMarginHeight+text_height/2){
                    draw_y=mCenterRectHeight/2+mLinesHeight+mTextMarginHeight+text_height/2;
                }
            }
        }
        if (move_type==MOVE_TYPE_DOWN){
            draw_y=mCenterRectHeight/2+mLinesHeight+mTextMarginHeight+text_height/2;
            draw_y-=move;
            if (draw_y<mTextMarginHeight+text_height){
                draw_y=mTextMarginHeight+text_height;
            }
        }

        for (int i=0;i<3;i++) {
            float y=draw_center_y+move+draw_y;
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            float baseline = y-(fontMetrics.bottom + fontMetrics.top) / 2.0f;
            String text=str[location+i+1];
            canvas.drawText(text, draw_center_x, baseline, paint);
            draw_y+=mTextMarginHeight+text_height;
        }
    }

    float location_down;
    int location_now;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                location_down=event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                move+=event.getY()-location_down;
                Log.d("asd",move+"");
                if (move<-(mCenterRectHeight/2+mLinesHeight/2)){
//                    location_now=location+1;
                    move+=mCenterRectHeight+mLinesHeight;
                    location++;
                    move_type=MOVE_TYPE_UP;
//                    move+=mCenterRectHeight/2;
                }else if (move>mCenterRectHeight/2+mLinesHeight/2){
//                    location_now=location-1;
                    move-=mCenterRectHeight+mLinesHeight;
                    location--;
                    move_type=MOVE_TYPE_DOWN;
//                    move-=mCenterRectHeight/2;
                }
                location_down=event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (move>0){
                    int a=((int) move)/50;
                    int b=((int) move)%50;
                }   else if (move<0){

                }
                break;
        }
        return true;
    }
}
