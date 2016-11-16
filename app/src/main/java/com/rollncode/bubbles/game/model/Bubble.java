package com.rollncode.bubbles.game.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import com.rollncode.bubbles.application.AContext;

/**
 * @author Maxim Ambroskin kkxmshu@gmail.com
 * @since 14.07.16
 */
public class Bubble extends GameObject {

    private float mOriginalX;
    private float mSpeed;
    private int mWave;

    private boolean mEscaped;
    private boolean mBusted;
    private boolean mPlayable;

    public Bubble(@NonNull Level level, int width, int height, boolean playable) {
        final int speed = AContext.nextInt((level.getMaxSpeed() - level.getMinSpeed()) + 1) + level.getMinSpeed();
        mSpeed = AContext.toDp(speed);
        mRadius = AContext.nextInt((level.getMaxRadius()- level.getMinRadius()) + 1) + level.getMinRadius();
        mRadius = (int) AContext.toDp(mRadius);
        mY = height + mRadius;
        mWave = 2 * mRadius;
        mX = AContext.nextInt(((width - mWave / 2) - mWave / 2) + 1) + mWave / 2;
        mOriginalX = mX;

        mPlayable = playable;
        mEscaped = false;
        mBusted = false;
    }

    @Override
    public void update(long time) {
        mY -= mSpeed;
        mX = (float) (mWave * Math.sin(time * 0.002) + mOriginalX);

        if (mY < -mRadius * 2) {
            mEscaped = true;
        }
    }

    public int collide(MotionEvent event, int statusBarHeight) {
        statusBarHeight = VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP ? 0 : statusBarHeight;
        final float x = event.getX();
        final float y = event.getY() - statusBarHeight;
        final double distance = Math.sqrt(Math.pow(x - mX, 2) + Math.pow(y - mY, 2));
        if (distance < mRadius) {
            mBusted = true;
            return 1;
        }
        return 0;
    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull Paint paint) {
        canvas.drawCircle(mX, mY, mRadius, paint);
    }

    float getSpeed() {
        return mSpeed;
    }

    public boolean isBusted() {
        return mBusted;
    }

    public void setBusted(boolean busted) {
        mBusted = busted;
    }

    public boolean isEscaped() {
        return mEscaped;
    }

    public void setEscaped(boolean escaped) {
        mEscaped = escaped;
    }

    public boolean isPlayable() {
        return mPlayable;
    }

    public void setPlayable(boolean playable) {
        mPlayable = playable;
    }
}
