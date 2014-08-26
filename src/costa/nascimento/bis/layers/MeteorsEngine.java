package costa.nascimento.bis.layers;

import java.util.Random;

import org.cocos2d.layers.CCLayer;

import costa.nascimento.bis.constants.Constants;
import costa.nascimento.bis.sprites.Meteor;
import costa.nascimento.bis.util.Runner;

public class MeteorsEngine extends CCLayer {
	private MeteorsEngineDelegate delegate;

	public MeteorsEngine() {
		this.schedule("meteorsEngine", 1.0f / 10f);
	}

	/**
	 * Chamado pelo Schedule, ele cria um novo meteoro com uma chaance de 1/30.
	 * 
	 * @param dt
	 *            não tenho certeza de pra que serve =/
	 */
	public void meteorsEngine(float dt) {
		// só cria meteoros se o jogo não tiver pausado
		if (Runner.check().isGamePlaying() && !Runner.check().isGamePaused()) {
			// sorte: 1 em 30 gera um novo meteoro!
			if (new Random().nextInt(30) == 0) {
				this.getDelegate().createMeteor(new Meteor(Constants.METEOR), 0,
						0, 0, 0, 0);
			}
		}

	}

	public void setDelegate(MeteorsEngineDelegate delegate) {
		this.delegate = delegate;
	}

	public MeteorsEngineDelegate getDelegate() {
		return delegate;
	}

}
