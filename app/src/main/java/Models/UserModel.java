package Models;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.facebook.login.LoginManager;

import mx.evp.abogapp.AppLeyesApplication;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by roblero on 30/03/15.
 */
public class UserModel extends Activity{

    // Singleton Class.
    private static UserModel instance = null;
    // Fields defined in UML diagram:
    private long    id;
    private String  name;
    private String  usuario;
    private String  token;
    private String  state;
    private String  city;
    private static final String TAG = "UserModel";
    public int error_code;
    public String error_message;

    public UserModel(){
        load();
    }

    // Method to call Class instance ((Singleton)
    public static UserModel instance() {
        if (instance == null)
            instance = new UserModel();
        return instance;
    }

    // login: defined in UML
    // Description: Logs the user to the server, a token is returned.
    public boolean login(String arg_usuario, String arg_contrasena) {
        UserModelHTTP http = new UserModelHTTP(this);

        try{
            JSONObject user = http.post_login(arg_usuario, arg_contrasena);
            if(user == null) return false;
            this.name = "name";
            this.usuario = user.getString("email");
            this.token = user.getString("auth_token");
            this.id = user.getLong("user_id");
            save();
            Log.d(TAG, "login: " + this.to_hash().toString());
            return true;
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "login: " + e.getLocalizedMessage());
            return false;
        }
    }

    public boolean fb_login(String id, String access_token) {
        UserModelHTTP modelo_usuariohttp = new UserModelHTTP(this);

        try{
            JSONObject user = modelo_usuariohttp.post_fb_login(id, access_token);
            if(user== null) return false;
            this.name = user.getString("name");
            this.usuario = user.getString("email");
            this.token = user.getString("authentication_token");
            this.id = user.getLong("id");
            save();
            Log.d(TAG, "fb_login: " + this.to_hash().toString());
            return true;
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "login: " + e.getLocalizedMessage());
            return false;
        }
    }

    // logout: defined in UML
    // Description: logs out the user reseting the token in server.
    public boolean logout(){
        LoginManager.getInstance().logOut();
        id = 0L;
        name = "";
        usuario = "";
        token = "";
        save();
        return true;
    }

    // register: defined in UML
    // Description: registers the user in te server.
    public boolean register(String name, String email, String password, String state, String city){
        UserModelHTTP http = new UserModelHTTP(this);
        HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("email", email);
        params.put("password", password);
        params.put("state", state);
        params.put("city", city);
        try {
            JSONObject user = http.register(params);
            if(user == null) return false;
            this.usuario = user.getString("email");
            this.token = user.getString("auth_token");
            this.id = user.getLong("user_id");
            this.city = user.getString("city");
            this.state = user.getString("state");
            save();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "register: " + e.getLocalizedMessage());
            return false;
        }
    }

    public boolean fetch() {
        UserModelHTTP http = new UserModelHTTP(this);
        try{
            JSONObject user = http.get_user(this);
            if(user != null)
                return true;
        } catch (Exception e){
            return false;
        }
        return false;
    }

    // save: defined in UML
    private UserModel save() {
        try {
            SharedPreferences.Editor editor = AppLeyesApplication.getSharedPrefs().edit();
            editor.putLong("user_id", id);
            editor.putString("user_name", name);
            editor.putString("user_user", usuario);
            editor.putString("user_token", token);
            editor.commit();
            load();
            Log.d(TAG, "save: " + this.to_hash().toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "save: " + e.getLocalizedMessage());
        }
        return this;
    }

    // load: defined in UML
    private UserModel load() {
        // Default values
        id = 0L;
        name = "";
        usuario = "";
        token = "";
        try {
            SharedPreferences prefs = AppLeyesApplication.getSharedPrefs();
            id = prefs.getLong("user_id", id);
            name = prefs.getString("user_name", name);
            usuario = prefs.getString("user_user", usuario);
            token = prefs.getString("user_token", token);
            Log.d(TAG, "load: " + this.to_hash().toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "load: " + e.getLocalizedMessage());
        }
        return this;
    }

    public boolean  esta_autenticado() {
        return !token.isEmpty();
    }


    private HashMap to_hash(){
        HashMap<String, Object> ret = new HashMap<>();
        ret.put("id", this.id);
        ret.put("name", this.name);
        ret.put("esta_autenticado", esta_autenticado());
        ret.put("type", type());
        ret.put("usuario", this.usuario);
        ret.put("token", this.token);
        return ret;
    }

    //Getters: defined in UML
    public long     id() {      return this.id;}
    //public String   nombre() {  return this.name;}
    public int      type(){     return 0;}
    public String   usuario(){  return this.usuario;}
    public String   token(){    return this.token;}

    public void set_error(int _error) {
        this.error_code = _error;
        this.error_message = ErrorCluster.get_error_message(_error);
    }
}




