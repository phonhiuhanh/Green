// Gửi mã OTP
function sendOtp() {
    const emailOrPhone = document.getElementById('emailOrPhone').value;
    const sendOtpButton = document.getElementById('sendOtpButton');
    $.post(`/api/customers/p/forgot-password/request?email=${emailOrPhone}`, function () {
        $('#otpMessage').show();
        sendOtpButton.disabled = true;
        setTimeout(() => {
            sendOtpButton.disabled = false;
        }, 5000);
    }).fail(function () {
        Swal.fire({
            title: 'Lỗi',
            text: 'Gửi mã OTP thất bại. Vui lòng thử lại.',
            icon: 'error',
            confirmButtonText: 'OK'
        });
    });
}

// Xác nhận đặt lại mật khẩu
function confirmPasswordReset() {
    const emailOrPhone = document.getElementById('emailOrPhone').value;
    const otp = document.getElementById('otp').value;
    const newPassword = document.getElementById('newPassword').value;
    const confirmButton = document.getElementById('confirmButton');

    // Kiểm tra nếu trường mật khẩu mới bị bỏ trống
    if (!newPassword.trim()) {
        Swal.fire({
            title: 'Lỗi',
            text: 'Vui lòng nhập mật khẩu mới.',
            icon: 'error',
            confirmButtonText: 'OK'
        });
        return; // Dừng xử lý nếu mật khẩu mới rỗng
    }

    // Vô hiệu hóa nút trong khi đang xử lý
    confirmButton.disabled = true;

    // Gửi yêu cầu POST để đặt lại mật khẩu
    $.post(`/api/customers/p/forgot-password/verify?email=${emailOrPhone}&otp=${otp}&newPassword=${newPassword}`, function () {
        Swal.fire({
            title: 'Thành công',
            text: 'Đặt lại mật khẩu thành công. Đang chuyển đến trang đăng nhập...',
            icon: 'success',
            confirmButtonText: 'OK'
        }).then(() => {
            window.location.href = '/login';
        });
    }).fail(function () {
        $('#errorMessage').show();
        confirmButton.disabled = false;
    });
}
