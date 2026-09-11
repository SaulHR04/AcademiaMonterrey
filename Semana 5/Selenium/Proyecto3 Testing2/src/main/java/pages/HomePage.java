package pages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.BasePage;

public class HomePage extends BasePage {

    
	private static final By PIMDASH = By.cssSelector("a[href*='viewPimModule']");	
	private static final By TITLE = By.className("oxd-topbar-header-title");
	public HomePage(WebDriver driver) {
		super(driver);
	}
	
	public String GetTitlte() {
		return text(TITLE);
	}
	public PimPage dashboardPim() {
		click(PIMDASH);
		return new PimPage(driver);
	}


}
