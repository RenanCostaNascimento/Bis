package costa.nascimento.bis.layers;

import java.util.Random;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCSpriteFrameCache;

import costa.nascimento.bis.constants.Constants;
import costa.nascimento.bis.sprites.NormalMeteor;
import costa.nascimento.bis.sprites.SpecialMeteor;

/**
 * Classe respons�vel por criar os meteoros.
 * 
 * @author Renan
 * 
 */
public class MeteorsEngine extends CCLayer {
	private MeteorsEngineObserver observer;

	private static final int NORMAL_METEOR_SPAWN_CHANCE = 25;
	private static final int ESPECIAL_METEOR_SPAWN_CHANCE = 3;

	public MeteorsEngine() {
		this.schedule("meteorsEngine", 1.0f / 10f);

		// preenche o cache de frames
		CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames(
				Constants.METEOR_SHEET);
		CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames(
				Constants.ESPECIAL_METEOR_SHEET);
	}

	/**
	 * Chamado pelo Schedule, ele cria um novo meteoro com uma chaance de 1/30.
	 * 
	 * @param dt
	 *            n�o tenho certeza de pra que serve =/
	 */
	public void meteorsEngine(float dt) {
		// sorte: 1 em 25 gera um novo meteoro!
		if (new Random().nextInt(NORMAL_METEOR_SPAWN_CHANCE) == 0) {
			// mais sorte ainda, h� chance de criar um meteoro especial
			if (new Random().nextInt(ESPECIAL_METEOR_SPAWN_CHANCE) == 0) {
				this.getDelegate().createMeteor(new SpecialMeteor());
			} else {
				this.getDelegate().createMeteor(new NormalMeteor());
			}
		}
	}

	public void setDelegate(MeteorsEngineObserver delegate) {
		this.observer = delegate;
	}

	public MeteorsEngineObserver getDelegate() {
		return observer;
	}

}
