# Requirements

Implement a RESTful web service that would handle GET requests to path “weather” by returningthe weather data
determined by IP of the request originator.Upon receiving a request, the service should perform a geolocation
search using a non commercial,3rd party IP to location provider.Having performed the reverse geo search service
should use another non commercial, 3rd partyservice to determine current weather conditions using the coordinates
of the IP
## Tehnology stack
* Java 11
* Spring Boot
* H2 In-Memory database
* Hibernate/JPA
* Gradle
* GIT

## API

Get weather:

```
  GET: /weather
```
Reponse
```
{
    "success": true,
    "status": "Actual Response from provider",
    "forecast": {
        "visibility": 10000,
        "timezone": 21600,
        "main": {
            "temp": 274.15,
            "pressure": 1020,
            "humidity": 100,
            "temp_min": 274.15,
            "temp_max": 274.15
        },
        "clouds": {
            "all": 90
        },
        "sys": {
            "type": 1,
            "id": 8820,
            "country": "KZ",
            "sunrise": 1573265983,
            "sunset": 1573299378
        },
        "dt": 1573296609,
        "coord": {
            "lon": 71.45,
            "lat": 51.17
        },
        "weather": [
            {
                "id": 804,
                "main": "Clouds",
                "description": "overcast clouds",
                "icon": "04d"
            }
        ],
        "name": "Astana",
        "cod": 200,
        "id": 1526273,
        "base": "stations",
        "wind": {
            "speed": 4,
            "deg": 70
        }
    }
}
```

