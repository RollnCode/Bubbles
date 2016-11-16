package com.rollncode.bubbles.util;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.rollncode.bubbles.R;
import com.rollncode.bubbles.application.AContext;
import com.rollncode.bubbles.game.model.Bubble;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * @author Maxim Ambroskin kkxmshu@gmail.com
 * @since 14.07.16
 */
public class Utils {

    public static void setPlayable(final ArrayList<Bubble> bubbles, boolean playable) {
        for (Bubble bubble : bubbles) {
            bubble.setPlayable(playable);
        }
    }

    public static int[] getLevelsRange() {
        final int[] arr = new int[3];
        final int[] min = AContext.getIntegerArray(R.array.levels_min);
        final int[] max = AContext.getIntegerArray(R.array.levels_max);
        for (int i = 0; i < arr.length; i++) {
            arr[i] = AContext.nextInt((max[i] - min[i]) + 1) + min[i];
        }
        return arr;
    }

    public static boolean receiveObjects(@Nullable WeakReference<ObjectsReceiver> weakReceiver,
                                         @IdRes int code, @NonNull Object... objects) {
        final ObjectsReceiver receiver = weakReceiver == null ? null : weakReceiver.get();
        final boolean received = receiver != null;
        if (received) {
            receiver.onObjectsReceive(code, objects);
        }
        return received;
    }

}
