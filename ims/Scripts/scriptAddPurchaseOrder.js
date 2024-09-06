const baseURL = SERVER_URL;

document.getElementById('productForm').addEventListener('submit', handleFormSubmit);
  

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
        document.getElementById("addpurchaseorderbody").style.display = "block";
      }
    } else {
      window.location.href = "login.html";
      alert("Please login first.");
    }
  }

  

window.onload = ()=>{
    checkCookie()
    fetchSuppliers()
    
};
function extractCookie() {
    const cookieRow = document.cookie.split('; ').find(row => row.startsWith('cookie=='));
    return cookieRow ? cookieRow.split('==')[1] : '';
}

const godownId = parseInt(JSON.parse(localStorage.getItem('user')).godownId);
const cookie = extractCookie();

var godownIdInput = document.querySelector('input[placeholder="Godown Id"]');

// Set the value of the input field to the fetched GodownId
if (godownIdInput) {
    godownIdInput.value = "Godown Id: " + godownId;
    console.log(godownIdInput.value);
}

var cart = []; // Array to store the cart items

function fetchSuppliers() {
    fetch(`${baseURL}/api/getAllSuppliers`,{
    headers:{
        "Content-Type": "application/json",
        'Authorization': `Bearer ${cookie}`
    }})
        .then(response => response.json())
        .then(data => {
            populateSupplierDropdown(data);
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}

function populateSupplierDropdown(suppliers) {
    var supplierDropdown = document.querySelector('select[name="supplierName"]');

    // Add an option for each supplier
    suppliers.forEach(function (supplier) {
        var option = document.createElement('option');
        option.value = supplier.supplierId; // Replace 'id' with the actual property name for the supplier ID
        option.textContent = supplier.supplierName; // Replace 'name' with the actual property name for the supplier name
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

    // Calculate total volume of products in the cart
    var totalVolume = cart.reduce((acc, product) => {
        return acc + (parseInt(product.productVolume) * parseInt(product.purchaseQuantity));
    }, 0);


    fetch(`${baseURL}/api/getCapacity/${godownId}`, {
        headers: {
            "Content-Type": "application/json",
            'Authorization': `Bearer ${cookie}`
        }
    })
    .then(response => response.json())
    .then(data => {
        // Check if available capacity is less than total volume of products in the cart
        if (parseInt(data.availableCapacity) < totalVolume) {
            Notify('Not enough capacity available in godown.', 'danger');
        } else {


    // Getting godownId and supplierId from the form
   

    var supplierId = document.querySelector('select[name="supplierName"]').value;

    // Prepare the data
    var data = {
        godownId: godownId,
        supplierId: supplierId,
        products: cart
    };

console.log(data);
    // Send a POST request
    fetch(`${baseURL}/api/createPurchaseOrder`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${cookie}`
        },
        body: JSON.stringify(data),
    })
        .then(response => response.text())
        .then(data => {
            console.log('Success:', data);
            cart = [];
            // Redirect to Purchase.html
            window.location.href = 'Purchase.html';
        })
        .catch((error) => {
            console.error('Error:', error);
        });


    // Enable all options in the supplier dropdown
    var supplierDropdown = document.querySelector('select[name="supplierName"]');
    var options = supplierDropdown.options;

    for (var i = 0; i < options.length; i++) {
        options[i].disabled = false;
    }
}
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
    var godownId = event.target.elements[1].value;
    var supplierName = event.target.elements[2].value;
    var productName = event.target.elements[3].value;
    var productVolume = event.target.elements[4].value;
    var price = event.target.elements[5].value;
    var quantity = event.target.elements[6].value;
    var productCategory = event.target.elements[7].value;
    var productType = event.target.elements[8].value;

    // Check if all fields are filled
    if (godownId.trim() === '' || supplierName.trim() === '' || productName.trim() === '' ||
        productVolume.trim() === '' || price.trim() === '' || quantity.trim() === '' || productCategory.trim() === '' || productType.trim() === '') {
        // alert('Please fill in all fields.');
        // Notify('Please fill in all fields.', 'danger');
        return;
    }

    // Check if product volume is a valid integer
    if (!isValidInteger(productVolume)) {
        // alert('Product volume must be a whole number.');
        Notify('Product volume must be a whole number.', 'danger');
        return;
    }

    // Check if price is a valid integer
    if (!isValidInteger(price)) {
        // alert('Price must be a whole number.');
        Notify('Price must be a whole number.', 'danger');
        return;
    }

    // Check if quantity is a valid integer
    if (!isValidInteger(quantity)) {
        // alert('Quantity must be a whole number.');
        Notify('Quantity must be a whole number.', 'danger');
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
    var productVolume = document.querySelector('input[placeholder="Product Volume"]').value;
    var productCategory = document.querySelector('input[placeholder="Product Category"]').value;
    var productType = document.querySelector('select[name="productTypeSelect"]').value;

    // Add the product to the cart array
    cart.push({
        productName: productName,
        purchaseQuantity: quantity,
        costPrice: price,
        productVolume: productVolume,
        productCategory : productCategory,
        productType : productType
    });

    // Disable all other options in the supplier dropdown
    var supplierDropdown = document.querySelector('select[name="supplierName"]');
    var options = supplierDropdown.options;
    var selectedOption = supplierDropdown.value;

    for (var i = 0; i < options.length; i++) {
        if (options[i].value !== selectedOption) {
            options[i].disabled = true;
        }
    }

}



// Function to clear the cart
function clearCart() {
    var stackContainer = document.getElementById('stack');
    var cartItems = document.querySelectorAll('.stack-item');

    // Remove each cart item from the stack
    cartItems.forEach(function (item) {
        item.remove();
    });

    stackContainer.style.display = 'none';
}

