package com.bxs.bnbtest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

/**
 * Created by bhupinder on 19/4/15.
 */
public class CartRoomList extends Activity {

    public static TextView tvPrice;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_room_list);

        listView = (ListView) findViewById(R.id.listView);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        listView.setAdapter(new CartRoomAdapter(CartRoomList.this, getApplicationContext(), getIntent().getStringExtra("arrivalDate"), getIntent().getStringExtra("departureDate")));
        float price = 0;

        if(Constants.bookModel.getRoomModelArrayList()!=null){
            for(int i=0;i<Constants.bookModel.getRoomModelArrayList().size();i++){
                try {
                    List<String> elephantList = Arrays.asList(Constants.bookModel.getRoomModelArrayList().get(i).getPrice().split(","));
                    for(int j=0;j<elephantList.size();j++)
                        price += Float.parseFloat(elephantList.get(j));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            NumberFormat formatter = NumberFormat.getNumberInstance();
            formatter.setMinimumFractionDigits(2);
            formatter.setMaximumFractionDigits(2);
            String output = formatter.format(price);
            Log.i("output", output);
            tvPrice.setText("$"+output);
        } else
        tvPrice.setText("$0.00");

    }

}
