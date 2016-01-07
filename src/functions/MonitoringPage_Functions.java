package functions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.Properties;

public class MonitoringPage_Functions {
	
	public static Properties prop;
	public static WebDriver driver;
	
	public void HA_BF_Login (WebDriver driver, String sUserName, String sPassword) throws InterruptedException {
		
		driver.findElement(By.id(prop.getProperty("Txt_Login_Username"))).clear();
	    driver.findElement(By.id(prop.getProperty("Txt_Login_Username"))).sendKeys(sUserName);
	    driver.findElement(By.id(prop.getProperty("Txt_Login_Password"))).clear();
	    driver.findElement(By.id(prop.getProperty("Txt_Login_Password"))).sendKeys(sPassword);
	    driver.findElement(By.id(prop.getProperty("Btn_Login_Login"))).click();
	    Thread.sleep(4000);
	    
	    //Verify welcome message
	    try {
	    	WebElement welcomeTxt = driver.findElement(By.id(prop.getProperty("Lbl_SearchHotel_WelcomeMessage")));
		    String text = welcomeTxt.getAttribute("value");
		    
		    if(text.contains("Hello " + sUserName)) {
		    	System.out.println("Login Test Pass for: " + sUserName);
		    	return;
		    }
	    }
	    
	    catch(Exception e) {
	    	e.printStackTrace();
	    	System.out.println("Login Test Fail for: " + sUserName);
	    	//to show results as fail
	    	assert false;
	    }
	    
	}
	
	public String AQ_WaitForNewSourceId(WebDriver driver, String messageSourceId1, String messageSourceId2) throws IOException, InterruptedException {
		int whileCount = 0;
		
		while(messageSourceId2.equals(messageSourceId1)) {
	    	Thread.sleep(1000);
	    	whileCount ++;
	    	System.out.println("Waiting for new message for " + whileCount + " second(s).");
	    	messageSourceId2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewSourceId"))).getText();
	    		if(whileCount >= 15) {
	    			throw new IOException("Message was not moved from the Analyst Queue");
	    		}
	    }
		return messageSourceId2;
	}
	
	public int APQ_WaitForRefresh(WebDriver driver, int originalCount, int updatedCount) throws IOException, InterruptedException {
		int countCounter = 0;
    	while(updatedCount != originalCount - 1) {
    		Thread.sleep(1000);
    		String strUpdatedCount = driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_TotalCount"))).getText();
    	    updatedCount = Integer.parseInt(strUpdatedCount);
    	    countCounter ++;
    	    System.out.println("Waiting for count to update for " + countCounter +" second(s)");
    	    if(countCounter == 10) {
    	    	driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RefreshButton"))).click();
    	    	System.out.println("Refreshing again.");
    	    }
    	    if(countCounter == 20) {
    	    	throw new IOException("Count was not updated in the Action Pending Queue tab.");
    	    }  
    	}
    	return updatedCount;
	}

}
