package costa.nascimento.bis.sprites;

import static costa.nascimento.bis.settings.DeviceSettings.screenWidth;

import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCScaleBy;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.actions.interval.CCSpawn;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;

import costa.nascimento.bis.R;
import costa.nascimento.bis.constants.Constants;
import costa.nascimento.bis.layers.ShootEngineObserver;
import costa.nascimento.bis.util.Accelerometer;
import costa.nascimento.bis.util.AccelerometerObserver;
import costa.nascimento.bis.util.Runner;

public class Player extends CCSprite implements AccelerometerObserver {
	private float positionX = screenWidth() / 2;
	private float positionY = 100;

	private float currentAccelX;

	// constante criada com o intuito de impedir que qualquer variação no
	// acelerômetro gere um evento de movimentação do player.
	private static final double NOISE = 1;

	private ShootEngineObserver delegate;
	private Accelerometer accelerometer;

	public Player() {
		super(Constants.NAVE);
		setPosition(positionX, positionY);
	}

	public void setDelegate(ShootEngineObserver delegate) {
		this.delegate = delegate;
	}

	/**
	 * Faz o jogador atirar, se o jogo não estiver em pause.
	 */
	public void shoot() {
		if (!Runner.isGamePaused()) {
			delegate.createShoot(new Shoot(positionX, positionY));
		}

	}

	/**
	 * Faz o jogador se movimentar para a esquerda.
	 */
	public void moveLeft() {
		if (!Runner.isGamePaused()) {
			if (positionX > 30) {
				positionX -= 10;
			}
			setPosition(positionX, positionY);
		}
	}

	/**
	 * Faz o jogador se movimentar para a direita.
	 */
	public void moveRight() {
		if (!Runner.isGamePaused()) {
			if (positionX < screenWidth() - 30) {
				positionX += 10;
			}
			setPosition(positionX, positionY);
		}
	}

	/**
	 * Faz todos os ajustes necessários para que o player seja destruído: remove
	 * do array em GameScene, cria efeitos de destruição e remove da memória.
	 */
	public void explode() {
		// cria o som de explosão
		SoundEngine.sharedEngine().playEffect(
				CCDirector.sharedDirector().getActivity(), R.raw.over);

		// para a música do jogo
		SoundEngine.sharedEngine().pauseSound();

		// Cria efeitos
		float dt = 0.2f;
		CCScaleBy a1 = CCScaleBy.action(dt, 2f);
		CCFadeOut a2 = CCFadeOut.action(dt);
		CCSpawn s1 = CCSpawn.actions(a1, a2);

		// Roda os efeitos
		this.runAction(CCSequence.actions(s1));
	}

	/**
	 * Método chamado quando o Accelerometer muda seus valores de X ou Y.
	 */
	@Override
	public void accelerometerDidAccelerate(float x) {
		this.currentAccelX = x;
		update();

	}

	/**
	 * Configura o Accelerometer para a classe.
	 */
	public void catchAccelerometer() {
		this.accelerometer = Accelerometer.sharedAccelerometer();
		this.accelerometer.setDelegate(this);
	}

	/**
	 * Move o avião com base no acelerômetro.
	 */
	private void update() {
		if (this.currentAccelX < -NOISE) {
			this.positionX++;
		}

		if (this.currentAccelX > NOISE) {
			this.positionX--;
		}

		this.setPosition(CGPoint.ccp(this.positionX, this.positionY));
	}

}
