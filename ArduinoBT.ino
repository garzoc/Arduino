//#include <SoftwareSerial.h>
#include <Smartcar.h>
#include <Servo.h>

Car car;
SR04 UltraSonic;
Servo sensServo;

const int TRIGGER_PIN = 6;
const int ECHO_PIN = A5;

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

  UltraSonic.attach(TRIGGER_PIN, ECHO_PIN);

  sensServo.attach(8);
  sensServo.write(servoPosition);
}

void loop() {
  controller();
  //Serial.println(UltraSonic.getDistance());
  checkDistance();
}

//This is the controller that handels the input
void controller() {
  if (Serial3.available()) {
    char input = (byte)Serial3.read();
    switch (input) {
      case 'f':
        car.setSpeed(50);
        car.setAngle(0);
        break;
      case 'b':
        car.setSpeed(-50);
        car.setAngle(0);
        break;
      case 'l':
        car.setSpeed(50);
        car.setAngle(-90);
        break;
      case 'r':
        car.setSpeed(50);
        car.setAngle(90);
        break;
      case 's':
        car.setSpeed(0);
        car.setAngle(0);
        break;
    }
  }
}

void checkDistance(){
  if(UltraSonic.getDistance()<= 15){
    car.stop();
    Serial3.write(1);
    Serial.println(UltraSonic.getDistance());
    delay(100);
  }
}


