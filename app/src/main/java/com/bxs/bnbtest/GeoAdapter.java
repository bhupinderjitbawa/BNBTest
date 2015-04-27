package com.bxs.bnbtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by bhupinder on 5/4/15.
 */
public class GeoAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<GeoModel> geoModelArrayList;

    public GeoAdapter(Context context, ArrayList<GeoModel> propertyModelArrayList){
        this.context = context;
        this.geoModelArrayList = propertyModelArrayList;
    }
    @Override
    public int getCount() {
        if(Utilities.hasData(geoModelArrayList))
        return geoModelArrayList.size();
        else return 0;
    }

    @Override
    public Object getItem(int i) {
        return geoModelArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.adapter_geo, viewGroup,false);

        TextView tvName = (TextView) view.findViewById(R.id.tvname);
        TextView tvAddress = (TextView) view.findViewById(R.id.tvAddress);

        tvName.setText(geoModelArrayList.get(i).getName());
        tvAddress.setText(geoModelArrayList.get(i).getState() + ", " + geoModelArrayList.get(i).getCountry());
        return view;
    }
}
