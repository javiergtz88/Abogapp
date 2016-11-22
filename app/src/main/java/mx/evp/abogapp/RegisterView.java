package mx.evp.abogapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;

import Presenters.RegisterPresenter;

public class RegisterView extends Activity implements LoaderManager.LoaderCallbacks<Object> {
    private AutoCompleteTextView mNameView;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mRegisterFormView;
    private RegisterPresenter presenter;
    private EditText mCityView;
    private Spinner mStateSpinner;
    private Button RegisterButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load view layout
        setContentView(R.layout.activity_register_view);
        // Load presenter
        presenter = new RegisterPresenter(this);
        // Load name text view
        mNameView = (AutoCompleteTextView) findViewById(R.id.name);
        populateAutoComplete();
        // Load email view
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();
        // Load state spinner
        mStateSpinner = (Spinner) findViewById(R.id.registerStateSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.estados_mexico_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mStateSpinner.setAdapter(adapter);
        // Load city view
        mCityView = (EditText) findViewById(R.id.registerCityTextView);
          // Load password view
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    presenter.attemptRegister();
                    return true;
                }
                return false;
            }
        });
        // Load register button
        RegisterButton = (Button) findViewById(R.id.register_button);
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validacionCorrecta()) {
                presenter.attemptRegister();
                } else {

                }
            }
        });
        mRegisterFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }



    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    // Getters/Setters
    public String get_name() {
        return  mNameView.getText().toString();
    }
    public String get_email(){
        return mEmailView.getText().toString();
    }
    public String get_password(){
        return mPasswordView.getText().toString();
    }
    public String get_state(){
        return mStateSpinner.getSelectedItem().toString();
    }
    public String get_city(){
        return mCityView.getText().toString();
    }

    public void set_error_name(int id){
        null_required_invalid_error(mNameView, id);
    }

    public void set_error_city(int id){
        null_required_invalid_error(mCityView, id);
    }
    public void set_error_email(int id){
        null_required_invalid_error(mEmailView, id);
    }

    private void null_required_invalid_error(TextView a, int id){
        //0: null, 1: required, 2: invalid
        View focusView = null;
        boolean cancel = false;
        switch (id) {
            case 0:
                a.setError(null);
                break;
            case 1:
                a.setError(getString(R.string.error_field_required));
                focusView = a;
                cancel = true;
                break;
            case 2:
                a.setError(getString(R.string.error_invalid_field));
                focusView = a;
                cancel = true;
                break;
            case 3:
                validaFormatoMail();
                focusView = a;
                cancel = true;
                break;

        }
        if (cancel)
            focusView.requestFocus();
    }

    public void set_error_password(int id){
        //0: null, 1: required, 2: invalid, 3: incorrect
        View focusView = null;
        boolean cancel = false;
        switch (id) {
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
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {

        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    public void call_main_view(){
        Intent intent = new Intent(this, MainScreenView.class);
        startActivity(intent);
        finish();
    }

    public void error(){
        Intent intent = new Intent(this, LoginView.class);
        startActivity(intent);
        Toast.makeText(this,"Ha ocurrido un error al conectar al servidor",Toast.LENGTH_LONG).show();
    }


    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {
    }
    private Boolean validacionCorrecta(){
        if(mPasswordView.getText().toString().equalsIgnoreCase("")){
            mPasswordView.setError("Debe ingresar su contraseña.");
            return false;
        }
        if(mCityView.getText().toString().equalsIgnoreCase("")){
            mCityView.setError("Debe ingresar su ciudad.");
            return false;
        }
        if(mNameView.getText().toString().equalsIgnoreCase("")){
            mNameView.setError("Debe ingresar su nombre.");
            return false;
        }
        if(mPasswordView.getText().length() <8){
            mPasswordView.setError("Longitud no válida, debe ser mínimo de 8 caracteres.");
        }
        









        return true;
    }

    private void validaFormatoMail(){
           mEmailView.addTextChangedListener(new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (mEmailView.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+") && s.length() > 0)
            {

            }
            else
            {
                mPasswordView.setError(getString(R.string.error_invalid_field));
            }
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
    });

    }
}
