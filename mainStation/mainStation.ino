//Weatcher station by Filip Kryczek: main station
//--------------------------------------------------
//used library:
//RF24: github.com/nRF24/RF24
//Low-Power: github.com/rocketscream/Low-Power
//Adafruit BMP280: github.com/adafruit/Adafruit_BMP280_Library (in Adafruit_BMP280.h must edit #define BMP280_ADDRESS (0x77) to #define BMP280_ADDRESS (0x76))
//Adafruit SSD1306: https://github.com/adafruit/Adafruit_SSD1306
//Adafruit GFX: github.com/adafruit/Adafruit-GFX-Library
//Adafruit BusIO: github.com/adafruit/Adafruit_BusIO
//--------------------------------------------------
#include <SPI.h>
#include <nRF24L01.h>
#include <RF24.h>
#include <Wire.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>
#include <Adafruit_BMP280.h>

#define LED1 4
#define LED2 5
#define LED3 6
#define LED4 7
#define CEPIN 9
#define CSPIN 10
#define BATTERY 6

const uint64_t pipeAddressTX = 0xFF00202100FF;
const uint64_t pipeAddressRX = 0xFFFF2021FFFF;
bool waiting;
String command = "";

RF24 mainRadio(CEPIN, CSPIN);
Adafruit_BMP280 bmp280;
Adafruit_SSD1306 leftDisplay(128, 64, &Wire);
Adafruit_SSD1306 rightDisplay(128, 64, &Wire);

void setup()
{
  pinMode(LED1, OUTPUT);
  pinMode(LED2, OUTPUT);
  pinMode(LED3, OUTPUT);
  pinMode(LED4, OUTPUT);


  helloWorld();
  blinkLED(5);

  Serial.begin(9600);
  Serial.println("main weatcher station is connected");
  Serial.println("setup started");

  Serial.println("NRF24L01 setup");
  mainRadio.begin();
  mainRadio.setAutoAck(false);         //disable auto-acknowlede packets
  mainRadio.setChannel(100);           //set communication channel
  mainRadio.setPALevel(RF24_PA_LOW);   //set Power Amplifier (PA) level: 0dBm
  mainRadio.setDataRate(RF24_250KBPS); //set the transmission data rate
  mainRadio.setRetries(4, 5);          //delay and number of retries upon failed submit: 1000us
  mainRadio.openReadingPipe(1, pipeAddressRX);
  mainRadio.startListening();

  Serial.println("BM280 setup");
  bmp280.begin();

  Serial.println("Display 1 setup");
  leftDisplay.begin(SSD1306_SWITCHCAPVCC, 0x3C);
  rightDisplay.begin(SSD1306_SWITCHCAPVCC, 0x3C);
  testdrawchar();

  Serial.println("Display 2 setup");
}
void testdrawchar(void)
{
  leftDisplay.clearDisplay();

  leftDisplay.setTextSize(1);               // Normal 1:1 pixel scale
  leftDisplay.setTextColor(SSD1306_WHITE);  // Draw white text
  leftDisplay.setCursor(0, 0);              // Start at top-left corner
  leftDisplay.println(F("TAKI TO TEST 0")); // Use full 256 char 'Code Page 437' font
  leftDisplay.println(F("TAKI TO TEST 1"));
  leftDisplay.println(F("TAKI TO TEST 2"));
  leftDisplay.println(F("TAKI TO TEST 3"));
  leftDisplay.println(F("TAKI TO TEST 4"));
  leftDisplay.println(F("TAKI TO TEST 5"));
  leftDisplay.println(F("TAKI TO TEST 6"));
  leftDisplay.println(F("TAKI TO TEST 7"));
  leftDisplay.display();
  delay(2000);
}
void helloWorld()
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

void LEDstatus(bool first, bool secound, bool third, bool fourth)
{
  if (first)
    digitalWrite(LED1, HIGH);
  else
    digitalWrite(LED1, LOW);
  if (secound)
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
void sendCommand(char command)
{
  bool sentData;
  mainRadio.stopListening();
  mainRadio.openWritingPipe(pipeAddressTX);
  sentData = mainRadio.write(&command, sizeof(command));
  mainRadio.startListening();
  if (sentData)
  {
    waiting = true;
    Serial.println("command was sent");
  }
  else
  {
    Serial.println("command wasn't sent");
  }
}

float receiveData(char command)
{
  short resendCommandTime = 8000;
  short recivedData = 0;
  short repeatCount = 2;
  float dataType;

  while (waiting)
  {
    if (mainRadio.available())
    {
      mainRadio.read(&dataType, sizeof(dataType));
      Serial.println(dataType);
      repeatCount = 0;
      resendCommandTime = 500;
    }
    delay(50);
    resendCommandTime = resendCommandTime - 50;
    if (resendCommandTime < 0)
      if (repeatCount > 0)
      {
        sendCommand(command);
        resendCommandTime = 8000;
        repeatCount--;
      }
      else
        break;
  }
  Serial.println("all data from remote station were recived");
  waiting = false;
}

void loop()
{
  if (Serial.available() > 0)
  {
    //animation that arduino is connected to computer
    command = Serial.readStringUntil('\n');
    Serial.println(command[0]);
    switch (command[0])
    {
    case 'C':
      sendCommand('C');
      receiveData('C');
      break;
    case 'D':
      sendCommand('D');
      receiveData('D');
      break;
    case 'O':
      sendCommand('T');
      receiveData('T');
      break;
    case 'H':
      sendCommand('H');
      receiveData('H');
      break;
    case 'B':
      sendCommand('B');
      receiveData('B');

      Serial.println(analogRead(BATTERY) * (5.0 / 1024.0));
      break;
    case 'R':
      sendCommand('R');
      receiveData('R');
      break;
    case 'I':
      Serial.println(bmp280.readTemperature());
      break;
    case 'P':
      Serial.println(bmp280.readPressure());
      break;
    case 'S':
      //clean and refresh OLEDs
      break;
    }
  }
  //else if(waiting==false){
  //  LowPower.powerDown(SLEEP_8S, ADC_OFF, BOD_OFF);
  //}
}
