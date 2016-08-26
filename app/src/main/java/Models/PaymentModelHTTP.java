package Models;

import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by agustin on 4/18/15.
 */
public class PaymentModelHTTP extends HTTPBaseModel{

    private static final String TAG = "PaymentModelHTTP2";
    private static final String url_api_v1_upgrade_membership = "/api/v1/memberships/upgrade";
    private static final String url_api_v1_buy_credits = "/api/v1/wallets/buy_credits";
    private static final String url_api_v1_wallet = "/api/v1/wallets";
    private static final String url_api_v1_membership = "/api/v1/memberships";
    private final ErrorizableModel model;

    public PaymentModelHTTP(ErrorizableModel model){
        this.model = model;
    }


    public boolean post_upgrade_membership(PaymentModel paymentModel, HashMap params) {
        StringBuilder url = new StringBuilder();
        JSONObject json_request = new JSONObject();
        url.append(url_api_v1_upgrade_membership);

        try {
            json_request.put("user_email", params.get("user_email"));
            json_request.put("user_token", params.get("user_token"));
            json_request.put("type", params.get("type"));
            json_request.put("number", params.get("number"));
            json_request.put("expire_month", params.get("expire_month"));
            json_request.put("expire_year", params.get("expire_year"));
            json_request.put("cvv2", params.get("cvv2"));
            json_request.put("first_name", params.get("first_name"));
            json_request.put("last_name", params.get("last_name"));
            // HTTPs post
            JSONObject json_response = new JSONObject(
                    request("POST", url.toString(), json_request));
            //Log.d(TAG, "upgrade_membership: " + json_response.toString());
            if(json_response.getBoolean("success")){
                paymentModel.set_expire_date(json_response.getJSONObject("data")
                        .getString("expire_date"));
                paymentModel.set_m_type(json_response.getJSONObject("data")
                        .getInt("m_type"));
                return true;
            }
            else if(json_response.getInt("error_code") == 505){
                model.set_error(505);
                return false;
            } else{
                model.set_error(1);
                return false;
            }
        } catch (Exception e) {
            model.set_error(2);
            //Log.e(TAG, "upgrade_membership: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean post_buy_credits(PaymentModel paymentModel, HashMap params) {
        StringBuilder url = new StringBuilder();
        JSONObject json_request = new JSONObject();
        url.append(url_api_v1_buy_credits);

        try {
            json_request.put("user_email", params.get("user_email"));
            json_request.put("user_token", params.get("user_token"));
            json_request.put("amount", params.get("amount"));
            json_request.put("type", params.get("type"));
            json_request.put("number", params.get("number"));
            json_request.put("expire_month", params.get("expire_month"));
            json_request.put("expire_year", params.get("expire_year"));
            json_request.put("cvv2", params.get("cvv2"));
            json_request.put("first_name", params.get("first_name"));
            json_request.put("last_name", params.get("last_name"));
            //Log.d(TAG, json_request.toString());
            // HTTPs post
            String string_response = request("POST", url.toString(), json_request, 60000);
            if(string_response == null){
                model.set_error(510);
                return false;
            }
            JSONObject json_response = new JSONObject(string_response);

            //Log.d(TAG, "post_buy_credits: " + json_response.toString());
            if(json_response.getBoolean("success")){
                paymentModel.set_credits_left(json_response.getJSONObject("data")
                        .getInt("credits_left"));
                return true;
            }else if(json_response.getInt("error_code") == 505){
                model.set_error(505);
                return false;
            } else{
                model.set_error(1);
                return false;
            }
        } catch (Exception e) {
            model.set_error(2);
            //Log.e(TAG, "post_buy_credits: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean get_wallet(PaymentModel paymentModel, HashMap params) {
        StringBuilder url = new StringBuilder();
        url.append(url_api_v1_wallet);
        url.append("?user_email=");
        url.append(params.get("user_email"));
        url.append("&user_token=");
        url.append(params.get("user_token"));

        try {
            // HTTPs get
            JSONObject json_response = new JSONObject(
                    request("GET", url.toString()));
            //Log.d(TAG, "get_wallet: " + json_response.toString());
            if(json_response.getBoolean("success")){
                JSONObject data = json_response.getJSONObject("data");
                if (!data.isNull("credits_left")){
                    paymentModel.set_credits_left(data.getInt("credits_left"));
                }
            }else{
                model.set_error(1);
                return false;
            }
        } catch (Exception e) {
            model.set_error(2);
            //Log.e(TAG, "get_wallet: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean get_membership(PaymentModel paymentModel, HashMap params) {
        StringBuilder url = new StringBuilder();
        url.append(url_api_v1_membership);
        url.append("?user_email=");
        url.append(params.get("user_email"));
        url.append("&user_token=");
        url.append(params.get("user_token"));

        try {
            // HTTPs post
            JSONObject json_response = new JSONObject(
                    request("GET", url.toString()));

            //Log.d(TAG, "get_membership: " + json_response.toString());
            if(json_response.getBoolean("success")){
                JSONObject data = json_response.getJSONObject("data");
                if(!data.isNull("expire_date")){
                    paymentModel.set_expire_date(data.getString("expire_date"));
                }
                paymentModel.set_m_type(json_response.getJSONObject("data")
                        .getInt("m_type"));
            }else{
                model.set_error(1);
                return false;
            }
        } catch (Exception e) {
            model.set_error(2);
            //Log.e(TAG, "get_membership: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return false;
    }


    public boolean post_token(PaymentModel paymentModel, String token, String amount, HashMap pay_data) {
        StringBuilder url = new StringBuilder();
        JSONObject json_request = new JSONObject();
        url.append(url_api_v1_buy_credits);

        try {
            json_request.put("token",token);
            json_request.put("user_email", UserModel.instance().usuario());
            json_request.put("user_token", UserModel.instance().token());
            json_request.put("amount", amount);
            json_request.put("first_name", pay_data.get("first_name").toString());
            json_request.put("user_phone", pay_data.get("user_phone").toString());

            String string_response = request("POST", url.toString(), json_request, 60000);
            Log.d(TAG, "post_token: " + string_response.toString());
            if(string_response == null){
                model.set_error(510);
                return false;
            }
            JSONObject json_response = new JSONObject(string_response);

            Log.d(TAG, "post_token: " + json_response.toString());
            if(json_response.getBoolean("success")){
                paymentModel.set_credits_left(json_response.getJSONObject("data")
                        .getInt("credits_left"));
                return true;
            }else if(json_response.getInt("error_code") == 505){
                model.set_error(505);
                return false;
            } else {
                model.set_error(1);
                return false;
            }
        } catch (Exception e) {
            model.set_error(2);
            e.printStackTrace();
        }
        return false;
    }
}
