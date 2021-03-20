# [IN PROGRESS]

# Design and implementation of a weather system for road cyclists

## WEATHER STATION
### Main station 
- [X] wireless communication with remote station
- [X] air temperature and atmospheric pressure measurement
- [X] battery voltage measurement
- [X] remote reset
- [X] display data on OLED displays
- [X] wire communication with desktop application
- [X] possibility to charging battery by micro USB
- [ ] low battery notification 
#### Used libraries
- [DHT-sensor-library](github.com/adafruit/DHT-sensor-library)
- [Adafruit_Sensor](github.com/adafruit/Adafruit_Sensor)
- [RF24](github.com/nRF24/RF24)
- [Low-Power](github.com/rocketscream/Low-Power)
  
### Remote station
- [X] wireless communication with main station
- [X] air temperature and humidity measurement
- [X] battery voltage measurement
- [X] possibility to charging battery by micro USB
- [X] remote reset
#### Used libraries
- [RF24](github.com/nRF24/RF24)
- [Low-Power](github.com/rocketscream/Low-Power)
- [Adafruit BMP280](github.com/adafruit/Adafruit_BMP280_Library) => in Adafruit_BMP280.h you must edit #define BMP280_ADDRESS (0x77) to #define BMP280_ADDRESS (0x76)
- [SSD1306Ascii](github.com/greiman/SSD1306Ascii)
