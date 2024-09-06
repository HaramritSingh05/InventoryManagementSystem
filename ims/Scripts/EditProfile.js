const baseURL = SERVER_URL;

function extractCookie() {
    const cookieRow = document.cookie.split('; ').find(row => row.startsWith('cookie=='));
    return cookieRow ? cookieRow.split('==')[1] : '';
}

function capitalizeFirstLetter(str) {
    return str.toLowerCase().replace(/(^|\s)\S/g, function (match) {
      return match.toUpperCase();
    });
  }

  function breakNameIntoFirstAndLast(godownHeadName) {
    const nameParts = godownHeadName.split(' ');
    const firstName = capitalizeFirstLetter(nameParts[0]);
    const lastName = capitalizeFirstLetter(nameParts.slice(1).join(' '));
    return { firstName, lastName };
}

function breakFullAddress(fullAddress) {
    const parts = fullAddress.split(',').map(part => part.trim());
    const address = parts.slice(0, -3).join(', '); 
    let city = parts[parts.length - 3].trim();
    let state = parts[parts.length - 2].trim();
    let country = parts[parts.length - 1].trim();

    return { address, city, state, country };
}

function setProfileDetails() {
    var userData = JSON.parse(localStorage.getItem("userData"));

    if (userData) {
        if (userData.address) {
            const { address, city, state, country } = breakFullAddress(userData.address);

            document.getElementById("address").value = capitalizeFirstLetter(address)
            document.getElementById("city").value = capitalizeFirstLetter(city)
            document.getElementById("state").value = capitalizeFirstLetter(state)
            document.getElementById("country").value = capitalizeFirstLetter(country)
        } else {
            document.getElementById("address").value = "";
            document.getElementById("city").value = "";
            document.getElementById("state").value = "";
            document.getElementById("country").value = "";
        }
        document.getElementById("name1").innerHTML = capitalizeFirstLetter(userData.godownHeadName) || "";
        document.getElementById("designation1").innerHTML = capitalizeFirstLetter(userData.role) || "";
        document.getElementById("email").innerHTML = userData.email || "";

        document.getElementById("name").value = breakNameIntoFirstAndLast(userData.godownHeadName).firstName || "";
        document.getElementById("surname").value = breakNameIntoFirstAndLast(userData.godownHeadName).lastName || "";
        document.getElementById("username").value = userData.username || "";

    } else {
        console.log("userData is null or undefined");
    }
}


function validateFirstName(nameField) {
    var nameRegex = /^[A-Za-z]+$/;
    if (!nameRegex.test(nameField.value.trim())) {
        nameField.classList.add('is-invalid');
        nameField.classList.remove('is-valid');
        return false;
    } else {
        nameField.classList.remove('is-invalid');
        nameField.classList.add('is-valid');
        return true;
    }
}

function validateLastName(surnameField) {

    if (surnameField.value.trim() !== "") {
        var nameRegex = /^[A-Za-z]+$/;
        if (!nameRegex.test(surnameField.value.trim())) {
            surnameField.classList.add('is-invalid');
            surnameField.classList.remove('is-valid');
            return false;
        } else {
            surnameField.classList.remove('is-invalid');
            surnameField.classList.add('is-valid');
            return true;
        }
    } else {

        surnameField.classList.remove('is-invalid');
        surnameField.classList.add('is-valid');
        return true;
    }
}

function validateUsername(usernameField) {
    var usernameRegex = /^[a-zA-Z][a-zA-Z0-9]*$/;
    if (!usernameRegex.test(usernameField.value.trim())) {
        usernameField.classList.add('is-invalid');
        usernameField.classList.remove('is-valid');
        return false;
    } else {
        usernameField.classList.remove('is-invalid');
        usernameField.classList.add('is-valid');
        return true;
    }
}

function validateAddress(addressField) {
    var addressRegex = /^[a-zA-Z0-9\s,'-]*$/;
    if (!addressRegex.test(addressField.value.trim())) {
        addressField.classList.add('is-invalid');
        addressField.classList.remove('is-valid');
        return false;
    } else {
        addressField.classList.remove('is-invalid');
        addressField.classList.add('is-valid');
        return true;
    }
}

function validateCity(cityField) {
    if (cityField.value.trim() !== "") {
        var cityRegex = /^[A-Za-z\s]+$/;
        if (!cityRegex.test(cityField.value.trim())) {
            cityField.classList.add('is-invalid');
            cityField.classList.remove('is-valid');
            return false;
        } else {
            cityField.classList.remove('is-invalid');
            cityField.classList.add('is-valid');
            return true;
        }
    } else {

        cityField.classList.remove('is-invalid');
        cityField.classList.add('is-valid');
        return true;
    }
}

function validateState(stateField) {
    if (stateField.value.trim() !== "") {
        var stateRegex = /^[A-Za-z\s]+$/;
        if (!stateRegex.test(stateField.value.trim())) {
            stateField.classList.add('is-invalid');
            stateField.classList.remove('is-valid');
            return false;
        } else {
            stateField.classList.remove('is-invalid');
            stateField.classList.add('is-valid');
            return true;
        }
    } else {
        stateField.classList.remove('is-invalid');
        stateField.classList.add('is-valid');
        return true;
    }
}

function validateCountry(countryField) {
    if (countryField.value.trim() !== "") {
        var countryRegex = /^[A-Za-z\s]+$/;
        if (!countryRegex.test(countryField.value.trim())) {
            countryField.classList.add('is-invalid');
            countryField.classList.remove('is-valid');
            return false;
        } else {
            countryField.classList.remove('is-invalid');
            countryField.classList.add('is-valid');
            return true;
        }
    } else {

        countryField.classList.remove('is-invalid');
        countryField.classList.add('is-valid');
        return true;
    }
}


function editProfile() {
    const cookie = extractCookie();

    document.getElementById("editProfile").addEventListener("submit", function(e) {
        e.preventDefault();
        const formData = new FormData();
        
        const godownHeadName = `${e.target.elements.name.value.toLowerCase()} ${e.target.elements.surname.value.toLowerCase()}`;
        
        const username = `${e.target.elements.username.value.toLowerCase()}`;

        const address = `${e.target.elements.address.value.toLowerCase()}, ${e.target.elements.city.value.toLowerCase()}, ${e.target.elements.state.value.toLowerCase()}, ${e.target.elements.country.value.toLowerCase()}`;


        const isFirstnameValid = validateFirstName(e.target.elements.name);
        const isSurnameValid = validateLastName(e.target.elements.surname);
        const isUsernameValid = validateUsername(e.target.elements.username);
        const isAddressValid = validateAddress(e.target.elements.address);
        const isCityValid = validateCity(e.target.elements.city);
        const isStateValid = validateState(e.target.elements.state);
        const isCountryValid = validateCountry(e.target.elements.country);

        
        if (isFirstnameValid && isSurnameValid && isUsernameValid && isAddressValid && isCityValid && isStateValid && isCountryValid) {
            const godownHeadId = JSON.parse(localStorage.getItem("userData")).godownHeadId;
            formData.append("godownHeadName", godownHeadName);
            formData.append("username", username);
            formData.append("address", address);
            formData.append("godownHeadId", godownHeadId);

            axios.put(`${baseURL}/api/updateGodownHead`, formData, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${cookie}`
                }
            }).then(response => {
                Notify('Profile updated successfully', 'success');
                setTimeout(() => {
                    window.location.href = "Profile.html";
                }, 500);
            }).catch(error => {
                console.error('Error updating profile:', error);
                Notify('Failed to update profile: ' + error.response.data.message, 'danger');
            });
        } else {
            e.stopPropagation();
            console.log("Please fill out the form correctly.");
        }
    });
}


document.addEventListener("DOMContentLoaded", function() {
    setProfileDetails();
    editProfile();
});


