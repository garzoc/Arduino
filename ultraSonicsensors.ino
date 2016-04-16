#include <Smartcar.h>
#include <Wire.h>
#include <Servo.h>
SR04 ultrasonicSensor;
Car car;
Servo sensServo;
const int TRIGGER_PIN = 7; //D7
const int ECHO_PIN = A4; //
const int fSpeed = 90; //70% of the full speed forward
const int nSpeed = 20;
const int bSpeed = -50; //70% of the full speed backward
const int lDegrees = -46; //degrees to turn left
const int rDegrees = 46; //degrees to turn right
int servoPosition = 90;

void setup() {
  Serial.begin(9600);
  ultrasonicSensor.attach(TRIGGER_PIN, ECHO_PIN);
  car.begin(); //initialize the car using the encoders and the gyro
 //sensServo.attach(8);
  //sensServo.write(servoPosition);

}

void loop() {

  handleInput();
 // distanceChecker();
}
void handleInput() { //handle serial input if there is any
  if (Serial.available()) {
    char input = Serial.read(); //read everything that has been received so far and log down the last entry
    switch (input) {
      case 'l': //rotate counter-clockwise going forward
        car.setSpeed(nSpeed);
        car.setAngle(lDegrees);
        break;
      case 'r': //turn clock-wise
        car.setSpeed(nSpeed);
        car.setAngle(rDegrees);
        break;
      case 'f': //go ahead
        car.setSpeed(nSpeed);
        car.setAngle(0);
        break;
      case 'b': //go back
        car.setSpeed(bSpeed);
        car.setAngle(20);
        break;
      case 's': //stop
        car.setSpeed(0);
        car.setAngle(0);
        break;
      default: 
        car.setSpeed(0);
        car.setAngle(0);
    
    }}}
    void distanceChecker(){
     if (ultrasonicSensor.getDistance()<=15){
      car.setSpeed(fSpeed);
       Serial.println(ultrasonicSensor.getDistance());
       delay(100);
     }
    else if (ultrasonicSensor.getDistance()==0){
      car.stop();
    }
  }

