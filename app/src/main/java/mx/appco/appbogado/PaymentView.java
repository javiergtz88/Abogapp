package mx.appco.appbogado;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;

import java.util.HashMap;

import Models.ContentModel;
import Presenters.PayPresenter;

public class PaymentView extends Activity implements SomeView {

    private EditText paymentNameEditText;
    private Spinner paymentCardTypeSpinner;
    private EditText paymentSecNumberEditText;
    private PayPresenter presenter;
    private Spinner paymentDateMonthSpinner;
    private Spinner paymentDateYearSpinner;
    private String premium;
    public Integer credits;
    private EditText paymentCardNumberEditText;
    private EditText paymentLastNameEditText;
    private Button AcceptButton,asasas;
    private static final String TAG = "PaymentView";
    private ProgressDialog progressDialog;





    /******************************
     * View life cycle
     *******************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set view
        setContentView(R.layout.activity_payment_view);
        // load Conekta class
        presenter = new PayPresenter(this);
        // get intent data
        Intent intent = getIntent();
        premium = intent.getStringExtra("premium");
        credits = intent.getIntExtra("credits", 0);
        // UI elements
        // Card type spinner
        paymentCardTypeSpinner = (Spinner) findViewById(R.id.paymentCardTypeSpinner);
        paymentCardTypeSpinner.setPrompt("Tipo de tarjeta");
        ArrayAdapter<CharSequence> card_type_adapter = ArrayAdapter.createFromResource(this,
                R.array.premium_card_type, android.R.layout.simple_spinner_item);
        card_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentCardTypeSpinner.setAdapter(card_type_adapter);
        // Date month spinner
        paymentDateMonthSpinner = (Spinner) findViewById(R.id.paymentDateMonthSpinner);
        paymentDateMonthSpinner.setPrompt("Mes de vigencia");
        ArrayAdapter<CharSequence> date_month_adapter = ArrayAdapter.createFromResource(this,
                R.array.year_moths, android.R.layout.simple_spinner_item);
        date_month_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentDateMonthSpinner.setAdapter(date_month_adapter);
        // Date year spinner
        paymentDateYearSpinner = (Spinner) findViewById(R.id.paymentDateYearSpinner);
        paymentDateYearSpinner.setPrompt("Año de vigencia");
        ArrayAdapter<CharSequence> date_year_adapter = ArrayAdapter.createFromResource(this,
                R.array.card_years, android.R.layout.simple_spinner_item);
        date_month_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentDateYearSpinner.setAdapter(date_year_adapter);
        // Edit texts
        paymentSecNumberEditText = (EditText) findViewById(R.id.paymentSecNumberEditText);


        paymentNameEditText = (EditText) findViewById(R.id.paymentNameEditText);


        paymentCardNumberEditText = (EditText) findViewById(R.id.paymentCardNumberEditText);


        paymentLastNameEditText = (EditText) findViewById(R.id.paymentLastNameEditText);


        //Íconos y fondo de los EditText de Payment
        paymentNameEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_paymentname, 0);
        paymentLastNameEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_paymentname, 0);
        paymentCardNumberEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_paymentcredit, 0);
        paymentSecNumberEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_paymentcredit, 0);

        //ProgressBar al haber hecho la compra


        AcceptButton = (Button) findViewById(R.id.acceptData_button);

        AcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate_data()) {
                    progresDialog();
                     click_event();
                }
            }
        });

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

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_payment_view, menu);
        return true;
    }

    /******************************
     * Events
     *******************************/

    private void click_event() {
        if (credits > 0)
           presenter.buy_credits();
    }


    /******************************
     * Setters/Getters
     *******************************/
    @Override
    public HashMap get_data() {
        validate_data();
        if (validate_data()) {
            HashMap<String, Object> a_hash = new HashMap<>();
            a_hash.put("amount", credits);
            a_hash.put("type", paymentCardTypeSpinner.getSelectedItem().toString());
            a_hash.put("expire_month", paymentDateMonthSpinner.getSelectedItem().toString());
            a_hash.put("expire_year", paymentDateYearSpinner.getSelectedItem().toString());
            a_hash.put("cvv2", paymentSecNumberEditText.getText().toString());
            a_hash.put("first_name", paymentNameEditText.getText().toString());
            a_hash.put("user_phone", paymentLastNameEditText.getText().toString());
            a_hash.put("number", paymentCardNumberEditText.getText().toString());/*.replaceAll("\\s+",""))*/;
            return a_hash;
        } else
            return new HashMap();
    }



    @Override
    public void set_data(HashMap a) {
        if(a.containsKey("payment_success")){
            if ((boolean) a.get("payment_success")){
                is_payment_ok(true);
            }
        }
    }

    public void is_payment_ok(boolean result){
        if(result) {
            Intent intent = new Intent();
            intent.putExtra("is_payment_ok", true);
            setResult(1, intent);
        }
        finish();
    }

    @Override
    public void alert(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    /******************************
     * Validations
     *******************************/
    private boolean validate_data() {
        boolean cancel = false;
        cancel = cancel || validate_cardType();
        cancel = cancel || validate_Name();
        cancel = cancel || validate_last_Name();
        cancel = cancel || validate_secNumber();
        cancel = cancel || validate_date();
        cancel = cancel || validate_card_number();
        return !cancel;
    }

    public boolean validate_cardType() {
        return false;
    }

    public boolean validate_Name() {
        View focusView = null;
        boolean cancel = false;

        paymentNameEditText.setError(null);
        if (TextUtils.isEmpty(paymentNameEditText.getText())) {
            paymentNameEditText.setError(getString(R.string.error_field_name_payment));
            focusView = paymentNameEditText;
            cancel = true;
        } else {
            paymentNameEditText.setError(null);
        }
        if (cancel)
            focusView.requestFocus();
        return cancel;
    }

    private boolean validate_last_Name() {
        View focusView = null;
        boolean cancel = false;

        paymentLastNameEditText.setError(null);
        if (TextUtils.isEmpty(paymentLastNameEditText.getText())) {
            paymentLastNameEditText.setError(getString(R.string.error_field_address_payment));
            focusView = paymentLastNameEditText;
            cancel = true;
        } else {
            paymentNameEditText.setError(null);
        }
        if (cancel)
            focusView.requestFocus();
        return cancel;
    }

    public boolean validate_secNumber() {
        View focusView = null;
        boolean cancel = false;

        paymentSecNumberEditText.setError(null);
        if (TextUtils.isEmpty(paymentSecNumberEditText.getText())) {
            paymentSecNumberEditText.setError(getString(R.string.error_field_Secnumber_payment));
            focusView = paymentSecNumberEditText;
            cancel = true;
        } else {
            paymentSecNumberEditText.setError(null);
        }
        if (cancel)
            focusView.requestFocus();
        return cancel;
    }

    public boolean validate_card_number() {
        View focusView = null;
        boolean cancel = false;

        paymentCardNumberEditText.setError(null);
        if (TextUtils.isEmpty(paymentCardNumberEditText.getText())) {
            paymentCardNumberEditText.setError(getString(R.string.error_field_number_payment));
            focusView = paymentCardNumberEditText;
            cancel = true;
        } else {
            paymentCardNumberEditText.setError(null);
        }
        if (paymentCardNumberEditText.getText().toString().replaceAll("\\s+","").length() != 16) {
            paymentCardNumberEditText.setError("Longitud no válida");
            focusView = paymentCardNumberEditText;
            cancel = true;
        }
        if (cancel)
            focusView.requestFocus();
        return cancel;
    }

    private void progresDialog() {
        //dialogo de espera
        progressDialog = ProgressDialog.show(PaymentView.this, "", "Espere por favor...");
    }

    public void dismis_progress(){
        progressDialog.dismiss();
    }

    @Override
    public void add(HashMap params) {

    }

    @Override
    public void add(ContentModel content) {

    }

    public boolean validate_date(){
        return false;
    }

}
