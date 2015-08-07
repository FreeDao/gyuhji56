package com.funtv.player.states;

import android.view.KeyEvent;

interface PlayerStateInterface {
	public void prepareExecute(PlayerBaseActivity va);
	public void onExecute(PlayerBaseActivity va);
	public void exitExecute(PlayerBaseActivity va);
	public boolean onKeyDown(PlayerBaseActivity va,int keyCode, KeyEvent event);
	public boolean onKeyUp(PlayerBaseActivity va,int keyCode, KeyEvent event);
}
