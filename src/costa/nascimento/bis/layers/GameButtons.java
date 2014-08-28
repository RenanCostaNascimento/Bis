package costa.nascimento.bis.layers;

import static costa.nascimento.bis.settings.DeviceSettings.screenResolution;
import static costa.nascimento.bis.settings.DeviceSettings.screenWidth;
import static costa.nascimento.bis.settings.DeviceSettings.screenHeight;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.types.CGPoint;

import costa.nascimento.bis.constants.Constants;
import costa.nascimento.bis.scene.GameScreen;

public class GameButtons extends CCLayer implements ButtonObserver {
	private Button leftControl;
	private Button rightControl;
	private Button shootButton;
	private Button pauseButton;
	private GameScreen delegate;

	public static GameButtons gameButtons() {
		return new GameButtons();
	}

	public GameButtons() {
		// Habilita o toque na tela
		this.setIsTouchEnabled(true);

		// Cria os botões
		this.leftControl = new Button(Constants.LEFTBUTTON);
		this.rightControl = new Button(Constants.RIGHTBUTTON);
		this.pauseButton = new Button(Constants.PAUSE);
		this.shootButton = new Button(Constants.SHOOTBUTTON);

		// Configura as delegações
		this.leftControl.setDelegate(this);
		this.rightControl.setDelegate(this);
		this.pauseButton.setDelegate(this);
		this.shootButton.setDelegate(this);

		// Configura posições
		setButtonspPosition();

		// Adiciona os botões na tela
		addChild(leftControl);
		addChild(rightControl);
		addChild(pauseButton);
		addChild(shootButton);
	}

	@Override
	public void buttonClicked(Button sender) {

		if (sender.equals(this.leftControl)) {
			this.delegate.startMovingLeft();
//			System.out.println("left");
		}
		if (sender.equals(this.rightControl)) {
			this.delegate.startMovingRight();
//			System.out.println("right");
		}
		if (sender.equals(this.shootButton)) {
			this.delegate.shoot();
//			System.out.println("shoot");
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
}
