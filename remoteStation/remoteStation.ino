//Weatcher station by Filip Kryczek: remote station
//--------------------------------------------------
//used library:
//DHT-sensor-library: github.com/adafruit/DHT-sensor-library
//Adafruit_Sensor: github.com/adafruit/Adafruit_Sensor
//RF24: github.com/nRF24/RF24
//Low-Power: github.com/rocketscream/Low-Power
//--------------------------------------------------
#include <DHT.h>
#include <SPI.h>
#include <nRF24L01.h>
#include <RF24.h>
#include <LowPower.h>

#define DHTPIN 8
#define CEPIN 9
#define CSPIN 10
#define BATTERY 6

const uint64_t pipeAddressRX = 0xFF00202100FF;
const uint64_t pipeAddressTX = 0xFFFF2021FFFF;

char command;
bool connected = false;

DHT dht(DHTPIN, DHT22);
RF24 remoteRadio(CEPIN, CSPIN);

void setup()
{
  dht.begin();
  remoteRadio.begin();
  remoteRadio.setAutoAck(false);         //disable auto-acknowlede packets
  remoteRadio.setChannel(100);           //set communication channel
  remoteRadio.setPALevel(RF24_PA_LOW);   //set Power Amplifier (PA) level: -12dBm
  remoteRadio.setDataRate(RF24_250KBPS); //set the transmission data rate
  remoteRadio.setRetries(4, 5);          //delay and number of retries upon failed submit: 1000us
  remoteRadio.openReadingPipe(1, pipeAddressRX);
  remoteRadio.startListening();
}

void (*resetFunction)(void) = 0;

void sendData(float data)
{
  remoteRadio.stopListening();
  remoteRadio.openWritingPipe(pipeAddressTX);
  remoteRadio.write(&data, sizeof(data));
  remoteRadio.startListening();
}

void loop()
{
  if (remoteRadio.available())
  {
    remoteRadio.read(&command, sizeof(command));
    switch (command)
    {
    case 'C':
      sendData(7918); //ON
      connected = true;
      break;
    case 'D':
      sendData(797070); //OFF
      connected = false;
      break;
    case 'T':
      sendData(84697780); //TEMP.
      sendData(dht.readTemperature());
      break;
    case 'H':
      sendData(728577); //HUM
      sendData(dht.readHumidity());
      break;
    case 'B':
      sendData(666584); //BAT
      sendData(analogRead(BATTERY) * (5.0 / 1024.0));
      break;
    case 'R':
      sendData(828384); //RST
      delay(3000);
      resetFunction();
      break;
    }
  }
  else if (connected == false)
    LowPower.powerDown(SLEEP_8S, ADC_OFF, BOD_OFF);
}
