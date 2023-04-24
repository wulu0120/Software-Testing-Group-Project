package com.jtspringproject.JtSpringProject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.springframework.boot.test.context.SpringBootTest;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BuyGUI {
    static String BASE_URL = "http://localhost:8080/";

    @BeforeEach
    public void login() {
        open(BASE_URL);
        $(By.id("username")).sendKeys("jay");
        $(By.id("password")).sendKeys("123");
        $(By.xpath("/html/body/div/div/form/input")).click();
    }

    @Test
    public void testBuyNow() {
        $(By.xpath("/html/body/section/div/div/div/div/div/div/div/div/div/div/div[2]/div[2]/a")).click();
        assertEquals(BASE_URL + "user/products", executeJavaScript("return window.location.href;"));
    }

    @Test
    public void testBuyProduct() {
        $(By.xpath("/html/body/section/div/div/div/div/div/div/div/div/div/div/div[2]/div[2]/a")).click();
        String id = $(By.xpath("/html/body/div/table/tbody[2]/tr/td")).getText();
        $(By.xpath("/html/body/div/table/tbody[2]/tr/td[9]/form/input[2]")).click();
        assertEquals(BASE_URL + "buy?id=" + id, executeJavaScript("return window.location.href;"));
    }

    @Test
    public void testBuyProductSubmit() {
        $(By.xpath("/html/body/section/div/div/div/div/div/div/div/div/div/div/div[2]/div[2]/a")).click();
        $(By.xpath("/html/body/div/table/tbody[2]/tr/td[9]/form/input[2]")).click();
        String firstName = "jay";
        String surname = "jay";
        String cardNumber = "1234567890";
        String mmyy = "0423";
        String ccv = "1234";
        String streetAddress = "1";
        String city = "1";
        String zipCode = "12345";
        String email = "123@gmail";
        $(By.xpath("/html/body/form/div/input[1]")).sendKeys(firstName);
        $(By.xpath("/html/body/form/div/input[2]")).sendKeys(surname);
        $(By.xpath("/html/body/form/div/input[3]")).sendKeys(cardNumber);
        $(By.xpath("/html/body/form/div/input[4]")).sendKeys(mmyy);
        $(By.xpath("/html/body/form/div/input[5]")).sendKeys(ccv);
        $(By.xpath("/html/body/form/div/input[6]")).sendKeys(streetAddress);
        $(By.xpath("/html/body/form/div/input[7]")).sendKeys(city);
        $(By.xpath("/html/body/form/div/input[8]")).sendKeys(zipCode);
        $(By.xpath("/html/body/form/div/input[9]")).sendKeys(email);
        $(By.id("input-button")).click();
        assertEquals(BASE_URL + "buy?" + "first-name=" + firstName + "&last-name=" + surname + "&number=" + cardNumber + "&expiry=" + mmyy + "&cvc=" + ccv + "&streetaddress=" + streetAddress + "&city=" + city + "&zipcode=" + zipCode + "&email=" + "123%40gmail", executeJavaScript("return window.location.href;"));
    }

    @Test
    public void testContact() {
        $(By.xpath("/html/body/section/div/div[2]/div[2]/div/div[2]/a")).click();
        assertEquals(BASE_URL + "contact", executeJavaScript("return window.location.href;"));
    }
}
