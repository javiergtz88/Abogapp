package mx.evp.abogapp;

/**
 * Created by roblero on 14/04/15.
 */

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

class ChatArrayAdapter extends ArrayAdapter<HashMap> {

    private Context context;
    private TextView chatText;
    private LinearLayout contenedor;
    private List<HashMap> chatMessageList = new ArrayList<HashMap>();
    private static final String TAG = "ChatArrayAdapter";
    private TextView chatName;
    private TextView chatTimeDate;
    private TextView chatIcon;


    @Override
    public void add(HashMap hash) {
        chatMessageList.add(hash);
        super.add(hash);
    }

    @Override
    public void clear(){
        chatMessageList.clear();
        super.clear();
    }

    public ChatArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.chatMessageList.size();
    }

    @Override
    public HashMap getItem(int index) {
        return this.chatMessageList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HashMap chatMessageObj = getItem(position);
        LayoutInflater inflater = (LayoutInflater) this.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_element_chat_msg, parent, false);

        contenedor = (LinearLayout) rowView.findViewById(R.id.contenedor);

        chatIcon = (TextView) rowView.findViewById(R.id.chatIconTextView);
        chatIcon.setTypeface(AppLeyesApplication.getTypeface(context, "fontawesome-webfont.ttf"));
        chatIcon.setText(Html.fromHtml("&#xf075;").toString());

        chatText = (TextView) rowView.findViewById(R.id.chatMessageTextTextView);
        chatText.setText((String) chatMessageObj.get("message_text"));

        chatName = (TextView) rowView.findViewById(R.id.chatMessageNameTextView);
        chatName.setText((String) chatMessageObj.get("sender"));

        chatTimeDate = (TextView) rowView.findViewById(R.id.chatMessageTimeDateTextView);
        String timestamp = format_date((String) chatMessageObj.get("timestamp"));
        chatTimeDate.setText(timestamp);

        return rowView;
    }

    private String format_date(String date_s){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Date newDate = null;
        try {
            newDate = format.parse(date_s);
        } catch (ParseException e) {
            //Log.e(TAG, e.getLocalizedMessage());
            e.printStackTrace();
        }

        format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(newDate);
    }

}