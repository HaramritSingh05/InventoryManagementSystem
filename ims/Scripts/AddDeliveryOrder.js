const baseURL = SERVER_URL;

document.getElementById('productForm').addEventListener('submit', handleFormSubmit);


// const toggleButton = document.getElementById('nav-toggle');
// const navLinks = document.getElementById('nav-links');
var customerId = getCustomerIdFromUrl();

var orderIdInput = document.querySelector('input[placeholder="Order Id"]');

// Set the value of the input field to the fetched GodownId
if (orderIdInput) {
    orderIdInput.value = "Customer Id: " + customerId;
}

// toggleButton.addEventListener('click', () => {
//     navLinks.classList.toggle('active')
// })

var cart = []; // Array to store the cart items
const godownId = parseInt(JSON.parse(localStorage.getItem('user')).godownId);
const cookie = extractCookie();
var role = JSON.parse(localStorage.getItem('user')).role; // Get the role from the local storage
function fetchProducts() {
  
    fetch(`${baseURL}/api/getDistinctProduct`, { headers:{
        "Content-Type": "application/json",
        'Authorization': `Bearer ${cookie}`
 } })
        
    .then(response => response.json())
        .then(data => {
            populateSupplierDropdown(data);
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}

function populateSupplierDropdown(suppliers) {
    var supplierDropdown = document.querySelector('select[name="productName"]');

    // Add an option for each supplier
    suppliers.forEach(function (supplier) {
        var option = document.createElement('option');
        option.value = supplier; // Replace 'id' with the actual property name for the supplier ID
        option.textContent = supplier; // Replace 'name' with the actual property name for the supplier name
        supplierDropdown.appendChild(option);
    });
}
// Function to handle placing the order
function placeOrder() {
    // Check if the cart is empty
    if (cart.length === 0) {
        // alert('Your cart is empty. Please add items before placing an order.');
        Notify('Your cart is empty. Please add items before placing an order.', 'danger');
        return;
    }

   
    // Prepare the data
    var data = {
        
        products: cart
    };

    console.log(data);
    // Send a POST request
    fetch(`${baseURL}/api/placeOrder/${customerId}`, {
       
        method: 'POST',
        headers: {
            "Content-Type": "application/json",
             'Authorization': `Bearer ${cookie}`,
            
        },
        body: JSON.stringify(data),
    })
        .then(response => response.text())
        .then(data => {
            console.log('Success:', data);
            cart = [];
            // Redirect to Purchase.html
            // alert('ORDER HAS BEEN PLACED SUCESSFULLY')
            Notify('ORDER HAS BEEN PLACED SUCESSFULLY', 'success');
            setTimeout(() => {
                window.location.href = 'DeliveryOrder.html';
            }, 1000)
        })
        .catch((error) => {
            console.error('Error:', error);
        });

}




function onNavItemClick(itemId, url) {



    if (url) {
        window.location.href = url;
    }
}




// Function to handle form submission
function handleFormSubmit(event) {
    // Prevent the form from submitting normally
    event.preventDefault();

    // Get the form data
   
    var productName = event.target.elements[2].value;
    var price = event.target.elements[3].value;
    var quantity = event.target.elements[4].value;

    // Check if all fields are filled
    if (productName.trim() === '' ||  price.trim() === '' || quantity.trim() === '') {
        // alert('Please fill in all fields.');
        Notify('Please fill in all fields.', 'danger');
        return;
    }


    // Add the product to the stack
    addToStack(productName, quantity);

    // Clear the form
    event.target.reset();
}

// Function to check if a value is a valid integer
function isValidInteger(value) {
    // Use regular expression to match integer pattern
    var integerPattern = /^\d+$/;
    return integerPattern.test(value);
}




function addToStack(productName, quantity) {

    // Create a new div element for the product
    var productElement = document.createElement('div');
    // productElement.textContent = productName ;
    productElement.className = 'stack-item';

    var productnameElement = document.createElement('span');
    productnameElement.textContent = productName;
    productnameElement.className = 'product-name'

    productElement.appendChild(productnameElement);


    var quantityElement = document.createElement('span');
    quantityElement.textContent = quantity;
    quantityElement.className = 'quantity';

    productElement.appendChild(quantityElement);




    // Create an img element for the minus sign
    var minusSign = document.createElement('img');
    minusSign.src = '../resources/x-button.png'; // Replace with the path to your image
    minusSign.className = 'delete-button';

    minusSign.alt = 'Delete';
    minusSign.onclick = function () {
        productElement.remove();
        if (stackContainer.childElementCount === 2) {
            stackContainer.style.display = 'none';
        }
    }


    // Add the delete button to the product element
    productElement.appendChild(minusSign);

    // Get the stack container
    var stackContainer = document.getElementById('stack');

    stackContainer.style.display = 'flex';

    // Add the new product to the top of the stack
    stackContainer.insertBefore(productElement, stackContainer.children[2]);

    // Get price and volume from the form
    var price = document.querySelector('input[placeholder="Price"]').value;
 

    // Add the product to the cart array
    cart.push({
        productName: productName,
        orderQuantity: quantity,
        sellPrice: price,
        
    });


}
function extractCookie() {
    const cookieRow = document.cookie.split('; ').find(row => row.startsWith('cookie=='));
    return cookieRow ? cookieRow.split('==')[1] : '';
}

// Function to extract customer ID from URL parameters
function getCustomerIdFromUrl() {
    var urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('customerId');
}
window.onload = fetchProducts;


// Function to clear the cart
// function clearCart() {
//     var stackContainer = document.getElementById('stack');
//     var cartItems = document.querySelectorAll('.stack-item');

//     // Remove each cart item from the stack
//     cartItems.forEach(function (item) {
//         item.remove();
//     });

//     stackContainer.style.display = 'none';
// }