const baseURL = SERVER_URL;

function capitalizeFirstLetter(str) {
    return str.toLowerCase().replace(/(^|\s)\S/g, function (match) {
      return match.toUpperCase();
    });
  }

  function extractCookie() {
    const cookieRow = document.cookie.split('; ').find(row => row.startsWith('cookie=='));
    return cookieRow ? cookieRow.split('==')[1] : '';
}


function getUserData() {
    const user = localStorage.getItem('user');
    const userData = JSON.parse(user);
    const godownHeadId = parseInt(userData.godownHeadId);
    const cookie = extractCookie();
    
    axios.get(`${baseURL}/api/getGodownHead/${godownHeadId}`, {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${cookie}`
            
        }
    })
    .then( response => {

        const userData = response.data;
        const userDataWithoutPassword = { ...userData };
        delete userDataWithoutPassword.password;
        

        localStorage.setItem('userData', JSON.stringify(userDataWithoutPassword));

        document.getElementById('name1').innerText = capitalizeFirstLetter(userData.godownHeadName);
        document.getElementById('designation1').innerText = capitalizeFirstLetter(userData.role);
        document.getElementById('email').innerText = userData.email;
        
        document.getElementById('empid').value = userData.godownHeadId;
        document.getElementById('godownId').value = userData.godownId;
        document.getElementById('name2').value = capitalizeFirstLetter(userData.godownHeadName);
        document.getElementById('designation2').value = capitalizeFirstLetter(userData.role);
        document.getElementById('mobileNo').value = userData.godownheadNo;
        document.getElementById('username2').value = userData.username;
        const address = userData.address;
        if (address && address.trim() !== "") {
            document.getElementById('address').value = capitalizeFirstLetter(address);
        }
    })
    .catch( error => {
        console.error('Error fetching user data:', error);
        // alert('Error fetching user data');
        Notify('Error fetching user data', 'danger');
    });

}

function updatePassword() {
    const username = JSON.parse(localStorage.getItem('user')).username;
    const cookie = extractCookie();

    const changePasswordModal = document.getElementById('changePasswordModal');
    changePasswordModal.addEventListener('submit', async (e) => {
        e.preventDefault();

        const formData = new FormData(e.target);
        const oldPassword = formData.get('oldPassword');
        const newPassword = formData.get('newPassword');

        const requestData = {
            username: username,
            oldPassword: oldPassword,
            newPassword: newPassword
        };

        try {
            const response = await axios.put(`${baseURL}/api/updatePassword`, requestData, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + cookie
                }
            });
            const responseData = response.data;
            Notify(responseData, 'success');
            setTimeout(() => {
                window.location.href = 'Profile.html';
            }, 500);

        } catch (error) {
            console.error('Error updating password:', error); 
            Notify('Error updating password: '+ error.response.data, 'danger');
        }
    })
}






