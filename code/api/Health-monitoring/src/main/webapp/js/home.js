document.addEventListener('DOMContentLoaded', function () {
    const userToken = localStorage.getItem('accesstoken');
    const userId = localStorage.getItem('mail');
    let locations = []; // Declare locations globally

    if (!userToken) {
        window.location.href = 'welcome.html';
    }

    var map = L.map('map').setView([0, 0], 2);
    var markers = [];
    var marker;

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors'
    }).addTo(map);

    map.on('click', function (e) {
        document.getElementById('latitude').value = e.latlng.lat.toFixed(6);
        document.getElementById('longitude').value = e.latlng.lng.toFixed(6);

        if (marker) {
            map.removeLayer(marker);
        }

        marker = L.marker(e.latlng).addTo(map);
    });
    function addMarker(latitude, longitude, title) {
        var marker = L.marker([latitude, longitude]).addTo(map).bindPopup(title);
        markers.push(marker);
    }
    async function addSensor(userId, sensorLocation) {
        try {
            const response = await fetch(`https://labidiaymen.me/api/sensor/${userId}/${sensorLocation}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                const sensor = await response.json();
                console.log('Sensor added:', sensor);
                const [lat, long] = sensor.location.split(':');
                addMarker(lat, long, 'Sensor Location');
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
    async function deleteSensor() {
        const location = document.getElementById('latitude').value + ':' + document.getElementById('longitude').value;
        console.info(location);

        try {
            const response = await fetch(`https://labidiaymen.me/api/sensor/delete/${userId}/${location}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                alert('Sensor deleted successfully!');
                // Remove the marker from the map if it exists
                removeMarker(location);
            } else if (response.status === 404) {
                const errorData = await response.json();
                console.error('Sensor not found:', errorData);
                alert('Sensor not found. Please check the location.');
            } else {
                const errorData = await response.json();
                console.error('Error deleting sensor:', errorData);
                alert('Error deleting sensor. Please try again.');
            }
        } catch (error) {
            console.error('Error deleting sensor:', error);
            alert('Error deleting sensor. Please try again.');
        }
    }
    function removeMarker(location) {
        const [lat, long] = location.split(':');
        const markerToRemove = markers.find(marker => marker.getLatLng().lat === parseFloat(lat) && marker.getLatLng().lng === parseFloat(long));

        if (markerToRemove) {
            map.removeLayer(markerToRemove);
            markers = markers.filter(marker => marker !== markerToRemove);
        }
    }


    async function fetchSensorLocationsAndAddMarkers(userId) {
        try {
            const response = await fetch(`https://labidiaymen.me/api/sensor/${userId}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                locations = await response.json();
                console.log(locations);
                locations.forEach(location => {
                    const [lat, long] = location.split(':');
                    addMarker(parseFloat(lat), parseFloat(long), 'Sensor Location');
                });
            } else {
                console.error('Error fetching sensor locations:', response.statusText);
            }
        } catch (error) {
            console.error('Error fetching sensor locations:', error);
        }
    }

    function addSensorOnMapSelection() {
        const location = document.getElementById('latitude').value + ':' + document.getElementById('longitude').value;
        console.info(location);
        addSensor(userId, location);
    }

    async function fetchAndDisplayUserInfo() {
        const userEmail = localStorage.getItem('mail');

        if (!userEmail) {
            console.error('User email not found in local storage');
            return;
        }

        try {
            const response = await fetch(`https://labidiaymen.me/api/sensor/${userEmail}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${userToken}`,
                    'Content-Type': 'application/json',
                },
            });

            if (!response.ok) {
                throw new Error('Failed to fetch user information');
            }

            const userInformation = await response.json();

            const userInfoContainer = document.getElementById('userInfo');

            if (userInfoContainer) {
                const userInfoHTML = `
                    <p><strong>Username:</strong> ${userInformation.fullname}</p>
                    <p><strong>Email:</strong> ${userInformation.email}</p>
                    <p><strong>Phone:</strong> ${userInformation.mobile}</p>
                    <p><strong>Emergency:</strong> ${userInformation.emergency}</p>
                `;

                userInfoContainer.innerHTML = userInfoHTML;
            }
        } catch (error) {
            console.error('Error fetching user information:', error.message);
        }
    }

    function logout() {
        localStorage.removeItem('accesstoken');
        localStorage.removeItem('codeverif');
        localStorage.removeItem('mail');
        localStorage.removeItem('refreshtoken');
        localStorage.removeItem('signInId');
        sessionStorage.clear();
        window.location.href = 'signin.html';
    }

    function initializeMap() {

        // Fetch and display sensor locations immediately after initializing the map
        fetchSensorLocationsAndAddMarkers(userId);
    }



    initializeMap();

    document.getElementById('addSensorButton').addEventListener('click', addSensorOnMapSelection);
    document.getElementById('deleteSensorButton').addEventListener('click', deleteSensor);
    document.getElementById("logout").addEventListener("click", function () {
        alert("You have logged out successfully");
        window.location.href = '../signin.html';
    });

    document.getElementById("userInfo").addEventListener("click", function () {
        fetchAndDisplayUserInfo();
    });

    document.getElementById("dashboard").addEventListener("click", function () {
        window.location.href = '../dashboard.html';
    });
});