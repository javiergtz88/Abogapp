package Models;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by agustin on 4/29/15.
 */
public class ContentModelHTTP extends HTTPBaseModel {
    private static final String api_v1_content_search_path = "/api/v1/contents/search.json";
    private static final String api_v1_content_path = "/api/v1/contents/article.json";
    private static final String api_v1_contents_path = "/api/v1/contents.json";

    private static final String TAG = "ContentModelHTTP";
    private ContentModel model;

    public ContentModelHTTP(ContentModel contentModel) {
        model = contentModel;
    }


      public JSONArray get_scenarios(int page) {
          StringBuilder url = new StringBuilder();
          url.append(api_v1_contents_path);
          url.append("?user_email=");
          url.append(UserModel.instance().usuario());
          url.append("&user_token=");
          url.append(UserModel.instance().token());
          url.append("&page=");
          url.append(page);
        try {
            JSONObject json_response = new JSONObject(
                    request("GET", url.toString()));
            Log.d(TAG, "json_response" + json_response);
            if (json_response.getBoolean("success"))
                return json_response.getJSONArray("data");
            else
                model.set_error(106);
            Log.e(TAG, "get_scenarios" + json_response);
        }catch (Exception e){
            //e.printStackTrace();
            Log.e(TAG, e.getLocalizedMessage());
            model.set_error(101);
        }
        return null;
    }


    public JSONObject get_scenario(int id) {
        StringBuilder url = new StringBuilder();
        url.append(api_v1_content_path);
        url.append("?user_email=");
        url.append(UserModel.instance().usuario());
        url.append("&user_token=");
        url.append(UserModel.instance().token());
        url.append("&id=");
        url.append(id);

        try {
            JSONObject json_response = new JSONObject(
                    request("GET", url.toString()));

            //Log.d(TAG, json_response.toString());
            if (json_response.getBoolean("success"))
                return json_response.getJSONObject("data");
            else
                model.set_error(106);
        }catch (Exception e){
            //e.printStackTrace();
            //Log.e(TAG, e.getLocalizedMessage());
            model.set_error(101);
        }
        return null;
    }

    public JSONArray post_search(String search) {
        StringBuilder url = new StringBuilder();
        url.append(api_v1_content_search_path);

        try {
            JSONObject json_request = new JSONObject();
            json_request.put("user_email", UserModel.instance().usuario());
            json_request.put("user_token", UserModel.instance().token());
            json_request.put("search", search);

            JSONObject json_response = new JSONObject(
                    request("POST", url.toString(), json_request));

            if (json_response.getBoolean("success"))
                return json_response.getJSONArray("data");
            else
                model.set_error(106);
        }catch (Exception e){
            //e.printStackTrace();
            //Log.e(TAG, e.getLocalizedMessage());
            model.set_error(101);
        }
        return null;
    }

}
