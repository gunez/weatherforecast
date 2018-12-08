# Weather Forecast

Finleap java case study

## Settings

### Compile & Run Project

All gradle commands should be executed under root folder of the project, which contains _build.gradle_ file.

**To clean project:** `gradle clean`

**To build project:** `gradle build`

**To run tests:** `gradle test`

**To run project:** `gradle bootRun`

The project will be served in `http://localhost:8080/` with default configurations.

**Note:** Supported gradle version is gradle-4.10.2. Project is developed with this version and it may not work 
properly with gradle version > 5.

### Project Properties

##### application.properties

Application properties file is placed under resources. It contains application name, profile, server configurations and 
Open Weather Map API urls.

Default server port is 8080.

##### logback.xml

Logback configuration file is placed under resources.

##### lombok.config

Lombok configuration file is placed under resources.

##### swagger.properties

Swagger property file is placed under resources.

## Usage

### API

The API exposes `/data` endpoint to retrieve estimated average temperature values for the next 3 days.

#### Weather Forecast Service

##### GET /data/{cityName}

###### Parameters

| Name | Description |
| --- | --- |
| cityName | Name of the city to retrieve average temperature estimations. |

###### Responses

| Code | Description |
| --- |--- |
| 200 | OK |
| 400 | The request will return BAD REQUEST in case of invalid input |
| 404 | The request will return NOT FOUND in cases below. |
| | * There is no city with given name. |
| | * The resource Open Weather Map API has internal problem and cannot return proper response. |
| 500 | Internal Server Error |

###### Response Parameters

| Name | Description |
| --- | --- |
| cityName | Name of the city, which is given in the request. |
| timeZone | Time zone of the temperature values, which calculations based. |
| days | List of average temperature values for the next 3 days. This list cannot be larger than 3. It should contain 3 elements regarding the next 3 days. |
| date | Date of average temperature values. |
| dailyAvgTemperature | Average temperature value for day times between 06:00-18:00 |
| nightlyAvgTemperature | Average temperature value for night times between 18:00-06:00 |

###### Successful response
```
{
    "cityName": "London",
    "timeZone": "UTC",
    "days": [
        {
            "date": "2018-12-07",
            "dailyAvgTemperature": 10,
            "nightlyAvgTemperature": 8
        },
        {
            "date": "2018-12-08",
            "dailyAvgTemperature": 9,
            "nightlyAvgTemperature": 10
        },
        {
            "date": "2018-12-09",
            "dailyAvgTemperature": 9,
            "nightlyAvgTemperature": 7
        }
    ]
}
```
## Architecture

### Open Weather Map API

Open Weather Map API is directly called as  with the parameters in order to retrieve estimations for the 5 days.

* **endPoint:** `http://api.openweathermap.org/data/2.5/forecast?q={city}&units=metric&appid={apiKey}`
* **apiKey:** Stored in application.properties file.
* **city:** Taken from the user.

### Calculations

Calculations are done in 3 steps.

1. Extract temperature data from whole weather data.
2. Map temperature data according to days.
3. Split mapped data as day or night according to their times and calculate averages.

First step is handled in OpenWeatherMapAPI client not to carry any entity, which is related with remote API, into the 
application. After first step raw temperature data returned from this client class.

Second step is handled in WeatherService. At this step, raw data is split into parts according to days. If time of a 
temperature data is before 06:00 it will be mapped to the previous day. So, a day in the mapped temperature data 
contains the data between 06:00 - 23:59 of current day and 00:00 - 05:59 of next day.   

Third step is handled in WeatherService. At this step, temperature values are split as day and night for each day and 
average values are calculated. Calculations are done for the fallowing 3 days which are after the day of query. Average 
values are returned as integer but all calculations are done with double.

**Example raw temperature data:**
```
{
    "name": "London",
    "zoneId": "UTC",
    "instant": "2018-12-06T21:56:52.927Z",
    "temperatures": [
        {
            "localDate": "2018-12-07",
            "localTime": "00:00",
            "temp": 12.55
        },
        {
            "localDate": "2018-12-07",
            "localTime": "06:00",
            "temp": 12.68
        },
        {
            "localDate": "2018-12-07",
            "localTime": "12:00",
            "temp": 12.19
        },
        {
            "localDate": "2018-12-08",
            "localTime": "00:00",
            "temp": 11.92
        },
        {
            "localDate": "2018-12-08",
            "localTime": "12:00",
            "temp": 8.35
        },
        {
            "localDate": "2018-12-09",
            "localTime": "00:00",
            "temp": 11.92
        },
        {
            "localDate": "2018-12-09",
            "localTime": "12:00",
            "temp": 15.92
        },
        {
            "localDate": "2018-12-09",
            "localTime": "21:00",
            "temp": 11.32
        }
    ]
}
```

**Example mapped temperature data:**
```
{
    "name": "London",
    "zoneId": "UTC",
    "instant": "2018-12-06T21:56:52.927Z",
    "temperatures": {
        "2018-12-06": [
            {
                "localDate": "2018-12-07",
                "localTime": "00:00",
                "temp": 12.55
            }
        },
        "2018-12-07": [
            {
                "localDate": "2018-12-07",
                "localTime": "06:00",
                "temp": 12.68
            },
            {
                "localDate": "2018-12-07",
                "localTime": "12:00",
                "temp": 12.19
            },
            {
                "localDate": "2018-12-08",
                "localTime": "00:00",
                "temp": 11.92
            }
        },
        "2018-12-08": [

            {
                "localDate": "2018-12-08",
                "localTime": "12:00",
                "temp": 8.35
            },
            {
                "localDate": "2018-12-09",
                "localTime": "00:00",
                "temp": 11.92
            }
        ],
        "2018-12-09": [
            {
                "localDate": "2018-12-09",
                "localTime": "12:00",
                "temp": 15.92
            },
            {
                "localDate": "2018-12-09",
                "localTime": "21:00",
                "temp": 11.32
            }
         ]
    }
}
```
**Example temperature summary data:**
```
{
    "cityName": "London",
    "timeZone": "UTC",
    "days": [
        {
            "date": "2018-12-07",
            "dailyAvgTemperature": 12,
            "nightlyAvgTemperature": 12
        },
        {
            "date": "2018-12-08",
            "dailyAvgTemperature": 8,
            "nightlyAvgTemperature": 12
        },
        {
            "date": "2018-12-09",
            "dailyAvgTemperature": 16,
            "nightlyAvgTemperature": 11
        }
    ]
}
```

### Caching

Caching is done via Spring cachable annotation. If a city is queried before in current day, the values are returned 
from cache. Cache is cleaned every day in 00:00.
 
Cache cleaning is done in CacheCleanerConfig by using scheduler.

### Validation 

Validations of an input city name is done by validation annotation. It is created under validator package. This 
CityValidator checks the name of the city if it other then letter or comma, it invalidates the input. In addition to 
this, city name cannot have more than 128 character. In case of having an input more than 128 characters, the input will 
be invalidated again.

## Notes

##### Separate Common Module
 
I had an intention to have separate common module to gather common configurations, exception handling objects, 
constants and similar requirements with enabler annotations. Than, it would be easy to enable these all in every 
micro-service by just putting an enabled annotation in their application launchers. I did not fallow that idea to avoid 
doing some over engineering without having any requirement for second micro-service.

##### Small Logic on API Client

In OpenWeatherMapAPI client the method **extractTemperatureData** does filtering and outputs just the raw temperature 
data to the service client. This logic is placed into the API client in order to hide entity objects of external 
service from internal logic.