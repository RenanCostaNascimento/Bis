package costa.nascimento.bis.layers;

import costa.nascimento.bis.sprites.Meteor;

public interface MeteorsEngineObserver {
	public void createMeteor(Meteor meteor, float x, float y, float vel,
			double ang, int vl);
	
	public void removeMeteor(Meteor meteor);
}
