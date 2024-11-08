let currentPage = 1;
const recordsPerPage = 10;
let totalPages = 1;

function updatePaginationControls() {
    document.getElementById('firstPage').disabled = currentPage === 1;
    document.getElementById('prevPage').disabled = currentPage === 1;
    document.getElementById('nextPage').disabled = currentPage === totalPages;
    document.getElementById('lastPage').disabled = currentPage === totalPages;
}

function fetchAttendances(page) {
    fetch(`/api/attendances?page=${page - 1}&size=${recordsPerPage}`)
        .then(response => response.json())
        .then(data => {
            const tbody = document.getElementById('attendanceTableBody');
            tbody.innerHTML = '';
            totalPages = data.totalPages;
            data.content.forEach(attendance => {
                const row = `<tr>
                        <td>${attendance.attendanceID}</td>
                        <td>${attendance.user.username}</td>
                        <td>${attendance.work_date}</td>
                        <td>${attendance.check_in_time}</td>
                        <td>${attendance.check_out_time}</td>
                        <td>${formatCurrency(attendance.daily_wage) + '₫'}</td>
                        <td>
                            <button class="btn btn-sm btn-primary" onclick="editAttendance(${attendance.attendanceID})">Sửa</button>
                            <button class="btn btn-sm btn-danger" onclick="deleteAttendance(${attendance.attendanceID})">Xóa</button>
                        </td>
                    </tr>`;
                tbody.insertAdjacentHTML('beforeend', row);
            });
            document.getElementById('currentPage').innerText = `Trang ${page} / ${totalPages}`;
            updatePaginationControls();
        });
}

document.getElementById('firstPage').addEventListener('click', () => fetchAttendances(1));
document.getElementById('prevPage').addEventListener('click', () => fetchAttendances(currentPage > 1 ? --currentPage
    : 1));
document.getElementById('nextPage').addEventListener('click', () => fetchAttendances(++currentPage));
document.getElementById('lastPage').addEventListener('click', () => fetchAttendances(totalPages));

fetchAttendances(currentPage);


document.addEventListener('DOMContentLoaded', () => {
    fetch('/api/users')
        .then(response => response.json())
        .then(users => {
            const userSelect = document.getElementById('user');
            users.forEach(user => {
                console.log(user);
                const option = document.createElement('option');
                option.value = user.userID;
                option.text = `${user.userID} - ${user.username}`;
                userSelect.add(option);
            });
        });

    document.getElementById('addAttendanceForm').addEventListener('submit', event => {
        event.preventDefault();
        const form = event.target;
        const data = {
            user: {userID: form.user.value},
            work_date: form.work_date.value,
            check_in_time: form.check_in_time.value,
            check_out_time: form.check_out_time.value,
            daily_wage: form.daily_wage.value
        };
        fetch('/api/attendances/manager', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(data)
        })
            .then(response => {
                if (response.ok) {
                    Swal.fire('Thành công', 'Đã thêm chấm công', 'success')
                        .then(() => {
                            $('#addAttendanceModal').modal('hide');
                            fetchAttendances(currentPage);
                        });
                } else {
                    Swal.fire('Lỗi', 'Không thể thêm chấm công', 'error');
                }
            });
    });
});


function editAttendance(id) {
    fetch(`/api/attendances/${id}`)
        .then(response => response.json())
        .then(attendance => {
            document.getElementById('edit_attendanceID').value = attendance.attendanceID;
            document.getElementById('edit_work_date').value = attendance.work_date;
            document.getElementById('edit_check_in_time').value = attendance.check_in_time;
            document.getElementById('edit_check_out_time').value = attendance.check_out_time;
            document.getElementById('edit_daily_wage').value = attendance.daily_wage;
            // document.getElementById('edit_user').value = attendance.user.userID;
            $('#editAttendanceModal').modal('show');
        });
}

document.getElementById('editAttendanceForm').addEventListener('submit', event => {
    event.preventDefault();
    const form = event.target;
    const id = document.getElementById('edit_attendanceID').value;
    const data = {
        // user: {userID: form.edit_user.value},
        work_date: form.edit_work_date.value,
        check_in_time: form.edit_check_in_time.value,
        check_out_time: form.edit_check_out_time.value,
        daily_wage: form.edit_daily_wage.value
    };
    fetch(`/api/attendances/manager/${id}`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    })
        .then(response => {
            if (response.ok) {
                Swal.fire('Thành công', 'Đã cập nhật chấm công', 'success')
                    .then(() => {
                        $('#editAttendanceModal').modal('hide');
                        fetchAttendances(currentPage);
                    });
            } else {
                Swal.fire('Lỗi', 'Không thể cập nhật chấm công', 'error');
            }
        });
});


function deleteAttendance(id) {
    Swal.fire({
        title: 'Bạn có chắc chắn?',
        text: "Bạn sẽ không thể hoàn tác hành động này!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Xóa'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`/api/attendances/manager/${id}`, {
                method: 'DELETE'
            })
                .then(response => {
                    if (response.ok) {
                        Swal.fire('Đã xóa!', 'Chấm công đã được xóa.', 'success')
                            .then(() => fetchAttendances(currentPage));
                    } else {
                        Swal.fire('Lỗi', 'Không thể xóa chấm công', 'error');
                    }
                });
        }
    });
}
