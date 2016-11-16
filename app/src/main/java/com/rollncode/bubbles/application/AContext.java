package com.rollncode.bubbles.application;

import android.content.res.Resources;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

import java.util.Random;

/**
 * @author Maxim Ambroskin kkxmshu@gmail.com
 * @since 18.07.16
 */
public class AContext {

    private static AContext sInstance;

    private final App mApp;
    private final Resources mResources;
    private final int mStatusBarHeight;
    private final Random mRandom;

    static void init(@NonNull App app) {
        sInstance = new AContext(app);
    }

    private AContext(@NonNull App app) {
        mApp = app;
        mResources = mApp.getResources();
        mRandom = new Random();
        {
            final int resourceId = mResources.getIdentifier("status_bar_height", "dimen", "android");
            mStatusBarHeight = resourceId > 0 ? mResources.getDimensionPixelSize(resourceId) : 0;
        }
    }

    public static int getInteger(@IntegerRes int id) {
        return sInstance.mResources.getInteger(id);
    }

    public static int[] getIntegerArray(@ArrayRes int id) {
        return sInstance.mResources.getIntArray(id);
    }

    public static int nextInt(int n) {
        return sInstance.mRandom.nextInt(n);
    }

    public static Random getRandom() {
        return sInstance.mRandom;
    }

    public static int getColor(@ColorRes int colorRes) {
        return ContextCompat.getColor(sInstance.mApp, colorRes);
    }

    @NonNull
    public static String getString(@StringRes int stringRes) {
        return sInstance.mResources.getString(stringRes);
    }

    @NonNull
    public static String getString(@StringRes int stringRes, @NonNull Object... objects) {
        return sInstance.mResources.getString(stringRes, objects);
    }

    public static App getApp() {
        return sInstance.mApp;
    }

    public static Resources getResources() {
        return sInstance.mResources;
    }

    public static int getStatusBarHeight() {
        return sInstance.mStatusBarHeight;
    }

    public static float toDp(float value) {
        return sInstance.mResources.getDisplayMetrics().density * value;
    }

    public static int getStatusBarPadding(boolean forBubbles) {
        return forBubbles ?
                (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP ? 0 : sInstance.mStatusBarHeight):
                (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP ? sInstance.mStatusBarHeight : 0);
    }

}
