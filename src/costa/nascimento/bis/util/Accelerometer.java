package costa.nascimento.bis.util;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import costa.nascimento.bis.settings.DeviceSettings;

public class Accelerometer implements SensorEventListener {

	private float currentAccelerationX;
	private float calibratedAccelerationX = 0;
	private int calibrated = 0;

	private static Accelerometer sharedAccelerometer = null;
	private SensorManager sensorManager;

	private AccelerometerObserver delegate;

	private Accelerometer() {
		this.catchAccelerometer();
	}

	/**
	 * Configura o aceler�metro.
	 */
	public void catchAccelerometer() {
		sensorManager = DeviceSettings.getSensorManager();
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_GAME);
	}

	/**
	 * Singleton para a classe Accelerometer.
	 * 
	 * @return O Accelerometer.
	 */
	public static Accelerometer sharedAccelerometer() {
		if (sharedAccelerometer == null) {
			sharedAccelerometer = new Accelerometer();
		}
		return sharedAccelerometer;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO M�todo n�o implementado.
	}

	@Override
	public void onSensorChanged(SensorEvent acceleration) {

		if (calibrated < 100) {
			initialCalibration(acceleration);
		}

		// pega a posi��o X do aparelho menos a posi��o inicial configurada.
		this.currentAccelerationX = acceleration.values[0]
				- this.calibratedAccelerationX;

		this.delegate.accelerometerDidAccelerate(currentAccelerationX);

	}

	/**
	 * Calibra a posi��o inicial do aceler�metro baseado na posi��o inicial em
	 * que o jogador segura o aparelho. O m�todo � chamado 100 vezes de modo a
	 * aumentar a precis�o do aceler�metro.
	 * 
	 * @param acceleration
	 *            O SensorEvent que cont�m os dados do aceler�metro.
	 */
	private void initialCalibration(SensorEvent acceleration) {

		this.calibratedAccelerationX += acceleration.values[0];
		calibrated++;
		if (calibrated == 100) {
			this.calibratedAccelerationX /= 100;
		}
		return;

	}

	public void setDelegate(AccelerometerObserver delegate) {
		this.delegate = delegate;
	}

	public AccelerometerObserver getDelegate() {
		return delegate;
	}

}
