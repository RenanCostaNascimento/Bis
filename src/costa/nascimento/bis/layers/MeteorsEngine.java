package costa.nascimento.bis.layers;

import java.util.Random;

import org.cocos2d.layers.CCLayer;

import costa.nascimento.bis.constants.Constants;
import costa.nascimento.bis.sprites.Meteor;
import costa.nascimento.bis.util.Runner;

/**
 * Classe responsável por criar os meteoros.
 * @author Renan
 *
 */
public class MeteorsEngine extends CCLayer {
	private MeteorsEngineObserver observer;
	
	private static final int NORMAL_METEOR_SPAWN_CHANCE = 25;
	private static final int ESPECIAL_METEOR_SPAWN_CHANCE = 3;

	public MeteorsEngine() {
//		this.schedule("meteorsEngine", 1.0f / 10f);
	}

	/**
	 * Chamado pelo Schedule, ele cria um novo meteoro com uma chaance de 1/30.
	 * 
	 * @param dt
	 *            não tenho certeza de pra que serve =/
	 */
	public void meteorsEngine(float dt) {
		// só cria meteoros se o jogo não tiver pausado
		Runner.check();
		if (!Runner.isGamePaused()) {
			// sorte: 1 em 25 gera um novo meteoro!
			if (new Random().nextInt(NORMAL_METEOR_SPAWN_CHANCE) == 0) {
				// mais sorte ainda, há chance de criar um meteoro especial
				if(new Random().nextInt(ESPECIAL_METEOR_SPAWN_CHANCE) == 0){
					this.getDelegate().createMeteor(new Meteor(Constants.ESPECIAL_METEOR));
				}else{
					this.getDelegate().createMeteor(new Meteor(Constants.METEOR));
				}
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
