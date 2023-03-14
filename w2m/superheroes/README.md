# Superheroes

## Build
To build the application run:
```
mvn clean verify
```

## Run locally

#### Prerequisites

1. Application run with Java17.
2. Install Docker.

### Run it

From the root folder execute this:
```bash
docker compose up
```

Otherwise, you can build the docker image first and then run it:
```bash
docker build -t superheroes .
docker run -p 8080:8080 superheroes
```

To test it from your cmd you can use some functions executing this in the root folder:
   ```bash
   source curl -H "Content-Type: application/json" -X GET "http://localhost:8080/api/superhero/all"
   ## Check for more in bash_functions.sh  file.
   ```
