package costa.nascimento.bis.scene;

import static costa.nascimento.bis.settings.DeviceSettings.screenHeight;
import static costa.nascimento.bis.settings.DeviceSettings.screenResolution;
import static costa.nascimento.bis.settings.DeviceSettings.screenWidth;

import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.ccColor4B;

import costa.nascimento.bis.constants.Constants;
import costa.nascimento.bis.layers.Button;
import costa.nascimento.bis.layers.ButtonDelegate;
import costa.nascimento.bis.util.PauseDelegate;

public class PauseScreen extends CCLayer implements ButtonDelegate {
	private Button resumeButton;
	private Button quitButton;
	private PauseDelegate pauseDelegate;
	private CCColorLayer background;

	public PauseScreen() {
		// habilita o toque na tela
		this.setIsTouchEnabled(true);

		// adiciona um background preto (0,0,0) com transparência 175
		this.background = CCColorLayer.node(ccColor4B.ccc4(0, 0, 0, 175),
				screenWidth(), screenHeight());
		this.addChild(this.background);

		// logo
		CCSprite title = CCSprite.sprite(Constants.LOGO);
		title.setPosition(screenResolution(CGPoint.ccp(screenWidth() / 2,
				screenHeight() - 130)));
		this.addChild(title);

		// Adiciona botoes
		this.resumeButton = new Button(Constants.PLAY);
		this.quitButton = new Button(Constants.EXIT);

		this.resumeButton.setDelegate(this);
		this.quitButton.setDelegate(this);

		this.addChild(this.resumeButton);
		this.addChild(this.quitButton);

		// Posiciona botoes
		this.resumeButton.setPosition(screenResolution(CGPoint.ccp(
				screenWidth() / 2, screenHeight() - 250)));
		this.quitButton.setPosition(screenResolution(CGPoint.ccp(
				screenWidth() / 2, screenHeight() - 300)));
	}

	@Override
	public void buttonClicked(Button sender) {
		// Verifica se o botão foi pressionado
		if (sender == this.resumeButton) {
			this.pauseDelegate.resumeGame();
			this.removeFromParentAndCleanup(true);
		}
		// Verifica se o botao foi pressionado
		if (sender == this.quitButton) {
			this.pauseDelegate.quitGame();
		}

	}

	public void setDelegate(PauseDelegate delegate) {
		this.pauseDelegate = delegate;
	}
}
