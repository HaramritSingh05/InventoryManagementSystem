const baseURL = SERVER_URL;

// Function to get create a Godown
document.addEventListener("DOMContentLoaded", function () {
  // Add event listener once the DOM content is loaded
  console.log(document.getElementById("formCa"));
  const addCustomerForm = document.getElementById("formCa");

  if (addCustomerForm) {
    // Check if the form element exists
    addCustomerForm.addEventListener("submit", function (event) {
      event.preventDefault(); // Prevent the default form submission
      addCustomer(); // Call the function to add the customer
    });
  } else {
    console.error("Element with ID 'addCustomer' not found.");
  }
});

function extractCookie() {
  const cookieRow = document.cookie.split('; ').find(row => row.startsWith('cookie=='));
  return cookieRow ? cookieRow.split('==')[1] : '';
}

function addCustomer(event) {
  // Get form values
  const name = document.getElementById("name").value;
  const address = document.getElementById("address").value; 
  const contactNo = document.getElementById("contactNo").value;

  // Prepare data to send
  const formData = {
    customerName: name,
    customerAddress: address,
    customerNo: contactNo,
  };

  const cookie = extractCookie();
  // Send data to backend
  fetch(`${baseURL}/api/createCustomer`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      'Authorization': `Bearer ${cookie}`
    },
    withCredentials: true,
    body: JSON.stringify(formData),
  })
    .then((response) => {
      //   console.log(typeof formData);
      //   console.log(formData);
      //   console.log(response);
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      return response.text(); // Read the response as text
    })
    .then((data) => {
      console.log("Response from server:", data);
      // Optionally, you can handle the response here, for example, show a message to the user
      // alert(data); // Show the response message in an alert box
      Notify(data, "success");
      setTimeout(() => {
        window.location.href = "customer.html";
      }, 1000);
    })
    .catch((error) => {
      console.log(error);
      console.error("There was a problem adding the customer:", error);
      // Handle error, show error message, etc.
    });
}
