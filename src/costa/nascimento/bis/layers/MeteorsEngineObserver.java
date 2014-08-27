package costa.nascimento.bis.layers;

import costa.nascimento.bis.sprites.Meteor;

public interface MeteorsEngineObserver {
	public void createMeteor(Meteor meteor);
	
	public void removeMeteor(Meteor meteor);
	
	public void meteorEscaped(Meteor meteor);
}
