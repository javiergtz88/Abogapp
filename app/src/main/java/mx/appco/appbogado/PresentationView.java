package mx.appco.appbogado;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
//import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;

import com.facebook.appevents.AppEventsLogger;

import java.util.HashMap;

import Models.ContentModel;
import Presenters.ContentsPresenter;
import mx.appco.appbogado.R;


public class PresentationView extends ActionBarActivity implements SomeView {

    private static final String TAG = "PresentationView";
    private WebView webv;
    private Button detailGetPremiumButton;
    private ImageButton buttonActionBarBack;
    private ContentsPresenter presenter;
    private HashMap<String, Object> item_hash;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation_view);
        progressDialog = ProgressDialog.show(PresentationView.this, "", "Espere un momento...");
        presenter = new ContentsPresenter(this);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        //ActionBar personalizado para los botones
        View mActionBarView = getLayoutInflater().inflate(R.layout.actiobar_presentationview_custome, null);
        getSupportActionBar().setCustomView(mActionBarView);
        getSupportActionBar().setDisplayOptions(getSupportActionBar().DISPLAY_SHOW_CUSTOM);
        bar_actionBar_button();


        Intent intent = getIntent();
        item_hash = (HashMap<String, Object>)intent.getSerializableExtra("item_hash");
        presenter.get_scenario();




        detailGetPremiumButton = (Button) findViewById(R.id.preguntasGoactivity);
        detailGetPremiumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("premium", true);
                setResult(1, intent);
                finish();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_presentation_view, menu);
        return true;
    }

    public void bar_actionBar_button() {

        buttonActionBarBack = (ImageButton) findViewById(R.id.buttonActionBarPresentationBack);

        buttonActionBarBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();

            }

        });

    }


    @Override
    public HashMap get_data() {
        return item_hash;
    }

    @Override
    public void set_data(HashMap a) {

       //Log.d(TAG,a.get("scenario").toString());

        System.out.println(a);

        webv = (WebView) findViewById(R.id.webView);
        //Volver transparente al WebView
        webv.setBackgroundColor(Color.TRANSPARENT);
        webv.loadData((String) a.get("scenario"), "text/html; charset=utf-8", "UTF-8");
        //System.out.println("Esto contiene el WebView"+ a.get("content"));


    }

    @Override
    public void alert(String s) {

    }

    @Override
    public void dismis_progress() {
        progressDialog.dismiss();

    }

    @Override
    public void add(HashMap params) {

    }

    @Override
    public void add(ContentModel content) {

    }

}
