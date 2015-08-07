package com.funtv.player.states;

import android.view.KeyEvent;

public class PlayerPlayingState implements PlayerStateInterface {

	@Override
	public void prepareExecute(PlayerBaseActivity va) {
		// TODO Auto-generated method stub
		va.showPlayInfo();
	}

	@Override
	public void onExecute(PlayerBaseActivity va) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitExecute(PlayerBaseActivity va) {
		// TODO Auto-generated method stub
		va.hidePlayInfo();
	}

	@Override
	public boolean onKeyDown(PlayerBaseActivity va,int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_MENU || keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
			va.setCurrentState(new PlayerMenuState(), false);
			return true;
		}
		
		if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			va.setCurrentState(new PlayerExitingState(), false);
			return true;
		}
		
		if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
			va.setCurrentState(new PlayerControlState(), false);
			return true;
		}
		
		return false;	
	}

	@Override
	public boolean onKeyUp(PlayerBaseActivity va, int keyCode,
			KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
			va.setCurrentState(new PlayerPauseState(), false);
			return true;
		}
		
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
			va.seek();
		}
		return false;
	}
}
