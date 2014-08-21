package costa.nascimento.bis.layers;

import static costa.nascimento.bis.settings.DeviceSettings.screenWidth;
import static costa.nascimento.bis.settings.DeviceSettings.screenHeight;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.opengl.CCBitmapFontAtlas;

import costa.nascimento.bis.assets.Assets;

public class Score extends CCLayer {
	private int score;
	// Vari�vel utilizada para imprimir o valor de score na tela.
	private CCBitmapFontAtlas text;

	public Score() {
		this.score = 0;
		this.text = CCBitmapFontAtlas.bitmapFontAtlas(
				String.valueOf(this.score), Assets.FONT);
		this.text.setScale((float) 240 / 240);
		this.setPosition(screenWidth() - 50, screenHeight() - 50);
		this.addChild(this.text);
	}

	/**
	 * Aumenta placar do jogador.
	 */
	public void increase() {
		score++;
		this.text.setString(String.valueOf(this.score));
	}
}
