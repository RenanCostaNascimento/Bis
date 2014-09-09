package costa.nascimento.bis.sprites;

import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.sound.SoundEngine;

import costa.nascimento.bis.R;
import costa.nascimento.bis.constants.Constants;

public class MovementSpeedUpgrade extends Upgrade {

	private static final int MAX_MOVEMENT_SPEED = 2;
	private static final float MOVEMENT_SPEED_VARIATION = 0.25f;
	private static final int SPRITE_QUANTITY = 7;

	public MovementSpeedUpgrade() {
		super(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName(
				Constants.MOVEMENT_SPEED_1));

		animateUpgrade("movementSpeed", SPRITE_QUANTITY, ANIMATION_SPEED);
	}

	@Override
	public void addEffect(Player player) {
		if (player.getCurrentMovementSpeed() < MAX_MOVEMENT_SPEED) {
			player.setCurrentMovementSpeed(player.getCurrentMovementSpeed()
					+ MOVEMENT_SPEED_VARIATION);
		}

	}

	@Override
	protected void playUpgradeSoundEffect() {
		SoundEngine.sharedEngine().playEffect(
				CCDirector.sharedDirector().getActivity(),
				R.raw.movementspeedpickup);

	}

}
