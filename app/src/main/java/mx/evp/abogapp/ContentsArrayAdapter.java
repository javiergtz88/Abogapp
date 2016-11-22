package mx.evp.abogapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by saul on 8/29/15.
 */
public class ContentsArrayAdapter extends ArrayAdapter<HashMap> implements Filterable {

    private final Context context;
    private TextView txtTitulo;

    public ContentsArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    @Override
    public void add(HashMap hash) {
        super.add(hash);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HashMap chatMessageObj = getItem(position);
        LayoutInflater inflater = (LayoutInflater) this.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_element_scenario, parent, false);

        txtTitulo = (TextView) rowView.findViewById(R.id.mainListScenarioTitleTextView);
        txtTitulo.setText((String) chatMessageObj.get("title"));


        return rowView;
    }


}
