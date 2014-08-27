package costa.nascimento.bis.sprites;

import static costa.nascimento.bis.settings.DeviceSettings.screenHeight;
import static costa.nascimento.bis.settings.DeviceSettings.screenResolution;
import static costa.nascimento.bis.settings.DeviceSettings.screenWidth;

import java.util.Random;

import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCScaleBy;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.actions.interval.CCSpawn;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;

import costa.nascimento.bis.R;
import costa.nascimento.bis.constants.Constants;
import costa.nascimento.bis.layers.MeteorsEngineObserver;
import costa.nascimento.bis.util.Runner;

public class Meteor extends CCSprite {
	private float positionX, positionY;
	private int pointsWorth, flightSpeed;

	private static final int NORMAL_POINTS_WORTH = 1;
	private static final int NORMAL_FLIGHT_SPEED = 1;
	private static final int ESPECIAL_POINTS_WORTH = 3;
	private static final int ESPECIAL_FLIGHT_SPEED = 2;

	private MeteorsEngineObserver observer;

	public Meteor(String image) {
		super(image);
		positionX = new Random().nextInt(Math.round(screenWidth()) - 30) + 30;
		positionY = screenHeight();
		if(image.equals(Constants.METEOR)){
			this.pointsWorth = NORMAL_POINTS_WORTH;
			this.flightSpeed = NORMAL_FLIGHT_SPEED;
		}else{
			this.pointsWorth = ESPECIAL_POINTS_WORTH;
			this.flightSpeed = ESPECIAL_FLIGHT_SPEED;
		}
		
	}

	/**
	 * Chamado a cada frame, o m�todo start invoca o m�todo update.
	 */
	public void start() {
		this.schedule("update");
	}

	/**
	 * Diminui a posi��o do meteoro, se o jogo n�o estiver pausado.
	 * 
	 * @param dt
	 */
	public void update(float dt) {
		Runner.check();
		if (!Runner.isGamePaused()) {
			positionY -= this.flightSpeed;
			if (positionY > 0) {
				this.setPosition(screenResolution(CGPoint.ccp(positionX,
						positionY)));
			} else {
				// m�todo chamado quando o jogador n�o consegue abater um
				// meteoro.
				meteorEscaped();
			}
		}
	}

	/**
	 * Remove o meteoro do array de meteoros do observador e diminiu a pontua��o
	 * do jogador.
	 */
	private void meteorEscaped() {
		this.observer.meteorEscaped(this);
		this.unschedule("update");
		removeMe();
	}

	public void setDelegate(MeteorsEngineObserver delegate) {
		this.observer = delegate;
	}

	/**
	 * Faz todos os ajustes necess�rios para que o meteoro seja destru�do:
	 * remove do array em GameScene, cria efeitos de destrui��o e remove da
	 * mem�ria.
	 */
	public void shooted() {
		this.observer.removeMeteor(this);

		// para de ficar chamando o update
		this.unschedule("update");

		// som de explos�o
		SoundEngine.sharedEngine().playEffect(
				CCDirector.sharedDirector().getActivity(), R.raw.bang);

		// As a��es abaixo, juntas, far�o o efeito de destrui��o do meteoro.
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
	 * Remove o meteoro de mem�ria. Esse m�todo deve ser p�blico, caso contr�rio
	 * ele n�o ser� chamado pelo runAction.
	 */
	public void removeMe() {
		// Esse m�todo remove e coleta um objeto de mem�ria.
		this.removeFromParentAndCleanup(true);
	}

	public int getPointsWorth() {
		return pointsWorth;
	}
	
}
