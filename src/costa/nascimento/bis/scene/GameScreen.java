package costa.nascimento.bis.scene;

import static costa.nascimento.bis.settings.DeviceSettings.screenHeight;
import static costa.nascimento.bis.settings.DeviceSettings.screenWidth;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

import costa.nascimento.bis.R;
import costa.nascimento.bis.constants.Constants;
import costa.nascimento.bis.layers.GameButtons;
import costa.nascimento.bis.layers.MeteorsEngine;
import costa.nascimento.bis.layers.MeteorsEngineObserver;
import costa.nascimento.bis.layers.Score;
import costa.nascimento.bis.layers.ShootEngineObserver;
import costa.nascimento.bis.sprites.Meteor;
import costa.nascimento.bis.sprites.Player;
import costa.nascimento.bis.sprites.Shoot;
import costa.nascimento.bis.util.PauseObserver;
import costa.nascimento.bis.util.Runner;

public class GameScreen extends CCScene implements MeteorsEngineObserver,
		ShootEngineObserver, PauseObserver {
	private CCSprite background1, background2;
	private MeteorsEngine meteorsEngine;
	private CCLayer meteorsLayer;
	private CCLayer playerLayer;
	private CCLayer shootsLayer;
	private CCLayer pauseLayer;
	private PauseScreen pauseScreen;
	private Player player;
	private CCLayer scoreLayer;
	private Score score;
	private GameButtons gameButtons;
	private List<Meteor> meteorsArray;
	private List<Shoot> shootsArray;
	private List<Player> playersArray;

	private static final int SCORE_2_WIN = 10;
	private static final int SCORE_2_LOOSE = -10;
	private static final float BACKGROUND_SCROLL_SPEED = 1;

	private GameScreen() {
		addBackground();

		// Adiciona a camada dos meteoros
		this.meteorsLayer = CCLayer.node();
		this.addChild(this.meteorsLayer);

		// Adiciona a camada do jogador
		this.playerLayer = CCLayer.node();
		this.addChild(this.playerLayer);

		// Adiciona a camada do score
		this.scoreLayer = CCLayer.node();
		this.addChild(this.scoreLayer);

		// Adiciona a camada dos botões
		gameButtons = new GameButtons();
		this.addChild(gameButtons.getMoveLeftButton());
		this.addChild(gameButtons.getMoveRightButton());
		this.addChild(gameButtons.getPauseButton());

		// Adiciona a camada dos tiros
		this.shootsLayer = CCLayer.node();
		this.addChild(this.shootsLayer);

		// Adiciona a camada de pause
		this.pauseLayer = CCLayer.node();
		this.addChild(pauseLayer);

		this.addGameObjects();

		// player.catchAccelerometer();

		preloadCache();

	}

	/**
	 * Inicia a tela final do jogo, quando o jogador vence.
	 */
	private void startFinalScreen() {
		CCDirector.sharedDirector().replaceScene(new FinalScreen().scene());

	}

	/**
	 * Inicia a tela de game over do jogo, quando o jogador perde.
	 */
	private void startGameOverScreen() {
		CCDirector.sharedDirector().replaceScene(new GameOverScreen().scene());
	}

	public static CCScene createGame() {
		CCScene scene = CCScene.node();
		GameScreen layer = new GameScreen();
		scene.addChild(layer);
		return scene;
	}

	/**
	 * Adiciona o backgroung na tela do jogo.
	 */
	private void addBackground() {
		
		background1 = CCSprite.sprite(Constants.BACKGROUND);
		background1.setPosition(screenWidth() / 2.0f, screenHeight() / 2.0f);
		this.addChild(this.background1);

		background2 = CCSprite.sprite(Constants.BACKGROUND);
		background2.setScaleY(-1);
		background2.setPosition(screenWidth() / 2.0f,
				((background1.getBoundingBox().size.height) * 1.5f));
		this.addChild(this.background2);
	}

	/**
	 * Faz o background se mover.
	 */
	public void scrollBackground(float dt) {
		// movimenta os background para baixo, eixo y
		background1.setPosition(background1.getPosition().x,
				background1.getPosition().y - BACKGROUND_SCROLL_SPEED);
		background2.setPosition(background2.getPosition().x,
				background2.getPosition().y - BACKGROUND_SCROLL_SPEED);

		// faz a verificação de reposição da imagem
		if (background1.getPosition().y < -(background1.getBoundingBox().size.height * 0.5)) {
			changeScaleX(background1);
			background1.setPosition(background1.getPosition().x,
					background1.getBoundingBox().size.height * 1.5f);
		}
		if (background2.getPosition().y < -(background2.getBoundingBox().size.height * 0.5)) {
			changeScaleX(background2);
			background2.setPosition(background2.getPosition().x,
					background2.getBoundingBox().size.height * 1.5f);
		}
	}

	/**
	 * Inverte o eixo X de um sprite.
	 * 
	 * @param ccSprite
	 *            O sprite que deverá ter o eixo X invertido.
	 */
	private void changeScaleX(CCSprite ccSprite) {
		if (ccSprite.getScaleX() == 1) {
			ccSprite.setScaleX(-1);
		} else {
			ccSprite.setScaleX(1);
		}
	}

	/**
	 * Cria uma meteoro, adicionando-o tanto na Layer de meteoro quanto no array
	 * de meteoro.
	 */
	@Override
	public void createMeteor(Meteor meteor) {
		meteor.setDelegate(this);
		this.meteorsLayer.addChild(meteor);
		this.meteorsArray.add(meteor);
		meteor.start();
	}

	/**
	 * Faz as inicializações necessárias.
	 */
	private void addGameObjects() {
		// meteoros
		this.meteorsArray = new ArrayList<>();
		this.meteorsEngine = new MeteorsEngine();

		// jogador
		this.player = new Player();
		this.playerLayer.addChild(this.player);
		this.player.setDelegate(this);

		// score
		this.score = new Score();
		this.scoreLayer.addChild(this.score);

		// tiros e botões
		this.shootsArray = new ArrayList<>();
		this.gameButtons.setDelegate(this);

		// array de jogadores
		this.playersArray = new ArrayList<>();
		this.playersArray.add(this.player);
	}

	/**
	 * Invocado pelo Cocos2D assim que a tela do game está pronta para
	 * orquestrar os objetos do jogo.
	 */
	@Override
	public void onEnter() {
		super.onEnter();

		// Configura o status do jogo
		Runner.setGamePaused(false);

		// pause
		SoundEngine.sharedEngine().setEffectsVolume(1f);
		SoundEngine.sharedEngine().setSoundVolume(1f);

		// verifica colisões
		this.schedule("checkHits");
		// move o background
		this.schedule("scrollBackground");

		this.startEngines();
	}

	private void startEngines() {
		this.addChild(this.meteorsEngine);
		this.meteorsEngine.setDelegate(this);
		 player.startShooting();
	}

	/**
	 * Adiciona o Sprite passado como parâmetro e o adiciona tanto no Layer de
	 * tiro quanto no array de tiro.
	 */
	@Override
	public void createShoot(Shoot shoot) {

		this.shootsLayer.addChild(shoot);
		shoot.setDelegate(this);
		shoot.start();
		this.shootsArray.add(shoot);

	}

	/**
	 * Começa a movimentar o jogador para a esquerda.
	 */
	public void startMovingLeft() {
		player.startMovingLeft();
	}

	/**
	 * Para de movimentar o jogador para esqueda.
	 */
	public void stopMovingLeft() {
		player.stopMovingLeft();
	}

	/**
	 * Começa a movimentar o jogador para a direita.
	 */
	public void startMovingRight() {
		player.startMovingRight();
	}

	/**
	 * Para de movimentar o jogador para direita.
	 */
	public void stopMovingRight() {
		player.stopMovingRight();
	}

	/**
	 * Dado um Sprite, determina suas bordas, de modo a facilitar a checagem de
	 * colisão com outros Sprites.
	 * 
	 * @param object
	 *            Sprite que será comparado
	 * @return Um objeto CGRect que representa os contornos do sprite mapeados
	 *         de forma retangular e sua posição.
	 */
	public CGRect getBoarders(CCSprite object) {
		// pega os contornos do sprite.
		CGRect rect = object.getBoundingBox();
		// pega a posição do sprite.
		CGPoint GLpoint = rect.origin;
		// cria um novo CGRect com base na posição e tamanho do sprite.
		CGRect GLrect = CGRect.make(GLpoint.x, GLpoint.y, rect.size.width,
				rect.size.height);
		return GLrect;
	}

	/**
	 * Verifica a colisão entre objetos do tipo Sprite dentro do jogo, como
	 * tiro-meteoro ou jogador-meteoro, e toma as devidas ações.
	 * 
	 * @param array1
	 *            Primeiro array de Sprites que deverá ser comparado.
	 * @param array2
	 *            Segundo array de Sprites que deverá ser comparado.
	 * @param gameScene
	 *            Utilizado para que o jogo tome alguma ação caso haja uma
	 *            colisão.
	 * @param hit
	 *            Nome do método que deverá ser executado caso haja alguma
	 *            colisão.
	 * @return true se houve uma colisão.
	 */
	private boolean checkRadiusHitsOfArray(List<? extends CCSprite> array1,
			List<? extends CCSprite> array2, GameScreen gameScene, String hit) {

		boolean result = false;

		for (CCSprite obj1 : array1) {
			// Pega objeto do primeiro array
			CGRect rect1 = getBoarders(obj1);
			for (CCSprite obj2 : array2) {
				// Pega objeto do segundo array
				CGRect rect2 = getBoarders(obj2);
				// Verifica colisão
				if (CGRect.intersects(rect1, rect2)) {
					result = true;

					Method method;
					try {
						method = GameScreen.class.getMethod(hit,
								CCSprite.class, CCSprite.class);
						method.invoke(gameScene, obj1, obj2);
					} catch (SecurityException e1) {
						e1.printStackTrace();
					} catch (NoSuchMethodException e1) {
						e1.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return result;
	}

	/**
	 * Método chamado caso haja colisão entre meteoro e o tiro.
	 * 
	 * @param meteor
	 *            O meteoro atingido.
	 * @param shoot
	 *            O tiro que atingiu.
	 */
	public void meteorHit(CCSprite meteor, CCSprite shoot) {
		int meteorPoints = ((Meteor) meteor).getPointsWorth();
		((Meteor) meteor).shooted();
		((Shoot) shoot).explode();
		this.score.increase(meteorPoints);
		if (this.score.getScore() >= SCORE_2_WIN) {
			startFinalScreen();
		}
	}

	/**
	 * Método chamado caso haja colisão entre meteoro e o player.
	 * 
	 * @param meteor
	 *            O meteoro que atingiu o player.
	 * @param player
	 *            O jogador atingido.
	 */
	public void playerHit(CCSprite meteor, CCSprite player) {
		((Meteor) meteor).shooted();
		((Player) player).explode();
		startGameOverScreen();
	}

	/**
	 * O método é chamado pelo Schedule do GameScene, ou seja, será executado de
	 * tempos em tempos. Esse método deve ser público, caso contrário não será
	 * chamado pelo schedule.
	 * 
	 * @param dt
	 *            não tenho tanta certeza do que isso significa... =/
	 */
	public void checkHits(float dt) {
		this.checkRadiusHitsOfArray(this.meteorsArray, this.shootsArray, this,
				"meteorHit");
		this.checkRadiusHitsOfArray(this.meteorsArray, this.playersArray, this,
				"playerHit");
	}

	/**
	 * Remove o meteodo do array.
	 */
	@Override
	public void removeMeteor(Meteor meteor) {
		this.meteorsArray.remove(meteor);
	}

	/**
	 * Remove o meteoro do array e diminui a pontuação do jogador.
	 */
	@Override
	public void meteorEscaped(Meteor meteor) {
		removeMeteor(meteor);
		this.score.decrease();
		if (this.score.getScore() == SCORE_2_LOOSE) {
			startGameOverScreen();
		}

	}

	/**
	 * Remove o tiro do array.
	 */
	@Override
	public void removeShoot(Shoot shoot) {
		this.shootsArray.remove(shoot);
	}

	/**
	 * Carrega os áudios de efeito previamente no cache, e inicia a música da
	 * fase.
	 */
	private void preloadCache() {
		SoundEngine.sharedEngine().preloadEffect(
				CCDirector.sharedDirector().getActivity(), R.raw.shoot);

		SoundEngine.sharedEngine().preloadEffect(
				CCDirector.sharedDirector().getActivity(), R.raw.bang);

		SoundEngine.sharedEngine().preloadEffect(
				CCDirector.sharedDirector().getActivity(), R.raw.over);

		SoundEngine.sharedEngine().playSound(
				CCDirector.sharedDirector().getActivity(), R.raw.music, true);
	}

	@Override
	public void resumeGame() {
		if (Runner.isGamePaused()) {
			// Continua o jogo
			this.pauseScreen = null;
			Runner.setGamePaused(false);
			resumeAll();

			// player.catchAccelerometer();
		}

	}

	@Override
	public void quitGame() {
		SoundEngine.sharedEngine().setEffectsVolume(0f);
		SoundEngine.sharedEngine().setSoundVolume(0f);

		CCDirector.sharedDirector().replaceScene(new TitleScreen().scene());

	}

	@Override
	public void pauseGameAndShowLayer() {
		if (!Runner.isGamePaused()) {
			Runner.setGamePaused(true);
			pauseAll();
		}
		if (Runner.isGamePaused() && this.pauseScreen == null) {
			this.pauseScreen = new PauseScreen();
			this.pauseLayer.addChild(this.pauseScreen);
			this.pauseScreen.setDelegate(this);
		}

	}

	/**
	 * Resume todas as ações e schedulers necessários
	 */
	private void resumeAll() {
		this.resumeSchedulerAndActions();
		
		meteorsEngine.resumeSchedulerAndActions();

		for (Meteor meteor : meteorsArray) {
			meteor.resumeSchedulerAndActions();
		}

		for (Player player : playersArray) {
			player.resumeSchedulerAndActions();
		}

		for (Shoot shoot : shootsArray) {
			shoot.resumeSchedulerAndActions();
		}
	}

	/**
	 * Pausa todas as ações e schedulers necessários
	 */
	private void pauseAll() {
		this.pauseSchedulerAndActions();
		
		meteorsEngine.pauseSchedulerAndActions();

		for (Meteor meteor : meteorsArray) {
			meteor.pauseSchedulerAndActions();
		}

		for (Player player : playersArray) {
			player.pauseSchedulerAndActions();
		}

		for (Shoot shoot : shootsArray) {
			shoot.pauseSchedulerAndActions();
		}
	}

	@Override
	public void muteUnmute() {
		if (SoundEngine.sharedEngine().isMute()) {
			SoundEngine.sharedEngine().unmute();
		} else {
			SoundEngine.sharedEngine().mute();
		}
	}
}
