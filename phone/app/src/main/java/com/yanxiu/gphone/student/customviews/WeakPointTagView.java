package com.yanxiu.gphone.student.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.learning.KnowledgePointLabelItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lufengqing on 2018/2/1.
 */

public class WeakPointTagView extends ViewGroup {

    /**
     * 存储所有的View，按行记录
     */
    public List<List<View>> mAllViews = new ArrayList<List<View>>();
    /**
     * 记录每一行的最大高度
     */
    private List<Integer> mLineHeight = new ArrayList<Integer>();
    private Context context;
    int mWidth, mHeight;
    private List<KnowledgePointLabelItem> mList;
    private int freeWidth;
    private int lastItem;
    private boolean isShowSetRight=false;
    private int mHeightMeasureSpec;
    private int mWidthMeasureSpec;
    private int mMinWidth;
    private boolean isShouldShowMore = false;
    private int top=0;

    public boolean isCollapseMode() {
        return isCollapseMode;
    }

    public void setCollapseMode(boolean collapseMode) {
        isCollapseMode = collapseMode;
    }

    boolean isCollapseMode = true;

    public WeakPointTagView(Context context) {
        this(context, null);
    }

    public WeakPointTagView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeakPointTagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeight.clear();

        int width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;
        // 存储每一行所有的childView
        List<View> lineViews = new ArrayList<View>();
        int cCount = getChildCount();
        int lineNum = 0;
        int modifyWidth = 0;
        isShowSetRight=false;
        // 遍历所有的孩子
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child
                    .getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            // 如果已经需要换行
            if (childWidth + lp.leftMargin + lp.rightMargin + lineWidth > width && lineWidth > 0) {
                lineNum++;
                if (i==cCount-1&&child.getTag()==null && isShouldShowMore){
                    isShowSetRight=true;
                }
                if (isCollapseMode && lineNum == 2 ) {
                    child = getChildAt(cCount - 1);
                    if(i < cCount - 1) {
                        isShouldShowMore = true;
                    }
                    lp = (MarginLayoutParams) child
                            .getLayoutParams();
                    childWidth = child.getMeasuredWidth();
                    childHeight = child.getMeasuredHeight();
                    while (childWidth + lp.leftMargin + lp.rightMargin + lineWidth > width) {
                        if(lineViews.size() == 0) {
                            break;
                        }
                        lastItem=i-1;
                        View lastChild = lineViews.remove(lineViews.size() - 1);
                        MarginLayoutParams lastLp = (MarginLayoutParams) child.getLayoutParams();
                        int lastChildWidth = lastChild.getMeasuredWidth();
                        int lastChildHeight = lastChild.getMeasuredHeight();
                        lineWidth -= (lastChildWidth+lastLp.leftMargin+lastLp.rightMargin);
                        if(childWidth + lp.leftMargin + lp.rightMargin + lineWidth < width) {
                            modifyWidth = width - (lineWidth + childWidth + lp.leftMargin + lp.rightMargin);
                            if(modifyWidth <mMinWidth) {
                                modifyWidth = 0;
                                isShouldShowMore = true;
                            } else {
                                freeWidth=modifyWidth;
                                lineViews.add(lastChild);
                            }
                        }
                    }
                    lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
                    lineHeight = childHeight + lp.topMargin
                            + lp.bottomMargin;
                    //standardLineHeight = Math.max(standardLineHeight, childHeight + lp.topMargin
                    //+ lp.bottomMargin);
                    lineViews.add(child);
                    break;
                }
                // 记录这一行所有的View以及最大高度
                mLineHeight.add(lineHeight);
                // 将当前行的childView保存，然后开启新的ArrayList保存下一行的childView
                mAllViews.add(lineViews);
                lineWidth = 0;// 重置行宽
                lineViews = new ArrayList<View>();
            }
            /**
             * 如果不需要换行，则累加
             */
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = childHeight + lp.topMargin
                    + lp.bottomMargin;
            //standardLineHeight = Math.max(standardLineHeight, childHeight + lp.topMargin
            //+ lp.bottomMargin);
            lineViews.add(child);
        }
        // 记录最后一行
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);

        int left = 0;
        top = 0;
        // 得到总行数
        int lineNums = mAllViews.size();
        if(isShouldShowMore) {
            getChildAt(cCount -1).setVisibility(VISIBLE);
        } else {
            getChildAt(cCount -1).setVisibility(GONE);
        }
        if (isCollapseMode && lineNums > 2) {
            lineNums = 2;
        }
        for (int i = 0; i < lineNums; i++) {
            // 每一行的所有的views
            lineViews = mAllViews.get(i);
            // 当前行的最大高度
            lineHeight = mLineHeight.get(i);

            if (isCollapseMode && i == 2) {
//                getLayoutParams().height=top;
//                setLayoutParams(getLayoutParams());
                return;
            }
            // 遍历当前行所有的View
            for (int j = 0; j < lineViews.size(); j++) {
                final View child = lineViews.get(j);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                final MarginLayoutParams lp = (MarginLayoutParams) child
                        .getLayoutParams();

                //计算childView的left,top,right,bottom
                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();

                if(isCollapseMode && i==1 && j == lineViews.size() -2 && modifyWidth > 0 && isShouldShowMore) {
                    rc = lc + modifyWidth-lp.rightMargin;
                    left += modifyWidth + lp.rightMargin
                            + lp.leftMargin;
                    child.getLayoutParams().width = modifyWidth-lp.leftMargin-lp.rightMargin;
                } else {
                    left += child.getMeasuredWidth() + lp.rightMargin
                            + lp.leftMargin;
                }
//                child.measure(modifyWidth, child.getMeasuredHeight());
                if (isShowSetRight&&i==mAllViews.size()-1&&j==lineViews.size()-1){
                    child.layout(mWidth-(rc-lc),tc,mWidth,bc);
                }else {
                    child.layout(lc, tc, rc, bc);
                }
            }
            left = 0;
            top += lineHeight;
        }
        post(new Runnable() {
            @Override
            public void run() {
                getLayoutParams().height=top;
                setLayoutParams(getLayoutParams());
            }
        });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidthMeasureSpec=widthMeasureSpec;
        mHeightMeasureSpec=heightMeasureSpec;
        //        measureChildren(widthMeasureSpec,heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获得它的父容器为它设置的测量模式和大小
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);


        // 如果是warp_content情况下，记录宽和高
        int width = 0;
        int height = 0;
        /**
         * 记录每一行的宽度，width不断取最大宽度
         */
        int lineWidth = 0;
        /**
         * 每一行的高度，累加至height
         */
        int lineHeight = 0;

        int cCount = getChildCount();

        // 遍历每个子元素
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            // 测量每一个child的宽和高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            // 得到child的lp
            MarginLayoutParams lp = (MarginLayoutParams) child
                    .getLayoutParams();
            // 当前子空间实际占据的宽度
            int childWidth = child.getMeasuredWidth() + lp.leftMargin
                    + lp.rightMargin;
            // 当前子空间实际占据的高度
            int childHeight = child.getMeasuredHeight() + lp.topMargin
                    + lp.bottomMargin;
            /**
             * 如果加入当前child，则超出最大宽度，则的到目前最大宽度给width，类加height 然后开启新行
             */
            if (lineWidth + childWidth > sizeWidth) {
                width = Math.max(lineWidth, childWidth);// 取最大的
                lineWidth = childWidth; // 重新开启新行，开始记录
                // 叠加当前高度，
                if (lineHeight == Math.max(lineHeight, childHeight)) {
                    height += lineHeight;
                } else {
                    height += Math.max(lineHeight, childHeight);
                }

                // 开启记录下一行的高度
                lineHeight = childHeight;
            } else
            // 否则累加值lineWidth,lineHeight取最大高度
            {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }
            // 如果是最后一个，则将当前记录的最大宽度和当前lineWidth做比较
            if (i == cCount - 1) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }

        }
        setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth : width, (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight : height);
        mWidth = WeakPointTagView.this.getMeasuredWidth() - WeakPointTagView.this.getPaddingRight() - WeakPointTagView.this.getPaddingLeft();
        mHeight = getResources().getDimensionPixelSize(R.dimen.classify_choice_img_height);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
    }

    /**
     * 报告中使用
     *
     * @param list
     */
    public void setData(final List<KnowledgePointLabelItem> list) {
        mList = list;
//        isCollapseMode=true;
        checkMinWidth(list);
        LayoutInflater inflater = LayoutInflater.from(context);
        this.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.weakpoint_tagview_layout, null);
            final TextView view = (TextView) layout.findViewById(R.id.classfy_choice_text);
            final String content = list.get(i).content;
            if (null == content) {
                continue;
            }
            view.setText(content);
            view.setTextSize(list.get(i).textSize);
            view.setTextColor(list.get(i).textColor);
            if (list.get(i).backGroundId != 0) {
                view.setBackgroundResource(list.get(i).backGroundId);
//                int paddingTop = getResources().getDimensionPixelSize(R.dimen.chioce_item_topmargin);
//                int paddingLeft = getResources().getDimensionPixelSize(R.dimen.chioce_item_leftmargin);
//                view.setPadding(paddingLeft, paddingTop, paddingLeft, paddingTop);
            }

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int marginRight = list.get(i).marginRight;
            lp.setMargins(0, 10, marginRight, 0);
            layout.setLayoutParams(lp);
            layout.setTag(content);
            this.addView(layout);
        }

        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.weakpoint_moreview_layout, null);
        final TextView view = (TextView) layout.findViewById(R.id.more_text);
        final ImageView imageView = (ImageView) layout.findViewById(R.id.more_img);
        if (isCollapseMode) {
            view.setText("更多");
            imageView.setImageResource(R.drawable.selector_more_text_img);
        } else {
            view.setText("收起");
            imageView.setImageResource(R.drawable.selector_collapse_img);
        }
        /* *
         * 这种改法确实能暂时满足需求，但是在部分情况下会出异常，希望以后修改
         * cwq
         * */
        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                setChildLayout();
                isCollapseMode = !isCollapseMode;
                if (isCollapseMode) {
                    setData(list);
                    LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) WeakPointTagView.this.getChildAt(lastItem).getLayoutParams();
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(freeWidth, LayoutParams.WRAP_CONTENT);
                    params.topMargin=layoutParams.topMargin;
                    params.rightMargin=layoutParams.rightMargin;
                    params.leftMargin=layoutParams.leftMargin;
                    params.bottomMargin=layoutParams.bottomMargin;
                    WeakPointTagView.this.getChildAt(lastItem).setLayoutParams(params);
                    view.setText("更多");
                    imageView.setImageResource(R.drawable.selector_more_text_img);
                } else {
                    setData(list);
                    LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) WeakPointTagView.this.getChildAt(lastItem).getLayoutParams();
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    params.topMargin=layoutParams.topMargin;
                    params.rightMargin=layoutParams.rightMargin;
                    params.leftMargin=layoutParams.leftMargin;
                    params.bottomMargin=layoutParams.bottomMargin;
                    WeakPointTagView.this.getChildAt(lastItem).setLayoutParams(params);
                    view.setText("收起");
                    imageView.setImageResource(R.drawable.selector_collapse_img);
                }
            }
        });
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 10, 0, 0);
        layout.setLayoutParams(lp);
        this.addView(layout);
    }

    private void checkMinWidth(List<KnowledgePointLabelItem> list){
        LinearLayout layout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.weakpoint_tagview_layout, null);
        final TextView view = (TextView) layout.findViewById(R.id.classfy_choice_text);
        view.setText("是...");
        view.setTextSize(list.get(0).textSize);
        view.setTextColor(list.get(0).textColor);
        view.setBackgroundResource(R.drawable.selector_knowledge_item_layout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int marginRight = list.get(0).marginRight;
        lp.setMargins(0, 10, marginRight, 0);
        layout.setLayoutParams(lp);

        measureChild(layout, mWidthMeasureSpec, mHeightMeasureSpec);
        int childWidth = layout.getMeasuredWidth();
        int childHeight = layout.getMeasuredHeight();
        mMinWidth=childWidth+10+marginRight;
    }

}

