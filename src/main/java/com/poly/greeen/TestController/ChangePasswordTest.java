package com.poly.greeen.TestController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

public class ChangePasswordTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Public\\chromedriver\\chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
    @Test
    public void testChangePasswordAndLoginAgain() throws InterruptedException {
        driver.get("http://localhost:8080/login");
        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.className("btn-login"));
        usernameField.sendKeys("khoa33738@gmail.com");
        passwordField.sendKeys("123456");
        loginButton.click();

        wait.until(ExpectedConditions.urlContains("/index/shop"));
        WebElement usernameDropdown = driver.findElement(By.className("username"));
        usernameDropdown.click();
        WebElement changePasswordButton = driver.findElement(By.cssSelector("[data-bs-target='#changePasswordModal']"));
        changePasswordButton.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("changePasswordModal")));
        WebElement currentPassword = driver.findElement(By.id("currentPassword"));
        WebElement newPassword = driver.findElement(By.id("newPassword"));
        WebElement confirmPassword = driver.findElement(By.id("confirmPassword"));
        currentPassword.sendKeys("123456");
        newPassword.sendKeys("031124");
        confirmPassword.sendKeys("031124");
        WebElement confirmButton = driver.findElement(By.id("confirmButton"));
        confirmButton.click();

        WebElement successAlert = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("swal2-popup")));
        String alertTitle = successAlert.findElement(By.className("swal2-title")).getText();
        assertEquals("Thành công", alertTitle);
        System.out.println("Test Passed: Đổi mật khẩu thành công!");
        WebElement confirmOK = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".swal2-confirm")));
        confirmOK.click();
        Thread.sleep(10000);

        driver.get("http://localhost:8080/login");
        WebElement username = driver.findElement(By.id("username"));
        WebElement password = driver.findElement(By.id("password"));
        WebElement login = driver.findElement(By.className("btn-login"));
        username.sendKeys("khoa33738@gmail.com");
        password.sendKeys("031124");
        login.click();

        wait.until(ExpectedConditions.urlContains("/index/shop"));
        System.out.println("Test Passed: Đăng nhập lại với mật khẩu mới thành công!");
    }



    @Test
    public void testChangePasswordWrongCurrentPassword() {
        driver.get("http://localhost:8080/login");
        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.className("btn-login"));
        usernameField.sendKeys("khoa33738@gmail.com");
        passwordField.sendKeys("123456");
        loginButton.click();


        wait.until(ExpectedConditions.urlContains("/index/shop"));
        WebElement usernameDropdown = driver.findElement(By.className("username"));
        usernameDropdown.click();
        WebElement changePasswordButton = driver.findElement(By.cssSelector("[data-bs-target='#changePasswordModal']"));
        changePasswordButton.click();


        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("changePasswordModal")));
        WebElement currentPassword = driver.findElement(By.id("currentPassword"));
        WebElement newPassword = driver.findElement(By.id("newPassword"));
        WebElement confirmPassword = driver.findElement(By.id("confirmPassword"));
        currentPassword.sendKeys("wrongpassword");
        newPassword.sendKeys("newpass123");
        confirmPassword.sendKeys("newpass123");
        WebElement confirmButton = driver.findElement(By.id("confirmButton"));
        confirmButton.click();

        WebElement errorAlert = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("swal2-popup")));
        String alertTitle = errorAlert.findElement(By.className("swal2-title")).getText();
        assertEquals("Lỗi", alertTitle);
        System.out.println("Test Passed: Mật khẩu hiện tại không đúng!");
    }

    @Test
    public void testChangePasswordMismatch() {
        driver.get("http://localhost:8080/login");
        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.className("btn-login"));
        usernameField.sendKeys("khoa33738@gmail.com");
        passwordField.sendKeys("123456");
        loginButton.click();

        wait.until(ExpectedConditions.urlContains("/index/shop"));
        WebElement usernameDropdown = driver.findElement(By.className("username"));
        usernameDropdown.click();
        WebElement changePasswordButton = driver.findElement(By.cssSelector("[data-bs-target='#changePasswordModal']"));
        changePasswordButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("changePasswordModal")));
        WebElement currentPassword = driver.findElement(By.id("currentPassword"));
        WebElement newPassword = driver.findElement(By.id("newPassword"));
        WebElement confirmPassword = driver.findElement(By.id("confirmPassword"));
        currentPassword.sendKeys("123456");
        newPassword.sendKeys("newpass123");
        confirmPassword.sendKeys("differentpass123");
        WebElement confirmButton = driver.findElement(By.id("confirmButton"));
        confirmButton.click();

        WebElement errorAlert = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("swal2-popup")));
        String alertTitle = errorAlert.findElement(By.className("swal2-title")).getText();
        assertEquals("Lỗi", alertTitle);
        System.out.println("Test Passed: Mật khẩu mới và xác nhận mật khẩu không khớp!");
    }
    @Test
    public void testChangePasswordNull() {
        driver.get("http://localhost:8080/login");
        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.className("btn-login"));
        usernameField.sendKeys("khoa33738@gmail.com");
        passwordField.sendKeys("123456");
        loginButton.click();

        wait.until(ExpectedConditions.urlContains("/index/shop"));
        WebElement usernameDropdown = driver.findElement(By.className("username"));
        usernameDropdown.click();
        WebElement changePasswordButton = driver.findElement(By.cssSelector("[data-bs-target='#changePasswordModal']"));
        changePasswordButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("changePasswordModal")));
        WebElement currentPassword = driver.findElement(By.id("currentPassword"));
        WebElement newPassword = driver.findElement(By.id("newPassword"));
        WebElement confirmPassword = driver.findElement(By.id("confirmPassword"));
        currentPassword.sendKeys("");
        newPassword.sendKeys("");
        confirmPassword.sendKeys("");
        WebElement confirmButton = driver.findElement(By.id("confirmButton"));
        confirmButton.click();

        WebElement errorAlert = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("swal2-popup")));
        String alertTitle = errorAlert.findElement(By.className("swal2-title")).getText();
        assertEquals("Lỗi", alertTitle);
        System.out.println("Test Passed: Mật khẩu mới và xác nhận mật khẩu không khớp!");
    }

}