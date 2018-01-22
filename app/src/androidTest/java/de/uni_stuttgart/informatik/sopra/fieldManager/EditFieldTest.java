package de.uni_stuttgart.informatik.sopra.fieldManager;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * sopra_priv
 * Created by Felix B on 08.12.17.
 * Mail: felix.burk@gmail.com
 */

public class EditFieldTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void editAgrarianFieldTest(){
        String nameAgrar = "Test";
        String regionAgrar = "Sample Region";
        String ownerAgrar = "Sample Owner";

        try{
            SearchTest.testSearch("Agrar");
        }catch(Exception e){
            e.printStackTrace();
            pressBack();
            SearchTest.testSearch("Agrar");
        }

        onView(withId(R.layout.fragment_item_list_dialog));
        onView(withText("Agrar Feld 2")).perform(click());

        onView(withId(R.id.finish_edit_button_agr)).perform(click());

        onView(withId(R.id.field_detail_name_edit))
                .perform(replaceText(nameAgrar), closeSoftKeyboard());

        onView(withId(R.id.field_detail_region_edit))
                .perform(replaceText(regionAgrar), closeSoftKeyboard());

        onView(withId(R.id.field_detail_policyholder_edit))
                .perform(replaceText(regionAgrar), closeSoftKeyboard());

        onView(withId(R.id.finish_edit_button_agr)).perform(click());

        try{
            SearchTest.testSearch(nameAgrar);
        }catch(Exception e){
            e.printStackTrace();
            pressBack();
            SearchTest.testSearch(nameAgrar);
        }

        onView(withId(R.layout.fragment_item_detail_dialog_damagefield)).toString().contains(nameAgrar);
        onView(withId(R.layout.fragment_item_detail_dialog_damagefield)).toString().contains(regionAgrar);
        onView(withId(R.layout.fragment_item_detail_dialog_damagefield)).toString().contains(ownerAgrar);

    }
}
