package Models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by roblero on 30/03/15.
 */
public class ContentModel {


    private static final String TAG = "ContentModel";
    public int error_code;
    public String error_message;

    public ContentModel(){
        // Constructor
    }

    public HashMap<String, Object> get_scenario(int id) {
        ContentModelHTTP http_server = new ContentModelHTTP(this);
        JSONObject scenario = http_server.get_scenario(id);
        if (scenario != null) {
            try {
                return scenario_to_hash(scenario);
            } catch (JSONException e) {
                set_error(101);
            }
        }
        return null;
    }

    public ArrayList<HashMap> get_scenarios(){
        return get_scenarios(0);

    }

    public ArrayList<HashMap> get_scenarios(int page){
        ContentModelHTTP http_server = new ContentModelHTTP(this);
        JSONArray scenarios_JSON = http_server.get_scenarios(page);
        if (scenarios_JSON != null)
            return scenarios_to_hash(scenarios_JSON);
         return null;
    }

    public ArrayList<HashMap> search(String search_st){
        ContentModelHTTP http_server = new ContentModelHTTP(this);
        JSONArray scenarios_JSON = http_server.post_search(search_st);
        if (scenarios_JSON != null)
            return scenarios_to_hash(scenarios_JSON);
        return null;
    }

    private HashMap<String, Object> scenario_to_hash(JSONObject scenario) throws JSONException {
        HashMap<String,Object> result = new HashMap<>();
        String title = scenario.getString("title");
        String content = scenario.getString("content");

        result.put("title",  title);
        result.put("content", content);
        result.put("scenario",  format_scenario(title, content));
        //Log.d(TAG, "scenario hash: " + scenario.toString());
        return result;
    }

    private String format_scenario(String title, String content) {
        StringBuilder content_html = new StringBuilder();

        content_html.append("<html>");
        content_html.append("<head>");
        content_html.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /> ");
        content_html.append("</head>");
        content_html.append("<h1>");
        content_html.append(title);
        content_html.append("</h1>");
        content_html.append("<body style=\"text-align:justify\">");
        content_html.append(content);
       // content_html.append(" <a href=\"http://www.profeco.gob.mx/formas/f_esp_quejas.asp\">Profeco</a> ");
        content_html.append("</body");
        content_html.append("</html>");

        Log.d(TAG, "format_scenario: " + content_html.toString());


        return content_html.toString();
    }

    private ArrayList<HashMap> scenarios_to_hash(JSONArray scenarios_json) {
        ArrayList list = new ArrayList();
        try {
            if (scenarios_json != null) {
                for (int i=0;i<scenarios_json.length();i++){
                    list.add(scenarios_to_hash_map((JSONObject) scenarios_json.get(i)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d(TAG, "scenarios_to_hash" + list.toString());
        return list;
    }


    public HashMap scenarios_to_hash_map(JSONObject json_object) throws JSONException {
        HashMap<String,Object> result = new HashMap<>();
        result.put("id",  json_object.getString("ID"));
        result.put("title",  json_object.getString("title"));
        result.put("remote",  true);
        return result;
    }

    public void set_error(int _ecode){
        this.error_code = _ecode;
        this.error_message = ErrorCluster.get_error_message(_ecode);

    }


}
