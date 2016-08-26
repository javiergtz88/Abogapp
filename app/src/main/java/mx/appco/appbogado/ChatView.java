package mx.appco.appbogado;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import mx.appco.appbogado.R;

import java.util.HashMap;

import Presenters.ChatPresenter;

public class ChatView extends android.support.v4.app.Fragment{

    private ListView listaMsj;
    private ChatArrayAdapter adapter;
    private ChatPresenter presentador;
    private static final String TAG = "ChatView";
    private Button askButton;

    /* Fragment life cycle */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // load view layout
        View rootView = inflater.inflate(R.layout.fragment_chat_view, container, false);
        // load presenter
        this.presentador = new ChatPresenter(this);
        
        adapter = new ChatArrayAdapter(getActivity(), R.layout.list_element_chat_msg);
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listaMsj.setSelection(adapter.getCount() - 1);
            }
        });

        listaMsj = (ListView) rootView.findViewById(R.id.listView);
        listaMsj.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listaMsj.setAdapter(adapter);

        askButton = (Button) rootView.findViewById(R.id.chatAskButton);
        askButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call_chat_form();
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainScreenView) activity).onSectionAttached(2);
    }


    /* Actions */
    public void add_msg(HashMap message){
        adapter.add(message);
    }

    public void clear_msgs() {
        adapter.clear();
    }

    public void alert(String error_message) {
        Toast.makeText(getActivity(), error_message, Toast.LENGTH_SHORT).show();
    }

    public void call_chat_form(){
        Intent intent = new Intent(getActivity(), MessageFormView.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.d(TAG, "on activity resume: " + String.valueOf(resultCode));
        if (resultCode == 1){
            presentador.refresh_messages();
        }
    }

}