let products = [];
const itemsPerPage = 10;
let currentPage = 1;

function formatCurrency(number) {
    number = Math.round(number);
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
}

function displayPage(page) {
    const start = (page - 1) * itemsPerPage;
    const end = start + itemsPerPage;
    const paginatedItems = products.slice(start, end);

    const tableBody = document.getElementById('productTableBody');
    tableBody.innerHTML = '';

    paginatedItems.forEach(product => {
        const row = document.createElement('tr');
        const thumbnail = product.images?.find(image => image.isMain) || product?.images[0];
        row.innerHTML = `
                <td>${product.productID}</td>
                <td><img alt="Hình ảnh" style="width: 100px; height: 100px" src="${thumbnail?.imageURL}"></td>
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
                alert('Product updated successfully!');
                location.reload();
            },
            error: function (error) {
                console.error('Error updating product:', error);
            }
        });
    });

    // Event listener for "Xóa" button
    $(document).on('click', '.delete-product', function () {
        const productID = $(this).data('id');
        if (confirm('Are you sure you want to delete this product?')) {
            $.ajax({
                url: `/api/products/manager/${productID}`,
                method: 'DELETE',
                success: function () {
                    alert('Product deleted successfully!');
                    location.reload();
                },
                error: function (error) {
                    console.error('Error deleting product:', error);
                }
            });
        }
    });
});


