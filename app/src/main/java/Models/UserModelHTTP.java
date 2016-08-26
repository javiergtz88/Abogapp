package Models;

import android.util.Log;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import Models.Exceptions.ServerError;


/**
 * Created by roblero on 12/03/15.
 */
public class UserModelHTTP extends HTTPBaseModel{

    private static String url_user_path="/users/:id.json";
    private static final String url_api_v1_register_path= "/api/v1/registrations.json";
    private static final String url_api_v1_login_path= "/api/v1/sessions.json";
    private static final String url_api_v1_logout_path= "/api/v1/sessions.json";
    private static final String TAG = "UserModelHTTP";
    private static String url_api_v1_update_path = "/api/v1/users/:id.json";
    private UserModel model;
    private static final String url_api_v1_fb_login_path = "/api/v1/users/android/fb_create.json";

    public UserModelHTTP(UserModel model){
        this.model = model;
    }

    // get_user: defined in UML
    // Description:
    public JSONObject get_user(UserModel user){
        StringBuilder url = new StringBuilder();
        url.append(url_user_path.replace(":id", String.valueOf(user.id())));
        url.append("?user_email=");
        url.append(user.usuario());
        url.append("&user_token=");
        url.append(user.token());

        try {
            // HTTPs get
            JSONObject json_response = new JSONObject(request("GET", url.toString()));
            if (json_response.getBoolean("success")) {
                return json_response;
            } else{
                model.set_error(1);
                return null;
            }
        }
        catch (Exception e) {
            model.set_error(2);
            return null;
        }
    }

    // post_login: defined in UML
    // Description:
    public JSONObject post_login (String email, String password) throws Exception{
        StringBuilder url = new StringBuilder();
        url.append(url_api_v1_login_path);

        try {
            JSONObject json = new JSONObject();
            JSONObject json_hijo = new JSONObject();
            json_hijo.put("email", email);
            json_hijo.put("password", password);
            json.put("user", json_hijo);

            // HTTPs post
            //Log.d(TAG, "post_login.request: " + json.toString());
            JSONObject json_response = new JSONObject(
                    request("POST", url.toString(), json));
            if (json_response.getBoolean("success")) {
                return json_response.getJSONObject("data").getJSONObject("user");
            }
            else if(json_response.getInt("error_code") == 102){
                model.set_error(102);
                return null;
            }
            else{
                model.set_error(1);
                return null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            //Log.e(TAG, "post_login: " + e.getLocalizedMessage());
            model.set_error(2);
            return null;
        }
    }

    // post_register: defined in UML
    public JSONObject register(HashMap params){
        try {
            // params = { user: { email: @user.email, password: @user.password,
            //    password_confirmation: @user.password_confirmation } }.to_json
            String post_register_url = url_api_v1_register_path;

            JSONObject json_hijo = new JSONObject();
            json_hijo.put("name", params.get("name"));
            json_hijo.put("email", params.get("email"));
            json_hijo.put("password", params.get("password"));
            json_hijo.put("password_confirmation", params.get("password"));
            JSONObject json = new JSONObject();
            json.put("user", json_hijo);

            // HTTPs post
            JSONObject result1 = new JSONObject(
                    request("POST", post_register_url, json));
            if(result1.getInt("error_code") == 210){
                model.set_error(210);
                return null;
            }
            else if(!result1.getBoolean("success")){
                model.set_error(1);
                return null;
            }
            result1 = result1.getJSONObject("data").getJSONObject("user");
            //Log.d(TAG, "Result 1: " + result1.toString());
            // params2 = {user_email: @user.email, user_token: a_token,
            // user_data: user_data}.to_json
            String post_update_url = url_api_v1_update_path.replace(":id", result1.getString("user_id"));

            JSONObject user_data_json = new JSONObject();
            user_data_json.put("nombre", params.get("name"));
            user_data_json.put("apellido", params.get("name"));
            user_data_json.put("city", params.get("city"));
            user_data_json.put("state", params.get("state"));
            JSONObject json_request = new JSONObject();
            json_request.put("user_email", result1.getString("email"));
            json_request.put("user_token", result1.getString("auth_token"));
            json_request.put("user_data", user_data_json);

            // HTTPs post
            JSONObject result2 = new JSONObject(
                    request("POST", post_update_url, json_request));
            if(result2.getInt("error_code") == 210){
                model.set_error(210);
                return null;
            }
            else if(!result2.getBoolean("success")){
                model.set_error(1);
                return null;
            }
            result2 = result2.getJSONObject("data").getJSONObject("user");
            //Log.d(TAG, "Result 2: " + result2.toString());

            JSONObject merged = new JSONObject();
            JSONObject[] objs = new JSONObject[] { result1, result2 };
            for (JSONObject obj : objs) {
                Iterator it = obj.keys();
                while (it.hasNext()) {
                    String key = (String)it.next();
                    merged.put(key, obj.get(key));
                }
            }
            //Log.d(TAG, "Merged: " + merged.toString());
            return merged;
        }catch (Exception e){
            model.set_error(2);
            return null;
        }
    }


    public JSONObject post_fb_login(String id, String access_token) {
        StringBuilder url = new StringBuilder();
        url.append(url_api_v1_fb_login_path);

        try {
            JSONObject json = new JSONObject();
            JSONObject json_hijo = new JSONObject();
            json_hijo.put("user_id", id);
            json_hijo.put("access_token", access_token);
            json.put("user_data", json_hijo);

            // HTTPs post
            //Log.d(TAG, "post_login.request: " + json.toString());
            JSONObject json_response = new JSONObject(
                    request("POST", url.toString(), json));
            if (json_response.getBoolean("success")) {
                return json_response.getJSONObject("data");
            }
            else if(json_response.getInt("error_code") == 102){
                model.set_error(102);
                return null;
            }
            else{
                model.set_error(1);
                return null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            //Log.e(TAG, "post_login: " + e.getLocalizedMessage());
            model.set_error(2);
            return null;
        }
    }
}




