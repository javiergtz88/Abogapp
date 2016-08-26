package AppLeyes_db;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table CHAT_ORM.
 */
public class ChatORM {

    private Long id;
    private String recipient;
    private String sender;
    private Integer status;
    private String message_text;
    private String timestamp;
    private String created_at;
    private String updated_at;
    private Integer user_id;
    private Integer msg_type;
    private Integer priority;

    public ChatORM() {
    }

    public ChatORM(Long id) {
        this.id = id;
    }

    public ChatORM(Long id, String recipient, String sender, Integer status, String message_text, String timestamp, String created_at, String updated_at, Integer user_id, Integer msg_type, Integer priority) {
        this.id = id;
        this.recipient = recipient;
        this.sender = sender;
        this.status = status;
        this.message_text = message_text;
        this.timestamp = timestamp;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.user_id = user_id;
        this.msg_type = msg_type;
        this.priority = priority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(Integer msg_type) {
        this.msg_type = msg_type;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

}
