package ir.khaled.myleitner.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.khaled.myleitner.R;
import ir.khaled.myleitner.activity.MainActivity;
import ir.khaled.myleitner.model.User;
import ir.khaled.myleitner.view.AppButton;
import ir.khaled.myleitner.view.AppEditText;

/**
 * Created by kh.bakhtiari on 7/11/2014.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener, User.Register.RegisterListener {
    Context context;
    ViewHolder viewHolder;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
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
        ((MainActivity) getActivity()).onFragmentAttached(MainActivity.Fragment.FRAGMENT_REGISTER, getString(R.string.register_title));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewHolder = new ViewHolder();
        viewHolder.v_root = inflater.inflate(R.layout.register_fragment, container, false);

        viewHolder.et_email = (AppEditText) viewHolder.v_root.findViewById(R.id.et_email);
        viewHolder.et_displayName = (AppEditText) viewHolder.v_root.findViewById(R.id.et_displayName);
        viewHolder.et_password = (AppEditText) viewHolder.v_root.findViewById(R.id.et_password);
        viewHolder.et_passwordRepeat = (AppEditText) viewHolder.v_root.findViewById(R.id.et_passwordRepleat);

        AppButton btn_register = (AppButton) viewHolder.v_root.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);

        return viewHolder.v_root;
    }

    @Override
    public void onClick(View v) {
        register();
    }

    private void register() {
        User.Register register = new User.Register(context, viewHolder, this);
        register.register();
    }

    @Override
    public void onRegisterSucceed() {
        getFragmentManager().popBackStack(LoginFragment.POP_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//        getFragmentManager().beginTransaction().replace(R.id.container, MainFragment.newInstance()).addToBackStack(null).commit();
    }

    @Override
    public void onRegisterFailed() {

    }

    private class ViewHolder extends User.Register.ViewHolderRegister {
        View v_root;
    }
}
