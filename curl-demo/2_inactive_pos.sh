#! /bin/bash

#curl -s -d "@json/inactive_pos.json" -H "Content-Type: application/json" -X POST http://localhost:8080/easypay/api/payments | sed 's/,/\n/g' | sed 's/^{/{\n/' | sed 's/}$/\n}\n/g'

curl -s -d "@json/inactive_pos.json" -H "Content-Type: application/json" -X POST http://localhost:8080/payments | python -m json.tool
