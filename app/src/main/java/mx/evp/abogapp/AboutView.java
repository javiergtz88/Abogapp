package mx.evp.abogapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;


public class AboutView extends android.support.v4.app.Fragment {

    private WebView webv;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AboutView newInstance(int sectionNumber) {
        AboutView fragment = new AboutView();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_about_view, container, false);

        String letrasEspañol = "¿?ñáéíóú";

         webv = (WebView) rootView.findViewById(R.id.webView);
        //Volver transparente al WebView
        webv.setBackgroundColor(Color.TRANSPARENT);
        String text = getResources().getString(R.string.AcercaText);
        webv.loadData(text, "text/html; charset=utf-8", "UTF-8");

       return rootView;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainScreenView) activity).onSectionAttached(4);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }


}
