from flask import Flask, request, jsonify
from flask_restful import Resource, Api
from joblib import load
import numpy as np
import tensorflow as tf
from tensorflow.keras.models import load_model

app = Flask(__name__)
api = Api(app)

# Load the AutoEncoder model
model = load_model('autoencoder_model.joblib')

class PredictionResource(Resource):
    def post(self):
        try:
            # Get data from the request
            data = request.get_json(force=True)
            input_data = data.get('input_data')

            # Perform inference using the loaded model
            input_data_np = np.array([input_data])
            reconstructed_data = model.predict(input_data_np)

            # Convert the result to a list
            result = reconstructed_data.flatten().tolist()

            return jsonify({'result': result})

        except Exception as e:
            return {'error': str(e)}

api.add_resource(PredictionResource, '/predict')

if __name__ == '__main__':
    app.run(port=5000, debug=True)