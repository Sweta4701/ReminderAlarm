package com.example.remainderalarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class TableCustomAdapter extends ArrayAdapter<TableDataModel> {
    private Context context;
    private int resource;
    private List<TableDataModel> dataList;
    public TableCustomAdapter(Context context, int resource, List<TableDataModel> dataList) {
        super(context, resource, dataList);
        this.context = context;
        this.resource = resource;
        this.dataList = dataList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        TableDataModel data = dataList.get(position);

        TextView srnoTextView = convertView.findViewById(R.id.srnoTextView);
        TextView postTimeTextView = convertView.findViewById(R.id.postTimeTextView);
        TextView statusTextView = convertView.findViewById(R.id.statusTextView);

        srnoTextView.setText(String.valueOf(data.getSrno()));
        postTimeTextView.setText(data.getPostTime());
        statusTextView.setText(data.getStatus());

        return convertView;
    }
}
