package com.example.admin.boxtest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.admin.boxtest.R;
import com.example.admin.boxtest.entity.NormalMultipleEntity;

import java.util.List;

/**GirdView 数据适配器*/
public class GridViewAdapter extends BaseAdapter {
    Context context;
    List<NormalMultipleEntity> list;
    public GridViewAdapter(Context _context, List<NormalMultipleEntity> _list) {
        this.list = _list;
        this.context = _context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(R.layout.recommend_item_normal, null);
        /*TextView tvCity = (TextView) convertView.findViewById(R.id.tvCity);
        TextView tvCode = (TextView) convertView.findViewById(R.id.tvCode);
        CityItem city = list.get(position);

        tvCity.setText(city.getCityName());
        tvCode.setText(city.getCityCode());*/
        return convertView;
    }
}

