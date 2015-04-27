package com.bxs.bnbtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by bhupinder on 5/4/15.
 */
public class PropertyAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<PropertyModel> propertyModelArrayList;

    public PropertyAdapter(Context context, ArrayList<PropertyModel> propertyModelArrayList){
        this.context = context;
        this.propertyModelArrayList = propertyModelArrayList;
    }
    @Override
    public int getCount() {
        return propertyModelArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return propertyModelArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.adapter_property, viewGroup,false);

        TextView tvName = (TextView) view.findViewById(R.id.tvname);
        TextView tvPhone = (TextView) view.findViewById(R.id.tvPhone);
        TextView tvAddress = (TextView) view.findViewById(R.id.tvAddress);
        ImageView ivImage = (ImageView) view.findViewById(R.id.imageView);

        tvName.setText(propertyModelArrayList.get(i).getName());
        tvPhone.setText(propertyModelArrayList.get(i).getPhone());
        tvAddress.setText(propertyModelArrayList.get(i).getAddress());
        Utilities.displayImage(context, propertyModelArrayList.get(i).getPicture(), ivImage);
        return view;
    }
}
