package Presenters;

import android.os.AsyncTask;

import mx.appco.appbogado.ConektaPayment;
import mx.appco.appbogado.SomeView;

import java.util.HashMap;

import Models.PaymentModel;

/**
 * Created by roblero on 30/03/15.
 */
public class PayPresenter {

    private static String TAG = "PayPresenter";
    private SomeView activity;
    private LoadTask load_task;
    private GetWalletTask get_wallet_task;
    private GetMembershipTask get_membership_task;
    private PaymentModel payment;
    private BuyCreditsTask buy_credits_task;
    private UpgradeMemTask upgrade_mem_task;
    private boolean ready;

    public PayPresenter(SomeView view){
        activity = view;
        load_task = new LoadTask();
        load_task.execute();
    }

    public void buy_credits(){
        ConektaPayment coneckta = new ConektaPayment(activity, payment);
        coneckta.buy_credits(activity.get_data());
    }

    public void upgrade_membership(){
        upgrade_mem_task = new UpgradeMemTask(activity.get_data());
        upgrade_mem_task.execute();
    }


    public class LoadTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            payment = new PaymentModel();
            return true;
        }

        @Override
        public void onPostExecute(final Boolean success){
            get_wallet_task = new GetWalletTask();
            get_wallet_task.execute();
            get_membership_task = new GetMembershipTask();
            get_membership_task.execute();
            load_task = null;
        }

        @Override
        protected void onCancelled(){
            load_task = null;
        }
    }

    public class GetWalletTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            return payment.get_wallet();
        }

        @Override
        public void onPostExecute(final Boolean success){
            activity.set_data(payment.to_h());
            get_wallet_task = null;
        }

        @Override
        protected void onCancelled(){
            get_wallet_task = null;
        }
    }

    public class GetMembershipTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            return payment.get_membership();
        }

        @Override
        public void onPostExecute(final Boolean success){
            activity.set_data(payment.to_h());
            get_membership_task = null;
        }

        @Override
        protected void onCancelled(){
            get_membership_task = null;
        }
    }

    public class UpgradeMemTask extends AsyncTask<Void, Void, Boolean> {

        private final HashMap data;

        public UpgradeMemTask(HashMap data){
            this.data = data;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return payment.upgrade_membership(data);
        }

        @Override
        public void onPostExecute(final Boolean success){
            if(success) {
                activity.alert("pago exitoso!");
                activity.dismis_progress();
                upgrade_mem_task = null;
                HashMap params = new HashMap();
                params.put("payment_success", true);
                activity.set_data(params);
            }
            else
                activity.alert(payment.error_message);
            activity.dismis_progress();
            upgrade_mem_task = null;
        }

        @Override
        protected void onCancelled(){
            upgrade_mem_task = null;
        }
    }

    public class BuyCreditsTask extends AsyncTask<Void, Void, Boolean> {

        private final HashMap data;

        public BuyCreditsTask(HashMap data){
            this.data = data;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //return true;
            ConektaPayment coneckta = new ConektaPayment(activity, payment);
            return coneckta.buy_credits(data);
        }

        @Override
        public void onPostExecute(final Boolean success){
            buy_credits_task = null;
        }

        @Override
        protected void onCancelled(){
            buy_credits_task = null;
        }
    }
}
