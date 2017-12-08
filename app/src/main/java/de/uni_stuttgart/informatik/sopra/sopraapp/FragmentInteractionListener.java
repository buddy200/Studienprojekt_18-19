package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.support.annotation.Nullable;

/**
 * sopra_priv
 * Created by Felix B on 06.12.17.
 * Mail: felix.burk@gmail.com
 *
 * general interfaces for the communication between a fragment and it's activity
 */

public interface FragmentInteractionListener<T> {
    void onFragmentMessage( String Tag, String action, @Nullable T data);
}
