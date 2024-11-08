$(document).ready(function () {
    // Fetch and display orders
    $.get('/api/orders', function (orders) {
        const orderTableBody = $('#orderTable tbody');
        orderTableBody.empty();
        orders.forEach(order => {
            const formattedTotalAmount = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(order.totalAmount);
            const row = `
                <tr>
                    <td>${order.orderID}</td>
                    <td>${new Date(order.orderDate).toLocaleDateString('vi-VN')}</td>
                    <td>${formattedTotalAmount}</td>
                    <td>${order.buyerId}</td>
                    <td>${order.orderStatus}</td>
                    <td>
                        <button class="btn btn-success btn-approve" data-id="${order.orderID}" ${order.orderStatus !== 'Đang chờ xử lý' ? 'disabled' : ''}>✔</button>
                        <button class="btn btn-danger btn-reject" data-id="${order.orderID}" ${order.orderStatus !== 'Đang chờ xử lý' ? 'disabled' : ''}>✘</button>
                        <button class="btn btn-info btn-view-details" data-id="${order.orderID}">👁</button>
                    </td>
                </tr>
            `;
            orderTableBody.append(row);
        });

        // Approve order
        $('.btn-approve').click(function () {
            const orderId = $(this).data('id');
            $.ajax({
                url: `/api/orders/${orderId}/status`,
                method: 'PUT',
                data: { orderStatus: 'Đang giao' },
                success: function () {
                    Swal.fire({
                        title: 'Thành công',
                        text: 'Đơn hàng đã được duyệt!',
                        icon: 'success',
                        confirmButtonText: 'OK'
                    }).then(() => {
                        location.reload();
                    });
                },
                error: function (error) {
                    Swal.fire({
                        title: 'Lỗi',
                        text: 'Không thể duyệt đơn hàng',
                        icon: 'error',
                        confirmButtonText: 'OK'
                    });
                }
            });
        });

        // Reject order
        $('.btn-reject').click(function () {
            const orderId = $(this).data('id');
            $.ajax({
                url: `/api/orders/${orderId}/status`,
                method: 'PUT',
                data: { orderStatus: 'Bị hủy' },
                success: function () {
                    Swal.fire({
                        title: 'Thành công',
                        text: 'Đơn hàng đã bị hủy!',
                        icon: 'success',
                        confirmButtonText: 'OK'
                    }).then(() => {
                        location.reload();
                    });
                },
                error: function (error) {
                    Swal.fire({
                        title: 'Lỗi',
                        text: 'Không thể hủy đơn hàng',
                        icon: 'error',
                        confirmButtonText: 'OK'
                    });
                }
            });
        });

        // View order details
        $('.btn-view-details').click(function () {
            const orderId = $(this).data('id');
            $.get(`/api/orders/${orderId}`, function (order) {
                const orderDetailsTableBody = $('#orderDetailsTable tbody');
                orderDetailsTableBody.empty();
                order.orderDetails.forEach(detail => {
                    const formattedPrice = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(detail.price);
                    const formattedTotal = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(detail.price * detail.quantity);
                    const row = `
                        <tr>
                            <td>${detail.product.productID}</td>
                            <td>${detail.product.name}</td>
                            <td>${formattedPrice}</td>
                            <td>${detail.quantity}</td>
                            <td>${formattedTotal}</td>
                        </tr>
                    `;
                    orderDetailsTableBody.append(row);
                });
                $('#orderDetailsModal').modal('show');
            });
        });
    });
});