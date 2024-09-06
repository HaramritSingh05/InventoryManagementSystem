const xValues1 = [
  "PEGTSI10KA2",
  "PEGTSI3K3A2",
  "PEGTSI7K0F2",
  "PEGTSI5K0A2",
  "PEGTSI4K0A2",
];

new Chart("myChart1", {
  type: "line",
  data: {
    labels: xValues1,
    datasets: [
      {
        data: [860, 1140, 1060, 1060, 2070],
        borderColor: "red",
        fill: false,
      },
      {
        data: [1600, 1700, 1700, 1900, 4000],
        borderColor: "green",
        fill: false,
      },
      {
        data: [300, 700, 2000, 5000, 6000],
        borderColor: "blue",
        fill: false,
      },
      {
        data: [150, 300, 4500, 600, 750],
        borderColor: "grey",
        fill: false,
      },
      {
        data: [200, 400, 600, 800, 1000],
        borderColor: "lavender",
        fill: false,
      },
    ],
  },
  options: {
    legend: { display: false },
  },
});

const xValues2 = [
  "PEGTSI10KA2",
  "PEGTSI3K3A2",
  "PEGTSI7K0F2",
  "PEGTSI5K0A2",
  "PEGTSI4K0A2",
];

new Chart("myChart2", {
  type: "line",
  data: {
    labels: xValues2,
    datasets: [
      {
        data: [860, 1140, 1060, 1060, 3070],
        borderColor: "yellow",
        fill: false,
      },
      {
        data: [1600, 1700, 1700, 1900, 2000],
        borderColor: "green",
        fill: false,
      },
      {
        data: [300, 700, 2000, 5000, 6000],
        borderColor: "blue",
        fill: false,
      },
      {
        data: [150, 300, 4500, 600, 1200],
        borderColor: "grey",
        fill: false,
      },
      {
        data: [200, 400, 600, 800, 1000],
        borderColor: "lavender",
        fill: false,
      },
    ],
  },
  options: {
    legend: { display: false },
  },
});

const xValues3 = [
  "PEGTSI10KA2",
  "PEGTSI3K3A2",
  "PEGTSI7K0F2",
  "PEGTSI5K0A2",
  "PEGTSI4K0A2",
];

new Chart("myChart3", {
  type: "line",
  data: {
    labels: xValues3,
    datasets: [
      {
        data: [860, 1140, 1060, 1060, 1070],
        borderColor: "red",
        fill: false,
      },
      {
        data: [1600, 1700, 1700, 1900, 2000],
        borderColor: "seagreen",
        fill: false,
      },
      {
        data: [300, 700, 2000, 5000, 6000],
        borderColor: "lavender",
        fill: false,
      },
      {
        data: [150, 300, 4500, 600, 750],
        borderColor: "yellow",
        fill: false,
      },
      {
        data: [200, 400, 600, 800, 1000],
        borderColor: "black",
        fill: false,
      },
    ],
  },
  options: {
    legend: { display: false },
  },
});

// TSP1

var xValues4 = ["G1", "G2", "G3"];
var yValues1 = [55, 49, 44];
var barColors = ["#b91d47", "#00aba9", "#e8c3b9"];

new Chart("myChart4", {
  type: "doughnut",
  data: {
    labels: xValues4,
    datasets: [
      {
        backgroundColor: barColors,
        data: yValues1,
      },
    ],
  },
});

// TSP2
var xValues5 = ["G1", "G2", "G3"];
var yValues2 = [55, 49, 44];
var barColors = ["#b91d47", "#00aba9", "#e8c3b9"];

new Chart("myChart5", {
  type: "doughnut",
  data: {
    labels: xValues5,
    datasets: [
      {
        backgroundColor: barColors,
        data: yValues2,
      },
    ],
  },
});

// LSP
var xValues6 = ["G1", "G2", "G3"];
var yValues3 = [55, 49, 44];
var barColors = ["#b91d47", "#00aba9", "#e8c3b9"];

new Chart("myChart6", {
  type: "doughnut",
  data: {
    labels: xValues6,
    datasets: [
      {
        backgroundColor: barColors,
        data: yValues3,
      },
    ],
  },
});

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

document.getElementById("myDropdown").addEventListener("click", (e) => {
  const id = e.target.id;
  const chart1Canvas = document.getElementById("myChart1");
  const chart2Canvas = document.getElementById("myChart2");
  const chart3Canvas = document.getElementById("myChart3");

  // By default chart1 will be shown
  chart1Canvas.style.display = "block";
  document.getElementById("bttn").innerText = document.getElementById(
    `${id}`
  ).innerText;

  if (id == "chart1") {
    document.getElementById("bttn").innerText = document.getElementById(
      `${id}`
    ).innerText;
    chart1Canvas.style.display = "block";
    chart2Canvas.style.display = "none";
    chart3Canvas.style.display = "none";
  } else if (id == "chart2") {
    document.getElementById("bttn").innerText = document.getElementById(
      `${id}`
    ).innerText;
    chart1Canvas.style.display = "none";
    chart2Canvas.style.display = "block";
    chart3Canvas.style.display = "none";
  } else {
    document.getElementById("bttn").innerText = document.getElementById(
      `${id}`
    ).innerText;
    chart1Canvas.style.display = "none";
    chart2Canvas.style.display = "none";
    chart3Canvas.style.display = "block";
  }
  console.log(document.getElementById("Gno").innerText);
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

updateLowStockValue();

function extractCookie() {
  const cookieRow = document.cookie.split('; ').find(row => row.startsWith('cookie=='));
  return cookieRow ? cookieRow.split('==')[1] : '';
}

function updateLowStockValue() {
  // Fetch data from the API endpoint
  const cookie = extractCookie();

  fetch("http://localhost:8080/api/getGodwnCount", {
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${cookie}`
    },
    withCredentials: true
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      return response.text(); // Assuming the response is a plain text
    })
    .then((data) => {
      // Parse the data as a long integer
      const count = parseInt(data);
      if (!isNaN(count)) {
        // Update the value of the element with ID 'lowStock'
        const lowStockElement = document.getElementById("gdnCount");
        if (lowStockElement) {
          lowStockElement.innerText = count;
        } else {
          throw new Error('Element with ID "lowStock" not found');
        }
      } else {
        throw new Error("Invalid count value returned");
      }
    })
    .catch((error) => {
      // Handle errors, such as network issues or invalid response
      console.error("Error fetching or parsing data:", error);
    });
}

function updateTotalQuantity() {

  const cookie = extractCookie();

  fetch("http://localhost:8080/api/getAllProduct", {
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${cookie}`
    },
    withCredentials: true
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      return response.json();
    })
    .then((productList) => {
      let TQ = 0;
      for (const product of productList) {
        if (Array.isArray(product) && product.length >= 3) {
          TQ += product[2];
        }
      }
      const TQE = document.getElementById("TPP");
      if (TQE) {
        TQE.innerText = TQ;
      } else {
        throw new Error('Element with ID "TPP" not found');
      }
    })
    .catch((error) => {
      console.error("Error fetching or parsing data:", error);
    });
}

// Call the function to update the total quantity
updateTotalQuantity();

function gettotalRevenue() {

  const cookie = extractCookie();

  fetch("localhost:8080/api/getDeliveryOrders", {
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${cookie}`
    },
    withCredentials: true
  }).then((response) => {
    if (!response.ok) {
      throw new Error("Network response was not ok");
    }
    return response.json();
  }).then;
}

// const totalGodown = document.getElementById("gdnCount");

// function callUpdate(gdnCount) {
//   for (let i = 0; i < gdnCount; i++) {
//     updateGodownCapacity(i + 1);
//   }
// }

// function updateGodownCapacity(godownId) {
//   // Fetch data from the API endpoint for the given godownId
//   fetch("http://localhost:8080/api/getCapacity/${godownId}")
//       .then(response => {
//           if (!response.ok) {
//               throw new Error('Network response was not ok');
//           }
//           return response.json(); // Assuming the response is JSON
//       })
//       .then(data => {
//           // Update the value of the element with ID 'allItems' for the corresponding godown
//           const capacityElement = document.getElementById(godown${godownId}Capacity);
//           if (capacityElement) {
//               capacityElement.innerText = data.capacity;
//           } else {
//               throw new Error("Element with ID ${godownId} not found");
//           }
//       })
//       .catch(error => {
//           // Handle errors, such as network issues or invalid response
//           console.error("Error fetching or parsing data for godown ${godownId}:", error);
//       });
// }

totalOrderQuantity();

function totalOrderQuantity() {

  const cookie = extractCookie();

  fetch("http://localhost:8080/api/getDeliveryOrders", {
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${cookie}`
    },
    withCredentials: true
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      return response.json();
    })
    .then((deliveryOrders) => {
      let totalOQ = 0;
      let totalrevenue = 0;
      for (const order of deliveryOrders) {
        totalOQ += order.orderQuantity;
        totalrevenue += order.totalSellPrice;
      }
      const trty = document.getElementById("TRTY");
      const TOE = document.getElementById("TNOOR");
      if (trty) {
        trty.innerText = totalrevenue;
      } else {
        throw new Error("Element with ID TRTY not found");
      }
      if (TOE) {
        TOE.innerText = totalOQ;
      } else {
        throw new Error('Element with ID "TNOOR" not found');
      }
    })
    .catch((error) => {
      console.error("Error fetching or parsing data:", error);
    });
}

document.addEventListener("DOMContentLoaded", () => {
  setActivePage();
});
