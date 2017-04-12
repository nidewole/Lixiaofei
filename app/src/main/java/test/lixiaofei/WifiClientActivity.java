package test.lixiaofei;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.ctvit.dev.RootActivity;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WifiClientActivity extends RootActivity implements OnClickListener  , SensorEventListener {
	private static final String TAG = WifiClientActivity.class.getSimpleName();
	private EditText editText_ip,editText_data;
	private EditText et_Send_Rate;
    private OutputStream mOutputStream = null;
    private Socket mSocket = null;
    private String mIp;
    private String mSendData;
    private boolean mSocketStatus = false;

	// 将纳秒转化为秒
	private static final float NS2S = 1.0f / 1000000000.0f;

	private float timestamp = 1.0f;

	private float angle[] = new float[3];
	private BufferedWriter bufferedWriter;
	private ArrayList<SensorEvent> list;
	private Sensor sensor1;
	private Sensor gyroscopeSensor;
	private SensorManager sensorManager;
	private ExecutorService executorThreadPool = Executors.newSingleThreadExecutor();
//	private ExecutorService executorThreadPoolSend = Executors.newSingleThreadExecutor();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_wifi_client);

		mDeviceRotation = this.getWindowManager().getDefaultDisplay().getRotation();
		list = new ArrayList<>();
		editText_ip = (EditText) findViewById(R.id.et_ip);
		editText_data = (EditText) findViewById(R.id.et_data);


		et_Send_Rate = (EditText) findViewById(R.id.et_send_rate);

		bindListener(this.findViewById(R.id.btnSendIp));
		bindListener(this.findViewById(R.id.btnSendData));
//		connect();



//注册陀螺仪传感器，并设定传感器向应用中输出的时间间隔类型是SensorManager.SENSOR_DELAY_GAME(20000微秒)

//SensorManager.SENSOR_DELAY_FASTEST(0微秒)：最快。最低延迟，一般不是特别敏感的处理不推荐使用，该模式可能在成手机电力大量消耗，由于传递的为原始数据，诉法不处理好会影响游戏逻辑和UI的性能

//SensorManager.SENSOR_DELAY_GAME(20000微秒)：游戏。游戏延迟，一般绝大多数的实时性较高的游戏都是用该级别

//SensorManager.SENSOR_DELAY_NORMAL(200000微秒):普通。标准延时，对于一般的益智类或EASY级别的游戏可以使用，但过低的采样率可能对一些赛车类游戏有跳帧现象

//SensorManager.SENSOR_DELAY_UI(60000微秒):用户界面。一般对于屏幕方向自动旋转使用，相对节省电能和逻辑处理，一般游戏开发中不使用
//		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//		gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
//		sensorManager.registerListener(this,gyroscopeSensor,SensorManager.SENSOR_DELAY_UI);
//		sensorRegister();
	   }

	private void sensorRegister() {
		String send_rate = et_Send_Rate.getText().toString().trim();
		if (TextUtils.isEmpty(send_rate)) {
			send_rate = "40000";
		}
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensor1 = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		if (sensor1 == null){
			Log.e(TAG,"TYPE_ROTATION_VECTOR sensor not support!");
			return;
		}
		sensorManager.registerListener(this,sensor1, Integer.parseInt(send_rate));
	}


	public void bindListener(View view){
		view.setOnClickListener(this);
	}
	
	
   	public void connect(){
	   sensorRegister();

		mIp = editText_ip.getText().toString();
		if(mIp == null){
			Toast.makeText(WifiClientActivity.this,"请输入连接Ip",Toast.LENGTH_SHORT).show();
			return;
		}

		executorThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				if (!mSocketStatus) {

					try {
						mSocket = new Socket(mIp,4700);

						if(mSocket == null){

						}else {
							mSocketStatus = true;
							mSocket.setKeepAlive(true);
							mSocket.setSoTimeout(10);
							mOutputStream = mSocket.getOutputStream();
							bufferedWriter = new BufferedWriter(new OutputStreamWriter(mOutputStream , "GBK"));
							while (true) {
								mSocket.sendUrgentData(0xFF); // 发送心跳包
								System.out.println("连接服务器正常！");
								Thread.sleep(3 * 1000);
							}
						}
					} catch (IOException e) {
//	        	            Toast.makeText(WifiClientActivity.this,"连接失败...",Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(WifiClientActivity.this,"客户端和服务器已断开...",Toast.LENGTH_SHORT).show();

					}

				}
			}
		});






   	}

	public void send( String mSendData){
		if(mSendData == null){
			Toast.makeText(WifiClientActivity.this,"请输入发送内容",Toast.LENGTH_SHORT).show();
		}else {
			//在后面加上 '\0' ,是为了在服务端方便我们去解析；
			mSendData = mSendData + '\0';
		}


		final String finalMSendData = mSendData;
//			executorThreadPoolSend.execute(new Runnable() {
//				@Override
//				public void run() {
//					if(mSocketStatus) {
//						try {
//							bufferedWriter.write(finalMSendData);
//							bufferedWriter.flush();
//						} catch (IOException e) {
////	        	            Toast.makeText(WifiClientActivity.this,"发送数据失败...",Toast.LENGTH_SHORT).show();
//							e.printStackTrace();
//						}
//					}
//				}
//			});
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				if(mSocketStatus) {
					try {
						bufferedWriter.write(finalMSendData);
						bufferedWriter.flush();
					} catch (IOException e) {
//	        	            Toast.makeText(WifiClientActivity.this,"发送数据失败...",Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();

	}


	public void send(final ArrayList<SensorEvent> list){
//	        mSendData = editText_data.getText().toString();
		if(list == null && list.size() == 0){
			Toast.makeText(WifiClientActivity.this,"请输入发送内容",Toast.LENGTH_SHORT).show();
		}else {
			//在后面加上 '\0' ,是为了在服务端方便我们去解析；
			mSendData = mSendData + '\0';
		}


		Thread thread = new Thread(){
			@Override
			public void run() {
				super.run();
				if(mSocketStatus){
					try {
						bufferedWriter.write(mSendData);
						bufferedWriter.flush();
					} catch (IOException e) {
//	        	            Toast.makeText(WifiClientActivity.this,"发送数据失败...",Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}

				}

			}
		};
		thread.start();

	}

	/**
	 * 两次退出程序
	 */
	private long mExitTime;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 1400) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();

			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	    

	/*当客户端界面返回时，关闭相应的socket资源*/
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		/*关闭相应的资源*/
		try {
			if(bufferedWriter != null){
				bufferedWriter.close();
			}
			if(mOutputStream != null){
				mOutputStream.close();
			}
			if(mSocket != null){
				mSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnSendIp://发送IP地址
			connect();
			break;
		case R.id.btnSendData://发送数据

			count++;
			send(count + "\n");
			break;

		default:
			break;
		}
	}

	private int count = 100;
//	@Override  /* 对于陀螺仪，测量的是x、y、z三个轴向的角速度，分别从values[0]、values[1]、values[2]中读取，单位为弧度/秒。*/
//	public void onSensorChanged(SensorEvent event) {
//		if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
//			count++;
//			if (timestamp != 0) {
//				final float dT = (event.timestamp - timestamp) * NS2S;
//
//				angle[0] += event.values[0] * dT;
//
//				angle[1] += event.values[1] * dT;
//
//				angle[2] += event.values[2] * dT;
//
//				float anglex = (float) Math.toDegrees(angle[0]);
//
//				float angley = (float) Math.toDegrees(angle[1]);
//
//				float anglez = (float) Math.toDegrees(angle[2]);
//
//
//				timestamp = event.timestamp;
//
//				if (flag && count % 10 == 0) {
//					count = 0;
////					showInfo("\nTYPE_GYROSCOPE:" + " x=" + event.values[0] + " y=" + event.values[1] + " z=" + event.values[2]);
////					send("TYPE_GYROSCOPE:客户端" + " x=" + event.values[0] + " y=" + event.values[1] + " z=" + event.values[2] + "\n");
//					send(event.values.toString() + "\n");
//					showInfo(event.values.toString());
//				}
//			}
//		}
//	}
//
//	//在华为P6的机器上，陀螺仪非常敏感，平放在桌面，由于电脑照成的轻微震动在不断地刷屏，为了避免写UI造成的性能问题，只写Log。
//
//	@Override
//	public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//	}

	private void showInfo(String info){
		Log.e("陀螺仪",info);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		sensorManager.unregisterListener(this);
	}



	private int mDeviceRotation;

	private float[] mSensorMatrix = new float[16];
	private final float base =  0.0001f;
	private float[] lastValues = new float[5];
	@Override
	public void onSensorChanged(SensorEvent event) {
		int type = event.sensor.getType();
		switch (type){
			case Sensor.TYPE_ROTATION_VECTOR:
//				VRUtil.sensorRotationVector2Matrix(event, mDeviceRotation, mSensorMatrix);
				float[] values = event.values;
				boolean flag = false;
				for (int i = 0; i < values.length ; i++){
					if (Math.abs(values[i] - lastValues[i]) > base) {
						flag = true;
						break;
					}
				}

				for (int i = 0; i < values.length ; i++){
					lastValues[i] = values[i];
				}

				if (flag) {
//					showInfo(substring);
					String s = Arrays.toString(values);
					showInfo(s);
					String substring = s.substring(1, s.length() - 1);
					showInfo(substring);
					send(substring + "\n");
				}

				break;
		}

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}
}
