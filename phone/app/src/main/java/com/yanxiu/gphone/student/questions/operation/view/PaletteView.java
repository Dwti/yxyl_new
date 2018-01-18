package com.yanxiu.gphone.student.questions.operation.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.questions.operation.TouchMode;
import com.yanxiu.gphone.student.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2017/11/8.
 */

public class PaletteView extends View {

    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private static final int TIME_INTERVAL = 500;  //时间间隔（用来判定是单指触摸还是多指）
    private Paint mPaint;
    private Path mPath;
    private DashPathEffect mThinDashPathEffect;  //细线
    private DashPathEffect mBoldDashPathEffect;  //粗线
    private LineMode mLineMode = LineMode.NONE;

    private float mLineWidth,mDefaultLineWidth;
    private int mEraseWidth;
    private int mColor;
    long mActionDownBegin = 0;  //单根手指按下的时间
    long mActionPointerDownBegin = 0; //第二根或者以上的手指按下屏幕
    private PointF mLastPoint0 = new PointF();
    private float mLastDis;
    private float mLineStartX,mLineStartY; //画直线的时候，保存的直线的起始点坐标

    private PointF midPoint;
    private PointF mP1,mP2; //测试画线用
    private Boolean mIsMultiTouch = false;

    private Bitmap mBufferBitmap;
    private Canvas mBufferCanvas;

    private PaintMode mPaintMode;
    private TouchMode mTouchMode;

    private Xfermode mClearMode;
    private Matrix mMatrix;

    private Bitmap mBgBitmap;

    private Line mBaseLine; //尺子的基准线

    private boolean mShouldBaseRuler = false;

    private ArrayList<PathDrawingInfo> mCachedPathList;
    private ArrayList<PathDrawingInfo> mRemovedPathList;

    private UndoStatusChangedListener mUndoStatusChangedListener;
    private RedoStatusChangedListener mRedoStatusChangedListener;

    public enum PaintMode {
        DRAW,
        ERASER
    }

    public enum LineMode{
        STRAIGHT,  //直线
        DOTTED,    //虚线
        NONE       //任意画
    }


    public PaletteView(Context context) {
        super(context);
        init();
    }

    public PaletteView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaletteView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PaletteView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mPaintMode = PaintMode.DRAW;
        mPath = new Path();
        mMatrix = new Matrix();

        mThinDashPathEffect = new DashPathEffect(new float[] { ScreenUtils.dpToPx(getContext(),3),ScreenUtils.dpToPx(getContext(),5) }, 0);
        mBoldDashPathEffect = new DashPathEffect(new float[] { ScreenUtils.dpToPx(getContext(),3),ScreenUtils.dpToPx(getContext(),15) }, 0);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setFilterBitmap(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mDefaultLineWidth = ScreenUtils.dpToPx(getContext(),2);
        mLineWidth = mDefaultLineWidth;
        mColor = Color.BLACK;
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setColor(mColor);

        mClearMode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

        mCachedPathList = new ArrayList<>();
        mRemovedPathList = new ArrayList<>();


    }

    private void initDrawBuffer() {
        mBufferBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mBufferCanvas = new Canvas(mBufferBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mBgBitmap == null){
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mm);
            mBgBitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight());
        }

        float  tranX = 0 ,tranY = 0;
//        canvas.save();
        if(mBgBitmap.getWidth() < getWidth()){
            tranX = ( getWidth() - mBgBitmap.getWidth() ) / 2 ;
        }
        if(mBgBitmap.getHeight() < getHeight()){
            tranY = (getHeight() - mBgBitmap.getHeight()) / 2;
        }
//        canvas.translate(tranX,tranY);
        canvas.drawBitmap(mBgBitmap,mMatrix,null);
//        canvas.restore();

        if (mBufferBitmap != null) {
            canvas.drawBitmap(mBufferBitmap,mMatrix,null);
        }

        if(mP1 != null && mP2 != null){
            canvas.drawLine(mP1.x,mP1.y,mP2.x,mP2.y,mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
         final  int action = event.getAction() & MotionEvent.ACTION_MASK;
         final float x = event.getX();
         final float y = event.getY();
         float[] values = new float[9];
         mMatrix.getValues(values);
         float drawX = x;
         float drawY = y;
         if(mBaseLine != null){
             if(action == MotionEvent.ACTION_DOWN && mBaseLine.distanceToPoint(drawX,drawY) < 150){
                 PointF p = mBaseLine.shadowPoint(drawX,drawY);
                 drawX = p.x + mLineWidth;
                 drawY = p.y;
                 mShouldBaseRuler = true;
             }
             if(mTouchMode == TouchMode.SINGLE_TOUCH && mShouldBaseRuler){
                 PointF p = mBaseLine.shadowPoint(drawX,drawY);
                 drawX = p.x + mLineWidth;
                 drawY = p.y;
             }
         }
         float matrixX =( drawX - values[Matrix.MTRANS_X] ) / values[Matrix.MSCALE_X];
         float matrixY = (drawY - values[Matrix.MTRANS_Y]) / values[Matrix.MSCALE_Y];
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mTouchMode = TouchMode.SINGLE_TOUCH;
                mActionDownBegin = SystemClock.uptimeMillis();
                mLineStartX = matrixX;
                mLineStartY = matrixY;
                mPath.moveTo(matrixX, matrixY);
                mLastPoint0.set(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                //判断是否是多根手指的原因是因为存在一种情况就是一开始是multiTouch,但是过程中，抬起了几根手指，只剩下一根手指
                if(mTouchMode == TouchMode.MULTITOUCH){
                    float dis = distance(event);
                    int multiTouchGesture = -1;
                    if(Math.abs(dis - mLastDis) < 10){
                        multiTouchGesture = DRAG;
                    }else {
                        multiTouchGesture = ZOOM;
                    }
                    // 拖拉图片
                    if (multiTouchGesture == DRAG) {
                        float dx = event.getX() - mLastPoint0.x; // 得到x轴的移动距离
                        float dy = event.getY() - mLastPoint0.y; // 得到y轴的移动距离
                        mMatrix.postTranslate(dx, dy);
                    }
                    // 放大缩小图片
                    else if (multiTouchGesture == ZOOM) {
                        midPoint = mid(event);
                        float endDis = distance(event);// 结束距离
                        float scale = endDis / mLastDis;// 得到缩放倍数
                        mMatrix.postScale(scale, scale,midPoint.x,midPoint.y);
                    }

                    mLastDis = dis;
                    mLastPoint0.set(event.getX(), event.getY());
                    postInvalidate();

                }else if(mTouchMode == TouchMode.SINGLE_TOUCH){
                    if (mBufferBitmap == null)
                        initDrawBuffer();
                    if(mLineMode == LineMode.NONE || mPaintMode == PaintMode.ERASER){
                        mPath.lineTo(matrixX, matrixY);
                        mBufferCanvas.drawPath(mPath, mPaint);

                    }else {
                        //清除上一次画的线(也就是当前的path，因为当前的path还没保存进去，只是画上去了)
                        reDrawBufferedPath();

                        mPath.reset();
                        mPath.moveTo(mLineStartX,mLineStartY);
                        mPath.lineTo(matrixX,matrixY);
                        mBufferCanvas.drawPath(mPath, mPaint);
                    }
                    postInvalidate();

                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mTouchMode = TouchMode.NONE;
                break;
            case MotionEvent.ACTION_UP:
                if(!mPath.isEmpty()){
                    savePathDrawingInfo(mPaint, mPath);
                    //清除反撤销
                    boolean isRemovedPathEmpty = mRemovedPathList.isEmpty();
                    mRemovedPathList.clear();
                    if(mRedoStatusChangedListener != null && !isRemovedPathEmpty){
                        mRedoStatusChangedListener.onStatusChanged(false);
                    }
                }
                mPath.reset();
                mTouchMode = TouchMode.SINGLE_TOUCH;
                mShouldBaseRuler = false;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //优化体验，以免想双指操作的时候，因为时间差，第一个手指被判定为画线操作
                if(!mPath.isEmpty()){
                    mActionPointerDownBegin = SystemClock.uptimeMillis();
                    if(mActionPointerDownBegin - mActionDownBegin < TIME_INTERVAL){
                        mPath.reset();
                        reDraw();
                        mTouchMode = TouchMode.MULTITOUCH;
                        mLastDis = distance(event);
                    }else {
                        mTouchMode = TouchMode.NONE;
                    }
                }
                break;
            default:
                break;
        }

        return true;
    }

    private float distance(MotionEvent event) {
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    private PointF mid(MotionEvent event) {
        float midX = (event.getX(1) + event.getX(0)) / 2;
        float midY = (event.getY(1) + event.getY(0)) / 2;
        return new PointF(midX, midY);
    }

    private void savePathDrawingInfo(Paint paint, Path path) {
        PathDrawingInfo cachedPathDrawingInfo = new PathDrawingInfo();
        cachedPathDrawingInfo.mPaint = new Paint(paint);
        cachedPathDrawingInfo.mPath = new Path(path);
        mCachedPathList.add(cachedPathDrawingInfo);
        if(mUndoStatusChangedListener != null && mCachedPathList.size() == 1){
            mUndoStatusChangedListener.onStatusChanged(true);
        }
    }

    private void reDraw() {
        if (mBufferBitmap != null) {
            mBufferBitmap.eraseColor(Color.TRANSPARENT);
            for (PathDrawingInfo pathDrawingInfo : mCachedPathList) {
                pathDrawingInfo.draw(mBufferCanvas);
            }
            postInvalidate();
        }
    }

    /**
     * 只是重新画bitmap，但是view并不重绘
     */
    private void reDrawBufferedPath() {
        if (mBufferBitmap != null) {
            mBufferBitmap.eraseColor(Color.TRANSPARENT);
            for (PathDrawingInfo pathDrawingInfo : mCachedPathList) {
                pathDrawingInfo.draw(mBufferCanvas);
            }
        }
    }

    public void clear(){
        if(mBufferBitmap != null)
            mBufferBitmap.eraseColor(Color.TRANSPARENT);
        boolean isCachedPathEmpty = mCachedPathList.isEmpty();
        boolean isRemovedPathEmpty = mRemovedPathList.isEmpty();
        mCachedPathList.clear();
        mRemovedPathList.clear();
        postInvalidate();

        if(mUndoStatusChangedListener != null && !isCachedPathEmpty){
            mUndoStatusChangedListener.onStatusChanged(false);
        }
        if(mRedoStatusChangedListener != null && !isRemovedPathEmpty){
            mRedoStatusChangedListener.onStatusChanged(false);
        }
    }
    public void undo() {
        if(!canUndo())
            return;
        PathDrawingInfo removedPath = mCachedPathList.remove(mCachedPathList.size() - 1);
        mRemovedPathList.add(removedPath);
        reDraw();

        if(mUndoStatusChangedListener != null && mCachedPathList.isEmpty()){
            mUndoStatusChangedListener.onStatusChanged(false);
        }
        if(mRedoStatusChangedListener != null && mRemovedPathList.size() == 1){
            mRedoStatusChangedListener.onStatusChanged(true);
        }
    }

    public void redo(){
        if(!canRedo())
            return;
        PathDrawingInfo removedPath = mRemovedPathList.remove(mRemovedPathList.size() - 1);
        mCachedPathList.add(removedPath);
        reDraw();

        if(mUndoStatusChangedListener != null && mCachedPathList.size() == 1){
            mUndoStatusChangedListener.onStatusChanged(true);
        }
        if(mRedoStatusChangedListener != null && mRemovedPathList.isEmpty()){
            mRedoStatusChangedListener.onStatusChanged(false);
        }
    }

    private boolean canUndo() {
        return mCachedPathList != null && mCachedPathList.size() > 0;
    }

    private boolean canRedo() {
        return mRemovedPathList != null && mRemovedPathList.size() > 0;
    }

    public void setPaintMode(PaintMode mode) {
        if (mPaintMode != mode) {
            mPaintMode = mode;
            if (mPaintMode == PaintMode.DRAW) {
                mPaint.setXfermode(null);
                setPathEffect();
            } else if(mPaintMode == PaintMode.ERASER){
                mPaint.setXfermode(mClearMode);
                mPaint.setPathEffect(null);
            }
        }
    }

    public void setStrokeWidth(float width){
        if(mLineWidth != width){
            mLineWidth = width;
            mPaint.setStrokeWidth(width);
            setPathEffect();
        }
    }

    public void setLineMode(LineMode lineMode){
        if(mLineMode == lineMode){
            return;
        }else {
            mLineMode = lineMode;
        }
        setPathEffect();
    }

    private void setPathEffect() {
        if(mLineMode == LineMode.DOTTED){
            if(mLineWidth == mDefaultLineWidth){
                mPaint.setPathEffect(mThinDashPathEffect);
            }else {
                mPaint.setPathEffect(mBoldDashPathEffect);
            }
        }else {
            mPaint.setPathEffect(null);
        }
    }

    public PaintMode getPaintMode() {
        return mPaintMode;
    }

    public void setBaseLine(Line line){
        mBaseLine = line;
    }

    public void setLinePoints(PointF p1, PointF p2){
        this.mP1 = new PointF(p1.x,p1.y);
        this.mP2 = new PointF(p2.x,p2.y);
        postInvalidate();
    }

    public void setPaintColor(int color){
        if(mColor != color){
            mPaint.setColor(color);
        }
    }


    public Bitmap getBufferedBitmap(){
        return mBufferBitmap;
    }

    public void restoreBuffedBitmap(Bitmap bitmap){
        if(mBufferBitmap == null && bitmap != null){
            mBufferBitmap = bitmap;
            mBufferCanvas = new Canvas(mBufferBitmap);
            postInvalidate();
        }
    }

    public ArrayList<PathDrawingInfo> getCachedPathList() {
        return mCachedPathList;
    }

    public UndoStatusChangedListener getUndoStatusChangedListener() {
        return mUndoStatusChangedListener;
    }

    public void setUndoStatusChangedListener(UndoStatusChangedListener undoStatusChangedListener) {
        mUndoStatusChangedListener = undoStatusChangedListener;
    }

    public RedoStatusChangedListener getRedoStatusChangedListener() {
        return mRedoStatusChangedListener;
    }

    public void setRedoStatusChangedListener(RedoStatusChangedListener redoStatusChangedListener) {
        mRedoStatusChangedListener = redoStatusChangedListener;
    }

    public interface UndoStatusChangedListener {
        void onStatusChanged(boolean canUndo);
    }

    public interface RedoStatusChangedListener {
        void onStatusChanged(boolean canRedo);
    }
}
