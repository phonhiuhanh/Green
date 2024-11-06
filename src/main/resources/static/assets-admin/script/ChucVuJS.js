// $(document).ready(function() {
//     async function getAllChucVu() {
//         return new Promise((resolve, reject) => {
//             $.ajax({
//                 url: '/api/chuc-vu',
//                 method: 'GET',
//                 success: function(response) {
//                     console.log(response);
//                     var chucVuContainer = $('#tableChucVu tbody');
//                     chucVuContainer.empty();
//                     response.forEach(function(data) {
//                         var row = $('<tr>');
//                         row.append(`<td>${data.maChucVu}</td>`);
//                         row.append(`<td>${data.tenChucVu}</td>`);
//                         row.append(`<td>${data.mieuTa}</td>`);
//                         row.append('<td>' +
//                             '<a href="/Admin/Chức-vụ?maCV=' + data.maChucVu + '" class="btn btn-sm btn-primary">Sửa</a>\n' +
//                             '<btn class="btn btn-sm btn-danger deleteChucVu">Xóa</btn>\n' +
//                             '</td>');
//                         row.append('</tr>');
//                         chucVuContainer.append(row);
//                         $('.deleteChucVu').click(function(){
//                             var maCV = $(this).parent().parent().find('td:first-child').text();
//                             if(confirm('Bạn có chắc muốn xóa chức vụ này?')) {
//                                 deleteChucVu(maCV).then(function(response) {
//                                     alert('Xóa thành công!');
//                                     getAllChucVu();
//                                 }).catch(function(error) {
//                                     alert('Xóa thất bại!');
//                                 });
//                             }
//                         })
//                     });
//                     resolve(response);
//                 },
//                 error: function(error) {
//                     console.error('Error fetching positions:', error);
//                     reject(error);
//                 }
//             });
//         });
//     }
//
//     async function deleteChucVu(maCV) {
//         return new Promise((resolve, reject)=>
//             $.ajax({
//                 url: '/api/chuc-vu/delete?maCV=' + maCV,
//                 method: 'DELETE',
//                 success: function(response) {
//                     console.log(response);
//                     resolve(response);
//                 },
//                 error: function(error) {
//                     console.error('Error deleting ChucVu:', error);
//                     reject(error);
//                 }
//             })
//         )
//     }
//
//     async function getOneChucVu()  {
//         try {
//             var chucVu =  window.location.href;
//             var url =  new URL(currentURL);
//             var manv =  url.searchParams.get("maCV");
//             const response = await $.ajax({
//                 url: '/api/chuc-vu/one?maCV=',
//                 method: 'GET'
//             });
//             alert(response)
//         }catch (error) {
//             // Handle errors here
//             console.error('Error fetching ChucVu:', error);
//         }
//
//
//     }
//
//     async function addChucVu() {
//         try {
//             const tenChucVu = $('#tenChucVu').val();
//             const descChucVu = $('#descChucVu').val();
//             const data = {
//                 tenChucVu: tenChucVu,
//                 mieuTa: descChucVu,
//             };
//
//             await $.ajax({
//                 url: '/api/chuc-vu/add',
//                 method: 'POST',
//                 contentType: 'application/json',
//                 data: JSON.stringify(data),
//             });
//             $('#tenChucVu').val("");
//             $('#descChucVu').val("");
//             await getAllChucVu();
//             $('#btnChucVuModalThem').modal('hide');l
//         } catch (error) {
//             console.error('Error in addChucVu:', error);
//         }
//     }
//
//     $('#btnAddChucVu').click(function(e) {
//         e.preventDefault();
//         addChucVu();
//     });
//
//     $('#btnUpdateChucVu').click(function(e) {
//         e.preventDefault();
//         var href = window.location.href;
//         var url = new URL(href);
//         var maCV = url.searchParams.get("maCV");
//         const tenChucVu = $('#tenChucVuUpdate').val();
//         const descChucVu = $('#descChucVuUpdate').val();
//         const data = {
//             maChucVu: maCV,
//             tenChucVu: tenChucVu,
//             mieuTa: descChucVu,
//         };
//
//         $.ajax({
//             url: '/api/chuc-vu/update',
//             method: 'PUT',
//             contentType: 'application/json',
//             data: JSON.stringify(data),
//             success: function(response) {
//                 alert(response);
//                 window.location.href = "/Admin/Quản-lý-chức-vụ"; // Điều hướng đến trang quản lý chức vụ
//             },
//             error: function(error) {
//                 alert('Cập nhật thất bại!');
//                 console.error('Error updating position:', error);
//             }
//         });
//
//     });
//     getAllChucVu();
// });
$(document).ready(function () {
    // Populate role dropdown for add and update modals
    $.get('/api/roles', function (data) {
        const roleSelect = $('#roleSelect, #updateRoleSelect');
        roleSelect.empty();
        data.forEach(role => {
            roleSelect.append(new Option(role.roleName, role.roleID));
        });
    });

    // Handle form submission for adding user
    $('#btnAddUser').click(function (e) {
        e.preventDefault();
        const formData = {
            userID: 0,
            username: $('#username').val(),
            password: $('#password').val(),
            email: $('#email').val(),
            phone: $('#phone').val(),
            address: $('#address').val(),
            city: $('#city').val(),
            role: { roleID: $('#roleSelect').val() }
        };

        $.ajax({
            url: '/api/users/manager',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function (response) {
                alert('User added successfully!');
                location.reload();
            },
            error: function (error) {
                console.error('Error adding user:', error);
            }
        });
    });

    // Handle form submission for updating user
    $('#btnUpdateUser').click(function (e) {
        e.preventDefault();
        const userID = $('#updateUserID').val();
        const formData = {
            userID: userID,
            username: $('#updateUsername').val(),
            password: $('#updatePassword').val(),
            email: $('#updateEmail').val(),
            phone: $('#updatePhone').val(),
            address: $('#updateAddress').val(),
            city: $('#updateCity').val(),
            role: { roleID: $('#updateRoleSelect').val() }
        };

        $.ajax({
            url: `/api/users/manager/${userID}`,
            method: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function (response) {
                alert('User updated successfully!');
                location.reload();
            },
            error: function (error) {
                console.error('Error updating user:', error);
            }
        });
    });

    // Populate update modal with user data
    $('.btn-edit-user').click(function () {
        const userID = $(this).data('id');
        $.get(`/api/users/${userID}`, function (data) {
            $('#updateUserID').val(data.userID);
            $('#updateUsername').val(data.username);
            // $('#updatePassword').val(''); // Password should be empty for security reasons
            $('#updateEmail').val(data.email);
            $('#updatePhone').val(data.phone);
            $('#updateAddress').val(data.address);
            $('#updateCity').val(data.city);
            $('#updateRoleSelect').val(data.role.roleID);
            $('#updateUserModal').modal('show');
        });
    });

    $('.btn-delete-user').click(function () {
        const userID = $(this).data('id');
        if (confirm('Are you sure you want to delete this user?')) {
            $.ajax({
                url: `/api/users/manager/${userID}`,
                method: 'DELETE',
                success: function (response) {
                    alert('User deleted successfully!');
                    location.reload();
                },
                error: function (error) {
                    console.error('Error deleting user:', error);
                }
            });
        }
    });
});
// $(document).ready(function () {
//     // Populate role dropdown
//     $.get('/api/roles', function (data) {
//         const roleSelect = $('#roleSelect');
//         roleSelect.empty();
//         data.forEach(role => {
//             roleSelect.append(new Option(role.roleName, role.roleID));
//         });
//     });
//
//     // Handle form submission
//     $('#btnAddUser').click(function (e) {
//         e.preventDefault();
//         const formData = {
//             userID: 0,
//             username: $('#username').val(),
//             password: $('#password').val(),
//             email: $('#email').val(),
//             phone: $('#phone').val(),
//             address: $('#address').val(),
//             city: $('#city').val(),
//             role: { roleID: $('#roleSelect').val() }
//         };
//
//         console.log(formData);
//
//         $.ajax({
//             url: '/api/users/manager',
//             method: 'POST',
//             contentType: 'application/json',
//             data: JSON.stringify(formData),
//             dataType: 'json',
//             success: function (response) {
//                 alert('User added successfully!');
//                 location.reload();
//             },
//             error: function (error) {
//                 console.error('Error adding user:', error);
//             }
//         });
//     });
// });