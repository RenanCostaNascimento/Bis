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
import org.cocos2d.types.CGRect;

import costa.nascimento.bis.R;
import costa.nascimento.bis.constants.Constants;
import costa.nascimento.bis.layers.ShootEngineObserver;
import costa.nascimento.bis.util.Accelerometer;
import costa.nascimento.bis.util.AccelerometerObserver;
import costa.nascimento.bis.util.Runner;

public class Player extends CCSprite implements AccelerometerObserver {
	private float positionX = screenWidth() / 2;
	private float positionY = 50;

	private float currentMovementSpeed;

	private float currentAccelX;

	private static final float BASE_MOVEMENT_SPEED = 0.75f;
	private static final float BASE_ATTACK_SPEED = 1.5f;

	// constante criada com o intuito de impedir que qualquer varia��o no
	// aceler�metro gere um evento de movimenta��o do player.
	private static final double THRESHOLD_OF_MOVEMENT = 1;

	private ShootEngineObserver delegate;

	public Player() {
		super(Constants.NAVE);
		setPosition(positionX, positionY);
		currentMovementSpeed = BASE_MOVEMENT_SPEED;
	}

	public void setDelegate(ShootEngineObserver delegate) {
		this.delegate = delegate;
	}

	/**
	 * Agenda a movimenta��o do jogador para a direita enquanto o bot�o de
	 * movimento estiver pressionado.
	 */
	public void startMovingLeft() {
		this.schedule("keepMovingLeft");
	}

	/**
	 * Desagenda a movimenta��o para a esquerda do jogador quando o bot�o �
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
		if (!CGRect.containsPoint(this.getBoundingBox(),
				CGPoint.ccp(0, this.positionY))) {
			positionX -= currentMovementSpeed;
		} else {
			this.unschedule("keepMovingLeft");
		}
		setPosition(positionX, positionY);
	}

	/**
	 * Agenda a movimenta��o do jogador para a direita enquanto o bot�o de
	 * movimento estiver pressionado.
	 */
	public void startMovingRight() {
		this.schedule("keepMovingRight");
	}

	/**
	 * Desagenda a movimenta��o para a direita do jogador quando o bot�o � deixa
	 * de ser pressionado.
	 */
	public void stopMovingRight() {
		this.unschedule("keepMovingRight");
	}

	/**
	 * Movimenta o jogador para a direita.
	 */
	public void keepMovingRight(float dt) {
		if (!CGRect.containsPoint(this.getBoundingBox(),
				CGPoint.ccp(screenWidth(), this.positionY))) {
			positionX += currentMovementSpeed;
		} else {
			this.unschedule("keepMovingRight");
		}
		setPosition(positionX, positionY);
	}

	/**
	 * Move a nave baseado na posi��o do toque do jogador.
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
	 * Faz verifica��es de quando a nave deve ser movimentada, e para onde.
	 * 
	 * @param newPositionX
	 *            Posi��o x do toque do jogador na tela.
	 */
	private void moveXAxis(float newPositionX) {
		// direita
		if (positionX < screenWidth() - 30 && newPositionX > positionX) {
			positionX += currentMovementSpeed;
		}
		// esquerda
		if (positionX > 30 && newPositionX < positionX) {
			positionX -= currentMovementSpeed;
		}
	}

	/**
	 * Agenda o tiro do jogador.
	 */
	public void startShooting() {
		schedule("shoot", BASE_ATTACK_SPEED);
	}

	/**
	 * Faz o jogador atirar, se o jogo n�o estiver me pause. Esse m�todo �
	 * utilizado pelo scheduler do Cocos2D.
	 * 
	 * @param dt
	 *            De quanto em quanto tempo a a��o deve ser executada.
	 */
	public void shoot(float dt) {
		delegate.createShoot(new Shoot(positionX, positionY + 20));
	}

	/**
	 * Faz todos os ajustes necess�rios para que o player seja destru�do: remove
	 * do array em GameScene, cria efeitos de destrui��o e remove da mem�ria.
	 */
	public void explode() {
		// cria o som de explos�o
		SoundEngine.sharedEngine().playEffect(
				CCDirector.sharedDirector().getActivity(), R.raw.over);

		// Cria efeitos
		float dt = 0.2f;
		CCScaleBy a1 = CCScaleBy.action(dt, 2f);
		CCFadeOut a2 = CCFadeOut.action(dt);
		CCSpawn s1 = CCSpawn.actions(a1, a2);

		// Roda os efeitos
		this.runAction(CCSequence.actions(s1));
	}

	/**
	 * M�todo chamado quando o Accelerometer muda seus valores de X.
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
	 * Move o avi�o com base no aceler�metro.
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

	/**
	 * Cont�m a l�gica do que acontece quando o jogador pega um upgrade.
	 * 
	 * @param upgrade
	 *            O upgrade coletado.
	 */
	public void upgradeCollected(Upgrade upgrade) {
		upgrade.addEffect(this);
	}

	public void setCurrentMovementSpeed(float currentMovementSpeed) {
		this.currentMovementSpeed = currentMovementSpeed;
	}

	public float getCurrentMovementSpeed() {
		return currentMovementSpeed;
	}

}
