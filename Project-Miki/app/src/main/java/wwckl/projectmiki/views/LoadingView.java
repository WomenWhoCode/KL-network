package wwckl.projectmiki.views;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import wwckl.projectmiki.R;

/**
 * Created by lydialim on 5/31/15.
 *
 * Not final
 */
public class LoadingView {

    public static void show (Activity activity) {
        View loadingView = activity.findViewById(R.id.loadingOverlay);

        if (loadingView == null) {
            ViewGroup viewGroup = ((ViewGroup) activity.findViewById(android.R.id.content));
            View inflatedView = View.inflate(activity, R.layout.view_loading_overlay, viewGroup);
            loadingView = inflatedView.findViewById(R.id.loadingOverlay);
        }

        loadingView.setVisibility(View.VISIBLE);
    }

    public static void updateText (Activity activity, String loadingMessage) {
        if (activity == null) {
            return;
        }

        View loadingView = activity.findViewById(R.id.loadingOverlay);
        if (loadingView == null) {
            return;
        }

        TextView tv = (TextView) loadingView.findViewById(R.id.tvLoading);
        tv.setText(loadingMessage);
    }

    public static void dismiss (Activity activity) {
        if (activity == null) {
            return;
        }

        View loadingView = activity.findViewById(R.id.loadingOverlay);
        if (loadingView == null) {
            return;
        }

        loadingView.setVisibility(View.GONE);
    }
}
