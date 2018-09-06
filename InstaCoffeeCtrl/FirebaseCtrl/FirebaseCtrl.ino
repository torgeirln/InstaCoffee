/*
 * This code should run on an ESP8266 module. In this case the module is built in on 
 * the Arduino Uno Wifi Developer Edition and communicating with the code on the 
 * ATmega using Serial.
 */
#include <ESP8266WiFi.h>
#include <Firebase.h>
#include <FirebaseArduino.h>
#include <FirebaseError.h>
#include "Constants.h"

#define RUN_PATH "InstaCoffee/arduinoCtrl/runBrewProcess"
#define FB_ERROR_PATH "InstaCoffee/androidFeedback/error"          // Feedback if error
#define FB_RUNNING_PATH "InstaCoffee/androidFeedback/isRunning"    // Feedback when runnning

bool isRunning;

void setup() {
  Serial.begin(9600);
  // connect to wifi.
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
//  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
//    Serial.print(".");
    delay(500);
  }
//  Serial.println();
//  Serial.print("connected: ");
//  Serial.println(WiFi.localIP());

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  isRunning = false;
}

void loop() {
  if (isRunning) {
    getStatus();  
  }
  // check run command on firebase
  bool run = Firebase.getBool(RUN_PATH);
  if (run && !isRunning) {
    Serial.print('r');    // run
    isRunning = true;
    Firebase.setBool(FB_RUNNING_PATH, isRunning);
  } else if (!run && isRunning) {
    Serial.print('s');    // stop
    isRunning = false;
    Firebase.setBool(FB_RUNNING_PATH, isRunning);
  }   
  if (Firebase.failed()) {
    Serial.println('e');
    Serial.print("Firebase failed: ");
    Serial.println(Firebase.error());
    return;
  }
  delay(1000);
}

// Check for updates from main program running on ATmega processor
void getStatus(){
  while(Serial.available()) {
    char command = Serial.read();
    if (command == 'f') {         // if task finished
      isRunning = false;
      Firebase.setBool(FB_RUNNING_PATH, isRunning);
    }
    // TODO: Listen for error in InstaCoffeeCtrl and publish to firebase
  }
}

