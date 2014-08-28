package costa.nascimento.bis.scene;

import static costa.nascimento.bis.settings.DeviceSettings.screenHeight;
import static costa.nascimento.bis.settings.DeviceSettings.screenResolution;
import static costa.nascimento.bis.settings.DeviceSettings.screenWidth;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;

import costa.nascimento.bis.R;
import costa.nascimento.bis.constants.Constants;
import costa.nascimento.bis.layers.Button;
import costa.nascimento.bis.layers.ButtonObserver;
import costa.nascimento.bis.sprites.ScreenBackground;

public class FinalScreen extends CCLayer implements ButtonObserver {

	private ScreenBackground background;
	private Button beginButton;

	public CCScene scene() {
		CCScene scene = CCScene.node();
		scene.addChild(this);
		return scene;
	}

	public FinalScreen() {
		// background
		this.background = new ScreenBackground(Constants.BACKGROUND);
		this.background.setPosition(screenResolution(CGPoint.ccp(
				screenWidth() / 2.0f, screenHeight() / 2.0f)));
		this.addChild(this.background);

		// som
		SoundEngine.sharedEngine()
				.playSound(CCDirector.sharedDirector().getActivity(),
						R.raw.finalend, true);

		// imagem
		CCSprite title = CCSprite.sprite(Constants.FINALEND);
		title.setPosition(screenResolution(CGPoint.ccp(screenWidth() / 2,
				screenHeight() - 130)));
		this.addChild(title);

		this.setIsTouchEnabled(true);
		this.beginButton = new Button(Constants.PLAY);
		this.beginButton.setPosition(screenResolution(CGPoint.ccp(
				screenWidth() / 2, screenHeight() - 300)));
		this.beginButton.setDelegate(this);
		addChild(this.beginButton);
	}

	@Override
	public void buttonClicked(Button sender) {
		if (sender.equals(this.beginButton)) {
			SoundEngine.sharedEngine().pauseSound();
			CCDirector.sharedDirector().replaceScene(new TitleScreen().scene());
		}
	}

	@Override
	public void buttonUnclicked(Button sender) {
		// TODO Auto-generated method stub
		
	}
}
