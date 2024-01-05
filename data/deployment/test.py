import requests

# URL for the local API
api_url = 'http://127.0.0.1:5000/predict'

# Input data (adjust according to your model's input requirements)
input_data = [1.2]*187  # Your input vector of 187 values

# Prepare the JSON data
data = {'input_data': input_data}

# Send a POST request to the API
response = requests.post(api_url, json=data)

# Print the entire response
print('Response:', response.text)

# Check the response
if response.status_code == 200:
    try:
        result = response.json()['prediction'][0][0]
        if (result > 0.5): 
            result =1
        else:
            result = 0
        print('Prediction:', result)
    except ValueError as e:
        print('Error decoding JSON:', e)
else:
    print('Error:', response.text)