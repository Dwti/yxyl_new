package com.yanxiu.gphone.student.questions.answerframe.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.EXueELianBaseResponse;
import com.yanxiu.gphone.student.base.OnPermissionCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.common.Bean.CropCallbackMessage;
import com.yanxiu.gphone.student.common.Bean.PhotoDeleteBean;
import com.yanxiu.gphone.student.common.activity.CameraActivity;
import com.yanxiu.gphone.student.common.activity.PhotoActivity;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;
import com.yanxiu.gphone.student.db.UrlRepository;
import com.yanxiu.gphone.student.http.request.UpDataRequest;
import com.yanxiu.gphone.student.questions.answerframe.ui.bean.NoteBean;
import com.yanxiu.gphone.student.questions.answerframe.ui.bean.UploadImgBean;
import com.yanxiu.gphone.student.questions.answerframe.ui.interfaces.onItemClickListener;
import com.yanxiu.gphone.student.questions.answerframe.ui.request.NotesRequest;
import com.yanxiu.gphone.student.util.EditTextManger;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

import static com.yanxiu.gphone.student.constant.Constants.NOTES_KEY;

/**
 * 笔记页面
 * Created by 戴延枫 on 2017/6/9.
 */

public class NotesActicity extends YanxiuBaseActivity implements View.OnClickListener, onItemClickListener, OnPermissionCallback {

    private static final String WQID = "wqid";
    private static final String QID = "qid";
    private static final String CONTENT = "content";
    private static final String PHOTOPATH = "photo_path";

    private Context mContext;
    private PublicLoadLayout rootView;
    private TextView mConfirmView;
    private ImageView mBackView;
    private TextView mTitleView;
    private View mTopView;
    private EditText mNotesEditText;
    private NotesAdapter mAdapter;

    private int mViewHashCode;
    private String mWqid;
    private String mQid;
    private String mContent;
    private ArrayList<String> mPhotoPath;

    /**
     * 跳转NotesActicity
     */
    public static void invoke(Context context, int key, String wqid, String qid, String content, ArrayList<String> photoPath) {
        Intent intent = new Intent(context, NotesActicity.class);
        intent.putExtra(NOTES_KEY, key);
        intent.putExtra(WQID, wqid);
        intent.putExtra(QID, qid);
        intent.putExtra(CONTENT, content);
        intent.putStringArrayListExtra(PHOTOPATH, photoPath);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = NotesActicity.this;
        EventBus.getDefault().register(mContext);
        rootView = new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_notes);
        setContentView(rootView);
        mViewHashCode = getIntent().getIntExtra(NOTES_KEY, -1);
        mWqid = getIntent().getStringExtra(WQID);
        mQid = getIntent().getStringExtra(QID);
        mContent = getIntent().getStringExtra(CONTENT);
        mPhotoPath = getIntent().getStringArrayListExtra(PHOTOPATH);
        initView();
        listener();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(mContext);
    }

    private void listener() {
        mBackView.setOnClickListener(NotesActicity.this);
        mConfirmView.setOnClickListener(NotesActicity.this);
        mAdapter.setListener(NotesActicity.this);
    }

    private void initView() {
        mNotesEditText = (EditText) findViewById(R.id.notesContent);
        GridView gridView = (GridView) findViewById(R.id.gv_content);
        mTopView = findViewById(R.id.include_top);
        mBackView = (ImageView) findViewById(R.id.iv_left);
        mTitleView = (TextView) findViewById(R.id.tv_title);
        mConfirmView = (TextView) findViewById(R.id.tv_right);
        mAdapter = new NotesAdapter(mContext);
        gridView.setAdapter(mAdapter);
    }

    private void initData() {
        mTitleView.setText(getText(R.string.edit_note));
        mTitleView.setTextColor(ContextCompat.getColor(mContext, R.color.color_666666));
        mTopView.setBackgroundColor(Color.WHITE);
        mBackView.setVisibility(View.VISIBLE);
        mConfirmView.setVisibility(View.VISIBLE);
        mConfirmView.setText(getText(R.string.ok));
        mConfirmView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.selector_choose_ensure_bg));
        if (mPhotoPath==null){
            mAdapter.setData(new ArrayList<String>());
        }else {
            mAdapter.setData(mPhotoPath);
        }
        if (!TextUtils.isEmpty(mContent)) {
            mNotesEditText.setText(mContent);
            mNotesEditText.setSelection(mContent.length());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                EditTextManger.getManager(mBackView).hideSoftInput();
                NotesActicity.this.finish();
                break;
            case R.id.tv_right:
                String notesContent = mNotesEditText.getText().toString();
                uploadPicture(mAdapter.getData(),notesContent);
                break;
        }
    }

    private void uploadPicture(final ArrayList<String> paths, final String content) {
        rootView.showLoadingView();
        final ArrayList<PictureMessage> data = new ArrayList<>();
        for (int i = 0; i < paths.size(); i++) {
            String path = paths.get(i);
            if (!path.startsWith("http") && !path.startsWith("https")) {
                PictureMessage message = new PictureMessage();
                message.index = i;
                message.path = path;
                data.add(message);
            }
        }
        if (data.size()==0){
            uploadData(paths, content);
            return;
        }
        UpDataRequest.getInstense().setConstantParams(new UpDataRequest.findConstantParams() {
            @NonNull
            @Override
            public String findUpdataUrl() {
                return UrlRepository.getInstance().getServer() + "/common/uploadImgs.do?"+"token="+LoginInfo.getToken();
            }

            @Override
            public int findFileNumber() {
                return data.size();
            }

            @Nullable
            @Override
            public Map<String, String> findParams() {
                Map<String,String> map=new HashMap<>();
                map.put("token", LoginInfo.getToken());
                return null;
            }
        }).setImgPath(new UpDataRequest.findImgPath() {
            @NonNull
            @Override
            public String getImgPath(int position) {
                return data.get(position).path;
            }
        }).setTag(new UpDataRequest.findImgTag() {
            @Nullable
            @Override
            public Object getImgTag(int position) {
                return data.get(position);
            }
        }).setProgressListener(new UpDataRequest.onProgressListener() {
            @Override
            public void onRequestStart() {

            }
            @Override
            public void onProgress(int index,int position) {}
            @Override
            public void onRequestEnd() {
                uploadData(paths, content);
            }
        }).setListener(new UpDataRequest.onUpDatalistener() {
            @Override
            public void onUpDataStart(int position, Object tag) {}
            @Override
            public void onUpDataSuccess(int position, Object tag, String jsonString) {
                if (TextUtils.isEmpty(jsonString)){
                    return;
                }
                UploadImgBean imgBean=null;
                try {
                    imgBean= RequestBase.gson.fromJson(jsonString,UploadImgBean.class);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (imgBean!=null&&imgBean.getData().size()>0) {
                    paths.set(position, imgBean.getData().get(0));
                }
            }
            @Override
            public void onUpDataFailed(int position, Object tag, String failMsg) {

            }
            @Override
            public void onError(String errorMsg) {}
        });
    }

    private void uploadData(final ArrayList<String> paths, final String content){
        NoteBean noteBean=new NoteBean();
        noteBean.setQid(mQid);
        noteBean.setText(content);
        noteBean.setImages(paths);

        NotesRequest request=new NotesRequest();
        request.note=noteBean;
        request.wqid=mWqid;
        request.startRequest(EXueELianBaseResponse.class, new EXueELianBaseCallback<EXueELianBaseResponse>() {
            @Override
            protected void onResponse(RequestBase request, EXueELianBaseResponse response) {
                rootView.hiddenLoadingView();
                if (response.getStatus().getCode()==0){
                    NotesMessage notesMessage = new NotesMessage(mViewHashCode, content, paths);
                    EventBus.getDefault().post(notesMessage);
                    finish();
                }   else {
                    ToastManager.showMsg(response.getStatus().getDesc());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                ToastManager.showMsg(error.getMessage());
            }
        });
    }

    public void onEventMainThread(final CropCallbackMessage message) {
        if (mContext.hashCode() == message.fromId) {
            mAdapter.addData(message.path);
        }
    }

    public void onEventMainThread(PhotoDeleteBean deleteBean) {
        if (deleteBean != null && deleteBean.formId == mContext.hashCode()) {
            mAdapter.remove(deleteBean.deleteId);
        }
    }

    @Override
    public void onItemClick(int viewType, int position) {
        if (viewType == NotesAdapter.TYPE_CAMERA) {
            YanxiuBaseActivity.requestCameraPermission(NotesActicity.this);
        } else if (viewType == NotesAdapter.TYPE_IMAGE) {
            PhotoActivity.LaunchActivity(mContext, mAdapter.getData(), position, mContext.hashCode(), PhotoActivity.DELETE_CAN);
        }
    }

    @Override
    public void onPermissionsGranted(@Nullable List<String> deniedPermissions) {
        if (deniedPermissions != null && deniedPermissions.size() > 0) {
            if (deniedPermissions.get(0).equals(Manifest.permission.CAMERA)) {
                requestWriteAndReadPermission(NotesActicity.this);
            } else if (deniedPermissions.get(0).equals(Manifest.permission.READ_EXTERNAL_STORAGE) || deniedPermissions.get(0).equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                CameraActivity.LaunchActivity(mContext, mContext.hashCode());
            }
        }
    }

    @Override
    public void onPermissionsDenied(@Nullable List<String> deniedPermissions) {
        if (deniedPermissions != null && deniedPermissions.size() > 0) {
            if (deniedPermissions.get(0).equals(Manifest.permission.CAMERA)) {
                ToastManager.showMsg(R.string.no_camera_permissions);
            } else if (deniedPermissions.get(0).equals(Manifest.permission.READ_EXTERNAL_STORAGE) || deniedPermissions.get(0).equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ToastManager.showMsg(R.string.no_storage_permissions);
            }
        }
    }

    /**
     * 笔记封装数据
     */
    public static class NotesMessage {
        public int mViewHashCode;//接收view的id
        public String mNotesContent;//笔记内容
        public ArrayList<String> mPaths;

        NotesMessage(int viewHashCode, String notesContent, ArrayList<String> paths) {
            mViewHashCode = viewHashCode;
            mNotesContent = notesContent;
            mPaths = paths;
        }
    }

    private class PictureMessage {
        int index;
        String path;
    }

    private class NotesAdapter extends BaseAdapter {

        private static final int MAXNUMBER = 4;

        private static final int VIEWTYPECOUNT = 2;
        private static final int TYPE_IMAGE = 0x000;
        private static final int TYPE_CAMERA = 0x001;

        private static final String MODEL_DEFAULT = "default";
        private static final String MODEL_LONGPRESS = "long";
        private static final int MODEL_POSITION_DEFAULT = -1;

        private static final String DEFAULT_PATH = "xx";

        private LayoutInflater mInflater;
        private ArrayList<String> mPaths = new ArrayList<>();
        private String model = MODEL_DEFAULT;
        private int model_position = MODEL_POSITION_DEFAULT;

        private onItemClickListener mItemClickListener;

        NotesAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        public void setData(ArrayList<String> data) {
            this.mPaths.clear();
            this.mPaths.addAll(data);
            this.mPaths.add(DEFAULT_PATH);
            this.notifyDataSetChanged();
        }

        public void addData(String path) {
            int position = this.mPaths.size();
            if (position > 0) {
                this.mPaths.add(position - 1, path);
            } else {
                this.mPaths.add(path);
                this.mPaths.add(DEFAULT_PATH);
            }
            this.notifyDataSetChanged();
        }

        public void setListener(onItemClickListener itemClickListener) {
            this.mItemClickListener = itemClickListener;
        }

        public ArrayList<String> getData() {
            ArrayList<String> list = new ArrayList<>();
            list.addAll(this.mPaths);
            if (list.size() > 0) {
                list.remove(list.size() - 1);
            }
            return list;
        }

        private void remove(int position) {
            setModelDefault();
            if (position > -1 && position < mPaths.size()) {
                this.mPaths.remove(position);
                this.notifyDataSetChanged();
            }
        }

        private void setModelDefault() {
            model_position = MODEL_POSITION_DEFAULT;
            model = MODEL_DEFAULT;
        }

        @Override
        public int getViewTypeCount() {
            return VIEWTYPECOUNT;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == mPaths.size() - 1) {
                return TYPE_CAMERA;
            } else {
                return TYPE_IMAGE;
            }
        }

        @Override
        public int getCount() {
            int count = mPaths != null ? mPaths.size() : 0;
            count = count > MAXNUMBER ? MAXNUMBER : count;
            return count;
        }

        @Override
        public Object getItem(int position) {
            return mPaths.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final String path = mPaths.get(position);
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                if (path.equals(DEFAULT_PATH)) {
                    convertView = mInflater.inflate(R.layout.adapter_gridview_default, parent, false);
                } else {
                    convertView = mInflater.inflate(R.layout.adapter_gridview, parent, false);
                    holder.mDeleteView = (ImageView) convertView.findViewById(R.id.iv_delete);
                    holder.mStrokeView = (ImageView) convertView.findViewById(R.id.iv_bg);
                }
                holder.mPictureView = (ImageView) convertView.findViewById(R.id.iv_picture);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (!path.equals(DEFAULT_PATH)) {
                Glide.with(mInflater.getContext()).load(path).into(holder.mPictureView);
                if (model.equals(MODEL_LONGPRESS) && model_position == position) {
                    holder.mDeleteView.setVisibility(View.VISIBLE);
                } else {
                    holder.mDeleteView.setVisibility(View.GONE);
                }
                holder.mDeleteView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        remove(position);
                    }
                });
                holder.mStrokeView.setBackground(ContextCompat.getDrawable(mInflater.getContext(), R.drawable.shape_rectangle_color_fafafa));
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (path.equals(DEFAULT_PATH)) {
                        if (mItemClickListener != null) {
                            mItemClickListener.onItemClick(TYPE_CAMERA, position);
                        }
                    } else {
                        if (model.equals(MODEL_LONGPRESS) && model_position == position) {
                            setModelDefault();
                            notifyDataSetChanged();
                        } else {
                            if (mItemClickListener != null) {
                                mItemClickListener.onItemClick(TYPE_IMAGE, position);
                            }
                        }
                    }
                }
            });

            convertView.setOnLongClickListener(new View.OnLongClickListener() {
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
        }
    }
}
