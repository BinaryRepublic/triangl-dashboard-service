[![CircleCI](https://circleci.com/gh/codeuniversity/triangl-dashboard-service.svg?style=svg&circle-token=435ab9e49a21cdb34197103a8f7ccd483efed217)](https://circleci.com/gh/codeuniversity/triangl-dashboard-service)

# Triangl-Dashboard-Service

**Url**: https://api.triangl.io/dashboard-service/

**Place in the Infrastructure**: https://github.com/codeuniversity/triangl-infrastructure
 
## Routes

- Get amount of visitor count per requested timeslice with POST /visitors/count
- Get dwell time time per requested area with POST /visitors/areas/duration
- Get average visitor count per time per day with POST /visitors/byTimeOfDay/average

## What does it do
This Service is an Endpoint for the [Dashboard](https://github.com/codeuniversity/triangl-dashboard) to get statistics of the Visitors. It is connected to the Google Serving SQL database.

## Buggy/ToDo
- The /visitors/byTimeOfDay/average route is computing the visitors per time of day per weekday. One thing to note here is that the
the whole service uses timestamps in the [ISO 8601](https://en.wikipedia.org/wiki/ISO_8601) format. Therefore, to count right the service would need to 
know the timestamp of the requesting dashboard because Monday in Los Angeles starts 14 hours earlier than Monday in Shanghai.

## Security Actions taken
-- ToDo --

## Tools used
-- No special Tools yet --

## Environment Variables
The following Environment variables are need for this service:

```GOOGLE_APPLICATION_CREDENTIALS={pathToGoogleKeyFile.json}```

```SPRING_DATASOURCE_PASSWORD={password}```

```SPRING_DATASOURCE_USER={user}```

## Run
- With Gradle

  ```GOOGLE_APPLICATION_CREDENTIALS=/path/to/google/key/file.json SPRING_DATASOURCE_PASSWORD={password} SPRING_DATASOURCE_USER={user} ./gradlew bootRun```
