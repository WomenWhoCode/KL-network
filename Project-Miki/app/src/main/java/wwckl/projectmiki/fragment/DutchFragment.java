package wwckl.projectmiki.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wwckl.projectmiki.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class DutchFragment extends Fragment {

    public DutchFragment () {
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bill_splitter, container, false);
    }
}
