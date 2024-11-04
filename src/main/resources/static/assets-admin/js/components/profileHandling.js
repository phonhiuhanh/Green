document.addEventListener('DOMContentLoaded', function () {
    // Fetch current user data on modal open
    $('#profileModal').on('show.bs.modal', function () {
        $.get('/api/users/get-current-user', function (data) {
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
        url: '/api/users/update-profile',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(profileData),
        success: function () {
            alert('Cập nhật thông tin thành công');
            $('#profileModal').modal('hide');
        },
        error: function () {
            alert('Cập nhật thông tin thất bại. Vui lòng thử lại sau');
        }
    });
}

function changePassword() {
    const currentPassword = $('#currentPassword').val();
    const newPassword = $('#newPassword').val();
    const confirmPassword = $('#confirmPassword').val();

    if (newPassword !== confirmPassword) {
        alert('Mật khẩu mới và mật khẩu xác nhận phải giống nhau');
        return;
    }

    if (newPassword.length < 6) {
        alert('Mật khẩu phải chứa ít nhất 6 ký tự');
        return;
    }

    $('#confirmButton').prop('disabled', true);

    $.ajax({
        url: `/api/users/update-password?oldPassword=${currentPassword}&newPassword=${newPassword}`,
        type: 'PUT',
        success: function () {
            alert('Mật khẩu đã được thay đổi thành công');
            $('#changePasswordModal').modal('hide');
        },
        error: function () {
            alert('Đổi mật khẩu thất bại. Vui lòng kiểm tra lại mật khẩu hiện tại hoặc thử lại sau');
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