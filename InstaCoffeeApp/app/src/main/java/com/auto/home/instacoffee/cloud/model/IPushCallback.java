package com.auto.home.instacoffee.cloud.model;

public interface IPushCallback {

    void onSuccess(String parameterKey);

    void onFailure(String parameterKey, String errorMsg);

}
