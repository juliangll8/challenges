## Fetch all or matching a string
curl -H "Content-Type: application/json" -X GET "http://localhost:8080/api/superhero/all"
curl -H "Content-Type: application/json" -X GET "http://localhost:8080/api/superhero/all" --data 'MatchingName'

## Create, Delete, Undelete and Update Superhero
curl -H "Content-Type: application/json" -X POST "http://localhost:8080/api/superhero" --data '{ "name":"Messi", "superpower":"kindness", "strength":150000}'

curl -H "Content-Type: application/json" -X DELETE "http://localhost:8080/api/superhero/1"
curl -H "Content-Type: application/json" -X POST "http://localhost:8080/api/superhero/resurrect" --data 'Messi'

curl -H "Content-Type: application/json" -X PUT "http://localhost:8080/api/superhero" --data '{ "id":"1","name":"Messi", "superpower":"love", "strength":1500000}'

## Authentication needed:
curl -v -H "Content-Type: application/json" -X POST "http://localhost:8080/api/admin/initialSetup" --user w2m:difficultPass