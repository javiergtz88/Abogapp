package Models;

import java.util.HashMap;

/**
 * Created by roblero on 30/03/15.
 */
public class PaymentModel implements ErrorizableModel{

    private Integer m_type;
    private String expire_date;
    private Integer credits_left;
    public String error;
    private final static String TAG = "PaymentModel";
    public int error_code;
    public String error_message;

    public Integer get_m_type() { return m_type; }
    public void set_m_type(Integer m_type) { this.m_type = m_type; }
    public String get_expire_date() { return expire_date; }
    public void set_expire_date(String expire_date) { this.expire_date = expire_date; }
    public Integer getCredits_left() { return credits_left; }
    public void set_credits_left(Integer credits_left)
    { this.credits_left = credits_left; }

    public PaymentModel(){
        //
        m_type = 0;
        expire_date = "";
        credits_left = 0;
    }

    public boolean get_wallet(){
        PaymentModelHTTP http_server = new PaymentModelHTTP(this);
        http_server.get_wallet(this, generate_user_credentials());
        return false;
    }

    public boolean get_membership(){
        PaymentModelHTTP http_server = new PaymentModelHTTP(this);
        http_server.get_membership(this, generate_user_credentials());
        return false;
    }

    public boolean upgrade_membership(HashMap params){
        validate_payment_data(params);
        params.putAll(generate_user_credentials());
        PaymentModelHTTP http_server = new PaymentModelHTTP(this);
        return http_server.post_upgrade_membership(this, params);
    }

/*    public boolean sendToken(String params){
        PaymentModelHTTP http_server = new PaymentModelHTTP(this);
        return http_server.post_token(params);
    }   */

    public boolean buy_credits(HashMap params){
        validate_payment_data(params);
        params.putAll(generate_user_credentials());
        PaymentModelHTTP http_server = new PaymentModelHTTP(this);
        return http_server.post_buy_credits(this, params);
    }

    private boolean validate_payment_data(HashMap params){
        return false;
    }

    private HashMap generate_user_credentials(){
        HashMap<String, Object> params = new HashMap<>();
        params.put("user_email",UserModel.instance().usuario());
        params.put("user_token",UserModel.instance().token());
        return params;
    }

    public HashMap to_h() {
        HashMap<String, Object> a_hash = new HashMap<>();
        String membership_vals[] = {"Normal", "Premium"};
        a_hash.put("m_type", membership_vals[m_type]);
        a_hash.put("expire_date", expire_date);
        a_hash.put("credits_left", String.valueOf(credits_left));
        return a_hash;
    }

    public void set_error(int _error) {
        this.error_code = _error;
        this.error_message = ErrorCluster.get_error_message(_error);
    }
}
