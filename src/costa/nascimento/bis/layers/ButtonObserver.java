package costa.nascimento.bis.layers;

public interface ButtonObserver {
	/**
	 * Descreve o que ir� ocorrer quando o jogador inicia um toque na tela.
	 * 
	 * @param sender
	 *            O bot�o que foi pressionado.
	 */

	public void buttonClicked(Button sender);

	/**
	 * Descreve o que ir� ocorrer quando o jogador termina um toque na tela.
	 * 
	 * @param sender
	 *            O bot�o que foi despressionado.
	 */
	public void buttonUnclicked(Button sender);

	/**
	 * Descreve o que ir� ocorrer quando o jogador move o dedo pela tela.
	 * 
	 * @param sender
	 *            O bot�o que teve o toque cancelado.
	 */
	public void buttonMoved(Button sender);
}
