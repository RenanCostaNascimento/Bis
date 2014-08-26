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
import costa.nascimento.bis.layers.MeteorsEngineDelegate;
import costa.nascimento.bis.util.Runner;

public class Meteor extends CCSprite {
	private float positionX, positionY;
	private MeteorsEngineDelegate delegate;

	public Meteor(String image) {
		super(image);
		positionX = new Random().nextInt(Math.round(screenWidth()));
		positionY = screenHeight();
	}

	/**
	 * Chamado a cada frame, o método start invoca o método update.
	 */
	public void start() {
		this.schedule("update");
	}

	/**
	 * Diminui a posição do meteoro.
	 * 
	 * @param dt
	 */
	public void update(float dt) {
		if (Runner.check().isGamePlaying() && !Runner.check().isGamePaused()) {
			positionY -= 1;
			this.setPosition(screenResolution(CGPoint.ccp(positionX, positionY)));
		}
	}

	public void setDelegate(MeteorsEngineDelegate delegate) {
		this.delegate = delegate;
	}

	/**
	 * Faz todos os ajustes necessários para que o meteoro seja destruído:
	 * remove do array em GameScene, cria efeitos de destruição e remove da
	 * memória.
	 */
	public void shooted() {
		this.delegate.removeMeteor(this);

		// para de ficar chamando o update
		this.unschedule("update");

		// som de explosão
		SoundEngine.sharedEngine().playEffect(
				CCDirector.sharedDirector().getActivity(), R.raw.bang);

		// As ações abaixo, juntas, farão o efeito de destruição do meteoro.
		float dt = 0.2f;
		// Diminui a imagem...
		CCScaleBy a1 = CCScaleBy.action(dt, 0.5f);
		// Faz a imagem desaparecer devagar...
		CCFadeOut a2 = CCFadeOut.action(dt);
		// Combina as ações...
		CCSpawn s1 = CCSpawn.actions(a1, a2);

		// Indica o que deve ocorrer depois que a animação terminar...
		CCCallFunc c1 = CCCallFunc.action(this, "removeMe");

		// Executa as ações
		this.runAction(CCSequence.actions(s1, c1));
	}

	/**
	 * Remove o meteoro de memória. Esse método deve ser público, caso contrário
	 * ele não será chamado pelo runAction.
	 */
	public void removeMe() {
		// Esse método remove e coleta um objeto de memória.
		this.removeFromParentAndCleanup(true);
	}

}
