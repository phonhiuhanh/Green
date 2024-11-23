package com.poly.greeen.TestController;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

public class AddToCartTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Public\\chromedriver\\chromedriver.exe");
        driver = new ChromeDriver();
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testAddToCart() throws InterruptedException {
        driver.get("http://localhost:8080/login");
        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.className("btn-login"));
        usernameField.sendKeys("khoa33738@gmail.com");
        passwordField.sendKeys("031124");
        loginButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/index/shop"));
        driver.get("http://localhost:8080/index/details?id=157");

        WebElement buttonBuy = driver.findElement(By.cssSelector(".primary-btn"));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", buttonBuy);
        wait.until(ExpectedConditions.elementToBeClickable(buttonBuy));
        buttonBuy.click();

        Thread.sleep(2000);
        WebElement alertPopup = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("swal2-popup")));
        String alertTitle = alertPopup.findElement(By.className("swal2-title")).getText();
        String alertText = alertPopup.findElement(By.className("swal2-html-container")).getText();
        assertEquals("Thành công", alertTitle);
        assertEquals("Sản phẩm đã được thêm vào giỏ hàng.", alertText);

        WebElement confirmButton = driver.findElement(By.cssSelector(".swal2-confirm"));
        confirmButton.click();
        Thread.sleep(2000);

        wait.until(ExpectedConditions.urlContains("/index/order"));
        assertEquals("http://localhost:8080/index/order", driver.getCurrentUrl());

        System.out.println("Test Passed: Sản phẩm đã được thêm vào giỏ hàng thành công!");


        driver.get("http://localhost:8080/index/order");

        WebElement cartItem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".too-long-text")));

        js.executeScript("arguments[0].scrollIntoView(true);", cartItem);
        Thread.sleep(2000);

        assertTrue(cartItem.isDisplayed(), "Hành tây nhập khẩu Trung Quốc 2kg không hiển thị!");

        String itemText = cartItem.getText();
        assertEquals("Hành tây nhập khẩu Trung Quốc 2kg", itemText, "Nội dung sản phẩm không chính xác!");


        WebElement quantityField = driver.findElement(By.cssSelector(".quantity-input"));
        assertEquals("1", quantityField.getAttribute("value"));

        WebElement totalPrice = driver.findElement(By.cssSelector(".checkout__order__subtotal"));
        assertTrue(totalPrice.getText().contains("29.000₫"));

        WebElement cartTotal = driver.findElement(By.cssSelector(".checkout__order__total"));
        assertTrue(cartTotal.getText().contains("31.900₫"));

        System.out.println("Test Passed: Giỏ hàng hiển thị chính xác!");
    }
}
