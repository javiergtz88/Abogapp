package mx.evp.abogapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.HashMap;

import Models.ContentModel;
import Presenters.ContentsPresenter;
import Presenters.MainScreenPresenter;


public class MainScreenSubView extends android.support.v4.app.Fragment implements SomeView {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = "MainScreenSubView";
    private ListView mainScenariosListView;
    private MainScreenPresenter presenter;
    private EditText mainBuscarTextField;
    private ImageButton mainSearchButton;
    private ContentsArrayAdapter adapter;



    private ContentsPresenter presentador;
    private ProgressDialog progressDialog;
    private EndlessScrollListener scroll_listener;


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainScreenSubView newInstance(int sectionNumber) {
        MainScreenSubView fragment = new MainScreenSubView();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        presentador = new ContentsPresenter(this);
        View rootView = inflater.inflate(R.layout.fragment_main_screen_sub_view, container, false);

        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //Botones de Premium y Buscar
        mainSearchButton = (ImageButton) rootView.findViewById(R.id.mainSearchButton);
        mainSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();
                presentador.search();
                hide_keyboard();

            }
        });

        //Campo de texto y ListView para buscar en la list_element_scenario
        mainScenariosListView = (ListView) rootView.findViewById(R.id.mainScenariosListView);
        mainScenariosListView.getFirstVisiblePosition();
      //  mainScenariosListView.setStackFromBottom(true);
        //mainScenariosListView.setTextFilterEnabled(true);
        ////Log.d(TAG,"FINAL DEL LISTVIEW");
       // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mainBuscarTextField = (EditText) rootView.findViewById(R.id.mainBuscarTextField);
        mainBuscarTextField.setFocusable(true);
        mainBuscarTextField.setCompoundDrawables(null, null, getResources().getDrawable(R.drawable.abc_ic_search_api_mtrl_alpha), null);
        //Agrega o muestra elementos a la list_element_scenario según Strings.xml
        mainScenariosListView = (ListView) rootView.findViewById(R.id.mainScenariosListView);
        mainScenariosListView.setTextFilterEnabled(true);
        //Agrega elementos a la list_element_scenario
        adapter = new ContentsArrayAdapter(getActivity(), R.layout.list_element_scenario);
        load_local_scenarios();

        scroll_listener = new EndlessScrollListener(presentador, 1);
        mainScenariosListView.setOnScrollListener(scroll_listener);
        mainScenariosListView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);

        /*
        //Aquí hace el filtro del Editext de la búsqueda
        mainBuscarTextField.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                //MainScreenSubView.this.adapter.getFilter().filter(arg0);
                adapter.getFilter().filter(arg0.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });*/

        mainScenariosListView.setAdapter(adapter);

        mainScenariosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                call_show_content(adapter.getItem(position));

            }


        });

        return rootView;
    }


    private void load_local_scenarios() {
        Resources res= getResources();
        int i = 0;
        for( String titulo : res.getStringArray(R.array.titulos)){
            HashMap hash = new HashMap<>();
            hash.put("title", titulo);
            hash.put("remote", false);
            hash.put("id", i);
            i++;
            adapter.add(hash);


        }
    }

    private void hide_keyboard(){
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(mainBuscarTextField.getWindowToken(), 0);
    }

    @Override
    public void onPause() {
        super.onPause();
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainBuscarTextField.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mainSearchButton.getWindowToken(),0);

    }

    @Override
    public HashMap get_data() {
        HashMap<String, Object> hash = new HashMap<>();
        hash.put("search", mainBuscarTextField.getText().toString());


        return hash;
    }

    @Override
    public void set_data(HashMap a) {


    }

    @Override
    public void alert(String s) {

    }

    @Override
    public void dismis_progress() {

    }

    @Override
    public void add(HashMap params) {
        adapter.add(params);
    }

    @Override
    public void add(ContentModel content) {

    }


    public void call_fragment(int position) {
        FragmentManager fragmentManager = getFragmentManager();

        switch (position) {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new ChatView())
                        .commit();
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainScreenView) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void add_item(){
        //list.add();
    }

    public void call_mi_pyme(){
        call_fragment(1);
    }

    public void call_premium(){
        call_fragment(0);
    }

    public void call_show_content(HashMap hash){
        Intent intent = new Intent(getActivity(), PresentationView.class);
        intent.putExtra("item_hash", hash);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.d(TAG, "on activity resume: " + String.valueOf(resultCode));
        if (resultCode == 1)
            call_premium();
    }














 }

