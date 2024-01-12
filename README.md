
# <p align="center" style="font-size: 60px;"><strong>Heart Monitoring CoT system</strong> </p>

## Overview

The project aims to develop a comprehensive system that combines cardiac and respiratory monitoring to enable early detection and prevention of heart rate disorders, with a specific focus on Atrial Fibrillation (AFib). Heart diseases, including AFib, stand as leading causes of global mortality. Recognizing the imperative for accurate detection and continuous monitoring, the project seeks to address the limitations present in current solutions.

## Functionnalities
The IoT system is crafted with the following functionalities:

- Facilitating user integration of sensors to manage their state.
- Capturing and analyzing the user's ECG signals.
- Processing data to predict abnormal or normal conditions.
- Alerting the user by promptly sending an email to their designated emergency contact.
  
## Demo
As for now, you can test the application directly from your browser at https://labidiaymen.me/welcome.html
## Installation guide

We made sure that the architecture of the repository was well organized for users to test the project locally or build on it. If you want to run the application locally, please follow the following steps:

    - Clone the repo: git clone https://github.com/Aymen568/CoThealthmonit
    - Install Wildfly30, with Java 21.
    - Install Nodered on your Raspberry pi and then load the content of embedded folder into your raspberry pi. Feel free to change the sensors and actuators pins, the MQTT broker, and the API link for getting a list of installed sensors.
    - Configure  HTTPS/HSTS for Wildfly.
    - Open microprofile.config.properties and set your settings (certificate path, MQTT broker settings, and you Mongodb link).
    - Move into the Health-monitoring directory and run nvm clean package to install the required dependencies.
    - Deploy the war file created in the target forlder in the deployment folder, and start your wildfly server. 
    
## Architecture
<div align="center">
  <img src="/docs/media/archi(1).png" alt="architecture" >
</div>

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
        - Node RED
        - Communication protocols (MQTT)
        - Implementing UART protocol.

    Server
        - Mosquitto Broker
    Hardware
        - Raspberry Pi 4
        - Arduino
        - Ecg-module-ad8232-heart-pulse-rate-sensor
        - LCD


## Implementation

We adhered to a comprehensive guide that provided step-by-step instructions for the installation and configuration of the Wildfly server, with a specific focus on deployment within an Azure virtual machine. The guide encompassed critical tasks, including certificate provisioning and network configuration.

Concerning the sensor components, the Arduino code responsible for gathering sensor data can be found in the "embedding" folder. The "reader.py" file is dedicated to the implementation of the UART protocol for serial communication, while another file manages communication between the sensor and the broker through the Raspberry Pi.

On the client side, detailed information about both the front-end and back-end implementations can be located in the 'health monitoring/Web-App' folder. Within the Health Monitoring section, the middleware architecture is documented as the final component of the system.

## Deployment Server

We used A virtual machines from Microsoft Azure Cloud for running the project. (Basically, Azure provides us with a 100$ to use them to try the different Azure products).

We also, have created a deploy.sh file and a cron job that runs every day at midnight to check for changes and update the API. The machine was also set up as an MQTT broker using Mosquitto. 

    - Ram: 2 Gib
    - cores: 0.5
    - vCPUs: 2
    - Storage: 8 Gib
    - Zone: us-central1-a

## Security

The application's security has been bolstered through the incorporation of the following measures:

- Implementation of OAuth2 PKCE, utilizing JWT tokens to fortify the authentication and authorization flow.
- Activation of HTTPS/HSTS to ensure a secure connection between the client and the server.

HTTPS was ensured using Let's Encrypt's Certbot, providing secure communication with the middlewareMiddleware and the MQTT broker. DH (Diffie-Hellman parameters) parameters with 4096 bits are also used for TLS connections. In addition to that, some other security parameters were set on the Node.js server to ensure max security. The grading of the server was tested using SSLlabs, and we had a grade of A.
<div align="center">
  <img src="/docs/media/certificate.png" alt="architecture" height="500">
</div>

## Results 

Below, you will find the website architecture, as well as the materials hierarchy used for testing the system that we have established during this project.
<div align="center">
  <div>
    <img src="/docs/media/Screenshot 2024-01-12 142021.png" alt="froget password" height="300">
    <img src="/docs/media/front1.png" alt="architecture" height="300">
  </div>
  <div>
    <img src="/docs/media/front3.png" alt="signin" height="300">
    <img src="/docs/media/Screenshot 2024-01-12 141551.png" alt="signin" height="300">
  </div>
</div>


