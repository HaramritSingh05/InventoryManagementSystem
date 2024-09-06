// Wait for the DOM content to load
const baseURL = SERVER_URL;

function extractCookie() {
  const cookieRow = document.cookie
    .split("; ")
    .find((row) => row.startsWith("cookie=="));
  return cookieRow ? cookieRow.split("==")[1] : "";
}

document.addEventListener("DOMContentLoaded", function () {
  // Find the form by its ID
  const form = document.getElementById("updateCustomer");

  // Add event listener to the form on submit
  form.addEventListener("submit", function (event) {
    // Prevent the default form submission behavior
    event.preventDefault();

    // Get the values from the form fields
    const name = document.getElementById("updatedName").value;
    console.log(`${name}`);
    const address = document.getElementById("updatedAddress").value;
    const contactNo = document.getElementById("updatedContactNo").value;
    // console.log(name, address, contactNo);

    // Extract customerId from URL query parameter
    // const urlParams = new URLSearchParams(window.location.search);
    // const customerId = urlParams.get("customerId");

    const customerId = localStorage.getItem("customerId");
    localStorage.removeItem("customerId");
    const cookie = extractCookie();

    // Make an AJAX request to the API endpoint
    axios
      .put(
        `${baseURL}/api/updateCustomerById/${customerId}`,
        {
          customerName: name,
          customerAddress: address,
          customerNo: contactNo,
        },
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${cookie}`,
          },
          withCredentials: true,
        }
      )
      .then(function (response) {
        // Handle success response
        console.log("Updated successfully:", response.data);
        window.location.href = "customer.html";
        // You can perform additional actions here, such as showing a success message or redirecting the user
      })
      .catch(function (error) {
        // Handle error response
        console.error("Error updating customer:", error);
        // You can display an error message to the user or perform other error handling actions
      });
  });
});

// const toggleButton = document.getElementById("nav-toggle");
// const navLinks = document.getElementById("nav-links");

// toggleButton.addEventListener("click", () => {
//   console.log("hello");
//   navLinks.classList.toggle("active");
// });
