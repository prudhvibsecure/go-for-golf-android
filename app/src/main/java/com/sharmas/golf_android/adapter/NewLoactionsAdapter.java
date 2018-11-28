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

import com.sharmas.golf_android.Golf_SubArea;
import com.sharmas.golf_android.Golf_details;
import com.sharmas.golf_android.R;
import com.sharmas.golf_android.models.GolfStatesModel;

import java.util.List;

/**
 * Created by Admin on 2017-11-28.
 */

public class NewLoactionsAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<GolfStatesModel> stateList = null;

    public NewLoactionsAdapter(Context context, List<GolfStatesModel> list) {
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
        final StatesAdapter.ViewHolder holder;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.states_item_row, parent, false);
        holder = new StatesAdapter.ViewHolder();

        holder.name = (TextView) itemView.findViewById(R.id.state_name);

        holder.name.setText(Html.fromHtml(stateList.get(position).getStateName()));

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pas = new Intent(context, Golf_SubArea.class);
                pas.putExtra("locationid", stateList.get(position).getGolfId());
                pas.putExtra("LName", stateList.get(position).getStateName());
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