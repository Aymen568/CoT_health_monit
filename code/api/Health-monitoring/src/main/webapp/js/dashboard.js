document.addEventListener('DOMContentLoaded', function () {
        const userId = localStorage.getItem('mail');
        // Check if the user is signed in
        const userToken = localStorage.getItem('accesstoken'); // Adjust as per your authentication mechanism
        if (!userToken) {
            // Redirect to the sign-in page if not signed in
            window.location.href = 'welcome.html';
        }
    const websocketHost = "localhost";
    const websocketPort = "8080";
    const websocketUrl = `ws://${websocketHost}:${websocketPort}/websocket`;
    const apiUrl = "https://127.0.0.1:5000/predict";
    const mqtturl ='http://192.168.1.104:1880/start'
    let normalVal =0;
    let ubnormalVal = 0;
    let dataList = [];
    let percentageChart;
    let Username = "";
    let Email = "";
    let Phone= "";
    let Emergency = "";
    let subject = " Mesure Results";
    let from = "health.monitoring.911service@gmail.com";
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
    var ctx = document.getElementById("percentageChart").getContext("2d");
    var percentageData = {
        labels: ["Normal", "Abnormal"],
        datasets: [{
            data: [100, 0],
            backgroundColor: ["green", "red"],
        }],
    };

    var ecgStat = new Chart(ctx, {
        type: "doughnut",
        data: percentageData,
        options: {
            cutoutPercentage: 75, // Adjust as needed for a circular chart
            responsive: true,
            maintainAspectRatio: false,
        },
    });
    async function fetchUserInfo() {
        // Get user email from local storage (assuming it's stored there after sign-in)
        const userEmail = localStorage.getItem('mail');

        if (!userEmail) {
            console.error('User email not found in local storage');
            return;
        }

        // Fetch user information from the API endpoint
        fetch(`https://labidiaymen.me/api/${userEmail}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${userToken}`, // Send the user token for authentication
                'Content-Type': 'application/json',
            },
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch user information');
                }
                return response.json();
            })
            .then(userInformation => {
                // Display the fetched user information
                const userInfoContainer = document.getElementById('userInfo');

                if (userInfoContainer) {
                    Username = userInformation.fullname;
                    Email = userInformation.email;
                    Phone = userInformation.mobile;
                    Emergency = userInformation.emergency;

                }
            })
            .catch(error => {
                console.error('Error fetching user information:', error.message);
            });
    }

    async function DisplayUserInfo() {
        const userInfoHTML = `
                        <p><strong>Username:</strong> ${Username}</p>
                        <p><strong>Email:</strong> ${Email}</p>
                        <p><strong>Phone:</strong> ${Phone}</p>
                        <p><strong>Emergency:</strong> ${Emergency}</p>
                    `;
        // Update the content of the userInfoContainer
        userInfoContainer.innerHTML = userInfoHTML;
    }

    async function fetchDataAndCreateChart(userId) {
        try {
            const response = await fetch(`https://labidiaymen.me/api/user/getvalues/${userId}`, { method: 'GET' });

            if (!response.ok) {
                throw new Error(`Failed to fetch data. Status: ${response.status}`);
            }

            const data = await response.json();
            const normalValues = data['Normal'] !== undefined ? data['Normal'] : 100;
            const abnormalValues = data['Abnormal'];

            console.log(`Normal Values: ${normalValues}`);
            console.log(`Abnormal Values: ${abnormalValues}`);

            // Calculate percentages and update the chart
            const totalValues = normalValues + abnormalValues;
            const normalPercentage = (normalValues / totalValues) * 100;
            const abnormalPercentage = (abnormalValues / totalValues) * 100;
            ecgStat.data.datasets[0].data[0] = normalPercentage;
            ecgStat.data.datasets[0].data[1] = abnormalPercentage;
            ecgStat.update();

            console.log("New percentages are:", ecgStat.data.datasets[0].data);

        } catch (error) {
            console.error("Error fetching data:", error);
        }
    }
    // Function to start measurement for a sensor
    async function startMeasurement() {

                isMeasurementStarted = true;
                ecgChart.data.labels = [];
                ecgChart.data.datasets[0].data = [];
                ecgChart.update();

                // Trigger the exec node in Node-RED
                const mqtturl = 'http://192.168.1.104:1880/start';  // Replace with the actual URL

                await fetch(mqtturl, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        Accept: 'application/json',
                    },

                });

    }
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

    // Function to update the data list and UI
    function updateDataList(value) {
        dataList.push(value);
    }
    function updateChartBasedOnPrediction(result) {
        if (result) {
            ubnormalVal++;
        } else {
            normalVal++;
        }

        updatePercentage(normalVal, ubnormalVal);
        $.ajax({
            url: `https://labidiaymen.me/api/user/setvalues/${userId}`,
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify({ normal: result }),
            success: function (response) {
                console.log('User data updated successfully:', response);
                // Optionally, you can perform additional actions after a successful update
            },
            error: function (xhr, status, error) {
                console.error('Error updating user data:', error);

            }
        });
    }
    // Function to create the circle chart

    function updatePercentage(normalValues, abnormalValues) {
        const totalValues = normalValues + abnormalValues;
        const normalPercentage = (normalValues / totalValues) * 100;
        const abnormalPercentage = (abnormalValues / totalValues) * 100;
        ecgStat.data.datasets[0].data = [normalPercentage, abnormalPercentage];
        ecgStat.update();

        var percentageLabel = document.getElementById("percentageLabel");
        var percentageText = `${normalPercentage}% Normal, ${abnormalPercentage}% Abnormal`;
        percentageLabel.textContent = percentageText;
    }

    async function sendEmail(from, to, subject, content) {
        const emailData = {
            from: from,
            to: to,
            subject: subject,
            content: content,
        };

        try {
            const emailResponse = await fetch("https://labidiaymen.me/api/email/send", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Accept: "application/json",
                },
                body: JSON.stringify(emailData),
            });

            if (emailResponse.ok) {
                const emailResult = await emailResponse.text();
                console.log(emailResult);
            } else {
                console.error(`Failed to send email. Status: ${emailResponse.status}`);
            }
        } catch (error) {
            console.error("Error:", error.message);
        }
    }
    function logout() {
        // Clear user credentials (adjust as per your actual credential storage)
        localStorage.removeItem('accesstoken');
        localStorage.removeItem('codeverif');
        localStorage.removeItem('mail');
        localStorage.removeItem('refreshtoken');
        localStorage.removeItem('signInId');
        // You can clear additional credentials or session information here
        sessionStorage.clear();
        // Redirect to the sign-in page
        window.location.href = 'signin.html';
    }
    fetchUserInfo();
    fetchDataAndCreateChart(1)
    document.getElementById("startNewMeasure").addEventListener("click", function () {

        // Call the startMeasurement function with the
        startMeasurement();
        console.log("start measuring");
        ecgChart.data.labels=[]
        ecgChart.data.datasets[0].data=[]
        console.log("new percentages  are :", ecgStat.data.datasets[0].data);

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

    // Event listener for the "Predict" button
    document.getElementById("prediction").addEventListener("click", function () {
        dataList = Array(187).fill(0);
        if (dataList.length < 187) {
            alert("Not enough measure for prediction");
        } else {
            dataList = dataList.slice(dataList.length - 187);
            const dataJson = JSON.stringify({input_data: dataList});

            fetch(apiUrl, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Accept: "application/json",
                },
                body: dataJson,
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`HTTP error! Status: ${response.status}`);
                    }
                    return response.json();
                })
                .then(async predictionResult => {
                    console.log('Prediction result:', predictionResult);

                    if (predictionResult.hasOwnProperty('prediction')) {
                        const result = predictionResult.prediction[0][0] > 0.5 ? "NORMAL" : "ABNORMAL";
                        console.log('Prediction:', result);

                        const predictionResultElement = document.getElementById("Result");
                        predictionResultElement.innerText = `Prediction: ${result}`;
                        // Apply styles based on the result
                        if (result === "NORMAL") {
                            // Green for NORMAL
                            predictionResultElement.style.color = "green";
                        } else if (result === "Abnormal") {
                            // Red for Abnormal
                            predictionResultElement.style.color = "red";
                        }
                        // Center the element
                        predictionResultElement.style.textAlign = "center";
                        updateChartBasedOnPrediction(result);

                        let content = "The user " + Username + " having the phone number " + Phone;

                        if (result === "NORMAL") {
                            content += " has taken a safe measure today";
                        } else {
                            content += " has an abnormal measure and needs further treatment!";
                        }

                        sendEmail(from, Emergency, subject, content);

                        // Clear the data list after sending the prediction request
                        dataList.length = 0;
                    } else {
                        console.error('Prediction result does not have the expected structure.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        }
    });
    document.getElementById('logout').addEventListener("click", function () {
        logout();
        alert("You have logged out successfully");
    });

    // Attach the logout function to a logout button or link
    document.getElementById("userInfo").addEventListener("click", function () {
        DisplayUserInfo();
    });
    document.getElementById('mysensors').addEventListener('click', function () {
        window.location.href = 'home.html'; // Replace 'sensorpage.html' with the actual URL of your sensor page
    });
    // Flag to track whether the measurement has started
    let isMeasurementStarted = false;
});