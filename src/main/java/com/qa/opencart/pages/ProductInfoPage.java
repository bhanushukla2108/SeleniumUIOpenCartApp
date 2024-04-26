package com.qa.opencart.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.qa.opencart.utils.ElementUtil;

public class ProductInfoPage {

	private WebDriver driver;
	private ElementUtil eleUtil;
	private Map<String,String> productMap = new HashMap<String,String>();
	
	//1. Private By Locators
	
	private By productHeader = By.tagName("h1");
	private By images = By.cssSelector("ul.thumbnails img");
	private By productMetaData = By.xpath("(//div[@id='content']//ul[@class='list-unstyled'])[1]/li");
	private By productPricing = By.xpath("(//div[@id='content']//ul[@class='list-unstyled'])[2]/li");
	
	//2. Public Page Class Const..
	
	public ProductInfoPage(WebDriver driver) {
		this.driver=driver;
		eleUtil = new ElementUtil(driver);
	}
	
	public String getProductHeader() {
	String header =	eleUtil.doGetElementText(productHeader);
	System.out.println(header);
	return header;
	}
	
	public int getProductImagesCount() {
      int totalImages =  eleUtil.waitForElementsVisible(10, images).size();
      System.out.println("Image count for "+getProductHeader()+ " : " +totalImages);
      return totalImages;
	}
	
	private void getProductMetaData() {
		List<WebElement> metaList = eleUtil.getElements(productMetaData);
		for(WebElement e : metaList) {
			String text=e.getText();
		String metakey = text.split(":")[0].trim();
		String metavalue = text.split(":")[1].trim();
		productMap.put(metakey, metavalue);
			
		}
		
	}
	
	private void getProductPriceData() {
		List<WebElement> priceList = eleUtil.getElements(productPricing);
               String price = priceList.get(0).getText();
               String exTaxPrice = priceList.get(1).getText().split(":")[1].trim();
               productMap.put("productprice", price);
               productMap.put("extaxprice", exTaxPrice);
			
		}
	
	public Map<String, String> getProductDetailsMap() {
		productMap.put("header", getProductHeader());
		productMap.put("productimages", String.valueOf(getProductImagesCount()));
		getProductMetaData();
		getProductPriceData();
		return productMap;
	}
		
	
}
