package costa.nascimento.bis.layers;

import org.cocos2d.layers.CCLayer;

public class UpgradeEngine extends CCLayer{
	
	private UpgradeEngineObserver observer;
	
	public UpgradeEngine(){
		schedule("createUpgrade");
	}
	
	public void createUpgrade(float dt){
		
	}

	public UpgradeEngineObserver getObserver() {
		return observer;
	}

	public void setObserver(UpgradeEngineObserver observer) {
		this.observer = observer;
	}
}
