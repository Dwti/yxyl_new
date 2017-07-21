package com.yanxiu.gphone.student.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.questions.classify.ClassifyItemBean;
import com.yanxiu.gphone.student.util.HtmlImageGetterForClassify;

import java.util.ArrayList;
import java.util.List;

/**
 * 归类题，选项view（标签view）
 * Created by 戴延枫 on 2017/7/13.
 */
public class ClassifyChoice extends ViewGroup {

    private OnClassifyChoiceItemLitener mListener;
    /**
     * 存储所有的View，按行记录
     */
    public List<List<View>> mAllViews = new ArrayList<List<View>>();
    /**
     * 记录每一行的最大高度
     */
    private List<Integer> mLineHeight = new ArrayList<Integer>();
    private Context context;
    int mWidth,mHeight;

    public ClassifyChoice(Context context) {
        this(context, null);
    }

    public ClassifyChoice(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClassifyChoice(Context context, AttributeSet attrs, int defStyleAttr) {
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
        // 遍历所有的孩子
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child
                    .getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            // 如果已经需要换行
            if (childWidth + lp.leftMargin + lp.rightMargin + lineWidth > width) {
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
        int top = 0;
        // 得到总行数
        int lineNums = mAllViews.size();
        for (int i = 0; i < lineNums; i++) {
            // 每一行的所有的views
            lineViews = mAllViews.get(i);
            // 当前行的最大高度
            lineHeight = mLineHeight.get(i);


            // 遍历当前行所有的View
            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) child
                        .getLayoutParams();

                //计算childView的left,top,right,bottom
                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();


                child.layout(lc, tc, rc, bc);

                left += child.getMeasuredWidth() + lp.rightMargin
                        + lp.leftMargin;
            }
            left = 0;
            top += lineHeight;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
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
        mWidth = ClassifyChoice.this.getMeasuredWidth()-ClassifyChoice.this.getPaddingRight()-ClassifyChoice.this.getPaddingLeft();
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
     * 归类题中使用
     *
     * @param classfyItem
     * @param clickLitener
     */
    public void setData(List<String> classfyItem, OnClassifyChoiceItemLitener clickLitener) {
        mListener = clickLitener;
        LayoutInflater inflater = LayoutInflater.from(context);
        this.removeAllViews();
        for (int i = 0; i < classfyItem.size(); i++) {
//            final TextView view = (TextView) inflater.inflate(R.layout.classfy_layout_textview, null);
            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.classfy_layout_textview, null);
            final TextView view = (TextView) layout.findViewById(R.id.classfy_choice_text);
            final String content = classfyItem.get(i);
            if (null == content) {
                continue;
            }
            if (content.startsWith("<img src=")) {
                //img
                int img_height = getResources().getDimensionPixelSize(R.dimen.classify_choice_img_height);
                LinearLayout.LayoutParams textView_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, img_height);
                view.setLayoutParams(textView_lp);
            }else{
                int paddingLeft = getResources().getDimensionPixelSize(R.dimen.classify_choice_padding);
                int paddingTop = getResources().getDimensionPixelSize(R.dimen.classify_choice_Text_paddingTop);
                view.setPadding(paddingLeft,paddingTop,paddingLeft,paddingTop);
            }

            view.post(new Runnable() {
                @Override
                public void run() {
                    Spanned spanned = Html.fromHtml(content, new HtmlImageGetterForClassify(view,mWidth,mHeight), null);
                    view.setText(spanned);
                }
            });
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            int margin = getResources().getDimensionPixelSize(R.dimen.classify_choice_margin);
            lp.setMargins(0, 0, margin, margin);
            layout.setLayoutParams(lp);
            layout.setTag(content);
            layout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onClassifyChoiceItemClick(v);
                    }
                }
            });
            this.addView(layout);
        }
    }

    /**
     * 归类题中刷新控件使用
     *
     * @param classfyItem
     */
    public void refreshData(List<String> classfyItem) {
        ArrayList<View> tempList = new ArrayList<>();//临时存放要删除的view
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if(classfyItem.contains(child.getTag())){
                tempList.add(child);
            }
        }
        for(View child : tempList){
            removeView(child);
        }
        classfyItem.clear();
        tempList.clear();
    }

    /**
     * 归类抽屉布局使用
     */
    public void setDataForDrawer(ArrayList<String> choiceList, List<String> classfyItem, OnClassifyChoiceItemLitener clickLitener) {
        mListener = clickLitener;
        LayoutInflater inflater = LayoutInflater.from(context);
        this.removeAllViews();
        for (int i = 0; i < classfyItem.size(); i++) {
            final RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.classfy_drawer_item, null);
            final TextView textView = (TextView) layout.findViewById(R.id.classfy_choice_text);
            ImageView closeView = (ImageView) layout.findViewById(R.id.classify_drawer_close);
            int id = -1;
            try {
                id = Integer.parseInt(classfyItem.get(i));//获取id
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (id == -1) {
                continue;
            }
            final String content = choiceList.get(id);//获取chioce的name
            if (null == content) {
                continue;
            }
            if (content.startsWith("<img src=")) {
                //img
                int img_height = getResources().getDimensionPixelSize(R.dimen.classify_choice_img_height);
                RelativeLayout.LayoutParams textView_lp = (RelativeLayout.LayoutParams) textView.getLayoutParams();
                textView_lp.height = img_height;
                textView.setLayoutParams(textView_lp);
            }else{
                int paddingLeft = getResources().getDimensionPixelSize(R.dimen.classify_choice_padding);
                int paddingTop = getResources().getDimensionPixelSize(R.dimen.classify_choice_Text_paddingTop);
                textView.setPadding(paddingLeft,paddingTop,paddingLeft,paddingTop);
            }


            textView.post(new Runnable() {
                @Override
                public void run() {
                    Spanned spanned = Html.fromHtml(content, new HtmlImageGetterForClassify(textView,mWidth,mHeight), null);
                    textView.setText(spanned);
                }
            });

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            int margin = getResources().getDimensionPixelSize(R.dimen.classify_choice_margin_drawer);
            lp.setMargins(0, 0, margin, margin);
            layout.setLayoutParams(lp);
            closeView.setTag(content);
            closeView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeView(layout);
                    if (getChildCount() == 0) {
                        setVisibility(GONE);
                    }
                    if (mListener != null) {
                        mListener.onClassifyChoiceItemCloseClick(v);
                    }
                }
            });
            this.addView(layout);
        }
    }

    /**
     * 归类解析使用
     */
    public void setDataForAnalysis(ArrayList<ClassifyItemBean> classifyItemBeanArrayList) {
        LayoutInflater inflater = LayoutInflater.from(context);
        for (int i = 0; i < classifyItemBeanArrayList.size(); i++) {
            final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.classfy_analysis_chioce_item, null);
            final TextView textView = (TextView) layout.findViewById(R.id.classfy_choice_text);

            ClassifyItemBean classifyItemBean = classifyItemBeanArrayList.get(i);
            if(classifyItemBean.isRight()){
                textView.setBackgroundResource(R.drawable.shape_crop_reset_normal);
            }else{
                textView.setBackgroundResource(R.drawable.shape_classify_item_wrong_bg);
            }
            final String content = classifyItemBean.getContent();//获取chioce的name
            if (null == content) {
                continue;
            }
            if (content.startsWith("<img src=")) {
                //img
                int img_height = getResources().getDimensionPixelSize(R.dimen.classify_choice_img_height);
                LinearLayout.LayoutParams textView_lp = (LinearLayout.LayoutParams) textView.getLayoutParams();
                textView_lp.height = img_height;
                textView.setLayoutParams(textView_lp);
            }else{
                int paddingLeft = getResources().getDimensionPixelSize(R.dimen.classify_choice_padding);
                int paddingTop = getResources().getDimensionPixelSize(R.dimen.classify_choice_Text_paddingTop);
                textView.setPadding(paddingLeft,paddingTop,paddingLeft,paddingTop);
            }


            textView.post(new Runnable() {
                @Override
                public void run() {
                    Spanned spanned = Html.fromHtml(content, new HtmlImageGetterForClassify(textView,mWidth,mHeight), null);
                    textView.setText(spanned);
                }
            });

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            int margin = getResources().getDimensionPixelSize(R.dimen.classify_choice_margin);
            lp.setMargins(0, 0, margin, margin);
            layout.setLayoutParams(lp);
            this.addView(layout);
        }
    }

    public void clearFocuse() {
        int count = this.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = this.getChildAt(i);
            if (view instanceof TextView) {
                view.setOnClickListener(null);
            }
        }
    }

//    public void setViewBackground(int id) {
//        int number = this.getChildCount();
//        for (int i = 0; i < number; i++) {
//            if (((ClassfyBean) this.getChildAt(i).getTag()).getId() == id) {
//                this.getChildAt(i).setAlpha(0.5f);
//            } else {
//                this.getChildAt(i).setAlpha(1.0f);
//            }
//        }
//    }

    /**
     * ItemClick的回调接口
     *
     * @author dyf
     */
    public interface OnClassifyChoiceItemLitener {
        /**
         * 归类题，选项内容点击回调
         *
         * @param view
         */
        void onClassifyChoiceItemClick(View view);

        /**
         * 归类题，选项内容取消按钮点击回调
         *
         * @param view
         */
        void onClassifyChoiceItemCloseClick(View view);
    }
}
