package com.bxs.bnbtest;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bnb);

        new TestWebservice().execute();

    }
    private class TestWebservice extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("xml","<CRSRequest><Auth><VendorId>ENiagara</VendorId><VendorPassword>Exp!Niagara34x89</VendorPassword></Auth><Method>GetAvailability</Method></CRSRequest>"));

            WebServiceHandler wsh = new WebServiceHandler();
            try {
                    String lineEnd = "\r\n";
                    String twoHyphens = "--";
                    String boundary =  "RQdzAAihJq7Xp1kjraqf";

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    DataOutputStream dos = new DataOutputStream(baos);

                    // Send parameter #1
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"xml\"" + lineEnd);
                    dos.writeBytes("Content-Type: text/plain; charset=US-ASCII" + lineEnd);
                    dos.writeBytes("Content-Transfer-Encoding: 8bit" + lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes("<CRSRequest><Auth><VendorId>ENiagara</VendorId><VendorPassword>Exp!Niagara34x89</VendorPassword></Auth><Method>GetLodgingTypes</Method></CRSRequest>" + lineEnd);

                    dos.flush();
                    dos.close();

                    ByteArrayInputStream content = new ByteArrayInputStream(baos.toByteArray());
                    BasicHttpEntity entity = new BasicHttpEntity();
                    entity.setContent(content);

                    HttpPost httpPost = new HttpPost("https://www.bnbmanager.com/ext_crshandler.php");
                    httpPost.addHeader("Connection", "Keep-Alive");
                    httpPost.addHeader("Content-Type", "multipart/form-data; boundary="+boundary);


                    httpPost.setEntity(entity);
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpResponse response = httpclient.execute(httpPost);
                    String responseString = wsh.inputStreamToString(response.getEntity().getContent()).toString();
                    Log.i("JSONResponse: ", responseString);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    public static String requestUrl(String url, String postParameters)
             {
        if (Log.isLoggable("TAG", Log.INFO)) {
            Log.i("TAG", "Requesting service: " + url);
        }


        HttpURLConnection urlConnection = null;
        try {
            // create connection
            URL urlToRequest = new URL(url);
            urlConnection = (HttpURLConnection) urlToRequest.openConnection();
            urlConnection.setConnectTimeout(3000);
            urlConnection.setReadTimeout(5000);

            // handle POST parameters
            if (postParameters != null) {

                if (Log.isLoggable("TAG", Log.INFO)) {
                    Log.i("TAG", "POST parameters: " + postParameters);
                }

                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setFixedLengthStreamingMode(
                        postParameters.getBytes().length);
                urlConnection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");

                //send the POST out
                PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                out.print(postParameters);
                out.close();
            }

            // handle issues
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                // throw some exception
            }

            // read output (only for GET)
            if (postParameters != null) {
                return null;
            } else {
                InputStream in =
                        new BufferedInputStream(urlConnection.getInputStream());
                return getStringFromInputStream(in);
            }


        } catch (MalformedURLException e) {
            // handle invalid URL
        } catch (SocketTimeoutException e) {
            // hadle timeout    
        } catch (IOException e) {
            // handle I/0
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }


}
