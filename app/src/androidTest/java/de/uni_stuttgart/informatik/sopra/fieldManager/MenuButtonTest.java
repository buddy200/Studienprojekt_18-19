package de.uni_stuttgart.informatik.sopra.fieldManager;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


/**
 * sopra_priv
 * Created by Felix B on 07.12.17.
 * Mail: felix.burk@gmail.com
 */

@RunWith(AndroidJUnit4.class)
public class MenuButtonTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void ensureAddButtonWorks() {
        onView(withId(R.id.button_add))
                .perform(click());

        onView(withId(R.layout.activity_add_field));
    }

    @Test
    public void ensureListButtonWorks(){
        onView(withId(R.id.button_list))
                .perform(click());

        onView(withId(R.layout.fragment_item_list_dialog));
    }

}

