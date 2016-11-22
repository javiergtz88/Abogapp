package Models;

import android.content.Context;

import mx.evp.abogapp.AppLeyesApplication;

import java.util.List;

import AppLeyes_db.UserORM;
import AppLeyes_db.UserORMDao;

/**
 * Created by saul on 2/6/15.
 */
public class UserModelORM {

    public static void insertOrUpdate(Context context, UserORM arg_UserORM) {
        getUserORMDao(context).insertOrReplace(arg_UserORM);
    }

    public static void clear(Context context) {
        getUserORMDao(context).deleteAll();
    }

    public static void deleteWithId(Context context, long id) {
        getUserORMDao(context).delete(getForId(context, id));
    }

    public static List<UserORM> getAll(Context context) {
        return getUserORMDao(context).loadAll();
    }

    public static UserORM getForId(Context context, long id) {
        return getUserORMDao(context).load(id);
    }

    private static UserORMDao getUserORMDao(Context c) {
        return ((AppLeyesApplication) c.getApplicationContext()).getDaoSession().getUserORMDao();
    }

}
