package Presenters;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import mx.appco.appbogado.LoginView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Models.UserModel;

/**
 * Created by saul on 3/16/15.
 */
public class LoginPresenter {


    private UserModel user_model = UserModel.instance();
    private LoginView activity;
    private String TAG = "LoginPresenter";
    private UserLoginTask auth_task;
    private UserFBLoginTask fb_auth_task;

    public LoginPresenter(LoginView arg_activity) {
        activity = arg_activity;
    }


    public void loginClick(){
        auth_task = new UserLoginTask(activity.get_email(), activity.get_password());
        auth_task.execute((Void) null);
    }

    public void auth_check() {
        if(user_model.esta_autenticado())
        {
            activity.call_main_view();
        }
    }

    public void  attemptLogin() {
        if (auth_task != null) {
            return;
        }

        // Reset errors.
        activity.set_error_email(0); //null
        activity.set_error_password(0); //null

        // Store values at the time of the login attempt.
        String email = activity.get_email();
        String password = activity.get_password();

        boolean cancel = false;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password) || password.length() < 8)  {
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
            //ow a progress spinner, and kick off a background task to
            // perform the user login attempt.
            activity.showProgress(true);
            auth_task = new UserLoginTask(email, password);
            auth_task.execute((Void) null);
        }
    }

    /* ### Validation logic ### */
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
        return password.length() > 4;
    }

    public void fb_login(String id, String token) {
        fb_auth_task = new UserFBLoginTask(id, token);
        fb_auth_task.execute();
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String email;
        private final String password;

        UserLoginTask(String arg_email, String arg_password) {
            email = arg_email;
            password = arg_password;
            //Log.d(TAG, "UserLoginTask initated...");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                return UserModel.instance().login(email, password);
            } catch (Exception e){
                //Log.e(TAG, e.getLocalizedMessage());
            }
            return false;
        }

        @Override
        public void onPostExecute(final Boolean success){
            auth_task = null;
            if(success) {
                activity.call_main_view();
            } else{
                activity.showProgress(false);
                activity.alert(UserModel.instance().error_message);
            }
        }

        @Override
        protected void onCancelled(){
            auth_task= null;
        }

    }

    public class UserFBLoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String token;
        private final String id;

        UserFBLoginTask(String id, String token) {
            this.token = token;
            this.id = id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                return UserModel.instance().fb_login(id, token);
            } catch (Exception e){
                Log.e(TAG, e.getLocalizedMessage());
            }
            return false;
        }

        @Override
        public void onPostExecute(final Boolean success){
            fb_auth_task = null;
            if(success) {
                activity.call_main_view();
            } else{
                activity.showProgress(false);
                activity.alert(UserModel.instance().error_message);
            }
        }

        @Override
        protected void onCancelled(){
            fb_auth_task= null;
        }

    }

}
