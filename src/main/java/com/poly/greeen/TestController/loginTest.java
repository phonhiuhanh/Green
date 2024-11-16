package com.poly.greeen.TestController;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class loginTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver","C:\\Users\\Public\\chromedriver\\chromedriver.exe ");
        driver = new ChromeDriver();
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testLoginSuccess() {
        driver.get("http://localhost:8080/login");

        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.className("btn-login"));

        usernameField.sendKeys("khoa33738@gmail.com");
        passwordField.sendKeys("123456");
        loginButton.click();

        assertEquals("http://localhost:8080/index/shop", driver.getCurrentUrl());
    }

    @Test
    public void testLoginFailWithWrongPassword() {
        driver.get("http://localhost:8080/login");

        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.className("btn-login"));

        usernameField.sendKeys("khoa33738@gmail.com");
        passwordField.sendKeys("wrongpassword");
        loginButton.click();

        WebElement errorElement = driver.findElement(By.className("error-message"));
        assertEquals("Tên đăng nhập hoặc mật khẩu không đúng", errorElement.getText());
    }
    @Test
    public void testLoginFailWithEmptyUsername() {
        driver.get("http://localhost:8080/login");

        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.className("btn-login"));

        usernameField.sendKeys("wrongpassusername");
        passwordField.sendKeys("123456");
        loginButton.click();

        WebElement errorElement = driver.findElement(By.className("error-message"));
        assertEquals("Tên đăng nhập không được bỏ trống", errorElement.getText());
    }
    @Test
    public void testLoginFailWithEmptyPassword() {
        driver.get("http://localhost:8080/login");

        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.className("btn-login"));

        usernameField.sendKeys("");
        passwordField.sendKeys("");
        loginButton.click();

        WebElement errorElement = driver.findElement(By.className("error-message"));
        assertEquals("Mật khẩu không được bỏ trống", errorElement.getText());
    }
}
