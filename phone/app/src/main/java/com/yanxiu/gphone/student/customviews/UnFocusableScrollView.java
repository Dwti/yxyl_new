package com.yanxiu.gphone.student.customviews;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

public class UnFocusableScrollView extends ScrollView {

	public UnFocusableScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public UnFocusableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public UnFocusableScrollView(Context context) {
		super(context);
	}
    @Override
    public void requestChildFocus(View child, View focused) {
    }
	  @Override
	  protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
	    return true;
	  }
	  @Override
	  public ArrayList<View> getFocusables(int direction) {
	    return new ArrayList<View>();
	  }
}
