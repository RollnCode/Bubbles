package com.rollncode.bubbles.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.games.Games;
import com.rollncode.bubbles.R;
import com.rollncode.bubbles.activity.MainActivity;
import com.rollncode.bubbles.application.AContext;
import com.rollncode.bubbles.util.AnimatorUtils;
import com.rollncode.bubbles.util.GooglePlayHelper;
import com.rollncode.bubbles.util.SavingUtils;

import static com.rollncode.bubbles.util.GooglePlayHelper.REQUEST_GOOGLE_PLAY_SERVICES;

/**
 * @author Maxim Ambroskin kkxmshu@gmail.com
 * @since 18.07.16
 */
public class MenuFragment extends BaseFragment {

    private LinearLayout mLlGame;
    private TextView mBtnLeaderboards;

    public static MenuFragment newInstance() {
        return new MenuFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        (view.findViewById(R.id.image)).setPadding(0, AContext.getStatusBarPadding(false), 0, 0);
        mLlGame = (LinearLayout) view.findViewById(R.id.ll_game);
        mBtnLeaderboards = (TextView) view.findViewById(R.id.btn_leaderboards);
        mBtnLeaderboards.setOnClickListener(mOnClickListener);
        view.findViewById(R.id.btn_first).setOnClickListener(mOnClickListener);
        view.findViewById(R.id.btn_second).setOnClickListener(mOnClickListener);
        ((MainActivity) getContext()).getBubblesView().setPlaying(false);
    }

    private final OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_first:
                    final boolean open = mLlGame.findViewById(R.id.btn_second).getVisibility() == View.VISIBLE;
                    if (open) {
                        startFragment(GameFragment.newInstance(true), true);

                    } else if (SavingUtils.getInstance().isSaved()) {
                        AnimatorUtils.flipVertically(mLlGame).start();

                    } else {
                        startFragment(GameFragment.newInstance(), true);
                    }
                    break;

                case R.id.btn_second:
                    SavingUtils.getInstance().clear();
                    startFragment(GameFragment.newInstance(), true);
                    break;

                case R.id.btn_leaderboards:
                    if (GooglePlayHelper.getInstance().isConnected()) {
                        startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
                                GooglePlayHelper.getInstance().getClient(),
                                AContext.getString(R.string.leaderboard_id)),
                                REQUEST_GOOGLE_PLAY_SERVICES);
                    } else {
                        AnimatorUtils.flipHorizontally(mBtnLeaderboards, R.string.cant_connect).start();
                        GooglePlayHelper.getInstance().getClient().reconnect();
                    }
                    break;

                default:
                    break;
            }
        }
    };

}
