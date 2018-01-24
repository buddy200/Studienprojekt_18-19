package de.uni_stuttgart.informatik.sopra.fieldManager.UI;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

import de.uni_stuttgart.informatik.sopra.fieldManager.MainActivity;
import de.uni_stuttgart.informatik.sopra.fieldManager.R;

/**
 * sopra_priv
 * Created by Felix B on 22.01.18.
 * Mail: felix.burk@gmail.com
 */

public class TutorialOverlays {

    public static void mainTutorial(Activity activity){
        new TapTargetSequence(activity)
                .targets(
                        TapTarget.forView(activity.findViewById(R.id.action_toolbar_add), MainActivity.getmContext().getResources().getString(R.string.help_title_add_agr_field))
                                .dimColor(R.color.colorPrimaryDark)
                                .cancelable(false),
                        TapTarget.forView(activity.findViewById(R.id.action_toolbar_search), MainActivity.getmContext().getResources().getString(R.string.help_title_search),
                                MainActivity.getmContext().getResources().getString(R.string.help_desc_search))
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
                        TapTarget.forView(activity.findViewById(R.id.action_toolbar_search), MainActivity.getmContext().getResources().getString(R.string.help_title_search),
                                MainActivity.getmContext().getResources().getString(R.string.help_desc_search))
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

        String titleFab = MainActivity.getmContext().getResources().getString(R.string.help_title_fab);
        String descFab = MainActivity.getmContext().getResources().getString(R.string.help_desc_fab);

        String titleMenuDone = MainActivity.getmContext().getResources().getString(R.string.help_title_create_field);
        String descMenuDone = MainActivity.getmContext().getResources().getString(R.string.help_desc_create_field);

        new TapTargetSequence(activity)
                .targets(
                        TapTarget.forView(activity.findViewById(R.id.fab),  titleFab, descFab)
                                .icon(d)
                                .dimColor(R.color.colorPrimaryDark)
                                .cancelable(false),
                        TapTarget.forView(activity.findViewById(R.id.action_menu_done), titleMenuDone, descMenuDone)
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
