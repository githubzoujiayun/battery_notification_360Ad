package minggo.battery.util;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;


public class ShakeListener implements SensorEventListener ,HandshakeCompletedListener{
	private static final int SPEED_SHRESHOLD = 2000;
	private static final int UPTATE_INTERVAL_TIME = 70;
	private SensorManager sensorManager;
	private Sensor sensor;
	private OnShakeListener onShakeListener;
	private Context mContext;
	private float lastX;
	private float lastY;
	private float lastZ;
	private long lastUpdateTime;

	public ShakeListener(Context c) {
		mContext = c;
		start();
	}

	public void start() {
		sensorManager = (SensorManager) mContext
				.getSystemService(Context.SENSOR_SERVICE);
		if (sensorManager != null) {
			sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		}
		if (sensor != null) {
			sensorManager.registerListener(this, sensor,
					SensorManager.SENSOR_DELAY_GAME);
		}

	}

	public void stop() {
		sensorManager.unregisterListener(this);
	}

	public void setOnShakeListener(OnShakeListener listener) {
		onShakeListener = listener;
	}

	public void onSensorChanged(SensorEvent event) {
		long currentUpdateTime = System.currentTimeMillis();
		long timeInterval = currentUpdateTime - lastUpdateTime;
		if (timeInterval < UPTATE_INTERVAL_TIME)
			return;
		lastUpdateTime = currentUpdateTime;

		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];

		float deltaX = x - lastX;
		float deltaY = y - lastY;
		float deltaZ = z - lastZ;

		lastX = x;
		lastY = y;
		lastZ = z;

		double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ
				* deltaZ)
				/ timeInterval * 10000;
		//Log.v("thelog", "===========log===================");
		if (speed >= SPEED_SHRESHOLD) {
			onShakeListener.onShake();
		}
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	public interface OnShakeListener {
		public void onShake();
	}

	@Override
	public void handshakeCompleted(HandshakeCompletedEvent event) {
		
	}
	
}