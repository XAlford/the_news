package com.example.ivonneortega.the_news_project;

import android.os.SystemClock;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.view.Gravity;
import android.view.View;

import com.example.ivonneortega.the_news_project.mainActivity.MainActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by ivonneortega on 5/5/17.
 */

public class DetailActivityTest {


    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void clickOnButton()
    {
        Matcher<View> matcher = allOf(withText("Save"),
                isDescendantOfA(withId(R.id.tabLayout)));
        onView(matcher).perform(click());
        SystemClock.sleep(800);

        onView(withId(R.id.save_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(allOf(withId(R.id.detail_url), isDisplayed())).perform(click());
    }

    @Test
    public void unSaveAndSave()
    {
        Matcher<View> matcher = allOf(withText("Save"),
                isDescendantOfA(withId(R.id.tabLayout)));
        onView(matcher).perform(click());
        SystemClock.sleep(800);

        onView(withId(R.id.save_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(allOf(withId(R.id.heart_toolbar), isDisplayed())).perform(click());

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText("Article unsaved")))
                .check(matches(withEffectiveVisibility(
                        ViewMatchers.Visibility.VISIBLE)));

        SystemClock.sleep(2000);

        onView(allOf(withId(R.id.heart_toolbar), isDisplayed())).perform(click());

    }

    @Test
    public void clickOnSettingsIcon()
    {

        Matcher<View> matcher = allOf(withText("Save"),
                isDescendantOfA(withId(R.id.tabLayout)));
        onView(matcher).perform(click());
        SystemClock.sleep(800);

        onView(withId(R.id.save_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(allOf(withId(R.id.options_toolbar), isDisplayed())).perform(click());

        onView(withId(R.id.switch_theme)).check(matches(isDisplayed()));
    }

    @Test
    public void goToCategoryActivity()
    {
        Matcher<View> matcher = allOf(withText("Save"),
                isDescendantOfA(withId(R.id.tabLayout)));
        onView(matcher).perform(click());
        SystemClock.sleep(800);

        onView(withId(R.id.save_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(open());

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_world));


        onView(withId(R.id.category_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(allOf(withId(R.id.detail_url), isDisplayed())).perform(click());
    }

}
