package ir.khaled.myleitner.dialog;

/**
 * Created by kh.bakhtiari on 5/30/2014.
 */
public class LoginController {
//    private Context context;
//    public LogOrReg logOrReg;
//    public Login login;
//    public Register register;
//    private String messageContent;
//    boolean processSucceed;
//    private User user;
//    private OnLoginFinishedListener onLoginFinishedListener;
//
//    public LoginController(Context context, OnLoginFinishedListener onLoginFinishedListener) {
//        init(context, null, onLoginFinishedListener);
//    }
//
//    public LoginController(Context context, String messageContent, OnLoginFinishedListener onLoginFinishedListener) {
//        init(context, messageContent, onLoginFinishedListener);
//    }
//
//    private void init(Context context, String messageContent, OnLoginFinishedListener onLoginFinishedListener) {
//        this.context = context;
//        logOrReg = new LogOrReg(context);
//        login = new Login(context);
//        register = new Register(context);
//        this.onLoginFinishedListener = onLoginFinishedListener;
//        this.messageContent = messageContent;
//        this.user = User.getUser(context);
//    }
//
//    public void start() {
//        logOrReg.fillContent();
//        if (User.getUser(context).isLogin()) {
////            onLoginFinishedListener.onLoginFinished(OnLoginFinishedListener.SUCCESS);
//            //TODO show you're already login
//            logOrReg.show();
//        } else {
//            logOrReg.show();
//        }
//    }
//
//    public class LogOrReg extends AppDialog {
//        public boolean dontSendResponse;
//
//        public LogOrReg(Context context) {
//            super(context);
//        }
//
//        public void fillContent() {
//            if (messageContent == null || messageContent.length() == 0) {
//                setContentView(context.getResources().getString(R.string.inappBilling_login_required));
//            } else {
//                setContentView(messageContent);
//            }
//
//            setPositiveButton(context.getResources().getString(R.string.login), clickBtnRight);
//            setNegativeButton(context.getResources().getString(R.string.register), clickBtnLeft);
//            setOnDismissListener(dismissListener);
//        }
//
//        @Override
//        public void show() {
//            dontSendResponse = false;
//            super.show();
//        }
//
//        View.OnClickListener clickBtnRight = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dontSendResponse = true;
//                dismiss();
//                login.fillContent();
//                login.show();
//            }
//        };
//
//        View.OnClickListener clickBtnLeft = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dontSendResponse = true;
//                dismiss();
//                register.fillContent();
//                register.show();
//            }
//        };
//
//        private DialogInterface.OnDismissListener dismissListener = new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                if (dontSendResponse)
//                    return;
//
//                onLoginFinishedListener.onLoginFinished(OnLoginFinishedListener.CANCELED);
//            }
//        };
//    }
//
//    public class Login extends AppDialog implements User.OnLoginFinishedListener {
//        AppEditText ET_username;
//        AppEditText ET_password;
//        boolean contentIsSet;
//
//
//        public Login(Context context) {
//            super(context);
//        }
//
//        public void fillContent() {
//            if (contentIsSet)
//                return;
//            contentIsSet = true;
//            View V_root = LayoutInflater.from(context).inflate(R.layout.dialog_login, null);
//            ET_username = (AppEditText) V_root.findViewById(R.id.ET_username);
//            ET_password = (AppEditText) V_root.findViewById(R.id.ET_password);
//
////            setTitle(context.getResources().getString(R.string.user_login_title));
//            setContentView(V_root);
//
//            setPositiveButton(context.getResources().getString(R.string.login), clickBtnLogin);
//            setNegativeButton(context.getResources().getString(R.string.cancel), clickBtnCancel);
//
//            setOnDismissListener(new OnDismissListener() {
//                @Override
//                public void onDismiss(DialogInterface dialogInterface) {
//                    if (!processSucceed)
//                        logOrReg.show();
//                }
//            });
//        }
//
//        @Override
//        public void show() {
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//            super.show();
//        }
//
//        View.OnClickListener clickBtnLogin = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final String userName = ET_username.getText().toString();
//                final String password = ET_password.getText().toString();
//
//                boolean foundError = false;
//
//                if (!Util.isEmailValid(userName)) {
//                    ET_username.setError(context.getResources().getString(R.string.errorInvalidEmail));
//                    foundError = true;
//                }
//
//                if (password.length() < User.PASSWORD_MIN_LENGTH) {
//                    ET_password.setError(context.getResources().getString(R.string.errorPasswordTooShort));
//                    foundError = true;
//                }
//
//                if (foundError) return;
//
//                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
//                inputMethodManager.hideSoftInputFromWindow(ET_password.getWindowToken(), 0);
//                inputMethodManager.hideSoftInputFromWindow(ET_username.getWindowToken(), 0);
//
//
//                startLoading();
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        user.login(userName, password, Login.this);
//                    }
//                }).start();
//            }
//        };
//
//        View.OnClickListener clickBtnCancel = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dismiss();
//            }
//        };
//
//        @Override
//        public void onLoginSucceed() {
//            ((Activity)context).runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    processSucceed = true;
//                    Toast.makeText(context, context.getResources().getString(R.string.loggedIn), Toast.LENGTH_SHORT).show();
//                    setOnDismissListener(null);
//                    dismiss();
//                    onLoginFinishedListener.onLoginFinished(OnLoginFinishedListener.SUCCESS);
//                }
//            });
//        }
//
//        @Override
//        public void onLoginFailed() {
//            ((Activity)context).runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(context, context.getResources().getString(R.string.loginFailed), Toast.LENGTH_SHORT).show();
//                    stopLoading();
//                }
//            });
//        }
//
//        @Override
//        public void onWrongInfo() {
//            ((Activity)context).runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(context, context.getResources().getString(R.string.wrongEmailOrPassword), Toast.LENGTH_LONG).show();
//                    stopLoading();
//                }
//            });
//
//        }
//    }
//
//    public class Register extends AppDialog implements User.OnRegisterFinishedListener{
//        AppEditText ET_username;
//        AppEditText ET_password;
//        AppEditText ET_passwordRepeat;
//        AppEditText ET_email;
//        boolean contentIsSet;
//
//
//        public Register(Context context) {
//            super(context);
//        }
//
//        public void fillContent() {
//            if (contentIsSet)
//                return;
//            contentIsSet = true;
//
//            View V_root = LayoutInflater.from(context).inflate(R.layout.dialog_register, null);
//            ET_username = (AppEditText) V_root.findViewById(R.id.ET_username);
//            ET_password = (AppEditText) V_root.findViewById(R.id.ET_password);
//            ET_passwordRepeat = (AppEditText) V_root.findViewById(R.id.ET_passwordRepeat);
//            ET_email = (AppEditText) V_root.findViewById(R.id.ET_email);
//
//            ET_username.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//                    ET_username.setError(null);
//                }
//            });
//
////            setTitle(context.getResources().getString(R.string.register));
//
//            setContentView(V_root);
//
//            setPositiveButton(context.getResources().getString(R.string.register), clickBtnRegister);
//            setNegativeButton(context.getResources().getString(R.string.cancel), clickBtnCancel);
//
//            setOnDismissListener(new OnDismissListener() {
//                @Override
//                public void onDismiss(DialogInterface dialogInterface) {
//                    logOrReg.show();
//                }
//            });
//        }
//
//        @Override
//        public void show() {
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//            super.show();
//        }
//
//        View.OnClickListener clickBtnRegister = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final String password = ET_password.getText().toString();
//                String passwordRep = ET_passwordRepeat.getText().toString();
//                final String username = ET_username.getText().toString();
//                final String email = ET_email.getText().toString();
//
//                boolean foundError = false;
//                if (!Util.isEmailValid(email)) {
//                    ET_email.setError(context.getResources().getString(R.string.errorInvalidEmail));
//                    foundError = true;
//                }
//
//                if (username.length() < User.USERNAME_MIN_LENGTH) {
//                    ET_username.setError(context.getResources().getString(R.string.errorUsernameTooShort));
//                    foundError = true;
//                }
//
//                if (password.length() < User.PASSWORD_MIN_LENGTH) {
//                    ET_password.setError(context.getResources().getString(R.string.errorPasswordTooShort));
//                    foundError = true;
//                }
//                if (!password.equals(passwordRep)) {
//                    ET_passwordRepeat.setError(context.getResources().getString(R.string.errorPasswordsDontMatch));
//                    foundError = true;
//                }
//                if (!Util.isEmailValid(email)) {
//                    ET_email.setError(context.getResources().getString(R.string.errorInvalidEmail));
//                }
//
//                if (foundError) return;
//
//                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
//                inputMethodManager.hideSoftInputFromWindow(ET_password.getWindowToken(), 0);
//                inputMethodManager.hideSoftInputFromWindow(ET_username.getWindowToken(), 0);
//
//                startLoading();
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        user.register(username, email, password, Register.this);
//                    }
//                }).start();
//            }
//        };
//
//        View.OnClickListener clickBtnCancel = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dismiss();
//            }
//        };
//
//        @Override
//        public void onRegisterSucceed() {
//            ((Activity)context).runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    processSucceed = true;
//                    Toast.makeText(context, context.getResources().getString(R.string.accountCreated), Toast.LENGTH_SHORT).show();
//                    setOnDismissListener(null);
//                    dismiss();
//                    onLoginFinishedListener.onLoginFinished(AppHandler.SUCCESS);
//                }
//            });
//        }
//
//        @Override
//        public void onRegisterFailed() {
//            ((Activity)context).runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(context, context.getResources().getString(R.string.registerFailed), Toast.LENGTH_SHORT).show();
//                    stopLoading();
//                }
//            });
//        }
//
//        @Override
//        public void onEmailAlreadyExists() {
//            ((Activity)context).runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(context, context.getResources().getString(R.string.wrongEmailOrPassword), Toast.LENGTH_LONG).show();
//                    stopLoading();
//                }
//            });
//        }
//    }
//
//    public interface OnLoginFinishedListener {
//        public static int SUCCESS = 0;
//        public static int CANCELED = 1;
//
//        public void onLoginFinished(int responseCode);
//    }
}
