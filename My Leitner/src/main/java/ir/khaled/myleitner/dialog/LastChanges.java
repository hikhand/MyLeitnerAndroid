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
 * Created by kh.bakhtiari on 5/27/2014.
 */
public class LastChanges extends AppDialog implements ResponseListener<LastChanges>, View.OnClickListener {
    private static final String S_LAST_VERSION_CODE = "lastVersionCode";
    private static final String PARAM_VERSION_CODE = "versionCode";
    @SerializedName("lastChanges")
    @Expose
    private String lastChanges;

    public LastChanges(Context context) {
        super(context);
    }

    /**
     * shows welcome dialog only if its apps first run
     */
    public static void showIfIsNewVersion(Context context) {
//        if (!isUpdateVersion(context))
//            return;//TODO uncomment this two lines


        new LastChanges(context).show();
    }

    /**
     * checks whether its apps first run in the updated version.
     *
     * @return true if is apps first run in the updated version, false otherwise
     */
    private static boolean isUpdateVersion(Context context) {
        int lastVersionCode = StorageHelper.getSharedPreferences(context).getInt(S_LAST_VERSION_CODE, 0);

        if (lastVersionCode == 0) {/** is the first installed version. */
            SharedPreferences.Editor editor = StorageHelper.getSharedPreferencesEditor(context);
            editor.putInt(S_LAST_VERSION_CODE, Device.getInstance(context).appVersionCode);
            editor.commit();
            return false;
        } else if (Device.getInstance(context).appVersionCode > lastVersionCode) {/** this is updated version*/
            return true;
        }
        return false;
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
        setNotUpdateVersion();//TODO uncomment this line
    }

    private void init() {
        setPositiveButton(context.getResources().getString(R.string.ok), this);
        setTitle(R.string.lastChangesTitle);
        startLoading(context.getString(R.string.lastChangesLoadingMessage));
    }

    /**
     * start a new thread to get lastChanges
     */
    private void callWebService() {
        Request request = new Request(context, Request.Method.REGISTER_DEVICE);
        request.addParam(PARAM_VERSION_CODE, Device.getInstance(context).appVersionCode + "");

        WebClient<LastChanges> webClient = new WebClient<LastChanges>(request, WebClient.Connection.PERMANENT, this);
        webClient.start();
    }

    private void setNotUpdateVersion() {
        SharedPreferences.Editor editor = StorageHelper.getSharedPreferencesEditor(context);
        editor.putInt(S_LAST_VERSION_CODE, Device.getInstance(context).appVersionCode);
        editor.commit();
    }

    @Override
    public void onResponse(LastChanges changes) {
        stopLoading();
        setContentView(changes.lastChanges);
    }


    @Override
    public void onResponseError(Response<LastChanges> response) {
        showError(null, context.getString(R.string.retry), new OnClickError());
    }

    @Override
    public void onClick(View view) {
        //on click OK
        dismiss();
    }

    private class OnClickError implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            init();
            callWebService();
        }
    }
}