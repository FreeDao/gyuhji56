package com.funtv.utils;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

public class AnimationUtil {
	public static final long ANIM_DURATION = 500;
	public static final long HIDE_INDICATOR_DURATION = 10000;

	public static void hideIndicator(final TextView view) {		
		TranslateAnimation tranAnim = new TranslateAnimation(
                Animation.ABSOLUTE, 0.0f, Animation.ABSOLUTE, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f);
		tranAnim.setInterpolator(new LinearInterpolator());
//		AlphaAnimation alphaAnim = new AlphaAnimation(1, 0);
//		alphaAnim.setInterpolator(new AccelerateInterpolator(1.0f));
		AnimationSet set = new AnimationSet(false);
		set.addAnimation(tranAnim);
//		set.addAnimation(alphaAnim);
		set.setDuration(ANIM_DURATION);       
		set.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub    
				view.clearAnimation(); //清除动画
				view.setVisibility(View.GONE);//重新设置view的大小
				view.setText("");
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
			}
        });
        
		view.startAnimation(set);
	}
	
	public static void showIndicator(final TextView view) {
		TranslateAnimation tranAnim = new TranslateAnimation(
                Animation.ABSOLUTE, 0.0f, Animation.ABSOLUTE, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
		tranAnim.setInterpolator(new LinearInterpolator());
//		AlphaAnimation alphaAnim = new AlphaAnimation(0, 1);
//		alphaAnim.setInterpolator(new AccelerateInterpolator(1.0f));
		AnimationSet set = new AnimationSet(false);
		set.addAnimation(tranAnim);
//		set.addAnimation(alphaAnim);
		set.setDuration(ANIM_DURATION); 
		set.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub    
				//可以不加setFillAfter， 通过设置view位置实现效果
				view.clearAnimation(); //清除动画
				view.setVisibility(View.VISIBLE);//重新设置view的大小
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
			}
        });
        
		view.startAnimation(set);
	} 
	
	public static void hideView(final View view) {		
		AlphaAnimation alphaAnim = new AlphaAnimation(1, 0);
		alphaAnim.setInterpolator(new AccelerateInterpolator(1.0f));
		AnimationSet set = new AnimationSet(false);
		set.addAnimation(alphaAnim);
		set.setDuration(ANIM_DURATION);       
		set.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub    
				view.clearAnimation(); //清除动画
				view.setVisibility(View.GONE);//重新设置view的大小
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
			}
        });
        
		view.startAnimation(set);
	}
	
	public static void showView(final View view) {
		AlphaAnimation alphaAnim = new AlphaAnimation(0, 1);
		alphaAnim.setInterpolator(new AccelerateInterpolator(1.0f));
		AnimationSet set = new AnimationSet(false);
		set.addAnimation(alphaAnim);
		set.setDuration(ANIM_DURATION); 
		set.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub    
				//可以不加setFillAfter， 通过设置view位置实现效果
				view.clearAnimation(); //清除动画
				view.setVisibility(View.VISIBLE);//重新设置view的大小
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
			}
        });
        
		view.startAnimation(set);
	} 
	
	public static void showRotateAnimation(View view) {
		final RotateAnimation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(200);// 设置动画持续时间
		/** 常用方法 */
		animation.setRepeatCount(-1);// 设置重复次数
		view.setAnimation(animation);
		view.startAnimation(animation);
	}
}
