package com.auto.home.instacoffee.cloud.model;

public interface IListenerCallback {

    void onDataChange(String parameterPath, Object newValue);

    void onFailure(String parameterPath, String errorMsg);

}
