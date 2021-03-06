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
import costa.nascimento.bis.layers.MeteorsEngineObserver;

public abstract class Meteor extends CCSprite {
	private float positionX, positionY;
	protected int pointsWorth, flightSpeed;

	private MeteorsEngineObserver observer;

	public Meteor(CCSpriteFrame frame) {
		super(frame);

		int spriteWidth = Math.round(this.getBoundingBox().size.width);

		positionX = new Random().nextInt(Math.round(screenWidth()) - 2
				* spriteWidth)
				+ spriteWidth;

		positionY = screenHeight() * 1.1f;
		this.setPosition(positionX, positionY);

	}

	/**
	 * Chamado a cada frame, o m�todo start invoca o m�todo update.
	 */
	public void start() {
		this.schedule("update");
	}

	/**
	 * Diminui a posi��o do meteoro e o rotaciona.
	 * 
	 * @param dt
	 */
	public void update(float dt) {
		positionY -= this.flightSpeed;
		if (positionY > 0) {
			this.setPosition(screenResolution(CGPoint.ccp(positionX, positionY)));
		} else {
			// m�todo chamado quando o jogador n�o consegue abater um
			// meteoro.
			meteorEscaped();
		}
	}

	/**
	 * Faz a anima��o do meteoro.
	 * 
	 * @param animationName
	 *            O nome da anima��o. Precisa ter o nome das imagens
	 *            especificadas no arquivo .plist.
	 * @param spriteQuantity
	 *            A quantidade de sprites que a anima��o cont�m. Precisa ter no
	 *            m�nimo a mesma quantidade de sprites listada no arquivo
	 *            .plist.
	 * @param duration
	 *            Quanto tempo a anima��o demora para fazer um ciclo.
	 */
	protected void animateMeteor(String animationName, int spriteQuantity,
			float animationSpeed) {
		// cria uma anima��o chamada especialMeteorAnimation
		// nesse momento o nome n�o � importante
		CCAnimation animation = CCAnimation.animation(animationName);

		// preenche a anima��o com os sprites do sprite sheet
		for (int i = 1; i <= spriteQuantity; i++) {
			animation.addFrame(CCSpriteFrameCache.sharedSpriteFrameCache()
					.spriteFrameByName(
							String.format(animationName + "_%d.png", i)));
		}

		// cria uma a��o baseada na anima��o que ir� executar para sempre
		// ESPECIAL_ANIMATION_SPEED representa o tempo que cada frame dever�
		// ficar na tela antes de ser trocado
		CCIntervalAction intervalAction = CCAnimate.action(animationSpeed,
				animation, true);
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
