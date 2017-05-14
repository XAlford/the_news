package com.example.ivonneortega.the_news_project;

import android.os.SystemClock;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.example.ivonneortega.the_news_project.mainActivity.MainActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by ivonneortega on 5/5/17.
 */

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void topStoriesAndSave() {
        ViewInteraction appCompatTextView = onView(
                allOf(withText("Top Stories"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withText("Save"), isDisplayed()));
        appCompatTextView2.perform(click());

    }

    @Test
    public void settingButton() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.options_toolbar), isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction switch_ = onView(
                allOf(withId(R.id.switch_theme), isDisplayed()));
        switch_.perform(click());
    }

    @Test
    public void hamburgerMenuToNavDrawerClickTest() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        withParent(withId(R.id.root_toolbar)),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(R.id.design_menu_item_text), withText("World"), isDisplayed()));
        appCompatCheckedTextView.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.category_recycler_view), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

    }

    @Test
    public void searchIconToSearchActivityClickTest() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.search_toolbar), isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction searchAutoComplete = onView(
                allOf(withId(R.id.search_src_text),
                        withParent(allOf(withId(R.id.search_plate),
                                withParent(withId(R.id.search_edit_frame)))),
                        isDisplayed()));
        searchAutoComplete.perform(replaceText("q"), closeSoftKeyboard());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.search_recycler_view), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

    }

    @Test
    public void clickOnItemInSaveFragment()
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
    public void clickOnItemInTopStoriesFragment()
    {
        Matcher<View> matcher = allOf(withText("Top Stories"),
                isDescendantOfA(withId(R.id.tabLayout)));
        onView(matcher).perform(click());
        SystemClock.sleep(800);

        onView(withId(R.id.save_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(allOf(withId(R.id.detail_url), isDisplayed())).perform(click());
    }



}
