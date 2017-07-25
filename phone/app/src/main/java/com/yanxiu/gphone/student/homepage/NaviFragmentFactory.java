package com.yanxiu.gphone.student.homepage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.homepage.fragment.ExerciseFragment;
import com.yanxiu.gphone.student.homework.HomeworkFragment;
import com.yanxiu.gphone.student.user.MyFragment;


public class NaviFragmentFactory {
	private int mCurrItem = 0;
	private HomeworkFragment mHomeWorkFragment;    //作业
	private ExerciseFragment mExerciseFragment;    //练习
	private MyFragment mMyFragment;                //我的
	public NaviFragmentFactory() {
	}
	public int getCurrentItem(){
		return mCurrItem;
	}
	public int getCount() {
		return 3;
	}
	public Fragment getItem(int item){
		if(item == 0){
			return mHomeWorkFragment;
		} else if(item == 1){
			return mExerciseFragment;
		}else if(item == 2){
			return mMyFragment;
		}else {
			return mExerciseFragment;
		}
	}
	private void hideFragment(FragmentTransaction transaction){

		if (mCurrItem == 0 && mHomeWorkFragment != null) {
			transaction.hide(mHomeWorkFragment);
		}
		if (mCurrItem == 1 && mExerciseFragment != null) {
			transaction.hide(mExerciseFragment);
		}
		if (mCurrItem == 2 && mMyFragment != null) {
			transaction.hide(mMyFragment);
		}
	}
	public void hideAndShowFragment(FragmentManager fragmentManager, int index){
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if (mCurrItem == 0 && mHomeWorkFragment != null) {
			transaction.hide(mHomeWorkFragment);
		}
		if (mCurrItem == 1 && mExerciseFragment != null) {
			transaction.hide(mExerciseFragment);
		}
		if (mCurrItem == 2 && mMyFragment != null) {
			transaction.hide(mMyFragment);
		}
		mCurrItem = index;
		switch (mCurrItem) {
			case 0:
				if (mHomeWorkFragment == null) {
					mHomeWorkFragment = new HomeworkFragment();
					transaction.add(R.id.content_main, mHomeWorkFragment);
				} else {
					transaction.show(mHomeWorkFragment);
//					LogInfo.log("king", "transaction.show");
//					mHomeWorkFragment.requestGroupData(true,false);
				}
				break;
			case 1:
				if (mExerciseFragment == null) {
					mExerciseFragment = new ExerciseFragment();
					transaction.add(R.id.content_main, mExerciseFragment);
				} else {
					transaction.show(mExerciseFragment);
				}
				break;
			case 2:
				if (mMyFragment == null) {
					mMyFragment = new MyFragment();
					transaction.add(R.id.content_main, mMyFragment);
				} else {
					transaction.show(mMyFragment);
				}
				break;
		}
		transaction.commit();
	}
}
