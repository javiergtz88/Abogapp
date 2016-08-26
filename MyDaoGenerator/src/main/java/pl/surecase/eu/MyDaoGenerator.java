package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(3, "AppLeyes_db");

        Entity UserORM = schema.addEntity("UserORM");
        UserORM.addIdProperty();
        UserORM.addLongProperty("user_id");
        UserORM.addStringProperty("nombre");
        UserORM.addIntProperty("esta_autenticado");
        UserORM.addIntProperty("Type");
        UserORM.addStringProperty("usuario");
        UserORM.addStringProperty("contrasena");
        UserORM.addStringProperty("token");

        Entity ChatORM = schema.addEntity("ChatORM");
        ChatORM.addIdProperty();
        ChatORM.addStringProperty("recipient");
        ChatORM.addStringProperty("sender");
        ChatORM.addIntProperty("status");
        ChatORM.addStringProperty("message_text");
        ChatORM.addStringProperty("timestamp");
        ChatORM.addStringProperty("created_at");
        ChatORM.addStringProperty("updated_at");
        ChatORM.addIntProperty("user_id");
        ChatORM.addIntProperty("msg_type");
        ChatORM.addIntProperty("priority");

        new DaoGenerator().generateAll(schema, args[0]);


    }

}

