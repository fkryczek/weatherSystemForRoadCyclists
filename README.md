# Design and implementation of a weather systemfor road cyclists

## WEATHER STATION
- Weather station has two parts- main and remote. When you connected wires according to the scheme you must programme Arduino Nano boards.  
### Main station 
- [X] wireless communication with remote station
- [X] air temperature and atmospheric pressure measurement
- [X] battery voltage measurement
- [ ] remote reset
- [ ] display data on OLED displays
- [ ] wire communication with desktop application
- [ ] possibility to charging battery by micro USB
#### Used libraries
- [DHT-sensor-library](github.com/adafruit/DHT-sensor-library)
- [Adafruit_Sensor](github.com/adafruit/Adafruit_Sensor)
- [RF24](github.com/nRF24/RF24)
- [Low-Power](github.com/rocketscream/Low-Power)
  
### Remote station
- [X] wireless communication with main station
- [X] air tepmerature and humidity measurement
- [X] battery voltage measurement
- [X] possibility to charging battery by micro USB
- [X] remote reset
#### Used libraries
- [RF24](github.com/nRF24/RF24)
- [Low-Power](github.com/rocketscream/Low-Power)
- [Adafruit BMP280](github.com/adafruit/Adafruit_BMP280_Library) => in Adafruit_BMP280.h you must edit #define BMP280_ADDRESS (0x77) to #define BMP280_ADDRESS (0x76)
- [Adafruit SSD1306](https://)github.com/adafruit/Adafruit_SSD1306)
- [Adafruit GFX](github.com/adafruit/Adafruit-GFX-Library)
- [Adafruit BusIO](github.com/adafruit/Adafruit_BusIO)
