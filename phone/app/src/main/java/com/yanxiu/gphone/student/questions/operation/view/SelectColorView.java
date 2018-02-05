package com.yanxiu.gphone.student.questions.operation.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.questions.operation.SelectColorAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2018/1/12.
 */

public class SelectColorView extends FrameLayout {

    private GridView mGridView;
    private View mBackView;
    private SelectColorAdapter mAdapter ;
    private OnSelectedColorChangedListener mOnSelectedColorChangedListener;
    private OnColorBoardDismissListener mOnColorBoardDismissListener;
    public SelectColorView(@NonNull Context context) {
        super(context);
    }

    public SelectColorView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SelectColorView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SelectColorView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        View root = LayoutInflater.from(context).inflate(R.layout.layout_palette,this,true);
        mGridView = (GridView) root.findViewById(R.id.gridView);
        mBackView = root.findViewById(R.id.back);
        mBackView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(INVISIBLE);
                if(mOnColorBoardDismissListener != null){
                    mOnColorBoardDismissListener.onDismiss();
                }
            }
        });
        setupGridView();
    }

    private void setupGridView(){
        List<Integer> colors = new ArrayList<>(8);
        colors.add(getResources().getColor(R.color.palette_colo10));
        colors.add(getResources().getColor(R.color.palette_colo11));
        colors.add(getResources().getColor(R.color.palette_colo12));
        colors.add(getResources().getColor(R.color.palette_colo13));
        colors.add(getResources().getColor(R.color.palette_colo14));
        colors.add(getResources().getColor(R.color.palette_colo15));
        colors.add(getResources().getColor(R.color.palette_colo16));
        colors.add(getResources().getColor(R.color.palette_colo17));
        mAdapter = new SelectColorAdapter(colors);
        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CircleView circleView = (CircleView) view.findViewById(R.id.circleView);
                if(circleView != mAdapter.getSelectedColorView()){
                    circleView.setCircleStyle(CircleView.CircleStyle.RING);
                    mAdapter.getSelectedColorView().setCircleStyle(CircleView.CircleStyle.SOLID);
                    mAdapter.setSelectedColorView(circleView);
                    if(mOnSelectedColorChangedListener != null){
                        mOnSelectedColorChangedListener.onColorChanged(circleView.getCircleColor());
                    }
                }

            }
        });
    }

    public interface OnSelectedColorChangedListener{
        void onColorChanged(int color);
    }

    public interface OnColorBoardDismissListener {
        void onDismiss();
    }

    public OnSelectedColorChangedListener getOnSelectedColorChangedListener() {
        return mOnSelectedColorChangedListener;
    }

    public void setOnSelectedColorChangedListener(OnSelectedColorChangedListener onSelectedColorChangedListener) {
        mOnSelectedColorChangedListener = onSelectedColorChangedListener;
    }

    public OnColorBoardDismissListener getOnColorBoardDismissListener() {
        return mOnColorBoardDismissListener;
    }

    public void setOnColorBoardDismissListener(OnColorBoardDismissListener onColorBoardDismissListener) {
        mOnColorBoardDismissListener = onColorBoardDismissListener;
    }
}
