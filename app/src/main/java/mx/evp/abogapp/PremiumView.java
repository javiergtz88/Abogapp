package mx.evp.abogapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import Models.ContentModel;
import Presenters.PayPresenter;

public class PremiumView extends Fragment implements SomeView {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private TextView premiumCreditsLeftTextView;
    private Spinner spinnerCreditsChoose;
    private TextView premiumCreditsDescTextview;
    private TextView cantidadTextview;
    private PayPresenter presenter;
    private Button premiumGetCreditsButton;
    private static final String TAG = "PremiumView";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_premium_view, container, false);
        presenter = new PayPresenter(this);

        //Spinner para elegir el número de créditos a comprar
        spinnerCreditsChoose = (Spinner) rootView.findViewById(R.id.spinnerCreditsChoose);

        ArrayAdapter<CharSequence> card_type_adapter = ArrayAdapter.createFromResource(getActivity(),
        R.array.Credits, R.layout.spinner_item_customized);
        card_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCreditsChoose.setAdapter(card_type_adapter);
        spinnerCreditsChoose.setPrompt("Comprar");
        //Botón para pasar a la actividad de PaymentView



        premiumGetCreditsButton  = (Button) rootView.findViewById(R.id.premiumGetCreditsButton);
        View.OnClickListener getCreditsClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_data_valid()) {
                    payment_activity();
                }
            }
        };
        premiumGetCreditsButton.setOnClickListener(getCreditsClickListener);


        premiumCreditsLeftTextView = (TextView) rootView.findViewById(R.id.premiumCreditsLeftTextView);
        premiumCreditsDescTextview = (TextView) rootView.findViewById(R.id.premiumCreditsDescTextView);
        cantidadTextview = (TextView) rootView.findViewById(R.id.cantidadTextview);


        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainScreenView) activity).onSectionAttached(3);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public HashMap get_data() {
        return null;
    }

    @Override
    public void set_data(HashMap a) {

        if(a.containsKey("credits_left")){
            premiumCreditsLeftTextView.setText((String) a.get("credits_left")+"\n pesos"); }
    }

    @Override
    public void alert(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void dismis_progress() {
        // Stub method
    }

    @Override
    public void add(HashMap params) {

    }

    @Override
    public void add(ContentModel content) {

    }

    private boolean is_data_valid(){
        boolean valid = true;

        if(spinnerCreditsChoose.getSelectedItemPosition() == 0){
            alert("Selecciona los créditos que deseas");
            valid = false;
            spinnerCreditsChoose.setFocusable(true);

        }
        // Check for credits left!
        return valid;
    }

    private void payment_activity(){
        Integer buy_credit_values[] = {null, 100, 500, 1000, 1500};
        Intent intent = new Intent(getActivity(), PaymentView.class);
        intent.putExtra("credits", buy_credit_values[spinnerCreditsChoose.getSelectedItemPosition()]);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.d(TAG, "on activity resume: " + String.valueOf(resultCode));
        if (resultCode == 1){
            presenter = new PayPresenter(this);
        }
    }



}

