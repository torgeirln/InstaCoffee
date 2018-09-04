package com.auto.home.instacoffee.cloud.model;

public interface IPullCallback {

    void onSuccess(String parameterKey, Object parameter);

    void onFailure(String parameterKey, String errorMsg);
}
