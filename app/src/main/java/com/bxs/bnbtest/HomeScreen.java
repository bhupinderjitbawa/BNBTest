package com.bxs.bnbtest;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
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
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by bhupinder on 29/3/15.
 */
public class HomeScreen extends Activity {

    private EditText edtAvailabilityDate;
    private EditText edtAvailabilityDateTo;
    private NumberPicker npAdults;
    private NumberPicker npChildren;
    private NumberPicker npNights;
    private Button btnSubmit;
    private ArrayList<GeoModel> geoModelArrayList;
    private Spinner spinner;
    private String arrivalDate = "";
    private String departureDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_home);

        edtAvailabilityDate = (EditText) findViewById(R.id.edtAvailability);
        edtAvailabilityDateTo = (EditText) findViewById(R.id.edtAvailabilityTo);
        npChildren = (NumberPicker) findViewById(R.id.npChildren);
        npAdults = (NumberPicker) findViewById(R.id.npAdults);
        npNights = (NumberPicker) findViewById(R.id.npNights);
        spinner = (Spinner) findViewById(R.id.spinner);
        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String day = "";
        if(dayOfMonth<10)
            day = "0"+dayOfMonth;
        else
            day = ""+dayOfMonth;

        int monthOfYear = (calendar.get(Calendar.MONTH)+1);

        String month = "";
        if(monthOfYear<10)
            month = ""+"0"+monthOfYear;
        else
            month = ""+monthOfYear;
        edtAvailabilityDate.setText(day +"-"+month+"-"+calendar.get(Calendar.YEAR));
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(calendar.get(Calendar.YEAR), Integer.parseInt(month), Integer.parseInt(day));
        gc.add(Calendar.DATE, 1);
        edtAvailabilityDateTo.setText(gc.get(Calendar.DAY_OF_MONTH) +"-"+gc.get(Calendar.MONTH)+"-"+gc.get(Calendar.YEAR));
        arrivalDate = calendar.get(Calendar.YEAR)+month+day;
        departureDate = gc.get(Calendar.YEAR)+gc.get(Calendar.MONTH)+gc.get(Calendar.DAY_OF_MONTH)+"";


        edtAvailabilityDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateText = ((TextView) v).getText().toString();
                int year, month, day;
                String temp[] = dateText.split("-");
                if (temp.length >= 3) {
                    year = Integer.valueOf(temp[2]);
                    month = Integer.valueOf(temp[1]);
                    day = Integer.valueOf(temp[0]);
                } else {
                    Calendar calendar = Calendar.getInstance();
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH) + 1;
                    day = calendar.get(Calendar.DAY_OF_MONTH);

                }
                DatePickerDialog dtpkrdlg = new
                        DatePickerDialog(HomeScreen.this, 0,
                        new DateSetListener(), year, month - 1, day);
                dtpkrdlg.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dtpkrdlg.show();
            }
        });

        edtAvailabilityDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateText = ((TextView) v).getText().toString();
                int year, month, day;
                String temp[] = dateText.split("-");
                if (temp.length >= 3) {
                    year = Integer.valueOf(temp[2]);
                    month = Integer.valueOf(temp[1]);
                    day = Integer.valueOf(temp[0]);
                } else {
                    Calendar calendar = Calendar.getInstance();
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH) + 1;
                    day = calendar.get(Calendar.DAY_OF_MONTH);

                }
                DatePickerDialog dtpkrdlg = new
                        DatePickerDialog(HomeScreen.this, 0,
                        new DateSetListenerTo(), year, month - 1, day);
                dtpkrdlg.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dtpkrdlg.show();
            }
        });

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        npAdults.setMaxValue(30);
        npAdults.setMinValue(1);
        npNights.setMaxValue(30);
        npNights.setMinValue(0);
        npChildren.setMaxValue(30);
        npChildren.setMinValue(0);

        npNights.setValue(1);
        npAdults.setValue(2);
        npChildren.setValue(0);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(geoModelArrayList!=null) {
                    if(Integer.parseInt(departureDate)-Integer.parseInt(arrivalDate)>0) {
                        Intent intent = new Intent(HomeScreen.this, PropertyList.class);
                        intent.putExtra("availabilityDate", "" + arrivalDate);
                        intent.putExtra("availabilityDateTo", "" + departureDate);
                        intent.putExtra("adults", "" + npAdults.getValue());
                        intent.putExtra("children", "" + npChildren.getValue());
                        intent.putExtra("nights", "" + npNights.getValue());
                        intent.putExtra("geo", geoModelArrayList.get(spinner.getSelectedItemPosition()).getId());
                        startActivity(intent);
                    } else {
                        Utilities.toast(getApplicationContext(), "Invalid to date");
                    }
                } else {
                    Utilities.toast(getApplicationContext(), "Please wait until it loads location");
                }
            }
        });
        new GetGeographyList().execute();

    }

    class DateSetListener implements DatePickerDialog.OnDateSetListener{

        @Override
        public void onDateSet(DatePicker view,
                              int year, int monthOfYear,
                              int dayOfMonth) {
            String day = "";
            if(dayOfMonth<10)
                day = "0"+dayOfMonth;
            else
                day = dayOfMonth+"";

            String month = "";
            monthOfYear ++;
            if(monthOfYear<10)
                month = "0"+monthOfYear;
            else
                month = ""+monthOfYear;

            arrivalDate = year+month+day;

            edtAvailabilityDate.setText("" + day + "-" +
                    month + "-" + year);

            GregorianCalendar gc = new GregorianCalendar();
//            gc.set(Calendar.YEAR, year);
//            gc.set(Calendar.MONTH, Integer.parseInt(month));
//            gc.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
            gc.set(year, Integer.parseInt(month), Integer.parseInt(day));
            gc.add(Calendar.DATE, 1);
            edtAvailabilityDateTo.setText(gc.get(Calendar.DAY_OF_MONTH) +"-"+gc.get(Calendar.MONTH)+"-"+gc.get(Calendar.YEAR));
            departureDate = gc.get(Calendar.YEAR)+gc.get(Calendar.MONTH)+gc.get(Calendar.DAY_OF_MONTH)+"";


        }

    }
    class DateSetListenerTo implements DatePickerDialog.OnDateSetListener{

        @Override
        public void onDateSet(DatePicker view,
                              int year, int monthOfYear,
                              int dayOfMonth) {



            String day = "";
            if(dayOfMonth<10)
                day = ""+"0"+dayOfMonth;
            else
                day = ""+dayOfMonth;

            String month = "";
            monthOfYear ++;
            if(monthOfYear<10)
                month = ""+"0"+monthOfYear;
            else
                month = ""+monthOfYear;
            departureDate = year+month+day;
            edtAvailabilityDateTo.setText(""+day+"-"+
                    month+"-"+year);

        }

    }



    private class GetGeographyList extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            ArrayList<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("xml","<CRSRequest><Auth><VendorId>ENiagara</VendorId><VendorPassword>Exp!Niagara34x89</VendorPassword></Auth><Method>GetGeographyList</Method></CRSRequest>"));
            WebServiceHandler wsh = new WebServiceHandler();
//            String responseString = wsh.getWebServiceDataByGET("https://www.bnbmanager.com/ext_crshandler.php?xml=<CRSRequest><Auth><VendorId>ENiagara</VendorId><VendorPassword>Exp!Niagara34x89</VendorPassword></Auth><Method>GetGeographyList</Method></CRSRequest>");
            String responseString = wsh.getWebServiceData("https://www.bnbmanager.com/ext_crshandler.php", params);
            TheXMLParser parser = new TheXMLParser();
            geoModelArrayList = parser.getGeoList(responseString);


//            try {
//                String lineEnd = "\r\n";
//                String twoHyphens = "--";
//                String boundary =  "RQdzAAihJq7Xp1kjraqf";
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
//                dos.writeBytes("<CRSRequest><Auth><VendorId>ENiagara</VendorId><VendorPassword>Exp!Niagara34x89</VendorPassword></Auth><Method>GetGeographyList</Method></CRSRequest>" + lineEnd);
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
//                httpPost.addHeader("Content-Type", "multipart/form-data; boundary="+boundary);
//
//
//                httpPost.setEntity(entity);
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpResponse response = httpclient.execute(httpPost);
//                String responseString = wsh.inputStreamToString(response.getEntity().getContent()).toString();
//                TheXMLParser parser = new TheXMLParser();
//                geoModelArrayList = parser.getGeoList(responseString);
//
//
//                Log.e("JSONResponse: ", responseString);
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (Exception e){
//
//            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            spinner.setAdapter(new GeoAdapter(getApplicationContext(), geoModelArrayList));
        }
    }
}
