package com.yanxiu.gphone.student.customviews;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/14 14:40.
 * Function :
 */
public class AlbumGridView extends RelativeLayout {

    private static final String TAG = "AlbumGridView";

    public static final int TYPE_IMAGE = 0x000;
    public static final int TYPE_CAMERA = 0x001;

    private Context mContext;
    private GridView mContentView;
    private WavesLayout mConverLayout;
    private TextView mButtonView;
    private GridAdapter mAdapter;
    private boolean mIsCanAddItem = true;

    private onClickListener mClickListener;
    private onItemChangedListener mItemChangedListener;

    private interface onItemChangeListener {
        void onItemNumChange(int num, ArrayList<String> paths);

        void onItemClick(int viewType, int position);

        void onDeleteClick(GridAdapter adapter, int position);
    }

    public interface onClickListener {
        void onClick(int Type, int position);
    }

    public interface onItemChangedListener {
        void onChanged(ArrayList<String> paths);
    }

    public AlbumGridView(Context context) {
        this(context, null);
    }

    public AlbumGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlbumGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initData();
    }

    private void initView(Context context) {
        this.mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.album_gridview, this);
        mContentView = (GridView) findViewById(R.id.gv_content);
        mConverLayout = (WavesLayout) findViewById(R.id.wl_conver);
        mButtonView = (TextView) findViewById(R.id.tv_login);
    }

    private void initData() {
        mButtonView.setOnClickListener(clickListener);

        mAdapter = new GridAdapter(mContext);
        mAdapter.setItemNumChangeListener(itemChangeListener);
        mContentView.setAdapter(mAdapter);
    }

    public void setCanAddItem(boolean isCanAddItem) {
        this.mIsCanAddItem = isCanAddItem;
        if (!mIsCanAddItem) {
            mConverLayout.setVisibility(GONE);
        }
        mAdapter.setCanAddItem(isCanAddItem);
    }

    public void setData(List<String> list) {
        if (list == null) {
            return;
        }
        if (mAdapter != null) {
            mAdapter.setData(list);
        }
        if (list.size() == 0) {
            mContentView.setVisibility(GONE);
            if (mIsCanAddItem) {
                mConverLayout.setVisibility(VISIBLE);
            } else {
                mConverLayout.setVisibility(GONE);
            }
        } else {
            mContentView.setVisibility(VISIBLE);
            mConverLayout.setVisibility(GONE);
        }
    }

    public void addData(String... paths) {
        if (paths == null) {
            return;
        }
        for (String path : paths) {
            if (mAdapter != null) {
                mAdapter.addData(path);
            }
        }
    }

    public void remove(int position) {
        mAdapter.remove(position);
    }

    public void addClickListener(onClickListener onClickListener) {
        this.mClickListener = onClickListener;
    }

    public void addItemChangedListener(onItemChangedListener mItemChangedListener) {
        this.mItemChangedListener = mItemChangedListener;
    }

    private onItemChangeListener itemChangeListener = new onItemChangeListener() {
        @Override
        public void onItemNumChange(int num, ArrayList<String> paths) {
            if (num == 0) {
                mContentView.setVisibility(GONE);
                if (mIsCanAddItem) {
                    mConverLayout.setVisibility(VISIBLE);
                } else {
                    mConverLayout.setVisibility(GONE);
                }
            } else {
                mContentView.setVisibility(VISIBLE);
                mConverLayout.setVisibility(GONE);
            }
            if (mItemChangedListener != null) {
                mItemChangedListener.onChanged(paths);
            }
        }

        @Override
        public void onItemClick(int viewType, int position) {
            if (viewType == GridAdapter.TYPE_ONE) {
                if (mClickListener != null) {
                    mClickListener.onClick(TYPE_IMAGE, position);
                }
            } else {
                if (clickListener != null) {
                    clickListener.onClick(mButtonView);
                }
            }
        }

        @Override
        public void onDeleteClick(GridAdapter adapter, int position) {
            adapter.setModelDefault();
            adapter.remove(position);
            adapter.notifyDataSetChanged();
        }
    };

    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                mClickListener.onClick(TYPE_CAMERA, -1);
            }
        }
    };

    private class GridAdapter extends BaseAdapter {

        private static final int MAXNUMBER = 3;

        private static final int VIEWTYPECOUNT = 2;
        private static final int TYPE_ONE = 0x000;
        private static final int TYPE_TWO = 0x001;

        private static final String MODEL_DEFAULT = "default";
        private static final String MODEL_LONGPRESS = "long";
        private static final int MODEL_POSITION_DEFAULT = -1;

        private static final String DEFAULT_PATH = "xx";

        private LayoutInflater mInflater;
        private List<String> mDatas = new ArrayList<>();
        private String model = MODEL_DEFAULT;
        private int model_position = MODEL_POSITION_DEFAULT;
        private boolean isCanAddItem = true;
        private onItemChangeListener mItemChangeListener;

        GridAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        private void setData(List<String> list) {
            this.mDatas.clear();
            this.mDatas.addAll(list);
            this.mDatas.add(DEFAULT_PATH);
            this.notifyDataSetChanged();
        }

        private void setModelDefault() {
            model_position = MODEL_POSITION_DEFAULT;
            model = MODEL_DEFAULT;
        }

        private void setCanAddItem(boolean isCanAddItem) {
            this.isCanAddItem = isCanAddItem;
            this.notifyDataSetChanged();
        }

        private ArrayList<String> getData() {
            ArrayList<String> list = new ArrayList<>();
            list.addAll(this.mDatas);
            if (list.size() > 0) {
                list.remove(list.size() - 1);
            }
            return list;
        }

        private void addData(String path) {
            int position = this.mDatas.size();
            if (position > 0) {
                this.mDatas.add(position - 1, path);
            } else {
                this.mDatas.add(path);
                this.mDatas.add(DEFAULT_PATH);
            }
            this.notifyDataSetChanged();
            if (mItemChangeListener != null) {
                if (getCount() == 0) {
                    mItemChangeListener.onItemNumChange(getCount(), getData());
                } else {
                    mItemChangeListener.onItemNumChange(getCount() - 1, getData());
                }
            }
        }

        private void remove(int position) {
            if (position > -1 && position < mDatas.size()) {
                this.mDatas.remove(position);
                this.notifyDataSetChanged();
            }
            if (mItemChangeListener != null) {
                if (getCount() == 0) {
                    mItemChangeListener.onItemNumChange(getCount(), getData());
                } else {
                    mItemChangeListener.onItemNumChange(getCount() - 1, getData());
                }
            }
        }

        private void setItemNumChangeListener(onItemChangeListener mItemChangeListener) {
            this.mItemChangeListener = mItemChangeListener;
        }

        @Override
        public int getViewTypeCount() {
            return VIEWTYPECOUNT;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == mDatas.size() - 1 && isCanAddItem) {
                return TYPE_TWO;
            } else {
                return TYPE_ONE;
            }
        }

        @Override
        public int getCount() {
            int count = mDatas != null ? mDatas.size() : 0;
            if (count > 0 && !isCanAddItem) {
                count -= 1;
            }
            count = count > MAXNUMBER ? MAXNUMBER : count;
            Logger.d(TAG, "list  " + mDatas.toString());
            Logger.d(TAG, "getCount   " + count);
            return count;
        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Logger.d(TAG, "getView   " + position);
            final String path = mDatas.get(position);
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                if (path.equals(DEFAULT_PATH)) {
                    convertView = mInflater.inflate(R.layout.adapter_gridview_default, parent, false);
                    holder.mSiBgView= (ImageView) convertView.findViewById(R.id.si_bg);
                } else {
                    convertView = mInflater.inflate(R.layout.adapter_gridview, parent, false);
                    holder.mDeleteView = (ImageView) convertView.findViewById(R.id.iv_delete);
                    holder.mStrokeView = (ImageView) convertView.findViewById(R.id.iv_bg);
                    holder.mPictureView = (ImageView) convertView.findViewById(R.id.iv_picture);
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (!path.equals(DEFAULT_PATH)) {
                Glide.with(mInflater.getContext()).load(path).error(R.drawable.image_load_failed).into(holder.mPictureView);
                if (model.equals(MODEL_LONGPRESS) && model_position == position) {
                    holder.mDeleteView.setVisibility(VISIBLE);
                } else {
                    holder.mDeleteView.setVisibility(GONE);
                }
                holder.mDeleteView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mItemChangeListener != null) {
                            mItemChangeListener.onDeleteClick(GridAdapter.this, position);
                        }
                    }
                });

                if (!isCanAddItem) {
                    holder.mStrokeView.setBackground(ContextCompat.getDrawable(mInflater.getContext(), R.drawable.shape_rectangle_color_89e00d));
                } else {
                    holder.mStrokeView.setBackground(ContextCompat.getDrawable(mInflater.getContext(), R.drawable.shape_rectangle_color_fafafa));
                }
            }else {
                Glide.with(mInflater.getContext()).load(R.drawable.add_picture).into(holder.mSiBgView);
            }

            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (path.equals(DEFAULT_PATH)) {
                        if (mItemChangeListener != null) {
                            mItemChangeListener.onItemClick(TYPE_TWO, position);
                        }
                    } else {
                        if (model.equals(MODEL_LONGPRESS) && model_position == position) {
                            setModelDefault();
                            notifyDataSetChanged();
                        } else {
                            if (mItemChangeListener != null) {
                                mItemChangeListener.onItemClick(TYPE_ONE, position);
                            }
                        }
                    }
                }
            });

            convertView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    model = MODEL_LONGPRESS;
                    model_position = position;
                    notifyDataSetChanged();
                    return true;
                }
            });

            return convertView;
        }

        private class ViewHolder {
            ImageView mStrokeView;
            ImageView mPictureView;
            ImageView mDeleteView;
            ImageView mSiBgView;
        }
    }

}
