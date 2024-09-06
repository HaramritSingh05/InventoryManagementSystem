// let table = new DataTable('#myTable');

const baseURL = SERVER_URL;
 


function updateStatus(id) {
    var statusElement = document.getElementById('status' + id);

    var buttonElement = document.getElementById('button' + id);

    if (statusElement.textContent === 'In progress') {
        statusElement.textContent = 'Fulfilled';
        buttonElement.textContent = 'Order received';
    }
}





function extractCookie() {
    const cookieRow = document.cookie.split('; ').find(row => row.startsWith('cookie=='));
    return cookieRow ? cookieRow.split('==')[1] : '';
}

// var role = extractCookie('role');





function fetchPurchaseOrders() {
    const godownId = parseInt(JSON.parse(localStorage.getItem('user')).godownId);
    const cookie = extractCookie();
    var role = JSON.parse(localStorage.getItem('user')).role; // Get the role from the local storage
    fetch(`${baseURL}/api/getAllPurchaseOrders`, {
        headers:{
            "Content-Type": "application/json",
            'Authorization': `Bearer ${cookie}`
        }})
    .then(response => response.json())
    .then(data => {
        // Check the role
        if (role === 'admin') {
            // Proceed as before, no filtering needed
            // Fetch the supplier name for each purchase order
            var supplierPromises = data.map(function(order) {
                return fetch(`${baseURL}/api/getSupplierBySupplierId/` + order.supplierId, {
                    headers:{
                        "Content-Type": "application/json",
                        'Authorization': `Bearer ${cookie}`
                    }
                })
                .then(response => response.json())
                .then(supplierData => {
                    // Add the supplier name to the order
                    order.supplierName = supplierData.supplierName; // Replace 'name' with the actual property name for the supplier name
                    return order;
                });
            });

            // Wait for all supplier names to be fetched
            Promise.all(supplierPromises)
            .then(purchaseOrders => {
                localStorage.setItem("purchaseOrders",purchaseOrders.length);
                populatePurchaseTable(purchaseOrders);

            });
        } else if (role === 'godownhead') {
            // Filter the purchase orders by the godown id
            var filteredData = data.filter(function(order) {
                // console.log("hello");
                return order.godownId === godownId;
            });

            // Fetch the supplier name for each purchase order
            var supplierPromises = filteredData.map(function(order) {
                return fetch(`${baseURL}/api/getSupplierBySupplierId/` + order.supplierId, {
                    headers:{
                        "Content-Type": "application/json",
                        'Authorization': `Bearer ${cookie}`
                    }
                })
                .then(response => response.json())
                .then(supplierData => {
                    // Add the supplier name to the order
                    order.supplierName = supplierData.supplierName; // Replace 'name' with the actual property name for the supplier name
                    return order;
                });
            });

            // Wait for all supplier names to be fetched
            Promise.all(supplierPromises)
            .then(purchaseOrders => {
                localStorage.setItem("purchaseOrders",purchaseOrders.length);
                populatePurchaseTable(purchaseOrders);
            });
        } else {
            // Invalid role, handle accordingly
            console.error('Invalid role:', role);
        }

        
    })
    .catch((error) => {
        console.error('Error:', error);
    });
}


// Function to populate the purchase table with the fetched data
function populatePurchaseTable(purchaseOrders) {
    var tableBody = document.querySelector('#tbody');
    // Clear out existing rows
    // tbody.innerHTML = '';

    // Loop over the purchase orders and create a table row for each one
    purchaseOrders.forEach(function(order) {
        var row = document.createElement('tr');

        // Create a table cell for each property of the order
        var idCell = document.createElement('td');
        idCell.textContent = order.purchaseId;
        row.appendChild(idCell);

        var dateCell = document.createElement('td');
        var date_object = new Date(order.purchaseDate);
        var day = String(date_object.getDate()).padStart(2, '0');
        var month = String(date_object.getMonth() + 1).padStart(2, '0'); //January is 0!
        var year = date_object.getFullYear();
        var formatted_date = day + '/' + month + '/' + year;
        dateCell.textContent = formatted_date;
        row.appendChild(dateCell);
        

        var priceCell = document.createElement('td');
        priceCell.textContent = order.totalCostPrice;
        row.appendChild(priceCell);

        var quantityCell = document.createElement('td');
        quantityCell.textContent = order.purchaseQuantity;
        row.appendChild(quantityCell);

        var supplierCell = document.createElement('td');
        supplierCell.textContent = order.supplierName; // Use the supplier name from the order data
        row.appendChild(supplierCell);

        var actionCell = document.createElement('td');
        var button = document.createElement('button');
        button.className='btn'
        button.title = "Order Details"
        var icon = document.createElement('i');
        icon.className = 'fa-regular fa-xl fa-file-lines';
        
        button.appendChild(icon);
        // button.onclick = function() {
        //   location.href = `purchaseDetails.html?id=${order.purchaseId}`;
        // };

        button.setAttribute('data-bs-toggle', 'modal');
        button.setAttribute('data-bs-target', '#purchaseDetailsModal');
        button.onclick = function() {
            fetchPurchaseDetails(order.purchaseId);
        };
        actionCell.appendChild(button);
        row.appendChild(actionCell);
        

        

        // Add the row to the table body
        tableBody.appendChild(row);
        
       
    });
      // initialize DataTable
      $('#myTable').DataTable();
}


function conditionalRendering() {
    const role = JSON.parse(localStorage.getItem('user')).role;
    console.log(role)
    // console.log(role);
    if (role === "godownhead") {
        document.getElementById('rolebutton').style.display = 'block';
        

    } else if (role === "admin") {
        document.getElementById('rolebutton').style.display = 'none';
        
       
    }
}

function fetchPurchaseDetails(purchaseId) {
    console.log(purchaseId)
    const cookie = extractCookie();
    fetch(`${baseURL}/api/getPurchaseOrderByPurchaseId/` + purchaseId, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            'Authorization': `Bearer ${cookie}`
        }
    })
        .then(response => response.json())
        .then(data => {
            // Update purchase details using the fetched data
            updatePurchaseDetails(data);
        })
        .catch(error => console.error('Error fetching purchase details:', error));
}

function updatePurchaseDetails(purchaseData) {
    const purchaseDate = new Date(purchaseData.purchaseDate);
    const day = purchaseDate.getDate();
    const month = purchaseDate.getMonth() + 1; // Months are zero-based, so we add 1
    const year = purchaseDate.getFullYear();
    const formattedDate = `${day < 10 ? '0' + day : day}-${month < 10 ? '0' + month : month}-${year.toString().slice(-2)}`;

    const hours24 = purchaseDate.getHours();
    let hours12 = hours24 % 12 || 12; // Convert 0 to 12 for 12-hour format
    const minutes = purchaseDate.getMinutes();
    const seconds = purchaseDate.getSeconds();
    const ampm = hours24 >= 12 ? 'PM' : 'AM';
    const formattedTime = `${hours12 < 10 ? '0' + hours12 : hours12}:${minutes < 10 ? '0' + minutes : minutes} ${ampm}`;

    // Update HTML elements with purchase details
    document.querySelector('.purchase-id').textContent = purchaseData.purchaseId;
    document.querySelector('.purchase-date').textContent = formattedDate;
    document.querySelector('.purchase-time').textContent = formattedTime;

    // Update product details
    const productsContainer = document.getElementById('products-container');
    productsContainer.innerHTML = ''; // Clear existing content

    purchaseData.products.forEach((product, index) => {
        const productDiv = document.createElement('div');
        productDiv.classList.add('product-details');

        productDiv.innerHTML = `
        <div class="accordion" id="accordionExample_${index}>
            <div class="accordion-item">
                <h2 class="accordion-header">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapse_${index}" aria-expanded="true" aria-controls="collapse_${index}">
                        ${product.productName}
                    </button>

                </h2>
                <div id="collapse_${index}" class="accordion-collapse collapse" data-bs-parent="#accordionExample_${index}">
                <div class="accordion-body">
                    <div class="row my-2">
                        <div class="col-6 text-right">
                            Product Name:
                        </div>
                        <div class="col-6">
                            <span class="text-120 text-secondary-d1">${product.productName}</span>
                        </div>
                    </div>
                    <div class="row my-2">
                        <div class="col-6 text-right">
                            Purchase Quantity:
                        </div>
                        <div class="col-6">
                            <span class="text-120 text-secondary-d1">${product.purchaseQuantity}</span>
                        </div>
                    </div>
                    <div class="row my-2">
                        <div class="col-6 text-right">
                            Cost Price:
                        </div>
                        <div class="col-6">
                            <span class="text-120 text-secondary-d1">${product.costPrice}</span>
                        </div>
                    </div>
                    <div class="row my-2">
                        <div class="col-6 text-right">
                            Product Volume:
                        </div>
                        <div class="col-6">
                            <span class="text-120 text-secondary-d1">${product.productVolume}</span>
                        </div>
                    </div>
                    <div class="row my-2">
                        <div class="col-6 text-right">
                            Product Category:
                        </div>
                        <div class="col-6">
                            <span class="text-120 text-secondary-d1">${product.productCategory}</span>
                        </div>
                    </div>
                    <div class="row my-2">
                        <div class="col-6 text-right">
                            Product Type:
                        </div>
                        <div class="col-6">
                            <span class="text-120 text-secondary-d1">${product.productType} kw</span>
                        </div>
                    </div>
                    
                </div>
                </div>
            </div>
        </div>
`;

        productsContainer.appendChild(productDiv);
    });
}



// const toggleButton = document.getElementById('nav-toggle');
// const navLinks = document.getElementById('nav-links');

// toggleButton.addEventListener('click', () => {
//     navLinks.classList.toggle('active')
// })

function checkCookie() {
    var cookies = document.cookie.split(";");
    var cookieExists = false;

    for (var i = 0; i < cookies.length; i++) {
      var cookie = cookies[i].trim();
      if (cookie.indexOf("cookie==") === 0) {
        cookieExists = true;
        break;
      }
    }

    if (cookieExists && localStorage.getItem("user") != null) {
      var expirationDate = new Date(
        document.cookie.replace(
          /(?:(?:^|.;\s)cookie\s*\==\s*([^;]).$)|^.*$/,
          "$1"
        )
      );
      if (expirationDate && expirationDate < new Date()) {
        console.log("Cookie is expired.");
        alert("Your session has expired. Please login again.");
        localStorage.clear();
        window.location.href = "login.html";
      } else {
        document.getElementById("purchasebody").style.display = "block";
      }
    } else {
      window.location.href = "login.html";
      alert("Please login first.");
    }
  }

  

window.onload = ()=>{
    checkCookie()
    fetchPurchaseOrders()
    
};


document.addEventListener("DOMContentLoaded", function() {
    //generate product table
    conditionalRendering();
   
  
    
    
});





 












