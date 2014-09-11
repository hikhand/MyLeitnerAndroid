package ir.khaled.myleitner.dialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ir.khaled.myleitner.R;
import ir.khaled.myleitner.helper.StorageHelper;
import ir.khaled.myleitner.interfaces.ResponseListener;
import ir.khaled.myleitner.model.Device;
import ir.khaled.myleitner.webservice.Request;
import ir.khaled.myleitner.webservice.Response;
import ir.khaled.myleitner.webservice.WebClient;

/**
 * Created by kh.bakhtiari on 5/25/2014.
 */
public class Welcome extends AppDialog implements ResponseListener<Welcome>, View.OnClickListener {
    private static final String S_IS_FIRST_RUN = "isFirstRun";
    private static final String PARAM_VERSION_CODE = "versionCode";

    @SerializedName("message")
    @Expose
    private String message;

    public Welcome(Context context) {
        super(context);
    }

    /**
     * shows welcome dialog only if its apps first run
     */
    public static void showIfFirstRun(Context context) {
//        if (!isAppFirstRun(context))
//            return;//TODO uncomment this two lines

        new Welcome(context).show();
    }

    /**
     * checks whether its apps first run or not <br/>
     *
     * @param context used to get shared preferences
     * @return true if is apps first run, false otherwise
     */
    private static boolean isAppFirstRun(Context context) {
        return StorageHelper.getSharedPreferences(context).getBoolean(S_IS_FIRST_RUN, true);
    }

    @Override
    public void show() {
        init();
        callWebService();
        super.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        setIsNotFirstRun();//TODO uncomment this line
    }

    private void init() {
        setTitle(R.string.welcomeTitle);
        setPositiveButton(context.getResources().getString(R.string.ok), this);
        startLoading(context.getString(R.string.welcomeLoadingMessage));
    }

    private void callWebService() {
        Request request = new Request(context, Request.Method.WELCOME);
        request.addParam(PARAM_VERSION_CODE, Device.getInstance(context).appVersionCode + "");

        WebClient<Welcome> webClient = new WebClient<Welcome>(request, WebClient.Connection.PERMANENT, this);
        webClient.start();
    }

    private void setIsNotFirstRun() {
        SharedPreferences.Editor editor = StorageHelper.getSharedPreferencesEditor(context);
        editor.putBoolean(S_IS_FIRST_RUN, false);
        editor.commit();
    }

    @Override
    public void onResponse(Welcome welcome) {
        stopLoading();
        setContentView(welcome.message);
    }

    @Override
    public void onResponseError(Response<Welcome> response) {
        showError(null, context.getString(R.string.retry), new OnClickError());
    }

    @Override
    public void onClick(View view) {
        //on click ok
        if (view.getId() == ID_BTN_POS) {
            dismiss();
        }
    }

    private class OnClickError implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            init();
            callWebService();
        }
    }
}
