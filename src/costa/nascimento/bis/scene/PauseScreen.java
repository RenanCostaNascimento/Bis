package costa.nascimento.bis.scene;

import static costa.nascimento.bis.settings.DeviceSettings.screenHeight;
import static costa.nascimento.bis.settings.DeviceSettings.screenResolution;
import static costa.nascimento.bis.settings.DeviceSettings.screenWidth;

import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.ccColor4B;

import costa.nascimento.bis.R;
import costa.nascimento.bis.constants.Constants;
import costa.nascimento.bis.layers.Button;
import costa.nascimento.bis.layers.ButtonObserver;
import costa.nascimento.bis.util.PauseObserver;

public class PauseScreen extends CCScene implements ButtonObserver {
	private Button resumeButton;
	private Button quitButton;
	private Button soundButton;
	private PauseObserver pauseObserver;
	private CCColorLayer background;

	public PauseScreen() {

		// adiciona um background preto (0,0,0) com transparência 175
		this.background = CCColorLayer.node(ccColor4B.ccc4(0, 0, 0, 175),
				screenWidth(), screenHeight());
		this.addChild(this.background);

		// logo
		// CCSprite title = CCSprite.sprite(Constants.LOGO);
		// title.setPosition(screenResolution(CGPoint.ccp(screenWidth() / 2,
		// screenHeight() - 130)));
		// this.addChild(title);

		// Adiciona botoes
		this.resumeButton = new Button(Constants.PLAY, 1);
		this.quitButton = new Button(Constants.EXIT, 1);
		this.soundButton = new Button(Constants.SOUND, 1);

		this.resumeButton.setDelegate(this);
		this.quitButton.setDelegate(this);
		this.soundButton.setDelegate(this);

		this.addChild(this.resumeButton);
		this.addChild(this.quitButton);
		this.addChild(this.soundButton);

		// Posiciona botoes
		this.resumeButton.setPosition(screenResolution(CGPoint.ccp(
				screenWidth() / 2, screenHeight() - 250)));
		this.quitButton.setPosition(screenResolution(CGPoint.ccp(
				screenWidth() / 2, screenHeight() - 300)));
		this.soundButton.setPosition(screenResolution(CGPoint.ccp(
				screenWidth() / 2, screenHeight() - 350)));
	}

	@Override
	public void buttonClicked(Button sender) {
		// Verifica se o botão foi pressionado
		if (sender == this.resumeButton) {
			this.pauseObserver.resumeGame();
			this.removeFromParentAndCleanup(true);
		}
		// Verifica se o botao foi pressionado
		if (sender == this.quitButton) {
			SoundEngine.sharedEngine().playEffect(
					CCDirector.sharedDirector().getActivity(),
					R.raw.menuselected);
			this.pauseObserver.quitGame();
		}

		// Verifica se o botao foi pressionado
		if (sender == this.soundButton) {
			this.pauseObserver.muteUnmute();
		}

	}

	public void setDelegate(PauseObserver delegate) {
		this.pauseObserver = delegate;
	}

	@Override
	public void buttonUnclicked(Button sender) {
		// TODO Auto-generated method stub

	}

	@Override
	public void buttonMoved(Button sender) {
		// TODO Auto-generated method stub

	}
}
