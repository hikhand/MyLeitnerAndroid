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
import ir.khaled.myleitner.model.User;
import ir.khaled.myleitner.view.AppButton;
import ir.khaled.myleitner.view.AppEditText;
import ir.khaled.myleitner.view.AppTextView;

/**
 * Created by khaled on 7/10/2014.
 */
public class LoginFragment extends Fragment implements View.OnClickListener, User.Login.LoginListener {
    public static final String POP_TAG = "login";
    Context context;
    ViewHolder viewHolder;

    public static LoginFragment newInstance() {
        return new LoginFragment();
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
        ((MainActivity) getActivity()).onFragmentAttached(MainActivity.Fragment.FRAGMENT_LOGIN, getString(R.string.login_title));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewHolder = new ViewHolder();
        viewHolder.v_root = inflater.inflate(R.layout.login_fragment, container, false);

        AppButton btn_login = (AppButton) viewHolder.v_root.findViewById(R.id.btn_login);
        viewHolder.et_email = (AppEditText) viewHolder.v_root.findViewById(R.id.et_email);
        viewHolder.et_password = (AppEditText) viewHolder.v_root.findViewById(R.id.et_password);
        viewHolder.tv_register = (AppTextView) viewHolder.v_root.findViewById(R.id.tv_register);

        btn_login.setOnClickListener(this);
        viewHolder.tv_register.setOnClickListener(this);

        return viewHolder.v_root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    /**
     * @param v view btn_login
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_register) {
            showFragmentRegister();
        } else {
            login();
        }
    }

    private void showFragmentRegister() {
        getFragmentManager().beginTransaction().add(R.id.container, RegisterFragment.newInstance()).addToBackStack(POP_TAG).commit();
    }

    private void login() {
        User.Login login = new User.Login(context, viewHolder.et_email, viewHolder.et_password, this);
        login.login();
    }

    @Override
    public void onLoginSucceed() {
        getFragmentManager().popBackStack();
//        getFragmentManager().beginTransaction().replace(R.id.container, MainFragment.newInstance()).addToBackStack(null).commit();
    }

    @Override
    public void onLoginFailed() {

    }

    public class ViewHolder {
        public View v_root;
        public AppEditText et_email;
        public AppEditText et_password;
        public AppTextView tv_register;
    }
}
