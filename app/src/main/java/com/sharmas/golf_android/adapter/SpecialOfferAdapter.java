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
import com.sharmas.golf_android.models.GolfStatesModel;
import com.sharmas.golf_android.models.OffersModel;

import java.util.List;

/**
 * Created by Admin on 2017-11-07.
 */

public class SpecialOfferAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<OffersModel> offersModelList = null;

    public SpecialOfferAdapter(Context context, List<OffersModel> list) {
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
        TextView edate;
        TextView desc;
        TextView redeem;

    }

    public View getView(final int position, final View convertView, ViewGroup parent) {
        final OffersAdapter.ViewHolder holder;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.offers_row, parent, false);
        holder = new OffersAdapter.ViewHolder();

        holder.name = (TextView) itemView.findViewById(R.id.off_name);
        holder.edate = (TextView) itemView.findViewById(R.id.off_expdate);
        holder.edate.setVisibility(View.GONE);
        holder.desc = (TextView) itemView.findViewById(R.id.off_des);
        holder.redeem = (TextView) itemView.findViewById(R.id.off_reedeem);
        holder.redeem.setVisibility(View.GONE);
        holder.name.setText(Html.fromHtml(offersModelList.get(position).getSpecialname()));
        holder.desc.setText(Html.fromHtml(offersModelList.get(position).getSdescription()));

        return itemView;
    }

    private void showToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();

    }
}
