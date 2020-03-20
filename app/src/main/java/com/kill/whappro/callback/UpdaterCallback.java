package com.kill.whappro.callback;

import com.kill.whappro.enums.UpdaterError;
import com.kill.whappro.models.Update;

public interface UpdaterCallback {

    void onFinished(Update update, boolean isUpdateAvailable);
    void onLoading();
    void onError(UpdaterError error);

}
