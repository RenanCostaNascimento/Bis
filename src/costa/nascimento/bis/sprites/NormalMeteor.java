package costa.nascimento.bis.sprites;

import org.cocos2d.nodes.CCSpriteFrameCache;

import costa.nascimento.bis.constants.Constants;

public class NormalMeteor extends Meteor {

	// meteoros normais
	private static final int NORMAL_POINTS_WORTH = 1;
	private static final int NORMAL_FLIGHT_SPEED = 1;
	private static final int NORMAL_SPRITE_QUANTITY = 30;
	private static final float NORMAL_ANIMATION_SPEED = 3.5f;

	public NormalMeteor() {
		super(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName(
				Constants.METEOR_1));

		inicializeNormalMeteor();
		animateMeteor("meteor", NORMAL_SPRITE_QUANTITY, NORMAL_ANIMATION_SPEED);

	}

	/**
	 * Inicializa as variáveis com dados dos meteoros especiais
	 */
	private void inicializeNormalMeteor() {
		this.pointsWorth = NORMAL_POINTS_WORTH;
		this.flightSpeed = NORMAL_FLIGHT_SPEED;
	}

}
