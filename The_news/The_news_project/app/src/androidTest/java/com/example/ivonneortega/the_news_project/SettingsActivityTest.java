package com.example.ivonneortega.the_news_project;

import android.os.SystemClock;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.example.ivonneortega.the_news_project.mainActivity.MainActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by ivonneortega on 5/5/17.
 */

public class SettingsActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void clickOnSettingsIcon()
    {

        onView(allOf(withId(R.id.options_toolbar), isDisplayed())).perform(click());
        onView(withId(R.id.switch_theme)).check(matches(isDisplayed()));
    }

    @Test
    public void changeTheme() {

        onView(allOf(withId(R.id.options_toolbar), isDisplayed())).perform(click());
        onView(withId(R.id.switch_theme)).perform(click());

        Espresso.pressBack();
        onView(allOf(withId(R.id.options_toolbar), isDisplayed())).perform(click());
        onView(withId(R.id.switch_theme)).check(matches(isChecked()));
        onView(withId(R.id.switch_theme)).perform(click());
    }

    @Test
    public void notificationsSetting() {

        onView(allOf(withId(R.id.options_toolbar), isDisplayed())).perform(click());
        onView(withId(R.id.switch_notification)).perform(click());

        Espresso.pressBack();
        onView(allOf(withId(R.id.options_toolbar), isDisplayed())).perform(click());
        onView(withId(R.id.switch_notification)).check(matches(isChecked()));
        onView(withId(R.id.switch_notification)).perform(click());

    }

}
