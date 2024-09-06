function saveGodownHead() {
    // Get form input values
    var godownId = document.querySelector('input[placeholder="Godown Id"]').value;
    var godownHeadName = document.querySelector('input[placeholder="GodownHead Name"]').value;
    var email = document.querySelector('input[placeholder="Email"]').value;
    var password = document.querySelector('input[placeholder="Password"]').value;
    var contactNo = document.querySelector('input[placeholder="Contact No."]').value;

    // Prepare data to send to the backend
    var data = {
        godownHeadId: godownId,
        godownHeadName: godownHeadName,
        email: email,
        password: password,
        contactNo: contactNo
    };

    // Send data to backend API
    fetch(`${basURL}/api/setGodownHead`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (response.ok) {
            // alert('GodownHead details saved successfully!');
            Notify('GodownHead details saved successfully!', 'success');
            // You can redirect the user to another page here if needed
        } else {
            throw new Error('Failed to save GodownHead details');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        // alert('An error occurred while saving GodownHead details');
        Notify('An error occurred while saving GodownHead details', 'danger');
    });
}

// Function to handle form submission and confirmation
function updateStatus(event) {
    if (confirm('Are you sure you want to save GodownHead details?')) {
        saveGodownHead();
    }
    // Prevent the default form submission
    event.preventDefault();
}


function onNavItemClick(itemId, url) {

    // Your existing code...

    if (url) {
        window.location.href = url;
    }
}




// const toggleButton = document.getElementById('nav-toggle');
// const navLinks = document.getElementById('nav-links');

// toggleButton.addEventListener('click', () => {
//   console.log("hello")
//     navLinks.classList.toggle('active')
// })