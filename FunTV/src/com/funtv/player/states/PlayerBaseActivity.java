package com.funtv.player.states;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;

import com.android.volley.toolbox.ImageLoader;
import com.funtv.player.entity.LiveSessionData;
import com.umeng.analytics.MobclickAgent;

public class PlayerBaseActivity extends Activity {

	private PlayerStateInterface mPreviousState;
	private PlayerStateInterface mCurrentState;
	public boolean isPlayError;

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public void backToPreviousState(){
		setCurrentState(mPreviousState,true);
	}

	public void setCurrentState(PlayerStateInterface state,boolean finishPrevious){
		mPreviousState = mCurrentState;
		mCurrentState = state;
		if (mPreviousState != null && finishPrevious){
			mPreviousState.exitExecute(this);
		}
		if (mCurrentState != null){
			mCurrentState.prepareExecute(this);
			mCurrentState.onExecute(this);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (mCurrentState !=null && mCurrentState.onKeyDown(this,keyCode, event))
			return true;
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (mCurrentState !=null && mCurrentState.onKeyUp(this,keyCode, event))
			return true;
		return super.onKeyUp(keyCode, event);
	}

	//caching state
	public void showCacheInfo(){

	}

	public void hideCacheInfo(){

	}
	//caching state

	//exit state
	public void showExitInfo(){

	}

	public void hideExitInfo(){

	}
	//exit state

	//init state
	public void init(){

	}
	//init state

	//menu state
	public void showMenuInfo(){

	}

	public void hideMenuInfo(){

	}
	//menu state

	//pause state
	public void showPauseInfo(){

	}

	public void hidePauseInfo(){

	}
	//pause state

	//playing state
	public void showPlayInfo(){

	}

	public void hidePlayInfo(){

	}
	//playing state

	//stuck state
	public void showStuckInfo(){

	}

	public void hideStuckInfo(){

	}
	//stuck state

	//control state
	public void showControlInfo(){

	}

	public void hideControlInfo(){

	}
	//control state

	//exit fragement
	public void showVideoTitle() {
		// TODO Auto-generated method stub

	}

	public void hideVideoTitle() {
		// TODO Auto-generated method stub

	}

	public void onBackPress() {
		// TODO Auto-generated method stub

	}

	public void setVideoTitle(String videoTitle) {
		// TODO Auto-generated method stub

	}

	public void reset() {
		// TODO Auto-generated method stub

	}

	public void initPlayData(Intent intent, boolean chgClarify) {
		// TODO Auto-generated method stub

	}

	public int getCurrentPosition() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setmLastPlayPosition(int currentPosition) {
		// TODO Auto-generated method stub

	}

	public boolean isPlaying() {
		// TODO Auto-generated method stub
		return false;
	}

	public int getDuration() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void seekTo(int newposition) {
		// TODO Auto-generated method stub

	}

	public void pause(boolean isUser) {
		// TODO Auto-generated method stub

	}

	public void start() {
		// TODO Auto-generated method stub

	}

	public ImageLoader getImageLoader() {
		// TODO Auto-generated method stub
		return null;
	}

	public void seek() {
		// TODO Auto-generated method stub

	}

	public void retryPlayOnError() {
		// TODO Auto-generated method stub
	}

	public void sendMsgFadeOut(){
		// TODO Auto-generated method stub
	}
}
