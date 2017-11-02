package com.yanxiu.gphone.student.questions.spoken;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.unisound.edu.oraleval.sdk.sep15.IOralEvalSDK;
import com.unisound.edu.oraleval.sdk.sep15.OralEvalSDKFactory;
import com.unisound.edu.oraleval.sdk.sep15.SDKError;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.util.Logger;
import com.yanxiu.gphone.student.util.time.CountDownManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Canghaixiao.
 * Time : 2017/10/18 11:30.
 * Function :
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class SpokenUtils {

    private static final String TAG = "SpokenUtils";

    private static final String FILEPATH = Constants.VOICEDIR;

    private IOralEvalSDK mIOralEvalSDK;
    private MediaPlayer mPlay;
    private FileOutputStream mAudioFileOut;
    private CountDownManager mDownManager;

    public interface onPlayCallback {
        void onStart();

        void onEnd();

        void onError();
    }

    public interface onOralEvaProgressCallback {
        void onProgress(int progress);

        void onFinished();
    }

    public interface onBaseOralEvalCallback {
        void onStartRecord(String filePath);

        void onStopOralEval(boolean isError);

        void onVolume(int volume);

        void onStartOralEval();

        void onResult(String result);

        void onResultUrl(String filePath, String url);

        void onNoPermission();

        void onError(String result);

        void onFailed(String text);
    }

    public abstract class onOralEvalCallback implements onBaseOralEvalCallback {
        public void onAudioData(IOralEvalSDK iOralEvalSDK, byte[] bytes, int i, int i1) {
        }

        public void onOpusData(IOralEvalSDK iOralEvalSDK, byte[] bytes, int i, int i1) {
        }

        public void onAsyncResult(IOralEvalSDK iOralEvalSDK, String s) {
        }
    }

    private SpokenUtils() {
    }

    public static SpokenUtils create() {
        return new SpokenUtils();
    }

    public void start(Context context, String text, onBaseOralEvalCallback oralEvalCallback, onOralEvaProgressCallback oralEvaProgressCallback) {
        String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + FILEPATH;
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        String path = dir + String.valueOf(System.currentTimeMillis()) + ".mp3";
        start(context, text, path, oralEvalCallback, oralEvaProgressCallback);
    }

    public void start(final Context context, String text, final String path, final onBaseOralEvalCallback oralEvalCallback, final onOralEvaProgressCallback oralEvaProgressCallback) {
        if (mIOralEvalSDK != null) {
            oralEvalCallback.onFailed("您点击太频繁了");
            try {
                mIOralEvalSDK.stop();
            }catch (Exception e){
                e.printStackTrace();
            }
            mIOralEvalSDK.stop();
            return;
        }
        if (TextUtils.isEmpty(text)){
            oralEvalCallback.onFailed("服务器异常");
            return;
        }
        OralEvalSDKFactory.StartConfig cfg = getCfg(text, path);
        if (cfg == null) {
            oralEvalCallback.onFailed("文件地址错误");
            return;
        }
        mIOralEvalSDK = OralEvalSDKFactory.start(context, cfg, new IOralEvalSDK.ICallback() {
            @Override
            public void onStart(IOralEvalSDK iOralEvalSDK, final int i) {
                runOnUiThread(context, new Runnable() {
                    @Override
                    public void run() {
                        oralEvalCallback.onStartRecord(path);
                        if (oralEvaProgressCallback != null) {
                            final int totalTime = 180000;
                            if (mDownManager != null) {
                                mDownManager.cancel();
                                mDownManager = null;
                            }
                            mDownManager = CountDownManager.getManager().setTotalTime(totalTime).setScheduleListener(new CountDownManager.ScheduleListener() {
                                @Override
                                public void onProgress(long progress) {
                                    Logger.d(TAG, progress + "");
                                    int time = (totalTime - ((int) progress)) / 1000;
                                    oralEvaProgressCallback.onProgress(time);
                                }

                                @Override
                                public void onFinish() {
                                    oralEvaProgressCallback.onFinished();
                                }
                            });
                            mDownManager.start();
                        }
                    }
                });
            }

            @Override
            public void onError(final IOralEvalSDK iOralEvalSDK, final SDKError sdkError, final IOralEvalSDK.OfflineSDKError offlineSDKError) {
                runOnUiThread(context, new Runnable() {
                    @Override
                    public void run() {
                        if (mDownManager != null) {
                            mDownManager.cancel();
                            mDownManager = null;
                        }
                        int errorCode = sdkError.errno;
                        String errorText = sdkError.exp.getMessage();
                        if (errorCode == -1001) {
                            //SB云知声,返回码为啥不分开，浪费时间，浪费生命
                            if ("startRecording() called on an uninitialized AudioRecord.".equals(errorText)) {
                                //没有录音权限
                                oralEvalCallback.onNoPermission();
                            } else {
                                oralEvalCallback.onFailed("打开或读取录音设备失败");
                            }
                        } else {
                            oralEvalCallback.onError(sdkError.toString());
                        }
                        oralEvalCallback.onStopOralEval(true);
                        mIOralEvalSDK = null;
                        mAudioFileOut = null;
                    }
                });
            }

            @Override
            public void onStop(IOralEvalSDK iOralEvalSDK, final String s, boolean b, final String s1, IOralEvalSDK.EndReason endReason) {
                runOnUiThread(context, new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JsonParser PARSER = new JsonParser();
                            PARSER.parse(s);
                        } catch (JsonParseException jpe) {
                            oralEvalCallback.onError(s);
                            oralEvalCallback.onStopOralEval(true);
                            mIOralEvalSDK = null;
                            mAudioFileOut = null;
                            return;
                        }
                        oralEvalCallback.onResult(s);
                        oralEvalCallback.onResultUrl(path, s1);
                        oralEvalCallback.onStopOralEval(false);
                        mIOralEvalSDK = null;
                        mAudioFileOut = null;
                    }
                });
            }

            @Override
            public void onVolume(IOralEvalSDK iOralEvalSDK, final int i) {
                runOnUiThread(context, new Runnable() {
                    @Override
                    public void run() {
                        oralEvalCallback.onVolume(i);
                    }
                });
            }

            @Override
            public void onAudioData(IOralEvalSDK iOralEvalSDK, byte[] bytes, int i, int i1) {
                try {
                    if (mAudioFileOut == null) {
                        File files = new File(path);
                        mAudioFileOut = new FileOutputStream(files);
                    }
                    mAudioFileOut.write(bytes, i, i1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (oralEvalCallback instanceof onOralEvalCallback) {
                    ((onOralEvalCallback) oralEvalCallback).onAudioData(iOralEvalSDK, bytes, i, i1);
                }
            }

            @Override
            public void onOpusData(IOralEvalSDK iOralEvalSDK, byte[] bytes, int i, int i1) {
                if (oralEvalCallback instanceof onOralEvalCallback) {
                    ((onOralEvalCallback) oralEvalCallback).onOpusData(iOralEvalSDK, bytes, i, i1);
                }
            }

            @Override
            public void onAsyncResult(final IOralEvalSDK iOralEvalSDK, final String s) {
                if (oralEvalCallback instanceof onOralEvalCallback) {
                    runOnUiThread(context, new Runnable() {
                        @Override
                        public void run() {
                            ((onOralEvalCallback) oralEvalCallback).onAsyncResult(iOralEvalSDK, s);
                        }
                    });
                }
            }

            @Override
            public void onCancel() {
                runOnUiThread(context, new Runnable() {
                    @Override
                    public void run() {
                        if (mDownManager != null) {
                            mDownManager.cancel();
                            mDownManager = null;
                        }
                        oralEvalCallback.onStopOralEval(true);
                        mIOralEvalSDK = null;
                        mAudioFileOut = null;
                    }
                });
            }

            @Override
            public void onStartOralEval() {
                runOnUiThread(context, new Runnable() {
                    @Override
                    public void run() {
                        if (mDownManager != null) {
                            mDownManager.cancel();
                            mDownManager = null;
                        }
                        oralEvalCallback.onStartOralEval();
                    }
                });
            }
        });
    }

    public void stop() {
        try {
            if (mIOralEvalSDK != null)
                mIOralEvalSDK.stop();
//            mIOralEvalSDK = null;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void cancel() {
        try {
            if (mIOralEvalSDK != null)
                mIOralEvalSDK.cancel();
            mIOralEvalSDK = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playClear() {
        try {
            if (mPlay != null) {
                mPlay.stop();
                mPlay.release();
                mPlay = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play(Context context, String path, final onPlayCallback playCallback) {
        try {
            if (mPlay != null) {
                playCallback.onEnd();
                mPlay.stop();
                mPlay.release();
                mPlay = null;
            } else {
                mPlay = MediaPlayer.create(context, Uri.fromFile(new File(path)));
                mPlay.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        playCallback.onStart();
                    }
                });
                mPlay.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        playCallback.onEnd();
                        mPlay.release();
                        mPlay = null;
                    }
                });
                mPlay.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        playCallback.onError();
                        playCallback.onEnd();
                        mp.release();
                        mPlay = null;
                        return false;
                    }
                });
                mPlay.start();
            }
        } catch (Exception ee) {
            ee.printStackTrace();
            mPlay = null;
            playCallback.onEnd();
        }
    }

    private void runOnUiThread(Context context, Runnable runnable) {
        ((Activity) context).runOnUiThread(runnable);
    }

    private OralEvalSDKFactory.StartConfig getCfg(String text, String path) {
        File files = new File(path);
        OralEvalSDKFactory.StartConfig cfg;
        if (files.exists()) {
            try {
                cfg = new OralEvalSDKFactory.StartConfig(text, files.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
//            showToast("正在使用音频文件测评");
        } else {
            cfg = new OralEvalSDKFactory.StartConfig(text);
//            showToast("正在使用mic正常录音评测");
            cfg.setVadEnable(false);
        }
        cfg.setBufferLog(false);
        cfg.setScoreAdjuest(1.6f);
        cfg.setServiceType("A");
        cfg.setSocket_timeout(0);
        cfg.setMp3Audio(true);
        cfg.setAsyncRecognize(false);
        return cfg;
    }
}
