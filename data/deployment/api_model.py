from flask import Flask, request, jsonify
from flask_cors import CORS
import tensorflow as tf
import numpy as np

app = Flask(__name__)
CORS(app, resources={r"/predict": {"origins": ["https://labidiaymen.me", "http://localhost:8080"]}})
# Load the saved model
model = tf.keras.models.load_model('model.h5')

@app.route('/predict', methods=['POST', 'OPTIONS'])
def predict():
    try:
        # Get input data from the request
        data = request.get_json()
        input_data = np.array(data['input_data'])

        # Perform prediction using the loaded model
        prediction = model.predict(np.expand_dims(input_data, axis=0))

        # Return the prediction as JSON
        return jsonify({'prediction': prediction.tolist()})
    except Exception as e:
        return jsonify({'error': str(e)})

if __name__ == '__main__':
    app.run(debug=True, port=5000)