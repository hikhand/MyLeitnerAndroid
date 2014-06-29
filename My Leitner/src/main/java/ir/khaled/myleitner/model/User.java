package ir.khaled.myleitner.model;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kh.bakhtiari on 5/30/2014.
 */
public class User {
    public static final int PASSWORD_MIN_LENGTH = 6;
    public static final int USERNAME_MIN_LENGTH = 3;
    private static User instance;

    @SerializedName("id")
    public int id;
    @SerializedName("firstName")
    public String firstName;
    @SerializedName("lastName")
    public String lastName;
    @SerializedName("nickName")
    public String nickName;
    @SerializedName("displayName")
    public String displayName;
    @SerializedName("picture")
    public String picture;
    @SerializedName("biography")
    public Biography biography;
    @SerializedName("device")
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
        return false;
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

}
