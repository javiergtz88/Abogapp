package mx.appco.appbogado;

/**
 * Created by juan on 20/01/16.
 */

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.conekta.conektasdk.Card;
import com.conekta.conektasdk.Conekta;
import com.conekta.conektasdk.Token;

import org.json.JSONObject;

import java.util.HashMap;

import Models.ErrorCluster;
import Models.ErrorizableModel;
import Models.PaymentModel;
import Models.PaymentModelHTTP;
import Models.UserModel;

public class ConektaPayment implements ErrorizableModel {
    private PaymentView activity;
    private Token token;
    private Card card;
    private String TAG="conektaPayment";
    public PaymentView c;
    private PaymentModel pay_model;
    private UpgradeMemTaskToken upgrade_mem_task_token;
    public int error_code;
    public String error_message;
    private String amount_data;

    public ConektaPayment(SomeView _activity, PaymentModel _pay_model){
        activity = (PaymentView) _activity;
        pay_model = _pay_model;
        Conekta.setPublicKey("key_WsaHPUJ9ymuw8zvgDo8yQcg"); //Set public key
        Conekta.setApiVersion("1.0.0"); //Set api version (optional)
        Conekta.collectDevice(activity); //Collect device
    }


    public boolean buy_credits(HashMap params){
        amount_data = String.valueOf((Integer) params.get("amount"));
        init_token();
        return false;
    }

    public void init_token(){
        token = new Token(activity);
        Log.d(TAG, "init_token: Token:  " + token.toString());


        // Esto se va a ejecutar cuando el token sea recibido
        token.onCreateTokenListener(new Token.CreateToken() {
            @Override
            public void onCreateTokenReady(JSONObject data) {
                Log.d(TAG, "init_token: token.onCreateTokenListener:  " + data.toString());
                try {

                    String object_type = data.getString("object");
                    Log.d(TAG, "object_type: " + object_type);
                    if(object_type.equals("error")){
                        activity.dismis_progress();
                        activity.alert(data.getString("message_to_purchaser"));
                    } else if(object_type.equals("token")){
                        String token_id = data.getString("id");
                        send_token(token_id, amount_data);
                    } else {
                        activity.dismis_progress();
                        activity.alert("A ocurrido un error!");
                    }

                } catch (Exception err) {
                    activity.alert("A ocurrido un error!");
                    Log.d(TAG, "Error: " + err.toString());
                }
            }
        });

        create_token();
    }



    private void send_token(String token, String amount){
        upgrade_mem_task_token = new UpgradeMemTaskToken(token, amount);
        upgrade_mem_task_token.execute();
    }

    public void create_token(){
        HashMap a = activity.get_data();
        Log.d(TAG,"datos" + a.toString());
        card = new Card(
                a.get("first_name").toString(),
                a.get("number").toString(),
                a.get("cvv2").toString(),
                a.get("expire_month").toString(),
                a.get("expire_year").toString()
        );

        token.create(card);
    }

    @Override
    public void set_error(int _error) {
        this.error_code = _error;
        this.error_message = ErrorCluster.get_error_message(_error);
    }

    public class UpgradeMemTaskToken extends AsyncTask<String, Void, Boolean> {

        private final String token;
        private final String amount;
        private final HashMap pay_data;

        public UpgradeMemTaskToken(String dataToken, String _amount) {
           this.token = dataToken;
           this.amount = _amount;
           this.pay_data = activity.get_data();
       }


       @Override
       protected Boolean doInBackground(String... strings) {
           Log.d(TAG, "UpgradeMemTaskToken executing...");
           PaymentModelHTTP http_server = new PaymentModelHTTP(pay_model);
           return http_server.post_token(pay_model, token, amount, pay_data);
       }

        @Override
       public void onPostExecute(final Boolean success){
           if(success) {
               activity.alert("pago exitoso!");
               activity.dismis_progress();
               HashMap success_pay = new HashMap();
               success_pay.put("payment_success", true);
               activity.set_data(success_pay);
               upgrade_mem_task_token = null;

           }
           else {
               activity.alert(pay_model.error_message);
           }
           activity.dismis_progress();
           upgrade_mem_task_token = null;
       }

       @Override
       protected void onCancelled(){
           upgrade_mem_task_token = null;
       }
   }

}
