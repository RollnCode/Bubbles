package com.rollncode.bubbles.game.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

/**
 * @author Maxim Ambroskin kkxmshu@gmail.com
 * @since 21.07.16
 */
abstract class GameObject {

    float mX;
    float mY;
    int mRadius;

    public abstract void update(long time);
    public abstract void draw(@NonNull Canvas canvas, @NonNull Paint paint);

    float getX() {
        return mX;
    }

    float getY() {
        return mY;
    }

    int getRadius() {
        return mRadius;
    }

}
