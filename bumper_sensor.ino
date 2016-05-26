#include <Smartcar.h>
#include <Wire.h>
SR04 ultrasonicSensor;
SR04 ultrasonicSensor2;
Car car;
const int TRIGGER_PIN = 6; 
const int ECHO_PIN = A5; 
const int TRIGGER_PIN2 = 8;
const int ECHO_PIN2 = 7;

int minrange = 50;
int maxrange = 10;



void setup() {
  
  Serial.begin(9600);
  ultrasonicSensor.attach(TRIGGER_PIN, ECHO_PIN);
  ultrasonicSensor2.attach(TRIGGER_PIN2, ECHO_PIN2);
  car.begin();
  
}

void loop() {
  Serial.println(String(ultrasonicSensor.getDistance())+" "+ String(ultrasonicSensor2.getDistance()));
   distanceCheckerS1();
  distanceCheckerS2();
  //delay(100);
}
   
    void distanceCheckerS1(){
     if (ultrasonicSensor.getDistance()<= minrange && ultrasonicSensor.getDistance()> 0){
      car.stop();
       Serial.println(ultrasonicSensor.getDistance());
       delay(100);
       //Serial.flush();
     }
   
  }
  void distanceCheckerS2(){
  if(ultrasonicSensor2.getDistance() <= minrange && ultrasonicSensor2.getDistance() > 0){
    //  car.stop();
      Serial.write("1");
      delay(100);
    
    }
}
    
