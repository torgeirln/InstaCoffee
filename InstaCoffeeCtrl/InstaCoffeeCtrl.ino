/* 
 *  This code is running on the ATmega processor on an Arduino Uno Wifi Developer Edition.
 */
#include <Wire.h>
#include <UnoWiFiDevEd.h>

#define BAUD 9600

// Pin assignments
int aPin = 2;         //IN1: coil a one end
int bPin = 3;         //IN2: coil b one end
int aPrimePin = 4;    //IN3: coil aPrime other end of coil a
int bPrimePin = 5;    //IN4: coil bPrime other end of coil b

int currentDeg = 0;
int btnDeg = 45;
int stepDelay = 2;    // The delay between each step in milliseconds
int steps = 0;        // The number of steps
bool stepRunning = false;
WifiData ESPSerial;   // Communication with ESP module

void setup() {
  Serial.begin(BAUD);
  // Init pinmodes
  pinMode(aPin, OUTPUT);
  pinMode(bPin, OUTPUT);
  pinMode(aPrimePin, OUTPUT);
  pinMode(bPrimePin, OUTPUT);
  // Start with all coils in stepper motor off
  digitalWrite(aPin, LOW);
  digitalWrite(bPin, LOW);
  digitalWrite(aPrimePin, LOW);
  digitalWrite(bPrimePin, LOW);
  // initialize digital pin LED_BUILTIN as an output
  pinMode(LED_BUILTIN, OUTPUT);
  // start serial communication with ESP module
  ESPSerial.begin(BAUD);
}

void loop() {
  
  while (ESPSerial.available()) {
    char command = (char) ESPSerial.read();
    Serial.println(command);
    if (command == 'r') {
      digitalWrite(LED_BUILTIN, HIGH);   // turn the LED on (HIGH is the voltage level)
      pressBrewBtn();
    } else if (command == 's') {
      digitalWrite(LED_BUILTIN, LOW); 
      resetStepper();        
    }    
  }
  delay(1000);                       // wait for a second
}

//initialize rotation anti clockwise
void initACW(){   
  aPin = 5;
  bPin = 4;
  aPrimePin = 3;
  bPrimePin = 2;
}

//initialize rotation clockwise
void initCW(){
  aPin = 2;
  bPin = 3;
  aPrimePin = 4;
  bPrimePin = 5;
}

void updateDeg() {
  if (aPin == 5){
    steps = steps - 8;
  }
  else if (aPin == 2){
    steps = steps + 8;
  }
  currentDeg = (360.0 * (steps / 4096.0));
}

// Rotate the stepper motor 8 steps
void rotate() {
  updateStepperPins(HIGH, LOW, LOW, LOW);
  updateStepperPins(HIGH, HIGH, LOW, LOW);
  updateStepperPins(LOW, HIGH, LOW, LOW);
  updateStepperPins(LOW, HIGH, HIGH, LOW);
  updateStepperPins(LOW, LOW, HIGH, LOW);
  updateStepperPins(LOW, LOW, HIGH, HIGH);
  updateStepperPins(LOW, LOW, LOW, HIGH);
  updateDeg();
}

// Make the stepper motor press the brew button on coffee machine
void pressBrewBtn() {
  initCW();  
  stepRunning = true;
  while(currentDeg < btnDeg) {
    rotate();
  }
  delay(500);
  resetStepper();
}

// Reset the stepper motor to its initial state
void resetStepper() {
  initACW();
  stepRunning = true;
  while(currentDeg > 0) {
    rotate();
  }
  stepRunning = false;
}

void updateStepperPins(uint8_t aPinVal, uint8_t bPinVal, uint8_t aPrimePinVal, uint8_t bPrimePinVal) {
  digitalWrite(aPin, aPinVal);
  digitalWrite(bPin, bPinVal);
  digitalWrite(aPrimePin, aPrimePinVal);
  digitalWrite(bPrimePin, bPrimePinVal);
  delay(stepDelay); 
}

