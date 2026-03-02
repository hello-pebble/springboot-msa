#!/bin/bash

BASE_URL="http://localhost:8080"

echo "=== 1. Sign Up (testuser) ==="
curl -s -X POST "$BASE_URL/api/v1/users/signup" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }' ; echo

echo ""
echo "=== 2. Sign Up (testuser2) ==="
curl -s -X POST "$BASE_URL/api/v1/users/signup" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser2",
    "password": "password456"
  }' ; echo

echo ""
echo "=== 3. Sign Up Duplicate (testuser - should fail) ==="
curl -s -X POST "$BASE_URL/api/v1/users/signup" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }' ; echo

echo ""
echo "=== 4. Get All Users ==="
curl -s -X GET "$BASE_URL/api/v1/users" \
  -b cookies.txt ; echo
