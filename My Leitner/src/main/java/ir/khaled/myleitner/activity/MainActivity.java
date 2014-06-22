package ir.khaled.myleitner.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import ir.khaled.myleitner.R;
import ir.khaled.myleitner.dialog.DialogPublisher;
import ir.khaled.myleitner.dialog.LastChanges;
import ir.khaled.myleitner.dialog.Welcome;
import ir.khaled.myleitner.fragment.AddCardFragment;
import ir.khaled.myleitner.fragment.MainFragment;
import ir.khaled.myleitner.fragment.NavigationDrawerFragment;


public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    public static final int FRAGMENT_MAIN = 1;
    public static final int FRAGMENT_ADD_CARD = 2;
    private int currentFragment;
    AddCardFragment addCardFragment;
    MainFragment mainFragment;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        Welcome.showIfFirstRun(this);
        LastChanges.showIfIsNewVersion(this);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        switch (position) {
            case 1:
                break;

            case 2:
                break;
        }



        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commit();
    }

    public void onFragmentAttached(int fragment, String title) {
        currentFragment = fragment;
        mTitle = title;
        restoreActionBar();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
        switch (currentFragment) {
            case FRAGMENT_MAIN:
                actionBar.setDisplayShowHomeEnabled(true);
                mNavigationDrawerFragment.showAsUp(false);
                mNavigationDrawerFragment.unlockDrawer();
                break;

            case FRAGMENT_ADD_CARD:
                actionBar.setDisplayHomeAsUpEnabled(true);
                mNavigationDrawerFragment.showAsUp(true);
                mNavigationDrawerFragment.lockDrawer();
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        getSupportFragmentManager().popBackStack();
        return true;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        if (!mNavigationDrawerFragment.isDrawerOpen()) {
//            // Only show items in the action bar relevant to this screen
//            // if the drawer is not showing. Otherwise, let the drawer
//            // decide what to show in the action bar.
////            getMenuInflater().inflate(R.menu.main, menu);
////            restoreActionBar();
//            return true;
//        }
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;

            case R.id.action_addCard:
                getSupportFragmentManager().beginTransaction().add(R.id.container, AddCardFragment.newInstance()).addToBackStack(null).commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        DialogPublisher.getInstance().nothingIsShown();
        super.onDestroy();
    }
}
