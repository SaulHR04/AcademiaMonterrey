package pages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.BasePage;
public class PimPage extends BasePage{
	private static final By EMPLOYEENAME = By.cssSelector("input[placeholder='Type for hints...']");
	private static final By EMPLOYEEID = By.cssSelector(".oxd-grid-item:nth-child(2) input.oxd-input");
	private static final By BUTTON = By.cssSelector("button.orangehrm-left-space");
	private static final By CHECKLIST = By.cssSelector(".oxd-table-card:first-child .oxd-checkbox-input");
	
	public PimPage(WebDriver driver) {
		super(driver);
	}
	
	public PimPage filtrobusqueda(String name, String id) {
	    write(EMPLOYEENAME, name);
	    write(EMPLOYEEID, id);
	    click(BUTTON);
	    return this;
	}
	public PimPage check() {
		click(CHECKLIST);
		return this;
	}
}
