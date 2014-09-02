package costa.nascimento.bis.util;

public interface PauseObserver {
	/**
	 * Resume o jogo.
	 */
	public void resumeGame();

	/**
	 * Sai do jogo, voltando para a tela inicial.
	 */
	public void quitGame();

	/**
	 * Pause o jogo, interrompendo o movimento de todos os objetos do mesmo.
	 */
	public void pauseGameAndShowLayer();
	
	/**
	 * Silencia ou tr�s o som de volta para o jogo (m�sica e efeitos).
	 */
	public void muteUnmute();
}
