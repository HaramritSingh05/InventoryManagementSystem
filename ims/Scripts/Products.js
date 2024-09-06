var table;
var table1;

const baseURL = SERVER_URL;

function extractCookie() {
    const cookieRow = document.cookie.split('; ').find(row => row.startsWith('cookie=='));
    return cookieRow ? cookieRow.split('==')[1] : '';
}

function generateProductRows(godownId) {
    const cookie = extractCookie();

    axios.get(`${baseURL}/api/listProducts/${godownId}`, {
        headers: {
            'Content-Type': 'application/json', 
            'Authorization': `Bearer ${cookie}`
        },

        withCredentials: true,
        
    })
    .then(function(response) {
        
        var products = response.data;
        var tbody = document.getElementById('product-table').getElementsByTagName('tbody')[0];
       
        table.clear().draw();

        if (products.length > 0) {
            var LowStock = 0;
            var allItems = products.length;
            products.forEach(function(product) {
                var keyMappings = [
                    { key: 'productId', column: 'Product ID' },
                    { key: 'productName', column: 'Product Name' },
                    { key: 'productType', column: 'Product Type (kw)' },
                    { key: 'productCategory', column: 'Product Category' },
                    { key: 'totalQuantity', column: 'Product Quantity' },
                    { key: 'productVolume', column: 'Product Volume' },
                    { key: 'price', column: 'Cost Price' }
                ];

                const godownTotalCapacity = parseInt(localStorage.getItem('totalCapacity'));
                const productVolume = product.productVolume;
                const productQuantity = product.totalQuantity;
                var productVolumePercentage = productVolume*productQuantity/godownTotalCapacity * 100;
                
                if(productVolumePercentage < 10){
                    LowStock++;
                }
                
                var rowData = keyMappings.map(function(mapping) {
                    var value = product[mapping.key];
                    if (value === null || value === undefined) {
                        return 'NA';
                    } else {
                        return value;
                    }
                });
                
                var actionCellContent = '<a title="Update Product" class="btn " data-bs-toggle="modal" data-bs-target="#editProductModal" id="editProductBtn" ><i class="fa-solid fa-xl fa-pen-to-square" style="pointer-events: none;"></i></a> ';
                rowData.push(actionCellContent);

                table.row.add(rowData).draw();
            });

            
            // localStorage.setItem('LowStock', LowStock);
            // localStorage.setItem('allItems', allItems);
            
        } else {
            
            console.log('No products found.');
        }
    })
    .catch(function(error) {
        // Handle error
        console.error('Error fetching products:', error);
    });

    
}


function generateProductRowsAdmin() {
    const cookie = extractCookie();

    axios.get(`${baseURL}/api/listAllProducts`, {
        headers: {
            'Content-Type': 'application/json', 
            'Authorization': `Bearer ${cookie}`
        },

        withCredentials: true,
        
    })
    .then(function(response) {
        
        var products = response.data;

        var tbody = document.getElementById('product-table-1').getElementsByTagName('tbody')[0];
    
        table1.clear().draw();

        if (products.length > 0) {
            var LowStock = 0;
            var allItems = products.length;
            products.forEach(function(product) {
                var keyMappings = [
                    { key: 'productId', column: 'Product ID' },
                    { key: 'productName', column: 'Product Name' },
                    { key: 'productType', column: 'Product Type (kw)' },
                    { key: 'productCategory', column: 'Product Category' },
                    { key: 'totalQuantity', column: 'Product Quantity' },
                    { key: 'productVolume', column: 'Product Volume' },
                    { key: 'price', column: 'Cost Price' },
                    { key: 'godownId', column: 'Godown ID'}
                ];
                
                var rowData = keyMappings.map(function(mapping) {
                    var value = product[mapping.key];
                    if (value === null || value === undefined) {
                        return 'NA';
                    } else {
                        return value;
                    }
                });
                
                table1.row.add(rowData).draw();
            });
            
        } else {
            
            console.log('No products found.');
        }
    })
    .catch(function(error) {
        console.error('Error fetching products:', error);
    });

    
}

function validateCostPrice(costPrice, costPriceInput) {
    var costPriceInput = costPriceInput;
    if (isNaN(parseFloat(costPrice))) {
        costPriceInput.classList.add('is-invalid');
        costPriceInput.classList.remove('is-valid');
        return false;
    } else {
        costPriceInput.classList.remove('is-invalid');
        costPriceInput.classList.add('is-valid');
        return true;
    }
}

function validateProductVolume(productVolume, volumeInput) {
    var volumeInput = volumeInput;
    if (isNaN(parseFloat(productVolume))) {
        volumeInput.classList.add('is-invalid');
        volumeInput.classList.remove('is-valid');
        return false;
    } else {
        volumeInput.classList.remove('is-invalid');
        volumeInput.classList.add('is-valid');
        return true;
    }
}


function validateProductQuantity(productQuantity, quantityInput) {
    var quantityInput = quantityInput;
    if (!Number.isInteger(parseInt(productQuantity))) {
        quantityInput.classList.add('is-invalid');
        quantityInput.classList.remove('is-valid');
        return true;
        return false;
    } else {
        quantityInput.classList.remove('is-invalid');
        quantityInput.classList.add('is-valid');
        return true;
    }
}

function validateProductCode(productCode, productCodeInput) {
    var productCodeInput = productCodeInput;
    if (!/^[a-zA-Z][a-zA-Z0-9]*$/.test(productCode)) {
        productCodeInput.classList.add('is-invalid');
        productCodeInput.classList.remove('is-valid');
        return false;
    } else {
        productCodeInput.classList.remove('is-invalid');
        productCodeInput.classList.add('is-valid');
        return true;
    }
}

function validateProductCategory(productCategory, productCategoryInput) { 
    var productCategoryInput = productCategoryInput;
    if (!/^[a-zA-Z][a-zA-Z0-9]*$/.test(productCategory)) {
        productCategoryInput.classList.add('is-invalid');
        productCategoryInput.classList.remove('is-valid');
        return false;
    } else {
        productCategoryInput.classList.remove('is-invalid');
        productCategoryInput.classList.add('is-valid');
        return true;
    }
}

function validateProductType(productType, productTypeInput) {
    var productTypeInput = productTypeInput;
    if (productType === "Select Product Type") {
        productTypeInput.classList.add('is-invalid');
        productTypeInput.classList.remove('is-valid');
        return false;
    } else {
        productTypeInput.classList.remove('is-invalid');
        productTypeInput.classList.add('is-valid');
        return true;
    }
}


async function addProduct() {
    const godownId = parseInt(JSON.parse(localStorage.getItem('user')).godownId);
    const cookie = extractCookie();

    const addProductModal = document.getElementById("addProductModal");
    addProductModal.addEventListener("submit", async (e) => {
        e.preventDefault();

        const formData = new FormData(e.target);
        const productName = formData.get('productCode');
        const productCategory = formData.get('productCategory');
        const productVolume = formData.get('productVolume');
        const productQuantity = formData.get('productQuantity');
        const costPrice = formData.get('costPrice');

        const validCostPrice = validateCostPrice(costPrice,  document.getElementById('addProduct').querySelector('#costprice'));
        const validProductVolume = validateProductVolume(productVolume, document.getElementById('addProduct').querySelector('#productVolume'));
        const validProductQuantity = validateProductQuantity(productQuantity, document.getElementById('addProduct').querySelector('#productQuantity'));
        const validProductCode = validateProductCode(productName, document.getElementById('addProduct').querySelector('#productCode'));
        const validProductCategory = validateProductCategory(productCategory,  document.getElementById('addProduct').querySelector('#productCategory'));
        const validProductType = validateProductType(document.getElementById('productTypeSelect').value, document.getElementById('addProduct').querySelector('#productTypeSelect'));

        if (validCostPrice && validProductVolume && validProductQuantity && validProductCode && validProductCategory && validProductType) {
            const productTypeSelect = document.getElementById('productTypeSelect');
            const selectedProductType = productTypeSelect.value; 

            const newTotalVolume = productQuantity * productVolume;

            const availableGodownCapacity = await getAvailableGodownCapacity(godownId);

            if (newTotalVolume > availableGodownCapacity) {
                // window.alert("The new product quantity and volume would exceed the available godown capacity. Please decrease the quantity, volume, or choose another godown.");
                $('#addProductModal').modal('hide');
                Notify("The new product quantity & volume would exceed the available godown capacity.", "warning");
            } else {
                const data = {
                    godownId: godownId,
                    productName: productName,
                    productType: selectedProductType,
                    productCategory: productCategory,
                    productVolume: productVolume,
                    totalQuantity: productQuantity,
                    price: costPrice
                };

                try {
                    const response = await axios.post(`${baseURL}/api/addProduct`, data, {
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': `Bearer ${cookie}`
                        }
                    });

                    // window.location.href = "Products.html";
                    $('#addProductModal').modal('hide');

                    Notify("Product added successfully", "success");
                    generateProductTable();
                } catch (error) {
                    console.error('Error adding product:', error);
                    $('#addProductModal').modal('hide');
                    Notify("Failed to add product: "+ error.response.data.message, "danger");
                }
            }
        } else {
            console.log("Invalid input, please correct the errors.");
            e.stopPropagation();
        }
    });
}

function fillEditProductModal(productId) {
    const cookie = extractCookie();
    axios.get(`${baseURL}/api/getProduct/${productId}`, {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${cookie}`
        },
        withCredentials: true
    })
    .then(function(response) {
        const product = response.data;
        const productVolume = product.productVolume;
        const productQuantity = product.totalQuantity;
        const costPrice = product.price;
        console.log(costPrice);
        const editProductModal = document.getElementById('editProductModal');
        editProductModal.querySelector('#productVolume').value = productVolume;
        editProductModal.querySelector('#productQuantity').value = productQuantity;
        editProductModal.querySelector('#costprice').value = costPrice;
    })
    .catch(function(error) {
        console.error('Error fetching product:', error);
    });

}

function handleEditProductClick(){
    document.getElementById('product-table').addEventListener('click', function(e) { 
        if(e.target.id.includes('editProductBtn')) {
            var row = e.target.closest( "tr" );
            const productId = row.cells[0].textContent;

            const productName = row.cells[1].textContent;
            localStorage.setItem('productName', productName);
            const productVolume = row.cells[5].textContent;
            localStorage.setItem('productVolume', productVolume);
            const productQuantity = row.cells[4].textContent;
            localStorage.setItem('productQuantity', productQuantity);
            
            fillEditProductModal(productId);
        }

        
    });

}

async function editProduct() {
    const godownId = parseInt(JSON.parse(localStorage.getItem('user')).godownId);
    const cookie = extractCookie();

    const editProductModal = document.getElementById("editProductModal").addEventListener("submit", async (e) => {
        e.preventDefault();

        const formData = new FormData(e.target);
        const productName = localStorage.getItem('productName');
        localStorage.removeItem('productName');

        const productVolume = parseFloat(formData.get('productVolume'));
        const newProductQuantity = parseInt(formData.get('productQuantity'));
        const costPrice = parseFloat(formData.get('costPrice'));

        const validProductVolume = validateProductVolume(productVolume, document.getElementById('editProductModal').querySelector('#productVolume'));
        const validProductQuantity = validateProductQuantity(newProductQuantity, document.getElementById('editProductModal').querySelector('#productQuantity'));
        const validCostPrice = validateCostPrice(costPrice, document.getElementById('editProductModal').querySelector('#costprice'));

        if (validProductVolume && validProductQuantity && validCostPrice) {
            const previousProductQuantity = parseInt(localStorage.getItem('productQuantity'));
            const previousProductVolume = parseFloat(localStorage.getItem('productVolume'));
            const newTotalVolume = newProductQuantity * productVolume;
            const oldTotalVolume = previousProductQuantity * previousProductVolume;
            const netVolumeChange = newTotalVolume - oldTotalVolume;

            const availableGodownCapacity = await getAvailableGodownCapacity(godownId);

            if (netVolumeChange > availableGodownCapacity) {
                $('#editProductModal').modal('hide');
                Notify("The new product quantity & volume would exceed the available godown capacity.", "warning");
            } else {
                const data = {
                    godownId: godownId,
                    productVolume: productVolume,
                    totalQuantity: newProductQuantity,
                    price: costPrice,
                    productName: productName
                };

                try {
                    const response = await axios.patch(`${baseURL}/api/updateProduct`, data, {
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': `Bearer ${cookie}`
                        },
                        withCredentials: true
                    });

                    // window.alert("Product edited successfully");
                    $('#editProductModal').modal('hide');
                    Notify("Product edited successfully", "success");
                    generateProductTable();
                } catch (error) {
                    console.error('Error editing product:', error);
                    $('#editProductModal').modal('hide');
                    Notify("Failed to edit product: "+ error.response.data.message, "danger");
                }
            }
        } else {
            console.log("Invalid input, please correct the errors.");
            e.stopPropagation();
        }
    });
}


async function getAvailableGodownCapacity(godownId) {
    try {
        const cookie = extractCookie();
        const response = await axios.get(`${baseURL}/api/getCapacity/${godownId}`, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${cookie}`
            },
            withCredentials: true

        });
        return response.data.availableCapacity;
    } catch (error) {
        console.error('Error fetching available godown capacity:', error);
        return 0;
    }
}

function conditionalRendering() {
    const role = JSON.parse(localStorage.getItem('user')).role;
    if (role === "godownhead") {
        document.getElementById('addProductDisplaybtn').style.display = 'block';
        document.getElementById('godownHead-productTable').style.display = 'block';
        document.getElementById('admin-productTable').style.display = 'none';

    } else if (role === "admin") {
        document.getElementById('addProductDisplaybtn').style.display = 'none';
        document.getElementById('godownHead-productTable').style.display = 'none';
        document.getElementById('admin-productTable').style.display = 'block';
       
    }
}

function generateProductTable() {
    const user = JSON.parse(localStorage.getItem('user'));
    const godownId = parseInt(user.godownId);
    const role = user.role;

    if(role === "godownhead"){
        generateProductRows(godownId);
       
    }
    else{
        generateProductRowsAdmin();
    }
}

document.addEventListener("DOMContentLoaded", function() {
    conditionalRendering();
    handleEditProductClick();
    table = new DataTable('#product-table');
    table1 = new DataTable('#product-table-1');
    generateProductTable();
});


