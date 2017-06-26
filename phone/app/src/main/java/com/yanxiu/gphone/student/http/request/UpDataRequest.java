package com.yanxiu.gphone.student.http.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.yanxiu.gphone.student.util.LoginInfo;

import java.io.File;
import java.io.IOException;
import java.util.List;
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
    private static final String TYPE_IMG="img";
    private static final String TYPE_IMGS="imgs";
    private static final String IMGKEY="img";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    private static UpDataRequest mUpDataRequest;
    private final OkHttpClient mOkHttpClient;

    private String mUrl;
    private String mImg_Path;
    private List<String> mImg_Paths;
    private String mType=TYPE_IMG;
    private Map<String, String> mParams;

    public static UpDataRequest getInstense() {
        if (mUpDataRequest==null){
            mUpDataRequest=new UpDataRequest();
        }
        return mUpDataRequest;
    }

    private UpDataRequest() {
        mOkHttpClient = new OkHttpClient();
    }

    public UpDataRequest setUpDataUrl(@NonNull String url){
        this.mUrl=url;
        return mUpDataRequest;
    }

    public UpDataRequest setImg(@NonNull String img_path){
        this.mType=TYPE_IMG;
        this.mImg_Path=img_path;
        return mUpDataRequest;
    }

    public UpDataRequest setImgs(@NonNull List<String> img_paths){
        this.mType=TYPE_IMGS;
        this.mImg_Paths=img_paths;
        return mUpDataRequest;
    }

    public UpDataRequest setParams(@Nullable Map<String, String> params){
        this.mParams=params;
        return mUpDataRequest;
    }

    public void setListener(@Nullable onUpDatalistener upDatalistener){
        if (mType.equals(TYPE_IMG)) {
            upData(mUrl, 0, IMGKEY, mImg_Path, mParams, upDatalistener);
        }else if (mType.equals(TYPE_IMGS)){
            for (int i=0;i<mImg_Paths.size();i++){
                upData(mUrl, i, IMGKEY, mImg_Paths.get(i), mParams, upDatalistener);
            }
        }
    }

    private void upData(final String url,final int position, final String img_key, final String img_path, final Map<String, String> params, final onUpDatalistener upDatalistener) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        if (TextUtils.isEmpty(img_path)){
            if (upDatalistener!=null){
                upDatalistener.onError("The ImgPath can not be NULL");
            }
        }
        File f = new File(img_path);
        builder.addFormDataPart(img_key, f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));

        builder.addFormDataPart("token", LoginInfo.getToken());

        if (params!=null) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key));
            }
        }

        if (TextUtils.isEmpty(url)){
            if (upDatalistener!=null){
                upDatalistener.onError("The url can not be NULL");
            }
        }
        MultipartBody requestBody = builder.build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        if (upDatalistener!=null) {
            upDatalistener.onUpDataStart(position, img_path);
        }
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (upDatalistener!=null) {
                    String message=e.getMessage();
                    upDatalistener.onUpDataFailed(position, img_path,message);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (upDatalistener!=null) {
                    String message=response.body().string();
                    upDatalistener.onUpDataSuccess(position, img_path,message);
                }
            }
        });
    }

    public interface onUpDatalistener {
        void onUpDataStart(int position, String localPath);

        void onUpDataSuccess(int position, String localPath,String successMsg);

        void onUpDataFailed(int position, String localPath,String failMsg);

        void onError(String errorMsg);
    }

}
