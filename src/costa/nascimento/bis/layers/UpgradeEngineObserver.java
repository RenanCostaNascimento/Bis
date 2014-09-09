package costa.nascimento.bis.layers;

import costa.nascimento.bis.sprites.Upgrade;

public interface UpgradeEngineObserver {

	/**
	 * Cria um upgrade, adicionando-o ao jogo.
	 * 
	 * @param upgrade
	 *            O Upgrade que deverá ser adicionado ao jogo.
	 */
	public void createUpgrade(Upgrade upgrade);

	/**
	 * Remove um upgrade do jogo.
	 * 
	 * @param upgrade
	 *            O Upgrade que deverá ser removido do jogo.
	 */
	public void removeUpgrade(Upgrade upgrade);

}
