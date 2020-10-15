//Weatcher station by Filip Kryczek: main station
//--------------------------------------------------
//used library:
//RF24: github.com/nRF24/RF24
//Low-Power: github.com/rocketscream/Low-Power
//Adafruit BMP280: github.com/adafruit/Adafruit_BMP280_Library (in Adafruit_BMP280.h must edit #define BMP280_ADDRESS (0x77) to #define BMP280_ADDRESS (0x76))
//SSD1306Ascii: github.com/greiman/SSD1306Ascii
//--------------------------------------------------
#include <SPI.h>
#include <nRF24L01.h>
#include <RF24.h>
#include <Wire.h>
#include <LowPower.h>
#include <Adafruit_BMP280.h>
#include <SSD1306Ascii.h>
#include <SSD1306AsciiWire.h>

#define LED1 4
#define LED2 5
#define LED3 6
#define LED4 7
#define CEPIN 9
#define CSPIN 10
#define BATTERY 6

const uint64_t pipeAddressTX = 0xFF00202100FF;
const uint64_t pipeAddressRX = 0xFFFF2021FFFF;
bool waiting, connect, message = true;
char command;
float remoteTemperature = 150.01, remoteHumidity = 100.01, remoteBattery = 5.01;
short sleepTime=0;

RF24 mainRadio(CEPIN, CSPIN);
Adafruit_BMP280 bmp280;
SSD1306AsciiWire leftDisplay, rightDisplay;

void setup()
{
  Serial.begin(9600);
  Serial.println("main weather station is connected");
  Serial.println("setup started");

  Serial.println("left display setup");
  leftDisplay.begin(&Adafruit128x64, 0x3D);
  Serial.println("right display setup");
  rightDisplay.begin(&Adafruit128x64, 0x3C);
  helloOLED();

  Serial.println("LED setup");
  pinMode(LED1, OUTPUT);
  pinMode(LED2, OUTPUT);
  pinMode(LED3, OUTPUT);
  pinMode(LED4, OUTPUT);
  helloLED();
  blinkLED(5);

  Serial.println("NRF24L01 setup");
  mainRadio.begin();
  mainRadio.setAutoAck(false);         //disable auto-acknowledge packets
  mainRadio.setChannel(100);           //set communication channel
  mainRadio.setPALevel(RF24_PA_LOW);   //set Power Amplifier (PA) level: 0dBm
  mainRadio.setDataRate(RF24_250KBPS); //set the transmission data rate
  mainRadio.setRetries(4, 5);          //delay and number of retries upon failed submit: 1000us
  mainRadio.openReadingPipe(1, pipeAddressRX);
  mainRadio.startListening();

  Serial.println("BM280 setup");
  bmp280.begin();

  Serial.println("connecting to remote startion");
  getOutdoorData();

  leftDisplay.clear();
  rightDisplay.clear();

  Serial.println("setup ended");
}
void helloOLED()
{
  leftDisplay.setFont(Adafruit5x7);
  leftDisplay.clear();
  leftDisplay.println("Design and implementa-");
  leftDisplay.println("tion of a weather");
  leftDisplay.println("system for road");
  leftDisplay.println("cyclists.");
  leftDisplay.println("");
  leftDisplay.println("");
  leftDisplay.println("     by Filip Kryczek");

  rightDisplay.set2X();
  rightDisplay.setFont(Adafruit5x7);
  rightDisplay.clear();
  rightDisplay.println("-----------");
  rightDisplay.println("---SETUP---");
  rightDisplay.println("-----------");
  rightDisplay.println("-----------");
}
void helloLED()
{
  pinMode(4, OUTPUT);
  digitalWrite(4, HIGH);
  pinMode(5, OUTPUT);
  digitalWrite(5, HIGH);
  pinMode(6, OUTPUT);
  digitalWrite(6, HIGH);
  pinMode(7, OUTPUT);
  digitalWrite(7, HIGH);
}
void blinkLED(short counter)
{
  for (; counter > 0; counter--)
  {
    LEDstatus(true, true, true, true);
    delay(100);
    LEDstatus(false, false, false, false);
    delay(100);
  }
}
void LEDstatus(bool first, bool second, bool third, bool fourth)
{
  if (first)
    digitalWrite(LED1, HIGH);
  else
    digitalWrite(LED1, LOW);
  if (second)
    digitalWrite(LED2, HIGH);
  else
    digitalWrite(LED2, LOW);
  if (third)
    digitalWrite(LED3, HIGH);
  else
    digitalWrite(LED3, LOW);
  if (fourth)
    digitalWrite(LED4, HIGH);
  else
    digitalWrite(LED4, LOW);
}
void sendCommand()
{
  bool sentData;
  mainRadio.stopListening();
  mainRadio.openWritingPipe(pipeAddressTX);
  sentData = mainRadio.write(&command, sizeof(command));
  mainRadio.startListening();
  if (sentData)
  {
    waiting = true;
    if (message)
      Serial.println("command was sent");
  }
  else
  {
    if (message)
      Serial.println("command wasn't sent");
  }
}
float receiveData()
{
  short resendCommandTime = 8000;
  //short receivedData = 0;
  short repeatCount = 2;
  float dataType = 0.0;

  while (waiting)
  {
    if (mainRadio.available())
    {
      mainRadio.read(&dataType, sizeof(dataType));
      if (message)
        Serial.println(dataType);
      repeatCount = 0;
      resendCommandTime = 500;
    }
    delay(50);
    resendCommandTime = resendCommandTime - 50;
    if (resendCommandTime < 0)
      if (repeatCount > 0)
      {
        sendCommand();
        resendCommandTime = 8000;
        repeatCount--;
      }
      else
        break;
  }
  if (message)
    Serial.println("all data from remote station were received");
  waiting = false;
  return dataType;
}
void getOutdoorData()
{
  message = false;
  command = 'C';
  sendCommand();
  if (receiveData() == 7918.00)
  {
    command = 'T';
    sendCommand();
    remoteTemperature = receiveData();

    command = 'H';
    sendCommand();
    remoteHumidity = receiveData();

    command = 'B';
    sendCommand();
    remoteBattery = receiveData();

    command = 'D';
    sendCommand();
    receiveData();
  }
  message = true;
}
void drawLeftDisplay()
{
  leftDisplay.set1X();
  leftDisplay.setFont(Adafruit5x7);
  leftDisplay.setCursor(28, 0);
  leftDisplay.println("---INDOOR---");
  leftDisplay.setFont(Verdana12_bold);

  leftDisplay.print("Temp.: ");
  leftDisplay.print(bmp280.readTemperature());
  leftDisplay.println(" *C");

  leftDisplay.print("Press.: ");
  leftDisplay.print((bmp280.readPressure()) / 100);
  leftDisplay.println(" hPa");

  leftDisplay.print("Alt.: ");
  leftDisplay.print(bmp280.readAltitude());
  leftDisplay.println(" m a.s.l");

  leftDisplay.setFont(Adafruit5x7);
  leftDisplay.setCursor(92, 8);
  leftDisplay.print(analogRead(BATTERY) * (5.0 / 512.0));
  leftDisplay.println("V");
}
void drawRightDisplay()
{
  rightDisplay.set1X();
  rightDisplay.setFont(Adafruit5x7);
  rightDisplay.setCursor(25, 0);
  rightDisplay.println("---OUTDOOR---");
  rightDisplay.setFont(Verdana12_bold);

  rightDisplay.setCursor(0, 2);
  rightDisplay.print("Temp.: ");
  if (remoteTemperature < 150.01)
    rightDisplay.print(remoteTemperature);
  else
    rightDisplay.print("N/A");
  rightDisplay.println(" *C");

  rightDisplay.setCursor(0, 5);
  rightDisplay.print("Hum.: ");
  if (remoteHumidity < 100.01)
    rightDisplay.print(remoteHumidity);
  else
    rightDisplay.print("N/A");
  rightDisplay.println(" %RH");

  rightDisplay.setFont(Adafruit5x7);
  rightDisplay.setCursor(92, 8);
  if (remoteBattery < 5.01)
    rightDisplay.print(remoteBattery);
  else
    rightDisplay.print("N/A");
  rightDisplay.println("V");

  getOutdoorData();
}
void loop()
{
  if (Serial.available() > 0)
  {
    command = Serial.read();
    if (command > 64 && command < 91)
    {
      Serial.println(command);
      switch (command)
      {
      case 'C':
        sendCommand();
        if (receiveData() == 7918.00)
        {
          connect = true;

          rightDisplay.set2X();
          rightDisplay.setFont(Adafruit5x7);
          rightDisplay.clear();
          rightDisplay.println("-----------");
          rightDisplay.println("-CONNECTED-");
          rightDisplay.println("-----------");
          rightDisplay.println("-----------");
        }
        break;
      case 'D':
        sendCommand();
        if (receiveData() == 797070.00)
        {
          connect = false;
          rightDisplay.clear();
          sleepTime=0;
        }

        break;
      case 'O':
        command = 'T';
        sendCommand();
        receiveData();
        break;
      case 'H':
        sendCommand();
        receiveData();
        break;
      case 'B':
        sendCommand();
        message = false;
        Serial.println(receiveData());
        Serial.println(analogRead(BATTERY) * (5.0 / 1024.0));
        message = true;
        break;
      case 'R':
        sendCommand();
        if (receiveData() == 828384.0)
          ;
        {
          connect = false;
          rightDisplay.clear();
          sleepTime=0;
        }
        break;
      case 'I':
        Serial.println(84697780.00);
        Serial.println(bmp280.readTemperature());
        Serial.println("all data from main station were received");
        break;
      case 'P':
        Serial.println(80826983.00);
        Serial.println(bmp280.readPressure());
        Serial.println("all data from main station were received");
        break;
      case 'S':
        leftDisplay.clear();
        rightDisplay.clear();
        sleepTime=0;
        break;
      case 'A':
        Serial.println(84697780.00);
        Serial.println(bmp280.readAltitude());
        Serial.println("all data from main station were received");
        break;
      }
    }
  }
  else if (waiting == false && connect == false)
  {
    sleepTime--;
    if(sleepTime<1){
      drawLeftDisplay();
      drawRightDisplay();
      sleepTime=8;
    }
      LowPower.powerDown(SLEEP_8S, ADC_OFF, BOD_OFF);
  }
}
