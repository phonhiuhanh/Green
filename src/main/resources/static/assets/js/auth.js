$(document).ready(async () => {
    await axios.post(`/api/auth/get-username`)
        .then(res => {
            console.log(res.data);
            $('.username').text('Xin chào, ' + res.data);
        })
        .catch(err => {
            console.log(err);
        })
});

const logout = () => {
    confirm('Bạn có chắc chắn muốn đăng xuất?') && (window.location.href = '/logout');
}

document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.dropdown-toggle').forEach(function(toggle) {
        toggle.addEventListener('click', function(event) {
            event.preventDefault();
            const dropdownMenu = toggle.nextElementSibling;
            dropdownMenu.classList.toggle('show');
        });
    });
});