package ir.khaled.myleitner.dialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.View;

import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import ir.khaled.myleitner.R;
import ir.khaled.myleitner.helper.StorageHelper;
import ir.khaled.myleitner.interfaces.ResponseReceiveListener;
import ir.khaled.myleitner.model.Device;
import ir.khaled.myleitner.model.WebClient;
import ir.khaled.myleitner.model.WebRequest;
import ir.khaled.myleitner.model.WebResponse;

/**
 * Created by kh.bakhtiari on 5/27/2014.
 */
public class LastChanges extends AppDialog implements ResponseReceiveListener<LastChanges> {
    private static final String S_LAST_VERSION_CODE = "lastVersionCode";
    private static final String PARAM_VERSION_CODE = "versionCode";
    @Expose
    private String lastChanges;
    private Handler handlerUI = new Handler();

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

    @Override
    public void show() {
        getChanges();
        setPositiveButton(context.getResources().getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setTitle(R.string.lastChangesTitle);
        startLoading(context.getString(R.string.lastChangesLoadingMessage));
        super.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        setNotUpdateVersion();//TODO uncomment this line
    }

    /**
     * start a new thread to get lastChanges
     */
    private void getChanges() {
        WebRequest request = new WebRequest(context, WebRequest.REQUEST_CHANGES);
        request.addParam(PARAM_VERSION_CODE, Device.getInstance(context).appVersionCode + "");

        Type myType = new TypeToken<WebResponse<LastChanges>>() {
        }.getType();
        WebClient<LastChanges> webClient = new WebClient<LastChanges>(context, request, WebClient.Connection.PERMANENT, myType, this);
        webClient.start();
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

    private void setNotUpdateVersion() {
        SharedPreferences.Editor editor = StorageHelper.getSharedPreferencesEditor(context);
        editor.putInt(S_LAST_VERSION_CODE, Device.getInstance(context).appVersionCode);
        editor.commit();
    }

    @Override
    public void onResponseReceived(final LastChanges changes) {
        handlerUI.post(new Runnable() {
            @Override
            public void run() {
                stopLoading();
                setContentView(changes.lastChanges);
            }
        });
    }


    @Override
    public void onResponseReceiveFailed(WebResponse<LastChanges> response) {
        handlerUI.post(new Runnable() {
            @Override
            public void run() {
                showError();
            }
        });
    }
}