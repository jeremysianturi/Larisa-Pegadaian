package id.co.pegadaian.diarium.util.element;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.ContextThemeWrapper;

public class ProgressDialogHelper {

    private ProgressDialog progressDialog;

    public void showProgressDialog(final Activity activity, final String msg) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(new ContextThemeWrapper(
                            activity, android.R.style.Theme_DeviceDefault_Light));
                }

                if (null != msg && msg.length() > 0) {
                    progressDialog.setMessage(msg);
                }

                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        });
    }

    public void changeMessage(final String msg) {
        try {
            System.out.println("check change message : " + msg);
            progressDialog.setMessage(msg);
        } catch (Exception e){
            System.out.println("exception at change message progress dialog helper class : " + msg);
        }

    }

    public void dismissProgressDialog(final Activity activity) {
        if (progressDialog != null) {
            try {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                });
            }catch (Exception e){

            }
        }
    }
}