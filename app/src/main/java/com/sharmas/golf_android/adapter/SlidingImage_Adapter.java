package com.sharmas.golf_android.adapter;

import java.util.ArrayList;
import java.util.List;

import com.sharmas.golf_android.R;
import com.sharmas.golf_android.common.AppSettings;
import com.sharmas.golf_android.models.GolfImageModel;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SlidingImage_Adapter extends PagerAdapter {

	private LayoutInflater inflater;
	private Context context;

	private List<GolfImageModel> mvhomelist = null;
	private ArrayList<GolfImageModel> arraylist;


	public SlidingImage_Adapter(Context context, List<GolfImageModel> IMAGES) {
		this.context = context;
		this.mvhomelist = IMAGES;
		inflater = LayoutInflater.from(context);
		this.arraylist = new ArrayList<GolfImageModel>();
		this.arraylist.addAll(IMAGES);

	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public int getCount() {
		return arraylist.size();
	}

	@Override
	public Object instantiateItem(ViewGroup view, int position) {
		View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

		assert imageLayout != null;
		final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image_slide);

		((TextView) imageLayout.findViewById(R.id.title_name)).setText(Html.fromHtml(mvhomelist.get(position).getFullname()));

		String totla_url = AppSettings.getInstance(context).getPropertyValue("i_path");

		String t_path=totla_url+mvhomelist.get(position).getGimage();
		Picasso.with(context).load(t_path).placeholder(R.mipmap.gfg_location_img).noFade().into(imageView);
		//imageLoader.DisplayImage(mvhomelist.get(position).getGimage(), imageView);
		view.addView(imageLayout, 0);

		return imageLayout;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

}