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

    public static Type getType(WebClient.Type type) {
        switch (type) {
            case bool:
                return typeBoolean();

            case welcome:
                return typeWelcome();

            case lastChanges:
                return typeLastChanges();

            case user:
                return typeUser();

            default:
                return typeBoolean();
        }
    }

    public static Type typeWelcome() {
        return new TypeToken<Response<Welcome>>() {
        }.getType();
    }

    public static Type typeLastChanges() {
        return new TypeToken<Response<LastChanges>>() {
        }.getType();
    }

    public static Type typeBoolean() {
        return new TypeToken<Response<Boolean>>() {
        }.getType();
    }

    public static Type typeUser() {
        return new TypeToken<Response<User>>() {
        }.getType();
    }
}
