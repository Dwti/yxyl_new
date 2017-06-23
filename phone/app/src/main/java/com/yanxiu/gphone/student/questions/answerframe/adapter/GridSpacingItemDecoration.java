package com.yanxiu.gphone.student.questions.answerframe.adapter;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by anonymous on 2016/9/9 0009.
 * <p>
 * GridSpacingItemDecoration
 * <p>
 * int spanCount = 3; // 3 columns
 * int spacing = 50; // 50px
 * boolean includeEdge = false;
 * recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
 */

/**
 * Created by 戴延枫 on 2017/6/15.
 */

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;
    private boolean includeEdge;
    private int leftRight;
    private int topBottom;


//    public GridSpacingItemDecoration(int leftRight, int topBottom) {
//        this.leftRight = leftRight;
//        this.topBottom = topBottom;
//    }
    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

//    @Override
//    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
//        //判断总的数量是否可以整除
//        int totalCount = layoutManager.getItemCount();
//        int surplusCount = totalCount % layoutManager.getSpanCount();
//        int childPosition = parent.getChildAdapterPosition(view);
//        if (layoutManager.getOrientation() == GridLayoutManager.VERTICAL) {//竖直方向的
//            if (surplusCount == 0 && childPosition > totalCount - layoutManager.getSpanCount() - 1) {
//                //后面几项需要bottom
//                outRect.bottom = topBottom;
//            } else if (surplusCount != 0 && childPosition > totalCount - surplusCount - 1) {
//                outRect.bottom = topBottom;
//            }
//            if ((childPosition + 1) % layoutManager.getSpanCount() == 0) {//被整除的需要右边
//                outRect.right = leftRight;
//            }
//            outRect.top = topBottom;
//            outRect.left = leftRight;
//        } else {
//            if (surplusCount == 0 && childPosition > totalCount - layoutManager.getSpanCount() - 1) {
//                //后面几项需要右边
//                outRect.right = leftRight;
//            } else if (surplusCount != 0 && childPosition > totalCount - surplusCount - 1) {
//                outRect.right = leftRight;
//            }
//            if ((childPosition + 1) % layoutManager.getSpanCount() == 0) {//被整除的需要下边
//                outRect.bottom = topBottom;
//            }
//            outRect.top = topBottom;
//            outRect.left = leftRight;
//        }
//    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spacing;
            }
            outRect.bottom = spacing; // item bottom
        } else {
            outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacing; // item top
            }
        }
    }
}
