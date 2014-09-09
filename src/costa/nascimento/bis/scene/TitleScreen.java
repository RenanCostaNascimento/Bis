package costa.nascimento.bis.scene;

import static costa.nascimento.bis.settings.DeviceSettings.screenHeight;
import static costa.nascimento.bis.settings.DeviceSettings.screenResolution;
import static costa.nascimento.bis.settings.DeviceSettings.screenWidth;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;

import costa.nascimento.bis.R;
import costa.nascimento.bis.constants.Constants;
import costa.nascimento.bis.layers.MenuButtons;
import costa.nascimento.bis.sprites.ScreenBackground;

public class TitleScreen extends CCScene {

	private ScreenBackground background;

	public TitleScreen() {
		addBackground();
		addLogo();
		addButtons();

		SoundEngine.sharedEngine().realesAllSounds();
		SoundEngine.sharedEngine().playSound(
				CCDirector.sharedDirector().getActivity(), R.raw.titlescreen, true);
	}

	/**
	 * Cria uma Scene da tela inicial.
	 * 
	 * @return A Scene criada.
	 */
	public CCScene scene() {
		CCScene scene = CCScene.node();
		scene.addChild(this);
		return scene;
	}

	/**
	 * Adiciona o backgroung na tela inicial.
	 */
	private void addBackground() {
		this.background = new ScreenBackground(Constants.BACKGROUND);
		this.background.setPosition(screenResolution(CGPoint.ccp(
				screenWidth() / 2.0f, screenHeight() / 2.0f)));
		this.addChild(this.background);
	}

	/**
	 * Adiciona a logo na tela inicial.
	 */
	private void addLogo() {
		CCSprite logo = CCSprite.sprite(Constants.LOGO);
		logo.setPosition(screenResolution(CGPoint.ccp(screenWidth() / 2,
				screenHeight() - 130)));
		this.addChild(logo);
	}

	/**
	 * Adiciona os botões de PLAY, HIGHSCORE, HELP e SOUND na tela inicial.
	 */
	private void addButtons() {
		MenuButtons menuLayer = new MenuButtons();
		this.addChild(menuLayer.getPlayButton());
		// this.addChild(menuLayer.getHighscoredButton());
		// this.addChild(menuLayer.getHelpButton());
	}

}
