package de.uni_stuttgart.informatik.sopra.fieldManager;

import android.os.SystemClock;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.support.test.rule.ActivityTestRule;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static net.bytebuddy.matcher.ElementMatchers.is;

/**
 * sopra_priv
 * Created by Felix B on 23.01.18.
 * Mail: felix.burk@gmail.com
 */

public class b_AddFieldTest {

    private static final String TAG = "AddField Test";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void start(){
        ab_LoginTest.loginAsAdmin("ADD FIELD TEST");

        onView(withId(R.id.action_toolbar_add)).perform(click());
        drawFieldBig();

        writeData("field name");
        testData("field name");
        addDmgField("dmg name");
        testData("dmg name");
        addPhotoBottomSheet();

        ab_LoginTest.logout();
    }

    private void addPhotoBottomSheet() {
        onView(withId(R.id.finish_edit_button_agr)).perform(click());
        onView(withId(R.id.add_Photo_Button)).perform(click());
        onView(withId(R.id.finish_edit_button_agr)).perform(click());
    }

    private void addDmgField(String s) {
        onView(withId(R.id.add_damageField_button)).perform(click());
        drawFieldSmall();
        writeData(s);
    }

    static void testData(String s) {
        SystemClock.sleep(500);

        onView(withId(R.id.action_toolbar_search)).perform(click());
        onView(isAssignableFrom(EditText.class)).perform(typeText(s), pressKey(KeyEvent.KEYCODE_ENTER));

        SystemClock.sleep(500);

        onView(withId(R.id.list))
                .perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.field_detail_name)).toString().contains(s);
    }

    private void writeData(String s) {
        SystemClock.sleep(500);
        onView(withId(R.id.field_detail_name_edit)).perform(typeText(s), closeSoftKeyboard());
        onView(withId(R.id.finish_edit_button_agr)).perform(click());
        SystemClock.sleep(500);
        pressBack();
    }

    private void drawFieldBig(){
        onView(withId(R.id.map_fragment_add)).perform(clickXY(0.1f, 0.1f));
        SystemClock.sleep(500);

        onView(withId(R.id.map_fragment_add)).perform(clickXY(0.1f,0.9f));
        SystemClock.sleep(500);

        testRedoButton(0.1f, 0.9f);

        onView(withId(R.id.map_fragment_add)).perform(clickXY(0.9f,0.9f));
        SystemClock.sleep(500);

        onView(withId(R.id.map_fragment_add)).perform(clickXY(0.15f,0.7f));
        SystemClock.sleep(500);

        onView(withId(R.id.action_menu_done)).perform(click());

    }

    private void testRedoButton(float v, float v1) {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.redo_Button)).perform(click());

        onView(withId(R.id.map_fragment_add)).perform(clickXY(0.1f,0.9f));
        SystemClock.sleep(500);
    }

    private void drawFieldSmall(){
        onView(withId(R.id.map_fragment_add)).perform(clickXY(0.15f, 0.6f));
        SystemClock.sleep(500);

        onView(withId(R.id.map_fragment_add)).perform(clickXY(0.25f,0.6f));
        SystemClock.sleep(500);

        onView(withId(R.id.map_fragment_add)).perform(clickXY(0.1f,0.9f));
        SystemClock.sleep(500);

        onView(withId(R.id.map_fragment_add)).perform(clickXY(0.15f,0.7f));
        SystemClock.sleep(500);

        // onView(withId(R.id.map_fragment_add)).perform(clickXY(0.1f,0.1f));

        onView(withId(R.id.action_menu_done)).perform(click());

    }

    public static ViewAction clickXY(final float pctX, final float pctY){
        return new GeneralClickAction(
                Tap.SINGLE,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {

                        final int[] screenPos = new int[2];
                        view.getLocationOnScreen(screenPos);
                        int w = view.getWidth();
                        int h = view.getHeight();

                        float x = w * pctX;
                        float y = h * pctY;

                        final float screenX = screenPos[0] + x;
                        final float screenY = screenPos[1] + y;
                        float[] coordinates = {screenX, screenY};

                        return coordinates;
                    }
                },
                Press.FINGER);
    }
}
