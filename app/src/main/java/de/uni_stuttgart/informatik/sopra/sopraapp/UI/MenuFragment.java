package de.uni_stuttgart.informatik.sopra.sopraapp.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.FragmentInteractionListener;

 /**
 * sopra_priv
 * Created by Felix B on 03.11.17.
 * Mail: felix.burk@gmail.com
 *
 * A fragment to display the menu
 */
public class MenuFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "MenuFragment";

    private FragmentInteractionListener mCallback;

    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * this factory method is used to generate an instance
     * using the provided parameters
     *
     * @return A new instance of fragment MenuFragment.
     */
    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    EditText input;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        Button list = view.findViewById(R.id.button_list);
        Button loc = view.findViewById(R.id.button_location);
        Button add = view.findViewById(R.id.button_add);
        Button info = view.findViewById(R.id.button_info);
        Button search = view.findViewById(R.id.button_search);
        input = view.findViewById(R.id.search_edit_text);

        list.setOnClickListener(this);
        loc.setOnClickListener(this);
        info.setOnClickListener(this);
        add.setOnClickListener(this);
        search.setOnClickListener(this);
        input.setOnClickListener(this);

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof FragmentInteractionListener) {
            mCallback = (FragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMenuFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

     /**
      * handle clicks on the buttons
      * and tell the activity containing this fragment what button is pressed
      * @param v
      */
    @Override
    public void onClick(View v) {

        if(mCallback != null){
            switch (v.getId()) {
                case R.id.button_list:
                    mCallback.onFragmentMessage(TAG, "listButton", null);
                    break;
                case R.id.button_location:
                    mCallback.onFragmentMessage(TAG, "locButton", null);
                    break;
                case R.id.button_add:
                    mCallback.onFragmentMessage(TAG, "addButton", null);
                    break;
                case R.id.button_info:
                    mCallback.onFragmentMessage(TAG, "infoButton", null);
                    break;
                case R.id.search_edit_text:
                    //remove text if user clicks on search
                    input.setText("");
                    break;
                case R.id.button_search:
                    mCallback.onFragmentMessage(TAG, "searchButton", input.getText().toString());
            }
        }
    }

}
