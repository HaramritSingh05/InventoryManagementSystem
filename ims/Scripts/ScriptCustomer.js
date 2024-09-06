const baseURL = SERVER_URL;

function extractCookie() {

  const cookieRow = document.cookie
    .split("; ")
    .find((row) => row.startsWith("cookie=="));
  return cookieRow ? cookieRow.split("==")[1] : "";
}
//global data

function getCustomerById(customerId) {
  const cookie = extractCookie();
  axios.get(`${baseURL}/api/getCustomerById/${customerId}`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${cookie}`,
    },
    withCredentials: true,
  })
  .then(response => {
      const customer = response.data;
      document.getElementById('updatedName').value = customer.customerName;
      document.getElementById('updatedAddress').value = customer.customerAddress;
      document.getElementById('updatedContactNo').value = customer.customerNo;
  })
  .catch(error => {
      console.error('Error fetching customer details:', error);
  });
}

document.addEventListener("DOMContentLoaded", function () {
  const cookie = extractCookie();

  fetch(`${baseURL}/api/getAllCustomers`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${cookie}`,
    },
    withCredentials: true,
  })
    .then((response) => response.json())
    .then((data) => {
      //insert  initial data
      //call the next function to fill the remaining data
        //func 2
          //
      const tbody = document.querySelector("#tbody");
      console.log(tbody);
      data.forEach((customer) => {
        const row = document.createElement("tr");
        row.innerHTML = `
                      <td>${customer.customerId}</td>
                      <td>${customer.customerName}</td>
                      <td>${customer.customerAddress}</td>
                      <td>${customer.customerNo}</td>
                      <td>
                        <a id="updateCustomerLink" title="Update Customer" class="btn" data-bs-toggle="modal" data-bs-target="#updateCustomerModal" data-customer-id="${customer.customerId}"><i class="fa-solid fa-xl fa-pen-to-square"></i></a>
                        <a title="Generate Order" class="btn" href="./AddDeliveryOrder.html?customerId=${customer.customerId}"><i class="fa-solid fa-xl fa-plus"></i></a>
                      </td>
                  `;
                  // href = "./updateForm.html?customerId= ${customer.customerId}"
        tbody.appendChild(row);
      });
      $("#myTable").DataTable();

      document.querySelectorAll('#updateCustomerLink').forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const customerId = this.getAttribute('data-customer-id');
            localStorage.setItem('customerId', customerId);
            getCustomerById(customerId);
        });
      });
      
    })
    .catch((error) => console.log( error));
});

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

// // initialize DataTable
// $("#myTable").DataTable();

