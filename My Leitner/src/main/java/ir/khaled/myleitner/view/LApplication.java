package ir.khaled.myleitner.view;

import android.app.Application;

import ir.khaled.myleitner.model.Device;

/**
 * Created by khaled on 9/11/2014.
 */
public class LApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Device.initiate(this);
    }

}
