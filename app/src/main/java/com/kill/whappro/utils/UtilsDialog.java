package com.kill.whappro.utils;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.kill.whappro.R;
import com.kill.whappro.WhatsAppBetaUpdaterApplication;
import com.kill.whappro.models.Update;

import java.io.File;

public class UtilsDialog {

    public static MaterialDialog.Builder showDownloadingDialog(Context context, String version) {
        Boolean showMinMax = false; // Show a max/min ratio to the left of the seek bar

        MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
                .progress(false, 100, showMinMax)
                .cancelable(false)
                .negativeText(context.getResources().getString(android.R.string.cancel));

        builder.title(String.format(context.getResources().getString(R.string.downloading), context.getResources().getString(R.string.app_whatsapp), version));

        return builder;
    }

    public static MaterialDialog showSaveAPKDialog(final Context context, final File file, final String version) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(context.getResources().getString(R.string.delete))
                .content(context.getResources().getString(R.string.delete_description))
                .cancelable(false)
                .positiveText(context.getResources().getString(R.string.button_save))
                .negativeText(context.getResources().getString(R.string.button_delete))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        showSnackbarSavedAPK(context, file, version);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        file.delete();
                    }
                }).show();

        return dialog;
    }

    public static MaterialDialog showUpdateAvailableDialog(final Context context, final Update update) {
        final AppPreferences appPreferences = WhatsAppBetaUpdaterApplication.getAppPreferences();

        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(String.format(context.getResources().getString(R.string.app_update), update.getLatestVersion()))
                .content(context.getResources().getString(R.string.app_update_description))
                .positiveText(context.getResources().getString(R.string.button_update))
                .negativeText(context.getResources().getString(android.R.string.cancel))
                .neutralText(context.getResources().getString(R.string.button_disable_update))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        new UtilsAsync.DownloadFile(context, UtilsEnum.DownloadType.UPDATE, update).execute();
                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        appPreferences.setShowAppUpdate(false);
                    }
                }).show();

        return dialog;
    }

    public static void showSnackbar(Context context, String title) {
        Activity activity = (Activity) context;
        Snackbar.make(activity.findViewById(R.id.coordinatorLayout), title, Snackbar.LENGTH_LONG)
                .show();
    }

    public static void showSnackbarSavedAPK(final Context context, final File file, final String version) {
        Activity activity = (Activity) context;
        Snackbar.make(activity.findViewById(R.id.coordinatorLayout), String.format(context.getResources().getString(R.string.snackbar_saved), file.getName()), Snackbar.LENGTH_LONG)
                .setAction(context.getResources().getString(R.string.button_share), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String shareText = String.format(context.getResources().getString(R.string.snackbar_share), version, context.getResources().getString(R.string.app_name) + " " + "https://github.com/javiersantos/WhatsAppBetaUpdater/releases");
                        context.startActivity(UtilsIntent.getShareAPKIntent(file, shareText));
                    }
                })
                .show();
    }

}