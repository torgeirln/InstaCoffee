package com.auto.home.instacoffee.cloud;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.auto.home.instacoffee.cloud.model.CloudStructure;
import com.auto.home.instacoffee.cloud.model.IListenerCallback;
import com.auto.home.instacoffee.cloud.model.IPullCallback;
import com.auto.home.instacoffee.cloud.model.IPushCallback;

public class SyncManager {

    private static final String TAG = SyncManager.class.getName();
    private static final String BREW_REQ = "BREW_REQ";

    private SyncAdapter mSyncAdapter;
    private CloudStructure.ArduinoCtrl mArduinoCtrl;
    private CloudStructure.AndroidFeedback mAndroidFeedback;
    private String mAppName;

    public SyncManager(Context context) {
        mAppName = getAppName(context);
        mSyncAdapter = new SyncAdapter(mAppName);
        mArduinoCtrl = new CloudStructure.ArduinoCtrl();
        mAndroidFeedback = new CloudStructure.AndroidFeedback();
    }

    private String getAppName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ?
                applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    public void setupCloudStructure() {
        Log.v(TAG, "Manager: Setup cloud structure");
        mSyncAdapter.initDatabase(new CloudStructure());
    }

    public void runBrewProsses() {
        mSyncAdapter.pushParameter(mArduinoCtrl.runBrewVar, true, mPushCallback);
    }

    private void addErrorListener() {
        mSyncAdapter.addParameterListener(mAndroidFeedback.errorVar, new IListenerCallback() {
            @Override
            public void onDataChange(String parameterPath, Object newValue) {
                if (newValue != null) {
                    boolean errorValue = (boolean) newValue;
                    if (errorValue) {
                        Log.e(TAG, "Error received from Arduino!");
                        //TODO: Handle error!
                    }
                } else {
                    Log.e(TAG, "newValue is null for " + parameterPath);
                }
            }

            @Override
            public void onFailure(String parameterPath, String errorMsg) {

            }
        });
    }

    private IListenerCallback mListenerCallback = new IListenerCallback() {
        @Override
        public void onDataChange(String parameterPath, Object newValue) {
            switch (parameterPath) {
                //TODO: fix problem with paramPath vs paramKey
                case "":
                    boolean errorValue = (boolean) newValue;
                    if (errorValue) {
                        Log.e(TAG, "Error received from Arduino!");
                        //TODO: Handle error!
                    }
                    break;
            }
        }
        @Override
        public void onFailure(String parameterPath, String errorMsg) {

        }
    };

    private IPushCallback mPushCallback = new IPushCallback() {
        @Override
        public void onSuccess(String parameterKey) {
            switch (parameterKey) {
                case "":
                    Log.d(TAG, "Brew request successful!");
            }
        }
        @Override
        public void onFailure(String parameterKey, String errorMsg) {
            switch (parameterKey) {
                case BREW_REQ:
                    Log.d(TAG, "Brew request failed! " + errorMsg);
            }
        }
    };

    private IPullCallback mPullCallback = new IPullCallback() {
        @Override
        public void onSuccess(String parameterKey, Object parameter) {

        }
        @Override
        public void onFailure(String parameterKey, String errorMsg) {

        }
    };
}
