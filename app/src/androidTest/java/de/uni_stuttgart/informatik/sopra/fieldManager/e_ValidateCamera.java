package de.uni_stuttgart.informatik.sopra.fieldManager;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.view.KeyEvent;
import android.widget.EditText;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * sopra_priv
 * Created by Felix B on 24.01.18.
 * Mail: felix.burk@gmail.com
 */

public class e_ValidateCamera {

    @Rule
    public IntentsTestRule<MainActivity> intentsRule =
            new IntentsTestRule<>(MainActivity.class);

    @Test
    public void validateCameraScenario() {
        // Create a bitmap we can use for our simulated camera image
        Bitmap icon = BitmapFactory.decodeResource(
                InstrumentationRegistry.getTargetContext().getResources(),
                R.mipmap.ic_launcher);

        // Now that we have the stub in place, click on the button in our app that launches into the Camera
        ab_LoginTest.loginAsAdmin("CAMERA TEST");
        SystemClock.sleep(500);
        onView(withId(R.id.action_toolbar_search)).perform(click());
        onView(isAssignableFrom(EditText.class)).perform(typeText("dmg name"), pressKey(KeyEvent.KEYCODE_ENTER));
        SystemClock.sleep(500);

        onView(withId(R.id.list))
                .perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.finish_edit_button_agr)).perform(click());
        onView(withId(R.id.add_Photo_Button)).perform(click());

        // Build a result to return from the Camera app
        Intent resultData = new Intent();
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        // Stub out the Camera. When an intent is sent to the Camera, this tells Espresso to respond
        // with the ActivityResult we just created
        intending(toPackage("com.google.android.apps.photos")).respondWith(result);

        onView(withId(R.id.pick_form_gallery)).perform(click());

        // We can also validate that an intent resolving to the "camera" activity has been sent out by our app
        intended(toPackage("com.google.android.apps.photos"));

        onView(withId(R.id.finish_edit_button_agr)).perform(click());

        b_AddFieldTest.testData("dmg name");
        SystemClock.sleep(2000);
        pressBack();

        deleteAll();
        SystemClock.sleep(2000);


        ab_LoginTest.logout();
        SystemClock.sleep(2000);


        // ... additional test steps and validation ...
    }

    private void deleteAll() {


        b_AddFieldTest.testData("field name");
        onView(withId(R.id.finish_edit_button_agr)).perform(click());
        onView(withId(R.id.delete_button)).perform(click());
        onView(withText(R.string.word_yes)).perform(click());
    }
}
