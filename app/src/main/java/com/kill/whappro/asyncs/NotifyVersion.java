package com.kill.whappro.asyncs;

import android.content.Context;
import android.os.AsyncTask;

import com.kill.whappro.callback.UpdaterCallback;
import com.kill.whappro.enums.UpdaterError;
import com.kill.whappro.models.Update;
import com.kill.whappro.utils.UtilsNetwork;
import com.kill.whappro.utils.UtilsWhatsApp;

import java.lang.ref.WeakReference;

public class NotifyVersion extends AsyncTask<Void, Void, Update> {
    private WeakReference<Context> mContextRef;
    private String mInstalledUpdate;
    private UpdaterCallback mCallback;

    public NotifyVersion(Context context, String installedUpdate,UpdaterCallback callback) {
        this.mContextRef = new WeakReference<>(context);
        this.mInstalledUpdate = installedUpdate;
        this.mCallback = callback;
    }

    @Override
    protected Update doInBackground(Void... voids) {
        Context context = mContextRef.get();

        if (context != null && UtilsNetwork.isNetworkAvailable(context)) {
            Update update = GetLatestVersion.getUpdate();
            if (update != null)
                return update;
            else
                mCallback.onError(UpdaterError.UPDATE_NOT_FOUND);
        } else
            mCallback.onError(UpdaterError.NO_INTERNET_CONNECTION);
        return null;
    }

    @Override
    protected void onPostExecute(Update update) {
        super.onPostExecute(update);
        if (update != null)
            mCallback.onFinished(update, mInstalledUpdate != null && UtilsWhatsApp.isUpdateAvailable(mInstalledUpdate, update.getLatestVersion()));
    }
}
