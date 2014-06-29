package ir.khaled.myleitner.webservice;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.util.HashMap;

import ir.khaled.myleitner.model.Device;

/**
 * Created by kh.bakhtiari on 5/27/2014.
 */
public class Request {
    public static final String REQUEST_PING = "ping";
    public static final String REQUEST_WELCOME = "welcome";
    public static final String REQUEST_CHANGES = "lastChanges";
    public static final String REQUEST_REGISTER_DEVICE = "registerDevice";
    public static final String REQUEST_ADD_CARD = "addCard";
    private static final String PARAM_UDK = "udk";
    @Expose
    public String requestName;
    @Expose
    private HashMap<String, String> params;
    private boolean paramChanged;
    private String jsonParams;

    public Request(Context context, String requestName) {
        this.requestName = requestName;
        this.paramChanged = true;
        addParam(PARAM_UDK, Device.getUDK(context));
    }

    public void addParam(String name, String value) {
        if (params == null)
            params = new HashMap<String, String>();

        params.put(name, value);
        paramChanged = true;
    }

    /**
     * override this method to add params which need to be added from worker thread like making a json by gson
     */
    public HashMap<String, String> getExtraParams() {
        return null;
    }

    /**
     * this method shouldn't be called from main thread
     * @return
     */
    public String getJsonParams() {
        if (!paramChanged)
            return jsonParams;

        HashMap<String, String> extraParams = getExtraParams();
        if (extraParams != null)
            params.putAll(getExtraParams());

        jsonParams = getGson().toJson(this, Request.class);
        paramChanged = false;

        return jsonParams;
    }

    private Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        return gsonBuilder.create();

    }
}
