package costa.nascimento.bis.layers;

import costa.nascimento.bis.sprites.Upgrade;

public interface UpgradeEngineObserver {
	
	public void createUpgrade(Upgrade upgrade);
	
	public void removeUpgrade(Upgrade upgrade);

}
