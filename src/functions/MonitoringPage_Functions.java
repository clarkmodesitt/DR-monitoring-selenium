package functions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.Properties;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class MonitoringPage_Functions {
	
	public static Properties prop;
	public static WebDriver driver;
	
	public void Synthesys_Login (WebDriver driver, String sUserName, String sPassword) throws InterruptedException {
		
		driver.findElement(By.id(prop.getProperty("Txt_Login_Username"))).clear();
	    driver.findElement(By.id(prop.getProperty("Txt_Login_Username"))).sendKeys(sUserName);
	    driver.findElement(By.id(prop.getProperty("Txt_Login_Password"))).clear();
	    driver.findElement(By.id(prop.getProperty("Txt_Login_Password"))).sendKeys(sPassword);
	    driver.findElement(By.xpath(prop.getProperty("Btn_Login_SignIn"))).click();
	    
	}

	public void AQ_SelectKGForTesting(WebDriver driver, String knowledgeGraph) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, 15);
		String startingKG = driver.findElement(By.xpath(prop.getProperty("Btn_Monitoring_KGDropdownButton"))).getText();
		if(startingKG.equals("Select a knowledge graph...")) {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(prop.getProperty("Lbl_AnalystQueue_OopsMessage"))));
		}
		else {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(prop.getProperty("Lbl_AnalystQueue_MessageCount"))));
		}

		//Select KG
		String storedKG = driver.findElement(By.xpath(prop.getProperty("Btn_Monitoring_KGDropdownButton"))).getText();
		System.out.println(storedKG);
		if(storedKG.equals(knowledgeGraph) == false) {
			driver.findElement(By.xpath(prop.getProperty("Btn_Monitoring_KGDropdownButton"))).click();
			driver.findElement(By.xpath(".//*[@id='applicationHost']/div/nav/div/div/ul/li[a/span = \"" + knowledgeGraph + "\"]/a/span")).click();
			Thread.sleep(5000);
		}
	}

	public void AQ_TakeActionFollowUp(WebDriver driver) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, 15);

		driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_StatusDropdownButton"))).click();
		driver.findElement(By.linkText(prop.getProperty("Lst_AnalystQueue_StatusListFollowUp"))).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_TakeActionGreenTakeActionButton"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
		Thread.sleep(2000);
	}

	public void AQ_TakeActionFollowUp_AssigneeChange(WebDriver driver, String username, String comment) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, 15);
		String usernameArray[] = username.split("(?!^)");

		driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_StatusDropdownButton"))).click();
		driver.findElement(By.linkText(prop.getProperty("Lst_AnalystQueue_StatusListFollowUp"))).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_TakeActionAssigneeXButton"))).click();
		Thread.sleep(1000);

		for(int i = 0; i < usernameArray.length; i++) {
			driver.findElement(By.xpath(prop.getProperty("Txt_AnalystQueue_TakeActionAssigneeField"))).sendKeys(usernameArray[i]);
			Thread.sleep(300);
		}
		Thread.sleep(2000);
		driver.findElement(By.xpath(prop.getProperty("Lbl_AnalystQueue_TakeActionAssigneeAutoDropdown"))).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("Txt_AnalystQueue_TakeActionCommentField"))).sendKeys(comment);
		driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_TakeActionGreenTakeActionButton"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
		Thread.sleep(2000);
	}

	public void AQ_TakeActionEscalate(WebDriver driver) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, 15);

		driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_StatusDropdownButton"))).click();
		driver.findElement(By.linkText(prop.getProperty("Lst_AnalystQueue_StatusListEscalate"))).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_TakeActionGreenTakeActionButton"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
		Thread.sleep(2000);
	}

	public void AQ_TakeActionBreach(WebDriver driver) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, 15);

		driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_StatusDropdownButton"))).click();
		driver.findElement(By.linkText(prop.getProperty("Lst_AnalystQueue_StatusListBreach"))).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_TakeActionGreenTakeActionButton"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
		Thread.sleep(2000);
	}

	public void AQ_TakeActionReviewed(WebDriver driver) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, 15);

		driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_StatusDropdownButton"))).click();
		driver.findElement(By.linkText(prop.getProperty("Lst_AnalystQueue_StatusListReviewed"))).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_TakeActionGreenTakeActionButton"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
		Thread.sleep(2000);
	}

	public void AQ_TakeActionSpam(WebDriver driver) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, 15);

		driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_StatusDropdownButton"))).click();
		driver.findElement(By.linkText(prop.getProperty("Lst_AnalystQueue_StatusListSpam"))).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_TakeActionGreenTakeActionButton"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
		Thread.sleep(2000);
	}

	public void AQ_TakeActionNews(WebDriver driver) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, 15);

		driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_StatusDropdownButton"))).click();
		driver.findElement(By.linkText(prop.getProperty("Lst_AnalystQueue_StatusListNews"))).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_TakeActionGreenTakeActionButton"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
		Thread.sleep(2000);
	}

	public void AQ_TakeActionCancel1(WebDriver driver) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, 15);

		driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_StatusDropdownButton"))).click();
		driver.findElement(By.linkText(prop.getProperty("Lst_AnalystQueue_StatusListFollowUp"))).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(prop.getProperty("Btn_AnalystQueue_TakeActionCancelXButton"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
		Thread.sleep(2000);
	}

	public void AQ_TakeActionCancel2(WebDriver driver) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, 15);

		driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_StatusDropdownButton"))).click();
		driver.findElement(By.linkText(prop.getProperty("Lst_AnalystQueue_StatusListFollowUp"))).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_TakeActionLowerCancelButton"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
		Thread.sleep(2000);
	}


	public void SearchMessage(WebDriver driver, String messageSubject_Search, String messageDate_Search, String searchStatusXpath) throws InterruptedException {

		driver.findElement(By.xpath(prop.getProperty("Btn_Search_ClearAllFiltersButton"))).click();
		driver.findElement(By.id(prop.getProperty("Txt_Search_KeywordsInSubject"))).sendKeys(messageSubject_Search);
		driver.findElement(By.xpath(prop.getProperty(searchStatusXpath))).click();
		driver.findElement(By.xpath(prop.getProperty("Btn_Search_SentDateDropdownButton"))).click();
		driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateFromField"))).clear();
		driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateFromField"))).sendKeys(messageDate_Search);
		driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateToField"))).clear();
		driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateToField"))).sendKeys(messageDate_Search);
		driver.findElement(By.xpath(prop.getProperty("Btn_Search_SentDateApplyButton"))).click();
		driver.findElement(By.xpath(prop.getProperty("Btn_Search_SearchButton"))).click();
		Thread.sleep(2000);
	}
	
	public String AQ_WaitForNewSourceId(WebDriver driver, String messageSourceId1, String messageSourceId2) throws IOException, InterruptedException {
		int whileCount = 0;
		
		while(messageSourceId2.equals(messageSourceId1)) {
	    	Thread.sleep(1000);
	    	whileCount ++;
	    	System.out.println("Waiting for new message for " + whileCount + " second(s).");
	    	messageSourceId2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSourceId"))).getText();
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

	public void APQ_TakeActionReviewed(WebDriver driver) throws InterruptedException {

		driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RedTakeActionButton0"))).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_StatusDropdownButton"))).click();
		driver.findElement(By.linkText(prop.getProperty("Lst_ActionPendingQueue_StatusListReviewed"))).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_GreenTakeActionButton"))).click();
		Thread.sleep(2000);
	}

	public void APQ_TakeActionSpam(WebDriver driver, int spamNumber) throws InterruptedException {

		if (spamNumber == 1) {
			driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RedTakeActionButton1"))).click();
		}
		else {
			driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RedTakeActionButton0"))).click();
		}
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_StatusDropdownButton"))).click();
		driver.findElement(By.linkText(prop.getProperty("Lst_ActionPendingQueue_StatusListSpam"))).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_GreenTakeActionButton"))).click();
		Thread.sleep(2000);
	}

	public void APQ_TakeActionNews(WebDriver driver, int newsNumber) throws InterruptedException {

		if (newsNumber == 2) {
			driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RedTakeActionButton2"))).click();
		}
		else if (newsNumber == 1) {
			driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RedTakeActionButton1"))).click();
		}
		else {
			driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RedTakeActionButton0"))).click();
		}
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_StatusDropdownButton"))).click();
		driver.findElement(By.linkText(prop.getProperty("Lst_ActionPendingQueue_StatusListNews"))).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_GreenTakeActionButton"))).click();
		Thread.sleep(2000);
	}

	public void APQ_TakeActionEscalate_AssigneeChange(WebDriver driver, int statusOrder, String username) throws InterruptedException {
		String usernameArray[] = username.split("(?!^)");

		if (statusOrder == 2) {
			driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RedTakeActionButton2"))).click();
		}
		else if (statusOrder == 1) {
			driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RedTakeActionButton1"))).click();
		}
		else {
			driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RedTakeActionButton0"))).click();
		}
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_StatusDropdownButton"))).click();
		driver.findElement(By.linkText(prop.getProperty("Lst_ActionPendingQueue_StatusListEscalate"))).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_TakeActionAssigneeXButton"))).click();
		Thread.sleep(1000);

		for(int i = 0; i < usernameArray.length; i++) {
			driver.findElement(By.xpath(prop.getProperty("Txt_ActionPendingQueue_TakeActionAssigneeField"))).sendKeys(usernameArray[i]);
			Thread.sleep(300);
		}
		Thread.sleep(2000);
		driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_TakeActionAssigneeAutoDropdown"))).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_GreenTakeActionButton"))).click();
		Thread.sleep(2000);
	}

	public void APQ_TakeActionReviewed_AssigneeChange(WebDriver driver, int statusOrder, String username, String comment) throws InterruptedException {
		String usernameArray[] = username.split("(?!^)");

		if (statusOrder == 1) {
			driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RedTakeActionButton1"))).click();
		}
		else {
			driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RedTakeActionButton0"))).click();
		}
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_StatusDropdownButton"))).click();
		driver.findElement(By.linkText(prop.getProperty("Lst_ActionPendingQueue_StatusListReviewed"))).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_TakeActionAssigneeXButton"))).click();
		Thread.sleep(1000);

		for(int i = 0; i < usernameArray.length; i++) {
			driver.findElement(By.xpath(prop.getProperty("Txt_ActionPendingQueue_TakeActionAssigneeField"))).sendKeys(usernameArray[i]);
			Thread.sleep(300);
		}
		Thread.sleep(2000);
		driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_TakeActionAssigneeAutoDropdown"))).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(prop.getProperty("Txt_ActionPendingQueue_TakeActionCommentField"))).sendKeys(comment);
		driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_GreenTakeActionButton"))).click();
		Thread.sleep(2000);
	}

}
