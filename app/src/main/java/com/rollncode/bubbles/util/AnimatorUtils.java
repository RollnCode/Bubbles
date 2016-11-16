package com.rollncode.bubbles.util;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.Drawable;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rollncode.bubbles.R;

/**
 * @author Maxim Ambroskin kkxmshu@gmail.com
 * @since 22.07.16
 */
public class AnimatorUtils {

    public static Animator flipHorizontally(final TextView textView, final @StringRes int resId) {
        final AnimatorSet set = new AnimatorSet();
        final String sourceText = (String) textView.getText();
        final Drawable drawable = textView.getCompoundDrawables()[2];

        ObjectAnimator close1 = ObjectAnimator.ofFloat(textView, View.SCALE_X, 1, 0);
        close1.addListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                textView.setEnabled(false);
            }

            @Override public void onAnimationEnd(Animator animation) {
                textView.setText(resId);
                textView.setCompoundDrawables(null, null, null, null);
            }
            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}
        });
        ObjectAnimator open1 = ObjectAnimator.ofFloat(textView, View.SCALE_X, 0, 1);
        ObjectAnimator close2 = ObjectAnimator.ofFloat(textView, View.SCALE_X, 1, 0);
        close2.setStartDelay(2000);
        ObjectAnimator open2 = ObjectAnimator.ofFloat(textView, View.SCALE_X, 0, 1);
        open2.addListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                textView.setText(sourceText);
                textView.setCompoundDrawables(null, null, drawable, null);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                textView.setEnabled(true);
            }

            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}
        });
        set.playSequentially(close1, open1, close2, open2);
        return set;
    }

    public static Animator flipVertically(final LinearLayout ll) {
        final AnimatorSet set = new AnimatorSet();
        final TextView btnFirst = (TextView) ll.findViewById(R.id.btn_first);
        final TextView btnSecond = (TextView) ll.findViewById(R.id.btn_second);
        final boolean isOpen = btnSecond.getVisibility() == View.VISIBLE;

        ObjectAnimator close1 = ObjectAnimator.ofFloat(ll, View.SCALE_X, 1, 0);
        close1.addListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                ll.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                btnFirst.setText(isOpen ? R.string.play : R.string.continue_game);
                btnSecond.setVisibility(isOpen ? View.GONE : View.VISIBLE);
            }

            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}
        });
        ObjectAnimator open1 = ObjectAnimator.ofFloat(ll, View.SCALE_X, 0, 1);
        set.playSequentially(close1, open1);
        return set;
    }

}
