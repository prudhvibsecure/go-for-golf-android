package com.sharmas.golf_android.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;


import com.sharmas.golf_android.R;

import java.text.DecimalFormat;

public class Utils {
	private static Dialog dialog = null;
	private Context context;
	/**
	 * urlEncode -
	 *
	 * @param String
	 * @return String
	 */
	public static String urlEncode(String sUrl) {
		int i = 0;
		String urlOK = "";
		while (i < sUrl.length()) {
			if (sUrl.charAt(i) == ' ') {
				urlOK = urlOK + "%20";
			} else {
				urlOK = urlOK + sUrl.charAt(i);
			}
			i++;
		}
		return (urlOK);
	}

	public static void showProgress(String title, Context context) {
		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setCancelable(false);
		View view = View.inflate(context, R.layout.processing, null);

		((TextView) view.findViewById(R.id.progrtxt)).setText(title);

		dialog.setContentView(view);
		dialog.show();
	}

	public static void dismissProgress() {
		if (dialog != null)
			dialog.dismiss();
		dialog = null;
	}

}