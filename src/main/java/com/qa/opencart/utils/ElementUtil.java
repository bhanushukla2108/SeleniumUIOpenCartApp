package com.qa.opencart.utils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qa.opencart.exceptions.ElementException;
import com.qa.opencart.factory.DriverFactory;

public class ElementUtil {

	private WebDriver driver;
	private JavaScriptUtil jsUtil;
	
	private final String DEFAULT_ELEMENT_TIME_OUT_MESSAGE="Time out...Element is not found...";
	private final String DEFAULT_ALERT_TIME_OUT_MESSAGE="Time out...Alert is not found...";

	private void nullBlankCheck(String value) {
		if (value == null || value.length() == 0) {
			throw new ElementException("visible text cannot be null");
		}
	}

	public ElementUtil(WebDriver driver) {
		this.driver = driver;
		jsUtil = new JavaScriptUtil(driver);
	}

	public WebElement getElement(By locator) {
		WebElement element = null;
		try {
			element = driver.findElement(locator);
			if(Boolean.parseBoolean(DriverFactory.highlight)) {
				jsUtil.flash(element);
			}
			
		} catch (NoSuchElementException e) {
			System.out.println("Element is not present on the page");
			e.printStackTrace();
			return null;
		}
		return element;
	}

	public void doSendKeys(By locator, String value) {
		nullBlankCheck(value);
		getElement(locator).clear();
		getElement(locator).sendKeys(value);
	}

	// with wait
	public void doSendKeys(By locator, String value, int timeout) {
		nullBlankCheck(value);
		waitForElementVisible(locator, timeout).sendKeys(value);
	}

	public void doClick(By locator) {
		getElement(locator).click();
	}

	public void doClick(By locator, int timeout) {
		waitForElementVisible(locator, timeout).click();
	}

	public String doGetElementText(By locator) {
		return getElement(locator).getText();
	}

	public String doElementgetAttribute(By locator, String attrName) {
		return getElement(locator).getAttribute(attrName);
	}

	public boolean elementDisplay(By locator) {
		if (getElement(locator).isDisplayed()) {
			return true;
		}
		return false;
	}

	public boolean isElementExist(By locator) {
		if (getElements(locator).size() == 1) {
			return true;
		}
		return false;

	}

	public boolean multipleElementExist(By locator) {
		if (getElements(locator).size() > 0) {
			return true;
		}
		return false;

	}

	public List<WebElement> getElements(By locator) {
		return driver.findElements(locator);
	}

	public int getElementsCount(By locator) {
		return getElements(locator).size();
	}

	public ArrayList<String> getElementsTextList(By locator) {
		List<WebElement> eleList = getElements(locator);
		ArrayList<String> eleText = new ArrayList<String>();
		for (WebElement e : eleList) {
			String text = e.getText();
			if (text.length() != 0) {
				eleText.add(text);
			}
		}
		return eleText;
	}

	public ArrayList<String> getElementAttributeList(By locator, String attrName) {
		List<WebElement> eleList = getElements(locator);
		ArrayList<String> eleAttrList = new ArrayList<String>();

		for (WebElement e : eleList) {
			String attrValue = e.getAttribute(attrName);
			if (attrValue.length() != 0) {
				eleAttrList.add(attrValue);
			}
		}
		return eleAttrList;

	}

	// *************select based dropdown utils**********************

	public void doSelectByIndexDropDown(By locator, int index) {
		Select idx = new Select(getElement(locator));
		idx.selectByIndex(index);

	}

	public void doSelectByVisibleTextDropDown(By locator, String visibleText) {
		nullBlankCheck(visibleText);
		Select str = new Select(getElement(locator));
		str.selectByVisibleText(visibleText);

	}

	public void doSelectByValueDropDown(By locator, String value) {
		nullBlankCheck(value);
		Select str = new Select(getElement(locator));
		str.selectByValue(value);

	}

	// get all option in the form of string not webelement
	public List<String> getDropDownOptionsTextList(By locator) {
		List<WebElement> optionList = getDropDownOptionsList(locator);
		List<String> optionsTextList = new ArrayList<String>();
		for (WebElement e : optionList) {
			String optionText = e.getText();
			optionsTextList.add(optionText);
		}
		return optionsTextList;
	}

	public List<WebElement> getDropDownOptionsList(By locator) {
		Select select = new Select(getElement(locator));
		return select.getOptions();
	}

	public int getDropDownValues(By locator) {
		return getDropDownOptionsList(locator).size();
	}

	public void doSelectDropDownValue(By locator, String value) {
		nullBlankCheck(value);
		for (WebElement e : getDropDownOptionsList(locator)) {
			String text = e.getText();
			System.out.println(text);
			if (text.equals(value)) {
				e.click();
				break;
			}

		}

	}

	public void printSelectDropDownValue(By locator) {
		for (WebElement e : getDropDownOptionsList(locator)) {
			String text = e.getText();
			System.out.println(text);

		}

	}

	// without select class, select the dropdown value
	public void DoSelectValueFromDropDown(By locator, String value) {
		for (WebElement e : getElements(locator)) {
			String countries = e.getText();
			System.out.println(countries);
			if (countries.contains(value)) {
				e.click();
				break;
			}
		}
	}

	public void doSearch(By searchlocator, By locator, String searchKey, String name) throws InterruptedException {
		doSendKeys(searchlocator, searchKey);
		Thread.sleep(3000);
		for (WebElement e : getElements(locator)) {
			String text = e.getText();
			if (text.contains(name)) {
				e.click();
				break;
			}
		}
	}

	// ***********WaitUtils**************
	/**
	 * An expectation for checking an element is visible and enabled such that you can click it.
	 * 
	 */
	public void clickWhenReady(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
	}
	/**
	 * An expectation for checking that an element is present on the DOM of a page.
	 * This does notnecessarily mean that the element is visible.
	 */

	public WebElement waitForElementPresence(int time, By locator) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(time));
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	/**
	 * An expectation for checking that an element is present on the DOM of a page
	 * and visible. Visibility means that the element is not only displayed but also
	 * has a height and width that isgreater than 0.
	 */
	public WebElement waitForElementVisible(By locator,int time) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(time));
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
	
	public WebElement waitForElementVisible(int time, By locator, int intervalTime) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(time),Duration.ofMillis(intervalTime));
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
	/**
	 * An expectation for checking that there is at least one element present on a web page.
	 * @param time
	 * @param locator
	 * @return
	 */
	
	public List<WebElement> waitForElementsPresence(int time, By locator) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(time));
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
	}
	/**
	 * An expectation for checking that all elements present on the web page that match the locatorare visible. 
	 * Visibility means that the elements are not only displayed but also have a heightand width that is greater than 0.
	 * @param time
	 * @param locator
	 * @return
	 */
	public List<WebElement> waitForElementsVisible(int time, By locator) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(time));
		return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
	}
	
	public List<WebElement> waitForElementsVisibleWithFluentWait(int time, int interval,By locator) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				               .withTimeout(Duration.ofSeconds(time))
				               .pollingEvery(Duration.ofSeconds(interval))
				               .withMessage(DEFAULT_ELEMENT_TIME_OUT_MESSAGE);
		return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
	}

	public String waitForTitleContains(String title, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		try {
			if (wait.until(ExpectedConditions.titleContains(title))) {
				return driver.getTitle();
			}
		} catch (Exception e) {
			System.out.println("title is not found within timeout");
		}
		return null;

	}

	public String waitForTitleToBe(String title, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		try {
			if (wait.until(ExpectedConditions.titleIs(title))) {
				return driver.getTitle();
			}
		} catch (Exception e) {
			System.out.println("title is not found within timeout");
		}
		return null;

	}

	public String waitForURLFraction(String url, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		try {
			if (wait.until(ExpectedConditions.urlContains(url))) {
				return driver.getCurrentUrl();
			}
		} catch (Exception e) {
			System.out.println("url is not found within timeout");
		}
		return null;

	}

	public String waitForURLTobe(String url, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		try {
			if (wait.until(ExpectedConditions.urlToBe(url))) {
				return driver.getCurrentUrl();
			}
		} catch (Exception e) {
			System.out.println("url is not found within timeout");
		}
		return null;

	}
	public Alert waitForwaitForJSAlertWithFluentWait(int time, int interval) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				               .withTimeout(Duration.ofSeconds(time))
				               .pollingEvery(Duration.ofSeconds(interval))
				               .ignoring(NoAlertPresentException.class)
				               .withMessage(DEFAULT_ALERT_TIME_OUT_MESSAGE);
		return wait.until(ExpectedConditions.alertIsPresent());
	}
	public Alert waitForJSAlert(int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		return wait.until(ExpectedConditions.alertIsPresent());

	}
	public String getAlertText(int timeout) {
		return waitForJSAlert(timeout).getText();

	}
	public void acceptAlert(int timeout) {
		waitForJSAlert(timeout).accept();

	}
	
	public void dismissAlert(int timeout) {
		waitForJSAlert(timeout).dismiss();

	}
	
	public void alertSendKeys(int timeout,String value) {
		waitForJSAlert(timeout).sendKeys(value);

	}
	
	public boolean waitForWindow(int totalNumberOfWindowToBe, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		return wait.until(ExpectedConditions.numberOfWindowsToBe(totalNumberOfWindowToBe));


	}
	/**
	 * An expectation for checking whether the given frame is available to switch to. 
       If the frame is available it switches the given driver to the specified frame.
	 * @param locator
	 * @param timeOut
	 */
	
	public void waitForFrameAndSwitchToIt(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
	}
	
	public void waitForFrameAndSwitchToIt(int frameIndex, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameIndex));
	}
	
	public void waitForFrameAndSwitchToIt(WebElement frameElement, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameElement));
	}
	
	public WebElement retryingElement(By locator, int timeOut) {
		WebElement element = null;
		int attempts=0;
		while(attempts<timeOut) {
			try {
			element=getElement(locator);
			System.out.println("Element is found...." + locator + " in attempt "+attempts);
			break;
			}
			catch(NoSuchElementException e) {
				System.out.println("element is not found..."+ " in attempts "+attempts);
                TimeUtil.defaultTime();
			}
			attempts++;
		}
		if(element==null) {
			System.out.println("element is not found...tried for "+timeOut + " times "+ " with the interval of "+ 500 + " millisecods");
			throw new ElementException("No Such Element");
		}
		return element;
	}

	public WebElement retryingElement(By locator, int timeOut, int intervalTime) {
		WebElement element = null;
		int attempts=0;
		while(attempts<timeOut) {
			try {
			element=getElement(locator);
			System.out.println("Element is found...." + locator + " in attempt "+attempts);
			break;
			}
			catch(NoSuchElementException e) {
				System.out.println("element is not found..."+ " in attempts "+attempts);
                TimeUtil.applywait(intervalTime);
			}
			attempts++;
		}
		if(element==null) {
			System.out.println("element is not found...tried for "+timeOut + " times "+ " with the interval of "+ intervalTime + " seconds");
			throw new ElementException("No Such Element");
		}
		return element;
	}

	
	

}
