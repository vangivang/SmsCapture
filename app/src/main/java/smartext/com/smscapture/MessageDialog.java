package smartext.com.smscapture;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;


/**
 * Simple cad message dialog based on dialog fragment.
 * Automatically taking care of orientation changes.
 * Should be initiated through the {@link smartext.com.smscapture.MessageDialog.Builder}
 */
public class MessageDialog extends DialogFragment {

    public static final String TAG = MessageDialog.class.getSimpleName();

    public static final String ARG_MESSAGE = "MessageDialog.mesage";
    public static final String ARG_FINISH_ACTIVITY = "MessageDialog.shouldFinish";
    public static final String ARG_ADD_REPORT_BUTTON = "MessageDialog.addReportButton";
    public static final String ARG_ADD_REPORT_TITLE = "MessageDialog.reportTitle";


    public static class Builder {
        private final Bundle arguments;

        public Builder() {
            arguments = new Bundle();
        }

        public Builder setMessage(String name) {
            arguments.putString(ARG_MESSAGE, name);
            return this;
        }

        public Builder finishActivity(boolean finishActivity) {
            arguments.putBoolean(ARG_FINISH_ACTIVITY, finishActivity);
            return this;
        }

        public Builder addReportButton(boolean addReportButton) {
            arguments.putBoolean(ARG_ADD_REPORT_BUTTON, addReportButton);
            return this;
        }

        public Builder setReportTitle(String reportTitle) {
            arguments.putString(ARG_ADD_REPORT_TITLE, reportTitle);
            return this;
        }

        public void show(Activity activity) {
            try {
                if (!activity.isFinishing()) {

                    FragmentManager manager = activity.getFragmentManager();

                    // DialogFragment.show() will take care of adding the fragment
                    // in a transaction.  We also want to remove any currently showing
                    // dialog, so make our own transaction and take care of that here.
                    FragmentTransaction ft = manager.beginTransaction();
                    Fragment prev = manager.findFragmentByTag(TAG);
                    if (prev != null) {
                        ft.remove(prev).commit();
                    }

                    DialogFragment newFragment = MessageDialog.newInstance(arguments);
                    newFragment.setCancelable(false);
                    newFragment.show(manager, TAG);
                    manager.executePendingTransactions();
                }
            } catch (IllegalStateException ex) {
                // We're catching this exception to prevent app crash
                Log.w(TAG, "MessageDialog.show - state loss. " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private static  MessageDialog newInstance(Bundle arguments) {
        MessageDialog dialog = new MessageDialog();
        dialog.setArguments(arguments);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Get builder arguments
        String message = getArguments().getString(ARG_MESSAGE);
        final boolean finishActivity = getArguments().containsKey(ARG_FINISH_ACTIVITY) && getArguments().getBoolean(ARG_FINISH_ACTIVITY);
        boolean addReportButton = getArguments().containsKey(ARG_ADD_REPORT_BUTTON) && getArguments().getBoolean(ARG_ADD_REPORT_BUTTON);
        final String reportTitle = getArguments().getString(ARG_ADD_REPORT_TITLE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if (finishActivity) {
                                    getActivity().finish();
                                }
                            }
                        });

        if (!TextUtils.isEmpty(reportTitle)){
            builder.setTitle(reportTitle);
        }

        if (addReportButton) {
            builder.setPositiveButton("Contact Us", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (finishActivity) {
                        getActivity().finish();
                    }
                }
            });
        }

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }
}