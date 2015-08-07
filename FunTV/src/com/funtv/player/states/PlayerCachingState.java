package com.funtv.player.states;

import android.view.KeyEvent;


public class PlayerCachingState implements PlayerStateInterface {

	@Override
	public void prepareExecute(PlayerBaseActivity va){
		va.showCacheInfo();
	}
	
	@Override
	public void exitExecute(PlayerBaseActivity va){
		va.hideCacheInfo();
	}

	@Override
	public void onExecute(PlayerBaseActivity va) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onKeyDown(PlayerBaseActivity va, int keyCode,
			KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onKeyUp(PlayerBaseActivity va, int keyCode,
			KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
}
