package com.sharmas.golf_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.sharmas.golf_android.models.GolfStatesModel;
import com.sharmas.golf_android.Golf_Regions;
import com.sharmas.golf_android.R;

import java.util.List;

/**
 * Created by Admin on 2017-03-24.
 */

public class StatesAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<GolfStatesModel> stateList = null;

    public StatesAdapter(Context context, List<GolfStatesModel> list) {
        this.context = context;
        this.stateList = list;
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

    }

    public View getView(final int position, final View convertView, ViewGroup parent) {
        final ViewHolder holder;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.states_item_row, parent, false);
        holder = new ViewHolder();

        holder.name = (TextView) itemView.findViewById(R.id.state_name);

        holder.name.setText(Html.fromHtml(stateList.get(position).getStateName()));

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pas = new Intent(context, Golf_Regions.class);
                pas.putExtra("stateid", stateList.get(position).getStateId());
                pas.putExtra("stateName", stateList.get(position).getStateName());
                pas.putExtra("golfId", stateList.get(position).getGolfId());
                pas.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(pas);

            }
        });

        return itemView;
    }

    private void showToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();

    }
}
