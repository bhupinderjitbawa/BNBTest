package com.bxs.bnbtest;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import java.util.ArrayList;
import java.util.HashMap;

public class Utilities {

    /////////////////////////////////////////////////////////////////////////
    /// for images
    public static ImageLoaderConfiguration config;
    public static DisplayImageOptions options;

    public static void initializeImageLoaders(Context mContext){
        config = new ImageLoaderConfiguration.Builder(mContext)
                .threadPoolSize(1)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)  // default
                .delayBeforeLoading(0)
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .considerExifParams(false) // default
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .build();
    }

    // check for internet connection
    public static boolean isConnectingToInternet(Context mContext){
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////



    public static void toast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void toast(Context context, String message, int duration){
        Toast.makeText(context, message, duration).show();
    }

    public static void execute(Context context, AsyncTask<Void, Void,Void> asyncTask){
        if(isConnectingToInternet(context)){
            asyncTask.execute();
        } else {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }


    public static boolean hasData(String str){

        if(str!=null){
            if(!str.equals("")&&!str.equals("null"))
                return true;
            else
                return false;
        } else
            return false;
    }

    public static boolean hasData(EditText editText){

        if(editText!=null){
            if(!editText.getText().toString().equals("")&&!editText.getText().toString().equals("null"))
                return true;
            else
                return false;
        } else
            return false;
    }

    public static boolean hasData(HashMap hashMap){

        if(hashMap==null)
            return false;
        else {
            if(hashMap.isEmpty())
                return false;
            else
                return true;
        }
    }

    public static boolean hasData(ArrayList list){

        if(list!=null){
            if(list.size()>0)
                return true;
            else
                return false;
        } else
            return false;
    }


    public static String extractCharacters(String str, int numberOfChars){

        if(str.length()>0) {
            if(str.length()>numberOfChars)
                str = str.substring(0, numberOfChars);
        } else
            str = "";
        return str;
    }

    public static boolean cloneObjects(ArrayList sourceList, ArrayList destinationList){

        if(hasData(sourceList)){

            for(int i=0;i<sourceList.size();i++){
                destinationList.add(sourceList.get(i));
            }
            return  true;
        } else {
            return  false;
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }




    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static void displayImage(Context context, String imageURL, ImageView imageView){
        if(!ImageLoader.getInstance().isInited())
            initializeImageLoaders(context);

        if(hasData(imageURL)){
            ImageLoader.getInstance().displayImage(imageURL, imageView , options, null);
        }
    }



}