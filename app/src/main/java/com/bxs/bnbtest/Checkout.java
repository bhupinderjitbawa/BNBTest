package com.bxs.bnbtest;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by bhupinder on 14/4/15.
 */
public class Checkout extends Activity {


    private EditText edtFirstName;
    private EditText edtLastName;
    private EditText edtAddress;
    private EditText edtEmail;
    private EditText edtPhone;
    private EditText edtCity;
    private EditText edtState;
    private EditText edtCountry;
    private EditText edtCVV;
    private EditText edtCreditCardNumber;
    private EditText edtExpYear;
    private EditText edtExpMonth;
    private EditText edtPostalCode;
    private Button btnSubmit;

    private LinearLayout llProgressLayout;
    private float tax = 0;
    private TextView tvCVVRequired;
    private TextView tvDiscounts;
    private TextView tvPrice;
    private String totalPriceValue = "";
    private  NumberFormat formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        init();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isValidEmail(edtEmail.getText().toString())) {
                    if (getIntent().getBooleanExtra("cvvRequired", false) == true && Utilities.hasData(edtCVV.getText().toString()))
                        Utilities.execute(getApplicationContext(), new CheckoutTask());
                    else {
                        if (getIntent().getBooleanExtra("cvvRequired", false) == true && !Utilities.hasData(edtCVV.getText().toString())) {
                            Utilities.toast(getApplicationContext(), "Please fill CVV number");
                        } else {
                            Utilities.execute(getApplicationContext(), new CheckoutTask());
                        }
                    }
                } else
                    Utilities.toast(getApplicationContext(), "Inavlid Email Format");
            }
        });

        try {
            tvPrice = (TextView) findViewById(R.id.tvPrice);
            TextView tvTax = (TextView) findViewById(R.id.tvTax);
            tvDiscounts = (TextView) findViewById(R.id.tvDiscounts);
             tvCVVRequired = (TextView) findViewById(R.id.tvCVVRequired);
            TextView tvSubtotal = (TextView) findViewById(R.id.tvSubtotal);
            if(getIntent().getBooleanExtra("cvvRequired", false)== true){
                tvCVVRequired.setText("(required)");
            } else {
                tvCVVRequired.setText("(optional)");
            }
            float price =0;
            for(RoomModel model: Constants.bookModel.getRoomModelArrayList()){
                List<String> elephantList = Arrays.asList(model.getPrice().split(","));
                for(int j=0;j<elephantList.size();j++)
                    price += Float.parseFloat(elephantList.get(j));

            }
             formatter = NumberFormat.getNumberInstance();
            formatter.setMinimumFractionDigits(2);
            formatter.setMaximumFractionDigits(2);
            String output = formatter.format(price);
            float taxAmount = Float.parseFloat(getIntent().getStringExtra("ApplicableTaxRate"));
            float totalPrice = (price*taxAmount) + price;
            String taxValue = formatter.format(price*taxAmount);

            totalPriceValue = formatter.format((totalPrice));
            tvSubtotal.setText("$"+price);
            tvTax.setText("$"+taxValue);
            tvPrice.setText("$"+totalPriceValue);
            new CheckPricesTask().execute();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }
    }

    private class CheckoutTask extends AsyncTask<Void, Void, Void> {

        private String arrivalDate ="";
        private String departureDate ="";
        private String adults ="";
        private String children ="";
        private String xml = "";
        HashMap<String, String> UniqueId = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            llProgressLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
            arrivalDate = getIntent().getStringExtra("arrivalDate");
            departureDate = getIntent().getStringExtra("departureDate");
            adults = getIntent().getStringExtra("adults");
            children = getIntent().getStringExtra("children");
            String properties = "";
            properties += "<PropertyId>"+Constants.bookModel.getPropertyId() +"</PropertyId>";
            properties += "<RoomList>";
            for(int i=0;i<Constants.bookModel.getRoomModelArrayList().size();i++){
                properties += "<Room><RoomId>"+Constants.bookModel.getRoomModelArrayList().get(i).getId()+"</RoomId><RoomPeople>"+Constants.bookModel.getRoomModelArrayList().get(i).getMaxPeople()+"</RoomPeople></Room>";
            }
            properties += "</RoomList>";

            xml = "<CRSRequest><Auth><VendorId>ENiagara</VendorId><VendorPassword>Exp!Niagara34x89</VendorPassword></Auth>"+
                    "<Method>BookRooms</Method><ArrivalDate>"+arrivalDate+"</ArrivalDate>"+
                    "<NumAdults>"+adults+"</NumAdults><NumChildren>"+children+"</NumChildren><DepartureDate>"+departureDate+"</DepartureDate>"+
                    "<GeoId>"+getIntent().getStringExtra("geoId")+"</GeoId>"+
                    properties+
                    "<GuestFirstName>"+edtFirstName.getText().toString()+"</GuestFirstName><GuestLastName>"+edtLastName.getText().toString()+"</GuestLastName>"+
                    "<GuestEmail>"+edtEmail.getText().toString()+"</GuestEmail>"+
                    "<GuestPhone>"+edtPhone.getText().toString()+"</GuestPhone><GuestAddress1>"+edtAddress.getText().toString()+"</GuestAddress1>"+
                    "<GuestCity>"+edtCity.getText().toString()+"</GuestCity><GuestPostal>"+edtPostalCode.getText().toString()+"</GuestPostal>"+
                    "<GuestState>"+edtState.getText().toString()+"</GuestState><GuestCountry>"+edtCountry.getText().toString()+"</GuestCountry>"+
                    "<CreditCard>"+edtCreditCardNumber.getText().toString()+"</CreditCard><CCExpiryYear>"+edtExpYear.getText().toString()+"</CCExpiryYear>"+
                    "<CCExpiryMonth>"+edtExpMonth.getText().toString()+"</CCExpiryMonth><CVV>"+edtCVV.getText().toString()+"</CVV>"+
                    "<PolicyAcknowledged>TRUE</PolicyAcknowledged><OtherInformation>sometext</OtherInformation></CRSRequest>";
            Log.i("xml", xml);

            WebServiceHandler wsh = new WebServiceHandler();
            parameters.add(new BasicNameValuePair("xml",xml));
            String responseString = wsh.getWebServiceData("https://www.bnbmanager.com/ext_crshandler.php", parameters);
            Log.i("response", responseString);
            TheXMLParser parser = new TheXMLParser();
            UniqueId = parser.getUniqueId(responseString);
//            try {
//                String lineEnd = "\r\n";
//                String twoHyphens = "--";
//                String boundary = "RQdzAAihJq7Xp1kjraqf";
//
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                DataOutputStream dos = new DataOutputStream(baos);
//
//                // Send parameter #1
//                dos.writeBytes(twoHyphens + boundary + lineEnd);
//                dos.writeBytes("Content-Disposition: form-data; name=\"xml\"" + lineEnd);
//                dos.writeBytes("Content-Type: text/plain; charset=US-ASCII" + lineEnd);
//                dos.writeBytes("Content-Transfer-Encoding: 8bit" + lineEnd);
//                dos.writeBytes(lineEnd);
//                dos.writeBytes(xml
//                        + lineEnd);
//
//                dos.flush();
//                dos.close();
//
//                ByteArrayInputStream content = new ByteArrayInputStream(baos.toByteArray());
//                BasicHttpEntity entity = new BasicHttpEntity();
//                entity.setContent(content);
//
//                HttpPost httpPost = new HttpPost("https://www.bnbmanager.com/ext_crshandler.php");
//                httpPost.addHeader("Connection", "Keep-Alive");
//                httpPost.addHeader("Content-Type", "multipart/form-data; boundary=" + boundary);
//
//
//                httpPost.setEntity(entity);
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpResponse response = httpclient.execute(httpPost);
//                String responseString = wsh.inputStreamToString(response.getEntity().getContent()).toString();
//                TheXMLParser parser = new TheXMLParser();
//                UniqueId = parser.getUniqueId(responseString);
//
//
//                Log.e("JSONResponse: ", responseString);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }catch (Exception e){
//
//            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            llProgressLayout.setVisibility(View.GONE);
            Constants.bookModel = new BookModel();
            Intent intent = new Intent(Checkout.this, ThanksPage.class);
            intent.putExtra("transaction", UniqueId.get("id") + "");
            intent.putExtra("error", UniqueId.get("Error"));
            startActivity(intent);
            if(Utilities.hasData(UniqueId.get("id")))
                finish();
        }
    }


    private class CheckPricesTask extends AsyncTask<Void, Void, Void> {

        private String arrivalDate ="";
        private String departureDate ="";
        private String adults ="";
        private String children ="";
        private String xml = "";
        HashMap<String, String> UniqueId = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            llProgressLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
            arrivalDate = getIntent().getStringExtra("arrivalDate");
            departureDate = getIntent().getStringExtra("departureDate");
            adults = getIntent().getStringExtra("adults");
            children = getIntent().getStringExtra("children");
            String properties = "";
            properties += "<PropertyId>"+Constants.bookModel.getPropertyId() +"</PropertyId>";
            properties += "<RoomList>";
            for(int i=0;i<Constants.bookModel.getRoomModelArrayList().size();i++){
                properties += "<Room><RoomId>"+Constants.bookModel.getRoomModelArrayList().get(i).getId()+"</RoomId><RoomPeople>"+Constants.bookModel.getRoomModelArrayList().get(i).getMaxPeople()+"</RoomPeople></Room>";
            }
            properties += "</RoomList>";

            xml = "<CRSRequest><Auth><VendorId>ENiagara</VendorId><VendorPassword>Exp!Niagara34x89</VendorPassword></Auth>"+
                    "<Method>CheckPrices</Method><ArrivalDate>"+arrivalDate+"</ArrivalDate>"+
                    "<NumAdults>"+adults+"</NumAdults><NumChildren>"+children+"</NumChildren><DepartureDate>"+departureDate+"</DepartureDate>"+
                    "<GeoId>"+getIntent().getStringExtra("geoId")+"</GeoId>"+
                    properties+
//                    "<GuestFirstName>"+edtFirstName.getText().toString()+"</GuestFirstName><GuestLastName>"+edtLastName.getText().toString()+"</GuestLastName>"+
//                    "<GuestEmail>"+edtEmail.getText().toString()+"</GuestEmail>"+
//                    "<GuestPhone>"+edtPhone.getText().toString()+"</GuestPhone><GuestAddress1>"+edtAddress.getText().toString()+"</GuestAddress1>"+
//                    "<GuestCity>"+edtCity.getText().toString()+"</GuestCity><GuestPostal>"+edtPostalCode.getText().toString()+"</GuestPostal>"+
//                    "<GuestState>"+edtState.getText().toString()+"</GuestState><GuestCountry>"+edtCountry.getText().toString()+"</GuestCountry>"+
//                    "<CreditCard>"+edtCreditCardNumber.getText().toString()+"</CreditCard><CCExpiryYear>"+edtExpYear.getText().toString()+"</CCExpiryYear>"+
//                    "<CCExpiryMonth>"+edtExpMonth.getText().toString()+"</CCExpiryMonth><CVV>"+edtCVV.getText().toString()+"</CVV>"+
                    "<PolicyAcknowledged>TRUE</PolicyAcknowledged><OtherInformation>sometext</OtherInformation></CRSRequest>";
            Log.i("xml", xml);

            WebServiceHandler wsh = new WebServiceHandler();
            parameters.add(new BasicNameValuePair("xml",xml));
            String responseString = wsh.getWebServiceData("https://www.bnbmanager.com/ext_crshandler.php", parameters);
            Log.i("response", responseString);
            TheXMLParser parser = new TheXMLParser();
            UniqueId = parser.getUniqueId(responseString);
//            try {
//                String lineEnd = "\r\n";
//                String twoHyphens = "--";
//                String boundary = "RQdzAAihJq7Xp1kjraqf";
//
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                DataOutputStream dos = new DataOutputStream(baos);
//
//                // Send parameter #1
//                dos.writeBytes(twoHyphens + boundary + lineEnd);
//                dos.writeBytes("Content-Disposition: form-data; name=\"xml\"" + lineEnd);
//                dos.writeBytes("Content-Type: text/plain; charset=US-ASCII" + lineEnd);
//                dos.writeBytes("Content-Transfer-Encoding: 8bit" + lineEnd);
//                dos.writeBytes(lineEnd);
//                dos.writeBytes(xml
//                        + lineEnd);
//
//                dos.flush();
//                dos.close();
//
//                ByteArrayInputStream content = new ByteArrayInputStream(baos.toByteArray());
//                BasicHttpEntity entity = new BasicHttpEntity();
//                entity.setContent(content);
//
//                HttpPost httpPost = new HttpPost("https://www.bnbmanager.com/ext_crshandler.php");
//                httpPost.addHeader("Connection", "Keep-Alive");
//                httpPost.addHeader("Content-Type", "multipart/form-data; boundary=" + boundary);
//
//
//                httpPost.setEntity(entity);
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpResponse response = httpclient.execute(httpPost);
//                String responseString = wsh.inputStreamToString(response.getEntity().getContent()).toString();
//                TheXMLParser parser = new TheXMLParser();
//                UniqueId = parser.getUniqueId(responseString);
//
//
//                Log.e("JSONResponse: ", responseString);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }catch (Exception e){
//
//            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            llProgressLayout.setVisibility(View.GONE);
            String BookingLevelDiscounts = UniqueId.get("BookingLevelDiscounts");
            if(!Utilities.hasData(BookingLevelDiscounts)){
                BookingLevelDiscounts = "0.00";
            }
            tvDiscounts.setText("$"+BookingLevelDiscounts);
            tvPrice.setText("$"+(Float.parseFloat(totalPriceValue)-Float.parseFloat(BookingLevelDiscounts)));

        }
    }

    private void init(){
        edtFirstName = (EditText) findViewById(R.id.edtFirstName);
        edtLastName = (EditText) findViewById(R.id.edtLastName);
        edtAddress = (EditText) findViewById(R.id.edtAddress);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtCity = (EditText) findViewById(R.id.edtCity);
        edtState = (EditText) findViewById(R.id.edtState);
        edtCountry = (EditText) findViewById(R.id.edtCountry);
        edtCVV = (EditText) findViewById(R.id.edtCVV);
        edtCreditCardNumber = (EditText) findViewById(R.id.edtCreditCardNumber);
        edtExpMonth = (EditText) findViewById(R.id.edtExpMonth);
        edtExpYear = (EditText) findViewById(R.id.edtExpYear);
        edtPostalCode = (EditText) findViewById(R.id.edtPostalCode);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        llProgressLayout = (LinearLayout) findViewById(R.id.llProgressLayout);
    }
}
