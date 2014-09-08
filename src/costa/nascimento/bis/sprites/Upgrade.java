package costa.nascimento.bis.sprites;

import static costa.nascimento.bis.settings.DeviceSettings.screenHeight;
import static costa.nascimento.bis.settings.DeviceSettings.screenWidth;

import java.util.Random;

import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.types.CGPoint;

public abstract class Upgrade extends CCSprite {

	private float positionX, positionY;

	private static final float DESLOCATION_SPEED = 0.75f;

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
	 * Agenda a movimenta��o do upgrade.
	 */
	public void start() {
		schedule("update");
	}

	/**
	 * Diminui a posi��o do upgrade.
	 * 
	 * @param dt
	 *            De quanto em quanto tempo o m�todo ser� chamado pelo framwork.
	 */
	public void update(float dt) {
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
	 * Procedimentos que ser�o realizados quando o jogador n�o consegue pegar o
	 * upgrade.
	 */
	private void upgradeMissed() {
		// TODO Auto-generated method stub

	}

}
