package developer.montero.michael.com.popularmovies.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Michael A. Montero on 31-Aug-17.
 */

public class Commons {

    //This function check if the device is connected to any network
    public static Boolean isConnected(Context context){
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isConected = networkInfo != null && networkInfo.isConnectedOrConnecting();
        return  isConected;
    }
}
