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
	private float positionY = 50;

	private float currentAccelX;

	private static final int MOVEMENT_SPEED = 1;
	private static final float RATE_OF_FIRE = 1;

	// constante criada com o intuito de impedir que qualquer variação no
	// acelerômetro gere um evento de movimentação do player.
	private static final double THRESHOLD_OF_MOVEMENT = 1;

	private ShootEngineObserver delegate;

	public Player() {
		super(Constants.NAVE);
		setPosition(positionX, positionY);
	}

	public void setDelegate(ShootEngineObserver delegate) {
		this.delegate = delegate;
	}

	/**
	 * Agenda a movimentação do jogador para a direita enquanto o botão de
	 * movimento estiver pressionado.
	 */
	public void startMovingLeft() {
		this.schedule("keepMovingLeft");
	}

	/**
	 * Desagenda a movimentação para a esquerda do jogador quando o botão é
	 * deixa de ser pressionado.
	 */
	public void stopMovingLeft() {
		this.unschedule("keepMovingLeft");
	}

	/**
	 * Movimenta o jogador para a esquerda.
	 * 
	 * @param dt
	 */
	public void keepMovingLeft(float dt) {
		if (!Runner.isGamePaused()) {
			if (positionX > 30) {
				positionX -= MOVEMENT_SPEED;
			} else {
				this.unschedule("keepMovingLeft");
			}
			setPosition(positionX, positionY);
		}
	}

	/**
	 * Agenda a movimentação do jogador para a direita enquanto o botão de
	 * movimento estiver pressionado.
	 */
	public void startMovingRight() {
		this.schedule("keepMovingRight");
	}

	/**
	 * Desagenda a movimentação para a direita do jogador quando o botão é deixa
	 * de ser pressionado.
	 */
	public void stopMovingRight() {
		this.unschedule("keepMovingRight");
	}

	/**
	 * Movimenta o jogador para a direita.
	 */
	public void keepMovingRight(float dt) {
		if (!Runner.isGamePaused()) {
			if (positionX < screenWidth() - 30) {
				positionX += MOVEMENT_SPEED;
			} else {
				this.unschedule("keepMovingRight");
			}
			setPosition(positionX, positionY);
		}
	}

	/**
	 * Move a nave baseado na posição do toque do jogador.
	 * 
	 * @param position
	 */
	public void move(CGPoint position) {
		if (!Runner.isGamePaused()) {
			moveXAxis(position.x);
			this.setPosition(positionX, positionY);
		}
	}

	/**
	 * Faz verificações de quando a nave deve ser movimentada, e para onde.
	 * 
	 * @param newPositionX
	 *            Posição x do toque do jogador na tela.
	 */
	private void moveXAxis(float newPositionX) {
		// direita
		if (positionX < screenWidth() - 30 && newPositionX > positionX) {
			positionX += MOVEMENT_SPEED;
		}
		// esquerda
		if (positionX > 30 && newPositionX < positionX) {
			positionX -= MOVEMENT_SPEED;
		}
	}

	/**
	 * Agenda o tiro do jogador.
	 */
	public void startShooting() {
		schedule("shoot", RATE_OF_FIRE);
	}

	/**
	 * Faz o jogador atirar, se o jogo não estiver me pause. Esse método é
	 * utilizado pelo scheduler do Cocos2D.
	 * 
	 * @param dt
	 *            De quanto em quanto tempo a ação deve ser executada.
	 */
	public void shoot(float dt) {
		if (!Runner.isGamePaused()) {
			delegate.createShoot(new Shoot(positionX, positionY + 20));
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
		SoundEngine.sharedEngine().playEffect(
				CCDirector.sharedDirector().getActivity(), R.raw.over2);

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
	 * Método chamado quando o Accelerometer muda seus valores de X.
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

		Accelerometer.sharedAccelerometer().setDelegate(this);
	}

	/**
	 * Move o avião com base no acelerômetro.
	 */
	private void update() {

		// valores negativos, movimento para a direita
		if (this.currentAccelX < -THRESHOLD_OF_MOVEMENT
				&& this.positionX < screenWidth() - 30) {
			this.positionX += 2;
		}

		if (this.currentAccelX > THRESHOLD_OF_MOVEMENT && this.positionX > 30) {
			this.positionX -= 2;
		}

		this.setPosition(CGPoint.ccp(this.positionX, this.positionY));
	}

}
