package costa.nascimento.bis.layers;

import static costa.nascimento.bis.settings.DeviceSettings.screenHeight;
import static costa.nascimento.bis.settings.DeviceSettings.screenResolution;
import static costa.nascimento.bis.settings.DeviceSettings.screenWidth;

import org.cocos2d.nodes.CCDirector;
import org.cocos2d.transitions.CCFadeTransition;
import org.cocos2d.types.CGPoint;

import costa.nascimento.bis.constants.Constants;
import costa.nascimento.bis.scene.GameScreen;

public class MenuButtons implements ButtonObserver {
	private Button playButton;
	private Button highscoredButton;
	private Button helpButton;

	public MenuButtons() {

		this.playButton = new Button(Constants.PLAY, 1);
		this.highscoredButton = new Button(Constants.HIGHSCORE, 1);
		this.helpButton = new Button(Constants.HELP, 1);

		// coloca botões na posição correta
		setButtonsPosition();

		// registra os observadores
		this.playButton.setDelegate(this);
		this.highscoredButton.setDelegate(this);
		this.helpButton.setDelegate(this);
	}

	private void setButtonsPosition() {
		// Buttons Positions
		playButton.setPosition(screenResolution(CGPoint.ccp(screenWidth() / 2,
				screenHeight() - 250)));

		highscoredButton.setPosition(screenResolution(CGPoint.ccp(
				screenWidth() / 2, screenHeight() - 300)));

		helpButton.setPosition(screenResolution(CGPoint.ccp(screenWidth() / 2,
				screenHeight() - 350)));
	}

	@Override
	public void buttonClicked(Button sender) {
		if (sender.equals(this.playButton)) {
			CCDirector.sharedDirector().replaceScene(
					CCFadeTransition.transition(1.0f, GameScreen.createGame()));
		}		
	}

	@Override
	public void buttonUnclicked(Button sender) {
		// TODO Auto-generated method stub
	}

	public Button getPlayButton() {
		return playButton;
	}

	public Button getHighscoredButton() {
		return highscoredButton;
	}

	public Button getHelpButton() {
		return helpButton;
	}
}
