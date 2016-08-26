package Models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by saul on 4/14/15.
 */
public class ChatModelHTTP extends HTTPBaseModel{

    private static final String url_api_v1_message_create_path= "/api/v1/messages/create.json";
    private static final String url_api_v1_message_last20_path= "/api/v1/messages/last20.json";
    private static final String TAG = "ChatModelHTTP2";
    private final ChatModel model;

    public ChatModelHTTP(ChatModel model){
        this.model = model;
    }

    public boolean post_msg(HashMap message){
        StringBuilder post_msg_url = new StringBuilder();
        post_msg_url.append(url_api_v1_message_create_path);
        post_msg_url.append("?user_email=");
        post_msg_url.append(UserModel.instance().usuario());
        post_msg_url.append("&user_token=");
        post_msg_url.append(UserModel.instance().token());

        try {
            JSONObject json = new JSONObject();
            JSONObject jsonHijo = new JSONObject();
            jsonHijo.put("id", message.get("id"));
            jsonHijo.put("sender", message.get("sender"));
            jsonHijo.put("status", message.get("status"));
            jsonHijo.put("message_text", message.get("message_text"));
            jsonHijo.put("timestamp", message.get("timestamp"));
            jsonHijo.put("created_at", message.get("created_at"));
            jsonHijo.put("updated_at", message.get("updated_at"));
            jsonHijo.put("user_id", message.get("user_id"));
            jsonHijo.put("msg_type", message.get("msg_type"));
            jsonHijo.put("priority", message.get("priority"));
            json.put("message", jsonHijo);

            // HTTPs post
            JSONObject json_response = new JSONObject(
                    request("POST", post_msg_url.toString(), json));
            Log.d(TAG, "post_msg: message posted successfully" + json_response.toString());
            if (json_response.getBoolean("success")) {
                Log.d(TAG, "post_msg: message posted successfully");
                return true;
            }
            else if(json_response.getInt("error_code") == 303){
                model.set_error(303);
                return false;
            }
            else {
                model.set_error(1); /* error en el servidor */
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "chat post " + e.getLocalizedMessage());
            model.set_error(2); /* error interno */
            return false;
        }
    }

    // Definicion: hace un llamado al servidor utilizando GET
    // para descargar los mensajes nuevos que se hayan generado
    // en el servidor. El servidor contestara con un JSON.
    public ArrayList<HashMap> get_msgs() throws Exception{
        ArrayList<HashMap>  recieved_msges = new ArrayList<>();
        StringBuilder get_msgs_URL = new StringBuilder();
        get_msgs_URL.append(url_api_v1_message_last20_path);
        get_msgs_URL.append("?user_email=");
        get_msgs_URL.append(UserModel.instance().usuario());
        get_msgs_URL.append("&user_token=");
        get_msgs_URL.append(UserModel.instance().token());

        try {
            // HTTPs post
            JSONObject json_response = new JSONObject(
                    request("GET", get_msgs_URL.toString()));

            if (json_response.getBoolean("success")) {
                this.fillMessages(recieved_msges,
                        json_response.getJSONObject("data").getJSONArray("messages"));
                return recieved_msges;
            } else {
                model.set_error(1); /* error en el servidor */
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Log.e(TAG, "get_msgs: error en el get: " + e);
            model.set_error(2); /* error interno */
            return null;
        }
    }

    public void fillMessageObject(ArrayList msj,JSONObject json) throws JSONException{
            HashMap<String, Object> msg_hash = new HashMap<>();
            msg_hash.put("id", json.getLong("id"));
            msg_hash.put("recipient", json.getString("recipient"));
            msg_hash.put("sender",  json.getString("sender"));
            msg_hash.put("status", json.getInt("status"));
            msg_hash.put("message_text", json.getString("message_text"));
            msg_hash.put("timestamp", json.getString("timestamp"));
            msg_hash.put("created_at", json.getString("created_at"));
            msg_hash.put("updated_at", json.get("updated_at").toString());
            msg_hash.put("user_id", json.getInt("user_id"));
            msg_hash.put("msg_type", json.getInt("msg_type"));
            msj.add(msg_hash);
    }

    private void fillMessages(ArrayList msgs, JSONArray array){
        JSONObject jsonIndex;
        for(int i =0; i < array.length(); i++){
            try{
                jsonIndex = array.getJSONObject(i);
                this.fillMessageObject(msgs, jsonIndex);
            }
            catch (Exception e){
                model.set_error(2);
                //Log.e(TAG, "GET_user:fillMessages: " + e.getLocalizedMessage());
            }
        }
    }

}
