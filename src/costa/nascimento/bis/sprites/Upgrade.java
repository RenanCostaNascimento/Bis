package costa.nascimento.bis.sprites;

import static costa.nascimento.bis.settings.DeviceSettings.screenHeight;
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
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.types.CGPoint;

import costa.nascimento.bis.layers.UpgradeEngineObserver;

public abstract class Upgrade extends CCSprite {

	private float positionX, positionY;
	private UpgradeEngineObserver observer;

	private static final float DESLOCATION_SPEED = 0.75f;
	protected static final float ANIMATION_SPEED = 1.225f;

	public Upgrade(CCSpriteFrame spriteFrame) {
		super(spriteFrame);

		int spriteWidth = Math.round(this.getBoundingBox().size.width);

		positionX = new Random().nextInt(Math.round(screenWidth()) - 2
				* spriteWidth)
				+ spriteWidth;

		positionY = screenHeight() * 1.1f;
		this.setPosition(positionX, positionY);
	}

	/**
	 * Adicionado o efeito do upgrade no jogador.
	 * 
	 * @param player
	 *            O jogador que pegou o upgrade.
	 */
	public abstract void addEffect(Player player);

	/**
	 * Agenda a movimenta��o do upgrade.
	 */
	public void start() {
		schedule("startMoving");
	}

	/**
	 * Diminui a posi��o do upgrade.
	 * 
	 * @param dt
	 *            De quanto em quanto tempo o m�todo ser� chamado pelo framwork.
	 */
	public void startMoving(float dt) {
		positionY -= DESLOCATION_SPEED;
		if (positionY > 0) {
			this.setPosition(CGPoint.ccp(positionX, positionY));
		} else {
			// m�todo chamado quando o jogador n�o consegue abater um
			// meteoro.
			upgradeMissed();
		}
	}

	/**
	 * Procedimentos que ser�o realizados quando o upgrade precisa ser removido
	 * do jogo.
	 */
	private void upgradeMissed() {
		this.observer.removeUpgrade(this);
		this.removeFromParentAndCleanup(true);

	}

	public void upgradeCollected() {
		this.observer.removeUpgrade(this);

		// para de ficar chamando o update
		this.unschedule("startMoving");

		// som de upgrade pickup
		playUpgradeSoundEffect();

		// As a��es abaixo, juntas, far�o o efeito upgrade pickup.
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
	 * Toca o efeito do respectivo upgrade.
	 */
	protected abstract void playUpgradeSoundEffect();

	/**
	 * Faz a anima��o do upgrade.
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
	protected void animateUpgrade(String animationName, int spriteQuantity,
			float duration) {

		CCAnimation animation = CCAnimation.animation(animationName);

		for (int i = 1; i <= spriteQuantity; i++) {
			animation.addFrame(CCSpriteFrameCache.sharedSpriteFrameCache()
					.spriteFrameByName(
							String.format(animationName + "_%d.png", i)));
		}

		CCIntervalAction intervalAction = CCAnimate.action(duration, animation,
				false);
		CCRepeatForever forever = CCRepeatForever.action(intervalAction);
		this.runAction(forever);

	}

	public void setObserver(UpgradeEngineObserver observer) {
		this.observer = observer;
	}

	/**
	 * Retira o objeto da mem�ria.
	 */
	public void removeMe() {
		this.removeFromParentAndCleanup(true);
	}
}
