package com.rollncode.bubbles.game;

import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.rollncode.bubbles.R;
import com.rollncode.bubbles.application.AConstant;
import com.rollncode.bubbles.application.AContext;
import com.rollncode.bubbles.game.interfaces.Complexity;
import com.rollncode.bubbles.game.interfaces.ThreadState;
import com.rollncode.bubbles.game.model.Bubble;
import com.rollncode.bubbles.game.model.Level;
import com.rollncode.bubbles.game.model.Particle;
import com.rollncode.bubbles.util.SavingUtils;
import com.rollncode.bubbles.util.Utils;

import java.util.ArrayList;

/**
 * @author Maxim Ambroskin kkxmshu@gmail.com
 * @since 14.07.16
 */
public class GameThread extends BaseThread {

    public final static int ESCAPED_GAME_OVER = 6;

    private final static double WAITING_TIME = 1.5;
    private final static int BUBBLES_COUNT = 4;
    private final static int PARTICLES_COUNT = 60;

    private ArrayList<Bubble> mBubbles;
    private ArrayList<Particle> mParticles;
    private MotionEvent mEvent;

    private int mTaps;
    private int mBusted;
    private int mEscaped;
    private int mAccuracy;

    private int[] mLevelsRange;

    private Level mLevel;
    private boolean mPlaying;
    private boolean mNeedSave;

    public GameThread(SurfaceHolder holder) {
        super(holder);

        mBubbles = new ArrayList<>();
        mParticles = new ArrayList<>();
        reset();

        mState = ThreadState.RUNNING;
        mRunning = false;
        mPlaying = false;
    }

    @Override
    protected void update() {
        long now = System.currentTimeMillis();
        if (mLastTime > now) {
            return;
        }

        final double elapsed = (now - mLastTime) / 1000.0;
        if (mPlaying) {
            updateGame(now, elapsed);
        } else {
            updateMenu(now, elapsed);
        }
    }

    @Override
    protected void draw(Canvas canvas) {
        if (canvas != null) {
            canvas.drawColor(AContext.getColor(R.color.water));

            mMainPaint.setColor(AContext.getColor(R.color.bubble));
            for (Bubble bubble : mBubbles) {
                bubble.draw(canvas, mMainPaint);
            }

            for (Particle particle : mParticles) {
                particle.draw(canvas, mSecondaryPaint);
            }
        }
    }

    private void updateGame(long now, double elapsed) {
        boolean needSend = false;
        if (mBubbles.size() < mLevel.getBubblesCount() && elapsed >= mLevel.getWaiting()) {
            mBubbles.add(new Bubble(mLevel, mWidth, mHeight, true));
            mLastTime = now;
        }

        final ArrayList<Bubble> toRemove = new ArrayList<>();
        final ArrayList<Particle> toRemoveParticle = new ArrayList<>();
        if (mEvent != null) {
            mTaps++;
        }
        for (Bubble bubble : mBubbles) {
            bubble.update(now);
            if (mEvent != null) {
                mBusted += bubble.collide(mEvent, AContext.getStatusBarPadding(true));
                mAccuracy = Math.round(mBusted * 100.0f / mTaps);
                needSend = true;

                if (bubble.isBusted()) {
                    for (int i = 0; i < 6; i++) {
                        mParticles.add(new Particle(bubble));
                    }
                }
            }

            mEscaped += bubble.isEscaped() && bubble.isPlayable() ? 1 : 0;
            if (bubble.isEscaped() || bubble.isBusted()) {
                needSend = true;
                toRemove.add(bubble);
            }
        }

        for (Particle particle : mParticles) {
            particle.update(now);
            if (particle.isRemoved()) {
                toRemoveParticle.add(particle);
            }
        }

        if (mEscaped >= ESCAPED_GAME_OVER) {
            mNeedSave = false;
            Utils.setPlayable(mBubbles, false);
            final int score = mBusted * mAccuracy * mLevel.getBubblesCount();
            mPlaying = false;
            Message msg = getHandler().obtainMessage();
            Bundle b = new Bundle();
            b.putInt(AConstant.KEY_SCORE, score);
            msg.setData(b);
            getHandler().sendMessage(msg);
        }

        if (mBusted == mLevelsRange[0]) {
            mLevel = new Level(Complexity.MEDIUM);
        } else if (mBusted == mLevelsRange[1]) {
            mLevel = new Level(Complexity.HARD);
        } else if (mBusted == mLevelsRange[2]) {
            mLevel = new Level(Complexity.EXPERT);
        }

        mEvent = null;
        mBubbles.removeAll(toRemove);
        mParticles.removeAll(toRemoveParticle);

        if (needSend) {
            sendInfo();
        }
    }

    private void sendInfo() {
        Message msg = getHandler().obtainMessage();
        Bundle b = new Bundle();
        b.putInt(AConstant.KEY_ACCURACY, mAccuracy);
        b.putInt(AConstant.KEY_ESCAPED, mEscaped);
        b.putInt(AConstant.KEY_BUSTED, mBusted);
        msg.setData(b);
        getHandler().sendMessage(msg);
    }

    private void updateMenu(long now, double elapsed) {
        if (mBubbles.size() < BUBBLES_COUNT && elapsed >= WAITING_TIME) {
            mBubbles.add(new Bubble(new Level(Complexity.HARD), mWidth, mHeight, false));
            mLastTime = now;
        }

        if (mParticles.size() < PARTICLES_COUNT) {
            mParticles.add(new Particle(new Bubble(new Level(Complexity.MEDIUM), mWidth, mHeight, false)));
        }

        final ArrayList<Bubble> toRemove = new ArrayList<>();
        final ArrayList<Particle> toRemoveParticle = new ArrayList<>();

        for (Bubble bubble : mBubbles) {
            bubble.setPlayable(false);
            bubble.update(now);
            if (bubble.isEscaped()) {
                toRemove.add(bubble);
            }
        }

        for (Particle particle : mParticles) {
            particle.update(now);
            if (particle.isRemoved()) {
                toRemoveParticle.add(particle);
            }
        }

        mBubbles.removeAll(toRemove);
        mParticles.removeAll(toRemoveParticle);
    }

    private void reset() {
        mAccuracy = 0;
        mEscaped = 0;
        mBusted = 0;
        mTaps = 0;

        mLevelsRange = Utils.getLevelsRange();
        mLevel = new Level(Complexity.EASY);
        mNeedSave = true;
    }

    public void setPlaying(boolean playing) {
        mPlaying = playing;
        if (mPlaying) {
            reset();
        }
    }

    public void save() {
        if (mBusted > 0 && mNeedSave) {
            SavingUtils.getInstance().save(mTaps, mBusted, mEscaped, mLevel);
        }
    }

    public void onTouchEvent(MotionEvent event) {
        synchronized (mHolder) {
            mEvent = event;
        }
    }

    public void load() {
        final int[] info = SavingUtils.getInstance().getInformation();

        mTaps = info[0];
        mBusted = info[1];
        mEscaped = info[2];
        mAccuracy = Math.round(mBusted * 100.0f / mTaps);

        mLevel = SavingUtils.getInstance().getLevel();
        SavingUtils.getInstance().clear();
        sendInfo();
    }

}