package AppLeyes_db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import AppLeyes_db.ChatORM;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table CHAT_ORM.
*/
public class ChatORMDao extends AbstractDao<ChatORM, Long> {

    public static final String TABLENAME = "CHAT_ORM";

    /**
     * Properties of entity ChatORM.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Recipient = new Property(1, String.class, "recipient", false, "RECIPIENT");
        public final static Property Sender = new Property(2, String.class, "sender", false, "SENDER");
        public final static Property Status = new Property(3, Integer.class, "status", false, "STATUS");
        public final static Property Message_text = new Property(4, String.class, "message_text", false, "MESSAGE_TEXT");
        public final static Property Timestamp = new Property(5, String.class, "timestamp", false, "TIMESTAMP");
        public final static Property Created_at = new Property(6, String.class, "created_at", false, "CREATED_AT");
        public final static Property Updated_at = new Property(7, String.class, "updated_at", false, "UPDATED_AT");
        public final static Property User_id = new Property(8, Integer.class, "user_id", false, "USER_ID");
        public final static Property Msg_type = new Property(9, Integer.class, "msg_type", false, "MSG_TYPE");
        public final static Property Priority = new Property(10, Integer.class, "priority", false, "PRIORITY");
    };


    public ChatORMDao(DaoConfig config) {
        super(config);
    }
    
    public ChatORMDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'CHAT_ORM' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'RECIPIENT' TEXT," + // 1: recipient
                "'SENDER' TEXT," + // 2: sender
                "'STATUS' INTEGER," + // 3: status
                "'MESSAGE_TEXT' TEXT," + // 4: message_text
                "'TIMESTAMP' TEXT," + // 5: timestamp
                "'CREATED_AT' TEXT," + // 6: created_at
                "'UPDATED_AT' TEXT," + // 7: updated_at
                "'USER_ID' INTEGER," + // 8: user_id
                "'MSG_TYPE' INTEGER," + // 9: msg_type
                "'PRIORITY' INTEGER);"); // 10: priority
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'CHAT_ORM'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ChatORM entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String recipient = entity.getRecipient();
        if (recipient != null) {
            stmt.bindString(2, recipient);
        }
 
        String sender = entity.getSender();
        if (sender != null) {
            stmt.bindString(3, sender);
        }
 
        Integer status = entity.getStatus();
        if (status != null) {
            stmt.bindLong(4, status);
        }
 
        String message_text = entity.getMessage_text();
        if (message_text != null) {
            stmt.bindString(5, message_text);
        }
 
        String timestamp = entity.getTimestamp();
        if (timestamp != null) {
            stmt.bindString(6, timestamp);
        }
 
        String created_at = entity.getCreated_at();
        if (created_at != null) {
            stmt.bindString(7, created_at);
        }
 
        String updated_at = entity.getUpdated_at();
        if (updated_at != null) {
            stmt.bindString(8, updated_at);
        }
 
        Integer user_id = entity.getUser_id();
        if (user_id != null) {
            stmt.bindLong(9, user_id);
        }
 
        Integer msg_type = entity.getMsg_type();
        if (msg_type != null) {
            stmt.bindLong(10, msg_type);
        }
 
        Integer priority = entity.getPriority();
        if (priority != null) {
            stmt.bindLong(11, priority);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ChatORM readEntity(Cursor cursor, int offset) {
        ChatORM entity = new ChatORM( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // recipient
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // sender
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // status
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // message_text
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // timestamp
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // created_at
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // updated_at
            cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8), // user_id
            cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9), // msg_type
            cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10) // priority
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ChatORM entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setRecipient(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSender(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setStatus(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setMessage_text(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setTimestamp(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setCreated_at(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setUpdated_at(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setUser_id(cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8));
        entity.setMsg_type(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
        entity.setPriority(cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ChatORM entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ChatORM entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}