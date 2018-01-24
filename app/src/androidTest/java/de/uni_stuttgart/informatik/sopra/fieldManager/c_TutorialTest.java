package de.uni_stuttgart.informatik.sopra.fieldManager;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * sopra_priv
 * Created by Felix B on 23.01.18.
 * Mail: felix.burk@gmail.com
 */

public class c_TutorialTest {
    private static final String TAG = "Tutorial Test";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void start() {
        a_LoginTest.loginAsAdmin("TUTORIAL TEST");

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.help_label)).perform(click());

        onView(withId(R.id.action_toolbar_add)).perform(click());
        onView(withId(R.id.action_toolbar_search)).perform(click());

        onView(withId(R.id.action_toolbar_add)).perform(click());
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.help_label)).perform(click());

        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.action_menu_done)).perform(click());

        pressBack();
        a_LoginTest.logout();

    }
}
