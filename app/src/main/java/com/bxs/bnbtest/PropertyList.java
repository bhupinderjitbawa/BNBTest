package com.bxs.bnbtest;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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
public class PropertyList extends Activity {

    private ArrayList<PropertyModel> propertyModelArrayList;
    private ListView listView;
    private String availableDate;
    private String availableDateTo;
    private String children;
    private String adults;
    private String nights;
    private String geoId;
    private LinearLayout llProgressLayout;
    private TextView tvArrivalDate;
    private TextView tvDepartureDate;
    private TextView tvAdults;
    private TextView tvChildren;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);

        listView = (ListView) findViewById(R.id.listView);
        llProgressLayout = (LinearLayout) findViewById(R.id.llProgressLayout);
        tvAdults = (TextView) findViewById(R.id.tvNumberOfAdults);
        tvChildren = (TextView) findViewById(R.id.tvNumberOfChildren);
        tvArrivalDate = (TextView) findViewById(R.id.tvArrivalDate);
        tvDepartureDate = (TextView) findViewById(R.id.tvCheckoutDate);





        availableDate = getIntent().getStringExtra("availabilityDate");
        availableDateTo = getIntent().getStringExtra("availabilityDateTo");
        children = getIntent().getStringExtra("children");
        adults = getIntent().getStringExtra("adults");
        nights = getIntent().getStringExtra("nights");
        geoId = getIntent().getStringExtra("geo");

        tvAdults.setText(adults);
        tvChildren.setText(children);
        tvArrivalDate.setText(availableDate);
        tvDepartureDate.setText(availableDateTo);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(PropertyList.this, RoomList.class);
                intent.putExtra("property", propertyModelArrayList.get(i));
                intent.putExtra("arrivalDate" , getIntent().getStringExtra("availabilityDate"));
                intent.putExtra("departureDate" , getIntent().getStringExtra("availabilityDateTo"));
                intent.putExtra("children" , getIntent().getStringExtra("children"));
                intent.putExtra("adults" , getIntent().getStringExtra("adults"));
                intent.putExtra("nights" , getIntent().getStringExtra("nights"));
                intent.putExtra("geoId" , getIntent().getStringExtra("geo"));

                startActivity(intent);
//                finish();
            }
        });
        new GetAvailabilityTask().execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.bookModel = new BookModel();
    }

    private class GetAvailabilityTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            llProgressLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("xml","<CRSRequest><Auth><VendorId>ENiagara</VendorId><VendorPassword>Exp!Niagara34x89</VendorPassword></Auth><Method>GetAvailability</Method><ArrivalDate>"
                    +availableDate+"</ArrivalDate><NumAdults>"+adults+"</NumAdults><NumChildren>"
                    +children+"</NumChildren><DepartureDate>"
                    +availableDateTo+"</DepartureDate><GeoId>"+geoId+"</GeoId></CRSRequest>" ));

            WebServiceHandler wsh = new WebServiceHandler();
            String responseString = wsh.getWebServiceData("https://www.bnbmanager.com/ext_crshandler.php", parameters);
            TheXMLParser parser = new TheXMLParser();
            propertyModelArrayList = parser.getAvailability(responseString);

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
//                dos.writeBytes("<CRSRequest><Auth><VendorId>ENiagara</VendorId><VendorPassword>Exp!Niagara34x89</VendorPassword></Auth><Method>GetAvailability</Method><ArrivalDate>"
//                        +availableDate+"</ArrivalDate><NumAdults>"+adults+"</NumAdults><NumChildren>"
//                        +children+"</NumChildren><DepartureDate>"
//                        +availableDateTo+"</DepartureDate><GeoId>"+geoId+"</GeoId></CRSRequest>" + lineEnd);
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
//                propertyModelArrayList = parser.getAvailability(responseString);
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
            llProgressLayout.setVisibility(View.GONE);
            listView.setAdapter(new PropertyAdapter(getApplicationContext(), propertyModelArrayList));
        }
    }
}
