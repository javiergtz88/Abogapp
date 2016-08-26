package Presenters;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import mx.appco.appbogado.RegisterView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Models.UserModel;

/**
 * Created by roblero on 30/03/15.
 */
public class RegisterPresenter {
    RegisterView activity;
    private user_register_task mRegTask = null;
    private String TAG = "RegisterPresenter";

    public RegisterPresenter(RegisterView arg_activity) {
        activity = arg_activity;
    }


    public void attemptRegister() {
        if (mRegTask != null) {
            return;
        }

        // Reset errors.
        activity.set_error_name(0); //null
        activity.set_error_email(0);
        activity.set_error_city(0);
        activity.set_error_password(0); //null

        // Store values at the time of the login attempt.
        String name = activity.get_name();
        String email = activity.get_email();
        String password = activity.get_password();
        String city = activity.get_city();
        String state = activity.get_state();

        boolean cancel = false;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password) || password.length() < 8) {
            activity.set_error_password(2); //invalid
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            activity.set_error_email(1); //required
            cancel = true;
        } else if (!isEmailValid(email)) {
            activity.set_error_email(2); //invalid
            cancel = true;
        }

        if (!cancel) {
            // There was not an error; attempt login.
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            activity.showProgress(true);
            mRegTask = new user_register_task(name, email, password, city, state);
            mRegTask.execute((Void) null);
        }
    }



    private boolean isEmailValid(String email) {
        boolean isValid = false;

        String formato = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(formato, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 8;
    }

    public void click_facebook() {
        //Log.i("registerPresenter", "boton de facebook");
    }

    public class user_register_task extends AsyncTask<Void, Void, Boolean> {
        private final String name;
        private final String email;
        private final String password;
        private final String city;
        private final String state;

        user_register_task(String arg_name,String arg_email, String arg_password, String arg_city,
                           String arg_state) {
            name = arg_name;
            email = arg_email;
            password = arg_password;
            city = arg_city;
            state = arg_state;
            //Log.i(TAG, "user_register_task created...");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                if (UserModel.instance().register(name, email, password, state, city)) return true;
            } catch (Exception e){
                e.printStackTrace();
                //Log.e(TAG, "Register task error: " + e.getLocalizedMessage());
            }
            return false;
        }

        @Override
        public void onPostExecute(final Boolean success){
            mRegTask = null;
            if(success) {
                activity.call_main_view();
            } else{
               activity.error();
            }
        }

        @Override
        protected void onCancelled(){
            mRegTask= null;
        }
    }

}



