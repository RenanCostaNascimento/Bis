package costa.nascimento.bis.layers;

import costa.nascimento.bis.sprites.Shoot;

public interface ShootEngineObserver {
	public void createShoot(Shoot shoot);
	
	public void removeShoot(Shoot shoot);
}
