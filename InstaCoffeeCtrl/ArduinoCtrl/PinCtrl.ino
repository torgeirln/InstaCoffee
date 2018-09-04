#include <Wire.h>
#include <UnoWiFiDevEd.h>
#include <UnoWiFiDevEdSerial1.h>

#define BAUD 9600

WifiData ESPSerial;
bool brewing = false;

void setup() {
  Serial.begin(BAUD);
  // initialize digital pin LED_BUILTIN as an output.
  pinMode(LED_BUILTIN, OUTPUT);
  ESPSerial.begin(BAUD);
}

void loop() {
  
    while (ESPSerial.available()) {
    char command = (char) ESPSerial.read();
    Serial.write(command);
//    if (command == 'r') {
//      digitalWrite(LED_BUILTIN, HIGH);   // turn the LED on (HIGH is the voltage level)
//    } else if (command == 's') {
//      digitalWrite(LED_BUILTIN, LOW);         
//    } 
    if (command == 'h') {
      digitalWrite(LED_BUILTIN, HIGH);         
    } else if (command == 'l') {
      digitalWrite(LED_BUILTIN, LOW);         
    }
    
  }
  delay(1000);                       // wait for a second
}
