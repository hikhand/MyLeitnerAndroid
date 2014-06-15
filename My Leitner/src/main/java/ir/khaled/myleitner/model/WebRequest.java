package ir.khaled.myleitner.model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by kh.bakhtiari on 5/27/2014.
 */
public class WebRequest {
    public static final String REQUEST_PING = "ping";
    public static final String REQUEST_WELCOME = "welcome";
    public static final String REQUEST_CHANGES = "lastChanges";
    public static final String REQUEST_REGISTER_DEVICE = "registerDevice";
    private static final String PARAM_UDK = "udk";
    @Expose
    String requestName;
    @Expose
    private ArrayList<Param> params;
    private boolean paramChanged;
    private String jsonParams;

    public WebRequest(Context context, String requestName) {
        this.requestName = requestName;
        this.paramChanged = true;
        addParam(PARAM_UDK, Device.getUDK(context));
    }

    public void addParam(String name, String value) {
        if (params == null)
            params = new ArrayList<Param>();

        params.add(new Param(name, value));
        paramChanged = true;
    }

    public String getJsonParams() {
        if (!paramChanged)
            return jsonParams;

        jsonParams = getGson().toJson(this);
        paramChanged = false;

        return jsonParams;
    }

    private Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        return gsonBuilder.create();

    }

    private class Param {
        @Expose
        public String name;
        @Expose
        public String value;

        public Param(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }
}
