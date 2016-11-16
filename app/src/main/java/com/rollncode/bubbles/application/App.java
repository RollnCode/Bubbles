package com.rollncode.bubbles.application;

import android.app.Application;

import com.rollncode.bubbles.util.SavingUtils;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * @author Maxim Ambroskin kkxmshu@gmail.com
 * @since 18.07.16
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        {
            AContext.init(this);
            SavingUtils.init();
        }
    }

}
