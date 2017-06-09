package com.yanxiu.gphone.student.http.request;

import android.os.Environment;
import android.support.annotation.NonNull;

import com.yanxiu.gphone.student.util.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/8 16:22.
 * Function :
 */
public class DownLoadRequest {

    private static DownLoadRequest downLoadRequest;
    private final OkHttpClient okHttpClient;

    public static DownLoadRequest getInstense() {
        if (downLoadRequest == null) {
            downLoadRequest = new DownLoadRequest();
        }
        return downLoadRequest;
    }

    private DownLoadRequest() {
        okHttpClient = new OkHttpClient();
    }

    public void download(final String url, final String saveDir, final OnDownloadListener listener) {
        Request request = new Request.Builder().url(url).build();
        if (listener!=null) {
            listener.onDownloadStart();
        }
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (listener!=null) {
                    listener.onDownloadFailed();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(saveDir);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        if (listener!=null) {
                            listener.onDownloading(progress);
                        }
                    }
                    fos.flush();
                    if (listener!=null) {
                        listener.onDownloadSuccess(saveDir);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener!=null) {
                        listener.onDownloadFailed();
                    }
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public interface OnDownloadListener {
        void onDownloadStart();
        void onDownloading(int progress);
        void onDownloadSuccess(String saveDir);
        void onDownloadFailed();
    }
}
