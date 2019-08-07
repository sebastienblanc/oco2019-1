#! /bin/bash

# curl -s -d "@json/black_listed_card.json" -H "Content-Type: application/json" -X POST http://localhost:8080/payments | sed 's/,/\n/g' | sed 's/^{/{\n/' | sed 's/}$/\n}\n/g'

curl -s -d "@json/black_listed_card.json" -H "Content-Type: application/json" -X POST http://localhost:8080//payments | python -m json.tool
