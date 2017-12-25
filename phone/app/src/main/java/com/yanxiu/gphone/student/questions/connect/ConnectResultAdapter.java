package com.yanxiu.gphone.student.questions.connect;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.customviews.LoadingView;
import com.yanxiu.gphone.student.util.HtmlImageGetter;
import com.yanxiu.gphone.student.util.ScreenUtils;

import java.util.List;

/**
 * Created by sunpeng on 2017/7/13.
 */

public class ConnectResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ConnectedBean> mData;

    private OnItemDeletedListener mOnItemDeletedListener;

    private FrameLayout mAnimationLayout;

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_EMPTY = 1;

    public void setOnItemDeteleListener(OnItemDeletedListener onItemDeletedListener) {
        this.mOnItemDeletedListener = onItemDeletedListener;
    }

    public ConnectResultAdapter(List<ConnectedBean> data) {
        this.mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_NORMAL){
            return new ConnectedItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_connected,parent,false));
        }else {
            return new EmptyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.connect_basket_empty_view,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ConnectedItemViewHolder){

            ((ConnectedItemViewHolder)holder).ll_left.setVisibility(View.VISIBLE);
            ((ConnectedItemViewHolder)holder).ll_right.setVisibility(View.VISIBLE);
            ((ConnectedItemViewHolder)holder).delete.setClickable(true);
            ((ConnectedItemViewHolder)holder).delete.setLongClickable(true);
            holder.itemView.post(new Runnable() {
                @Override
                public void run() {
                    ((ConnectedItemViewHolder)holder).leftText.setText(Html.fromHtml(mData.get(position).getLeftItem().getText(),new HtmlImageGetter(((ConnectedItemViewHolder)holder).leftText),null));
                    ((ConnectedItemViewHolder)holder).rightText.setText(Html.fromHtml(mData.get(position).getRightItem().getText(),new HtmlImageGetter(((ConnectedItemViewHolder)holder).rightText),null));
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mData.size()==0?1:mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mData.size() == 0 ){
            return TYPE_EMPTY;
        }else {
            return TYPE_NORMAL;
        }
    }

    private void remove(int index){
        mData.remove(index);
        notifyItemRemoved(index);
    }

    public void clear(){
        mData.clear();
        notifyDataSetChanged();
    }

    public void setAnimationLayout(FrameLayout frameLayout){
        mAnimationLayout = frameLayout;
    }

    class ConnectedItemViewHolder extends RecyclerView.ViewHolder{
        private TextView leftText, rightText;
        private View delete,ll_left,ll_right;
        private LinearLayout ll_content;
        private ConnectedBean bean;
        public ConnectedItemViewHolder(final View itemView) {
            super(itemView);
            ll_content = (LinearLayout) itemView.findViewById(R.id.ll_content);
            leftText = (TextView) itemView.findViewById(R.id.textLeft);
            rightText = (TextView) itemView.findViewById(R.id.textRight);
            delete = itemView.findViewById(R.id.delete);
            ll_left = itemView.findViewById(R.id.ll_left);
            ll_right = itemView.findViewById(R.id.ll_right);


            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete.setClickable(false);
                    delete.setLongClickable(false);

                    bean = mData.get(getLayoutPosition());

                    int centerX = itemView.getWidth() / 2 ;
                    int centerY = itemView.getHeight() / 2;
                    int margin = ScreenUtils.dpToPxInt(itemView.getContext(),2);
                    int dis = margin + delete.getWidth() / 2;

                    Bitmap bmpLeft,bmpRight;
                    bmpLeft = ConnectAnimationHelper.getDrawBitmap(ll_left);
                    bmpRight = ConnectAnimationHelper.getDrawBitmap(ll_right);

                    final ImageView imgLeft = new ImageView(itemView.getContext());
                    imgLeft.setImageBitmap(bmpLeft);

                    final ImageView imgRight = new ImageView(itemView.getContext());
                    imgRight.setImageBitmap(bmpRight);

                    float pivotXLeft,pivotXRight,pivotY;
                    int[] locationLeft = new int[2];
                    int[] locationRight = new int[2];
                    ll_left.getLocationInWindow(locationLeft);
                    ll_right.getLocationInWindow(locationRight);

                    locationLeft[0] += centerX - delete.getWidth() / 2 - margin - ll_left.getWidth();
                    locationLeft[1] += centerY - ll_left.getHeight() / 2;
                    locationRight[0] += centerX + delete.getWidth() / 2 + margin;
                    locationRight[1] += centerY - ll_right.getHeight() / 2;

                    ll_left.setVisibility(View.INVISIBLE);
                    ll_right.setVisibility(View.INVISIBLE);



                    FrameLayout.LayoutParams paramsLeft = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    paramsLeft.leftMargin = locationLeft[0];
                    paramsLeft.topMargin = locationLeft[1];
                    imgLeft.setLayoutParams(paramsLeft);
                    mAnimationLayout.addView(imgLeft);

                    FrameLayout.LayoutParams paramsRight = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    paramsRight.leftMargin = locationRight[0];
                    paramsRight.topMargin = locationRight[1];
                    imgRight.setLayoutParams(paramsRight);
                    mAnimationLayout.addView(imgRight);

                    float scale = dis / ll_left.getWidth();
                    pivotXLeft = 1.0f + scale;
                    pivotXRight = - scale;
                    pivotY = 0.5f;

                    AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);
                    alphaAnimation.setInterpolator(new AccelerateInterpolator());
                    alphaAnimation.setFillAfter(true);
                    alphaAnimation.setDuration(300);
//                    delete.startAnimation(alphaAnimation);
                    ConnectAnimationHelper.startDeleteAnimation(imgLeft,360,270,pivotXLeft,pivotY,null);
                    ConnectAnimationHelper.startDeleteAnimation(imgRight, 0, 90, pivotXRight, pivotY, new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            remove(getLayoutPosition());
                            if(mOnItemDeletedListener !=null){
                                mOnItemDeletedListener.onDeleted(bean);
                            }
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Log.d("asd","onAnimationEnd");
                            mAnimationLayout.removeView(imgLeft);
                            mAnimationLayout.removeView(imgRight);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                            Log.d("asd","onAnimationRepeat");
                        }
                    });

                }
            });
        }
    }

    class EmptyViewHolder extends RecyclerView.ViewHolder{

        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void clearAnim(){
        if (mAnimationLayout!=null){
            mAnimationLayout.removeAllViews();
        }
    }

    public interface OnItemDeletedListener {
        void onDeleted(ConnectedBean bean);
    }
}
