package ir.khaled.myleitner.model;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ir.khaled.myleitner.R;
import ir.khaled.myleitner.dialog.AppDialog;
import ir.khaled.myleitner.interfaces.ResponseListener;
import ir.khaled.myleitner.library.Config;
import ir.khaled.myleitner.library.Util;
import ir.khaled.myleitner.view.AppEditText;
import ir.khaled.myleitner.webservice.Request;
import ir.khaled.myleitner.webservice.Response;
import ir.khaled.myleitner.webservice.WebClient;

/**
 * Created by kh.bakhtiari on 5/30/2014.
 */
public class User {
    public static final int PASSWORD_MIN_LENGTH = 6;
    public static final int USERNAME_MIN_LENGTH = 3;
    private static User instance;

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("firstName")
    @Expose
    public String firstName;
    @SerializedName("lastName")
    @Expose
    public String lastName;
    @SerializedName("nickName")
    @Expose
    public String nickName;
    @SerializedName("displayName")
    @Expose
    public String displayName;
    @SerializedName("picture")
    @Expose
    public String picture;
    @SerializedName("biography")
    @Expose
    public Biography biography;
    @SerializedName("device")
    @Expose
    public Device device;

    private Context context;

    private User(Context context) {
        this.context = context;
    }

    public static User getUser(Context context) {
        if (instance == null)
            instance = new User(context);
        return instance;
    }

    public boolean isLogin() {
        return id > 0;
    }

    public interface OnLoginFinishedListener {

        public void onLoginSucceed();

        public void onWrongInfo();

        public void onLoginFailed();
    }

    public interface OnRegisterFinishedListener {

        public void onRegisterSucceed();

        public void onEmailAlreadyExists();

        public void onRegisterFailed();
    }

    public static class Login extends AppDialog implements ResponseListener<User> {
        private static final String PARAM_EMAIL = "username";
        private static final String PARAM_PASSWORD = "password";
        private static final int ERROR_NO_USER = 7;
        private static final int ERROR_WRONG_INFO = 8;
        AppEditText et_email;
        AppEditText et_password;
        LoginListener loginListener;

        public Login(Context context, AppEditText et_email, AppEditText et_password, LoginListener loginListener) {
            super(context);
            this.et_email = et_email;
            this.et_password = et_password;
            this.loginListener = loginListener;
        }

        public void login() {
            if (isLoginInfoValid()) {
                startLoading(context.getString(R.string.loading));
                show();
                loginImpl();
            }
        }

        /**
         * checks to see whether login info is valid or not
         * <br/>
         * if not valid shows error on their EditTexts
         *
         * @return true if login info is valid, false otherwise
         */
        private boolean isLoginInfoValid() {
            String email = getEmail();
            String password = getPassword();

            boolean foundError = false;
            if (!Util.isEmailValid(email)) {
                showErrorInvalidEmailAddress();
                foundError = true;
            }

            if (password.length() < Config.PASSWORD_MIN_LENGTH) {
                showErrorShortPassword();
                foundError = true;
            }

            return !foundError;
        }

        /**
         * call server to login
         */
        private void loginImpl() {
            Request request = new Request(context, Request.REQUEST_LOGIN);
            request.addParam(PARAM_EMAIL, getEmail());
            request.addParam(PARAM_PASSWORD, getPassword());

            WebClient<User> webClient = new WebClient<User>(request, WebClient.Connection.PERMANENT, WebClient.Type.user, this);
            webClient.start();
        }

        private String getEmail() {
            return et_email.getText().toString();
        }

        private String getPassword() {
            return et_password.getText().toString();
        }

        private void showErrorInvalidEmailAddress() {
            et_email.setError(context.getString(R.string.error_invalidEmail));
        }

        private void showErrorShortPassword() {
            et_password.setError(context.getString(R.string.error_shortPassword));
        }

        @Override
        public void onResponse(User response) {
            dismiss();
            Toast.makeText(context, context.getString(R.string.loginSucceed), Toast.LENGTH_SHORT).show();
            loginListener.onLoginSucceed();
        }

        @Override
        public void onResponseError(Response<User> response) {
            stopLoading();
            switch (response.errorCode) {
                case ERROR_WRONG_INFO:
                    showError(context.getString(R.string.error_wrongInfo), context.getString(R.string.ok), new ClickWrongEmailOrPassword());
                    return;

                case ERROR_NO_USER:
                    showError(context.getString(R.string.error_noUserWithEmail), context.getString(R.string.ok), new ClickWrongEmailOrPassword());
                    return;
            }

            showError(context.getString(R.string.error_loginFailed), context.getString(R.string.retry), context.getString(R.string.cancel), new ClickFailed(), new ClickFailed());
        }

        public interface LoginListener {

            public void onLoginSucceed();

            public void onLoginFailed();
        }

        private class ClickWrongEmailOrPassword implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                dismiss();
                loginListener.onLoginFailed();
            }
        }

        private class ClickFailed implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                if (v.getId() == ID_BTN_POS) {
                    login();
                } else {
                    dismiss();
                    loginListener.onLoginFailed();
                }
            }
        }
    }

    public static class Register extends AppDialog implements ResponseListener<User>, View.OnClickListener {
        public static final String PARAM_DISPLAY_NAME = "displayName";
        public static final String PARAM_PASSWORD = "password";
        public static final String PARAM_EMAIL = "email";
        public static final int ERROR_EMAIL_ALREADY_EXISTS = 17;
        ViewHolderRegister viewHolder;
        RegisterListener registerListener;

        public Register(Context context, ViewHolderRegister viewHolder, RegisterListener registerListener) {
            super(context);
            this.viewHolder = viewHolder;
            this.registerListener = registerListener;
        }

        public void register() {
            if (isInfoValid()) {
                startLoading(context.getString(R.string.loading));
                show();
                registerImp();
            }
        }

        /**
         * checks to see whether register info is valid or not
         * <br/>
         * if not valid shows error on their EditTexts
         *
         * @return true if register infos are valid, false otherwise
         */
        private boolean isInfoValid() {
            String email = getEmail();
            String displayName = getDisplayName();
            String password = getPassword();
            String passwordRepeat = getPasswordRepeat();

            boolean foundError = false;

            if (!Util.isEmailValid(email)) {
                showErrorInvalidEmail();
                foundError = true;
            }

            if (displayName.length() < Config.DISPLAY_NAME_MIN_LENGTH) {
                showErrorShortDisplayName();
                foundError = true;
            } else if (displayName.length() > Config.DISPLAY_NAME_MAX_LENGTH) {
                showErrorLargeDisplayName();
                foundError = true;
            }

            if (password.length() < Config.PASSWORD_MIN_LENGTH) {
                showErrorShortPassword();
                foundError = true;
            } else if (!passwordRepeat.equals(password)) {
                showErrorPasswordMismatch();
                foundError = true;
            }

            return !foundError;
        }

        private void registerImp() {
            Request request = new Request(context, Request.REQUEST_REGISTER);
            request.addParam(PARAM_EMAIL, getEmail());
            request.addParam(PARAM_DISPLAY_NAME, getDisplayName());
            request.addParam(PARAM_PASSWORD, getPassword());

            WebClient<User> webClient = new WebClient<User>(request, WebClient.Connection.PERMANENT, WebClient.Type.user, this);
            webClient.start();
        }

        private String getEmail() {
            return viewHolder.et_email.getText().toString();
        }

        private String getDisplayName() {
            return viewHolder.et_displayName.getText().toString();
        }

        private String getPassword() {
            return viewHolder.et_password.getText().toString();
        }

        private String getPasswordRepeat() {
            return viewHolder.et_passwordRepeat.getText().toString();
        }

        private void showErrorInvalidEmail() {
            viewHolder.et_email.setError(context.getString(R.string.error_invalidEmail));
        }

        private void showErrorShortDisplayName() {
            viewHolder.et_displayName.setError(context.getString(R.string.error_shortDisplayName));
        }

        private void showErrorLargeDisplayName() {
            viewHolder.et_displayName.setError(context.getString(R.string.error_largeDisplayName));
        }

        private void showErrorShortPassword() {
            viewHolder.et_password.setError(context.getString(R.string.error_shortPassword));
        }

        private void showErrorPasswordMismatch() {
            viewHolder.et_passwordRepeat.setError(context.getString(R.string.error_passwordMismatch));
        }

        private void showErrorEmailAlreadyExists() {
            viewHolder.et_email.setError(context.getString(R.string.error_emailAlreadyExists));
        }

        @Override
        public void onResponse(User response) {
            dismiss();
            Toast.makeText(context, context.getString(R.string.registerSucceed), Toast.LENGTH_SHORT).show();
            registerListener.onRegisterSucceed();
        }

        @Override
        public void onResponseError(Response<User> response) {
            stopLoading();
            if (response.errorCode == ERROR_EMAIL_ALREADY_EXISTS) {
                showErrorEmailAlreadyExists();
                dismiss();
            } else {
                showError(context.getString(R.string.error_registerFailed), context.getString(R.string.retry), context.getString(R.string.cancel), this, this);
            }
            registerListener.onRegisterFailed();
        }

        @Override
        public void onClick(View v) {

        }

        public interface RegisterListener {

            public void onRegisterSucceed();

            public void onRegisterFailed();
        }

        public static class ViewHolderRegister {
            public AppEditText et_email;
            public AppEditText et_displayName;
            public AppEditText et_password;
            public AppEditText et_passwordRepeat;
        }
    }
}
