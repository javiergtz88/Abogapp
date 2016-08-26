package Models;

import android.util.Log;

import mx.appco.appbogado.AppLeyesApplication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import AppLeyes_db.ChatORM;

/**
 * Created by roblero on 30/03/15.
 */
public class ChatModel {

    public ArrayList<HashMap> messages;
    private static final String TAG = "ChatModel";
    public int error_code;
    public String error_message;

    public ChatModel(){
        this.messages = new ArrayList<>();
    }

    // Descripción: regresa un objeto list_element_scenario de los ultimos 20 mensajes
    // (MessageObject) ordenado del mas antiguo al mas reciente. Antes
    // de regresarlos intenta hacer un poll.
    public ArrayList<HashMap> get_msgs(){
        return this.messages;
    }

    // Descripcion: actualiza la list_element_scenario de mensajes locales y la sincroniza
    // con el servidor (usando las funciones del ChatModelHTTP). En caso de
    // que no exista una conexcion solo actualiza la base de datos local y
    // prende una bandera para sincronizarlos despues.\
    public boolean send_msg(String recipient, String message_text, int priority, int category){
        ChatModelHTTP chatHttp = new ChatModelHTTP(this);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //get current date time with Date()
        Date date = new Date();

        HashMap<String, Object> newMsg = new HashMap<>();
        newMsg.put("id", 0);
        newMsg.put("recipient", recipient);
        newMsg.put("sender",  UserModel.instance().usuario());
        newMsg.put("status", 0);
        newMsg.put("message_text", message_text);
        newMsg.put("timestamp", dateFormat.format(date));
        newMsg.put("created_at", null);
        newMsg.put("updated_at", null);
        newMsg.put("user_id", 0);
        newMsg.put("msg_type", category);
        newMsg.put("priority", priority);

        //Log.d(TAG, "validating message object map...");

        try{
            message_object_consistency_Q(newMsg);
            if(!chatHttp.post_msg(newMsg))
                return false;
            this.messages = chatHttp.get_msgs();
            //Log.d(TAG,"send_msg: successful post");
            return true;
        } catch (Exception e){
            e.printStackTrace();
            //Log.e(TAG, e.getLocalizedMessage());
            return false;
        }
    }

    // Descripcion: sincroniza los mensajes del servidor con los mensajes locales.
    public boolean poll_msgs(){
        ChatModelHTTP chatHttp = new ChatModelHTTP(this);
        try{
            this.messages = chatHttp.get_msgs();
            //Log.d(TAG,"poll_msgs: successful get");
            this.imprimirMsgs();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            //Log.e(TAG, "poll_msgs: " + e.getLocalizedMessage());
            return false;
        }

    }

    public void imprimirMsgs(){
        //Log.d(TAG, "messages: " + this.messages.toString());
    }

    //Guarda los datos de la instancia del usuario a la memoria de el dispositivo.
    public void guardar_msgs(){
        try {
            ChatORM guardar = new ChatORM();
            for(int i = 0; i < this.messages.size(); i++){
                if(this.messages.get(i) != null){
                    guardar.setId((long) i + 1L);
                    guardar.setCreated_at((String) this.messages.get(i).get("created_at"));
                    guardar.setMessage_text((String) this.messages.get(i).get("message_text"));
                    guardar.setMsg_type((Integer) this.messages.get(i).get("msg_type"));
                    guardar.setSender((String) this.messages.get(i).get("sender"));
                    guardar.setRecipient((String) this.messages.get(i).get("recipient"));
                    guardar.setStatus((Integer) this.messages.get(i).get("status"));
                    guardar.setTimestamp((String) this.messages.get(i).get("timestamp"));
                    guardar.setUser_id((Integer) this.messages.get(i).get("user_id"));
                    guardar.setCreated_at((String) this.messages.get(i).get("created_at"));

                    //Log.d(TAG, "guardar_msgs: " + ((long) i + 1L) + this.messages.get(i).toString());
                    ChatModelORM.insertOrUpdate(AppLeyesApplication.getAppContext(), guardar);
                }
                else{
                    //Log.i(TAG,"Guarde: " + i + " mensajes");
                    break;
                }
            }
            //Log.d(TAG, "Successfully saved messages to db");
            //UserModelORM.insertOrUpdate(ToyAppApplication.getAppContext(), guardar);
        }catch (Exception e) {
            e.printStackTrace();
            //Log.d(TAG,"Guardados sin exito");
            //Log.e(TAG, "Datos del usuario NO guardados: " + e);
        }
    }

    // Descripcion: carga la list_element_scenario actual de los mensajes en la base de datos local.
    public void cargar_msg(){
        ChatORM guardar;
        List<ChatORM> arregloORM;
        try {
            arregloORM = ChatModelORM.getAll(AppLeyesApplication.getAppContext());
            //Log.d(TAG,"Cargar_msg: numero de mensajes: " + arregloORM.size());
            for (int i = 0; i < arregloORM.size(); i++) {
                guardar = ChatModelORM.getForId(AppLeyesApplication.getAppContext(),1L);
                HashMap<String, Object> current_message = new HashMap<>();
                current_message.put("id", guardar.getId());
                current_message.put("recipient",  guardar.getRecipient());
                current_message.put("sender", guardar.getSender());
                current_message.put("status", guardar.getStatus());
                current_message.put("message_text", guardar.getMessage_text());
                current_message.put("timestamp",  guardar.getTimestamp());
                current_message.put("created_at", guardar.getCreated_at());
                current_message.put("updated_at", guardar.getUpdated_at());
                current_message.put("user_id", guardar.getUser_id());
                current_message.put("msg_type", guardar.getMsg_type());
                current_message.put("priority", guardar.getPriority());

                message_object_consistency_Q(current_message);
                this.messages.add(current_message);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            //Log.e(TAG,"Error al cargar los mensajes" + e.getLocalizedMessage());
        }

    }

    private void message_object_consistency_Q(HashMap<String, Object> current_message)
            throws Exception{
        String keys_array[] = {"id", "recipient", "sender", "status", "message_text", "timestamp",
                "created_at", "updated_at", "user_id", "msg_type", "priority"};
        //Log.d(TAG, "validating message object map...");
        for(String key: keys_array)
            if(!current_message.containsKey(key)) throw new Exception("Invalid message hash");
    }

    //MessageObject: {id : long, recipient : String, sender : String, status : integer
    // message_text : String, timestamp : String, created_at : String, updated_at :
    // String, user_id : integer, msg_type, integer}

    public void set_error(int _error) {
        this.error_code = _error;
        this.error_message = ErrorCluster.get_error_message(_error);
    }
}
