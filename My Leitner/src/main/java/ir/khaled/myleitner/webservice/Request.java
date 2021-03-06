package ir.khaled.myleitner.webservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.lang.reflect.Type;
import java.util.HashMap;

import ir.khaled.myleitner.model.Device;

/**
 * Created by kh.bakhtiari on 5/27/2014.
 */
public class Request {
    private static final String PARAM_UDK = "udk";
    @Expose
    public String requestName;
    public Type type;
    @Expose
    private HashMap<String, String> params;
    private boolean paramChanged;
    private String jsonParams;
    public ConType connection;

    public Request(Method method, ConType connection) {
        this.type = method.type;
        this.requestName = method.request;
        this.paramChanged = true;
        this.connection = connection;
        addParam(PARAM_UDK, Device.getUDK());
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
     */
    public String getJsonRequest() {
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

    public static enum Method {
        PING("ping", Types.booleanT()),
        WELCOME("welcome", Types.welcome()),
        CHANGES("lastChanges", Types.lastChanges()),
        REGISTER_DEVICE("registerDevice", Types.booleanT()),
        ADD_CARD("addCard", Types.booleanT()),
        LOGIN("login", Types.user()),
        REGISTER("register", Types.user());

        String request;
        Type type;

        Method(String request, Type type) {
            this.request = request;
            this.type = type;
        }
    }

    public static enum ConType {
        PERMANENT,
        IMMEDIATE
    }
}
