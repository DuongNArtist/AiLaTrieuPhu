package com.skynet.ailatrieuphu.facebook;

import com.skynet.ailatrieuphu.models.FacebookModel;

public interface FacebookCallback {

    void onSucceed(FacebookModel facebookModel);

    void onFailed();

}
