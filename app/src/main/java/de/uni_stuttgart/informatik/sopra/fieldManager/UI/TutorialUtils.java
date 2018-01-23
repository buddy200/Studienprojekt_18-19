package de.uni_stuttgart.informatik.sopra.fieldManager.UI;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

import de.uni_stuttgart.informatik.sopra.fieldManager.R;

/**
 * sopra_priv
 * Created by Felix B on 22.01.18.
 * Mail: felix.burk@gmail.com
 */

public class TutorialUtils {

    public static void mainTutorial(Activity activity){
        new TapTargetSequence(activity)
                .targets(
                        TapTarget.forView(activity.findViewById(R.id.action_toolbar_add), "Tab here to add a new field")
                                .dimColor(R.color.colorPrimaryDark)
                                .cancelable(false),
                        TapTarget.forView(activity.findViewById(R.id.action_toolbar_search), "Tab here to search for fields, or other stuff")
                                .dimColor(R.color.colorPrimaryDark)
                                .cancelable(false))
                .listener(new TapTargetSequence.Listener() {

                    @Override
                    public void onSequenceFinish() {
                        // Yay
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {

                    }
                }).start();

    }

    public static void mainTutorialNoAdmin(Activity activity){
        new TapTargetSequence(activity)
                .targets(
                        TapTarget.forView(activity.findViewById(R.id.action_toolbar_search), "Tab here to search for fields, or other stuff")
                                .dimColor(R.color.colorPrimaryDark)
                                .cancelable(false))
                .listener(new TapTargetSequence.Listener() {

                    @Override
                    public void onSequenceFinish() {
                        // Yay
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {

                    }
                }).start();

    }

    public static void addFieldTutorial(Activity activity){
        Drawable d = VectorDrawableCompat.create(activity.getResources(), R.drawable.ic_add_location_white_24px, null);
        d = DrawableCompat.wrap(d);

        new TapTargetSequence(activity)
                .targets(
                        TapTarget.forView(activity.findViewById(R.id.fab), "Add a corner point", "to add a new corner point to your new field press here, " +
                                "the corner point is your current location on the map \n but if you wish to add points manually just press on the map to add a new one")
                                .icon(d)
                                .dimColor(R.color.colorPrimaryDark)
                                .cancelable(false),
                        TapTarget.forView(activity.findViewById(R.id.action_menu_done), "Create your field",
                                "if you have enough points selected on the map press here to finish up your field and fill in the necessary data")
                                .targetRadius(60)
                                .dimColor(R.color.colorPrimaryDark)
                                .cancelable(false))
                .listener(new TapTargetSequence.Listener() {
                    // This listener will tell us when interesting(tm) events happen in regards
                    // to the sequence
                    @Override
                    public void onSequenceFinish() {
                        // Yay
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        // Boo
                    }
                }).start();

    }
}
