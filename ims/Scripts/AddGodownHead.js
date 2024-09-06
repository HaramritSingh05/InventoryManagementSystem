const baseURL = SERVER_URL;

// Handle navigation item clicks
function onNavItemClick(itemId, url) {
  if (url) {
    window.location.href = url;
  }
}

// Function to handle form submission and confirmation
function updateStatus() {
  if (confirm("Are you sure you want to save GodownHead details?")) {
    document
      .getElementById("addGodownHead")
      .addEventListener("click", function (e) {
        e.preventDefault(); // Prevent form submission

        // Fetch input values using e.target
        var formData = {
          godownId: e.target.elements.godownId.value,
          godownHeadName: e.target.elements.godownHeadName.value,
          username: e.target.elements.username.value,
          email: e.target.elements.email.value,
          password: e.target.elements.password.value,
          address: e.target.elements.address.value,
          godownHeadNo: e.target.elements.godownHeadNo.value,
        };
        console.log(formData);

        // Send data to backend API using Axios
        axios
          .post(`${baseURL}/api/register`, formData, {
            headers: {
              "Content-Type": "application/json",
            },
          })
          .then((response) => {
            console.log(response.data);
            // alert("GodownHead details saved successfully!");
            Notify("GodownHead details saved successfully!", "success");
            // You can redirect the user to another page here if needed
            // window.location.href = "Godown.html";
            setTimeout(() => {
              window.location.href = "Godown.html";
            }, 1000);

          })
          .catch((error) => {
            console.error("Error:", error);
            // alert("Username is already taken, Please use another username");
            Notify("Username is already taken, Please use another username", "danger");
          });
      });
  }
}
document.addEventListener("DOMContentLoaded", () => {
    document.getElementById("addGodownHead").addEventListener("submit", function(e) {
        e.preventDefault(); // Prevent form submission
    
        // Fetch input values using e.target
        var formData = {
            username: e.target.elements.username.value,
            password: e.target.elements.password.value,
            godownHeadName: e.target.elements.godownHeadName.value,
            godownId: e.target.elements.godownId.value,
            email: e.target.elements.email.value,
            address: e.target.elements.address.value, // Corrected line to capture address value
            godownHeadNo: e.target.elements.godownHeadNo.value
        };
        console.log(formData);
    
        // Send data to backend API using Axios
        axios.post(`${baseURL}/api/register`, formData, {
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            console.log(response.data);
            // alert('GodownHead details saved successfully!');
            Notify('GodownHead details saved successfully!', 'success');
            // You can redirect the user to another page here if needed
            // window.location.href = "Godown.html";
            setTimeout(() => {
                window.location.href = "Godown.html";
            }, 1000);
        })
        .catch(error => {
            console.error('Error:', error);
            // alert('Username is already taken, Please use another username');
            Notify('Username is already taken, Please use another username', 'danger');
        });
    });
});




// const toggleButton = document.getElementById('nav-toggle');
// const navLinks = document.getElementById('nav-links');

// toggleButton.addEventListener('click', () => {
//     navLinks.classList.toggle('active')
// })
