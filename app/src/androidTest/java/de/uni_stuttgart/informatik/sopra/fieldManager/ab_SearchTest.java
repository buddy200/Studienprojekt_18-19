package de.uni_stuttgart.informatik.sopra.fieldManager;

import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.view.KeyEvent;
import android.widget.EditText;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.core.StringContains.containsString;

/**
 * sopra_priv
 * Created by Felix B on 24.01.18.
 * Mail: felix.burk@gmail.com
 */

public class ab_SearchTest {
    private static final String TAG = "AddField Test";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void start(){
        a_LoginTest.loginAsAdmin(TAG);
        SystemClock.sleep(500);

        onView(withId(R.id.action_toolbar_search)).perform(click());
        onView(withId(R.id.spinner_search)).perform(click());
        SystemClock.sleep(500);
        onData(anything()).atPosition(0).perform(click());
        onView(isAssignableFrom(EditText.class)).perform(typeText("test"), pressKey(KeyEvent.KEYCODE_ENTER));
        pressBack();

        onView(withId(R.id.action_toolbar_search)).perform(click());
        onView(withId(R.id.spinner_search)).perform(click());
        SystemClock.sleep(500);
        onData(anything()).atPosition(1).perform(click());
        onView(isAssignableFrom(EditText.class)).perform(typeText("test"), pressKey(KeyEvent.KEYCODE_ENTER));
        pressBack();

        onView(withId(R.id.action_toolbar_search)).perform(click());
        onView(withId(R.id.spinner_search)).perform(click());
        SystemClock.sleep(500);
        onData(anything()).atPosition(2).perform(click());
        onView(isAssignableFrom(EditText.class)).perform(typeText("test"), pressKey(KeyEvent.KEYCODE_ENTER));
        pressBack();

        onView(withId(R.id.action_toolbar_search)).perform(click());
        onView(withId(R.id.spinner_search)).perform(click());
        onData(anything()).atPosition(3).perform(click());
        onView(isAssignableFrom(EditText.class)).perform(typeText("test"), pressKey(KeyEvent.KEYCODE_ENTER));
        pressBack();

        onView(withId(R.id.action_toolbar_search)).perform(click());
        onView(withId(R.id.spinner_search)).perform(click());
        SystemClock.sleep(500);
        onData(anything()).atPosition(4).perform(click());
        onView(isAssignableFrom(EditText.class)).perform(typeText("test"), pressKey(KeyEvent.KEYCODE_ENTER));
        pressBack();

        onView(withId(R.id.action_toolbar_search)).perform(click());
        onView(withId(R.id.spinner_search)).perform(click());
        SystemClock.sleep(500);
        onData(anything()).atPosition(5).perform(click());
        onView(isAssignableFrom(EditText.class)).perform(typeText("test"), pressKey(KeyEvent.KEYCODE_ENTER));
        pressBack();

        a_LoginTest.logout();
    }
}
