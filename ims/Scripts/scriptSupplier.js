let sId = 0;

const baseURL = SERVER_URL;

function refreshTable() {
  fetch(`${baseURL}/api/getAllSuppliers`, {
      headers: {
          "Content-Type": "application/json",
          'Authorization': `Bearer ${cookie}`
      }
  })
  

      .then(response => response.json())

      .then(data => {
          // console.log(data)
          var tbody = document.querySelector('#tbody');

          // // Clear out existing rows
          // tbody.innerHTML = '';

          // Add new rows for each item in the returned data
          data.forEach(item => {
              let row = tbody.insertRow();
              let cell1 = row.insertCell(0);
              let cell2 = row.insertCell(1);
              let cell3 = row.insertCell(2);
              let cell4 = row.insertCell(3);
              let cell5 = row.insertCell(4);
              cell1.textContent = item.supplierId;
              cell2.textContent = item.supplierName;
              cell3.textContent = item.address;
              cell4.textContent = item.contactNumber;
              cell5.innerHTML = `<a  title="Update Supplier" class="btn update-btn" data-bs-toggle="modal" data-bs-target="#editSupplierModal" onclick="FetchSupplierData(${item.supplierId})" ><i class="fa-solid fa-xl fa-pen-to-square"></i></a>` ;

          });
          $('#myTable').DataTable();


      });
      // $('#myTable').DataTable().destroy();

}





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
      document.getElementById("supplierbody").style.display = "block";
    }
  } else {
    window.location.href = "login.html";
    alert("Please login first.");
  }
}



window.onload = ()=>{
  checkCookie()
  refreshTable()
  // FetchSupplierData()
 
};

function FetchSupplierData(supplierId){
  sId=supplierId;
  fetch(`${baseURL}/api/getSupplierBySupplierId/` + supplierId ,{
      headers:{
          "Content-Type": "application/json",
          'Authorization': `Bearer ${cookie}`
      }})
      .then(response => response.json())
      .then(data => {
          // Pre-fill the form with the current data
          document.querySelector('input[placeholder="Name"]').value = data.supplierName;
          document.querySelector('input[placeholder="Address"]').value = data.address;
          document.querySelector('input[placeholder="Contact Number"]').value = data.contactNumber;
          
      });
      
}

function handleFormSubmit (event) {
  // Prevent the form from submitting normally
  event.preventDefault();
  // console.log(sId)


  // Get the updated data from the form
  var supplierName = document.querySelector('input[placeholder="Name"]').value;
  var address = document.querySelector('input[placeholder="Address"]').value;
  var contactNumber = document.querySelector('input[placeholder="Contact Number"]').value;

  // Call the Update API
  fetch(`${baseURL}/api/updateSuppliers`, {
      method: 'PUT',
      headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${cookie}`
      },
      body: JSON.stringify({
          supplierId:sId,
          supplierName: supplierName,
          address: address,
          contactNumber: contactNumber
      })
  })
  .then(response => response.text()) 
  .then(data => {
      // Handle the response...
      $('#editSupplierModal').modal('hide');
      Notify('Supplier updated successfully!', 'success');
      setTimeout(() => {
          window.location.href = 'supplier.html';
      }, 1000);
  })
  .catch(error => {
      console.error('Error:', error);
      Notify('An error occurred while updating the supplier.', 'danger');
  });
};




// Handle navigation item clicks
function onNavItemClick(itemId, url) {
    if (url) {
        window.location.href = url;
    }
}

function extractCookie() {
    const cookieRow = document.cookie.split('; ').find(row => row.startsWith('cookie=='));
    return cookieRow ? cookieRow.split('==')[1] : '';
}

const godownId = parseInt(JSON.parse(localStorage.getItem('user')).godownId);
const cookie = extractCookie();


// initialize DataTable

// const toggleButton = document.getElementById('nav-toggle');
// const navLinks = document.getElementById('nav-links');

// toggleButton.addEventListener('click', () => {
//     navLinks.classList.toggle('active');
//     console.log(navLinks);

// })


// Call refreshTable when the page loads and after updating a supplier



document.addEventListener("DOMContentLoaded", () => {
  setActivePage();
});
    












