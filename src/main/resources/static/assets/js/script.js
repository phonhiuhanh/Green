// login and register
$(document).ready(() => {
    if (window.location.href.indexOf('/login?logout') > -1) {
        alert('Đăng xuất thành công');
    } else if (window.location.href.indexOf('/login?error') > -1) {
        alert('Sai tên đăng nhập hoặc mật khẩu');
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
