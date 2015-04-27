package com.bxs.bnbtest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by bhupinder on 5/4/15.
 */
public class RoomAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<RoomModel> roomModelArrayList;

    private int arrivalDateInt = 0;
    private int arrivalYearInt = 0;
    private int arrivalMonthInt = 0;
    private int departureDateInt = 0;
    public RoomAdapter(Context context, ArrayList<RoomModel> propertyModelArrayList,  String arrivalDate, String departureDate){
        this.context = context;
        this.roomModelArrayList = propertyModelArrayList;
        arrivalDateInt = Integer.parseInt(arrivalDate)%100;
        arrivalMonthInt = (Integer.parseInt(arrivalDate)/100)%100;
        arrivalYearInt = (Integer.parseInt(arrivalDate)/100)/100;

        if(departureDate.length()==7)
            departureDateInt = Integer.parseInt(departureDate)%10;
        else
            departureDateInt = Integer.parseInt(departureDate)%100;
    }
    @Override
    public int getCount() {
        return roomModelArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return roomModelArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.adapter_room, viewGroup,false);

        TextView tvName = (TextView) view.findViewById(R.id.tvname);
        TextView tvPhone = (TextView) view.findViewById(R.id.tvPhone);
        TextView tvAddress = (TextView) view.findViewById(R.id.tvMaxPeople);
        final Button btnIncr = (Button) view.findViewById(R.id.btnIncrement);
        Button btnDecr = (Button) view.findViewById(R.id.btnDecrement);
        final Button btnNop = (Button) view.findViewById(R.id.btnNumberOfPeople);
        ImageView ivImage = (ImageView) view.findViewById(R.id.imageView);
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.cbadd);

        tvName.setText(roomModelArrayList.get(i).getName());
        String price = "";
        List<String> elephantList = Arrays.asList(roomModelArrayList.get(i).getPrice().split(","));
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(arrivalYearInt, arrivalMonthInt, arrivalDateInt);
        for(int j=0;j<elephantList.size();j++) {


            price += gc.get(Calendar.DAY_OF_MONTH) +" " + getMonth(gc.get(Calendar.MONTH)) + " for $" + elephantList.get(j);
            gc.add(Calendar.DATE, 1);
            if(j<elephantList.size()-1){
                price+=",";
            }

        }

        if(price.endsWith(","))
            price.substring(0, price.indexOf(",")-1);
        tvPhone.setText(price);
        tvAddress.setText(roomModelArrayList.get(i).getMaxPeople());
        Utilities.displayImage(context, roomModelArrayList.get(i).getPicture(), ivImage);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                try {
                    if (b) {
                        Utilities.toast(context, "Added to Bookings");
                        ArrayList<RoomModel> roomModels = null;
                        if (Constants.bookModel.getRoomModelArrayList() == null) {
                            roomModels = new ArrayList<RoomModel>();
                        } else {
                            roomModels = Constants.bookModel.getRoomModelArrayList();
                        }
                        roomModels.add(roomModelArrayList.get(i));
                        RoomList.btnCartRooms.setText("Rooms (" + roomModels.size() + ")");
                        Constants.bookModel.setRoomModelArrayList(roomModels);

                    } else {
                        Utilities.toast(context, "Removed from to Bookings");
                        if (Constants.bookModel.getRoomModelArrayList() != null) {
                            for (int j = 0; j < Constants.bookModel.getRoomModelArrayList().size(); j++) {
                                if (Constants.bookModel.getRoomModelArrayList().get(j).getId().equals(roomModelArrayList.get(i).getId())) {
                                    ArrayList<RoomModel> roomModels = null;
                                    if (Constants.bookModel.getRoomModelArrayList() == null) {
                                        roomModels = new ArrayList<RoomModel>();
                                    } else {
                                        roomModels = Constants.bookModel.getRoomModelArrayList();
                                    }
                                    try {
                                        roomModels.remove(j);
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        e.printStackTrace();
                                    }
                                    Log.i("size:", roomModels.size() + "");
                                    Constants.bookModel.setRoomModelArrayList(roomModels);
                                    RoomList.btnCartRooms.setText("Rooms (" + roomModels.size() + ")");
                                    break;
                                }
                            }
                        }

                    }
                } catch (Exception e) {
                    Utilities.toast(context, e.toString());
                }
            }
        });

        btnIncr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(Constants.bookModel.getRoomModelArrayList().get(i)!=null) {
                        if((Integer.parseInt(btnNop.getText().toString()) + 1) <=0) {
                            btnNop.setText((Integer.parseInt(btnNop.getText().toString()) + 1) + "");
                            Constants.bookModel.getRoomModelArrayList().get(i).setMaxPeople(btnNop.getText().toString());
                        } else {
                            Utilities.toast(context, "Total number of people can't exceed");
                        }
                    } else {
                        Utilities.toast(context, "Please add room to booking list");
                    }
                } catch (Exception e){
                    Utilities.toast(context, "Please add room to booking list");
                }

            }
        });

        btnDecr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((Integer.parseInt(btnNop.getText().toString()) - 1)>=0){
                    btnNop.setText((Integer.parseInt(btnNop.getText().toString()) - 1) + "");
                    Constants.bookModel.getRoomModelArrayList().get(i).setMaxPeople(btnNop.getText().toString());
                }

            }
        });
        return view;
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
