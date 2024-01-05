// Function to add a new sensor
// Function to add a new sensor
async function addSensor(userId) {
    const sensorLocation = prompt("Enter sensor Location:");
    if (sensorLocation) {
        const sensorData = {
            location: sensorLocation,
        };

        try {
            const response = await fetch(`http://localhost:8080/api/sensor/${userId}/${sensorLocation}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(sensorData),
            });

            if (response.ok) {
                const sensor = await response.json();
                console.log('Sensor added:', sensor);
                alert('Sensor added successfully!');
            } else if (response.status === 404) {
                const errorData = await response.json();
                console.error('User not found:', errorData);
                alert('User not found. Please check the user ID.');
            } else {
                const errorData = await response.json();
                console.error('Error adding sensor:', errorData);
                alert('Error adding sensor. Please try again.');
            }
        } catch (error) {
            console.error('Error adding sensor:', error);
            alert('Error adding sensor. Please try again.');
        }
    }
}

async function listSensors(userId) {
    try {
        const response = await fetch(`http://localhost:8080/api/sensor/${userId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (response.ok) {
            const sensorList = await response.json();

            if (sensorList.length > 0) {
                // Clear existing table data
                const tableBody = document.getElementById('sensorTableBody');
                tableBody.innerHTML = '';

                // Populate the table with sensor data
                sensorList.forEach(sensor => {
                    const row = tableBody.insertRow();
                    const cell1 = row.insertCell(0);
                    const cell2 = row.insertCell(1);

                    cell1.textContent = sensor.id;
                    cell2.textContent = sensor.location;
                });

                // Show the table
                document.getElementById('sensorTableContainer').style.display = 'block';
            } else {
                alert('No sensors found for the user.');
            }
        } else if (response.status === 404) {
            const errorData = await response.json();
            console.error('User not found:', errorData);
            alert('User not found. Please check the user ID.');
        } else {
            const errorData = await response.json();
            console.error('Error fetching sensors:', errorData);
            alert('Error fetching sensors. Please try again.');
        }
    } catch (error) {
        console.error('Error fetching sensors:', error);
        alert('Error fetching sensors. Please try again.');
    }
}
// Function to start a new measure


// Example usage:
document.getElementById("addSensor").addEventListener("click", function () {
    const userId = prompt("Enter your user ID:");
    if (userId) {
        addSensor(userId);
    }
});

document.getElementById("listSensors").addEventListener("click", function () {
    const userId = prompt("Enter your user ID:");
    if (userId && userId.trim() !== '') {
        listSensors(userId);
    } else {
        alert('Please enter a valid user ID.');
    }
});

document.getElementById("dashboard").addEventListener("click", function () {
    window.location.href = '../dashboard.html';
});