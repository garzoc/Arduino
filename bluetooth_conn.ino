#include <Smartcar.h>
#include <Wire.h>
#include <Servo.h>
Car car;
const int fSpeed = 90; //70% of the full speed forward
const int bSpeed = -50; //70% of the full speed backward
const int lDegrees = -46; //degrees to turn left
const int rDegrees = 46; //degrees to turn right

void setup() {
  // put your setup code here, to run once:
Serial.begin(9600);
car.begin(); //initialize the car using the encoders and the gyro
}
void loop() {
  // put your main code here, to run repeatedly:

  Serial.println("Hej");
  delay(1000);
handleInput();
}
void handleInput() { //handle serial input if there is any
  if (Serial.available()) {
    char input = Serial.read(); //read everything that has been received so far and log down the last entry
    switch (input) {
      case 'l': //rotate counter-clockwise going forward
        car.setSpeed(fSpeed);
        car.setAngle(lDegrees);
        break;
      case 'r': //turn clock-wise
        car.setSpeed(fSpeed);
        car.setAngle(rDegrees);
        break;
      case 'f': //go ahead
        car.setSpeed(fSpeed);
        car.setAngle(40);
        break;
      case 'b': //go back
        car.setSpeed(bSpeed);
        car.setAngle(20);
        break;
      default: 
        car.setSpeed(0
        );
        car.setAngle(0);
    }
  }
}
