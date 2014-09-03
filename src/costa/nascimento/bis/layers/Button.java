package costa.nascimento.bis.layers;

import org.cocos2d.events.CCTouchDispatcher;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

import android.view.MotionEvent;

public class Button extends CCLayer {

	protected CCSprite buttonImage;
	private ButtonObserver delegate;
	private int priority;

	public Button(String buttonImage, int priority) {
		this.setIsTouchEnabled(true);
		this.priority = priority;
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
		CCTouchDispatcher.sharedDispatcher().addTargetedDelegate(this,
				priority, false);
	}

	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		// pega a localização de um ponto na tela.
		CGPoint touchLocation = CGPoint.make(
				event.getX(event.getPointerCount() - 1),
				event.getY(event.getPointerCount() - 1));
		touchLocation = CCDirector.sharedDirector().convertToGL(touchLocation);
		touchLocation = this.convertToNodeSpace(touchLocation);

		// Verifica toque no botão
		if (CGRect.containsPoint(this.buttonImage.getBoundingBox(),
				touchLocation)) {
			delegate.buttonClicked(this);
		}

		return true;

	}

	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		// pega a localização de um ponto na tela.
		CGPoint touchLocation = CGPoint.make(
				event.getX(event.getPointerCount() - 1),
				event.getY(event.getPointerCount() - 1));
		touchLocation = CCDirector.sharedDirector()
				.convertToGL(touchLocation);
		touchLocation = this.convertToNodeSpace(touchLocation);

		// Verifica toque no botão
		if (CGRect.containsPoint(this.buttonImage.getBoundingBox(),
				touchLocation)) {
			delegate.buttonUnclicked(this);
		}

		return true;

	}
	
	@Override
	public boolean ccTouchesMoved(MotionEvent event) {
		// pega a localização de um ponto na tela.
		CGPoint touchLocation = CGPoint.make(
				event.getX(event.getPointerCount() - 1),
				event.getY(event.getPointerCount() - 1));
		touchLocation = CCDirector.sharedDirector()
				.convertToGL(touchLocation);
		touchLocation = this.convertToNodeSpace(touchLocation);

		// Verifica toque no botão
		if (!CGRect.containsPoint(this.buttonImage.getBoundingBox(),
				touchLocation)) {
			delegate.buttonMoved(this);
		}

		return true;

	}
}
