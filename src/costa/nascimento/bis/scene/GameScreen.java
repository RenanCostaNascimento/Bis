package costa.nascimento.bis.scene;

import static costa.nascimento.bis.settings.DeviceSettings.screenWidth;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import costa.nascimento.bis.layers.UpgradeEngine;
import costa.nascimento.bis.layers.UpgradeEngineObserver;
import costa.nascimento.bis.sprites.Meteor;
import costa.nascimento.bis.sprites.Player;
import costa.nascimento.bis.sprites.Shoot;
import costa.nascimento.bis.sprites.Upgrade;
import costa.nascimento.bis.util.PauseObserver;
import costa.nascimento.bis.util.Runner;

public class GameScreen extends CCScene implements MeteorsEngineObserver,
		ShootEngineObserver, PauseObserver, UpgradeEngineObserver {
	private CCSprite background1, background2;
	private MeteorsEngine meteorsEngine;
	private UpgradeEngine upgradeEngine;
	private CCLayer meteorsLayer, playerLayer, shootsLayer, pauseLayer,
			scoreLayer, upgradesLayer;
	private PauseScreen pauseScreen;
	private Player player;
	private Score score;
	private GameButtons gameButtons;
	private List<Meteor> meteorsArray;
	private List<Shoot> shootsArray;
	private List<Player> playersArray;
	private List<Upgrade> upgradesArray;

	private static final int SCORE_2_WIN = 64;
	private static final int SCORE_2_LOOSE = -5;
	private static final float BACKGROUND_SCROLL_SPEED = 2;

	/**
	 * Quando o background � resetado para a posi��o original ao chegar no fim
	 * da imagem, ele n�o se desloca para baixo como deveria, uma vez que o
	 * frame foi gasto para resetar a posi��o. Isso ger� um erro de dist�ncia
	 * acumulativo entre os dois backgrounds igual � velocidade de deslocamento
	 * dos mesmos (quantidade de pixels que eles n�o se moveram quando
	 * resetaram). Essa constante foi criada com o intuito de remover esse erro.
	 * Na pr�tica, � poss�vel utilizar a constante BACKGROUND_SCROLL_SPEED para
	 * acertar esse erro (j� que ambas devem possuir o mesmo valor), mas por
	 * motivos de entendimento e aprendizado, escolhi cri�-la para explicitar a
	 * exist�ncia do problema.
	 */
	private static final float BACKGROUND_DESLOCATION_ERROR = BACKGROUND_SCROLL_SPEED;

	private GameScreen() {
		addBackground();

		// Adiciona a camada dos meteoros
		this.meteorsLayer = CCLayer.node();
		this.addChild(this.meteorsLayer);

		// Adiciona a camada do jogador
		this.playerLayer = CCLayer.node();
		this.addChild(this.playerLayer);

		// Adiciona a camada dos upgrades
		this.upgradesLayer = CCLayer.node();
		this.addChild(upgradesLayer);

		// Adiciona a camada do score
		this.scoreLayer = CCLayer.node();
		this.addChild(this.scoreLayer);

		// Adiciona a camada dos bot�es
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
		CCDirector.sharedDirector().replaceScene(new FinalScreen());

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
		background1.setPosition(screenWidth() / 2.0f,
				background1.getBoundingBox().size.height / 2.0f);
		this.addChild(this.background1);

		background2 = CCSprite.sprite(Constants.BACKGROUND);
		background2.setScaleY(-1);
		background2.setPosition(screenWidth() / 2.0f,
				(background2.getBoundingBox().size.height * 1.5f));
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

		// verifica se a imagem chegou ao fim
		if (background1.getPosition().y < -(background1.getBoundingBox().size.height * 0.5)) {
			// verifica se h� necessidade de variar o background
			if (background2.getScaleX() != background1.getScaleX()) {
				changeScaleX(background1);
			}
			background1.setPosition(background1.getPosition().x,
					(background1.getBoundingBox().size.height * 1.5f)
							- BACKGROUND_DESLOCATION_ERROR);
		}
		// verifica se a imagem chegou ao fim
		if (background2.getPosition().y < -(background2.getBoundingBox().size.height * 0.5)) {
			// 50% de chance de haver mudan�a no background
			if (new Random().nextInt(2) == 0) {
				changeScaleX(background2);
			}
			background2.setPosition(background2.getPosition().x,
					(background2.getBoundingBox().size.height * 1.5f)
							- BACKGROUND_DESLOCATION_ERROR);
		}
	}

	/**
	 * Inverte o eixo X de um sprite.
	 * 
	 * @param sprite
	 *            O sprite que dever� ter o eixo X invertido.
	 */
	private void changeScaleX(CCSprite sprite) {
		if (sprite.getScaleX() == 1) {
			sprite.setScaleX(-1);
		} else {
			sprite.setScaleX(1);
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
	 * Faz as inicializa��es necess�rias.
	 */
	private void addGameObjects() {
		// meteoros
		this.meteorsArray = new ArrayList<>();
		this.meteorsEngine = new MeteorsEngine();

		// upgrades
		this.upgradesArray = new ArrayList<>();
		this.upgradeEngine = new UpgradeEngine();

		// jogador
		this.player = new Player();
		this.playerLayer.addChild(this.player);
		this.player.setDelegate(this);

		// score
		this.score = new Score();
		this.scoreLayer.addChild(this.score);

		// tiros e bot�es
		this.shootsArray = new ArrayList<>();
		this.gameButtons.setDelegate(this);

		// array de jogadores
		this.playersArray = new ArrayList<>();
		this.playersArray.add(this.player);
	}

	/**
	 * Invocado pelo Cocos2D assim que a tela do game est� pronta para
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

		// verifica colis�es
		this.schedule("checkHits");
		// move o background
		this.schedule("scrollBackground");

		this.startEngines();
	}

	private void startEngines() {
		// meteoros
		this.addChild(this.meteorsEngine);
		this.meteorsEngine.setDelegate(this);

		// upgrades
		this.addChild(this.upgradeEngine);
		this.upgradeEngine.setObserver(this);

		// tiros
		player.startShooting();
	}

	/**
	 * Adiciona o Sprite passado como par�metro e o adiciona tanto no Layer de
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
	 * Come�a a movimentar o jogador para a esquerda.
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
	 * Come�a a movimentar o jogador para a direita.
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
	 * colis�o com outros Sprites.
	 * 
	 * @param object
	 *            Sprite que ser� comparado
	 * @return Um objeto CGRect que representa os contornos do sprite mapeados
	 *         de forma retangular e sua posi��o.
	 */
	public CGRect getBoarders(CCSprite object) {
		// pega os contornos do sprite.
		CGRect rect = object.getBoundingBox();
		// pega a posi��o do sprite.
		CGPoint GLpoint = rect.origin;
		// cria um novo CGRect com base na posi��o e tamanho do sprite.
		CGRect GLrect = CGRect.make(GLpoint.x, GLpoint.y, rect.size.width,
				rect.size.height);
		return GLrect;
	}

	/**
	 * Verifica a colis�o entre objetos do tipo Sprite dentro do jogo, como
	 * tiro-meteoro ou jogador-meteoro, e toma as devidas a��es.
	 * 
	 * @param array1
	 *            Primeiro array de Sprites que dever� ser comparado.
	 * @param array2
	 *            Segundo array de Sprites que dever� ser comparado.
	 * @param gameScene
	 *            Utilizado para que o jogo tome alguma a��o caso haja uma
	 *            colis�o.
	 * @param hit
	 *            Nome do m�todo que dever� ser executado caso haja alguma
	 *            colis�o.
	 * @return true se houve uma colis�o.
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
				// Verifica colis�o
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
	 * M�todo chamado caso haja colis�o entre meteoro e o tiro.
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
	 * M�todo chamado caso haja colis�o entre meteoro e o player.
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
	 * M�todo chamada caso haja colis�o entre player e upgrade.
	 * 
	 * @param upgrade
	 *            O upgrade que atingiu o jogador.
	 * @param player
	 *            O jogador atingido.
	 */
	public void upgradeCollected(CCSprite player, CCSprite upgrade) {
		((Player) player).upgradeCollected((Upgrade) upgrade);
		((Upgrade) upgrade).upgradeCollected();

	}

	/**
	 * O m�todo � chamado pelo Schedule do GameScene, ou seja, ser� executado de
	 * tempos em tempos. Esse m�todo deve ser p�blico, caso contr�rio n�o ser�
	 * chamado pelo schedule.
	 * 
	 * @param dt
	 *            n�o tenho tanta certeza do que isso significa... =/
	 */
	public void checkHits(float dt) {
		this.checkRadiusHitsOfArray(this.meteorsArray, this.shootsArray, this,
				"meteorHit");
		this.checkRadiusHitsOfArray(this.meteorsArray, this.playersArray, this,
				"playerHit");
		this.checkRadiusHitsOfArray(this.playersArray, this.upgradesArray,
				this, "upgradeCollected");
	}

	/**
	 * Remove o meteodo do array.
	 */
	@Override
	public void removeMeteor(Meteor meteor) {
		this.meteorsArray.remove(meteor);
	}

	/**
	 * Remove o meteoro do array e diminui a pontua��o do jogador.
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
	 * Carrega os �udios de efeito previamente no cache, e inicia a m�sica da
	 * fase.
	 */
	private void preloadCache() {
		SoundEngine.sharedEngine().preloadEffect(
				CCDirector.sharedDirector().getActivity(), R.raw.shoot);

		SoundEngine.sharedEngine().preloadEffect(
				CCDirector.sharedDirector().getActivity(), R.raw.bang);

		SoundEngine.sharedEngine().preloadEffect(
				CCDirector.sharedDirector().getActivity(), R.raw.over);

		SoundEngine.sharedEngine().preloadEffect(
				CCDirector.sharedDirector().getActivity(), R.raw.over2);

		SoundEngine.sharedEngine().preloadEffect(
				CCDirector.sharedDirector().getActivity(),
				R.raw.movementspeedpickup);

		SoundEngine.sharedEngine().preloadEffect(
				CCDirector.sharedDirector().getActivity(),
				R.raw.attackspeedpickup);
		
		SoundEngine.sharedEngine().preloadEffect(
				CCDirector.sharedDirector().getActivity(),
				R.raw.pause);

		SoundEngine.sharedEngine().preloadEffect(
				CCDirector.sharedDirector().getActivity(), R.raw.finalend);

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
	 * Resume todas as a��es e schedulers necess�rios
	 */
	private void resumeAll() {
		this.resumeSchedulerAndActions();

		meteorsEngine.resumeSchedulerAndActions();
		upgradeEngine.resumeSchedulerAndActions();

		for (Meteor meteor : meteorsArray) {
			meteor.resumeSchedulerAndActions();
		}

		for (Upgrade upgrade : upgradesArray) {
			upgrade.resumeSchedulerAndActions();
		}

		for (Player player : playersArray) {
			player.resumeSchedulerAndActions();
		}

		for (Shoot shoot : shootsArray) {
			shoot.resumeSchedulerAndActions();
		}

		SoundEngine.sharedEngine().resumeSound();
	}

	/**
	 * Pausa todas as a��es e schedulers necess�rios
	 */
	private void pauseAll() {
		this.pauseSchedulerAndActions();

		meteorsEngine.pauseSchedulerAndActions();
		upgradeEngine.pauseSchedulerAndActions();

		for (Meteor meteor : meteorsArray) {
			meteor.pauseSchedulerAndActions();
		}

		for (Upgrade upgrade : upgradesArray) {
			upgrade.pauseSchedulerAndActions();
		}

		for (Player player : playersArray) {
			player.pauseSchedulerAndActions();
		}

		for (Shoot shoot : shootsArray) {
			shoot.pauseSchedulerAndActions();
		}

		SoundEngine.sharedEngine().pauseSound();
	}

	@Override
	public void muteUnmute() {
		if (SoundEngine.sharedEngine().isMute()) {
			SoundEngine.sharedEngine().unmute();
		} else {
			SoundEngine.sharedEngine().mute();
		}
	}

	@Override
	public void createUpgrade(Upgrade upgrade) {
		upgrade.setObserver(this);
		this.upgradesLayer.addChild(upgrade);
		this.upgradesArray.add(upgrade);
		upgrade.start();

	}

	@Override
	public void removeUpgrade(Upgrade upgrade) {
		this.upgradesArray.remove(upgrade);

	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// System.out.println("key pressed");
	// if(event.getKeyCode() == KeyEvent.KEYCODE_POWER){
	// System.out.println("it was power button");
	// this.pauseGameAndShowLayer();
	// return true;
	// }
	// return false;
	// }
}
