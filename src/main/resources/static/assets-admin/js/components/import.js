document.addEventListener('DOMContentLoaded', function () {
    const importTableBody = document.getElementById('importTableBody');
    const currentPageSpan = document.getElementById('currentPage');
    const firstPageBtn = document.getElementById('firstPage');
    const prevPageBtn = document.getElementById('prevPage');
    const nextPageBtn = document.getElementById('nextPage');
    const lastPageBtn = document.getElementById('lastPage');
    const addImportForm = document.getElementById('addImportForm');
    const updateImportForm = document.getElementById('updateImportForm');
    const productSearchInput = document.getElementById('productSearch');
    const productSearchResults = document.getElementById('productSearchResults');
    const importInfoTable = document.getElementById('importInfoTable').querySelector('tbody');
    const productSearchInput2 = document.getElementById('productSearch2');
    const productSearchResults2 = document.getElementById('productSearchResults2');
    const importInfoTable2 = document.getElementById('importInfoTable2').querySelector('tbody');

    let currentPage = 0;
    let totalPages = 1;

    function loadImports(page) {
        fetch(`/api/imports?page=${page}&size=10`)
            .then(response => response.json())
            .then(data => {
                totalPages = data.totalPages;
                currentPage = page;
                currentPageSpan.textContent = `Trang ${currentPage + 1}/${totalPages}`;
                importTableBody.innerHTML = '';
                data.content.forEach(importInfo => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${importInfo.importID}</td>
                        <td><img src="${importInfo.picture}" alt="Ảnh" width="50"></td>
                        <td>${importInfo.address}</td>
                        <td>${importInfo.date}</td>
                        <td>${importInfo.shipperName}</td>
                        <td>${importInfo.staffName}</td>
                        <td>${formatCurrency(importInfo.totalAmount)}</td>
                        <td>
                            <button class="btn btn-sm btn-primary" onclick="editImport(${importInfo.importID})">Sửa</button>
                            <button class="btn btn-sm btn-danger" onclick="deleteImport(${importInfo.importID})">Xóa</button>
                        </td>
                    `;
                    importTableBody.appendChild(row);
                });
                updatePaginationControls();
            })
            .catch(error => console.error('Error:', error));
    }

    function updatePaginationControls() {
        firstPageBtn.disabled = currentPage === 0;
        prevPageBtn.disabled = currentPage === 0;
        nextPageBtn.disabled = currentPage === totalPages - 1;
        lastPageBtn.disabled = currentPage === totalPages - 1;
    }

    firstPageBtn.addEventListener('click', () => loadImports(0));
    prevPageBtn.addEventListener('click', () => loadImports(currentPage - 1));
    nextPageBtn.addEventListener('click', () => loadImports(currentPage + 1));
    lastPageBtn.addEventListener('click', () => loadImports(totalPages - 1));

    addImportForm.addEventListener('submit', function (event) {
        event.preventDefault();
        const formData = new FormData(addImportForm);
        const importData = {
            address: formData.get('address'),
            shipperName: formData.get('shipperName'),
            staffName: formData.get('staffName'),
            date: formData.get('importDate'),
            isDeleted: false,
            importStatus: '',
            totalAmount: formData.get('totalAmount'),
            importInfos: []
        };
        importInfoTable.querySelectorAll('tr').forEach(row => {
            const importInfo = {
                product: {
                    productID: row.querySelector('.productID').textContent,
                    name: row.querySelector('.productName').textContent
                },
                priceImport: row.querySelector('.priceImport').value,
                quantityImport: row.querySelector('.quantityImport').value
            };
            importData.importInfos.push(importInfo);
        });
        formData.append('import', new Blob([JSON.stringify(importData)], { type: 'application/json' }));
        fetch('/api/imports', {
            method: 'POST',
            body: formData
        })
            .then(response => response.json())
            .then(data => {
                Swal.fire('Thành công', 'Nhập hàng đã được thêm', 'success');
                loadImports(currentPage);
                clearAddImportForm();
                updateImportForm.reset();
                $('#addImportModal').modal('hide');
            })
            .catch(error => {
                Swal.fire('Lỗi', 'Có lỗi xảy ra', 'error');
                console.error('Error:', error);
            });
    });

    updateImportForm.addEventListener('submit', function (event) {
        event.preventDefault();
        const formData = new FormData(updateImportForm);
        const importData = {
            address: formData.get('address'),
            shipperName: formData.get('shipperName'),
            staffName: formData.get('staffName'),
            date: formData.get('importDate'),
            isDeleted: false,
            importStatus: '',
            totalAmount: formData.get('totalAmount'),
            importInfos: []
        };
        importInfoTable2.querySelectorAll('tr').forEach(row => {
            const importInfo = {
                product: {
                    productID: row.querySelector('.productID').textContent,
                    name: row.querySelector('.productName').textContent
                },
                priceImport: row.querySelector('.priceImport').value,
                quantityImport: row.querySelector('.quantityImport').value
            };
            importData.importInfos.push(importInfo);
        });
        formData.append('import', new Blob([JSON.stringify(importData)], { type: 'application/json' }));
        const importID = formData.get('importID');
        fetch(`/api/imports/${importID}`, {
            method: 'PUT',
            body: formData
        })
            .then(response => response.json())
            .then(data => {
                Swal.fire('Thành công', 'Nhập hàng đã được cập nhật', 'success');
                loadImports(currentPage);
                clearUpdateImportForm();
                $('#updateImportModal').modal('hide');
            })
            .catch(error => {
                Swal.fire('Lỗi', 'Có lỗi xảy ra', 'error');
                console.error('Error:', error);
            });
    });

    productSearchInput.addEventListener('input', function () {
        const keyword = productSearchInput.value;
        if (keyword.length > 2) {
            fetch(`/api/products/q?keyword=${keyword}`)
                .then(response => response.json())
                .then(data => {
                    productSearchResults.innerHTML = '';
                    data.slice(0, 3).forEach(product => {
                        const resultItem = document.createElement('div');
                        resultItem.classList.add('search-result-item');
                        resultItem.textContent = product.name;
                        resultItem.addEventListener('click', () => {
                            Swal.fire({
                                title: 'Thành công',
                                icon: 'success',
                                text: 'Sản phẩm đã được thêm vào danh sách nhập hàng',
                                preConfirm: () => {
                                    const productID = product.productID;
                                    const productName = product.name;
                                    const priceImport = product.price ?? product.giacu ?? 0;
                                    const quantityImport = 0;
                                    const row = document.createElement('tr');
                                    row.innerHTML = `
                                        <td class="productID">${productID}</td>
                                        <td class="productName">${productName}</td>
                                        <td><input type="number" class="form-control priceImport" value="${priceImport}"></td>
                                        <td><input type="number" class="form-control quantityImport" value="${quantityImport}"></td>
                                        <td><button class="btn btn-sm btn-danger" onclick="removeImportInfoRow(this)">Xóa</button></td>
                                    `;
                                    importInfoTable.appendChild(row);
                                }
                            });
                        });
                        productSearchResults.appendChild(resultItem);
                    });
                })
                .catch(error => console.error('Error:', error));
        }
    });

    productSearchInput2.addEventListener('input', function () {
        const keyword = productSearchInput2.value;
        if (keyword.length > 2) {
            fetch(`/api/products/q?keyword=${keyword}`)
                .then(response => response.json())
                .then(data => {
                    productSearchResults2.innerHTML = '';
                    data.slice(0, 3).forEach(product => {
                        const resultItem = document.createElement('div');
                        resultItem.classList.add('search-result-item');
                        resultItem.textContent = product.name;
                        resultItem.addEventListener('click', () => {
                            Swal.fire({
                                title: 'Thành công',
                                icon: 'success',
                                text: 'Sản phẩm đã được thêm vào danh sách nhập hàng',
                                preConfirm: () => {
                                    const productID = product.productID;
                                    const productName = product.name;
                                    const priceImport = product.price ?? product.giacu ?? 0;
                                    const quantityImport = 0;
                                    const row = document.createElement('tr');
                                    row.innerHTML = `
                                        <td class="productID">${productID}</td>
                                        <td class="productName">${productName}</td>
                                        <td><input type="number" class="form-control priceImport" value="${priceImport}"></td>
                                        <td><input type="number" class="form-control quantityImport" value="${quantityImport}"></td>
                                        <td><button class="btn btn-sm btn-danger" onclick="removeImportInfoRow(this)">Xóa</button></td>
                                    `;
                                    importInfoTable2.appendChild(row);
                                }
                            });
                        });
                        productSearchResults2.appendChild(resultItem);
                    });
                })
                .catch(error => console.error('Error:', error));
        }
    });

    window.editImport = function (importID) {
        fetch(`/api/imports/${importID}`)
            .then(response => response.json())
            .then(importData => {
                document.getElementById('updateAddress').value = importData.address;
                document.getElementById('updateShipperName').value = importData.shipperName;
                document.getElementById('updateStaffName').value = importData.staffName;
                document.getElementById('updateImportDate').value = importData.date;
                document.getElementById('updateTotalAmount').value = importData.totalAmount;
                document.getElementById('updateImportID').value = importData.importID;
                importInfoTable2.innerHTML = '';
                importData.importInfos.forEach(info => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td class="productID">${info.product.productID}</td>
                        <td class="productName">${info.product.name}</td>
                        <td><input type="number" class="form-control priceImport" value="${info.priceImport}"></td>
                        <td><input type="number" class="form-control quantityImport" value="${info.quantityImport}"></td>
                        <td><button class="btn btn-sm btn-danger" onclick="removeImportInfoRow(this)">Xóa</button></td>
                    `;
                    importInfoTable2.appendChild(row);
                });
                $('#updateImportModal').modal('show');
            })
            .catch(error => console.error('Error:', error));
    };

    window.deleteImport = function (importID) {
        Swal.fire({
            title: 'Bạn có chắc chắn?',
            text: 'Bạn sẽ không thể hoàn tác hành động này!',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Có, xóa nó!',
            cancelButtonText: 'Không, hủy bỏ!'
        }).then((result) => {
            if (result.isConfirmed) {
                fetch(`/api/imports/${importID}`, {
                    method: 'DELETE'
                })
                    .then(response => {
                        if (response.ok) {
                            Swal.fire('Đã xóa!', 'Nhập hàng đã được xóa.', 'success');
                            loadImports(currentPage);
                        } else {
                            Swal.fire('Lỗi', 'Có lỗi xảy ra', 'error');
                        }
                    })
                    .catch(error => {
                        Swal.fire('Lỗi', 'Có lỗi xảy ra', 'error');
                        console.error('Error:', error);
                    });
            }
        });
    };

    window.removeImportInfoRow = function (button) {
        button.closest('tr').remove();
    };

    loadImports(0);

    function clearAddImportForm() {
        document.getElementById('addImportForm').reset();
        document.getElementById('productSearchResults').innerHTML = '';
        document.getElementById('importInfoTable').querySelector('tbody').innerHTML = '';
    }

    function clearUpdateImportForm() {
        document.getElementById('updateImportForm').reset();
        document.getElementById('productSearchResults2').innerHTML = '';
        document.getElementById('importInfoTable2').querySelector('tbody').innerHTML = '';
    }
});