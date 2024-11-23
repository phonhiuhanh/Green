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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

public class SearchTest {

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
    public void testSearch1() {
        driver.get("http://localhost:8080/login");
        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.className("btn-login"));
        usernameField.sendKeys("khoa33738@gmail.com");
        passwordField.sendKeys("123456");
        loginButton.click();

        wait.until(ExpectedConditions.urlContains("/index/shop"));

        String currentUrl = driver.getCurrentUrl();

        WebElement searchButton = driver.findElement(By.className("site-btn"));
        searchButton.click();

        assertNotEquals(currentUrl, driver.getCurrentUrl(), "Test failed: URL did not change.");
    }

    @Test
    public void testSearch2() {
        driver.get("http://localhost:8080/login");
        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.className("btn-login"));
        usernameField.sendKeys("khoa33738@gmail.com");
        passwordField.sendKeys("123456");
        loginButton.click();

        wait.until(ExpectedConditions.urlContains("/index/shop"));

        String currentUrl = driver.getCurrentUrl();

        WebElement searchButton = driver.findElement(By.className("site-btn"));
        searchButton.click();

        assertEquals(currentUrl, driver.getCurrentUrl(), "Test failed: URL should not change.");

        WebElement alertPopup = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("swal2-popup")));
        String alertTitle = alertPopup.findElement(By.className("swal2-title")).getText();
        String alertText = alertPopup.findElement(By.className("swal2-html-container")).getText();

        assertEquals("Lỗi", alertTitle, "Test failed: Incorrect Swal2 title.");
        assertEquals("Không tìm thấy sản phẩm.", alertText, "Test failed: Incorrect Swal2 message.");

        WebElement confirmButton = driver.findElement(By.cssSelector(".swal2-confirm"));
        confirmButton.click();
    }
    @Test
    public void testSearch3() {
        driver.get("http://localhost:8080/login");

        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.className("btn-login"));
        usernameField.sendKeys("khoa33738@gmail.com");
        passwordField.sendKeys("123456");
        loginButton.click();

        wait.until(ExpectedConditions.urlContains("/index/shop"));

        WebElement searchInput = driver.findElement(By.id("search-input"));
        searchInput.sendKeys("thịt");


        WebElement searchButton = driver.findElement(By.className("site-btn"));
        searchButton.click();

        wait.until(ExpectedConditions.urlContains("search?keyword=th%E1%BB%8Bt"));
        String currentUrl = driver.getCurrentUrl();

        assertNotEquals("http://localhost:8080/index/shop", currentUrl, "Test failed: URL did not change.");
        System.out.println("Test Passed: Tìm kiếm sp thành công!");
    }



}
