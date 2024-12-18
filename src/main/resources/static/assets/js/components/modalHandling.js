document.addEventListener('DOMContentLoaded', function () {
    // Lấy thông tin khách hàng hiện tại khi mở modal
    $('#profileModal').on('show.bs.modal', function () {
        $.get('/api/customers/get-current-customer', function (data) {
            $('#phone').val(data.phone);
            $('#email').val(data.email);
            $('#username').val(data.username);
            $('#address').val(data.address);
            $('#city').val(data.city);
        });
    });
});

function toggleEdit() {
    const isDisabled = $('#username').prop('disabled');
    $('#username, #address, #city').prop('disabled', !isDisabled);
    $('#saveButton').prop('disabled', isDisabled);
}

function saveChanges() {
    const profileData = {
        username: $('#username').val(),
        address: $('#address').val(),
        city: $('#city').val()
    };

    $.ajax({
        url: '/api/customers/update-profile',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(profileData),
        success: function () {
            Swal.fire({
                title: 'Thành công',
                text: 'Cập nhật thông tin thành công',
                icon: 'success',
                confirmButtonText: 'OK'
            }).then(() => handleCloseModal('#profileModal'));
        },
        error: function () {
            Swal.fire({
                title: 'Lỗi',
                text: 'Cập nhật thông tin thất bại. Vui lòng thử lại sau',
                icon: 'error',
                confirmButtonText: 'OK'
            });
        }
    });
}

function clearChangePasswordModal() {
    $('#currentPassword').val('');
    $('#newPassword').val('');
    $('#confirmPassword').val('');
}

function handleCloseModal(modalID) {
    $(modalID).modal('hide');
    $('.modal-backdrop').remove();
}

function changePassword() {
    const currentPassword = $('#currentPassword').val();
    const newPassword = $('#newPassword').val();
    const confirmPassword = $('#confirmPassword').val();

    if (newPassword !== confirmPassword) {
        Swal.fire({
            title: 'Lỗi',
            text: 'Mật khẩu mới và mật khẩu xác nhận phải giống nhau',
            icon: 'error',
            confirmButtonText: 'OK'
        });
        return;
    }

    if (newPassword.length < 6) {
        Swal.fire({
            title: 'Lỗi',
            text: 'Mật khẩu phải chứa ít nhất 6 ký tự',
            icon: 'error',
            confirmButtonText: 'OK'
        });
        return;
    }

    $('#confirmButton').prop('disabled', true);

    $.ajax({
        url: `/api/customers/update-password?oldPassword=${currentPassword}&newPassword=${newPassword}`,
        type: 'PUT',
        success: function () {
            Swal.fire({
                title: 'Thành công',
                text: 'Mật khẩu đã được thay đổi thành công',
                icon: 'success',
                confirmButtonText: 'OK'
            }).then(() => {
                clearChangePasswordModal();
                $('#confirmButton').prop('disabled', false);
                handleCloseModal('#changePasswordModal');
            });
        },
        error: function () {
            Swal.fire({
                title: 'Lỗi',
                text: 'Đổi mật khẩu thất bại. Vui lòng kiểm tra lại mật khẩu hiện tại hoặc thử lại sau',
                icon: 'error',
                confirmButtonText: 'OK'
            });
            $('#confirmButton').prop('disabled', false);
        }
    });
}

function toggleDropdown(button) {
    const dropdownMenu = button.nextElementSibling;
    dropdownMenu.classList.toggle('show');
}

function removeDropdownShow(button) {
    const dropdownMenu = button.nextElementSibling;
    dropdownMenu.classList.remove('show');
}

const myModal = document.getElementById('changePasswordModal')
const myInput = document.getElementById('currentPassword')

myModal.addEventListener('shown.bs.modal', () => {
    myInput.focus()
})