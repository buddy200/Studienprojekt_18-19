package de.uni_stuttgart.informatik.sopra.sopraapp.UI;

import android.app.Activity;
import android.content.Context;
import android.view.MenuItem;
import android.view.View;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;

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

    public static void addFieldTutorial(Activity activity){
        new TapTargetSequence(activity)
                .targets(
                        TapTarget.forView(activity.findViewById(R.id.fab_with_label), "Add corner point", "to add a new corner point to your new field press here, " +
                                "the corner point is your current location on the map \n but if you wish to add points manually just press on the map to add a new one"))
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
