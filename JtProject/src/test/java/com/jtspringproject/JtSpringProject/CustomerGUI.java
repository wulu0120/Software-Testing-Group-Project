package com.jtspringproject.JtSpringProject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.springframework.boot.test.context.SpringBootTest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;
import static com.codeborne.selenide.CollectionCondition.*;

@SpringBootTest
public class CustomerGUI {
    static String BASE_URL = "http://localhost:8080/";

    @BeforeAll
    public static void login() {
        open(BASE_URL + "admin");
        $(By.id("username")).sendKeys("admin");
        $(By.id("password")).sendKeys("123");
        $(By.xpath("/html/body/div/div/form/input")).click();
    }

    // Helper function to return row number in category table
    private int countCustomer() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/springproject","root","");
        Statement stmt = con.createStatement();
        ResultSet rst = stmt.executeQuery("select * from users;");
        int count = 1;
        while(rst.next()) {
            count++;
        }
        return count;
    }

    // Test show customer route from admin control panel
    @Test
    public void testShowCustomerRoute() {
        open(BASE_URL + "adminhome");
        $(By.xpath("/html/body/div[2]/div/div[3]/div/div/a")).click();
        assertEquals(BASE_URL + "admin/customers", executeJavaScript("return window.location.href;"));
    }

    // Test show customer table attributes and action names
    @Test
    public void testShowCategoryTableStructure() {
        open(BASE_URL + "admin/customers");
        $$("tbody tr th").shouldHave(texts("UserId", "Customer Name", "Email", "Address"));
    }

    // Test show customer table element size match with database
    @Test
    public void testShowCustomerTableSize() throws Exception {
        open(BASE_URL + "admin/customers");
        $$("tbody tr").filter(visible).shouldHave(size(countCustomer()));
    }
}
