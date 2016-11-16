package com.rollncode.bubbles.game.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.rollncode.bubbles.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vojko Vladimir vojkovladimir@gmail.com
 * @since 13.04.16.
 */
public class RatingView extends LinearLayoutCompat {

    @DrawableRes
    private static final int DEFAULT_DRAWABLE_NORMAL = R.drawable.vec_heart_empty;
    @DrawableRes
    private static final int DEFAULT_DRAWABLE_SELECTED = R.drawable.vec_heart_full;
    @DrawableRes
    private static final int DEFAULT_DRAWABLE_HALF = R.drawable.vec_heart_half;

    private static final int DEFAULT_STAR_NUMBERS = 5;
    private static final int DEFAULT_STAR_SPACING = 0;

    //VIEW's
    private final List<ImageView> mStars;

    //VALUE's
    private Drawable mDrawableNormal;
    private Drawable mDrawableHalf;
    private Drawable mDrawableSelected;

    private float mRating;
    private boolean mIsIndicator;
    private int mNumStars;
    private int mStarSize;
    private int mStarSpacing;

    private int mMinimum;

    public RatingView(@NonNull Context context) {
        this(context, null);
    }

    public RatingView(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingView(@NonNull Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        {
            super.setOrientation(HORIZONTAL);
            super.setGravity(Gravity.CENTER_VERTICAL);
        }
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RatingView, defStyle, 0);

        mDrawableNormal = a.getDrawable(R.styleable.RatingView_rv_drawable_normal);
        mDrawableHalf = a.getDrawable(R.styleable.RatingView_rv_drawable_half);
        mDrawableSelected = a.getDrawable(R.styleable.RatingView_rv_drawable_selected);

        mNumStars = a.getInteger(R.styleable.RatingView_rv_num_stars, DEFAULT_STAR_NUMBERS);

        if (mDrawableNormal == null) {
            mDrawableNormal = ContextCompat.getDrawable(context, DEFAULT_DRAWABLE_NORMAL);
        }
        if (mDrawableSelected == null) {
            mDrawableSelected = ContextCompat.getDrawable(context, DEFAULT_DRAWABLE_SELECTED);
        }
        if (mDrawableHalf == null) {
            mDrawableHalf = ContextCompat.getDrawable(context, DEFAULT_DRAWABLE_HALF);
        }

        final int starSize = getResources().getDimensionPixelOffset(R.dimen.icon_size_small);
        mStarSize = a.getDimensionPixelSize(R.styleable.RatingView_rv_star_size, starSize);
        mStarSpacing = a.getDimensionPixelSize(R.styleable.RatingView_rv_star_spacing, DEFAULT_STAR_SPACING);

        mIsIndicator = a.getBoolean(R.styleable.RatingView_rv_is_indicator, true);

        mStars = new ArrayList<>();

        prepareStars();
        setRatingInner(a.getFloat(R.styleable.RatingView_rv_rating, 0F));

        a.recycle();
    }

    public void setMinimum(int minimum) {
        mMinimum = minimum;
    }

    private void prepareStars() {
        if (mNumStars > mStars.size()) {
            for (int i = mStars.size(); i < mNumStars; i++) {
                final ImageView imageView = getStarView();
                final LayoutParams params = new LayoutParams(mStarSize, mStarSize);
                if (i != mNumStars - 1 && VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
                    params.setMarginEnd(mStarSpacing);
                }

                mStars.add(i, imageView);
                addViewInLayout(imageView, i, params, true);
            }
        } else if (mNumStars < mStars.size()) {
            for (int i = mStars.size() - 1; i > mNumStars - 1; i--) {
                final View view = mStars.get(i);
                removeView(view);
            }
        }
    }

    public void setRating(float rating) {
        setRatingInner(rating);
    }

    private void setRatingInner(float rating) {
        if (rating > mNumStars) {
            rating = mNumStars;

        } else if (rating < mMinimum) {
            rating = mMinimum;
        }
        final int selectedStars = (int) (rating / 1F);
        final boolean halfSelected = rating % 1F >= 0.5F;
        mRating = rating + (halfSelected ? 1 : 0);

        for (int i = 0; i < mStars.size(); i++) {
            final ImageView imageView = mStars.get(i);
            if (i == selectedStars) {
                setStarSelected(imageView, halfSelected, halfSelected);

            } else {
                setStarSelected(imageView, i < selectedStars);
            }
        }
    }

    public float getRating() {
        return mRating;
    }

    private ImageView getStarView() {
        final ImageView view = new ImageView(getContext());
        view.setImageDrawable(mDrawableNormal);
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        return view;
    }

    private void setStarSelected(ImageView imageView, boolean isSelected) {
        setStarSelected(imageView, isSelected, false);
    }

    private void setStarSelected(ImageView imageView, boolean isSelected, boolean isHalfSelected) {
        if (isSelected) {
            isHalfSelected &= mDrawableHalf != null;
            imageView.setImageDrawable(isHalfSelected ? mDrawableHalf : mDrawableSelected);

        } else {
            imageView.setImageDrawable(mDrawableNormal);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsIndicator || getChildCount() == 0) {
            return false;
        }
        final int action = event.getAction() & MotionEvent.ACTION_MASK;
        final float x = event.getX();
        final float start = getChildAt(0).getLeft();
        final float end = getChildAt(getChildCount() - 1).getRight();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                setRatingInner(x / (end - start) * mNumStars);
                return true;

            default:
                return super.onTouchEvent(event);
        }
    }

}
