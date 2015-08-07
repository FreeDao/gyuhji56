package com.funtv.player.states;

import android.view.KeyEvent;

public class PlayerPauseState implements PlayerStateInterface {

	@Override
	public void prepareExecute(PlayerBaseActivity va) {
		// TODO Auto-generated method stub
		va.showPauseInfo();
	}

	@Override
	public void onExecute(PlayerBaseActivity va) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitExecute(PlayerBaseActivity va) {
		va.hidePauseInfo();
	}

	@Override
	public boolean onKeyDown(PlayerBaseActivity va, int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_MENU || keyCode == KeyEvent.KEYCODE_BACK){
			va.backToPreviousState();
			return true;
		}
		return false;
	}

	@Override
	public boolean onKeyUp(PlayerBaseActivity va, int keyCode,
			KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

}
