package com.rollncode.bubbles.game.interfaces;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Maxim Ambroskin kkxmshu@gmail.com
 * @since 15.07.16
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({Complexity.EASY, Complexity.MEDIUM, Complexity.HARD, Complexity.EXPERT})
public @interface Complexity {
    int EASY = 0;
    int MEDIUM = 1;
    int HARD = 2;
    int EXPERT = 3;
}
