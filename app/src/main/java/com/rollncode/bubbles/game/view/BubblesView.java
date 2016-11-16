package com.rollncode.bubbles.game.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.rollncode.bubbles.R;
import com.rollncode.bubbles.game.GameThread;

/**
 * @author Maxim Ambroskin kkxmshu@gmail.com
 * @since 14.07.16
 */
public class BubblesView extends SurfaceView
        implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private GameThread mThread;

    public BubblesView(Context context) {
        this(context, null);
    }

    public BubblesView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubblesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHolder = getHolder();
        mHolder.addCallback(this);
        setFocusable(true);
        final TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BubblesView, 0, 0);
        final boolean playable = a.getBoolean(R.styleable.BubblesView_isPlayable, true);
        if (playable) {
            mThread = new GameThread(mHolder);
        }
    }

    public GameThread getThread() {
        return mThread;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {}

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (mThread.getState() == Thread.State.TERMINATED) {
            mThread = new GameThread(surfaceHolder);
        }
        mThread.setRunning(true);
        mThread.start();
    }

    public void setPlaying(boolean playing) {
        mThread.setPlaying(playing);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        mThread.setSurfaceSize(i1, i2);
    }

    public void setHandler(Handler handler) {
        mThread.setHandler(handler);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mThread.onTouchEvent(event);
            return true;
        }
        return false;
    }

    public void save() {
        mThread.save();
    }

    public void load() {
        mThread.load();
    }

    /*
     * Callback invoked when the Surface has been destroyed and must no longer
     * be touched. WARNING: after this method returns, the Surface/Canvas must
     * never be touched again!
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        mThread.pause();
        mThread.setRunning(false);
        while (retry) {
            try {
                mThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
