//#include <SoftwareSerial.h>
#include <Smartcar.h>

Car car;

//int bluetoothTx = 0; //15
//int bluetoothRx = 1; //14

//SoftwareSerial bluetooth(bluetoothTx, bluetoothRx);

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
}

void loop() {
  controller();
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


