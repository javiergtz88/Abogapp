package mx.appco.appbogado;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import mx.appco.appbogado.R;

import java.util.ArrayList;
import java.util.List;

import Presenters.LoginPresenter;


/**
 * A login screen that offers login via email/password.
 */
public class LoginView extends Activity implements LoaderCallbacks<Cursor> {


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    // Presenter
    private LoginPresenter presenter;
    private String TAG = "LoginView";
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log.d(TAG, "onCreate called...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Instantiate presenter
        presenter = new LoginPresenter(this);
        facebook_initialize();
        presenter.auth_check();

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    presenter.loginClick();
                    return true;
                }
                return false;
            }
        });

        // # Eventos de los botones #
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                presenter.attemptLogin();
            }
        });

        Button mRegisterButton = (Button) findViewById(R.id.register_button);


        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                call_register_view();
            }
        });

        /*
        Button mFacebookButton = (Button) findViewById(R.id.facebook_button);
        mFacebookButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
              presenter.click_facebook();
            }
        });
        */

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0);

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }




    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void login_pass(){
        presenter.attemptLogin();
    }
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    public void alert(String error_message) {
        Toast.makeText(this, error_message, Toast.LENGTH_SHORT).show();
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }
    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginView.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    public String get_email(){
        return  mEmailView.getText().toString();
    }
    public String get_password(){

        return mPasswordView.getText().toString()
                ;
    }
    public void set_error_email(int value){
        //0: null, 1: required, 2: invalid
        View focusView = null;
        boolean cancel = false;
        switch (value) {
            case 0:
                mEmailView.setError(null);
                break;
            case 1:
                mEmailView.setError(getString(R.string.error_field_required));
                focusView = mEmailView;
                cancel = true;
                break;
            case 2:
                mEmailView.setError(getString(R.string.error_invalid_email));
                focusView = mEmailView;
                cancel = true;

                break;
        }
        if (cancel)
            focusView.requestFocus();
    }
    public void set_error_password(int value){
        //0: null, 1: required, 2: invalid, 3: incorrect
        View focusView = null;
        boolean cancel = false;
        switch (value) {
            case 0:
                mPasswordView.setError(null);
                break;
            case 1:
                focusView = mPasswordView;
                break;
            case 2:
                mPasswordView.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordView;
                cancel = true;
                break;
            case 3:
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
                cancel = true;
                break;

        }
        if (cancel)
            focusView.requestFocus();
    }

    public void call_main_view(){

        Intent intent = new Intent(this, MainScreenView.class);
        startActivity(intent);
        finish();
    }

    public void call_register_view(){
        Intent intent = new Intent(this, RegisterView.class);
        startActivity(intent);
        finish();
    }

    public void error(){
        Intent intent = new Intent(this, LoginView.class);
        startActivity(intent);
        Toast.makeText(this, "Ha ocurrido un error al conectar al servidor", Toast.LENGTH_LONG).show();
    }

    public void facebook_initialize(){
        loginButton = (LoginButton) findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();

        // Callback registration
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d(TAG, "facebook login wow!" + loginResult.getAccessToken().toString()
                        + ", getAccessToken: " + loginResult.getAccessToken().getToken());
                presenter.fb_login(loginResult.getAccessToken().getUserId(),
                        loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                // App code
                Log.d(TAG, "facebook cancel wow!");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                alert("Error de autenticación");
                Log.d(TAG, "facebook error wow!");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
