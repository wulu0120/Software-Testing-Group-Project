package com.jtspringproject.JtSpringProject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.springframework.boot.test.context.SpringBootTest;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProfileGUI {
    static String BASE_URL = "http://localhost:8080/";

    @BeforeEach
    public void login() {
        open(BASE_URL);
        $(By.id("username")).sendKeys("jay");
        $(By.id("password")).sendKeys("123");
        $(By.xpath("/html/body/div/div/form/input")).click();
        $(By.xpath("/html/body/section/div/nav/div/div/ul/li[2]")).click();
    }

    @Test
    public void testGetUserProfile() {
        assertEquals(BASE_URL + "profileDisplay", executeJavaScript("return window.location.href;"));
    }

    @Test
    public void testUpdateProfileNoUsername() {
        $(By.id("firstName")).sendKeys("");
        $(By.id("email")).sendKeys("123@gmail");
        $(By.id("password")).sendKeys("Jay123..");
        assertEquals(BASE_URL + "profileDisplay", executeJavaScript("return window.location.href;"));
    }

    @Test
    public void testUpdateProfileInvalidEmailNoAt() {
        $(By.id("firstName")).sendKeys("jay");
        $(By.id("email")).sendKeys("123");
        $(By.id("password")).sendKeys("Jay123..");
        assertEquals(BASE_URL + "profileDisplay", executeJavaScript("return window.location.href;"));
    }

    @Test
    public void testUpdateProfileInvalidEmailNothingAfterAt() {
        $(By.id("firstName")).sendKeys("jay");
        $(By.id("email")).sendKeys("123@");
        $(By.id("password")).sendKeys("Jay123..");
        assertEquals(BASE_URL + "profileDisplay", executeJavaScript("return window.location.href;"));
    }

    @Test
    public void testUpdateProfileInvalidEmailTooShort() {
        $(By.id("firstName")).sendKeys("jay");
        $(By.id("email")).sendKeys("123@g");
        $(By.id("password")).sendKeys("Jay123..");
        assertEquals(BASE_URL + "profileDisplay", executeJavaScript("return window.location.href;"));
    }

    @Test
    public void testUpdateProfileNoEmail() {
        $(By.id("firstName")).sendKeys("jay");
        $(By.id("email")).sendKeys("");
        $(By.id("password")).sendKeys("Jay123..");
        assertEquals(BASE_URL + "profileDisplay", executeJavaScript("return window.location.href;"));
    }

    @Test
    public void testUpdateProfileInvalidPasswordNoNumber() {
        $(By.id("firstName")).sendKeys("jay");
        $(By.id("email")).sendKeys("123@gmail");
        $(By.id("password")).sendKeys("Jay...");
        assertEquals(BASE_URL + "profileDisplay", executeJavaScript("return window.location.href;"));
    }

    @Test
    public void testUpdateProfileInvalidPasswordNoUppercase() {
        $(By.id("firstName")).sendKeys("jay");
        $(By.id("email")).sendKeys("123@gmail");
        $(By.id("password")).sendKeys("jay123..");
        assertEquals(BASE_URL + "profileDisplay", executeJavaScript("return window.location.href;"));
    }

    @Test
    public void testUpdateProfileInvalidPasswordNoLowercase() {
        $(By.id("firstName")).sendKeys("jay");
        $(By.id("email")).sendKeys("123@gmail");
        $(By.id("password")).sendKeys("JAY123..");
        assertEquals(BASE_URL + "profileDisplay", executeJavaScript("return window.location.href;"));
    }

    @Test
    public void testUpdateProfileInvalidPasswordNoSpecial() {
        $(By.id("firstName")).sendKeys("jay");
        $(By.id("email")).sendKeys("123@gmail");
        $(By.id("password")).sendKeys("Jay12345");
        assertEquals(BASE_URL + "profileDisplay", executeJavaScript("return window.location.href;"));
    }

    @Test
    public void testUpdateProfileInvalidPasswordTooShort() {
        $(By.id("firstName")).sendKeys("jay");
        $(By.id("email")).sendKeys("123@gmail");
        $(By.id("password")).sendKeys("Jay123.");
        assertEquals(BASE_URL + "profileDisplay", executeJavaScript("return window.location.href;"));
    }

    @Test
    public void testUpdateProfileNoPassword() {
        $(By.id("firstName")).sendKeys("jay");
        $(By.id("email")).sendKeys("123@gmail");
        $(By.id("password")).sendKeys("");
        assertEquals(BASE_URL + "profileDisplay", executeJavaScript("return window.location.href;"));
    }

    @Test
    public void testUpdateProfileSuccess() {
        $(By.id("firstName")).sendKeys("jay");
        $(By.id("email")).sendKeys("123@gmail");
        $(By.id("password")).sendKeys("Jay123..");
        $(By.xpath("/html/body/div/div/form/input")).click();
        assertEquals(BASE_URL + "index", executeJavaScript("return window.location.href;"));
    }
}
