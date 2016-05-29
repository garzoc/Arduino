#include<Wire.h>
#include<Smartcar.h>
SR04 ultrasonicSensor;
const int TRIGGER_PIN=6; //D6
const int ECHO_PIN=5; //D5

boolean obstacleDetected=false;
Car car;

void setup()
{
  car.begin();
  Serial.begin(9600);
 ultrasonicSensor.attach(TRIGGER_PIN, ECHO_PIN);
obstacleDetected=false;
}

void loop()
{
  int distance=ultrasonicSensor.getDistance();
  if(distance<15&&distance!=0){//as long as there is an obstacle the car won't move
      Serial.println(distance);
     obstacleDetected=true;
     car.stop();
  }else if(obstacleDetected==false){
    car.setSpeed(40);
  }else{}

   //now do something to get the car moving again


//ßdelay(100);


}
