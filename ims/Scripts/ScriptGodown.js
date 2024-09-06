const baseURL = SERVER_URL;

function extractCookie() {
  const cookieRow = document.cookie
    .split("; ")
    .find((row) => row.startsWith("cookie=="));
  return cookieRow ? cookieRow.split("==")[1] : "";
}

document.addEventListener("DOMContentLoaded", function () {
  const cookie = extractCookie();
  console.log(cookie);

  fetch(`${baseURL}/api/findGodownsByGodownHead`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${cookie}`,
    },
    withCredentials: true,
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      return response.json();
    })
    .then((godownDetails) => {
      const tableBody = document.querySelector("#myTable tbody");

      // Clear existing rows
      tableBody.innerHTML = "";

      // Populate the table
      godownDetails.forEach((godown) => {
        const row = document.createElement("tr");
        row.innerHTML = `
                    <td>${godown[0]}</td>
                    <td>${godown[1]}</td>
                    <td>${godown[2]}</td>
                    <td>${godown[3]}</td>
                    <td>${godown[4]}</td>
                    <td>${godown[5]}</td>
                    <td>${godown[6]}</td>
                    <td>${godown[7]}</td>`;
        tableBody.appendChild(row);
      });
      $("#myTable").DataTable();
    })
    .catch((error) => {
      console.error("Error fetching data:", error);
    });
});
