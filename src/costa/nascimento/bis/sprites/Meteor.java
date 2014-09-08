package costa.nascimento.bis.sprites;

import static costa.nascimento.bis.settings.DeviceSettings.screenHeight;
import static costa.nascimento.bis.settings.DeviceSettings.screenResolution;
import static costa.nascimento.bis.settings.DeviceSettings.screenWidth;

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
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;

import costa.nascimento.bis.R;
import costa.nascimento.bis.constants.Constants;
import costa.nascimento.bis.layers.MeteorsEngineObserver;

public class Meteor extends CCSprite {
	private float positionX, positionY;
	private int pointsWorth, flightSpeed;

	// meteoros normais
	private static final int NORMAL_POINTS_WORTH = 1;
	private static final int NORMAL_FLIGHT_SPEED = 1;
	private static final int NORMAL_SPRITE_QUANTITY = 30;
	private static final float NORMAL_ANIMATION_SPEED = 3.5f;

	// meteoros especiais
	private static final int ESPECIAL_POINTS_WORTH = 2;
	private static final int ESPECIAL_FLIGHT_SPEED = 2;
	private static final int ESPECIAL_SPRITE_QUANTITY = 8;
	private static final float ESPECIAL_ANIMATION_SPEED = 1.225f;

	private MeteorsEngineObserver observer;

	public Meteor(CCSpriteFrame frame) {
		super(frame);

		int spriteWidth = Math.round(this.getBoundingBox().size.width);

		positionX = new Random().nextInt(Math.round(screenWidth()) - 2
				* spriteWidth)
				+ spriteWidth;

		positionY = screenHeight() * 1.1f;
		this.setPosition(positionX, positionY);

		if (frame.equals(CCSpriteFrameCache.sharedSpriteFrameCache()
				.spriteFrameByName(Constants.METEOR_1))) {
			inicializeNormalMeteor();
			animateMeteor("meteor", NORMAL_SPRITE_QUANTITY,
					NORMAL_ANIMATION_SPEED);
		} else {
			inicializeEspecialMeteor();
			animateMeteor("especialMeteor", ESPECIAL_SPRITE_QUANTITY,
					ESPECIAL_ANIMATION_SPEED);
		}

	}

	/**
	 * Inicializa as variáveis com dados dos meteoros especiais
	 */
	private void inicializeEspecialMeteor() {
		this.pointsWorth = ESPECIAL_POINTS_WORTH;
		this.flightSpeed = ESPECIAL_FLIGHT_SPEED;
	}

	/**
	 * Inicializa as variáveis com dados dos meteoros especiais
	 */
	private void inicializeNormalMeteor() {
		this.pointsWorth = NORMAL_POINTS_WORTH;
		this.flightSpeed = NORMAL_FLIGHT_SPEED;
	}

	/**
	 * Chamado a cada frame, o método start invoca o método update.
	 */
	public void start() {
		this.schedule("update");
	}

	/**
	 * Diminui a posição do meteoro e o rotaciona.
	 * 
	 * @param dt
	 */
	public void update(float dt) {
		positionY -= this.flightSpeed;
		if (positionY > 0) {
			this.setPosition(screenResolution(CGPoint.ccp(positionX, positionY)));
		} else {
			// método chamado quando o jogador não consegue abater um
			// meteoro.
			meteorEscaped();
		}
	}

	/**
	 * Faz a animação dos meteoros.
	 */
	private void animateMeteor(String animationName, int spriteQuantity,
			float animationSpeed) {
		// cria uma animação chamada especialMeteorAnimation
		// nesse momento o nome não é importante
		CCAnimation animation = CCAnimation.animation(animationName);

		// preenche a animação com os sprites do sprite sheet
		for (int i = 1; i <= spriteQuantity; i++) {
			animation.addFrame(CCSpriteFrameCache.sharedSpriteFrameCache()
					.spriteFrameByName(
							String.format(animationName + "_%d.png", i)));
		}

		// cria uma ação baseada na animação que irá executar para sempre
		// ESPECIAL_ANIMATION_SPEED representa o tempo que cada frame deverá
		// ficar na tela antes de ser trocado
		CCIntervalAction intervalAction = CCAnimate.action(animationSpeed,
				animation, true);
		CCRepeatForever forever = CCRepeatForever.action(intervalAction);
		this.runAction(forever);
	}

	/**
	 * Remove o meteoro do array de meteoros do observador e diminiu a pontuação
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
	 * Faz todos os ajustes necessários para que o meteoro seja destruído:
	 * remove do array em GameScene, cria efeitos de destruição e remove da
	 * memória.
	 */
	public void shooted() {
		this.observer.removeMeteor(this);

		// para de ficar chamando o update
		this.unschedule("update");

		// som de explosão
		SoundEngine.sharedEngine().playEffect(
				CCDirector.sharedDirector().getActivity(), R.raw.bang);

		// As ações abaixo, juntas, farão o efeito de destruição do meteoro.
		float dt = 0.2f;
		// Diminui a imagem...
		CCScaleBy a1 = CCScaleBy.action(dt, 0.5f);
		// Faz a imagem desaparecer devagar...
		CCFadeOut a2 = CCFadeOut.action(dt);
		// Combina as ações...
		CCSpawn s1 = CCSpawn.actions(a1, a2);

		// Indica o que deve ocorrer depois que a animação terminar...
		CCCallFunc c1 = CCCallFunc.action(this, "removeMe");

		// Executa as ações
		this.runAction(CCSequence.actions(s1, c1));
	}

	/**
	 * Remove o meteoro de memória. Esse método deve ser público, caso contrário
	 * ele não será chamado pelo runAction.
	 */
	public void removeMe() {
		// Esse método remove e coleta um objeto de memória.
		this.removeFromParentAndCleanup(true);
	}

	public int getPointsWorth() {
		return pointsWorth;
	}

}
