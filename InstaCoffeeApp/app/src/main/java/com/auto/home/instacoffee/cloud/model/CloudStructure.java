package com.auto.home.instacoffee.cloud.model;

import com.google.firebase.database.Exclude;

public class CloudStructure {

    public ArduinoCtrl arduinoCtrl;
    public AndroidFeedback androidFeedback;

    public CloudStructure() {
        arduinoCtrl = new ArduinoCtrl();
        androidFeedback = new AndroidFeedback();
    }

    public static class ArduinoCtrl {
        @Exclude
        public String runBrewVar = "runBrewProses";

        public boolean runBrewProses;

        public ArduinoCtrl() {
            runBrewProses = false;
        }
    }

    public static class AndroidFeedback {
        @Exclude
        public String isRunningVar = "isRunning";
        @Exclude
        public String errorVar = "error";
        @Exclude
        public String dcSpeedVar = "dcSpeed";
        @Exclude
        public String stepperSpeedVar = "stepperSpeed";

        public boolean isRunning;
        public boolean error;
        public int dcSpeed;
        public int stepperSpeed;

        public AndroidFeedback() {
            isRunning = false;
            error = false;
            dcSpeed = 0;
            stepperSpeed = 0;
        }
    }
}