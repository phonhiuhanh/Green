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
    confirm('Bạn có chắc chắn muốn đăng xuất?') && (window.location.href = '/logout');
}