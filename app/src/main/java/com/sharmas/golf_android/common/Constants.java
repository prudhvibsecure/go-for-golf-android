package com.sharmas.golf_android.common;

import android.os.Environment;

public class Constants {

	public static final String ITEM = "item.xml";

	public static String PATH = Environment.getExternalStorageDirectory()
			.toString();

	public static final String DWLPATH = PATH + "/Golf/downloads/";

	public static final String CACHEPATH = PATH + "/Golf/cache/";

	public static final String CACHEDATA = CACHEPATH + "data/";

	public static final String CACHETEMP = CACHEPATH + "temp/";

	public static final String CACHEIMAGE = CACHEPATH + "images/";


}
