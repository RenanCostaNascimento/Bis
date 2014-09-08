package costa.nascimento.bis;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.sound.SoundEngine;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.view.WindowManager;
import costa.nascimento.bis.scene.TitleScreen;
import costa.nascimento.bis.settings.DeviceSettings;
import costa.nascimento.bis.util.Runner;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// definindo orientação como portrait
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// configura a tela
		CCGLSurfaceView glSurfaceView = new CCGLSurfaceView(this);
		setContentView(glSurfaceView);
		CCDirector.sharedDirector().attachInView(glSurfaceView);

		// configura CCDirector
		CCDirector.sharedDirector().setScreenSize(320, 480);

		// configura o sensor
		configSensorManager();

		// abre tela principal
		CCScene scene = new TitleScreen().scene();
		CCDirector.sharedDirector().runWithScene(scene);
	}

	@Override
	protected void onStart() {
		super.onStart();

		// configura o runner
		Runner.check();

		// começa a ouvir o acelerômetro
		// Accelerometer.sharedAccelerometer().registerAccelerometer();
	}

	@Override
	protected void onResume() {
		super.onResume();

		SoundEngine.sharedEngine().resumeSound();
	}

	@Override
	protected void onPause() {
		super.onPause();
		SoundEngine.sharedEngine().pauseSound();
	}

	@Override
	protected void onStop() {
		super.onStop();
		// deixa de ouvir acelerômetro
		// Accelerometer.sharedAccelerometer().unregisterAccelerometer();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		SoundEngine.sharedEngine().realesAllEffects();
		SoundEngine.sharedEngine().realesAllSounds();
	}

	private void configSensorManager() {
		SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		DeviceSettings.setSensorManager(sensorManager);
	}

}
