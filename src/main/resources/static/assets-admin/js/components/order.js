$(document).ready(function () {
    // Fetch and display orders
    $.ajax({
        url: `/api/orders?statuses=${statuses.join(',')}`,
        method: 'GET',
        contentType: 'application/json',
        success: function (orders) {
            const orderTableBody = $('#orderTable tbody');
            orderTableBody.empty();
            orders.forEach(order => {
                const formattedTotalAmount = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(order.totalAmount);
                const row = `
                    <tr>
                        <td>${order.orderID}</td>
                        <td>${new Date(order.orderDate).toLocaleDateString('vi-VN')}</td>
                        <td>${formattedTotalAmount}</td>
                        <td>${order.phone} - ${order.username}</td>
                        <td>${order.address}</td>
                        <td>${order.orderStatus}</td>
                        <td>
                            <button class="btn btn-success btn-approve" data-id="${order.orderID}" ${order.orderStatus !== statuses[0] ? 'disabled' : ''}>✔</button>
                            <button class="btn btn-danger btn-reject" data-id="${order.orderID}" ${order.orderStatus !== statuses[0] ? 'disabled' : ''}>✘</button>
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
                    data: { orderStatus: statuses[1] },
                    success: function () {
                        Swal.fire({
                            title: 'Thành công',
                            text: statuses[1] === 'Đang giao' ? 'Đơn hàng đã được duyệt!' : 'Đơn hàng đã được giao!',
                            icon: 'success',
                            confirmButtonText: 'OK'
                        }).then(() => {
                            location.reload();
                        });
                    },
                    error: function (error) {
                        Swal.fire({
                            title: 'Lỗi',
                            text: statuses[1] === 'Đang giao' ? 'Không thể duyệt đơn hàng' : 'Không thể giao đơn hàng',
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
                    data: { orderStatus: statuses[2] },
                    success: function () {
                        Swal.fire({
                            title: 'Thành công',
                            text: statuses[2] === 'Bị hủy' ? 'Đơn hàng đã bị hủy!' : 'Đơn hàng giao thất bại!',
                            icon: 'success',
                            confirmButtonText: 'OK'
                        }).then(() => {
                            location.reload();
                        });
                    },
                    error: function (error) {
                        Swal.fire({
                            title: 'Lỗi',
                            text: statuses[2] === 'Bị hủy' ? 'Không thể hủy đơn hàng' : 'Không thể giao đơn hàng',
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
        }
    });
});