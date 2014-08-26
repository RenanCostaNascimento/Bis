package costa.nascimento.bis.layers;

import org.cocos2d.events.CCTouchDispatcher;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

import android.view.MotionEvent;

public class Button extends CCLayer {

	private CCSprite buttonImage;
	private ButtonObserver delegate;

	public Button(String buttonImage) {
		this.setIsTouchEnabled(true);
		this.buttonImage = CCSprite.sprite(buttonImage);
		addChild(this.buttonImage);
	}

	public void setDelegate(ButtonObserver sender) {
		this.delegate = sender;
	}

	/**
	 * Indica quem será chamado ao receber o toque na tela. Nesse caso, o
	 * próprio botão irá responder.
	 */
	@Override
	protected void registerWithTouchDispatcher() {
		CCTouchDispatcher.sharedDispatcher()
				.addTargetedDelegate(this, 0, false);
	}

	/**
	 * Método responsável por responder ao toque na tela.
	 */
	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		CGPoint touchLocation = CGPoint.make(event.getX(), event.getY());
		touchLocation = CCDirector.sharedDirector().convertToGL(touchLocation);
		touchLocation = this.convertToNodeSpace(touchLocation);

		// Verifica toque no botão
		if (CGRect.containsPoint(this.buttonImage.getBoundingBox(),
				touchLocation)) {
			delegate.buttonClicked(this);
		}

		return true;
	}

}
