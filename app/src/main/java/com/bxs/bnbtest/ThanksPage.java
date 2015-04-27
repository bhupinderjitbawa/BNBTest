package com.bxs.bnbtest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by bhupinder on 16/4/15.
 */
public class ThanksPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks_page);

        TextView tvPrice = (TextView) findViewById(R.id.tvTransactionNumber);
        TextView tvThankYou = (TextView) findViewById(R.id.tvThankYou);
        if(Utilities.hasData(getIntent().getStringExtra("transaction"))){
            tvThankYou.setText("Thank You");
            tvPrice.setText("Your transaction is successfull");
        } else {
            tvThankYou.setText("Sorry!!! Try Again!");
            tvPrice.setText(""+getIntent().getStringExtra("error"));
            tvPrice.setTextColor(getResources().getColor(R.color.Red));
        }
        //tvPrice.setText(getIntent().getStringExtra("transaction"));


    }
}
