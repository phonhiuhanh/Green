let products = [];
const itemsPerPage = 10;
let currentPage = 1;

function displayPage(page) {
    const start = (page - 1) * itemsPerPage;
    const end = start + itemsPerPage;
    const paginatedItems = products.slice(start, end);

    const tableBody = document.getElementById('productTableBody');
    tableBody.innerHTML = '';

    paginatedItems.forEach(product => {
        const row = document.createElement('tr');
        const thumbnail = product.images?.find(image => image.isMain) || "";
        row.innerHTML = `
                <td>${product.productID}</td>
                <td><img alt="Hình ảnh" style="max-width: 8rem;max-height: 6rem;" src="${thumbnail?.imageURL}"></td>
                <td>${product.name}</td>
                <td>${formatCurrency(product.giacu * (1 - product.giamgia / 100))}</td>
                <td>${product.giamgia}</td>
                <td>${formatCurrency(product.giacu)}</td>
                <td>
                    <button class="btn btn-sm btn-primary edit-product" data-id="${product.productID}" data-toggle="modal-update">Sửa</button>
                    <button class="btn btn-sm btn-danger delete-product" data-id="${product.productID}">Xóa</button>
                </td>
            `;
        tableBody.appendChild(row);
    });

    document.getElementById('pageInfo').innerText = `Trang ${currentPage} trên ${Math.ceil(products.length / itemsPerPage)}`;
}

document.getElementById('firstPage').addEventListener('click', () => {
    currentPage = 1;
    displayPage(currentPage);
});

document.getElementById('prevPage').addEventListener('click', () => {
    if (currentPage > 1) {
        currentPage--;
        displayPage(currentPage);
    }
});

document.getElementById('nextPage').addEventListener('click', () => {
    if (currentPage < Math.ceil(products.length / itemsPerPage)) {
        currentPage++;
        displayPage(currentPage);
    }
});

document.getElementById('lastPage').addEventListener('click', () => {
    currentPage = Math.ceil(products.length / itemsPerPage);
    displayPage(currentPage);
});

$(document).ready(function () {
    // Fetch products and categories as before

    // Event listener for "Sửa" button
    $(document).on('click', '.edit-product', function () {
        const productID = $(this).data('id');
        const product = products.find(p => p.productID === productID);
        if (product) {
            $('#updateProductID').val(product.productID);
            $('#updateName').val(product.name);
            $('#updateGiacu').val(product.giacu);
            $('#updateGiamgia').val(product.giamgia);
            $('#updateQuantity').val(product.quantity);
            $('#updateDescription').val(product.description);
            $('#updateCategorySelect').val(product.categoryID);
            // Populate other fields similarly
            $('#modalUpdateProduct').modal('show');
        }
    });

    // Event listener for update form submission
    $('#updateProductForm').on('submit', function (e) {
        e.preventDefault();
        let formData = new FormData(this);
        const product = {
            productID: formData.get('productID'),
            name: formData.get('name'),
            giacu: formData.get('giacu'),
            giamgia: formData.get('giamgia'),
            description: formData.get('description'),
            quantity: formData.get('quantity'),
            categoryID: $('#updateCategorySelect').val() ?? formData.get('category')
        };
        formData.append('product', new Blob([JSON.stringify(product)], {type: 'application/json'}));
        $.ajax({
            url: '/api/products/manager',
            method: 'PUT',
            processData: false,
            contentType: false,
            data: formData,
            success: function (response) {
                Swal.fire({
                    title: 'Thành công',
                    text: 'Sản phẩm đã được cập nhật thành công!',
                    icon: 'success',
                    confirmButtonText: 'OK'
                }).then(() => {
                    location.reload();
                });
            },
            error: function (error) {
                Swal.fire({
                    title: 'Lỗi',
                    text: 'Không thể cập nhật sản phẩm',
                    icon: 'error',
                    confirmButtonText: 'OK'
                });
            }
        });
    });

    // Event listener for "Xóa" button
    $(document).on('click', '.delete-product', function () {
        const productID = $(this).data('id');
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
                $.ajax({
                    url: `/api/products/manager/${productID}`,
                    method: 'DELETE',
                    success: function () {
                        Swal.fire({
                            title: 'Đã xóa!',
                            text: 'Sản phẩm đã được xóa.',
                            icon: 'success',
                            confirmButtonText: 'OK'
                        }).then(() => {
                            location.reload();
                        });
                    },
                    error: function (error) {
                        Swal.fire({
                            title: 'Lỗi',
                            text: 'Không thể xóa sản phẩm',
                            icon: 'error',
                            confirmButtonText: 'OK'
                        });
                    }
                });
            }
        });
    });
});