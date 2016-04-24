#include <Smartcar.h>
#include <Wire.h>
#include <Servo.h>
Odometer encoderLeft, encoderRight;
SR04 ultrasonicSensor;
SR04 ultrasonicSensor2;
Gyroscope gyro;
Car car;
Servo sensServo;

int minrange = 15;
int maxrange = 16;
int zero = 0;
const int TRIGGER_PIN = 7; //D7
const int ECHO_PIN = A4; //
const int TRIGGER_PIN2 = 6;
const int ECHO_PIN2 = A5; //A5

const int fSpeed = 90; //70% of the full speed forward
const int nSpeed = 50;
const int bSpeed = -50; //70% of the full speed backward
const int lDegrees = -46; //degrees to turn left
const int rDegrees = 46; //degrees to turn right
int servoPosition = 90;

void setup() {
  Serial.begin(9600);
  ultrasonicSensor.attach(TRIGGER_PIN, ECHO_PIN);
   ultrasonicSensor2.attach(TRIGGER_PIN2, ECHO_PIN2);
  car.begin(); //initialize the car using the encoders and the gyro
 //sensServo.attach(8);
  //sensServo.write(servoPosition);

}

void loop() {
   automatedMovement();
  //handleInput();
  distanceCheckerS1();
  distanceCheckerS2();
  sensorConflict();
 
}
void automatedMovement() {
   gyro.attach();
  encoderLeft.attach(2);
  encoderRight.attach(3);
  encoderLeft.begin();
  encoderRight.begin();
  car.begin(encoderLeft, encoderRight,gyro);
  car.enableCruiseControl();
  gyro.begin();
  car.go(0);//direction
  //car.rotate(90);
  encoderLeft =0;
  int tickGoal = (194 * 10) / 100;// calculating distance in inches
  while(encoderLeft.attach(2) < tickGoal){
    car.setSpeed(nSpeed);
  }
  car.stop();
  }

//distance sensor checker
    
    void distanceCheckerS1(){
     if (ultrasonicSensor.getDistance()<= minrange && ultrasonicSensor.getDistance()> zero){
      car.setSpeed(fSpeed);
       Serial.println(ultrasonicSensor.getDistance());
       delay(100);
       Serial.flush();
     }
    else if (ultrasonicSensor.getDistance() == zero && ultrasonicSensor.getDistance()>maxrange){
      car.setSpeed(nSpeed);
    }
  }
  void distanceCheckerS2(){
 
    if(ultrasonicSensor2.getDistance() > maxrange){
      Serial.write("0");
      //delay(100);
      //Serial3.flush();
    }else if(ultrasonicSensor2.getDistance() < minrange && ultrasonicSensor2.getDistance() > zero){
      car.stop();
      Serial.write("1");
      delay(100);
      Serial.flush();
    }
}

//in case both sensors are values at the same time 

void sensorConflict(){
 if((ultrasonicSensor.getDistance()&&ultrasonicSensor2.getDistance())<minrange){
   car.stop();
   Serial.write("1");
   delay(100);
   Serial.flush();
 }else if ((ultrasonicSensor.getDistance()&&ultrasonicSensor2.getDistance())==zero){
  Serial.write("0");
   car.setSpeed(nSpeed);
 }
}

