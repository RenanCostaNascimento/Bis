package costa.nascimento.bis.layers;

import static costa.nascimento.bis.settings.DeviceSettings.screenHeight;
import static costa.nascimento.bis.settings.DeviceSettings.screenWidth;

import org.cocos2d.nodes.CCDirector;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;

import costa.nascimento.bis.R;
import costa.nascimento.bis.constants.Constants;
import costa.nascimento.bis.scene.GameScreen;

public class GameButtons implements ButtonObserver {
	private Button pauseButton;
	private Button moveLeftButton;
	private Button moveRightButton;
	private GameScreen delegate;

	public GameButtons() {

		// Cria os botões
		this.pauseButton = new Button(Constants.PAUSE, 2);
		this.moveLeftButton = new Button(Constants.ACTION, 2);
		this.moveRightButton = new Button(Constants.ACTION, 2);

		// Configura as delegações
		this.pauseButton.setDelegate(this);
		this.moveLeftButton.setDelegate(this);
		this.moveRightButton.setDelegate(this);

		// Configura posições
		setButtonsPosition();
	}

	@Override
	public void buttonClicked(Button sender) {
		if (sender.equals(this.pauseButton)) {
			SoundEngine.sharedEngine().playEffect(
					CCDirector.sharedDirector().getActivity(), R.raw.pause);
			this.delegate.pauseGameAndShowLayer();
		}
		if (sender.equals(this.moveLeftButton)) {
			this.delegate.startMovingLeft();
		}
		if (sender.equals(this.moveRightButton)) {
			this.delegate.startMovingRight();
		}
	}

	@Override
	public void buttonUnclicked(Button sender) {
		if (sender.equals(this.moveLeftButton)) {
			this.delegate.stopMovingLeft();
		}
		if (sender.equals(this.moveRightButton)) {
			this.delegate.stopMovingRight();
		}
	}

	@Override
	public void buttonMoved(Button sender) {
		if (sender.equals(this.moveLeftButton)) {
			this.delegate.stopMovingLeft();
		}
		if (sender.equals(this.moveRightButton)) {
			this.delegate.stopMovingRight();
		}
	}

	private void setButtonsPosition() {
		// Posição dos botões
		pauseButton.setPosition(screenWidth() - 30, screenHeight() - 30);
		moveLeftButton.setPosition(CGPoint.ccp(screenWidth() / 4,
				screenHeight() / 4));
		moveRightButton.setPosition(CGPoint.ccp(3 * screenWidth() / 4,
				screenHeight() / 4));
	}

	public void setDelegate(GameScreen gameScene) {
		this.delegate = gameScene;
	}

	public Button getPauseButton() {
		return pauseButton;
	}

	public Button getMoveLeftButton() {
		return moveLeftButton;
	}

	public Button getMoveRightButton() {
		return moveRightButton;
	}
}
