package mx.appco.appbogado;

import android.util.Log;
import android.widget.AbsListView;

import Presenters.ContentsPresenter;

/**
 * Created by saul on 10/14/15.
 */
public class EndlessScrollListener implements AbsListView.OnScrollListener {

    private int visibleThreshold = 5;
    private int currentPage = -1;
    private int previousTotal = 0;
    private boolean loading = true;

    private ContentsPresenter presenter;
    private static final String TAG = "EndlessScrollListener";

    public EndlessScrollListener(ContentsPresenter arg_presenter) {
        presenter =  arg_presenter;
    }
    public EndlessScrollListener(ContentsPresenter arg_presenter, int visibleThreshold) {
        presenter =  arg_presenter;
        this.visibleThreshold = visibleThreshold;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        Log.d(TAG, "onScroll: loading: " + loading);
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
                currentPage++;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            // I load the next page of gigs using a background task,
            // but you can call any function here.
            presenter.scenarios(currentPage + 1);
            loading = true;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
}
