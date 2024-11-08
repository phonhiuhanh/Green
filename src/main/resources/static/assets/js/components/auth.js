$(document).ready(async () => {
    $.post(`/api/auth/get-username`)
    .done(res => {
        // console.log(res);
        $('.username').text('Xin chào, ' + res);
    })
    .fail(err => {
        // console.log(err);
    });
});

const logout = () => {
    Swal.fire({
        title: 'Bạn có chắc chắn muốn đăng xuất?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Đăng xuất',
        cancelButtonText: 'Hủy'
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = '/logout';
        }
    });
}