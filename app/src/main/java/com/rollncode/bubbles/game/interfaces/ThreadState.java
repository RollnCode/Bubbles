package com.rollncode.bubbles.game.interfaces;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Maxim Ambroskin kkxmshu@gmail.com
 * @since 14.07.16
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({ThreadState.RUNNING, ThreadState.PAUSED})
public @interface ThreadState {
    int RUNNING = 0xAA;
    int PAUSED = 0xBB;
}
