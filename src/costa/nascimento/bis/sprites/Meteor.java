package costa.nascimento.bis.sprites;

import static costa.nascimento.bis.settings.DeviceSettings.screenHeight;
import static costa.nascimento.bis.settings.DeviceSettings.screenResolution;
import static costa.nascimento.bis.settings.DeviceSettings.screenWidth;

import java.util.ArrayList;
import java.util.Random;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCIntervalAction;
import org.cocos2d.actions.interval.CCScaleBy;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.actions.interval.CCSpawn;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.nodes.CCSpriteSheet;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;

import costa.nascimento.bis.R;
import costa.nascimento.bis.constants.Constants;
import costa.nascimento.bis.layers.MeteorsEngineObserver;
import costa.nascimento.bis.util.Runner;

public class Meteor extends CCSprite {
	private float positionX, positionY, rotation;
	private int pointsWorth, flightSpeed, rotationSpeed;

	// meteoros normais
	private static final int NORMAL_POINTS_WORTH = 1;
	private static final int NORMAL_FLIGHT_SPEED = 1;
	private static final int NORMAL_ROTATION_SPEED = 1;

	// meteoros especiais
	private static final int ESPECIAL_POINTS_WORTH = 3;
	private static final int ESPECIAL_FLIGHT_SPEED = 1;
	private static final int ESPECIAL_ROTATION_SPEED = 0;

	private MeteorsEngineObserver observer;

	public Meteor(String image) {
		super(image);

		positionX = new Random().nextInt(Math.round(screenWidth()) - 30) + 30;
		positionY = screenHeight();
		rotation = 0;

		if (image.equals(Constants.METEOR)) {
			inicializeNormalMeteor();
		} else {
			inicializeEspecialMeteor();
			animateSpecialMeteor();
		}

	}

	/**
	 * Inicializa as vari�veis com dados dos meteoros especiais
	 */
	private void inicializeEspecialMeteor() {
		this.pointsWorth = ESPECIAL_POINTS_WORTH;
		this.flightSpeed = ESPECIAL_FLIGHT_SPEED;
		this.rotationSpeed = ESPECIAL_ROTATION_SPEED;
	}

	/**
	 * Inicializa as vari�veis com dados dos meteoros especiais
	 */
	private void inicializeNormalMeteor() {
		this.pointsWorth = NORMAL_POINTS_WORTH;
		this.flightSpeed = NORMAL_FLIGHT_SPEED;
		this.rotationSpeed = NORMAL_ROTATION_SPEED;
	}

	/**
	 * Chamado a cada frame, o m�todo start invoca o m�todo update.
	 */
	public void start() {
		this.schedule("update");
	}

	/**
	 * Diminui a posi��o do meteoro e o rotaciona, se o jogo n�o estiver
	 * pausado.
	 * 
	 * @param dt
	 */
	public void update(float dt) {
		Runner.check();
		if (!Runner.isGamePaused()) {
			rotation += rotationSpeed;
			positionY -= this.flightSpeed;
			if (positionY > 0) {
				this.setPosition(screenResolution(CGPoint.ccp(positionX,
						positionY)));
				this.setRotation(rotation);
			} else {
				// m�todo chamado quando o jogador n�o consegue abater um
				// meteoro.
				meteorEscaped();
			}
		}
	}

	/**
	 * Faz a anima��o dos meteoros especiais.
	 */
	private void animateSpecialMeteor() {
		// cria uma anima��o sem nome, com 1 de delay
		// TODO qual a diferen�a entre o delay da animation com o do intervalAction
		CCAnimation animation = CCAnimation.animation("", 1f);
		CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames(
				Constants.ESPECIAL_METEOR_SHEET);

		for (int i = 1; i <= 8; i++) {
			animation.addFrame(CCSpriteFrameCache.sharedSpriteFrameCache()
					.spriteFrameByName(
							String.format("especialMeteor_%d.png", i)));
		}

		CCIntervalAction intervalAction = CCAnimate.action(1.5f, animation,
				false);
		CCRepeatForever forever = CCRepeatForever.action(intervalAction);
		this.runAction(forever);
	}

	/**
	 * Remove o meteoro do array de meteoros do observador e diminiu a pontua��o
	 * do jogador.
	 */
	private void meteorEscaped() {
		this.observer.meteorEscaped(this);
		this.unschedule("update");
		removeMe();
	}

	public void setDelegate(MeteorsEngineObserver delegate) {
		this.observer = delegate;
	}

	/**
	 * Faz todos os ajustes necess�rios para que o meteoro seja destru�do:
	 * remove do array em GameScene, cria efeitos de destrui��o e remove da
	 * mem�ria.
	 */
	public void shooted() {
		this.observer.removeMeteor(this);

		// para de ficar chamando o update
		this.unschedule("update");

		// som de explos�o
		SoundEngine.sharedEngine().playEffect(
				CCDirector.sharedDirector().getActivity(), R.raw.bang);

		// As a��es abaixo, juntas, far�o o efeito de destrui��o do meteoro.
		float dt = 0.2f;
		// Diminui a imagem...
		CCScaleBy a1 = CCScaleBy.action(dt, 0.5f);
		// Faz a imagem desaparecer devagar...
		CCFadeOut a2 = CCFadeOut.action(dt);
		// Combina as a��es...
		CCSpawn s1 = CCSpawn.actions(a1, a2);

		// Indica o que deve ocorrer depois que a anima��o terminar...
		CCCallFunc c1 = CCCallFunc.action(this, "removeMe");

		// Executa as a��es
		this.runAction(CCSequence.actions(s1, c1));
	}

	/**
	 * Remove o meteoro de mem�ria. Esse m�todo deve ser p�blico, caso contr�rio
	 * ele n�o ser� chamado pelo runAction.
	 */
	public void removeMe() {
		// Esse m�todo remove e coleta um objeto de mem�ria.
		this.removeFromParentAndCleanup(true);
	}

	public int getPointsWorth() {
		return pointsWorth;
	}

}
