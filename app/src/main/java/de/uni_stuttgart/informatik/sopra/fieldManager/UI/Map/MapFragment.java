package de.uni_stuttgart.informatik.sopra.fieldManager.UI.Map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import de.uni_stuttgart.informatik.sopra.fieldManager.FragmentInteractionListener;
import de.uni_stuttgart.informatik.sopra.fieldManager.R;
import de.uni_stuttgart.informatik.sopra.fieldManager.UI.BasePresenter;

/**
 * sopra_priv
 * Created by Felix B on 03.11.17.
 * Mail: felix.burk@gmail.com
 */

public class MapFragment extends Fragment implements MapContract.MapFragment {
    private static final String TAG = "MapFragment";

    private ConstraintLayout cl;

    private boolean permissionGranted;

    private FragmentInteractionListener mListener;
    private MapViewHandler mPresenter;
    private TextView v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            permissionGranted = false;
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            permissionGranted = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        cl = getView().findViewById(R.id.cl);
    }

    @Override
    public void onResume() {
        super.onResume();
        cl.removeAllViews();
        if (permissionGranted) {
            cl.addView(mPresenter.getMap());

        } else {
            v = new TextView(getContext());
            v.setText("Permission not granted - sorry");
            cl.addView(v);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (permissionGranted) {
            mPresenter.start();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            this.mListener = (FragmentInteractionListener) context;
        } catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement FragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstance) {
        super.onActivityCreated(savedInstance);
    }

    /**
     * handle permission results
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    permissionGranted = true;
                    mPresenter.start();

                    this.onStart();
                } else {
                    //no permission - no map
                }
                break;
            case 1:
                //GPS Permission check
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.onStart();
                } else {
                    Toast.makeText(getActivity(), "Keine Standort Berechtigung", Toast.LENGTH_SHORT).show();

                }
                break;
            default:
                Log.e(TAG, "requested permission not handled");
        }
    }

    public void setPresenter(MapViewHandler presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setPresenter(BasePresenter presenter) {
    }
}