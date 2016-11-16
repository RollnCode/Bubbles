package com.rollncode.bubbles;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.rollncode.bubbles.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MyEspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * Dummy test ¯\_(ツ)_/¯
     */
    @Test
    public void loadActivity() {
        onView(withId(R.id.fragment_container)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.btn_first)).perform(ViewActions.click());
        onView(withId(R.id.tv_info)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        for (int i = 0; i < 50; i++) {
            onView(withId(R.id.game_view)).perform(ViewActions.click());
        }
    }
}
