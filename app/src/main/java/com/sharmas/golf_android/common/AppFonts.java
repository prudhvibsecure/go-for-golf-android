package com.sharmas.golf_android.common;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

public class AppFonts {
	
	static AppFonts appFonts = null;
	
	private Context mContext = null;

	private Typeface zawgyi = null;
	private Typeface futuraStdBook = null;
	private Typeface opificioBoldWebfont = null;
	
	public static AppFonts getInstance(Context context) {
		if (appFonts == null)
			appFonts = new AppFonts(context);
		return appFonts;
	}
	
	public AppFonts(Context context) {
		this.mContext = context;	
		
		AssetManager assets  = mContext.getAssets();
		
		zawgyi = Typeface.createFromAsset(assets, "Century Gothic Regular.ttf");
		futuraStdBook = Typeface.createFromAsset(assets, "Century Gothic Regular.ttf");
		
		opificioBoldWebfont = Typeface.createFromAsset(assets, "Century Gothic Regular.ttf");

	}	
		
	public Typeface getFutureStdBookFont() {
		return AppPreferences.getInstance(mContext).getFromStore("language").equals("2") ? zawgyi : futuraStdBook;
	}
	
	public Typeface getOpificioBoldWebfont() {
		return AppPreferences.getInstance(mContext).getFromStore("language").equals("2") ? zawgyi : opificioBoldWebfont;
	}
	
	public Typeface getZawgyiFont() {
		return zawgyi;
	}
				
}
