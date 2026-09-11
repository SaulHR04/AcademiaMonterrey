package tests;

import org.testng.annotations.Test;

import org.testng.Assert;

import pages.HomePage;
import pages.LoginPage;
import base.*;

public class LoginTest extends BaseTest{


@Test
public void testLogin() {
	LoginPage loginPage = new LoginPage(driver);
    
	
    HomePage homePage =  loginPage.login ("Admin","admin123");
    Assert.assertEquals(homePage.GetTitlte(), "Dashboard");
   
    
    
    
}

}
