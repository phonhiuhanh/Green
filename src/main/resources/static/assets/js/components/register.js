document.getElementById('registerForm').addEventListener('submit', async function (event) {
    event.preventDefault();

    // Xóa các thông báo lỗi trước đó
    document.querySelectorAll('.error-message').forEach(el => el.textContent = '');

    // Kiểm tra các trường trong form
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

    // Vô hiệu hóa nút submit
    const submitButton = document.querySelector('.btn-submit');
    submitButton.disabled = true;

    // Hiển thị thông báo ch���
    Swal.fire({
        title: 'Vui lòng đợi',
        text: 'Vui lòng đợi khoảng 5 giây trong khi email xác nhận đang được gửi.',
        icon: 'info',
        confirmButtonText: 'OK'
    });

    // Chuẩn bị dữ liệu yêu cầu
    const customer = {
        username: fullName,
        phone: phone,
        address: address,
        city: city,
        email: email,
        password: password
    };

    try {
        // Gửi yêu cầu
        const response = await fetch('/api/customers/p/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(customer)
        });

        // Xử lý thành công
        if (response.ok) {
            localStorage.setItem('registeredEmail', email);
            window.location.href = '/login';
        } else {
            const errorData = await response.json();
            Swal.fire({
                title: 'Lỗi',
                text: 'Lỗi: ' + errorData.message,
                icon: 'error',
                confirmButtonText: 'OK'
            });
            submitButton.disabled = false;
        }
    } catch (error) {
        // Xử lý lỗi
        Swal.fire({
            title: 'Lỗi',
            text: 'Lỗi: ' + error.message,
            icon: 'error',
            confirmButtonText: 'OK'
        });
        submitButton.disabled = false;
    }
});