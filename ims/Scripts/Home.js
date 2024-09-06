const baseURL = SERVER_URL;

function extractCookie() {
    const cookieRow = document.cookie.split('; ').find(row => row.startsWith('cookie=='));
    return cookieRow ? cookieRow.split('==')[1] : '';
}

const cookie = extractCookie();

const instance = axios.create({
    baseURL: `${baseURL}/api/`,
    headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${cookie}`
    },
    withCredentials: true 
});


function getCapacity(godownId){
    
    instance.get(`getCapacity/${godownId}`)
        .then(response => {
            const {totalCapacity, availableCapacity} = response.data;
            document.getElementById('totalCapacity').innerHTML = `${totalCapacity} m<sup>3</sup>`;
            document.getElementById('availableCapacity').innerHTML = `${availableCapacity} m<sup>3</sup>`;
            localStorage.setItem('totalCapacity', totalCapacity);
            localStorage.setItem('availableCapacity', availableCapacity);

        })
        .catch(error => {
            console.log(error);
        });
}

const listLowStockProducts = (godownId, percentage = 0.01) => {
    return instance.get('/listLowStockProducts', {
        params: {
            godownId: godownId,
            percentage: percentage
        }
    })
    .then(response => {
        return response.data;
    })
    .catch(error => {
        throw error;
    });
};

function getSalesByDate(godownId, date) {
    return instance.get('/getSalesByDate', {
        params: {
            godownId: godownId,
            date: date
        }
    });
}

function getCurrentDate() {
    const currentDate = new Date();
    const year = currentDate.getFullYear();
    const month = String(currentDate.getMonth() + 1).padStart(2, '0');
    const day = String(currentDate.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}

function resetDashboard() {
    document.getElementById('allItems').innerHTML = "No Items";
    document.getElementById('totalQuantitySum').innerHTML = "0";
    document.getElementById('purchaseOrders').innerHTML = "No Purchases";
    document.getElementById('LowStock').innerHTML = "No items";
    document.getElementById('saleOrders').innerHTML = "No Sales";
    document.getElementById('totalsaleorders').innerHTML = "0";
    document.getElementById('totalitemsorderd').innerHTML = "0";
    document.getElementById('totalItemsOrderedDay').innerHTML = "0";
    document.getElementById('totalSaleOrdersOfDay').innerHTML = "0";
    
    for (let i = 1; i <= 4; i++) {
        const productNameElement = document.getElementById(`productname${i}`);
        const productQuantityElement = document.getElementById(`productOrderQuantity${i}`);
        if (productNameElement && productQuantityElement) {
            productNameElement.textContent = "Not found";
            productQuantityElement.textContent = "";
        }
    }
}


function setProductDetails(godownId) {
    resetDashboard();
    instance.get(`productsCount/${godownId}`)
        .then(response => {
            const allItems = response.data.productsCount;
            const totalQuantitySum = response.data.totalQuantity;
            document.getElementById('allItems').innerHTML = allItems === 0 ? "No Items" : allItems;
            document.getElementById('totalQuantitySum').innerHTML = totalQuantitySum;
        })
        .catch(error => {
            console.log(error);     
        });

    instance.get(`getPurchaseOrderCount/${godownId}`)
        .then(response => {
            const purchaseOrders = response.data.purchaseOrderCount;
            document.getElementById('purchaseOrders').innerHTML = purchaseOrders === 0 ? "No Purchases" : purchaseOrders;
        })
        .catch(error => {
            console.log(error);
        });

    listLowStockProducts(godownId)
        .then(data => {
            const LowStockItems = data.length;
            document.getElementById('LowStock').innerHTML = LowStockItems === 0 ? "No Items" : LowStockItems;
        })
        .catch(error => {
            console.error(error);
        });

        instance.get(`getTotalSalesCount/${godownId}`)
        .then(response => {
            const saleOrdersCount = response.data.saleOrdersCount;
            const totalQuantitiesSold = response.data.totalQuantitiesSold;
            document.getElementById('saleOrders').innerHTML = saleOrdersCount === 0 ? "No Sales" : saleOrdersCount;
            document.getElementById('totalsaleorders').innerHTML = saleOrdersCount === 0 ? "No Sales" : saleOrdersCount;
            document.getElementById('totalitemsorderd').innerHTML = totalSaleOrdersOfDay === 0 ? "No Sales" : totalQuantitiesSold;
        })
        .catch(error => {
            console.log(error);
        });

        const currentDate = getCurrentDate();

        getSalesByDate(godownId, currentDate)
            .then(response => {
             
                const {totalQuantitiesSoldByDate, salesByDate} = response.data;
            
                document.getElementById('totalItemsOrderedDay').innerHTML = totalQuantitiesSoldByDate === 0 ? "0" : totalQuantitiesSoldByDate;
                document.getElementById('totalSaleOrdersOfDay').innerHTML = salesByDate === 0 ? "0" : salesByDate;
            })
            .catch(error => {
                console.error('Error fetching sales:', error);
            });

        instance.get(`/getTopSellingProducts/${godownId}`)
    .then(response => {
        const topSellingProducts = response.data;
        const numProducts = Math.min(topSellingProducts.length, 4);

        for (let i = 0; i < numProducts; i++) {
            const productName = topSellingProducts[i][0];
            const orderQuantity = topSellingProducts[i][1];

            const productNameElement = document.getElementById(`productname${i + 1}`);
            const productQuantityElement = document.getElementById(`productOrderQuantity${i + 1}`);

            if (productNameElement && productQuantityElement) {
                productNameElement.textContent = productName;
                productQuantityElement.textContent = `${orderQuantity} Pieces`;
            }
        }
    })
    .catch(error => {
        console.error('Error fetching top selling products:', error);
    });


       
}

function toggleGodownSelectRow() {
    const godownSelectRow = document.getElementById('godownSelectRow');
    const user = JSON.parse(localStorage.getItem('user'));
    const salesActivityDiv = document.querySelector('.sales-activity');

    if (user && user.role === 'admin') {
        godownSelectRow.style.display = 'block';
        salesActivityDiv.classList.remove('col-md-12');
        salesActivityDiv.classList.add('col-md-6');
    } else {
        godownSelectRow.style.display = 'none';
        salesActivityDiv.classList.remove('col-md-6');
        salesActivityDiv.classList.add('col-md-12');
    }
}


function populateGodownDropdown() {
    const godownSelect = document.getElementById('godownSelect');

    instance.get('getAllGodown')
        .then(response => {
            const godowns = response.data;
            
            godownSelect.innerHTML = '';

            godowns.forEach(godown => {
                const option = document.createElement('option');
                
                option.value = godown.godownId;
                option.textContent = `Godown ID: ${godown.godownId}`;
                godownSelect.appendChild(option);
            });

            document.getElementById('godownSelectRow').style.display = 'block';
            if (godowns.length > 0) {
                const defaultGodownId = godowns[0].godownId;
                setProductDetails(defaultGodownId);
                getCapacity(defaultGodownId);
            }
        })
        .catch(error => {
            console.error('Error fetching godowns:', error);
            godownSelect.innerHTML = '<option class="text-center" value="" selected>No Godowns Found</option>';
        });
}




function setHomePage(){
    var godownId = parseInt(JSON.parse(localStorage.getItem('user')).godownId);
    if(godownId!=0){
        getCapacity(godownId);
        setProductDetails(godownId);
    }
    else{
        populateGodownDropdown();
        document.getElementById('godownSelect').addEventListener('change', (e) =>{
            const selectedGodownId = e.target.value;
            getCapacity(selectedGodownId);
            setProductDetails(selectedGodownId);
        });
    }
}

  document.addEventListener('DOMContentLoaded', function() {
    toggleGodownSelectRow();
    setHomePage();
  });
  
