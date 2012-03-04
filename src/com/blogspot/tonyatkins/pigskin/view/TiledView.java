package com.blogspot.tonyatkins.pigskin.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blogspot.tonyatkins.pigskin.R;

public class TiledView extends LinearLayout {
	private final Activity activity;
	
	public TiledView(Activity activity, String text) {
		super(activity);
		this.activity = activity;
		this.setOrientation(LinearLayout.HORIZONTAL);
		
		
		LayoutInflater inflater =  activity.getLayoutInflater();
		for (char a : text.toCharArray()) {
			View view = inflater.inflate(R.layout.tile_layout, null);
			
			TextView letterView = (TextView) view.findViewById(R.id.tileLayoutLetter);
			letterView.setText(String.valueOf(a).toUpperCase());
			
			// TODO update the number in the corner
			
			addView(view);
		}
	}
}
