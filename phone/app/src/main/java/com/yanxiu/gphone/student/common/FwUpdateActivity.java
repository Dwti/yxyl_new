package com.yanxiu.gphone.student.common;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bde.nota.common.OADManager;
import com.bde.nota.common.OADManagerCallback;
import com.ydiot.ssp.R;
import com.ydiot.ssp.common.Utils.log.YD_Log;

import java.util.Timer;
import java.util.TimerTask;

public class FwUpdateActivity extends Activity {
	public final static String EXTRA_MESSAGE = "ti.android.ble.sensortag.MESSAGE";
	// Log
	private static String TAG = "FwUpdateActivity";

	// Activity
	private static final int FILE_ACTIVITY_REQ = 0;
	private static MediaPlayer mp = new MediaPlayer();

	// GUI
	private TextView mTargImage;
	private TextView mFileImage;
	private TextView mProgressInfo;
	private TextView mLog;

	private ProgressBar mProgressBar;

	private Button mBtnLoadA;
	private Button mBtnLoadB;
	private Button mBtnLoadC;
	private Button mBtnStart;
	private Button mBtnDown;

	private LinearLayout l_Down;

	private EditText mTvDown;

	private OADManager mOadManager;
	private OADManagerCallback mOadManagerCallback;

	private BluetoothDevice mTargetDevice;
	private BluetoothAdapter mBluetoothAdapter;

	public FwUpdateActivity() {
		Log.d(TAG, "construct");
	}
	BluetoothManager mBluetoothManager;
	String address;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		initial();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fwupdate);
		 mBluetoothManager= (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
		Intent intent = getIntent();
		address= intent.getStringExtra("Device_Address");
		mBluetoothAdapter = mBluetoothManager.getAdapter();
		// Icon padding
		ImageView view = (ImageView) findViewById(android.R.id.home);
		if(view != null)
			view.setPadding(10, 0, 20, 10);

		// Context title
		setTitle(R.string.title_oad);

		// Initialize widgets
		mProgressInfo = (TextView) findViewById(R.id.tw_info);
		mTargImage = (TextView) findViewById(R.id.tw_target);
		mFileImage = (TextView) findViewById(R.id.tw_file);
		mLog = (TextView) findViewById(R.id.tw_log);

		mLog.setMaxLines(30);
		l_Down = (LinearLayout) findViewById(R.id.l_Down);
		mTvDown = (EditText) findViewById(R.id.et_Down);
		mBtnDown = (Button) findViewById(R.id.btn_Down);
		mProgressBar = (ProgressBar) findViewById(R.id.pb_progress);
		mBtnStart = (Button) findViewById(R.id.btn_start);
		mBtnStart.setEnabled(true);
		mBtnLoadA = (Button) findViewById(R.id.btn_load_a);
		mBtnLoadB = (Button) findViewById(R.id.btn_load_b);
		mBtnLoadC = (Button) findViewById(R.id.btn_load_c);

		mTvDown.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().equals(""))
					mBtnDown.setEnabled(false);
				else
					mBtnDown.setEnabled(true);
			}
		});
		// Sanity check
		mBtnLoadA.setEnabled(true);
		mBtnLoadB.setEnabled(true);
		mBtnLoadC.setEnabled(true);
//		if(mOadManager != null)
//			mBtnLoadC.performClick();
	}

	/**
	 * OAD升级库初始化
	 */
	private void initial(){
		try {
			mOadManager = new OADManager(this, null);
			mOadManager.prepareOADManager();
			//Todo 加载固件升级文件，此处需要注意，若是将升级文件放在raw目录中，命名只可使用a-z 0-9 _ 后缀名的.似乎不计算在内
			mOadManager.loadFile(getResources().openRawResource(R.raw.update_spp_v1_2_6_cc2541_9600));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(mOadManager != null)
			//TODO 该回调接口仅仅是用来对外通知升级过程消息，不使用也不影响升级过程
			mOadManagerCallback = new OADManagerCallback() {
				@Override
				public void didConnectedDevice() {
					Log.w(TAG, "Connected");
					Message ms = new Message();
					Bundle bundle = new Bundle();
					bundle.putString("LOG", "Connected");
					bundle.putBoolean("ISCONNECT",true);
					ms.setData(bundle);
					handler.sendMessage(ms);
				}

				@Override
				public void didDisconnectedDevice() {
					Log.w(TAG, "DisConnected");

					Message ms = new Message();
					Bundle bundle = new Bundle();
					bundle.putString("LOG", "DisConnected");
					bundle.putBoolean("ISCONNECT",false);
					ms.setData(bundle);
					handler.sendMessage(ms);

					if(Mode != OADManagerCallback.Mode_ImgA) {
						new Timer().schedule(new TimerTask() {
							@Override
							public void run() {
								mOadManager.Connect(mTargetDevice);
							}
						}, 500);
					}
				}

				@Override
				public void didEnterOADMode(boolean success) {
					Log.w(TAG, "EnterOADMode");

					Message ms = new Message();
					Bundle bundle = new Bundle();
					bundle.putString("LOG", "EnterOADMode");
					ms.setData(bundle);
					handler.sendMessage(ms);
					mLog.append(getString(R.string.wait_10_seconds) + "\r\n");
				}

				@Override
				public void didFinishOAD(boolean success) {
//					mOadManager.Connect(mTargetDevice);
					Log.w(TAG, "FinishOAD");
					Message ms = new Message();
					Bundle bundle = new Bundle();
					bundle.putString("LOG", "FinishOAD");
					ms.setData(bundle);
					handler.sendMessage(ms);
				}

				@Override
				public void onProgress(int progress) {
					Log.w(TAG, "Progress:"+progress);
					mProgressBar.setProgress(progress);
				}

				@Override
				public void onLog(String msg) {
					Log.e(TAG, "LOG:" + msg);
					Message ms = new Message();
					Bundle bundle = new Bundle();
					bundle.putString("LOG", msg);
					ms.setData(bundle);
					handler.sendMessage(ms);

				}

				@Override
				public void onStateInfo(String msg) {
					Log.e(TAG, "STATEINFO:" + msg);
					Message ms = new Message();
					Bundle bundle = new Bundle();
					bundle.putString("STATEINFO", msg);
					ms.setData(bundle);
					handler.sendMessage(ms);

				}

				@Override
				public void onProgramingChanged(Boolean isPrograming) {
					Log.e(TAG, "Programing:" + isPrograming);
					Message ms = new Message();
					Bundle bundle = new Bundle();
					bundle.putString("LOG", isPrograming?"正在升级":"升级停止");
					bundle.putBoolean("PROGRAM", isPrograming);
					ms.setData(bundle);
					handler.sendMessage(ms);
				}

				@Override
				public void onModeChecked(byte Mode) {
					Log.e(TAG, "Device_Mode:" + Mode);
					Message ms = new Message();
					Bundle bundle = new Bundle();
					bundle.putString("MODE", Mode == OADManagerCallback.Mode_ImgA ? "ImgA" : "ImgB");
					ms.setData(bundle);
					handler.sendMessage(ms);
					FwUpdateActivity.this.Mode = Mode;
					if(Mode == Mode_ImgA){
						new Timer().schedule(new TimerTask() {
							@Override
							public void run() {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										mBtnStart.performClick();//模拟启动按钮
									}
								});
							}
						}, 1000);
					}
				}

				@Override
				public void onFileNeed() {
					Log.e(TAG, "Upgrade file need to be loaded");
					Message ms = new Message();
					Bundle bundle = new Bundle();
					bundle.putString("LOG", "Upgrade file need to be loaded");
					ms.setData(bundle);
					handler.sendMessage(ms);
					mOadManager.loadFile(getResources().openRawResource(R.raw.update_spp_v1_2_6_cc2541_9600));
					new Timer().schedule(new TimerTask() {
						@Override
						public void run() {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									mBtnStart.performClick();//模拟启动按钮
								}
							});
						}
					}, 1000);
				}
			};
			mOadManager.addCallback(mOadManagerCallback);
	}
	private byte Mode = 0x00;
	/**
	 * 显示oad升级库对外提供的消息
	 */
	public android.os.Handler handler = new android.os.Handler(){
		public void handleMessage(Message msg){
			if(mLog.getText().toString().split("\r\n").length>19)
				mLog.setText("");
			String m = msg.getData().getString("LOG", null);
			if(m != null) {
				mLog.append(m);
				mLog.append("\r\n");
			}
			m = msg.getData().getString("MODE", null);
			if(m != null){
				mTargImage.setText(m);
			}
			m = msg.getData().getString("STATEINFO", null);
			if(m != null){
				mProgressInfo.setText(m);
			}
			if(msg.getData().containsKey("PROGRAM")){
				Boolean p = msg.getData().getBoolean("PROGRAM");
				mBtnStart.setText(p ? getString(R.string.stop_prog):getString(R.string.start_prog));
				mBtnStart.setEnabled(!p);
				if(!p){
					//TODO 播放升级结束提示音
					if(mProgressBar.getProgress() >= mProgressBar.getMax())
						playBell();
					else
						playFail();
				}
			}
			if(msg.getData().containsKey("ISCONNECT")){
				Boolean p = msg.getData().getBoolean("ISCONNECT");
				mBtnLoadA.setText(p ?getString(R.string.disconnected):getString(R.string.connected));
			}
		}
	};
	protected void playBell(){
		mp = MediaPlayer.create(this, R.raw.bell);
		mp.start();
	}
	protected void playFail(){
		mp = MediaPlayer.create(this, R.raw.fail);
		mp.start();
	}
	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		super.onDestroy();
		mOadManager.close();

	}

	public boolean dispatchTouchEvent(MotionEvent event){
		if(mp != null)
			mp.stop();
		return super.dispatchTouchEvent(event);
	}

	@Override
	public void onBackPressed() {
		Log.d(TAG, "onBackPressed");
		if (mOadManager.getIsPrograming()) {
			//升级时，禁止返回键
			Toast.makeText(this, R.string.prog_ogoing, Toast.LENGTH_LONG).show();
		} else
			super.onBackPressed();
	}

	@Override 
	protected void onResume()
	{
		Log.d(TAG, "onResume");
		super.onResume();
		if (mBluetoothAdapter.isEnabled()) {
			BluetoothDevice mLeDevice = mBluetoothManager.getAdapter().getRemoteDevice(address);
			mTargetDevice = mLeDevice;
		}
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "onPause");
		super.onPause();
	}



	public void onStart(View v) {
		mOadManager.startProgramming();
		((Button)v).setText(getString(R.string.stop_prog));
		v.setEnabled(false);
	}

//	public void onNewFile(View v){
//		boolean isView = l_Down.getVisibility() == View.VISIBLE;
//		if(isView)
//			l_Down.setVisibility(View.GONE);
//		else
//			l_Down.setVisibility(View.VISIBLE);
//	}
//	public void onDown(View v){
//		final String fileCode = mTvDown.getText().toString();
//		final HttpUtil httpUtil = HttpUtil.getHttpUtil(mDirPath);
//		new Thread(){
//			public void run() {
//				httpUtil.downLoad(fileCode);
//			}
//		}.start();
//	}
//	public void onEnterOAD(View v){
//		mOadManager.onEnterOAD();
//	}


//	private static final String FW_CUSTOM_DIRECTORY = Environment.DIRECTORY_DOWNLOADS;


	public void onConnect(View v){
		mOadManager.Connect(mTargetDevice);
	}


//	private void updateGui() {
//		if (mOadManager.getIsPrograming()) {
//			mBtnStart.setText(R.string.cancel);
//			mBtnLoadA.setEnabled(false);
//			mBtnLoadB.setEnabled(false);
//			mBtnLoadC.setEnabled(false);
//		} else {
//			mProgressBar.setProgress(0);
//			mBtnStart.setText(R.string.start_prog);
//			mBtnLoadB.setEnabled(true);
//			mBtnLoadA.setEnabled(true);
//			mBtnLoadC.setEnabled(true);
//
//		}
//	}
//	public static String mDirPath = "";
//	private boolean loadFile(String filepath) {
//		return mOadManager.loadFile(filepath);
//	}
}
