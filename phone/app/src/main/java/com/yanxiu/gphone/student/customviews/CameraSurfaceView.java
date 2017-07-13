package com.yanxiu.gphone.student.customviews;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.util.FileUtil;
import com.yanxiu.gphone.student.util.ScreenUtils;
import com.yanxiu.gphone.student.util.ToastManager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/21 12:18.
 * Function :
 */
public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Camera.AutoFocusCallback {
    private Context mContext;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private int mScreenWidth;
    private int mScreenHeight;
    private CameraView.onTakePictureFinishedListener mTakePictureFinishedListener;

    private interface onSaveFinishedListener {
        void onFinished(String path);
    }

    public CameraSurfaceView(Context context) {
        this(context, null);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initScreenMetrix();
        listener();
    }

    private void initView(Context context) {
        this.mContext = context;
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private void initScreenMetrix() {
        mScreenWidth = ScreenUtils.getScreenWidth(mContext);
        mScreenHeight = ScreenUtils.getScreenHeight(mContext);
    }

    private void listener() {
        mSurfaceHolder.addCallback(CameraSurfaceView.this);
    }

    public void takePicture(CameraView.onTakePictureFinishedListener mTakePictureFinishedListener) {
        if (mCamera != null) {
            this.mTakePictureFinishedListener = mTakePictureFinishedListener;
            setCameraParams();
            mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
        }
    }

    public void onResume() {
        if (mCamera == null) {
            mCamera = Camera.open();
            if (mSurfaceHolder != null) {
                startPreview();
            }
        }
    }

    public void onPause() {
        if (mCamera != null) {
            release();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mCamera != null) {
            mCamera.stopPreview();
            setCameraParams();
            startPreview();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        release();
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
    }

    private void setCameraParams() {
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPictureFormat(PixelFormat.JPEG);
        List<Camera.Size> pictureSizeList = parameters.getSupportedPictureSizes();
        Camera.Size picSize = getProperSize(pictureSizeList, ((float) mScreenHeight / mScreenWidth));
        if (null == picSize) {
            picSize = parameters.getPictureSize();
        }
        float w = picSize.width;
        float h = picSize.height;
        parameters.setPictureSize(picSize.width, picSize.height);
        this.setLayoutParams(new FrameLayout.LayoutParams((int) (mScreenHeight * (h / w)), mScreenHeight));

        List<Camera.Size> previewSizeList = parameters.getSupportedPreviewSizes();
        Camera.Size preSize = getProperSize(previewSizeList, ((float) mScreenHeight) / mScreenWidth);
        if (null != preSize) {
            parameters.setPreviewSize(preSize.width, preSize.height);
        }

        parameters.setJpegQuality(100);
        if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        mCamera.cancelAutoFocus();
        mCamera.setDisplayOrientation(90);
        mCamera.setParameters(parameters);
        try {
            mCamera.autoFocus(CameraSurfaceView.this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Camera.Size getProperSize(List<Camera.Size> pictureSizeList, float screenRatio) {
        Camera.Size result = null;
        for (Camera.Size size : pictureSizeList) {
            float currentRatio = ((float) size.width) / size.height;
            if (currentRatio - screenRatio == 0) {
                result = size;
                break;
            }
        }

        if (null == result) {
            for (Camera.Size size : pictureSizeList) {
                float curRatio = ((float) size.width) / size.height;
                if (curRatio == 4f / 3) {
                    result = size;
                    break;
                }
            }
        }

        return result;
    }

    private void startPreview() {
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void release() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    private Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {

        }
    };

    private Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

        }
    };

    private Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                SavePictureTask savePictureTask = new SavePictureTask(mContext, saveFinishedListener);
                savePictureTask.execute(data);
            } else {
                ToastManager.showMsg(R.string.SD_cannot_use);
            }
        }
    };

    private onSaveFinishedListener saveFinishedListener = new onSaveFinishedListener() {
        @Override
        public void onFinished(String path) {
            if (mCamera!=null) {
                mCamera.stopPreview();
                mCamera.startPreview();
            }
            if (mTakePictureFinishedListener!=null) {
                mTakePictureFinishedListener.onFinish(path);
            }
        }
    };

    private class SavePictureTask extends AsyncTask<byte[], Integer, String> {

        private Context mContext;
        private onSaveFinishedListener mSaveFinishedListener;

        SavePictureTask(Context context, onSaveFinishedListener mSaveFinishedListener) {
            this.mContext = context;
            this.mSaveFinishedListener = mSaveFinishedListener;
        }

        @Override
        protected String doInBackground(byte[]... params) {
            byte[] data = params[0];
            BufferedOutputStream bos = null;
            Bitmap bitmap = null;
            String filePath = null;
            try {
                bitmap = ScreenUtils.decodeBitmap(data, ScreenUtils.getScreenWidth(mContext), ScreenUtils.getScreenHeight(mContext));
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();

                if (width > height) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                }

                filePath = FileUtil.getSavePicturePath(System.currentTimeMillis() + ".jpg");
                File file = new File(filePath);
                bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            } catch (Exception e) {
                e.printStackTrace();
                filePath = null;
            } finally {
                try {
                    if (bos != null) {
                        bos.flush();
                        bos.close();
                    }
                    if (bitmap != null) {
                        bitmap.recycle();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return filePath;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s!=null) {
                setPictureDegreeZero(s);
                Uri localUri = Uri.fromFile(new File(s));
                Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
                mContext.sendBroadcast(localIntent);
            }
            if (mSaveFinishedListener != null) {
                mSaveFinishedListener.onFinished(s);
            }
        }
    }

    private void setPictureDegreeZero(String path) {
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "no");
            exifInterface.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
