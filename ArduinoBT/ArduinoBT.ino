#include <Arduino.h>
#include <Smartcar.h>
#include <Servo.h>
#include <NewPing.h>
#include <SharpIR.h>

/*###############################
 * SONIC RADAR
 ##############################*/
#define PINSERVO A7//Pin attached to the servo
#define PINTRIGGER 6//Ultrasoic trigger pin
#define PINECO A5//Ultrasoic eco/output pin
#define MAXDISTANCE 80//maximum viewable distance
//----- LIMIT ANGLE indicates the from and to the trace. In this case -80º..80º
//** This should be from 15 to 165 degrees. (My change)
#define ANGLELIMIT 90//is the mid value and alos max in min value
//----- HIGH ANGLES is the increase applied to move the servo
#define ANGLEDELTA 1

Servo myServo;//front servo
NewPing ultraS(PINTRIGGER, PINECO, MAXDISTANCE);//use of library that handels the ultrsonic sensor output
int angle = 0;//angle of the front servo
int distance = 0;// distance read by the front detector
int dir = 1;//current directiob in which the front servo is going

/*###############################
 * CAMERA
 ##############################*/
#define CameraBasePin A8//Pin attached to the servo
#define CameraHeadPin A9//Pin attached to the servo
int baseAngle;
int headAngle;
int headDir=0;
int baseDir=0;
Servo cameraBaseServo;//front servo
Servo cameraHeadServo;//front servo
boolean cameraMoving=false;
long cameraLastMilli;//the time measument in milli seconds/timer
/*###############################
 * ir mapping
 ##############################*/
#define ir A14//pin for the ir sensor
#define model 20150//model 
SharpIR sharp(ir, 25, 93, model);//use of library that handels the ir sensor output

/*###############################
 * CAR
 ##############################*/
Car car;//car class from car shield
Odometer encoderLeft, encoderRight;
boolean radarIsActive=false;//the front ultrasonic sensro is currently in use along with the servo. Note: this is used for the radar
boolean mappingIsActive=false;//the front ir sensro is currently in use along with the servo. Note: this is used for the mapping
boolean safeDrivingOn=false;/*the front ultrasonic sensro is currently in use when the servo is static servo. 
Note: this is used for stopping the car when a obstackle is detected in fron of the car*/

long lastMilli;//the time measument in milli seconds/timer

void setup()
{
  //encoders
  encoderLeft.attach(2);
  encoderRight.attach(3);
  encoderLeft.begin();
  encoderRight.begin();
  //CAMERA
  cameraBaseServo.attach(CameraBasePin);
  cameraHeadServo.attach(CameraHeadPin);
  baseAngle=90;
  headAngle=135;
  cameraBaseServo.write(baseAngle);
  cameraHeadServo.write(headAngle);
  //FRONT SERVO AND SERIAL
  myServo.attach(PINSERVO);
  pinMode (ir, INPUT); 
  Serial.begin(9600);
  Serial3.begin(9600);
  Serial3.setTimeout(10);
  Serial.setTimeout(10);
  //TIMERS
  cameraLastMilli=millis();
  lastMilli=millis();
  //car.begin(encoderLeft, encoderRight);
  car.begin();
  car.enableCruiseControl();
}

void loop() {
  //cameraBaseServo.write(45);
  //cameraHeadServo.write(90);
  //cameraMove("v10|-10");
  car.updateMotors();
  if(Serial3.available()){
    //Serial.println("hej");
      String serialString = Serial3.readString();
      //Serial.println(serialString);
      switch(serialString.charAt(0)){//do something based on the first character in the string recieved, if fromat is incorrect stop the car.
        case 'c'://control mode sets the speed and rotation of car
          control(serialString);
          break;
        case 'd'://radar mode scans the area 180° with ultrasonic sensor and the servo and is used for a radar. Thhis cannot be used with safe driving or mapping mode
          safeDrivingOn=false;
          if(!mappingIsActive) radarIsActive=!radarIsActive;
          break;
         case 'm'://mapping mode scans the area 180° with ir sensor and the servo and is used for a mapping. This cannot be used with safe driving or radar mode
          safeDrivingOn=false;
          if(!radarIsActive) mappingIsActive=!mappingIsActive;
          break;
         case 's'://safe driving mode stops the car if an obstacle is in front of it cannot be used along with radar or mapping mode
          if(!radarIsActive&&!mappingIsActive) safeDrivingOn=!safeDrivingOn;
          break;
         case 'q'://stop all except camera
          mappingIsActive=radarIsActive=safeDrivingOn=false;
          car.stop();
          break;
        case 'v'://start camera movement and set direction
          cameraMoving=true;
          cameraParse(serialString);
          break;
        case 'w'://stop camera
          cameraMoving=false;
          cameraParse("0");
          break;
        default:
          car.setSpeed(0);
      }
  }
   //if(radarIsActive) checkDistance();//test for radar mode
   //if(mappingIsActive) mapping();//test for mapping mode
   if(cameraMoving)cameraMove();
   servoDetection();
   if(safeDrivingOn) detectObstacle();//test for safedriving mode
}

/*##################################
CAMERA 
###################################*/
void cameraParse(String input){
  //int headDir;
  //int baseDir;
  parseSpeed(input, &baseDir, &headDir);//parse camera movement
  //Serial.println(String(baseDir));
  //headAngle+=headDir<0 && headAngle>90?-5:headAngle<180&&headDir>0?5:0;
  //cameraHeadServo.write(headAngle);
  //baseAngle+=baseDir<0 && baseAngle>0?-5:baseAngle<180&&baseDir>0?5:0;
  //cameraBaseServo.write(baseAngle);
}

void cameraMove(){
  if(millis()-cameraLastMilli>50){
    headAngle+=5*(headDir<0 && headAngle>90?-1:headAngle<180&&headDir>0?1:0);
    baseAngle+=5*(baseDir<0 && baseAngle>0?-1:baseAngle<180&&baseDir>0?1:0);
    cameraHeadServo.write(headAngle);
    cameraBaseServo.write(baseAngle);
    cameraLastMilli=millis();
  }
  
}

/*##################################
RADAR & IR 
###################################*/

void servoDetection(){
    if((millis()-lastMilli)>50){//check the time that has passed since the last time we did a time measument if greater than 50 milliseconds do something
      int temS=0;
      if((distance=radarIsActive?(temS=getSonicDistance()-1)+1:temS=0)-temS||(distance=mappingIsActive?(temS=(getIrDistance()/2)-1)+1:temS=0)-temS){
        myServo.write(angle + ANGLELIMIT);//change angle with an offset in this case angle is the offset
        Serial3.print(String(angle+ANGLELIMIT)+"&"+String(distance)+"|");//print the result
        // If the variable-angle has reached one of its limits, change direction
        if (angle >= ANGLELIMIT || angle <= -ANGLELIMIT){
         dir = -dir;
        } 
        angle += (dir * ANGLEDELTA);//update the offset angle
        lastMilli=millis();//update time measument
      }
    }
 }

int getSonicDistance(){
  int temp;
  return (temp=ultraS.ping_cm())>80?80:temp;//get the distance from the ultrasonic sensor 
}

int getIrDistance(){
  int tempDistance=sharp.distance();//get the distance from the ir sensor
  if(tempDistance<50 && tempDistance>10){//send measurement if the distance is within a certain range else send with max range
      Serial.println(tempDistance);
        return tempDistance;
      }else{
        return 50;//retunr max distance of ir sensor
      }
}

/*##################################
SPEED
###################################*/
void control(String input){
  int theSpeed;
  int theAngle;
  parseSpeed(input, &theSpeed, &theAngle);//parse the speed and angle control
  if(theSpeed==0){//if format is incorrect or speed = 0 stop the car
    car.stop();
  }
  int temp=0;
 
  theSpeed=(temp=0.35*theSpeed)<50?theSpeed!=0?50:0:temp;
  Serial.println("the speed is "+String(theSpeed));
  theSpeed=theAngle>90||-90<theAngle?theSpeed>75?75:theSpeed:theSpeed;
  car.setSpeed(theSpeed);//set speed
  
  car.setAngle(theAngle);//set angle
  //car.rotate(theAngle);
}

void parseSpeed(String setSpeed, int * newSpeed, int * angle){
  String string ="";
    for(int i = 1; i<setSpeed.length(); i++){
      if(setSpeed.charAt(i)=='|'){//if character is =| push the parse value the newSpeed than clear the temp string and skip to the next character
        *newSpeed = string.toInt();
        string = "";
      }else{
        string += setSpeed.charAt(i);//push characters the the temp string
      }
    }
    *angle = string.toInt();//when the entrire string has been read push the value to angle
    string = "";//clear the string
}

/*##################################
BASIC DETECTION /SAFE DRIVING
###################################*/

void detectObstacle(){
  myServo.write(90);//set servo so it look straight forward
  distance = sharp.distance()/2;//set the distance detected to from the ir the the distance in centimeters
  if(distance<=20&&10<=distance){//stop the car if the distance is within a certain range
      car.stop();  
  }

}

/*##################################
radar  sonic
###################################*/
/*//This function checks the distance given by the ultrasonic sensor
void checkDistance(){
  if((millis()-lastMilli)>50){//check the time that has passed since the last time we did a time measument if greater than 50 milliseconds do something
      myServo.write(angle + ANGLELIMIT);//change angle with an offset in this case angle is the offset
      distance = ultraS.ping_cm();//get the distance from the ultrasonic sensor
      Serial3.print(String(angle+90)+"&"+String(distance)+"|");//print the result
      // If the variable-angle has reached one of its limits, change direction
      if (angle >= ANGLELIMIT || angle <= -ANGLELIMIT){
        dir = -dir;
      } 
      angle += (dir * ANGLEDELTA);//update the offset angle
      lastMilli=millis();//update time measument
  }
}*/


/*##################################
mapping ir
###################################*/
/*//This function checks the distance given by the ultrasonic sensor
void mapping(){
  if((millis()-lastMilli)>50){//check the time that has passed since the last time we did a time measument if greater than 50 milliseconds do something
      myServo.write(angle + ANGLELIMIT);//change angle with an offset in this case angle is the offset
      distance = sharp.distance();//get the distance from the ir sensor
      if(distance<50 && distance>10){//send measurement if the distance is within a certain range else send with max range
        Serial3.println(String(angle+90)+"&"+String(distance/2)+"|");
      }else{
        Serial3.println(String(angle+90)+"&"+String(25)+"|");
      }
      // If the variable-angle has reached one of its limits, change direction
      if (angle >= ANGLELIMIT || angle <= -ANGLELIMIT){
        dir = -dir;
      } 
      angle += (dir * ANGLEDELTA);//update the offset angle
      lastMilli=millis();//update time measument
  }
}*/


