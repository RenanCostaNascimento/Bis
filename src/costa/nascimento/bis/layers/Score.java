package costa.nascimento.bis.layers;

import static costa.nascimento.bis.settings.DeviceSettings.screenHeight;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.opengl.CCBitmapFontAtlas;
import org.cocos2d.types.ccColor3B;

import costa.nascimento.bis.constants.Constants;

public class Score extends CCLayer {
	private int score;
	// Variável utilizada para imprimir o valor de score na tela.
	private CCBitmapFontAtlas text;

	public Score() {
		this.score = 0;
		this.text = CCBitmapFontAtlas.bitmapFontAtlas(
				String.valueOf(this.score), Constants.FONT);
		this.text.setColor(ccColor3B.ccWHITE);
		this.text.setScale((float) 240 / 240);
		this.setPosition(40, screenHeight() - 40);
		this.addChild(this.text);
	}

	/**
	 * Aumenta placar do jogador.
	 */
	public void increase(int points) {
		score += points;
		changeColor();
		this.text.setString(String.valueOf(this.score));
	}

	/**
	 * Diminui o placar do jogador.
	 */
	public void decrease() {
		score--;
		changeColor();
		this.text.setString(String.valueOf(this.score));

	}

	/**
	 * Muda a cor da fonte baseado em seu valor, branco para positivo, vemelhor
	 * para negativo.
	 */
	private void changeColor() {
		if (score >= 0) {
			this.text.setColor(ccColor3B.ccWHITE);
		} else {
			this.text.setColor(ccColor3B.ccRED);
		}

	}

	public int getScore() {
		return score;
	}

}
