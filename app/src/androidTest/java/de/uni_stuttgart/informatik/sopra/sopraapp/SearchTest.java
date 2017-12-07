package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

/**
 * sopra_priv
 * Created by Felix B on 07.12.17.
 * Mail: felix.burk@gmail.com
 */

public class SearchTest {

    private static final String TAG = "SearchTest";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testSeatch(){
        Log.d(TAG, "setup");
        String searchQuery = "Agrarian";
        Log.d(TAG, "search for: " + searchQuery);


        onView(withId(R.id.search_edit_text))
                .perform(typeText(searchQuery), closeSoftKeyboard());

        //perform the search
        onView(withId(R.id.button_search))
                .perform(click());

        try{
            //is a list displayed?
            onView(withId(R.layout.fragment_item_list_dialog));
            Log.d(TAG, "search successful");

            withId(R.id.item_field_name).toString().contains(searchQuery);

            Log.d(TAG, "search successful");

        }  catch (NoMatchingViewException e) {
            Log.d(TAG, "search failed");
            //is toast message shown? because the search function didn't find anything
            onView(withText(R.string.toastmsg_nothing_found))
                    .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
        }

    }
}
