// login and register
$(document).ready(() => {
    if (window.location.href.indexOf('/login?logout') > -1) {
        Swal.fire({
            title: 'Thành công',
            text: 'Đăng xuất thành công',
            icon: 'success',
            confirmButtonText: 'OK'
        });
    } else if (window.location.href.indexOf('/login?error') > -1) {
        Swal.fire({
            title: 'Lỗi',
            text: 'Sai tên đăng nhập hoặc mật khẩu',
            icon: 'error',
            confirmButtonText: 'OK'
        });
    }
});

function togglePassword() {
    const passwordField = document.getElementById('password');
    const toggleButton = document.getElementById('togglePassword');

    if (passwordField.type === 'password') {
        passwordField.type = 'text';
        toggleButton.innerHTML = '👁️'; // Mắt mở
    } else {
        passwordField.type = 'password';
        toggleButton.innerHTML = '👁️'; // Mắt nhắm
    }
}

//index