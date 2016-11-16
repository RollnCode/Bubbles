package com.rollncode.bubbles.game.model;

import com.rollncode.bubbles.R;
import com.rollncode.bubbles.application.AContext;
import com.rollncode.bubbles.game.interfaces.Complexity;

/**
 * @author Maxim Ambroskin kkxmshu@gmail.com
 * @since 15.07.16
 */
public class Level {

    private int mBubblesCount;
    private int mMinSpeed;
    private int mMaxSpeed;
    private int mMinRadius;
    private int mMaxRadius;
    private float mWaiting;

    public Level(@Complexity int complexity) {
        mMinSpeed = AContext.getIntegerArray(R.array.min_speed)[complexity];
        mMaxSpeed = AContext.getIntegerArray(R.array.max_speed)[complexity];
        mMinRadius = AContext.getIntegerArray(R.array.min_radius)[complexity];
        mMaxRadius = AContext.getIntegerArray(R.array.max_radius)[complexity];
        mBubblesCount = AContext.getIntegerArray(R.array.bubbles)[complexity];
        mWaiting = AContext.getIntegerArray(R.array.waiting_time_millis)[complexity] / 1000;
    }

    public Level(int minSpeed, int maxSpeed, int minRadius,
                 int maxRadius, int bubblesCount, float waiting) {
        mBubblesCount = bubblesCount;
        mMinSpeed = minSpeed == 0 ? AContext.getIntegerArray(R.array.min_speed)[mBubblesCount - 1] : minSpeed;
        mMaxSpeed = maxSpeed == 0 ? AContext.getIntegerArray(R.array.max_speed)[mBubblesCount - 1] : maxSpeed;
        mMinRadius = minRadius == 0 ? AContext.getIntegerArray(R.array.min_radius)[mBubblesCount - 1] : minRadius;
        mMaxRadius = maxRadius == 0 ? AContext.getIntegerArray(R.array.max_radius)[mBubblesCount - 1] : maxRadius;
        mWaiting = waiting == 0 ? AContext.getIntegerArray(R.array.waiting_time_millis)[mBubblesCount - 1] : waiting;
    }

    public int getBubblesCount() {
        return mBubblesCount;
    }

    public int getMinSpeed() {
        return mMinSpeed;
    }

    public int getMaxSpeed() {
        return mMaxSpeed;
    }

    public int getMinRadius() {
        return mMinRadius;
    }

    public int getMaxRadius() {
        return mMaxRadius;
    }

    public float getWaiting() {
        return mWaiting;
    }

}
