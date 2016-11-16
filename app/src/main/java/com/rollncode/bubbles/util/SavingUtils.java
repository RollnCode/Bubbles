package com.rollncode.bubbles.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.rollncode.bubbles.application.AContext;
import com.rollncode.bubbles.game.model.Level;

/**
 * @author Maxim Ambroskin kkxmshu@gmail.com
 * @since 22.07.16
 */
public class SavingUtils {

    private static final String KEY_0 = "KEY_0"; // taps
    private static final String KEY_1 = "KEY_1"; // busted
    private static final String KEY_2 = "KEY_2"; // escaped

    private static final String KEY_3 = "KEY_3"; // minSpeed
    private static final String KEY_4 = "KEY_4"; // maxSpeed
    private static final String KEY_5 = "KEY_5"; // minRadius
    private static final String KEY_6 = "KEY_6"; // maxRadius
    private static final String KEY_7 = "KEY_7"; // bubblesCount
    private static final String KEY_8 = "KEY_8"; // waiting

    private static SavingUtils sInstance;

    private final SharedPreferences mPreferences;

    public static void init() {
        sInstance = new SavingUtils(AContext.getApp());
    }

    public static SavingUtils getInstance() {
        return sInstance;
    }

    private SavingUtils(@NonNull Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean isSaved() {
        return mPreferences.getInt(KEY_0, 0) != 0;
    }

    public void save(int taps, int busted, int escaped, @NonNull Level level) {
        mPreferences.edit()
                .putInt(KEY_0, taps)
                .putInt(KEY_1, busted)
                .putInt(KEY_2, escaped)
                .putInt(KEY_3, level.getMinSpeed())
                .putInt(KEY_4, level.getMaxSpeed())
                .putInt(KEY_5, level.getMinRadius())
                .putInt(KEY_6, level.getMaxRadius())
                .putInt(KEY_7, level.getBubblesCount())
                .putFloat(KEY_8, level.getWaiting())
                .apply();
    }

    public int[] getInformation() {
        int[] info = new int[3];

        info[0] = mPreferences.getInt(KEY_0, 0);
        info[1] = mPreferences.getInt(KEY_1, 0);
        info[2] = mPreferences.getInt(KEY_2, 0);

        return info;
    }

    public void clear() {
        mPreferences.edit().clear().apply();
    }

    public Level getLevel() {
        final int minSpeed = mPreferences.getInt(KEY_3, 0);
        final int maxSpeed = mPreferences.getInt(KEY_4, 0);
        final int minRadius = mPreferences.getInt(KEY_5, 0);
        final int maxRadius = mPreferences.getInt(KEY_6, 0);
        final int bubblesCount = mPreferences.getInt(KEY_7, 1);
        final float waiting = mPreferences.getFloat(KEY_8, 0);

        return new Level(minSpeed, maxSpeed, minRadius, maxRadius, bubblesCount, waiting);
    }

}
