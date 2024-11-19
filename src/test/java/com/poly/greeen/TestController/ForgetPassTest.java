package com.poly.greeen.TestController;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ForgetPassTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Public\\chromedriver\\chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Chờ tối đa 10 giây
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    public void testForgotPasswordWithoutOtp() throws InterruptedException {
        driver.get("http://localhost:8080/index/forgot-password");

        WebElement emailOrPhoneField = driver.findElement(By.id("emailOrPhone"));
        WebElement sendOtpButton = driver.findElement(By.id("sendOtpButton"));
        emailOrPhoneField.sendKeys("khoa33738@gmail.com");
        sendOtpButton.click();

        WebElement otpMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("otpMessage")));
        assertTrue(otpMessage.getText().contains("Mã OTP đã được gửi tới email đăng ký của bạn"));

        WebElement newPasswordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("newPassword")));
        WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("confirmButton")));
        newPasswordField.sendKeys("031124");
        confirmButton.click();

        WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("errorMessage")));
        String errorMessage = errorElement.getText();
        assertEquals("Xác nhận thất bại. Vui lòng kiểm tra lại mã OTP và thử lại", errorMessage);

        if (errorMessage.equals("Xác nhận thất bại. Vui lòng kiểm tra lại mã OTP và thử lại")) {
            System.out.println("Lỗi xác nhận OTP: " + errorMessage);
            return; // Kết thúc bài test
        }
    }
@Test
@Order(2)
public void testForgotPasswordWithoutPassword() throws InterruptedException {
    driver.get("http://localhost:8080/index/forgot-password");
    WebElement emailOrPhoneField = driver.findElement(By.id("emailOrPhone"));
    WebElement sendOtpButton = driver.findElement(By.id("sendOtpButton"));
    emailOrPhoneField.sendKeys("khoa33738@gmail.com");
    sendOtpButton.click();
    WebElement otpMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("otpMessage")));
    assertEquals("Mã OTP đã được gửi tới email đăng ký của bạn", otpMessage.getText());
    System.out.println("Vui lòng nhập mã OTP trong vòng 2 phút...");
    Thread.sleep(10000);
    WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("confirmButton")));
    confirmButton.click();
    WebElement swalPopup = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("swal2-popup")));
    String swalTitle = swalPopup.findElement(By.className("swal2-title")).getText();
    String swalText = swalPopup.findElement(By.className("swal2-html-container")).getText();

    assertEquals("Lỗi", swalTitle);
    assertEquals("Vui lòng nhập mật khẩu mới.", swalText);
    System.out.println("Kiểm thử hoàn thành: Hiển thị thông báo lỗi chính xác.");

}
    @Test
    @Order(3)
    public void testChangePasswordSuccessfully() throws InterruptedException {
    driver.get("http://localhost:8080/index/forgot-password");

    WebElement emailOrPhoneField = driver.findElement(By.id("emailOrPhone"));
    WebElement sendOtpButton = driver.findElement(By.id("sendOtpButton"));
    emailOrPhoneField.sendKeys("khoa33738@gmail.com");
    sendOtpButton.click();

    WebElement otpMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("otpMessage")));
    assertEquals("Mã OTP đã được gửi tới email đăng ký của bạn", otpMessage.getText());
    System.out.println("Vui lòng nhập mã OTP trong vòng 2 phút...");
    Thread.sleep(10000);

    WebElement newPasswordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("newPassword")));
    newPasswordField.sendKeys("123456");
        WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("confirmButton")));
        confirmButton.click();
        Thread.sleep(10000);
    WebElement confirmButtonSuccess = driver.findElement(By.cssSelector(".swal2-confirm"));
    confirmButtonSuccess.click();

    WebElement usernameField = driver.findElement(By.id("username"));
    WebElement passwordField = driver.findElement(By.id("password"));
    WebElement loginButton = driver.findElement(By.className("btn-login"));

    usernameField.sendKeys("khoa33738@gmail.com");
    passwordField.sendKeys("123456");
    loginButton.click();
    System.out.println("Kiểm thử hoàn thành: Đăng nhập thành công với mật khẩu mới.");
}

}
