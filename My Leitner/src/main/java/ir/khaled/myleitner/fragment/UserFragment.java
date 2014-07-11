package ir.khaled.myleitner.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.khaled.myleitner.R;
import ir.khaled.myleitner.activity.MainActivity;
import ir.khaled.myleitner.view.AppButton;
import ir.khaled.myleitner.view.AppEditText;

/**
 * Created by kh.bakhtiari on 7/10/2014.
 */
public class UserFragment extends Fragment {
    private Context context;

    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        context = getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).onFragmentAttached(MainActivity.FRAGMENT_USER, getString(R.string.user_title));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    public class ViewHolder {
        public View v_root;
        public AppEditText et_title;
        public AppEditText et_front;
        public AppButton bt_login;
    }

}
