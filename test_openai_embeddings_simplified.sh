#!/bin/bash

# Extract the API key properly from the .env file
API_KEY=$(cat .env | grep OPENAI_API_KEY | tr -d '\n\r' | sed 's/OPENAI_API_KEY=//' | sed 's/\\//g')
echo "API key length: ${#API_KEY}"

# Test the API key with a simple models list API call
echo "Testing API key with models list API..."
RESPONSE=$(curl -s https://api.openai.com/v1/models \
    -H "Authorization: Bearer $API_KEY")

# Save the response
echo "$RESPONSE" > tmp/models_response.json

# Check if the response contains an error
if echo "$RESPONSE" | grep -q "error"; then
    echo "Error in API response:"
    cat tmp/models_response.json
    exit 1
else
    echo "API key is valid! Models list retrieved successfully."
    echo "Response saved to tmp/models_response.json"
fi