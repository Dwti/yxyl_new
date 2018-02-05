package com.yanxiu.gphone.student.http.request;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.yanxiu.gphone.student.util.LoginInfo;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/26 9:23.
 * Function :
 */
public class UpDataRequest {

    public interface findConstantParams {
        @NonNull
        String findUpdataUrl();

        int findFileNumber();

        @Nullable
        Map<String, String> findParams();
    }

    public interface findImgPath {
        @NonNull
        String getImgPath(int position);
    }

    public interface findImgTag {
        @Nullable
        Object getImgTag(int position);
    }

    public interface onUpDataProgresslistener {
        void onUpDataStart(int position, Object tag);

        void onUpDataSuccess(int position, Object tag, String jsonString);

        void onUpDataFailed(int position, Object tag, String failMsg);

        void onError(String errorMsg);
    }

    public interface onRequestListener {
        void onRequestStart();

        void onProgress(int index,int position);

        void onRequestEnd();
    }

    private static final String IMGKEY = "img";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private int totalCount = 0;
    private int index = 0;

    private static UpDataRequest mUpDataRequest;
    private final OkHttpClient mOkHttpClient;

    private findConstantParams mFindConstantParams;
    private findImgPath mFindImgPath;
    private findImgTag mFindImgTag;
    private onRequestListener mRequestListener;

    public static UpDataRequest getInstense() {
        if (mUpDataRequest == null) {
            mUpDataRequest = new UpDataRequest();
        }
        return mUpDataRequest;
    }

    private UpDataRequest() {
        mOkHttpClient = new OkHttpClient();
    }

    public UpDataRequest setConstantParams(findConstantParams findConstantParams) {
        this.mFindConstantParams = findConstantParams;
        return mUpDataRequest;
    }

    public UpDataRequest setImgPath(@NonNull findImgPath findImgPath) {
        this.mFindImgPath = findImgPath;
        return mUpDataRequest;
    }

    public UpDataRequest setTag(@Nullable findImgTag findImgTag) {
        this.mFindImgTag = findImgTag;
        return mUpDataRequest;
    }

    public UpDataRequest setRequestListener(onRequestListener progressListener) {
        this.mRequestListener = progressListener;
        return mUpDataRequest;
    }

    public void setProgressListener(@Nullable onUpDataProgresslistener upDataProgresslistener) {
        int fileNumber;
        String url;
        Map<String, String> params;
        if (mFindConstantParams != null) {
            fileNumber = mFindConstantParams.findFileNumber();
            url = mFindConstantParams.findUpdataUrl();
            params = mFindConstantParams.findParams();
        } else {
            if (upDataProgresslistener != null) {
                upDataProgresslistener.onError("can not find constant params");
            }
            return;
        }
        index = 0;
        totalCount = fileNumber;
        if (mRequestListener != null) {
            mRequestListener.onRequestStart();
        }
        if (totalCount==0){
            postMainThread(new Runnable() {
                @Override
                public void run() {
                    mRequestListener.onRequestEnd();
                }
            });
        }else {
            for (int i = 0; i < fileNumber; i++) {
                upData(url, i, params, upDataProgresslistener);
            }
        }
    }

    private void postMainThread(Runnable runnable) {
        mHandler.post(runnable);
    }

    private void upData(final String url, final int position, final Map<String, String> params, final onUpDataProgresslistener upDataProgresslistener) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        final String imgPath;
        if (mFindImgPath != null) {
            imgPath = mFindImgPath.getImgPath(position);
        } else {
            if (upDataProgresslistener != null) {
                postMainThread(new Runnable() {
                    @Override
                    public void run() {
                        upDataProgresslistener.onError("Can not find img path ");
                    }
                });
            }
            return;
        }

        if (TextUtils.isEmpty(imgPath)) {
            if (upDataProgresslistener != null) {
                postMainThread(new Runnable() {
                    @Override
                    public void run() {
                        upDataProgresslistener.onError("The imgPath can not be NULL");
                    }
                });
            }
            return;
        }
        File f = new File(imgPath);
        builder.addFormDataPart(IMGKEY, f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));

        builder.addFormDataPart("token", LoginInfo.getToken());

        if (params != null) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key));
            }
        }

        if (TextUtils.isEmpty(url)) {
            if (upDataProgresslistener != null) {
                postMainThread(new Runnable() {
                    @Override
                    public void run() {
                        upDataProgresslistener.onError("The url can not be NULL");
                    }
                });
            }
            return;
        }
        MultipartBody requestBody = builder.build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        if (upDataProgresslistener != null) {
            postMainThread(new Runnable() {
                @Override
                public void run() {
                    upDataProgresslistener.onUpDataStart(position, imgPath);
                }
            });
        }
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (mRequestListener != null) {
                    index++;
                    postMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mRequestListener.onProgress(index,position);
                        }
                    });
                }
                if (upDataProgresslistener != null) {
                    final String message = e.getMessage();
                    Object tag = null;
                    if (mFindImgTag != null) {
                        tag = mFindImgTag.getImgTag(position);
                    }
                    final Object finalTag = tag;
                    postMainThread(new Runnable() {
                        @Override
                        public void run() {
                            upDataProgresslistener.onUpDataFailed(position, finalTag, message);
                        }
                    });
                }
                if (index == totalCount) {
                    if (mRequestListener != null) {
                        postMainThread(new Runnable() {
                            @Override
                            public void run() {
                                mRequestListener.onRequestEnd();
                            }
                        });
                    }
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (mRequestListener != null) {
                    index++;
                    postMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mRequestListener.onProgress(index,position);
                        }
                    });
                }
                if (upDataProgresslistener != null) {
                    final String jsonString = response.body().string();
                    Object tag = null;
                    if (mFindImgTag != null) {
                        tag = mFindImgTag.getImgTag(position);
                    }
                    final Object finalTag = tag;
                    postMainThread(new Runnable() {
                        @Override
                        public void run() {
                            upDataProgresslistener.onUpDataSuccess(position, finalTag, jsonString);
                        }
                    });
                }
                if (index == totalCount) {
                    if (mRequestListener != null) {
                        postMainThread(new Runnable() {
                            @Override
                            public void run() {
                                mRequestListener.onRequestEnd();
                            }
                        });
                    }
                }
            }
        });
    }

}
