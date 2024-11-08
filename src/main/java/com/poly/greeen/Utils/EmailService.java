package com.poly.greeen.Utils;

import com.poly.greeen.Entity.Order;
import com.poly.greeen.Entity.OrderDetail;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @SneakyThrows
    public void sendEmail(String to, String subject, String text) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true); // Set to true to indicate HTML content
        mailSender.send(message);
    }

    public void sendOrderStatusUpdateEmail(String to, Order order, String orderStatus) {
        // Format order details
        StringBuilder orderDetailsTable = new StringBuilder();
        orderDetailsTable.append("<table border='1'><tr><th>ID sản phẩm</th><th>Tên sản phẩm</th><th>Giá</th><th>Số lượng</th><th>Tổng tiền</th></tr>");
        for (OrderDetail detail : order.getOrderDetails()) {
            String formattedPrice = String.format("%,.0f₫", detail.getPrice());
            String formattedTotal = String.format("%,.0f₫", detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity())));
            orderDetailsTable.append("<tr>")
                    .append("<td>").append(detail.getProduct().getProductID()).append("</td>")
                    .append("<td>").append(detail.getProduct().getName()).append("</td>")
                    .append("<td>").append(formattedPrice).append("</td>")
                    .append("<td>").append(detail.getQuantity()).append("</td>")
                    .append("<td>").append(formattedTotal).append("</td>")
                    .append("</tr>");
        }
        orderDetailsTable.append("</table>");

        // Prepare email content
        String subject = "Thông báo thay đổi trạng thái đơn hàng";
        String text = String.format("Trạng thái đơn hàng của bạn đã được cập nhật thành: %s<br>Ngày đặt: %s<br>Tổng tiền: %,.0f₫<br>Chi tiết đơn hàng:<br>%s",
                orderStatus, new SimpleDateFormat("dd/MM/yyyy").format(order.getOrderDate()), order.getTotalAmount(), orderDetailsTable.toString());

        // Send email notification
        sendEmail(to, subject, text);
    }
}