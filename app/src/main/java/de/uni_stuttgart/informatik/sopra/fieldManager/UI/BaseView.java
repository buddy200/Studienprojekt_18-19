package de.uni_stuttgart.informatik.sopra.fieldManager.UI;

/**
 * sopra_priv
 * Created by Felix B on 19.12.17.
 * Mail: felix.burk@gmail.com
 *
 * this interface provides a base for some view classes
 * classes inheriting from BaseView will be controlled by the presenter
 * they handle everything needed for android ui's but no logic
 */

public interface BaseView {
    void setPresenter(BasePresenter presenter);
}
