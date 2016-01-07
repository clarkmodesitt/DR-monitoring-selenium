package tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.Assert;
import org.testng.AssertJUnit;
import functions.MonitoringPage_Functions;

public class ActionPendingQueueFlow extends MonitoringPage_Functions {
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();


  @BeforeMethod
  public void setUp() throws Exception {
	prop = new Properties();
	prop.load(new FileInputStream("./SharedUIMap/SharedUIMap.properties"));
	//driver = new FirefoxDriver();
	System.setProperty("webdriver.chrome.driver", "chromedriver");
	driver = new ChromeDriver();
    baseUrl = "http://naomi-nn.qa.digitalreasoning.com:8555/apps/login";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

  }

  @Test
  public void testActionPendingQueueFlow() throws Exception {
    // Log in
    driver.get(baseUrl);
    driver.manage().window().maximize();
    driver.findElement(By.id(prop.getProperty("Txt_Login_Username"))).clear();
    driver.findElement(By.id(prop.getProperty("Txt_Login_Username"))).sendKeys("admin");
    driver.findElement(By.id(prop.getProperty("Txt_Login_Password"))).clear();
    driver.findElement(By.id(prop.getProperty("Txt_Login_Password"))).sendKeys("1234");
    driver.findElement(By.xpath(prop.getProperty("Btn_Login_SignIn"))).click();
    

	//Wait for page to load
    WebDriverWait wait = new WebDriverWait(driver, 15);
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Lbl_Synthesys_KnowledgeGraphToQuery"))));
    Thread.sleep(500);
    
    // Go to monitoring and wait for page to load select KG (enron_rc2
    driver.findElement(By.linkText(prop.getProperty("Lnk_Synthesys_MonitoringTab"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_Monitoring_KGDropdownButton"))));
    
    if(driver.findElement(By.xpath(prop.getProperty("Btn_Monitoring_KGDropdownButton"))).equals("Select a knowledge graph...")) {
    	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(prop.getProperty("Lbl_AnalystQueue_OopsMessage"))));
    }	
    else {
    	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(prop.getProperty("Lbl_AnalystQueue_MessageCount"))));
    }
    
    //Select KG (enron_rc2)
    String storedKG = driver.findElement(By.xpath(prop.getProperty("Btn_Monitoring_KGDropdownButton"))).getText();
    System.out.println(storedKG);
    System.out.println(storedKG.equals("enron_rc2"));
    if(storedKG.equals("enron_rc2") == false) {
	    driver.findElement(By.xpath(prop.getProperty("Btn_Monitoring_KGDropdownButton"))).click();
	    driver.findElement(By.xpath(prop.getProperty("Lst_Monitoring_EnronRc2"))).click();
	    Thread.sleep(5000);
    }
    
    // Select first message and store the message subject, date, and source ID
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_ClickFirstMessage"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);
    String followUpMessageSubject = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewSubjectLabel"))).getText();
    System.out.println("Message 1 Subject: "+ followUpMessageSubject);
    
    String followUpMessageDate = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewDateLabel"))).getText();
    System.out.println("Message 1 Date: " + followUpMessageDate);
    
	String followUpMessageSourceId = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewSourceId"))).getText();
	System.out.println("Message 1 Source ID: " + followUpMessageSourceId);
    
    //Set status to follow-up and click green Take Action button
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))).click();
    Thread.sleep(1000);
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_StatusDropdownButton"))).click();
    driver.findElement(By.xpath(prop.getProperty("Lst_AnalystQueue_StatusListFollowUp"))).click();
    Thread.sleep(1000);;
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_TakeActionGreenTakeActionButton"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);
    
    //Check that message has been moved from the Analyst Queue
    String escalateMessageSourceId = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewSourceId"))).getText();
    
    if(escalateMessageSourceId.equals(followUpMessageSourceId)) {
    	String newEscalateMessageSourceId = AQ_WaitForNewSourceId(driver, followUpMessageSourceId, escalateMessageSourceId);
    	escalateMessageSourceId = newEscalateMessageSourceId;
    }
    
    String escalateMessageSubject = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewSubjectLabel"))).getText();
    System.out.println("Message 2 Subject: " + escalateMessageSubject);
    
    String escalateMessageDate = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewDateLabel"))).getText();
    System.out.println("Message 2 Date: " + escalateMessageDate);
    
	System.out.println("Message 2 Source ID: " + escalateMessageSourceId);
    
    //Set status to escalate and click green Take Action button
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))).click();
    Thread.sleep(1000);
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_StatusDropdownButton"))).click();
    driver.findElement(By.xpath(prop.getProperty("Lst_AnalystQueue_StatusListEscalate"))).click();
    Thread.sleep(1000);;
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_TakeActionGreenTakeActionButton"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);
    
    //Check that message has been moved from the Analyst Queue
    String breachMessageSourceId = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewSourceId"))).getText();
    
    if(breachMessageSourceId.equals(escalateMessageSourceId)) {
    	String newBreachMessageSourceId = AQ_WaitForNewSourceId(driver, escalateMessageSourceId, breachMessageSourceId);
    	breachMessageSourceId = newBreachMessageSourceId;
    }
    
    String breachMessageSubject = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewSubjectLabel"))).getText();
    System.out.println("Message 3 Subject: " + breachMessageSubject);
    
    String breachMessageDate = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewDateLabel"))).getText();
    System.out.println("Message 3 Date: " + breachMessageDate);
    
	System.out.println("Message 3 Source ID: " + breachMessageSourceId);
    
    //Set status to breach and click green Take Action button
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))).click();
    Thread.sleep(1000);
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_StatusDropdownButton"))).click();
    driver.findElement(By.xpath(prop.getProperty("Lst_AnalystQueue_StatusListBreach"))).click();
    Thread.sleep(1000);
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_TakeActionGreenTakeActionButton"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);
    
    //Check that message has been moved from the Analyst Queue
    String testMessageSourceId = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewSourceId"))).getText();
    
    if(testMessageSourceId.equals(breachMessageSourceId)) {
    	String newTestMessageSourceId = AQ_WaitForNewSourceId(driver, breachMessageSourceId, testMessageSourceId);
    	testMessageSourceId = newTestMessageSourceId;
    }
    
    //Go to the Action Pending Queue tab
    driver.findElement(By.linkText(prop.getProperty("Lnk_Monitoring_ActionPendingQueueTab"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RefreshButton"))));
    Thread.sleep(2000);
    
    //Find the message that was labeled "breach"and mark it as Reviewed
    driver.findElement(By.xpath(".//*[@id='status-0']/div/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + breachMessageSubject + "\"]/td[1]")).click();
    Thread.sleep(5000);
    driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_ReviewedButton"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RefreshButton"))));
    Thread.sleep(2000);
    
    //Check the count, click Refresh and verify that the count has gone down
    String strOriginalCount = driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_TotalCount"))).getText();
    int originalCount = Integer.parseInt(strOriginalCount);
    driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RefreshButton"))).click();
	    
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RefreshButton"))));
    String strUpdatedCount = driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_TotalCount"))).getText();
    int updatedCount = Integer.parseInt(strUpdatedCount);
    System.out.println("Original Count: " + originalCount);
    
    if(updatedCount != originalCount - 1) {
    	int newUpdatedCount = APQ_WaitForRefresh(driver, originalCount, updatedCount);
    	updatedCount = newUpdatedCount;
    }
    
    System.out.println("Updated Count: " + updatedCount);
  
    //Go to the Search tab
    driver.findElement(By.linkText(prop.getProperty("Lnk_Monitoring_SearchTab"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_Search_ClearAllFiltersButton"))));
    Thread.sleep(2000);
    
    //Add Quotes to breachMessageSubject so that it can be used to search
    String breachMessageSubject_Search = "\"" + breachMessageSubject + "\"";
    
    //Shorten message timestamp to date for search
    String breachMessageDate_Search = breachMessageDate.substring(0, 10);
    
    //Search for message by subject, status (Reviewed), and Date
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ClearAllFiltersButton"))).click();
    driver.findElement(By.id(prop.getProperty("Txt_Search_KeywordsInSubject"))).sendKeys(breachMessageSubject_Search);
    driver.findElement(By.xpath(prop.getProperty("Lst_Search_StatusListReviewed"))).click();
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SentDateDropdownButton"))).click();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateFromField"))).clear();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateFromField"))).sendKeys(breachMessageDate_Search);
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateToField"))).clear();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateToField"))).sendKeys(breachMessageDate_Search);
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SentDateApplyButton"))).click();
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SearchButton"))).click();
    Thread.sleep(2000);
    
    //Set list to sort by Sent - Newest First
    new Select(driver.findElement(By.xpath(prop.getProperty("Btn_Search_SortListDropdown")))).selectByVisibleText("Sent - Newest First");
    driver.findElement(By.xpath(prop.getProperty("Lst_Search_SortListNewestFirst"))).click();
    Thread.sleep(2000);
    
    //Verify that message details (subject, status, date) match the message
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_SearchResultMessageSubject"))).getText(), breachMessageSubject, "Message subjects do not match");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SearchResultMessageStatus"))).getText(), "Reviewed", "Message status does not match");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SearchResultMessageDate"))).getText(), breachMessageDate, "Message Date does not match");

    //Go to the Action Pending Queue tab
    driver.findElement(By.linkText(prop.getProperty("Lnk_Monitoring_ActionPendingQueueTab"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RefreshButton"))));
    Thread.sleep(2000);
    
    //Go to the escalate tab, click on the message and mark it as Spam (also check if anything is in the breach column - changes the xpath query if so
    driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_StatusColumnEscalate"))).click();
    Thread.sleep(1000);
    
    String firstButtonText = driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_StatusColumnFirstButton"))).getText();
    
    if(firstButtonText.equals("breach")) {
    	driver.findElement(By.xpath(".//*[@id='status-1']/div/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + escalateMessageSubject + "\"]/td[1]")).click();
    	
    	Thread.sleep(5000);
        driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_SpamButton1"))).click();
    }
    else {
    	driver.findElement(By.xpath(".//*[@id='status-0']/div/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + escalateMessageSubject + "\"]/td[1]")).click();
    	
    	Thread.sleep(5000);
        driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_SpamButton0"))).click();
    }
    
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RefreshButton"))));
    Thread.sleep(2000);
    
    //Check the count, click Refresh and verify that the count has gone down
    strOriginalCount = driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_TotalCount"))).getText();
    originalCount = Integer.parseInt(strOriginalCount);
    driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RefreshButton"))).click();
	    
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RefreshButton"))));
    strUpdatedCount = driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_TotalCount"))).getText();
    updatedCount = Integer.parseInt(strUpdatedCount);
    System.out.println("Original Count: " + originalCount);
    
    if(updatedCount != originalCount - 1) {
    	int newUpdatedCount = APQ_WaitForRefresh(driver, originalCount, updatedCount);
    	updatedCount = newUpdatedCount;
    }
    
    System.out.println("Updated Count: " + updatedCount);
  
    //Go to the Search tab
    driver.findElement(By.linkText(prop.getProperty("Lnk_Monitoring_SearchTab"))).click();
    Thread.sleep(2000);
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);

    //Add Quotes to breachMessageSubject so that it can be used to search
    String escalateMessageSubject_Search = "\"" + escalateMessageSubject + "\"";
    
  //Shorten message timestamp to date for search
    String escalateMessageDate_Search = escalateMessageDate.substring(0, 10);
    
    //Search for message by subject, status (News), and Date
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ClearAllFiltersButton"))).click();
    driver.findElement(By.id(prop.getProperty("Txt_Search_KeywordsInSubject"))).sendKeys(escalateMessageSubject_Search);
    driver.findElement(By.xpath(prop.getProperty("Lst_Search_StatusListSpam"))).click();
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SentDateDropdownButton"))).click();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateFromField"))).clear();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateFromField"))).sendKeys(escalateMessageDate_Search);
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateToField"))).clear();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateToField"))).sendKeys(escalateMessageDate_Search);
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SentDateApplyButton"))).click();
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SearchButton"))).click();
    Thread.sleep(2000);
    
    //Set list to sort by Sent - Newest First
    new Select(driver.findElement(By.xpath(prop.getProperty("Btn_Search_SortListDropdown")))).selectByVisibleText("Sent - Newest First");
    driver.findElement(By.xpath(prop.getProperty("Lst_Search_SortListNewestFirst"))).click();
    Thread.sleep(2000);
    
    //Verify that message details (subject, status, date) match the message
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_SearchResultMessageSubject"))).getText(), escalateMessageSubject, "Message subjects do not match");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SearchResultMessageStatus"))).getText(), "Spam", "Message status does not match");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SearchResultMessageDate"))).getText(), escalateMessageDate, "Message Date does not match");
    
    //Go to the Action Pending Queue tab
    driver.findElement(By.linkText(prop.getProperty("Lnk_Monitoring_ActionPendingQueueTab"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RefreshButton"))));
    Thread.sleep(2000);
    
  //Go to the follow-up tab, click on the message and mark it as News (also check if anything is in the breach or escalate column - changes the xpath query if so
    driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_StatusColumnFollowUp"))).click();
    Thread.sleep(1000);
    
    firstButtonText = driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_StatusColumnFirstButton"))).getText();
    String secondButtonText = driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_StatusColumnSecondButton"))).getText();
    
    if(firstButtonText.equals("breach") && secondButtonText.equals("escalate")) {
    	driver.findElement(By.xpath(".//*[@id='status-2']/div/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + followUpMessageSubject + "\"]/td[1]")).click();
    	
    	Thread.sleep(5000);
        driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_NewsButton2"))).click();
    }
    else if (firstButtonText.equals("breach") || firstButtonText.equals("escalate")){
    	driver.findElement(By.xpath(".//*[@id='status-1']/div/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + followUpMessageSubject + "\"]/td[1]")).click();
    	
    	Thread.sleep(5000);
        driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_NewsButton1"))).click();
    }
    else {
    	driver.findElement(By.xpath(".//*[@id='status-0']/div/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + followUpMessageSubject + "\"]/td[1]")).click();
    	
    	Thread.sleep(5000);
        driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_NewsButton0"))).click();
    }
    
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RefreshButton"))));
    Thread.sleep(2000);
    
    //Check the count, click Refresh and verify that the count has gone down
    strOriginalCount = driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_TotalCount"))).getText();
    originalCount = Integer.parseInt(strOriginalCount);
    driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RefreshButton"))).click();
	    
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RefreshButton"))));
    strUpdatedCount = driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_TotalCount"))).getText();
    updatedCount = Integer.parseInt(strUpdatedCount);
    System.out.println("Original Count: " + originalCount);
    
    if(updatedCount != originalCount - 1) {
    	int newUpdatedCount = APQ_WaitForRefresh(driver, originalCount, updatedCount);
    	updatedCount = newUpdatedCount;
    }
    
    System.out.println("Updated Count: " + updatedCount);
    
    //Go to the Search tab
    driver.findElement(By.linkText(prop.getProperty("Lnk_Monitoring_SearchTab"))).click();
    Thread.sleep(2000);
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);

    //Add Quotes to breachMessageSubject so that it can be used to search
    String followUpMessageSubject_Search = "\"" + followUpMessageSubject + "\"";
    
    //Shorten message timestamp to date for search
    String followUpMessageDate_Search = followUpMessageDate.substring(0, 10);
    
    //Search for message by subject, status (Spam), and Date
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ClearAllFiltersButton"))).click();
    driver.findElement(By.id(prop.getProperty("Txt_Search_KeywordsInSubject"))).sendKeys(followUpMessageSubject_Search);
    driver.findElement(By.xpath(prop.getProperty("Lst_Search_StatusListNews"))).click();
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SentDateDropdownButton"))).click();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateFromField"))).clear();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateFromField"))).sendKeys(followUpMessageDate_Search);
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateToField"))).clear();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateToField"))).sendKeys(followUpMessageDate_Search);
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SentDateApplyButton"))).click();
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SearchButton"))).click();
    Thread.sleep(2000);
    
    //Set list to sort by Sent - Newest First
    new Select(driver.findElement(By.xpath(prop.getProperty("Btn_Search_SortListDropdown")))).selectByVisibleText("Sent - Newest First");
    driver.findElement(By.xpath(prop.getProperty("Lst_Search_SortListNewestFirst"))).click();
    Thread.sleep(2000);
    
    //Verify that message details (subject, status, date) match the message
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_SearchResultMessageSubject"))).getText(), followUpMessageSubject, "Message subjects do not match");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SearchResultMessageStatus"))).getText(), "News", "Message status does not match");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SearchResultMessageDate"))).getText(), followUpMessageDate, "Message Date does not match");
    
    //End of test
    System.out.println("Test PASSED!");
  }

@AfterMethod
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      AssertJUnit.fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}

