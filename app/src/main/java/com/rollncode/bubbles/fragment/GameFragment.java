package com.rollncode.bubbles.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rollncode.bubbles.R;
import com.rollncode.bubbles.activity.MainActivity;
import com.rollncode.bubbles.application.AConstant;
import com.rollncode.bubbles.application.AContext;
import com.rollncode.bubbles.dialog.GameDialog;
import com.rollncode.bubbles.game.view.RatingView;
import com.rollncode.bubbles.util.GooglePlayHelper;
import com.rollncode.bubbles.util.ObjectsReceiver;

import java.lang.ref.WeakReference;

import static com.rollncode.bubbles.game.GameThread.ESCAPED_GAME_OVER;

/**
 * @author Maxim Ambroskin kkxmshu@gmail.com
 * @since 14.07.16
 */
public class GameFragment extends BaseFragment
        implements ObjectsReceiver {

    private static final String KEY_0 = "KEY_0"; // load

    private GameDialog mDialog;
    private TextView mTvInfo;
    private RatingView mRvHealth;

    private boolean mLoad;

    public static GameFragment newInstance() {
        return new GameFragment();
    }

    public static GameFragment newInstance(boolean load) {
        Bundle args = new Bundle();
        args.putBoolean(KEY_0, load);
        GameFragment fragment = new GameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialog = new GameDialog(getContext(), this);

        if (savedInstanceState != null) {
            mLoad = savedInstanceState.getBoolean(KEY_0, false);
        } else {
            mLoad = getArguments() != null && getArguments().containsKey(KEY_0)
                    && getArguments().getBoolean(KEY_0, false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvInfo = (TextView) view.findViewById(R.id.tv_info);
        mRvHealth = (RatingView) view.findViewById(R.id.rv_health);
        mTvInfo.setPadding(0, AContext.getStatusBarPadding(false), 0, 0);
        ViewHandler handler = new ViewHandler(mTvInfo, mRvHealth, mDialog);
        ((MainActivity) getContext()).getBubblesView().setHandler(handler);

        if (!mLoad) {
            reset();
        } else {
            ((MainActivity) getContext()).getBubblesView().setPlaying(true);
            ((MainActivity) getContext()).getBubblesView().load();
        }
    }

    private void reset() {
        mTvInfo.setText(AContext.getString(R.string.information_format, 0, 0));
        mRvHealth.setRating(ESCAPED_GAME_OVER / 2);
        ((MainActivity) getContext()).getBubblesView().setPlaying(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity) getContext()).getBubblesView().save();
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
        popBackStack();
    }

    @Override
    public void onObjectsReceive(@IdRes int code, @NonNull Object... objects) {
        final boolean response;
        switch (code) {
            case R.id.code_game_over:
                response = (boolean) objects[0];
                if (response) {
                    reset();

                } else {
                    popBackStack();
                }
                break;

            default:
                break;
        }
    }

    private static class ViewHandler extends Handler {
        private final WeakReference<TextView> mTextView;
        private final WeakReference<RatingView> mRatingView;
        private final WeakReference<GameDialog> mDialog;

        ViewHandler(TextView textView, RatingView ratingView, GameDialog dialog) {
            mTextView = new WeakReference<>(textView);
            mRatingView = new WeakReference<>(ratingView);
            mDialog = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            final int score = msg.getData().getInt(AConstant.KEY_SCORE, -1);
            if (score != -1) {
                final GameDialog dialog = mDialog.get();
                GooglePlayHelper.getInstance().enterScore(score);
                dialog.show(R.id.code_game_over, score);

            } else {
                final TextView textView = mTextView.get();
                final RatingView ratingView = mRatingView.get();
                if (textView != null) {
                    final int accuracy = msg.getData().getInt(AConstant.KEY_ACCURACY, 0);
                    final int escaped = msg.getData().getInt(AConstant.KEY_ESCAPED, 0);
                    final int busted = msg.getData().getInt(AConstant.KEY_BUSTED, 0);
                    final float health = (ESCAPED_GAME_OVER / 2) - escaped * 0.5f;

                    textView.setText(AContext.getString(R.string.information_format, accuracy, busted));
                    if (ratingView != null) {
                        ratingView.setRating(health);
                    }
                }
            }
        }
    }

}
