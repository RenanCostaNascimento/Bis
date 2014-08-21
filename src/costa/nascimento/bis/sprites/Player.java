package costa.nascimento.bis.sprites;

import static costa.nascimento.bis.settings.DeviceSettings.screenWidth;

import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCScaleBy;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.actions.interval.CCSpawn;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.sound.SoundEngine;

import costa.nascimento.bis.R;
import costa.nascimento.bis.assets.Assets;
import costa.nascimento.bis.layers.ShootEngineDelegate;

public class Player extends CCSprite {
	float positionX = screenWidth() / 2;
	float positionY = 100;
	ShootEngineDelegate delegate;

	public Player() {
		super(Assets.NAVE);
		setPosition(positionX, positionY);
	}

	public void setDelegate(ShootEngineDelegate delegate) {
		this.delegate = delegate;
	}

	/**
	 * Faz o jogador atirar.
	 */
	public void shoot() {
		delegate.createShoot(new Shoot(positionX, positionY));

	}

	/**
	 * Faz o jogador se movimentar para a esquerda.
	 */
	public void moveLeft() {
		if (positionX > 30) {
			positionX -= 10;
		}
		setPosition(positionX, positionY);
	}

	/**
	 * Faz o jogador se movimentar para a direita.
	 */
	public void moveRight() {
		if (positionX < screenWidth() - 30) {
			positionX += 10;
		}
		setPosition(positionX, positionY);
	}

	/**
	 * Faz todos os ajustes necessários para que o player seja destruído: remove
	 * do array em GameScene, cria efeitos de destruição e remove da memória.
	 */
	public void explode() {
		// cria o som de explosão
		SoundEngine.sharedEngine().playEffect(
				CCDirector.sharedDirector().getActivity(), R.raw.over);
		
		// para a música do jogo
		SoundEngine.sharedEngine().pauseSound();

		// Cria efeitos
		float dt = 0.2f;
		CCScaleBy a1 = CCScaleBy.action(dt, 2f);
		CCFadeOut a2 = CCFadeOut.action(dt);
		CCSpawn s1 = CCSpawn.actions(a1, a2);

		// Roda os efeitos
		this.runAction(CCSequence.actions(s1));
	}

}
