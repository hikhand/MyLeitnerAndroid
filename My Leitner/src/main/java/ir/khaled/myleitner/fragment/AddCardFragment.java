package ir.khaled.myleitner.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.khaled.myleitner.R;
import ir.khaled.myleitner.activity.MainActivity;

/**
 * Created by khaled on 6/20/2014.
 */
public class AddCardFragment extends Fragment {
    View v_root;

    public static AddCardFragment newInstance() {
        AddCardFragment fragment = new AddCardFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).onFragmentAttached(MainActivity.FRAGMENT_ADD_CARD, getString(R.string.addCard_title));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v_root = inflater.inflate(R.layout.fragment_add_card, container, false);
        return v_root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.add_card, menu);
    }
}
