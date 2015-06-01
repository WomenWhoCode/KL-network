package wwckl.projectmiki.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import wwckl.projectmiki.R;
import wwckl.projectmiki.models.Receipt;

/**
 * A placeholder fragment containing a simple view.
 */
public class DutchFragment extends Fragment {

    private static final String ARG_RECEIPT = "receipt";

    private TextView mTextView;
    private Receipt mReceipt;

    public static DutchFragment newInstance (Receipt receipt) {
        DutchFragment fragment = new DutchFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RECEIPT, receipt);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DutchFragment () {
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mReceipt = getArguments().getParcelable(ARG_RECEIPT);
        }

        if (mReceipt == null) {
            mReceipt = new Receipt();
        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dutch, container, false);

        mTextView = (TextView) view.findViewById(R.id.textView);

        if (mReceipt.getTotalItems() > 0) {
            mTextView.setText(mReceipt.getItem(0).getName());
        }

        return view;
    }
}
