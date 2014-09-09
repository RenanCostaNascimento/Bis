package costa.nascimento.bis.layers;

import java.util.Random;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCSpriteFrameCache;

import costa.nascimento.bis.constants.Constants;
import costa.nascimento.bis.sprites.AttackSpeedUpgrade;
import costa.nascimento.bis.sprites.MovementSpeedUpgrade;

public class UpgradeEngine extends CCLayer {

	private UpgradeEngineObserver observer;

	// 10% de chance de aparecer um upgrade
	private static final int UPGRADE_SPAWN_CHANCE = 10;
	private static final float UPGRADE_SPAWN_TIME = 3f;
	private static final int UPGRADE_QUANTITY = 2;
	

	public UpgradeEngine() {

		CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames(
				Constants.MOVEMENT_SPEED_SHEET);
		CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames(
				Constants.ATTACK_SPEED_SHEET);

		schedule("createUpgrade", UPGRADE_SPAWN_TIME);
	}

	/**
	 * Criar um upgrade de tempos em tempos
	 * 
	 * @param dt
	 *            Indica a quantidade de tempo, em segundos, que o método é
	 *            chamado.
	 */
	public void createUpgrade(float dt) {
		if (new Random().nextInt(UPGRADE_SPAWN_CHANCE) == 0) {
			switch (new Random().nextInt(UPGRADE_QUANTITY)) {
			case 0:
				this.observer.createUpgrade(new MovementSpeedUpgrade());
				break;
			default:
				this.observer.createUpgrade(new AttackSpeedUpgrade());
				break;
			}
		}
	}

	public UpgradeEngineObserver getObserver() {
		return observer;
	}

	public void setObserver(UpgradeEngineObserver observer) {
		this.observer = observer;
	}
}
