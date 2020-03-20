package com.kill.whappro.asyncs;

import android.content.Context;
import android.os.AsyncTask;

import com.kill.whappro.Config;
import com.kill.whappro.callback.UpdaterCallback;
import com.kill.whappro.enums.UpdaterError;
import com.kill.whappro.models.Update;
import com.kill.whappro.utils.UtilsNetwork;
import com.kill.whappro.utils.UtilsWhatsApp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class GetLatestVersion extends AsyncTask<Void, Void, Update> {
    private UpdaterCallback mCallback;
    private String mInstalledUpdate;
    private WeakReference<Context> mContextRef;

    public GetLatestVersion(Context context, String installedUpdate, UpdaterCallback callback) {
        this.mContextRef = new WeakReference<>(context);
        this.mInstalledUpdate = installedUpdate;
        this.mCallback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mCallback.onLoading();
    }

    @Override
    protected Update doInBackground(Void... voids) {
        Context context = mContextRef.get();

        if (context != null && UtilsNetwork.isNetworkAvailable(context)) {
            Update update = getUpdate();
            if (update != null)
                return update;
            else
                mCallback.onError(UpdaterError.UPDATE_NOT_FOUND);
        } else
            mCallback.onError(UpdaterError.NO_INTERNET_CONNECTION);
        return null;
    }

    public static Update getUpdate() {
        try {
            Document downLoadDocument = Jsoup.connect(Config.WHATSAPP_URL)
                    .get();
            if (downLoadDocument != null) {
                String version = downLoadDocument.getElementsByClass("version").first().getAllElements().get(1).text();
                String url = downLoadDocument.getElementsByClass("data download").attr("abs:href");
                return new Update(version, url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Update update) {
        super.onPostExecute(update);
        if (update != null)
            mCallback.onFinished(update, mInstalledUpdate != null && UtilsWhatsApp.isUpdateAvailable(mInstalledUpdate, update.getLatestVersion()));
    }

}
