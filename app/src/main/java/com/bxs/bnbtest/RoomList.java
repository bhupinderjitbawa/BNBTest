package com.bxs.bnbtest;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by bhupinder on 5/4/15.
 */
public class RoomList extends Activity {

    private PropertyModel model;
    private ListView listView;
    public static Button btnCartRooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);




        listView = (ListView) findViewById(R.id.listView);
        model = (PropertyModel) getIntent().getSerializableExtra("property");
        Constants.bookModel.setPropertyId(model.getId());
        listView.setAdapter(new RoomAdapter(getApplicationContext(), model.getRoomModelArrayList(),  getIntent().getStringExtra("arrivalDate"), getIntent().getStringExtra("departureDate")));

        TextView tvName = (TextView) findViewById(R.id.tvname);
        TextView tvPhone = (TextView) findViewById(R.id.tvPhone);
        TextView tvAddress = (TextView) findViewById(R.id.tvAddress);
        ImageView ivImage = (ImageView) findViewById(R.id.imageView);
        Button btnCheckout = (Button) findViewById(R.id.btnCheckout);
         btnCartRooms = (Button) findViewById(R.id.btnCartRooms);

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(Constants.bookModel.getRoomModelArrayList().size()>0) {
                        Intent intent = new Intent(RoomList.this, Checkout.class);
                        intent.putExtra("adults", "" + getIntent().getStringExtra("adults"));
                        intent.putExtra("children", "" + getIntent().getStringExtra("children"));
                        intent.putExtra("arrivalDate", "" + getIntent().getStringExtra("arrivalDate"));
                        intent.putExtra("departureDate", "" + getIntent().getStringExtra("departureDate"));

                        intent.putExtra("cvvRequired", model.isCvvRequired());
                        intent.putExtra("ApplicableTaxRate", model.getTaxRate());
                        intent.putExtra("geoId", "" + getIntent().getStringExtra("geoId") == null ? "2" : getIntent().getStringExtra("geoId"));
                        startActivity(intent);
//                        finish();
                    } else {
                        Utilities.toast(getApplicationContext(), "Please add rooms to list");
                    }
                } catch (Exception e) {
                    Utilities.toast(getApplicationContext(), "Please add rooms to list");
                }
            }
        });

        btnCartRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(RoomList.this, CartRoomList.class);

                    intent.putExtra("arrivalDate",""+getIntent().getStringExtra("arrivalDate"));
                    intent.putExtra("departureDate",""+getIntent().getStringExtra("departureDate"));
                    startActivity(intent);
                } catch (Exception e) {
                    Utilities.toast(getApplicationContext(), e.toString());
                }
            }
        });

        tvName.setText(model.getName());
        tvPhone.setText(model.getPhone());
        tvAddress.setText(model.getAddress());
        Utilities.displayImage(getApplicationContext(), model.getPicture(), ivImage);



    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Constants.bookModel.getRoomModelArrayList()!=null)
        RoomList.btnCartRooms.setText("Rooms (" + Constants.bookModel.getRoomModelArrayList().size() + ")");

    }
}
