function Notify(message, type) {
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
