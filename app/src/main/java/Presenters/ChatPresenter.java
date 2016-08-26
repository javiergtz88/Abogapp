package Presenters;

import android.os.AsyncTask;
import android.util.Log;

import mx.appco.appbogado.ChatView;
import mx.appco.appbogado.MessageFormView;

import java.util.ArrayList;
import java.util.HashMap;

import Models.ChatModel;

/**
 * Created by roblero on 30/03/15.
 */
public class ChatPresenter {
    private MessageFormView activity2;
    ChatModel chat_model;
    private LoadModelsClass load_task;
    private SendMessageTask send_task;
    private static final String TAG = "ChatPresenter";
    private ChatView activity;
    private boolean presenter_loaded;

    public ChatPresenter(ChatView arg_activity){
        this.activity = arg_activity;
        presenter_loaded = false;
        chat_model = new ChatModel();
        refresh_messages();
    }

    public void refresh_messages(){
        load_task = new LoadModelsClass();
        load_task.execute();
    }

    public ChatPresenter(MessageFormView arg_activity){
        this.activity2 = arg_activity;
        chat_model = new ChatModel();
        presenter_loaded = true;
    }

    public void send_message() {
        if(presenter_loaded) {
            send_task = new SendMessageTask();
            send_task.execute();
        }
    }

    public boolean loaded(){
        return presenter_loaded;
    }

    public class SendMessageTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            return chat_model.send_msg("AppLeyes", activity2.get_msg_text(),
                    activity2.get_msg_priority(), activity2.get_msg_category());
        }

        @Override
        public void onPostExecute(final Boolean success){
            send_task = null;
            if(success) {
                // Return to message list
                activity2.is_sent_ok(true);
            }
            else{
                activity2.alert(chat_model.error_message);
            }
            activity2.dismis_progress();
            //activity2.is_sent_ok(false);
        }

        @Override
        protected void onCancelled(){
            send_task = null;
        }

    }

    public class LoadModelsClass extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            //Log.d(TAG, "LoadModelsClass executed...");
            return chat_model.poll_msgs();
        }

        @Override
        public void onPostExecute(final Boolean success){
            load_task = null;
            if(success) {
                ArrayList<HashMap> messages= chat_model.get_msgs();
                if(messages != null) {
                    activity.clear_msgs();
                    for (HashMap message : messages) {
                        activity.add_msg(message);
                    }
                }
            }
            presenter_loaded = true;
        }

        @Override
        protected void onCancelled(){
            load_task = null;
        }

    }

}
