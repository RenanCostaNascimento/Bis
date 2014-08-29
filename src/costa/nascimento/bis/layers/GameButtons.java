package costa.nascimento.bis.layers;

import static costa.nascimento.bis.settings.DeviceSettings.screenHeight;
import static costa.nascimento.bis.settings.DeviceSettings.screenResolution;
import static costa.nascimento.bis.settings.DeviceSettings.screenWidth;

import org.cocos2d.types.CGPoint;

import costa.nascimento.bis.constants.Constants;
import costa.nascimento.bis.scene.GameScreen;

public class GameButtons implements ButtonObserver {
	private Button leftControl;
	private Button rightControl;
	private Button shootButton;
	private Button pauseButton;
	private GameScreen delegate;

	public GameButtons() {

		// Cria os botões
		this.shootButton = new Button(Constants.SHOOTBUTTON, 0);
		this.leftControl = new Button(Constants.LEFTBUTTON, 1);
		this.rightControl = new Button(Constants.RIGHTBUTTON, 1);
		this.pauseButton = new Button(Constants.PAUSE, 2);

		// Configura as delegações
		this.shootButton.setDelegate(this);
		this.leftControl.setDelegate(this);
		this.rightControl.setDelegate(this);
		this.pauseButton.setDelegate(this);

		// Configura posições
		setButtonspPosition();
	}

	@Override
	public void buttonClicked(Button sender) {

		if (sender.equals(this.leftControl)) {
			this.delegate.startMovingLeft();
		}
		if (sender.equals(this.rightControl)) {
			this.delegate.startMovingRight();
		}
		if (sender.equals(this.shootButton)) {
			this.delegate.shoot();
		}
		if (sender.equals(this.pauseButton)) {
			this.delegate.pauseGameAndShowLayer();
		}

	}

	@Override
	public void buttonUnclicked(Button sender) {

		if (sender.equals(this.leftControl)) {
			this.delegate.stopMovingLeft();
		}
		if (sender.equals(this.rightControl)) {
			this.delegate.stopMovingRight();
		}
	}

	private void setButtonspPosition() {
		// Posição dos botões
		leftControl.setPosition(screenResolution(CGPoint.ccp(40, 40)));
		rightControl.setPosition(screenResolution(CGPoint.ccp(100, 40)));
		pauseButton.setPosition(screenResolution(CGPoint.ccp(40,
				screenHeight() - 30)));
		shootButton.setPosition(screenResolution(CGPoint.ccp(
				screenWidth() - 40, 40)));
	}

	public void setDelegate(GameScreen gameScene) {
		this.delegate = gameScene;
	}

	public Button getLeftControl() {
		return leftControl;
	}

	public Button getRightControl() {
		return rightControl;
	}

	public Button getShootButton() {
		return shootButton;
	}

	public Button getPauseButton() {
		return pauseButton;
	}

}
