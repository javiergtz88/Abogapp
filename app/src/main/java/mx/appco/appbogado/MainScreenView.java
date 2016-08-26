package mx.appco.appbogado;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import Models.UserModel;
import mx.appco.appbogado.R;

import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainScreenView extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private static final String TAG = "MainScreenView";
    private TextView titleFragment;


    /*@Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }*/
    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            getFragmentManager().popBackStack();
        } else {
            getFragmentManager().popBackStack();
        }

    }

    //Variable for navigation fragment drawer instance.
    private NavigationDrawerFragment mNavigationDrawerFragment;
    //Memory variable to store the last screen title. For use in #restoreActionBar().
    private CharSequence mTitle;

    /* ### Activity methods ### */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Puts the activity_main layout.
        setContentView(R.layout.activity_main);
        //Oculta el teclado virtual del MainScreenSubView hasta que el Edittext sea clickeado
       //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        //Populates the variable containing the navigation drawer instance.
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        /*
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice("3F87C1697D9A9624")                    //SAMSUNG ID
                .addTestDevice("34D8BCF1F46E5CA7")                  // Esteban
                .addTestDevice("3ECBA233B4A379D3")                  //Rogelio
                .addTestDevice("364D40FDE56AFD8B")
                .addTestDevice("3CDFB9BD924ABFA5")
                .build();
        mAdView.loadAd(adRequest);
        */
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }




    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
//        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
       // mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.d(TAG, "on activity resume: " + String.valueOf(resultCode));
        super.onActivityResult(requestCode, resultCode, data);
    }

    /* ### Drawer helpers ### */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        //This is the method event handler calls for the drawer buttons.
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (position) {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, MainScreenSubView.newInstance(position + 1))
                        .commit();
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new ChatView()).addToBackStack(null)
                        .commit();
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new PremiumView()).addToBackStack(null)
                        .commit();
                break;
            case 3:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new AboutView()).addToBackStack(null)
                        .commit();
                break;
            case 4:
                UserModel.instance().logout();
                System.exit(0);
                finish();
        }
    }




    public void onSectionAttached(int number) {
        String title_sections[] = getResources().getStringArray(R.array.title_sections);
        if(number < title_sections.length)
            mTitle = title_sections[number - 1];
    }

    /* ### Action bar helpers ### */
    public void restoreActionBar() {
        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(R.layout.actionbar_custom, null);
        ActionBar action = getSupportActionBar();
        action.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        action.setCustomView(R.layout.actionbar_custom);
        action.setDisplayShowCustomEnabled(true);
        action.setDisplayShowTitleEnabled(true);
        action.setHomeAsUpIndicator(R.drawable.drawer_close);
        action.setDisplayHomeAsUpEnabled(true);

        titleFragment = (TextView) findViewById(R.id.titleFragment);
        titleFragment.setText(mTitle);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the *action* bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            /*SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));*/
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle *action bar* item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

                    return super.onOptionsItemSelected(item);
            }

}



