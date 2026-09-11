package tests;

import org.testng.annotations.Test;
import base.*;
import pages.*;

public class PimTest extends BaseTest{
@Test
public void TestPim() {
LoginPage loginPage = new LoginPage(driver);
    
	
    HomePage homePage =  loginPage.login ("Admin","admin123");
    homePage.dashboardPim().filtrobusqueda("99N75 425","0295").check();
    
}
}
