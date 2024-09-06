const baseURL = SERVER_URL;

function extractCookie() {
  const cookieRow = document.cookie
    .split("; ")
    .find((row) => row.startsWith("cookie=="));
  return cookieRow ? cookieRow.split("==")[1] : "";
}

// Function to get create a godown
document.addEventListener("DOMContentLoaded", function () {
  // Add event listener once the DOM content is loaded
  const addGodownForm = document.getElementById("formCa");

  if (addGodownForm) {
    // Check if the form element exists
    addGodownForm.addEventListener("submit", function (event) {
      event.preventDefault(); // Prevent the default form submission
      addGodown(); // Call the function to add the customer
    });
  } else {
    console.error("Error");
  }
});

function addGodown(event) {
  // Get form values
  const address = document.getElementById("GA").value;
  const volume = document.getElementById("GV").value;

  const cookie = extractCookie();

  // Prepare data to send
  const formData = {
    address: address,
    volume: volume,
  };

  // Send data to backend
  fetch(`${baseURL}/api/createGodown`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${cookie}`,
    },
    withCredentials: true,
    body: JSON.stringify(formData),
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      return response.text(); // Read the response as text
    })
    .then((data) => {
      console.log("Response from server:", data);
      // alert(data); // Show the response message in an alert box
      Notify(data, "success");
    })
    .catch((error) => {
      console.error("There was a problem adding the Godown:", error);
      // Handle error, show error message, etc.
    });
}

/* When the user clicks on the button, 
toggle between hiding and showing the dropdown content */
function myFunction() {
  document.getElementById("myDropdown").classList.toggle("show");
}

// Close the dropdown if the user clicks outside of it
window.onclick = function (event) {
  if (!event.target.matches(".dropbtn")) {
    var dropdowns = document.getElementsByClassName("dropdown-content");
    var i;
    for (i = 0; i < dropdowns.length; i++) {
      var openDropdown = dropdowns[i];
      if (openDropdown.classList.contains("show")) {
        openDropdown.classList.remove("show");
      }
    }
  }
};

function onNavItemClick(itemId, url) {
  // Your existing code...

  if (url) {
    window.location.href = url;
  }
}

// const toggleButton = document.getElementById("nav-toggle");
// const navLinks = document.getElementById("nav-links");

// toggleButton.addEventListener("click", () => {
//   console.log("hello");
//   navLinks.classList.toggle("active");
// });