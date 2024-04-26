package com.qa.opencart.factory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.qa.opencart.errors.AppError;
import com.qa.opencart.exceptions.BrowserException;
import com.qa.opencart.exceptions.FrameworkException;
import com.qa.opencart.logger.Log;

public class DriverFactory {
	
	WebDriver driver;
	Properties prop;
	OptionsManager optionsmanager;
	
	public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<WebDriver>();
	
	public static String highlight;
	
    public WebDriver initDriver(Properties prop) {
    	
    	String browserName=prop.getProperty("browser");
    	
    	highlight=prop.getProperty("highlight");
    	
    	//System.out.println("browser name is : "+browserName);
    	Log.info("browser name is : "+browserName);
    	optionsmanager = new OptionsManager(prop);
    	switch (browserName.trim().toLowerCase()) {
		case "chrome":
			//driver = new ChromeDriver(optionsmanager.getChromeOptions());
			tlDriver.set(new ChromeDriver(optionsmanager.getChromeOptions()));
			break;
		case "firefox":
			//driver = new FirefoxDriver(optionsmanager.getFirefoxOptions());
			tlDriver.set(new FirefoxDriver(optionsmanager.getFirefoxOptions()));
			break;
		case "edge":
			//driver = new EdgeDriver(optionsmanager.getEdgeOptions());
			tlDriver.set(new EdgeDriver(optionsmanager.getEdgeOptions()));
			break;
		case "safari":
			//driver = new SafariDriver();
			tlDriver.set(new SafariDriver());
			break;

		default:
			//System.out.println("plz pass the right browser..." +browserName);
			Log.error("plz pass the right browser..." +browserName);
			throw new BrowserException("NO BROWSER FOUND..." +browserName);
			
		}
    	getDriver().manage().deleteAllCookies();
    	getDriver().manage().window().maximize();
    	getDriver().get(prop.getProperty("url"));
    	return driver;
    }
    
    public static WebDriver getDriver() {
    	return tlDriver.get();
    }
    
    public Properties initProp() {
    	//envName=qa,stage,prod,uat,dev
    	//mvn clean install -Denv="qa"
    	FileInputStream ip =null;
    	prop = new Properties();
    	
    String envName = System.getProperty("env");
    System.out.println("Running test on Env " + envName);
    try {
    if(envName==null) {
    	System.out.println("No env is given...hence running it on QA env...");
    	ip = new FileInputStream("./src/test/resources/config/config.qa.properties"); 
    } else {
    
    	switch (envName.toLowerCase().trim()) {
		case "qa":
			ip = new FileInputStream("./src/test/resources/config/config.qa.properties");
			break;
		case "dev":
			ip = new FileInputStream("./src/test/resources/config/config.dev.properties");
			break;
		case "stage":
			ip = new FileInputStream("./src/test/resources/config/config.stage.properties");
			break;
		case "uat":
			ip = new FileInputStream("./src/test/resources/config/config.uat.properties");
			break;
		case "prod":
			ip = new FileInputStream("./src/test/resources/config/config.properties");
			break;
		default:
			System.out.println("please pass the right env name... " + envName);
			throw new FrameworkException(AppError.ENV_NAME_NOT_FOUND + ":" +envName);
				
		}
    }
    }catch(FileNotFoundException e) {
    	e.printStackTrace();
    }
    	
       try {
			prop.load(ip);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       return prop;
    	
    }

}
