package com.softacles.myjournal;

import android.content.Context;
import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class JournalListUiTest {
    @Rule public ActivityTestRule<JournalList> mActivityTestRule = new ActivityTestRule<>(JournalList.class);

    /*Test method to assign click on my recycler view, I could test lot more but no time*/
    @Test
    public void checkRecyclerClick(){
        onView(withId(R.id.recycler_journals))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

}
