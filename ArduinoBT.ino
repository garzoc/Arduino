#include <Arduino.h>
#include <Smartcar.h>
#include <Servo.h>

Car car;
SR04 UltraSonic;
Servo sensServo;

const int TRIGGER_PIN = 6;
const int ECHO_PIN = A5; //A5

int servoPosition = 90;

void setup()
{
  //initialize the car
  car.begin();
  //Setup usb serial connection to computer if you want output in the serial monitor
  Serial.begin(9600);
  Serial.print("Serial ready");

  //Setup Bluetooth serial connection to android
  Serial3.begin(115200);
  Serial3.print("$$$");
  delay(100);
  Serial3.println("U,9600,N");
  Serial3.begin(9600);
  
  //Use this to set the waiting time before reads to 10-50 millieseconds. Default is 1 second.
  Serial3.setTimeout(10);

  UltraSonic.attach(TRIGGER_PIN, ECHO_PIN);

  sensServo.attach(A7);
  sensServo.write(servoPosition);
}

void loop() {
    if(Serial3.available()){
      //Serial.println("Recieved Message");
      String serialString = Serial3.readString();
      //Serial.println(serialString);
      switch(serialString.charAt(0)){
        case 'c':
          control(serialString);
          break;
        default:
          car.setSpeed(0);
    }
  
}
//checkDistance();
}

void control(String input){
  int theSpeed;
  int theAngle;
  //Serial.println("input = " + input); 
  parseSpeed(input, &theSpeed, &theAngle);
  //Serial.println("speed "+String(theSpeed));
  if(theSpeed==0){
    car.stop();
  }
  
  car.setSpeed(theSpeed);
  //Serial.println("angle "+ String(theAngle));
  car.setAngle(theAngle);
}

void parseSpeed(String setSpeed, int * newSpeed, int * angle){
  String string ="";
    for(int i = 1; i<setSpeed.length(); i++){
      if(setSpeed.charAt(i)=='|'){
        *newSpeed = string.toInt();//Serial3.parseInt(string);
        string = "";
        //Serial.print(*newSpeed);
      }else{
        string += setSpeed.charAt(i);
      }
    }
    *angle = string.toInt();//Serial3.parseInt(string);
    string = "";
    //Serial.print(*angle);
}

//This function checks the distance given by the ultrasonic sensor
void checkDistance(){
  int minrange = 15;
  int maxrange = 16;
  int zero = 0;
    if(UltraSonic.getDistance() > maxrange){
      Serial3.write("0");
      //delay(100);
      //Serial3.flush();
    }else if(UltraSonic.getDistance() < minrange && UltraSonic.getDistance() > zero){
      car.stop();
      Serial3.write("1");
      //delay(100);
      //Serial3.flush();
    }
}

//This function handels input during button control mode
//void controller() {
//  if (Serial3.available()) {
//    char input = (byte)Serial3.read();
//    switch (input) {
//      case 'f':
//        car.setSpeed(50);
//        car.setAngle(0);
//        break;
//      case 'b':
//        car.setSpeed(-50);
//        car.setAngle(0);
//        break;
//      case 'l':
//        car.setSpeed(50);
//        car.setAngle(-90);
//        break;
//      case 'r':
//        car.setSpeed(50);
//        car.setAngle(90);
//        break;
//      case 's':
//        car.setSpeed(0);
//        car.setAngle(0);
//        break;
//    }
//  }
//}


//This function handels the joystick control mode
//void joystickControl(){
//  }
//  if(Serial3.read()=='a'){
//  delay(1);
//  int angle = Serial3.read();
//  Serial.println(angle);
//  car.setAngle(angle);
//  }else if(Serial.read()=='p'){
//  delay(1);  
//  int power = Serial3.read();
//  Serial.println(power);
//  car.setSpeed(power);
//  }
//}
