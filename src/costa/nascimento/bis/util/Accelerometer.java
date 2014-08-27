package costa.nascimento.bis.util;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import costa.nascimento.bis.settings.DeviceSettings;

public class Accelerometer implements SensorEventListener {

	private float currentAccelerationX;

	private static Accelerometer sharedAccelerometer = null;
	private SensorManager sensorManager;

	private AccelerometerObserver observer;

	private Accelerometer() {
		sensorManager = DeviceSettings.getSensorManager();
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

	/**
	 * Configura o aceler�metro.
	 */
	public void registerAccelerometer() {

		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_GAME);
	}

	/**
	 * Deixa de ouvir os sensores do aparelho.
	 */
	public void unregisterAccelerometer() {
		sensorManager.unregisterListener(this);

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO M�todo n�o implementado.
	}

	/**
	 * M�todo chamado quando h� mudan�as no aceler�metro. Curiosamente, quando o
	 * aparelho � virado para a esquerda, o aceler�metro retorna valores
	 * positivos, enquanto para a direita ele retorna valores negativos.
	 */
	@Override
	public void onSensorChanged(SensorEvent acceleration) {

		// pega a posi��o X do aparelho menos a posi��o inicial configurada.
		this.currentAccelerationX = acceleration.values[0];

		if (this.observer != null) {
			this.observer.accelerometerDidAccelerate(currentAccelerationX);
		}

	}

	public void setDelegate(AccelerometerObserver delegate) {
		this.observer = delegate;
	}

	public AccelerometerObserver getDelegate() {
		return observer;
	}

}
