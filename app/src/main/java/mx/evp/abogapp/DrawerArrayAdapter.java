package mx.evp.abogapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by appco on 2/09/15.
 */
public class DrawerArrayAdapter extends BaseAdapter {
    List<HashMap> mItems;
    Context mContext;

    public DrawerArrayAdapter(Context context, List<HashMap> mItems) {
        this.mItems = mItems;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_list_drawermenu, null);
        }

        TextView tvTitle = (TextView) convertView.findViewById(R.id.menuitem);

        ImageView logo = (ImageView) convertView.findViewById(R.id.logo);


        HashMap item = mItems.get(position);

        tvTitle.setText((String) item.get("title"));

        return convertView;
    }
}