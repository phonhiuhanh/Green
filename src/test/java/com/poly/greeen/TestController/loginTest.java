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
        System.out.println("Testpass: Đăng nhâp thành công!");
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
        System.out.println("nhập sai passwork!");
        assertEquals("http://localhost:8080/index/shop", driver.getCurrentUrl());

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
        System.out.println("nhập sai username!");
        assertEquals("http://localhost:8080/index/shop", driver.getCurrentUrl());

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
        System.out.println("Mật khẩu và username không được bỏ trống");
        assertEquals("http://localhost:8080/index/shop", driver.getCurrentUrl());

    }
}
