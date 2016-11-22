package mx.evp.abogapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;

import com.facebook.FacebookSdk;

import AppLeyes_db.DaoMaster;
import AppLeyes_db.DaoSession;

/**
 * Created by saul on 1/31/15.
 */
public class AppLeyesApplication extends Application
{
    private static Typeface mFont;
    public DaoSession daoSession;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        setupDatabase();
        FacebookSdk.sdkInitialize(getApplicationContext());
        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.
        AppLeyesApplication.context = getApplicationContext();
    }

    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "AppLeyes_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
    public static Context getAppContext() { return AppLeyesApplication.context; }
    public static SharedPreferences getSharedPrefs(){return context.getSharedPreferences(
            context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);}

    public static Typeface getTypeface(Context context, String typeface) {
        if (mFont == null) {
            mFont = Typeface.createFromAsset(context.getAssets(), typeface);
        }
        return mFont;
    }
}
