package ir.khaled.myleitner.webservice;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import ir.khaled.myleitner.dialog.LastChanges;
import ir.khaled.myleitner.dialog.Welcome;
import ir.khaled.myleitner.model.User;

/**
 * Created by khaled on 6/30/2014.
 */
public class Types {
    private static Type bolean;
    private static Type user;

    public static Type welcome() {
        return new TypeToken<Response<Welcome>>() {
        }.getType();
    }

    public static Type lastChanges() {
        return new TypeToken<Response<LastChanges>>() {
        }.getType();
    }

    public static Type booleanT() {
        if (bolean == null)
            bolean = new TypeToken<Response<Boolean>>() {
            }.getType();
        return bolean;
    }

    public static Type user() {
        if (user == null)
            user = new TypeToken<Response<User>>() {
            }.getType();
        return user;
    }
}
