package com.bxs.bnbtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by bhupinder on 5/4/15.
 */
public class CartRoomAdapter extends BaseAdapter {

    private Context context;
    private Activity activity;
    private int arrivalDateInt = 0;
    private int arrivalYearInt = 0;
    private int arrivalMonthInt = 0;
    private int departureDateInt = 0;
    public CartRoomAdapter(Activity activity, Context context, String arrivalDate, String departureDate){
        this.context = context;
        this.activity = activity;
        arrivalDateInt = Integer.parseInt(arrivalDate)%100;
        arrivalMonthInt = (Integer.parseInt(arrivalDate)/100)%100;
        arrivalYearInt = (Integer.parseInt(arrivalDate)/100)/100;

        if(Constants.bookModel.getRoomModelArrayList()==null){
            ArrayList<RoomModel> roomModels = new ArrayList<>();
            Constants.bookModel.setRoomModelArrayList(roomModels);
        }
    }
    @Override
    public int getCount() {

        return Constants.bookModel.getRoomModelArrayList().size();
    }

    @Override
    public Object getItem(int i) {
        return Constants.bookModel.getRoomModelArrayList().get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.adapter_cart, viewGroup,false);

        TextView tvName = (TextView) view.findViewById(R.id.tvname);
        TextView tvPhone = (TextView) view.findViewById(R.id.tvPhone);
        TextView tvAddress = (TextView) view.findViewById(R.id.tvMaxPeople);
        ImageView ivImage = (ImageView) view.findViewById(R.id.imageView);
        ImageView ivDelete = (ImageView) view.findViewById(R.id.ivDelete);
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.cbadd);

        tvName.setText(Constants.bookModel.getRoomModelArrayList().get(i).getName());

        // put code here
        String prices = "";
        List<String> elephantList = Arrays.asList(Constants.bookModel.getRoomModelArrayList().get(i).getPrice().split(","));

        GregorianCalendar gc = new GregorianCalendar();
        gc.set(arrivalYearInt, arrivalMonthInt, arrivalDateInt);
        for(int j=0;j<elephantList.size();j++) {


            prices += gc.get(Calendar.DAY_OF_MONTH) +" " + getMonth(gc.get(Calendar.MONTH)) + " for $" + elephantList.get(j) ;
            if(j<elephantList.size()-1){
                prices+=",";
            }
            gc.add(Calendar.DATE, 1);

        }

        tvPhone.setText(prices);


        tvAddress.setText(Constants.bookModel.getRoomModelArrayList().get(i).getMaxPeople());
        Utilities.displayImage(context, Constants.bookModel.getRoomModelArrayList().get(i).getPicture(), ivImage);
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

                // Setting Dialog Title
                alertDialog.setTitle("Confirm Delete...");

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want delete this?");


                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                        // Write your code here to invoke YES event
                        delete(i);
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();
            }
        });


        return view;
    }
    private void delete( int position) {
        Constants.bookModel.getRoomModelArrayList().remove(position);
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
            CartRoomList.tvPrice.setText("$" + output);
        } else
            CartRoomList.tvPrice.setText("$0.00");
        notifyDataSetChanged();
    }

    private String getMonth(int month) {
        switch (month){
            case 0:
                return  "Jan";
            case 1:
                return "Feb";
            case 2:
                return "Mar";
            case 3:
                return "Apr";
            case 4:
                return  "May";
            case 5:
                return "Jun";
            case 6:
                return "Jul";
            case 7:
                return "Aug";
            case 8:
                return  "Sep";
            case 9:
                return "Oct";
            case 10:
                return "Nov";
            case 11:
                return "Dec";
            default:
                return "Jan";
        }
    }

}
