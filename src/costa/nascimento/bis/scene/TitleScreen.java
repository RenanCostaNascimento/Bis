package costa.nascimento.bis.scene;

import static costa.nascimento.bis.settings.DeviceSettings.screenHeight;
import static costa.nascimento.bis.settings.DeviceSettings.screenResolution;
import static costa.nascimento.bis.settings.DeviceSettings.screenWidth;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;

import costa.nascimento.bis.assets.Assets;
import costa.nascimento.bis.layers.MenuButtons;
import costa.nascimento.bis.sprites.ScreenBackground;

public class TitleScreen extends CCLayer {

	private ScreenBackground background;

	public TitleScreen() {
		addBackground();
		addLogo();
		addButtons();
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
		this.background = new ScreenBackground(Assets.BACKGROUND);
		this.background.setPosition(screenResolution(CGPoint.ccp(
				screenWidth() / 2.0f, screenHeight() / 2.0f)));
		this.addChild(this.background);
	}

	/**
	 * Adiciona a logo na tela inicial.
	 */
	private void addLogo() {
		CCSprite logo = CCSprite.sprite(Assets.LOGO);
		logo.setPosition(screenResolution(CGPoint.ccp(screenWidth() / 2,
				screenHeight() - 130)));
		this.addChild(logo);
	}

	/**
	 * Adiciona os botões de PLAY, HIGHSCORE, HELP e SOUND na tela inicial.
	 */
	private void addButtons() {
		MenuButtons menuLayer = new MenuButtons();
		this.addChild(menuLayer);
	}

}
