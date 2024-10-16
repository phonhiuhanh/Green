// login and register
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
