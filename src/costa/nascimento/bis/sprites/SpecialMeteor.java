package costa.nascimento.bis.sprites;

import org.cocos2d.nodes.CCSpriteFrameCache;

import costa.nascimento.bis.constants.Constants;

public class SpecialMeteor extends Meteor {

	// meteoros especiais
	private static final int ESPECIAL_POINTS_WORTH = 2;
	private static final int ESPECIAL_FLIGHT_SPEED = 2;
	private static final int ESPECIAL_SPRITE_QUANTITY = 8;
	private static final float ESPECIAL_ANIMATION_SPEED = 1.225f;

	public SpecialMeteor() {
		super(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName(
				Constants.ESPECIAL_METEOR_1));

		inicializeEspecialMeteor();
		animateMeteor("especialMeteor", ESPECIAL_SPRITE_QUANTITY,
				ESPECIAL_ANIMATION_SPEED);

	}

	/**
	 * Inicializa as variáveis com dados dos meteoros especiais
	 */
	private void inicializeEspecialMeteor() {
		this.pointsWorth = ESPECIAL_POINTS_WORTH;
		this.flightSpeed = ESPECIAL_FLIGHT_SPEED;
	}

}
