package ir.khaled.myleitner.webservice;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;

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
    private ArrayList<Param> params;
    private boolean paramChanged;
    private String jsonParams;

    public Request(Context context, String requestName) {
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

    /**
     * override this method to add params which need to be added from worker thread like making a json by gson
     */
    public ArrayList<Param> getExtraParams() {
        return null;
    }

    /**
     * this method shouldn't be called from main thread
     * @return
     */
    public String getJsonParams() {
        if (!paramChanged)
            return jsonParams;

        ArrayList<Param> extraParams = getExtraParams();
        if (extraParams != null)
            params.addAll(getExtraParams());

        jsonParams = getGson().toJson(this, Request.class);
        paramChanged = false;

        return jsonParams;
    }

    private Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        return gsonBuilder.create();

    }

    public class Param {
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
