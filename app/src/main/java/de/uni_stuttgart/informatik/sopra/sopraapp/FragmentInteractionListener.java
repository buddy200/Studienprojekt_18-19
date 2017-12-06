package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.support.annotation.Nullable;

/**
 * sopra_priv
 * Created by Felix B on 06.12.17.
 * Mail: felix.burk@gmail.com
 */

public interface FragmentInteractionListener<T> {
    public void onFragmentMessage( String Tag, String action, @Nullable T data);
}
