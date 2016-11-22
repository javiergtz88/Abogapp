package Models;

import android.content.Context;

import mx.evp.abogapp.AppLeyesApplication;

import java.util.List;

import AppLeyes_db.ChatORM;
import AppLeyes_db.ChatORMDao;

/**
 * Created by agustin on 4/24/15.
 */
public class ChatModelORM {
    public static void insertOrUpdate(Context context, ChatORM arg_ChatORM) {
        getChatORMDao(context).insertOrReplace(arg_ChatORM);
    }

    public static void Update(Context context, ChatORM arg_ChatORM) {
        getChatORMDao(context).update(arg_ChatORM);
    }

    public static void insert(Context context, ChatORM arg_ChatORM){
        getChatORMDao(context).insert(arg_ChatORM);
    }

    public static void clear(Context context) {
        getChatORMDao(context).deleteAll();
    }

    public static void deleteWithId(Context context, long id) {
        getChatORMDao(context).delete(getForId(context, id));
    }

    public static List<ChatORM> getAll(Context context) {
        return getChatORMDao(context).loadAll();
    }

    public static ChatORM getForId(Context context, long id) {
        return getChatORMDao(context).load(id);
    }

    private static ChatORMDao getChatORMDao(Context c) {
        return ((AppLeyesApplication) c.getApplicationContext()).getDaoSession().getChatORMDao();
    }
}
