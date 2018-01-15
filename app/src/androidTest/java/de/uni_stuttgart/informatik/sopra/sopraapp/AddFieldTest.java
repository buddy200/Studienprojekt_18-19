package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

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
    public void checkAddAgrarianField(){
        String nameAgrar = "Feld";
        String regionAgrar = "Sample Region";
        String ownerAgrar = "Sample Owner";

        onView(withId(R.id.button_add))
                .perform(click());
        addFieldAndCheckIfThere(nameAgrar, regionAgrar, ownerAgrar);

        String nameDmg = "Test Damage";
        String regionDmg = "Sample Region";
        String ownerDmg = "Sample Owner";

        onView(withId(R.id.add_damageField_button))
                .perform(click());
        addFieldAndCheckIfThere(nameDmg, regionDmg, ownerDmg);
    }


    private void addFieldAndCheckIfThere(String name, String region, String owner){

        onView(withId(R.id.fab))
                .perform(click());

        onView(withId(R.id.fab))
                .perform(click());

        onView(withId(R.id.fab))
                .perform(click());

        onView(withId(R.id.action_menu_done))
                .perform(click());

        onView(withId(R.id.field_detail_name_edit))
                .perform(replaceText(name), closeSoftKeyboard());

        onView(withId(R.id.field_detail_region_edit))
                .perform(replaceText("Sample Region"), closeSoftKeyboard());

        onView(withId(R.id.field_detail_policyholder_edit))
                .perform(replaceText("Sample Owner"), closeSoftKeyboard());

        onView(withId(R.id.finish_edit_button_agr)).perform(click());

        onView(withId(R.id.action_menu_done)).perform(click());

        try{
            SearchTest.testSearch(name);
        }catch(Exception e){
            e.printStackTrace();
            pressBack();
            SearchTest.testSearch(name);
        }
        onView(withId(R.layout.fragment_item_list_dialog));
        onView(withText(name)).perform(click());

        onView(withId(R.layout.fragment_item_detail_dialog_damagefield)).toString().contains(name);
        onView(withId(R.layout.fragment_item_detail_dialog_damagefield)).toString().contains(region);
        onView(withId(R.layout.fragment_item_detail_dialog_damagefield)).toString().contains(owner);


    }
}
