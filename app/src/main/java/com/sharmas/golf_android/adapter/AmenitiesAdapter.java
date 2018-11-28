package com.sharmas.golf_android.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.sharmas.golf_android.R;
import com.sharmas.golf_android.models.AminitesModel;
import com.sharmas.golf_android.models.OffersModel;

import java.util.List;

/**
 * Created by Admin on 2017-11-07.
 */

public class AmenitiesAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<AminitesModel> offersModelList = null;

    public AmenitiesAdapter(Context context, List<AminitesModel> list) {
        this.context = context;
        this.offersModelList = list;
    }

    @Override
    public int getCount() {
        return offersModelList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position + 1;
    }

    public void Clear() {
        offersModelList.clear();
    }


    public static class ViewHolder {

        TextView name;
    }

    public View getView(final int position, final View convertView, ViewGroup parent) {
        final AmenitiesAdapter.ViewHolder holder;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.aminites_text_view, parent, false);
        holder = new AmenitiesAdapter.ViewHolder();

        holder.name = (TextView) itemView.findViewById(R.id.aminit_name);
        holder.name.setText(Html.fromHtml(offersModelList.get(position).getName()));

        return itemView;
    }

    private void showToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();

    }
}
