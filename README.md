# Heart Monitoring CoT system

A brief description of what this project does and who it's for


## Overview

The project aims to develop a comprehensive system that combines cardiac and respiratory monitoring to enable early detection and prevention of heart rate disorders, with a specific focus on Atrial Fibrillation (AFib). Heart diseases, including AFib, stand as leading causes of global mortality. Recognizing the imperative for accurate detection and continuous monitoring, the project seeks to address the limitations present in current solutions.
## Functionnalities
The IoT system is crafted with the following functionalities:

- Facilitating user integration of sensors to manage their state.
- Capturing and analyzing the user's ECG signals.
- Processing data to predict abnormal or normal conditions.
- Alerting the user by promptly sending an email to their designated emergency contact.
## Demo
As for now, you can test the application directly from your browser at https://labidiaymen.me/api 
## Installation guide

We made sure that the architecture of the repository was well organized for users to test the project locally or build on it. If you want to run the application locally, please follow the following steps:

    - Clone the repo: git clone https://github.com/Aymen568/CoThealthmonit
    - Install Wildfly30
    - Install Nodered on your Raspberry pi and then load the content of flows.json into a new flow. Feel free to change the sensors and actuators pins, the MQTT broker, and the API link for getting a list of installed sensors.
    - Move into the api directory and run npm install to install the required dependencies. (Please ensure that you have node.js installed in your machine).
    - Open env.config.js and set your settings (certificate path, MQTT broker settings, and you Mongodb link).
    - Run npm start to start the server locally.
    - Move into the frontend directory - make sure that you have flutter installed on your machine - run flutter pub get Then select whether you want to run on your mobile, emulator, or web and run flutter pub run.

## Architecture
## Technologies
Multiple technologies, plugins, packages and hardware sensors were used while developing this project, the technologies are diverse and used for backend and frontend development.

    Middleware:
        - Jakarta EE 10.
        - MongoDB.

    Frontend
        - Google Maps API
        - Html/ CSS
    
    Backend:
        - Vanilla Javascript

    IoT:
        Node RED
        Communication protocols (MQTT)

    Server
        Mosquitto Broker
    Hardware
        Raspberry Pi 4
        Arduino
        Ecg-module-ad8232-heart-pulse-rate-sensor
        LCD

## Deployment Server

We used A virtual machines from Microsoft Azure Cloud for running the project. (Basically, Azure provides us with a 100$ to use them to try the different Azure products).

We also, have created a deploy.sh file and a cron job that runs every day at midnight to check for changes and update the API. The machine was also set up as an MQTT broker using Mosquitto. 
We took that approach to increase the security and ensure that everything ran smoothly without an interception. But, it is not necessary to use two separate machines; you can use one VM as an MQTT broker and middlewareMiddleware. The VMs are both e2-small machines (optimized for web hosting and serving (i.e., day to day usage)) and have the following characteristics:


    - Ram: 2 Gib
    - cores: 0.5
    - vCPUs: 2
    - Storage: 8 Gib
    - Zone: us-central1-a

## Security

The application's security has been bolstered through the incorporation of the following measures:

- Implementation of OAuth2 PKCE, utilizing JWT tokens to fortify the authentication and authorization flow.
- Activation of HTTPS/HSTS to ensure a secure connection between the client and the server.

HTTPS was ensured using Let's Encrypt's Certbot, providing secure communication with the middlewareMiddleware and the MQTT broker. DH (Diffie-Hellman parameters) parameters with 4096 bits are also used for TLS connections. In addition to that, some other security parameters were set on the Node.js server to ensure max security. The grading of the server was tested using SSLlabs, and we had a grade of A+. 
