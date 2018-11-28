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
import com.sharmas.golf_android.common.AppPreferences;
import com.sharmas.golf_android.models.GolfStatesModel;

import java.util.List;

/**
 * Created by Admin on 2017-03-28.
 */

public class OffersAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<GolfStatesModel> stateList = null;
    private OffersAdapterListener listener;

    public OffersAdapter(Context context, List<GolfStatesModel> list, OffersAdapterListener listener) {
        this.context = context;
        this.stateList = list;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return stateList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position + 1;
    }

    public void Clear() {
        stateList.clear();
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
        holder.desc = (TextView) itemView.findViewById(R.id.off_des);
        holder.redeem = (TextView) itemView.findViewById(R.id.off_reedeem);

        holder.name.setText(Html.fromHtml(stateList.get(position).getStateName()));
        holder.edate.setText(Html.fromHtml(stateList.get(position).getExpdate()));
        holder.desc.setText(Html.fromHtml(stateList.get(position).getDesc()));

//        holder.redeem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        applyClickEvents(holder, position);
        return itemView;
    }

    private void showToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();

    }

    public interface OffersAdapterListener {

        void onMessageReedmClicked(int position);

        void onMessageReedmClicked(String golfId, String offerId);
    }

    private void applyClickEvents(ViewHolder holder, final int position) {

        holder.redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = AppPreferences.getInstance(context).getFromStore("email");
                if (email.length() == 0) {
                    showToast("Please Register to Redeem this Offer");
                } else {
                    listener.onMessageReedmClicked(stateList.get(position).getGolfId(), stateList.get(position).getOfferId());
                }
            }
        });

    }
}