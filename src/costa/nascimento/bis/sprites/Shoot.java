package costa.nascimento.bis.sprites;

import static costa.nascimento.bis.settings.DeviceSettings.screenHeight;
import static costa.nascimento.bis.settings.DeviceSettings.screenResolution;

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
import costa.nascimento.bis.layers.ShootEngineObserver;

public class Shoot extends CCSprite {
	private ShootEngineObserver observer;
	float positionX, positionY;

	public Shoot(float positionX, float positionY) {
		super(Constants.SHOOT);
		this.positionX = positionX;
		this.positionY = positionY;
		setPosition(this.positionX, this.positionY);

		// de tempos em tempos executa o m�todo update()
		this.schedule("update");
	}

	/**
	 * Atualiza a posi��o do tiro se o game n�o estiver em pause.
	 * 
	 * @param dt
	 */
	public void update(float dt) {
		positionY += 2;
		if (positionY <= screenHeight()) {
			this.setPosition(screenResolution(CGPoint.ccp(positionX, positionY)));
		} else {
			// m�todo chamado quando o tiro n�o acerta nenhum meteoro
			missedShot();
		}
	}

	/**
	 * Remove o meteoro de mem�ria e do array em GameScreen
	 */
	private void missedShot() {
		this.observer.removeShoot(this);
		this.unschedule("update");
		removeMe();

	}

	public void setDelegate(ShootEngineObserver delegate) {
		this.observer = delegate;
	}

	/**
	 * Realiza o som do tiro sendo disparado.
	 */
	public void start() {
		SoundEngine.sharedEngine().playEffect(
				CCDirector.sharedDirector().getActivity(), R.raw.shoot);
	}

	/**
	 * Faz todos os ajustes necess�rios para que o tiro seja destru�do: remove
	 * do array em GameScene, cria efeitos de destrui��o e remove da mem�ria.
	 */
	public void explode() {
		// Remove do array
		this.observer.removeShoot(this);

		// Para o agendamento
		this.unschedule("update");

		// Cria efeitos
		float dt = 0.2f;
		CCScaleBy a1 = CCScaleBy.action(dt, 2f);
		CCFadeOut a2 = CCFadeOut.action(dt);
		CCSpawn s1 = CCSpawn.actions(a1, a2);

		// Fun��o a ser executada ap�s efeito
		CCCallFunc c1 = CCCallFunc.action(this, "removeMe");

		// Roda efeito
		this.runAction(CCSequence.actions(s1, c1));
	}

	/**
	 * Remove o tiro da mem�ria.
	 */
	public void removeMe() {
		// Esse m�todo remove e coleta um objeto de mem�ria.
		this.removeFromParentAndCleanup(true);
	}

}