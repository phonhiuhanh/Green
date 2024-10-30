document.getElementById('registerForm').addEventListener('submit', async function (event) {
    event.preventDefault();

    // Clear previous error messages
    document.querySelectorAll('.error-message').forEach(el => el.textContent = '');

    // Validate form fields
    const fullName = document.getElementById('fullName').value.trim();
    const phone = document.getElementById('phone').value.trim();
    const address = document.getElementById('address').value.trim();
    const city = document.getElementById('city').value.trim();
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value.trim();

    let isValid = true;

    if (!fullName) {
        document.getElementById('fullNameError').textContent = 'Họ và tên không được để trống.';
        isValid = false;
    }

    if (!phone) {
        document.getElementById('phoneError').textContent = 'Số điện thoại không được để trống.';
        isValid = false;
    }

    if (!address) {
        document.getElementById('addressError').textContent = 'Địa chỉ không được để trống.';
        isValid = false;
    }

    if (!city) {
        document.getElementById('cityError').textContent = 'Thành phố không được để trống.';
        isValid = false;
    }

    if (!email) {
        document.getElementById('emailError').textContent = 'Email không được để trống.';
        isValid = false;
    }

    if (!password || password.length < 6) {
        document.getElementById('passwordError').textContent = 'Mật khẩu phải trên 6 ký tự.';
        isValid = false;
    }

    if (!isValid) {
        return;
    }

    // Disable the submit button
    const submitButton = document.querySelector('.btn-submit');
    submitButton.disabled = true;

    // Show waiting message
    alert('Vui lòng đợi khoảng 5 giây trong khi email xác nhận đang được gửi.');

    // Prepare the request body
    const customer = {
        username: fullName,
        phone: phone,
        address: address,
        city: city,
        email: email,
        password: password
    };

    try {
        // Send the request
        const response = await fetch('/api/customers/p/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(customer)
        });

        // Handle success
        if (response.ok) {
            localStorage.setItem('registeredEmail', email);
            window.location.href = '/login';
        } else {
            const errorData = await response.json();
            alert('Lỗi: ' + errorData.message);
            submitButton.disabled = false;
        }
    } catch (error) {
        // Handle error
        alert('Lỗi: ' + error.message);
        submitButton.disabled = false;
    }
});