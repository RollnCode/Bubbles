package com.rollncode.bubbles.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.rollncode.bubbles.game.interfaces.ThreadState;

/**
 * @author Maxim Ambroskin kkxmshu@gmail.com
 * @since 19.07.16
 */
public abstract class BaseThread extends Thread {

    final SurfaceHolder mHolder;

    long mLastTime;
    int mWidth;
    int mHeight;

    Paint mMainPaint;
    Paint mSecondaryPaint;

    boolean mRunning;
    @ThreadState int mState;

    private Handler mHandler;

    BaseThread(SurfaceHolder holder) {
        mHolder = holder;

        mMainPaint = new Paint();
        mMainPaint.setAntiAlias(true);
        mMainPaint.setTextSize(30);

        mSecondaryPaint = new Paint();
        mSecondaryPaint.setAntiAlias(true);
        mSecondaryPaint.setColor(Color.WHITE);
    }

    @Override
    public void run() {
        while (mRunning) {
            Canvas canvas = null;
            try {
                canvas = mHolder.lockCanvas();

                synchronized (mHolder) {
                    if (mState == ThreadState.RUNNING) {
                        update();
                    }
                    draw(canvas);
                }

            } finally {
                if (canvas != null) {
                    mHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    public void pause() {
        setState(ThreadState.PAUSED);
    }

    public void unpause() {
        synchronized (mHolder) {
            mLastTime = System.currentTimeMillis() + 100;
        }
        setState(ThreadState.RUNNING);
    }

    private void setState(@ThreadState int state) {
        synchronized (mHolder) {
            mState = state;
        }
    }

    public void setRunning(boolean running) {
        mRunning = running;
    }

    public void setSurfaceSize(int width, int height) {
        synchronized (mHolder) {
            mWidth = width;
            mHeight = height;
        }
    }

    protected abstract void update();
    protected abstract void draw(Canvas canvas);
    public abstract void onTouchEvent(MotionEvent event);

    Handler getHandler() {
        return mHandler;
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }
}
