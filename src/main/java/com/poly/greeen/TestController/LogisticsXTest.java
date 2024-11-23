package com.poly.greeen.TestController;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogisticsXTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        // Cấu hình ChromeDriver
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Public\\chromedriver\\chromedriver.exe");
        driver = new ChromeDriver();
    }

    @AfterEach
    public void tearDown() {
        // Đóng trình duyệt sau mỗi test
        driver.quit();
    }

    @Test
    public void test() throws InterruptedException {
        driver.get("http://localhost:8080/login");
        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.className("btn-login"));
        usernameField.sendKeys("123");
        passwordField.sendKeys("123");
        loginButton.click();
        Thread.sleep(2000);
        driver.get("http://localhost:8080/admin/delivery");
        Thread.sleep(1000);
        WebElement approveButton = driver.findElement(By.cssSelector(".btn-danger"));
        WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait2.until(ExpectedConditions.elementToBeClickable(approveButton));
        approveButton.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        WebElement alertPopup = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("swal2-popup")));
        String alertTitle = alertPopup.findElement(By.className("swal2-title")).getText();
        String alertText = alertPopup.findElement(By.className("swal2-html-container")).getText();
        WebElement confirmButton = driver.findElement(By.cssSelector(".swal2-confirm"));
        confirmButton.click();
        System.out.println("Test Passed: Đơn hàng đã được giao thành công!");


    }

}
