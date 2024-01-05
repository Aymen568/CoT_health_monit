// Check if the browser supports service workers
if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('service-worker.js', { scope: '/' })
        .then((registration) => {
            console.log('Service Worker registered with scope:', registration.scope);
        })
        .catch((error) => {
            console.error('Service Worker registration failed:', error);
        });
}

// Dummy data for illustration purposes
const previousMeasuresData = [
    { id: 1, date: '2023-01-01', value: 75 },
    { id: 2, date: '2023-02-01', value: 82 },
    { id: 3, date: '2023-03-01', value: 70 },
    // Add more data as needed
];

document.addEventListener('DOMContentLoaded', function () {
    // Display previous measures as buttons on page load
    displayPreviousMeasuresButtons();

    // Event listeners for buttons
    const createNewButton = document.getElementById('createNewMeasure');
    const visualizeCurveButton = document.getElementById('visualizeCurve');
    const mlModelButton = document.getElementById('mlModel');

    if (createNewButton && visualizeCurveButton && mlModelButton) {
        createNewButton.addEventListener('click', createNewMeasure);
        visualizeCurveButton.addEventListener('click', visualizeCurve);
        mlModelButton.addEventListener('click', applyMLModel);
    } else {
        console.error("One or more buttons not found");
    }
});

function displayPreviousMeasuresButtons() {
    const measuresList = document.getElementById('measuresList');

    if (!measuresList) {
        console.error("Element with ID 'measuresList' not found");
        return;
    }

    // Clear existing list
    measuresList.innerHTML = '';

    // Add a button for each previous measure
    previousMeasuresData.forEach(measure => {
        const measureButton = createStaticButton(`ID: ${measure.id}, Date: ${measure.date}, Value: ${measure.value}`, () => {
            displayMeasureDetails(measure);
        });

        measuresList.appendChild(measureButton);

        // Add a line break after each button to display them vertically
        measuresList.appendChild(document.createElement('br'));
        measureButton.style.marginBottom = '10px';
    });
}


function createStaticButton(text, clickHandler) {
    const button = document.createElement('button');
    button.textContent = text;
    button.addEventListener('click', clickHandler);
    return button;
}

function createNewMeasure() {
    // Implement logic for creating a new BCG measure
    alert('Create New Measure functionality is not implemented in this example.');
}

function visualizeCurve() {
    // Implement logic for visualizing the curve
    alert('Visualize Curve functionality is not implemented in this example.');
}

function applyMLModel() {
    // Implement logic for applying the ML model
    alert('Apply ML Model functionality is not implemented in this example.');
}

function displayMeasureDetails(measure) {
    // Display the details of the selected measure
    const measureDetailsContainer = document.getElementById('measureDetails');

    if (measureDetailsContainer) {
        measureDetailsContainer.innerHTML = `
            <h2>Measure Details</h2>
            <p>ID: ${measure.id}</p>
            <p>Date: ${measure.date}</p>
            <p>Value: ${measure.value}</p>
        `;
    } else {
        console.error("Element with ID 'measureDetails' not found");
    }
}
