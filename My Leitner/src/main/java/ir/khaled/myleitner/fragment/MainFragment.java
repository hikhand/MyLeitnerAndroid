package ir.khaled.myleitner.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ir.khaled.myleitner.R;
import ir.khaled.myleitner.activity.MainActivity;

/**
 * Created by khaled on 6/18/2014.
 */
public class MainFragment extends Fragment implements FragmentManager.OnBackStackChangedListener {
    private ListView lv_main;

    /**
     * Returns a new instance of this fragment.
     */
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentAttached();
    }

    private void fragmentAttached() {
        ((MainActivity) getActivity()).onFragmentAttached(MainActivity.FRAGMENT_MAIN, getString(R.string.app_name));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        lv_main = (ListView) inflater.inflate(R.layout.fragment_main, container, false);
        return lv_main;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public void onBackStackChanged() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager == null)
            return;

        if (fragmentManager.getBackStackEntryCount() == 0){
            //if nothing is in the fragment backstack meaning main fragment is on screen
            fragmentAttached();
        }
    }
}