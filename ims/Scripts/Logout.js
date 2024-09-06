
function NotifyLogout(message, type) {
    const toastLiveExample = document.getElementById('liveToast');
    const toastBody = toastLiveExample.querySelector('.toast-body');
    
    toastBody.innerText = message;
    toastLiveExample.classList.remove('bg-success', 'bg-danger', 'bg-warning', 'bg-info');

    if (type === 'success') {
        toastLiveExample.classList.add('bg-success');
    } else if (type === 'danger') {
        toastLiveExample.classList.add('bg-danger');
    } else if (type === 'warning') {
        toastLiveExample.classList.add('bg-warning');
    }

    const toastBootstrap = new bootstrap.Toast(toastLiveExample);
    toastBootstrap.show();
}

function extractCookie() {
    const cookieRow = document.cookie.split('; ').find(row => row.startsWith('cookie=='));
    return cookieRow ? cookieRow.split('==')[1] : '';
}

function logout(buttonId) {
    document.getElementById(buttonId).addEventListener("click", function(e) {
        e.preventDefault();
        const cookie = extractCookie();
        axios.post(`${baseURL}/api/logout`, null, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${cookie}`
            }
        })
        .then(response => {
            if (response.status === 200) {
                localStorage.clear();
                document.cookie = `cookie==${cookie}; max-age=-99999999;`;
                NotifyLogout('You have been logged out successfully.', 'success');
                setTimeout(() => {
                    window.location.href = "login.html";
                }, 500);
            } else {
                console.error('Logout error:', response.data);
                // alert('Logout failed. Please try again.');
                NotifyLogout('You have been logged out successfully.', 'success');
                setTimeout(() => {
                    window.location.href = "login.html";
                }, 500);
            }
        })
        .catch(error => {
            console.error('Logout error:', error);
            // alert('An unexpected error occurred. Please try again later.');
            NotifyLogout('You have been logged out successfully.', 'success');
                setTimeout(() => {
                    window.location.href = "login.html";
                }, 500);
        });
    });
}

document.addEventListener('DOMContentLoaded', function() {
    logout('logout-btn-1');
    // logout('logout-btn-2');
});
