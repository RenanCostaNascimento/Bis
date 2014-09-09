package costa.nascimento.bis.sprites;

import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.sound.SoundEngine;

import costa.nascimento.bis.R;
import costa.nascimento.bis.constants.Constants;

public class AttackSpeedUpgrade extends Upgrade {

	private static final float MIN_ATTACK_SPEED = 0.5f;
	private static final float ATTACK_SPEED_VARIATION = 0.25f;
	private static final int SPRITE_QUANTITY = 8;

	public AttackSpeedUpgrade() {
		super(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName(
				Constants.ATTACK_SPEED_1));

		animateUpgrade("attackSpeed", SPRITE_QUANTITY, ANIMATION_SPEED);
	}

	@Override
	public void addEffect(Player player) {
		if (player.getCurrentAttackSpeed() > MIN_ATTACK_SPEED) {
			player.setCurrentAttackSpeed(player.getCurrentAttackSpeed()
					- ATTACK_SPEED_VARIATION);
		}

	}

	@Override
	protected void playUpgradeSoundEffect() {
		SoundEngine.sharedEngine().playEffect(
				CCDirector.sharedDirector().getActivity(),
				R.raw.attackspeedpickup);

	}

}
