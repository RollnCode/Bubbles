package com.rollncode.bubbles.game.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import com.rollncode.bubbles.R;
import com.rollncode.bubbles.application.AContext;

/**
 * @author Maxim Ambroskin kkxmshu@gmail.com
 * @since 15.07.16
 */
public class Particle extends GameObject {

    private float mVelocityX;
    private float mVelocityY;
    private int mOpacity;

    private boolean mRemoved;

    public Particle(@NonNull Bubble owner) {
        mX = owner.getX();
        mY = owner.getY() + AContext.nextInt(owner.getRadius() + 20);
        final int direction = AContext.nextInt(2) == 1 ? 1 : -1;
        mVelocityX = AContext.nextInt(AContext.getInteger(R.integer.particles_velocity)) * direction;
        mVelocityY = owner.getSpeed();
        mRadius = (int) AContext.toDp(AContext.nextInt(AContext.getInteger(R.integer.particles_radius)));
        mOpacity = AContext.nextInt(AContext.getInteger(R.integer.particles_opacity));

        mRemoved = false;
    }

    @Override
    public void update(long time) {
        mX += mVelocityX;
        mY -= mVelocityY;

        mVelocityX *= 0.99;
        mVelocityY *= 0.99;
        mOpacity -= 1;

        if (mY <= 0 || mOpacity <= 0) {
            mRemoved = true;
        }
    }

    public void draw(@NonNull Canvas canvas, @NonNull Paint paint) {
        paint.setAlpha(mOpacity);
        canvas.drawCircle(mX, mY, mRadius, paint);
    }

    public boolean isRemoved() {
        return mRemoved;
    }

}
