package Presenters;

import android.os.AsyncTask;
import android.util.Log;

import mx.appco.appbogado.AppLeyesApplication;
import mx.appco.appbogado.R;
import mx.appco.appbogado.SomeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Models.ContentModel;


/**
 * Created by appco on 23/09/15.
 */
public class ContentsPresenter {

    private static final String TAG = "ContentsPresenter";
    private SomeView activity;
    private ArrayList<HashMap> content_model;
    private ContentModel content;
    private SearchTask search_task;
    private GetScenariosTask scenarios_task;
    private ScenarioTask scenario_task;


    public ContentsPresenter(SomeView activity) {
        this.activity = activity;
    }


    public void search() {
        if(search_task == null) {
            search_task = new SearchTask();
            search_task.execute();
        }
    }

    public void scenarios(int page) {
        if(scenarios_task == null) {
            scenarios_task = new GetScenariosTask(page);
            scenarios_task.execute();
            Log.d(TAG, "scenarios_task.execute()");
        }
    }

    public void get_scenario() {
        HashMap data_hash = activity.get_data();
        Log.d(TAG, "data_hash= " + data_hash);
        if(data_hash == null)
            return;
        if((Boolean)data_hash.get("remote")) {
              // Es remote
            if (scenario_task == null) {
                scenario_task = new ScenarioTask();
                scenario_task.execute();
            }
        }else{
            // No es remote
            HashMap response_hash = new HashMap();
            String[] scenarios_detail = AppLeyesApplication.getAppContext()
                    .getResources().getStringArray(R.array.scenarios_detail);
            int k = (int) data_hash.get("id");
            String scenario = scenarios_detail[k];
            response_hash.put("scenario", scenario);
            activity.set_data(response_hash);
            activity.dismis_progress();



        }
    }


    public class GetScenariosTask extends AsyncTask<Void, List, List<HashMap>> {

        private final int page;

        public GetScenariosTask(int page){
            this.page = page;
        }

        @Override
        protected List<HashMap>  doInBackground(Void... params) {
            ArrayList<HashMap> contents = new ContentModel().get_scenarios(page);
            return contents;
        }

        @Override
        public void onPostExecute(final List<HashMap> contents) {
            if (contents != null) {
              for (HashMap content : contents)
                  activity.add(content);
            }
            scenarios_task = null;
        }

    }


    public class SearchTask extends AsyncTask<Void, List, List<HashMap>> {

        @Override
        protected List<HashMap> doInBackground(Void... params) {
            ContentModel content_model = new ContentModel();
            String search_string = (String) activity.get_data().get("search");
            if(search_string.length() > 1)
                return content_model.search(search_string);
            else
                return null;
        }

        @Override
        public void onPostExecute(final List<HashMap> contents) {
            if (contents != null) {
                for (HashMap item : contents) {
                    //Log.d(TAG, "SearchTask" + item.toString() + item.get("title"));
                    activity.add(item);
                }
            }
            search_task = null;
        }
    }

    public class ScenarioTask extends AsyncTask<Void, List, HashMap> {

        @Override
        protected HashMap doInBackground(Void... params) {
            ContentModel content_model = new ContentModel();
            int id = Integer.valueOf((String) activity.get_data().get("id"));
            return content_model.get_scenario(id);

        }

        @Override
        public void onPostExecute(final HashMap content) {
            if (content != null) {
                activity.set_data(content);
                activity.dismis_progress();
            }

            activity.dismis_progress();
            scenario_task = null;
        }
    }
}
