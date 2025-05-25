#!/bin/bash

# Extract the API key properly from the .env file
API_KEY=$(cat .env | grep OPENAI_API_KEY | tr -d '\n\r' | sed 's/OPENAI_API_KEY=//' | sed 's/\\//g')
echo "API key length: ${#API_KEY}"

# Two image URLs to compare
REFERENCE_URL="https://korean.visitseoul.net/data/kukudocs/seoul2133/20220518/202205181617250471.jpg"
TARGET_URL="https://i.namu.wiki/i/DEvKxYg-TEz6O53jeZyS9kndJSgSQnFysm3T-R70yXIyWi9-HknJZXoK1ghHFMwB365TyyMj7MlIebAKMrLSFA.webp"

# Create a temporary directory for curl output
mkdir -p tmp

# Download the images
echo "Downloading reference image..."
curl -s "$REFERENCE_URL" -o tmp/reference.jpg
echo "Downloading target image..."
curl -s "$TARGET_URL" -o tmp/target.webp

# Check if downloads succeeded
if [ ! -f tmp/reference.jpg ] || [ ! -f tmp/target.webp ]; then
    echo "Error downloading one or both images."
    exit 1
fi

# Convert images to base64 (macOS compatible)
echo "Converting images to base64..."
base64 -i tmp/reference.jpg > tmp/reference_base64.txt
base64 -i tmp/target.webp > tmp/target_base64.txt

# Create JSON payload files for the vision API requests
echo "Creating API request payloads for GPT-4o-mini Vision..."

cat > tmp/reference_payload.json << EOF
{
    "model": "gpt-4o-mini",
    "messages": [
        {
            "role": "system",
            "content": "Analyze this image in detail and provide a description that captures key visual elements."
        },
        {
            "role": "user",
            "content": [
                {
                    "type": "image_url",
                    "image_url": {
                        "url": "data:image/jpeg;base64,$(cat tmp/reference_base64.txt)"
                    }
                }
            ]
        }
    ],
    "max_tokens": 300
}
EOF

cat > tmp/target_payload.json << EOF
{
    "model": "gpt-4o-mini",
    "messages": [
        {
            "role": "system",
            "content": "Analyze this image in detail and provide a description that captures key visual elements."
        },
        {
            "role": "user",
            "content": [
                {
                    "type": "image_url",
                    "image_url": {
                        "url": "data:image/webp;base64,$(cat tmp/target_base64.txt)"
                    }
                }
            ]
        }
    ],
    "max_tokens": 300
}
EOF

# Get image descriptions and compare them
echo "Getting description for reference image..."
REFERENCE_RESPONSE=$(curl -s https://api.openai.com/v1/chat/completions \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $API_KEY" \
    -d @tmp/reference_payload.json)

# Save the response
echo "$REFERENCE_RESPONSE" > tmp/reference_response.json

# Check for errors
if echo "$REFERENCE_RESPONSE" | grep -q "error"; then
    echo "Error in reference image response:"
    cat tmp/reference_response.json
    exit 1
fi

echo "Getting description for target image..."
TARGET_RESPONSE=$(curl -s https://api.openai.com/v1/chat/completions \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $API_KEY" \
    -d @tmp/target_payload.json)

# Save the response
echo "$TARGET_RESPONSE" > tmp/target_response.json

# Check for errors
if echo "$TARGET_RESPONSE" | grep -q "error"; then
    echo "Error in target image response:"
    cat tmp/target_response.json
    exit 1
fi

# Extract the descriptions using jq if available, otherwise fallback to Python
if command -v jq &> /dev/null; then
    REFERENCE_DESCRIPTION=$(cat tmp/reference_response.json | jq -r '.choices[0].message.content')
    TARGET_DESCRIPTION=$(cat tmp/target_response.json | jq -r '.choices[0].message.content')
elif command -v python3 &> /dev/null; then
    REFERENCE_DESCRIPTION=$(python3 -c "import json, sys; print(json.load(open('tmp/reference_response.json'))['choices'][0]['message']['content'])")
    TARGET_DESCRIPTION=$(python3 -c "import json, sys; print(json.load(open('tmp/target_response.json'))['choices'][0]['message']['content'])")
else
    echo "Error: Neither jq nor python3 is available for JSON parsing. Please install one of them."
    exit 1
fi

echo "------------------------------"
echo "Reference Image Description:"
echo "------------------------------"
echo "$REFERENCE_DESCRIPTION"
echo ""
echo "------------------------------"
echo "Target Image Description:"
echo "------------------------------"
echo "$TARGET_DESCRIPTION"
echo ""

# Compare the images by asking GPT to assess similarity
echo "Comparing image descriptions for similarity..."
cat > tmp/comparison_payload.json << EOF
{
    "model": "gpt-4o-mini",
    "messages": [
        {
            "role": "system",
            "content": "You are an image similarity assessment expert. Calculate a similarity score between 0 and 1 based on the descriptions of two images."
        },
        {
            "role": "user",
            "content": "I have two image descriptions. Please analyze them and give me a similarity score between 0 and 1, where 1 means they are identical and 0 means completely different. Only respond with a number between 0 and 1, with up to 4 decimal places.\n\nFirst image: $REFERENCE_DESCRIPTION\n\nSecond image: $TARGET_DESCRIPTION"
        }
    ],
    "max_tokens": 10
}
EOF

COMPARISON_RESPONSE=$(curl -s https://api.openai.com/v1/chat/completions \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $API_KEY" \
    -d @tmp/comparison_payload.json)

# Save the response
echo "$COMPARISON_RESPONSE" > tmp/comparison_response.json

# Check for errors
if echo "$COMPARISON_RESPONSE" | grep -q "error"; then
    echo "Error in comparison response:"
    cat tmp/comparison_response.json
    exit 1
fi

# Extract the similarity score using JSON parsing tools
if command -v jq &> /dev/null; then
    SIMILARITY_SCORE=$(cat tmp/comparison_response.json | jq -r '.choices[0].message.content')
elif command -v python3 &> /dev/null; then
    SIMILARITY_SCORE=$(python3 -c "import json, sys; print(json.load(open('tmp/comparison_response.json'))['choices'][0]['message']['content'])")
else
    # Fallback to simple grep method for the score (should be simple enough)
    SIMILARITY_SCORE=$(cat tmp/comparison_response.json | grep -o '"content":"[^"]*"' | sed 's/"content":"//;s/"$//')
fi

echo "------------------------------"
echo "Similarity Score: $SIMILARITY_SCORE (0-1)"
echo "------------------------------"
echo "All responses are saved in the tmp/ directory." 