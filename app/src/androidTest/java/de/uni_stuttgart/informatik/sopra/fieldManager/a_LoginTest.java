package de.uni_stuttgart.informatik.sopra.fieldManager;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.test.espresso.Root;
import android.support.test.rule.ActivityTestRule;
import android.test.ActivityInstrumentationTestCase2;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.app.PendingIntent.getActivity;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static de.uni_stuttgart.informatik.sopra.fieldManager.MainActivity.getmContext;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * sopra_priv
 * Created by Felix B on 22.01.18.
 * Mail: felix.burk@gmail.com
 */

public class a_LoginTest {

    /**
     * BEFORE RUNNING THESE GRANT THE APP ALL PERMISSIONS,
     * PERMISSION TESTING WITH ESPRESSO IS NASTY
     * AND LOGGED OUT
     */

    private static final String TAG = "Login Test";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    static void start(){
        loginAsFarmer();
        checkLoginAndLogout("FARMER NAME");
        String name = "ADMIN NAME";
        loginAsAdmin(name);

        checkLoginAndLogout(name);
    }

    @Test
    static void loginAsFarmer(){

        onView(withId(R.id.login_dialog_usr_privileges))
                .perform(click());

        onView(withId(R.id.login_dialog_editText_username))
                .perform(typeText("FARMER NAME"), closeSoftKeyboard());

        onView(withId(R.id.login_dialog_btn_login))
                .perform(click());

        SystemClock.sleep(1000);
       // onView(withId(R.id.action_toolbar_add)).check(matches(isDisplayed()));
    }

    static void loginAsAdmin(String name) {
        onView(withText(R.string.admin_label))
                .perform(click());

        onView(withId(R.id.login_dialog_editText_username))
                .perform(typeText(name), closeSoftKeyboard());

        onView(withId(R.id.login_dialog_btn_login))
                .perform(click());

        SystemClock.sleep(1000);
        onView(withId(R.id.action_toolbar_add)).check(matches(isDisplayed()));
    }


    private static void checkLoginAndLogout(String name){
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withId(R.id.action_toolbar_username)).toString().contains(name);

        onView(withText(R.string.logout_label)).perform(click());

        onView(withText(R.string.word_yes)).perform(click());
    }

    static void logout(){
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.logout_label)).perform(click());
        onView(withText(R.string.word_yes)).perform(click());

    }




}
