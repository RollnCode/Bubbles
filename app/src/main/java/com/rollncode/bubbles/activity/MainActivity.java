package com.rollncode.bubbles.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.rollncode.bubbles.R;
import com.rollncode.bubbles.fragment.MenuFragment;
import com.rollncode.bubbles.game.view.BubblesView;
import com.rollncode.bubbles.util.GooglePlayHelper;

/**
 * @author Maxim Ambroskin kkxmshu@gmail.com
 * @since 14.07.16
 */
public class MainActivity extends BaseActivity {

    private BubblesView mBubblesView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GooglePlayHelper.init(this, findViewById(android.R.id.content));
        mBubblesView = (BubblesView) findViewById(R.id.game_view);
        if (savedInstanceState == null) {
            startFragment(MenuFragment.newInstance(), false);
        }
    }

    @Override
    public int getFragmentContainerId() {
        return R.id.fragment_container;
    }

    private boolean popBackStack() {
        final int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount > 0) {
            final Fragment fragment = getFragmentFromContainer();
            if (fragment != null && fragment instanceof NavigationFragment) {
                final NavigationFragment navigationFragment = (NavigationFragment) fragment;
                if (navigationFragment.onBackPressed()) {
                    return true;
                }
            }
            getSupportFragmentManager().popBackStack();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (!popBackStack()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GooglePlayHelper.getInstance().connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        GooglePlayHelper.getInstance().disconnect();
    }

    public BubblesView getBubblesView() {
        return mBubblesView;
    }

    public void onDialogDismissed() {
        GooglePlayHelper.getInstance().setResolvingError(false);
    }

    public interface NavigationFragment {
        boolean onBackPressed();
    }

}
