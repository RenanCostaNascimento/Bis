package costa.nascimento.bis.layers;

import static costa.nascimento.bis.settings.DeviceSettings.screenHeight;
import static costa.nascimento.bis.settings.DeviceSettings.screenResolution;
import static costa.nascimento.bis.settings.DeviceSettings.screenWidth;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.transitions.CCFadeTransition;
import org.cocos2d.types.CGPoint;

import costa.nascimento.bis.assets.Assets;
import costa.nascimento.bis.scene.GameScreen;

public class MenuButtons extends CCLayer implements ButtonDelegate {
	private Button playButton;
	private Button highscoredButton;
	private Button helpButton;
	private Button soundButton;

	public MenuButtons() {
		this.setIsTouchEnabled(true);

		this.playButton = new Button(Assets.PLAY);
		this.highscoredButton = new Button(Assets.HIGHSCORE);
		this.helpButton = new Button(Assets.HELP);
		this.soundButton = new Button(Assets.SOUND);

		// coloca botões na posição correta
		setButtonsPosition();

		addChild(playButton);
		addChild(highscoredButton);
		addChild(helpButton);
		addChild(soundButton);

		// registra os observadores
		this.playButton.setDelegate(this);
		this.highscoredButton.setDelegate(this);
		this.helpButton.setDelegate(this);
		this.soundButton.setDelegate(this);
	}

	private void setButtonsPosition() {
		// Buttons Positions
		playButton.setPosition(screenResolution(CGPoint.ccp(screenWidth() / 2,
				screenHeight() - 250)));

		highscoredButton.setPosition(screenResolution(CGPoint.ccp(
				screenWidth() / 2, screenHeight() - 300)));

		helpButton.setPosition(screenResolution(CGPoint.ccp(screenWidth() / 2,
				screenHeight() - 350)));

		soundButton.setPosition(screenResolution(CGPoint.ccp(
				screenWidth() / 2 - 100, screenHeight() - 420)));
	}

	@Override
	public void buttonClicked(Button sender) {
		if (sender.equals(this.playButton)) {
			CCDirector.sharedDirector().replaceScene(
					CCFadeTransition.transition(1.0f, GameScreen.createGame()));
		}
		if (sender.equals(this.highscoredButton)) {
			System.out.println("Button clicked: Highscore");
		}
		if (sender.equals(this.helpButton)) {
			System.out.println("Button clicked: Help");
		}
		if (sender.equals(this.soundButton)) {
			System.out.println("Button clicked: Sound");
		}
	}

}
