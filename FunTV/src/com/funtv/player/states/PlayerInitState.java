package com.funtv.player.states;

import android.view.KeyEvent;


public class PlayerInitState implements PlayerStateInterface {

	@Override
	public void onExecute(PlayerBaseActivity va){
		va.init();
		va.setCurrentState(new PlayerCachingState(), true);
	}

	@Override
	public void prepareExecute(PlayerBaseActivity va) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitExecute(PlayerBaseActivity va) {
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
