#include <NewPing.h>
#include <Servo.h>

#define PINSERVO A7
#define PINTRIGGER 6
#define PINECO A5
#define MAXDISTANCE 200

//----- LIMIT ANGLE indicates the from and to the trace. In this case -80º..80º
//** This should be from 15 to 165 degrees. (My change)
#define ANGLELIMIT 80

//----- HIGH ANGLES is the increase applied to move the servo
#define ANGLEDELTA 1
Servo myServo;
NewPing ultraS(PINTRIGGER, PINECO, MAXDISTANCE);

int angle = 0;
int distance = 0;
int dir = 1;

void setup()
{
  myServo.attach(PINSERVO); 
// Initialise bluetooth. As I'm using a plate ZUM-bq One of the initialize at 19200 bps
// which is marking the manufacturer. If another Bluetooth module is used
// the rate for that module.
  //Serial3.begin(9600);
//   Serial3.begin(115200);
//  Serial3.print("$$$");
//  delay(100);
//  Serial3.println("U,9600,N");
  Serial.begin(9600);
  Serial3.begin(9600);
   Serial3.setTimeout(10);
   Serial.setTimeout(10);
}

void loop()
{
  delay(50);
  myServo.write(angle + ANGLELIMIT);
  //myServo.write(90);
  distance = ultraS.ping_cm();
//  Serial3.print(angle,DEC);
//  Serial3.print(",");
//  Serial3.print(distance,DEC);
  Serial3.print(String(angle+90)+"&"+String(distance)+"|");
  Serial.println(ultraS.ping_cm());
 
 // If the variable-angle has reached one of its limits, change direction
  if (angle >= ANGLELIMIT || angle <= -ANGLELIMIT){
     dir = -dir;
  } 
  angle += (dir * ANGLEDELTA);
}

