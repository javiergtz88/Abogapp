package mx.appco.appbogado;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;

import mx.appco.appbogado.R;

import java.util.HashMap;

import Models.ContentModel;
import Presenters.ChatPresenter;
import Presenters.PayPresenter;

public class MessageFormView extends ActionBarActivity implements SomeView {

    private static final String TAG = "MessageFormView";
    private Spinner chatCategoriesSpinner;
    private Spinner chatCreditsSpinner;
    private ChatPresenter presentador;
    private Button enviar;
    private ProgressDialog progressDialog;
    private EditText mensaje;
    private ImageButton buttonActionBarBack;
    private TextView creditosTv;
    private TextView prioridadTv;
    private TextView categoriaTv;
    private PayPresenter presentador2;
    private TextView chatCreditsTextView;
    private TextView caracteresTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
      //ActionBar personalizado para los botones
        View mActionBarView = getLayoutInflater().inflate(R.layout.actiobar_message_form, null);
        getSupportActionBar().setCustomView(mActionBarView);
        getSupportActionBar().setDisplayOptions(getSupportActionBar().DISPLAY_SHOW_CUSTOM);
        bar_actionBar_button();

        setContentView(R.layout.activity_message_form_view);
        presentador = new ChatPresenter(this);
        presentador2 = new PayPresenter(this);

        Intent intent = getIntent();
        Integer content_index = intent.getIntExtra("id", 0);

        enviar  = (Button) findViewById(R.id.chatSendButton);
        enviar.setTypeface(AppLeyesApplication.getTypeface(this, "fontawesome-webfont.ttf"));
        enviar.setText(Html.fromHtml("&#xf1d8;"));


        // set enviarOnKeyListener listeners
        View.OnKeyListener enviarOnKeyListener = new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //Log.d(TAG, "enviarOnKeyListener");
                    if (is_data_valid())
                        presentador.send_message();
                    return true;
                }
                return false;
            }
        };
        enviar.setOnKeyListener(enviarOnKeyListener);

        // set enviarClickListener listeners
        View.OnClickListener enviarClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //presentador.chat_test();
                if (is_data_valid()) {
                    presentador.send_message();
                    //dialogo de espera del mensaje enviado
                    progressDialog = ProgressDialog.show(MessageFormView.this, "", "Eviando mensaje...");
                }
            }

        };


        enviar.setOnClickListener(enviarClickListener);

        mensaje = (EditText) findViewById(R.id.chatFMsgTextView);
        creditosTv = (TextView) findViewById(R.id.creditosTv);
        prioridadTv = (TextView) findViewById(R.id.prioridadTv);
        categoriaTv = (TextView) findViewById(R.id.categoriaTv);

        //Contador de caracteres
        InputFilter[] filtro = new InputFilter[1];
        filtro[0] = new InputFilter.LengthFilter(1200);
        mensaje .setFilters(filtro);
        textView_contador();




        // Load categories spinner
        chatCategoriesSpinner = (Spinner) findViewById(R.id.chatCategoriesSpinner);
                ArrayAdapter<CharSequence> adapterCategories = ArrayAdapter.createFromResource(this,
                R.array.chat_categories, R.layout.spinner_item_customized);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chatCategoriesSpinner.setAdapter(adapterCategories);

        // Load credits spinner
        chatCreditsSpinner = (Spinner) findViewById(R.id.chatCreditsSpinner);
        ArrayAdapter<CharSequence> adapterCredits = ArrayAdapter.createFromResource(this,
                R.array.chat_credits, R.layout.spinner_item_customized);
        adapterCredits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chatCreditsSpinner.setAdapter(adapterCredits);

        // Credits text view
        chatCreditsTextView = (TextView) findViewById(R.id.chatCreditsTextView);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message_form_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    /* Validators */
    private boolean is_data_valid(){
        boolean valid = true;
        mensaje.setError(null);
        if( mensaje.length() < 1) {
            mensaje.setError(getString(R.string.error_message_missing));
            valid = false;
        }
        if( !presentador.loaded()){
            valid = false;
        }
        if(chatCreditsSpinner.getSelectedItemPosition() == 0){
            alert("Selecciona los créditos");
            valid = false;
        }
        if(chatCategoriesSpinner.getSelectedItemPosition() == 0){
            alert("Selecciona la categoría");
            valid = false;
        }
        // Check for credits left!
        return valid;
    }

    @Override
    public HashMap get_data() {
        return null;
    }

    @Override
    public void set_data(HashMap a) {

        if(a.containsKey("credits_left")){
            chatCreditsTextView.setText((String) a.get("credits_left")); }
    }

    public void alert(String error_message) {
        Toast.makeText(this, error_message, Toast.LENGTH_SHORT).show();
    }


    /* Getters */
    public String get_msg_text(){
        //Log.d("ChatView", "get_msg_text: " + mensaje.getText().toString());
        return mensaje.getText().toString();
    }

    public int get_msg_priority() {
        Integer credit_values[] = {null, 25, 75, 225, 500};
        return credit_values[chatCreditsSpinner.getSelectedItemPosition()];
    }

    public int get_msg_category() {
        return chatCategoriesSpinner.getSelectedItemPosition();
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

    public void is_sent_ok(boolean result){
        if(result) {
            Intent intent = new Intent();
            intent.putExtra("is_sent_ok", true);
            setResult(1, intent);
        }
        finish();
    }

    public void bar_actionBar_button() {

        buttonActionBarBack = (ImageButton) findViewById(R.id.buttonActionBarBack);

        buttonActionBarBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();

            }

        });

    }

    private void textView_contador() {
        caracteresTv = (TextView) findViewById(R.id.caracteres);

        mensaje.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // this will show characters remaining
                caracteresTv.setText(1200 - s.toString().length() + "/1200");


            }
        });

    }
}
