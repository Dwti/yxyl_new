package com.yanxiu.gphone.student.questions.connect;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.util.HtmlImageGetter;
import com.yanxiu.gphone.student.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2017/7/14.
 */

public class ConnectedView extends LinearLayout {

    private boolean isAllChildAdded = false;
    private List<ConnectPositionInfo> mConnectPositionInfo;
    public ConnectedView(Context context) {
        super(context);
        init();
    }

    public ConnectedView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ConnectedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ConnectedView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isAllChildAdded){
            drawLines(canvas,calculateLinePoints(mConnectPositionInfo));
        }
    }


    private void init(){
        setWillNotDraw(false);
        addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                invalidate();
            }
        });
    }

    public void setConnectPositionInfo(List<ConnectPositionInfo> list ){
        mConnectPositionInfo = list;
    }
    private List<LinePoints> calculateLinePoints(List<ConnectPositionInfo> connectPositionInfoList){
        if(connectPositionInfoList == null || connectPositionInfoList.size() == 0)
            return null;
        List<LinePoints> result = new ArrayList<>();
        for(ConnectPositionInfo positionInfo: connectPositionInfoList){
            View leftChild = getChildAt(positionInfo.getLeftPosition());
            View rightChild = getChildAt(positionInfo.getRightPosition());
            View leftItem = leftChild.findViewById(R.id.text_left);
            View rightItem = rightChild.findViewById(R.id.text_right);
            if(leftItem == null || rightItem ==null){
                result.add(null);
                continue;
            }
            Point leftPoint = new Point(leftItem.getRight(),leftChild.getTop() + leftChild.getHeight() / 2);
            Point rightPoint = new Point(rightItem.getLeft(),rightChild.getTop() + rightChild.getHeight() / 2);
            int color;
            if(positionInfo.isRight()){
                color = Color.parseColor("#89e00d");
            }else {
                color = Color.parseColor("#ff7a05");
            }
            result.add(new LinePoints(leftPoint,rightPoint,color));
        }
        return result;
    }

    public void addItems(List<String> leftData, List<String> rightData, List<ConnectPositionInfo> connPosInfos){
        if(leftData == null || rightData == null || leftData.size() != rightData.size())
            return;
        for(int i = 0;i < leftData.size();i++){
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_connect_analysis,this,false);
            TextView leftText = (TextView) view.findViewById(R.id.text_left);
            TextView rightText = (TextView) view.findViewById(R.id.text_right);
            for(ConnectPositionInfo info : connPosInfos){
                if(info.getLeftPosition() == i){
                    if(!info.isRight()){
                        leftText.setBackground(getResources().getDrawable(R.drawable.shape_connect_item_orange));
                    }
                }

                if(info.getRightPosition() == i){
                    if(!info.isRight()){
                        rightText.setBackground(getResources().getDrawable(R.drawable.shape_connect_item_orange));
                    }
                }
            }
            leftText.setText(Html.fromHtml(leftData.get(i),new HtmlImageGetter(leftText),null));
            rightText.setText(Html.fromHtml(rightData.get(i),new HtmlImageGetter(rightText),null));
            addView(view);
        }
        isAllChildAdded = true;
    }

    private void drawLines(Canvas canvas,List<LinePoints> points){
        if(points == null || points.size() == 0)
            return;
        float strokeWidth = ScreenUtils.dpToPx(getContext(),2);
        for(int i=0;i<points.size();i++){
            if(points.get(i) == null)
                continue;
            Paint paint = new Paint();
            paint.setColor(points.get(i).getLineColor());
            paint.setStrokeWidth(strokeWidth);
            paint.setAntiAlias(true); //抗锯齿
            canvas.drawLine(points.get(i).getLeftPoint().x,points.get(i).getLeftPoint().y,points.get(i).getRightPoint().x,points.get(i).getRightPoint().y,paint);
        }
    }
}
