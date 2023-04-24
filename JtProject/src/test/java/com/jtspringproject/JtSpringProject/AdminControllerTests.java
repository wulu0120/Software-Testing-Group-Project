package com.jtspringproject.JtSpringProject;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.jtspringproject.JtSpringProject.controller.AdminController;
import com.jtspringproject.JtSpringProject.controller.UserController;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class AdminControllerTests {
    static String BASIC_URL = "http://localhost:8080/";
    @Autowired
    private AdminController adminController;

    @Autowired
    private UserController userController;

    private Model model;

    @BeforeEach
    public void setModel() {
        model = new ConcurrentModel();
    }

    // Helper function to connect database and create statement
    private Statement createStmt () throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/springproject","root","");
        Statement stmt = con.createStatement();
        return stmt;
    }

    // ------------- Testing Part ---------------

    // Blackbox - getCategory(): check return url value
    @Test
    @Order(1)
    void testGetCategoryURL() {
        Assertions.assertEquals("categories", adminController.getcategory());
    }

    // Blackbox / Whitebox - getcategory(): check status code
    @Test
    @Order(2)
    void testGetCategoryStatus() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(BASIC_URL + "/admin/categories", String.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // Blackbox - addcategorytodb(): check return url value
    @Test
    @Order(3)
    void testAddCategoryToDBurl() {
        String catname = "test1";
        String returnStr = adminController.addcategorytodb(catname);
        Assertions.assertEquals("redirect:/admin/categories", returnStr);
    }

    // Whitebox - addcategorytodb(): check database insert
    @Test
    @Order(4)
    void testAddCategoryToDBdata() throws Exception {
        String catname = "test1";
        Statement stmt = createStmt();
        ResultSet rst = stmt.executeQuery("select * from categories where name = '"+catname+"';");
        Assertions.assertTrue(rst.next());
    }

    // Whitebox - addcategorytodb(): insert duplicate category
    @Test
    @Order(5)
    void testAddCategoryDup() throws Exception {
        String catname = "test1";
        adminController.addcategorytodb(catname);
        Statement stmt = createStmt();
        ResultSet rst = stmt.executeQuery("select * from categories where name = '"+catname+"';");

        int count = 0;
        while(rst.next()) {
            count++;
        }
        Assertions.assertEquals(2, count);
    }

    // Blackbox - removeCategoryDB(): negative category id  [FAIL]
    @Test
    @Order(6)
    void testRemoveCategoryDBNeg() {
        int removeId = -1;
        Assertions.assertThrows(MismatchedInputException.class, () -> {adminController.removeCategoryDb(removeId);});
    }

    // Blackbox - removeCategoryDB(): zero category id  [FAIL]
    @Test
    @Order(7)
    void testRemoveCategoryDBZero() {
        int removeId = 0;
        Assertions.assertThrows(MismatchedInputException.class, () -> {adminController.removeCategoryDb(removeId);});
    }

    // Blackbox - removeCategoryDB(): normal, test return url
    @Test
    @Order(8)
    void testRemoveCategoryDBurl() {
        int removeId = 12;
        String returnStr = adminController.removeCategoryDb(removeId);
        Assertions.assertEquals("redirect:/admin/categories", returnStr);
    }

    // Whitebox - removeCategoryDB(): normal, check database value
    @Test
    @Order(9)
    void testRemoveCategoryDBdata() throws Exception {
        int removeId = 12;
        Statement stmt = createStmt();
        ResultSet rst = stmt.executeQuery("select * from categories where categoryid = '"+removeId+"';");
        Assertions.assertFalse(rst.next());
    }

    // Whitebox - removeCategoryDB(): not exist
    @Test
    @Order(10)
    void testRemoveCategoryNotExist() throws Exception {
        int removeId = 12;
        adminController.removeCategoryDb(removeId);
        Statement stmt = createStmt();
        ResultSet rst = stmt.executeQuery("select * from categories where categoryid = '"+removeId+"';");
        Assertions.assertFalse(rst.next());
    }

    // Blackbox - updateCategoryDB(): negative category id  [FAIL]
    @Test
    @Order(11)
    void testUpdateCategoryDBNeg() {
        int updateId = -1;
        String newName = "category8";
        Assertions.assertThrows(MismatchedInputException.class, () -> {adminController.updateCategoryDb(updateId, newName);});
    }

    // Blackbox - updateCategoryDB(): zero category id  [FAIL]
    @Test
    @Order(12)
    void testUpdateCategoryDBZero() {
        int updateId = 0;
        String newName = "category8";
        Assertions.assertThrows(MismatchedInputException.class, () -> {adminController.updateCategoryDb(updateId, newName);});
    }

    // Blackbox - updateCategory(): normal, check return url
    @Test
    @Order(13)
    void testUpdateCategoryDBurl() {
        int updateId = 8;
        String newName = "category8";
        Assertions.assertEquals("redirect:/admin/categories", adminController.updateCategoryDb(updateId, newName));
    }

    // Whitebox - updateCategory(): check database update
    @Test
    @Order(14)
    void testUpdateCategoryDB() throws Exception {
        int updateId = 8;
        Statement stmt = createStmt();
        ResultSet rst = stmt.executeQuery("select name from categories where categoryid = '"+updateId+"';");

        String result = "";
        while (rst.next()) {
            result = rst.getString("name");
        }
        Assertions.assertEquals("category8", result);
    }

    @Test
    @Order(15)
    void testGetProductURL() {
        assertEquals("products", adminController.getproduct(model));
    }

    // Blackbox / Whitebox - getproduct(): check status code
    @Test
    @Order(16)
    void testGetProductStatus() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(BASIC_URL + "/admin/products", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // Blackbox - addproduct() - check return url
    @Test
    @Order(17)
    void testAddProductURL() {
        assertEquals("productsAdd", adminController.addproduct(model));
    }

    // Blackbox / Whitebox - addproduct(): check status code
    @Test
    @Order(18)
    void testAddProductStatus() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(BASIC_URL + "/admin/products/add", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // Blackbox - updateproduct() - negative id
    @Test
    @Order(19)
    void testUpdateProductNegId() {
        int updateProductId = -5;
        Assertions.assertThrows(MismatchedInputException.class, () -> {adminController.updateproduct(updateProductId, model);});
    }

    // Blackbox - updateproduct() - check return url
    @Test
    @Order(20)
    void testUpdateProductZeroId() {
        int updateProductId = 0;
        Assertions.assertThrows(MismatchedInputException.class, () -> {adminController.updateproduct(updateProductId, model);});
    }

    // Blackbox - updateproduct() - check return url
    @Test
    @Order(21)
    void testUpdateProductURL() {
        int updateProductId = 14;
        assertEquals("productsUpdate", adminController.updateproduct(updateProductId, model));
    }

    // Whitebox - updateproduct() - check specific model attribute (same as db)
    @Test
    @Order(22)
    void testUpdateProductAttribute() throws Exception {
        int updateProductId = 14;
        Statement stmt = createStmt();
        ResultSet rst = stmt.executeQuery("select * from products where id = '"+updateProductId+"';");

        adminController.updateproduct(updateProductId, model);

        String pname = "", pdescription = "", pimage = "", pcategoryName = "";
        int pid = 0, pprice = 0, pweight = 0, pquantity = 0, pcategory = 0;

        if (rst.next()) {
            pid = rst.getInt(1);
            pname = rst.getString(2);
            pimage = rst.getString(3);
            pcategory = rst.getInt(4);
            pquantity = rst.getInt(5);
            pprice =  rst.getInt(6);
            pweight =  rst.getInt(7);
            pdescription = rst.getString(8);
        }

        Statement stmt1 = createStmt();
        ResultSet rst1 = stmt1.executeQuery("select * from categories where categoryid = '"+pcategory+"';");
        if (rst1.next()) {
            pcategoryName = rst1.getString(2);
        }

        assertEquals(pid, model.getAttribute("pid"));
        assertEquals(pname, model.getAttribute("pname"));
        assertEquals(pimage, model.getAttribute("pimage"));
        assertEquals(pcategoryName, model.getAttribute("pcategory"));
        assertEquals(pquantity, model.getAttribute("pquantity"));
        assertEquals(pprice, model.getAttribute("pprice"));
        assertEquals(pweight, model.getAttribute("pweight"));
        assertEquals(pdescription, model.getAttribute("pdescription"));
    }

    // Blackbox - updateproducttodb - check return url
    @Test
    @Order(23)
    void testUpdateProductToDbURL() {
        int updateProductId = 16;
        String newName = "mango";
        int newPrice = 10;
        int newWeight = 100;
        int newQuantity = 20;
        String newDescripton = "This is a mango";
        String newImage = "1.jpg";

        assertEquals("redirect:/admin/products",
                adminController.updateproducttodb(updateProductId, newName, newPrice, newWeight, newQuantity, newDescripton, newImage));
    }

    // Whitebox - updateproducttodb - check database info
    @Test
    @Order(24)
    void testUpdateProductToDbData() throws Exception {
        int updateProductId = 16;
        String newName = "mango111";
        int newPrice = 30;
        int newWeight = 80;
        int newQuantity = 10;
        String newDescripton = "This is a mango111";
        String newImage = "1.jpg";
        adminController.updateproducttodb(updateProductId, newName, newPrice, newWeight, newQuantity, newDescripton, newImage);

        Statement stmt = createStmt();
        ResultSet rst = stmt.executeQuery("select * from products where id = '"+updateProductId+"';");

        adminController.updateproduct(updateProductId, model);

        String pname = "", pdescription = "", pimage = "", pcategoryName = "";
        int pprice = 0, pweight = 0, pquantity = 0;

        if (rst.next()) {
            pname = rst.getString(2);
            pimage = rst.getString(3);
            pquantity = rst.getInt(5);
            pprice =  rst.getInt(6);
            pweight =  rst.getInt(7);
            pdescription = rst.getString(8);
        }

        assertEquals(newName, pname);
        assertEquals(newImage, pimage);
        assertEquals(newQuantity, pquantity);
        assertEquals(newPrice, pprice);
        assertEquals(newWeight, pweight);
        assertEquals(newDescripton, pdescription);
    }

    // Blackbox - removeProductDb - check return url
    @Test
    @Order(25)
    void testRemoveProductDbURL() {
        int removeId = 16;
        assertEquals("redirect:/admin/products", adminController.removeProductDb(removeId));
    }

    // Blackbox - removeProductDb - check return url when removeId not exist
    @Test
    @Order(26)
    void testRemoveProductDbIdNotExist() {
        int removeId = 16;
        assertEquals("redirect:/admin/products", adminController.removeProductDb(removeId));
    }

    // Blackbox - removeProductDb: negative category id  [FAIL]
    @Test
    @Order(27)
    void testRemoveProductDBNeg() {
        int removeId = -1;
        Assertions.assertThrows(MismatchedInputException.class, () -> {adminController.removeProductDb(removeId);});
    }

    // Blackbox - removeProductDb: zero category id  [FAIL]
    @Test
    @Order(28)
    void testRemoveProductDBZero() {
        int removeId = 0;
        Assertions.assertThrows(MismatchedInputException.class, () -> {adminController.removeProductDb(removeId);});
    }

    // Whitebox - removeProductDb - check database update
    @Test
    @Order(29)
    void testRemoveProductDbData() throws Exception {
        int removeId = 15;
        adminController.removeProductDb(removeId);
        Statement stmt = createStmt();
        ResultSet rst = stmt.executeQuery("select * from products where id = '"+removeId+"';");
        assertFalse(rst.next());
    }

    // Blackbox - postproduct: check return url
    @Test
    @Order(30)
    void testPostProductURL() {
        assertEquals("redirect:/admin/categories", adminController.postproduct());
    }

    // Blackbox / Whitebox - postproduct: check status code
    @Test
    @Order(31)
    void testPostProductStatus() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(BASIC_URL + "/admin/products", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // Blackbox - addproducttodb: check return url
    @Test
    @Order(32)
    void testAddProductToDbURL() {
        String name = "steam game";
        String catid = "test1";
        int price = 60;
        int weight = 10;
        int quantity = 999;
        String descripton = "New image added";
        String image = "2.jpg";

        assertEquals("redirect:/admin/products", adminController.addproducttodb(name, catid, price, weight, quantity, descripton, image));
    }

    // Whitebox - addproducttodb: check database update
    @Test
    @Order(33)
    void testAddProductToDbData() throws Exception {
        String name = "steam game 111";
        String catid = "test1";
        int price = 30;
        int weight = 30;
        int quantity = 1111;
        String descripton = "This is a popular steam game";
        String image = "1.jpg";

        adminController.addproducttodb(name, catid, price, weight, quantity, descripton, image);

        Statement stmt = createStmt();
        ResultSet rst = stmt.executeQuery("select * from products where name = '"+name+"';");

        String name1 = "", catname = "";
        int catid1 = 0, price1 = 0, weight1 = 0, quantity1 = 0;
        String descripton1 = "", image1 = "";

        if (rst.next()) {
            name1 = rst.getString("name");
            catid1 = rst.getInt("categoryid");
            image1 = rst.getString("image");
            quantity1 = rst.getInt("quantity");
            price1 = rst.getInt("price");
            weight1 = rst.getInt("weight");
            descripton1 = rst.getString("description");
        }

        Statement stmt1 = createStmt();
        ResultSet rst1 = stmt1.executeQuery("select * from categories where categoryid = '"+catid1+"';");
        if (rst1.next()) {
            catname = rst1.getString("name");
        }
        assertEquals(name, name1);
        assertEquals(catid, catname);
        assertEquals(image, image1);
        assertEquals(quantity, quantity1);
        assertEquals(price, price1);
        assertEquals(weight, weight1);
        assertEquals(descripton, descripton1);
    }

    // Blackbox - returnIndex(): check return url value
    @Test
    public void testReturnIndexURL() {
        Assertions.assertEquals("userLogin", adminController.returnIndex());
    }

    // Blackbox / Whitebox - returnIndex(): check status code
    @Test
    public void testReturnIndexStatus() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(BASIC_URL + "/logout", String.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // Blackbox - index(): check return url value when username is empty
    @Test
    public void testIndexURLEmptyUsername() {
        adminController.usernameforclass = "";
        Assertions.assertEquals("userLogin", adminController.index(model));
    }

    // Blackbox - index(): check return url value when username is not empty
    @Test
    public void testIndexURLUniqueUsername() {
        adminController.usernameforclass = "admin";
        Assertions.assertEquals("index", adminController.index(model));
    }

    // Blackbox / Whitebox - index(): check status code
    @Test
    public void testIndexStatus() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(BASIC_URL + "/index", String.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // Blackbox - userLog(): check return url value
    @Test
    public void testUserLogURL() {
        Assertions.assertEquals("userLogin", adminController.userlog(model));
    }

    // Blackbox / Whitebox - userLog(): check status code
    @Test
    public void testUserLogStatus() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(BASIC_URL + "/userloginvalidate", String.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // Blackbox - userLogin(): check return url value when correct login is provided
    @Test
    public void testUserLoginCorrectCredentialsURL() {
        userController.newUseRegister("user", "123", "user@gmail.com");
        Assertions.assertEquals("redirect:/index", adminController.userlogin("user", "123", model));
    }

    // Blackbox / Whitebox - userLogin(): check usernameforclass matches
    @Test
    public void testUserLoginCorrectCredentialsFieldValues() {
        userController.newUseRegister("user", "123", "user@gmail.com");
        adminController.userlogin("user", "123", model);
        Assertions.assertEquals("user", adminController.usernameforclass);
    }

    // Blackbox / Whitebox - userLogin(): check user exists in db
    @Test
    public void testUserLoginUserExistsTrue() throws SQLException {
        userController.newUseRegister("user", "123", "user@gmail.com");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/springproject","root","");
        Statement stmt = con.createStatement();
        ResultSet rst = stmt.executeQuery("select * from users where username = '"+ "user" +"' and password = '"+ "123" +"' ;");
        Assertions.assertTrue(rst.next());
    }

    // Blackbox / Whitebox - userLogin(): check if user doesn't exist in db
    @Test
    public void testUserLoginUserExistsFalse() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/springproject","root","");
        Statement stmt = con.createStatement();
        ResultSet rst = stmt.executeQuery("select * from users where username = '"+ "userWrong" +"' and password = '"+ "123" +"' ;");
        Assertions.assertFalse(rst.next());
    }

    // Blackbox - userLogin(): check return url value when incorrect login is provided
    @Test
    public void testUserLoginIncorrectCredentialsURL() {
        Assertions.assertEquals("userLogin", adminController.userlogin("userWrong", "123", model));
    }

    // Blackbox - adminLogin(): check return url value
    @Test
    public void testAdminLoginURL() throws Exception{
        Assertions.assertEquals("adminlogin", adminController.adminlogin(model));
    }

    // Blackbox / Whitebox - adminLogin(): check status code
    @Test
    public void testAdminLoginStatus() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(BASIC_URL + "/admin", String.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // Blackbox - adminHome(): check return url value when adminlogcheck = 0
    @Test
    public void testAdminHomeWithAdminLogCheckEquals0URL() {
        adminController.adminlogcheck = 0;
        Assertions.assertEquals("redirect:/admin", adminController.adminHome(model));
    }

    // Blackbox - adminHome(): check return url value when adminlogcheck != 0
    @Test
    public void testAdminHomeWithAdminLogCheckNotEquals0() {
        adminController.adminlogcheck = 1;
        Assertions.assertEquals("adminHome", adminController.adminHome(model));
    }

    // Blackbox - adminHome(): check return url value when adminlogcheck = 0
    @Test
    public void testAdminHomeStatus() {
        adminController.adminlogcheck = 0;
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(BASIC_URL + "/adminhome", String.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // Blackbox - adminlog(): check return url value
    @Test
    public void testAdminLogURL() {
        Assertions.assertEquals("adminlogin", adminController.adminlog(model));
    }

    // Blackbox / Whitebox - adminlog(): check status code
    @Test
    public void testAdminLogStatus() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(BASIC_URL + "/loginvalidate", String.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // Blackbox - adminLogin(username, password, model): check return url value for correct login values
    @Test
    public void testAdminLoginCorrectUsernameAndPasswordURL() {
        Assertions.assertEquals("redirect:/adminhome", adminController.adminlogin("admin", "123", model));
    }

    // Blackbox - adminLogin(username, password, model): check adminlogcheck value
    @Test
    public void testAdminLoginCorrectUsernameAndPasswordFieldValue() {
        Assertions.assertEquals(0, adminController.adminlogcheck);
        adminController.adminlogin("admin", "123", model);
        Assertions.assertEquals(1, adminController.adminlogcheck);
    }

    // Blackbox - adminLogin(username, password, model): check return url value for incorrect login values
    @Test
    public void testAdminLoginIncorrectUsernameAndPasswordURL() {
        Assertions.assertEquals("adminlogin", adminController.adminlogin("admin", "1234", model));
    }

    // Blackbox / Whitebox - adminLogin(username, password, model): check status code
    @Test
    public void testAdminLoginValidateStatus() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(BASIC_URL + "loginvalidate", String.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
