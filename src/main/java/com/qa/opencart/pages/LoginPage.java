package com.qa.opencart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.utils.ElementUtil;

public class LoginPage {
	//page class/page Library/page object
	private WebDriver driver;
	private ElementUtil eleUtil;
	
	//1. Private By Locators
	
	private By email = By.id("input-email");
	private By password = By.id("input-password");
	private By loginButton = By.xpath("//input[@value='Login']");
	private By forgotPWdLink = By.linkText("Forgotten Password");
	private By registerLink = By.linkText("Register");
	
	//2. Public Page Class Const..
	
	public LoginPage(WebDriver driver) {
		this.driver=driver;
		eleUtil = new ElementUtil(driver);
	}
	
	//3. Public Page Actions/Method
	public String getLoginPageTitle() {
		
		String title=eleUtil.waitForTitleToBe(AppConstants.LOGIN_PAGE_TITLE, 5);
		System.out.println("login page title : "+title);
		return title;
	}
	
	public String getLoginPageURL() {
		String url=eleUtil.waitForURLFraction(AppConstants.LOGIN_PAGE_URL_FRACTION, 5);
		System.out.println("login page url : "+url);
		return url;
	}
	
	public boolean isForgotPwdLinkExist() {
		return eleUtil.elementDisplay(forgotPWdLink);
	}
	
	public AccountsPage doLogin(String userName, String pwd) {
		System.out.println("User cred "+ userName+ " : "+pwd);
        eleUtil.waitForElementVisible(email,10).sendKeys(userName);
        eleUtil.doSendKeys(password, pwd);
        eleUtil.doClick(loginButton);
		return new AccountsPage(driver);
	}
	
	public RegistrationPage navigateToRegisterPage() {
		eleUtil.waitForElementVisible(registerLink, 10).click();
		return new RegistrationPage(driver);
	}
	

}
