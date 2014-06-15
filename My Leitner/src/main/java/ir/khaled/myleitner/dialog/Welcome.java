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
 * Created by kh.bakhtiari on 5/25/2014.
 */
public class Welcome extends AppDialog implements ResponseReceiveListener<Welcome> {
    private static final String S_IS_FIRST_RUN = "isFirstRun";
    private static final String PARAM_VERSION_CODE = "versionCode";
    @Expose
    private String message;
    private Handler handlerUI = new Handler();

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

    @Override
    public void show() {
        getWelcome();
        setTitle(R.string.welcomeTitle);
        setPositiveButton(context.getResources().getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        startLoading(context.getString(R.string.lastChangesLoadingMessage));
        super.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        setIsNotFirstRun();//TODO uncomment this line
    }


    private void getWelcome() {
        WebRequest request = new WebRequest(context, WebRequest.REQUEST_WELCOME);
        request.addParam(PARAM_VERSION_CODE, Device.getInstance(context).appVersionCode + "");

        Type myType = new TypeToken<WebResponse<Welcome>>() {
        }.getType();
        WebClient<Welcome> webClient = new WebClient<Welcome>(context, request, false, myType, this);
        webClient.start();
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

    private void setIsNotFirstRun() {
        SharedPreferences.Editor editor = StorageHelper.getSharedPreferencesEditor(context);
        editor.putBoolean(S_IS_FIRST_RUN, false);
        editor.commit();
    }

    @Override
    public void onResponseReceived(final Welcome welcome) {
        handlerUI.post(new Runnable() {
            @Override
            public void run() {
                stopLoading();
                setContentView(welcome.message);
            }
        });
    }

    @Override
    public void onResponseReceiveFailed(WebResponse<Welcome> response) {
        handlerUI.post(new Runnable() {
            @Override
            public void run() {
                showError();
            }
        });
    }
}
