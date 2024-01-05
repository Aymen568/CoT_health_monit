document.addEventListener('DOMContentLoaded', function () {
    const websocketHost = "localhost";
    const websocketPort = "8080";
    const websocketUrl = `ws://${websocketHost}:${websocketPort}/websocket`;
    const apiUrl = "http://127.0.0.1:5000/predict";
    let normalVal =0;
    let ubnormalVal = 0;
   let dataList = [];

    // Create a WebSocket connection
    console.log(websocketUrl);
    const socket = new WebSocket(websocketUrl);

    // Initialize ECG chart
    var ecgCtx = document.getElementById('ecgChart').getContext('2d');
    var ecgChart = new Chart(ecgCtx, {
        type: 'line',
        data: {
            labels: [],
            datasets: [{
                label: 'ECG Data',
                data:  [],
                borderColor: 'red',
                borderWidth: 1,
                fill: false,
            }],
        },
        options: {
            scales: {
                x: {
                    type: 'linear',
                    position: 'bottom',
                    scaleLabel: {
                        display: true,
                        labelString: 'Time (ms)',  // Add your X-axis label here
                    },
                },
                y: {
                    min: 0,
                    max: 100,
                    scaleLabel: {
                        display: true,
                        labelString: 'Value',  // Add your Y-axis label here
                    },
                },
            },
        },
    });

    async function fetchAndCreateChart(sensorId) {
        try {
            // Make API call to get normal and abnormal values
            const response = await fetch(`http://localhost:8080/api/sensor/setvalues/${sensorId}`);
            const data = await response.json();

            // Assuming the API response contains 'normalPercentage' and 'abnormalPercentage' fields
            const normalValues = data[`Sensor_${sensorId}Normal`];
            const abnormalValues = data[`Sensor_${sensorId}Abnormal`];
            normalVal = normalValues;
            ubnormalVal = abnormalValues;
            // Calculate percentages
            const totalValues = normalValues + abnormalValues;

            const normalPercentage = (normalValues / totalValues) * 100;
            const abnormalPercentage = (abnormalValues / totalValues) * 100;

            // Create percentage chart using Chart.js
            createPercentageChart(normalPercentage, abnormalPercentage);
        } catch (error) {
            console.error("Error fetching data:", error);
        }
    }

    function createPercentageChart(normalPercentage, abnormalPercentage) {
        var ctx = document.getElementById("percentageChart").getContext("2d");
        var percentageData = {
            labels: ["Normal", "Abnormal"],
            datasets: [{
                data: [normalPercentage, abnormalPercentage],
                backgroundColor: ["green", "red"],
            }],
        };

        new Chart(ctx, {
            type: "doughnut",
            data: percentageData,
            options: {
                cutoutPercentage: 75, // Adjust as needed for a circular chart
                responsive: true,
                maintainAspectRatio: false,
            },
        });
    }
    createPercentageChart(70, 30); // Replace with actual data
    // Function to update the data list and UI
    function updateDataList(value) {
        dataList.push(value);
    }


    // Function to start measurement for a sensor
    async function startMeasurement(sensorId) {
        try {
            const response = await fetch(`http://localhost:8080/api/sensor/start/${sensorId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Accept: "application/json",
                },
                body: JSON.stringify(data),
            });

            if (response.ok) {
                const result = await response.text();
                console.log(result);

                // Set the flag to indicate that the measurement has started
                isMeasurementStarted = true;
            } else if (response.status === 404) {
                console.error('Sensor not found');
            } else {
                console.error(`Failed to start measurement. Status: ${response.status}`);
            }
        } catch (error) {
            console.error('Error:', error.message);
        }
    }

    // Update the ECG chart
    // Update the ECG chart
    function updateChart(value) {
        console.log("Updating chart with value:", value);


        const timestampInMillis = Date.now();
        const minutes = Math.floor((timestampInMillis / (1000 * 60)) % 60);
        const seconds = Math.floor((timestampInMillis / 1000) % 60);
        const milliseconds = timestampInMillis % 1000;
        const time = milliseconds + 1000* seconds;
        console.log(`Minutes: ${minutes}, Seconds: ${seconds}, Milliseconds: ${milliseconds}`);
        ecgChart.data.labels.push(time);
        ecgChart.data.datasets[0].data.push(value);

        if (ecgChart.data.labels.length > 50) {
            ecgChart.data.labels.shift();
            ecgChart.data.datasets[0].data.shift();
        }
        ecgChart.update();
        console.log("new data is :", ecgChart.data.datasets[0].data);
    }

    document.getElementById("startNewMeasure").addEventListener("click", function () {
        // Replace 'your-sensor-id' with the actual sensor ID
        const sensorId = 1;
        console.log(sensorId);

        // Call the startMeasurement function with the sensor ID
        startMeasurement(sensorId);
        console.log("start measuring");
        ecgChart.data.labels=[]
        ecgChart.data.datasets[0].data=[]


        // Listen for messages from the WebSocket
        socket.onopen = function (event) {
            console.log("WebSocket connection opened");
        };

        socket.onmessage = function (event) {
            if (socket.readyState === WebSocket.OPEN) {
                const data = JSON.parse(event.data);
                updateChart(data.value);
            }
        };
    });

    // Add an event listener to the "End New Measure" button
    document.getElementById("endNewMeasure").addEventListener("click", function () {
        // Check if the measurement has started before allowing to end it
        if (isMeasurementStarted) {
            const endMessage = { command: "endMeasurement" };
            socket.send(JSON.stringify(endMessage));
            // Reset the flag to indicate that the measurement has ended
            isMeasurementStarted = false;
            console.log('Mesure has been successfully stopped');
        } else {
            console.log('Cannot end measurement. Measurement has not started.');
        }
    });
    // Function to create the circle chart

    function updateOldMeasures(normalPercentage, abnormalPercentage) {
        var percentageLabel = document.getElementById("percentageLabel");
        var percentageText = `${normalPercentage}% Normal, ${abnormalPercentage}% Abnormal`;
        percentageLabel.textContent = percentageText;
    }
    function updatePercentage(normalValues, abnormalValues) {
        const totalValues = normalValues + abnormalValues;
        const normalPercentage = (normalValues / totalValues) * 100;
        const abnormalPercentage = (abnormalValues / totalValues) * 100;

        // Call the function to create or update the chart
        createPercentageChart(normalPercentage, abnormalPercentage);
    }
    // Event listener for the "Predict" button
    document.getElementById("prediction").addEventListener("click", function () {
        dataList = Array(187).fill(0);
        const dataJson = JSON.stringify({ input_data: dataList });
        fetch(apiUrl, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Accept: "application/json",
            },
            body: dataJson,
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                return response.json();
            })
            .then((predictionResult) => {
                console.log('Prediction result:', predictionResult);

                // Handle the prediction result
                if (predictionResult.hasOwnProperty('prediction')) {
                    const result = predictionResult.prediction[0][0] > 0.5 ? "NORMAL" : "ABNORMAL";
                    console.log('Prediction:', result);
                    const predictionResultElement = document.getElementById("Result");
                    predictionResultElement.innerText = `Prediction: ${result}`;
                    // Update the chart based on normal and abnormal values
                    if(result) ubnormalVal++; else normalVal;
                    updatePercentage(normalVal, ubnormalVal);
                } else {
                    console.error('Prediction result does not have the expected structure.');
                }

                // Clear the data list after sending the prediction request
                dataList.length = 0;

            })
            .catch((error) => {
                console.error("Error:", error);
            });
    });

    // Flag to track whether the measurement has started
    let isMeasurementStarted = false;
});