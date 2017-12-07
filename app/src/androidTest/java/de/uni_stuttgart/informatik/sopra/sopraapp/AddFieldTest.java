package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * sopra_priv
 * Created by Felix B on 07.12.17.
 * Mail: felix.burk@gmail.com
 */

@RunWith(AndroidJUnit4.class)
public class AddFieldTest {
    private static final String TAG = "AddFieldTest";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void addField(){
        onView(withId(R.id.button_add))
                .perform(click());

        onView(withId(R.id.fab))
                .perform(click());

        onView(withId(R.id.fab))
                .perform(click());

        onView(withId(R.id.fab))
                .perform(click());

        onView(withId(R.id.action_menu_done))
                .perform(click());

        onView(withId(R.id.field_detail_name_edit))
                .perform(replaceText("Sample Name"), closeSoftKeyboard());

        onView(withId(R.id.field_detail_region_edit))
                .perform(replaceText("Sample Region"), closeSoftKeyboard());

        onView(withId(R.id.field_detail_policyholder_edit))
                .perform(replaceText("Sample Owner"), closeSoftKeyboard());

        onView(withId(R.id.edit_finish_button)).perform(click());

        onView(withId(R.id.action_menu_done)).perform(click());
    }
}
