package Models;

import android.util.Log;

import mx.appco.appbogado.AppLeyesApplication;
import mx.appco.appbogado.R;

/**
 * Created by saul on 9/6/15.
 */
public class ErrorCluster {

    private static final String TAG = "ErrorCluster";

    public static String get_error_message(int e_code){
        String[] error_codes_array = AppLeyesApplication.getAppContext()
                .getResources().getStringArray(R.array.error_codes);

        //Log.e(TAG, "error ID: " + String.valueOf(e_code));
        for(String error : error_codes_array){
            String[] splited_temp = error.split(":");
            if(Integer.valueOf(splited_temp[0]) == e_code){
                //Log.e(TAG, "error message: " + splited_temp[1]);
                return splited_temp[1];
            }
        }
        return null;
    }
}
