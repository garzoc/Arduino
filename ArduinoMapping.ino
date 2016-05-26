#include <SharpIR.h>
#include <Servo.h>
#include <Smartcar.h>

#define ir A14
#define model 20150

int pos;
boolean directionLeft = false;
int Degrades = 1;

Servo myServo;
SharpIR sharp(ir, 25, 93, model);

void setup(){
  Serial3.begin(9600);
  Serial3.setTimeout(10);
  
  pinMode (ir, INPUT);
  myServo.attach(A7);
  pos = 0;
}

void loop(){
  myServo.write(pos);              
  delay(100);
  !directionLeft&&pos<180-Degrades?pos=pos+Degrades:directionLeft=true;
  directionLeft&&pos>0+Degrades?pos=pos-Degrades:directionLeft=false;
  int dis=sharp.distance();
  if(dis<50 && dis>10){
  Serial3.println(String(pos)+"&"+String(dis/2)+"|");
  }else{
    Serial3.println(String(pos)+"&"+String(25)+"|");
  }
}


  

