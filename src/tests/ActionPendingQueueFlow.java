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
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  public static String sharedUIMapPath;
  public static String baseUrl;


  @BeforeMethod
  public void setUp() throws Exception {
	prop = new Properties();
	prop.load(new FileInputStream("./Configuration/Monitoring_Config.properties"));
	//driver = new FirefoxDriver();
	System.setProperty("webdriver.chrome.driver", "chromedriver");
	driver = new ChromeDriver();
    sharedUIMapPath = prop.getProperty("SharedUIMap");
    prop.load(new FileInputStream(sharedUIMapPath));
    baseUrl = prop.getProperty("ClusterUrl");
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

  }

  @Test
  public void testActionPendingQueueFlow() throws Exception {
    // Log in - using Synthesys_Login method
    driver.get(baseUrl);
    driver.manage().window().maximize();
    Synthesys_Login(driver, prop.getProperty("Username"), prop.getProperty("Password"));

	//Wait for page to load
    WebDriverWait wait = new WebDriverWait(driver, 10);
    //wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Lbl_Synthesys_KnowledgeGraphToQuery"))));
    Thread.sleep(500);
    
    // Go to monitoring and wait for page to load selected KG - using AQ_SelectKGForTesting method
    driver.findElement(By.linkText(prop.getProperty("Lnk_Synthesys_MonitoringTab"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_Monitoring_KGDropdownButton"))));

    AQ_SelectKGForTesting(driver, prop.getProperty("SelectedKG"));



    //Analyst Queue - Setting message statuses

    
    // Select first message and store the message subject, date, and source ID
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_ClickFirstMessage"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);
    String followUpMessageSubject = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSubjectLabel"))).getText();
    System.out.println("Follow-up message 1 Subject: "+ followUpMessageSubject);
    
    String followUpMessageDate = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewDateLabel"))).getText();
    System.out.println("Follow-up message 1 Date: " + followUpMessageDate);
    
	String followUpMessageSourceId = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSourceId"))).getText();
	System.out.println("Follow-up message 1 Source ID: " + followUpMessageSourceId);
    
    //Set status to follow-up and click green Take Action button - using AQ_TakeActionFollowUp method
    AQ_TakeActionFollowUp(driver);

    //Check that message has been moved from the Analyst Queue
    String followUpMessageSourceId2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSourceId"))).getText();

    if(followUpMessageSourceId2.equals(followUpMessageSourceId)) {
        String newFollowUpMessageSourceId2 = AQ_WaitForNewSourceId(driver, followUpMessageSourceId, followUpMessageSourceId2);
        followUpMessageSourceId2 = newFollowUpMessageSourceId2;
    }

    //Mark next message as Follow-up - using AQ_TakeActionFollowUp method
    String followUpMessageSubject2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSubjectLabel"))).getText();
    System.out.println("Follow-up message 2 Subject: "+ followUpMessageSubject2);

    String followUpMessageDate2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewDateLabel"))).getText();
    System.out.println("Follow-up message 2 Date: " + followUpMessageDate2);

    System.out.println("Follow-up message 2 Source ID: " + followUpMessageSourceId2);

    AQ_TakeActionFollowUp(driver);
    
    //Check that message has been moved from the Analyst Queue
    String escalateMessageSourceId = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSourceId"))).getText();
    
    if(escalateMessageSourceId.equals(followUpMessageSourceId2)) {
    	String newEscalateMessageSourceId = AQ_WaitForNewSourceId(driver, followUpMessageSourceId2, escalateMessageSourceId);
    	escalateMessageSourceId = newEscalateMessageSourceId;
    }
    
    String escalateMessageSubject = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSubjectLabel"))).getText();
    System.out.println("Escalate message 1 Subject: " + escalateMessageSubject);
    
    String escalateMessageDate = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewDateLabel"))).getText();
    System.out.println("Escalate message 1 Date: " + escalateMessageDate);
    
	System.out.println("Escalate message 1 Source ID: " + escalateMessageSourceId);
    
    //Set status to escalate and click green Take Action button - using AQ_TakeActionEscalate method
    AQ_TakeActionEscalate(driver);

    //Check that message has been moved from the Analyst Queue
    String escalateMessageSourceId2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSourceId"))).getText();

    if(escalateMessageSourceId2.equals(escalateMessageSourceId)) {
      String newEscalateMessageSourceId2 = AQ_WaitForNewSourceId(driver, escalateMessageSourceId, escalateMessageSourceId2);
      escalateMessageSourceId2 = newEscalateMessageSourceId2;
    }

    String escalateMessageSubject2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSubjectLabel"))).getText();
    System.out.println("Escalate message 2 Subject: " + escalateMessageSubject2);

    String escalateMessageDate2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewDateLabel"))).getText();
    System.out.println("Escalate message 2 Date: " + escalateMessageDate2);

    System.out.println("Escalate message 2 Source ID: " + escalateMessageSourceId2);

    //Set status to escalate and click green Take Action button - using AQ_TakeActionEscalate method
    AQ_TakeActionEscalate(driver);
    
    //Check that message has been moved from the Analyst Queue
    String breachMessageSourceId = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSourceId"))).getText();
    
    if(breachMessageSourceId.equals(escalateMessageSourceId)) {
    	String newBreachMessageSourceId = AQ_WaitForNewSourceId(driver, escalateMessageSourceId2, breachMessageSourceId);
    	breachMessageSourceId = newBreachMessageSourceId;
    }
    
    String breachMessageSubject = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSubjectLabel"))).getText();
    System.out.println("Breach message 1 Subject: " + breachMessageSubject);
    
    String breachMessageDate = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewDateLabel"))).getText();
    System.out.println("Breach message 1 Date: " + breachMessageDate);
    
	System.out.println("Breach message 1 Source ID: " + breachMessageSourceId);
    
    //Set status to breach and click green Take Action button - using AQ_TakeActionBreach method
    AQ_TakeActionBreach(driver);

    //Check that message has been moved from the Analyst Queue
    String breachMessageSourceId2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSourceId"))).getText();

    if(breachMessageSourceId2.equals(breachMessageSourceId)) {
      String newBreachMessageSourceId2 = AQ_WaitForNewSourceId(driver, breachMessageSourceId, breachMessageSourceId2);
      breachMessageSourceId2 = newBreachMessageSourceId2;
    }

    String breachMessageSubject2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSubjectLabel"))).getText();
    System.out.println("Breach message 2 Subject: " + breachMessageSubject2);

    String breachMessageDate2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewDateLabel"))).getText();
    System.out.println("Breach message 2 Date: " + breachMessageDate2);

    System.out.println("Breach message 2 Source ID: " + breachMessageSourceId2);

    //Set status to breach and click green Take Action button - using AQ_TakeActionBreach method
    AQ_TakeActionBreach(driver);
    
    //Check that message has been moved from the Analyst Queue
    String testMessageSourceId = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSourceId"))).getText();
    
    if(testMessageSourceId.equals(breachMessageSourceId)) {
    	String newTestMessageSourceId = AQ_WaitForNewSourceId(driver, breachMessageSourceId, testMessageSourceId);
    	testMessageSourceId = newTestMessageSourceId;
    }




    //Go to the Action Pending Queue tab
    driver.findElement(By.linkText(prop.getProperty("Lnk_Monitoring_ActionPendingQueueTab"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RefreshButton"))));
    Thread.sleep(2000);
    
    //Find the first message that was labeled "breach" and verify the message details
    driver.findElement(By.xpath(".//*[@id='status-0']/div/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + breachMessageSubject + "\"]/td[1]")).click();
    Thread.sleep(5000);
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_MsgViewSubject"))).getText(), breachMessageSubject, "Message subjects do not match for breachMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewStatus"))).getText(), "breach", "Message status does not match for breachMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewResolved"))).getText(), "false", "Message status does not match for breachMessage");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_MsgViewDate"))).getText(), breachMessageDate, "Message Date does not match for breachMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for breachMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for breachMessage");

    // Mark the message as reviewed
    driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_ReviewedButton0"))).click();
    System.out.println("breachMessage marked as Reviewed");
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
    Thread.sleep(2000);

    //Find the second message that was labeled "breach" and verify the message details
    driver.findElement(By.xpath(".//*[@id='status-0']/div/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + breachMessageSubject2 + "\"]/td[1]")).click();
    Thread.sleep(5000);
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_MsgViewSubject"))).getText(), breachMessageSubject2, "Message subjects do not match for breachMessage2");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewStatus"))).getText(), "breach", "Message status does not match for breachMessage2");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewResolved"))).getText(), "false", "Message status does not match for breachMessage2");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_MsgViewDate"))).getText(), breachMessageDate2, "Message Date does not match for breachMessage2");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for breachMessage2");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for breachMessage2");

    //Mark the message as Reviewed through the Take Action flow - using APQ_TakeActionReviewed method
    APQ_TakeActionReviewed(driver);
    System.out.println("breachMessage2 marked as Reviewed");

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
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_Search_ClearAllFiltersButton"))));
    Thread.sleep(2000);
    
    //Add Quotes to breachMessageSubject so that it can be used to search
    String breachMessageSubject_Search = "\"" + breachMessageSubject + "\"";
    
    //Shorten message timestamp to date for search
    String breachMessageDate_Search = breachMessageDate.substring(0, 10);
    
    //Search for message by subject, status (Reviewed), and Date - using SearchMessage method
    String searchReviewedXpath = "Lst_Search_StatusListReviewed";
    SearchMessage(driver, breachMessageSubject_Search, breachMessageDate_Search, searchReviewedXpath);

    //Set list to sort by Sent - Newest First
    new Select(driver.findElement(By.xpath(prop.getProperty("Btn_Search_SortListDropdown")))).selectByVisibleText("Sent - Newest First");
    driver.findElement(By.xpath(prop.getProperty("Lst_Search_SortListNewestFirst"))).click();
    Thread.sleep(2000);

    //Click on the message to verify the details
    driver.findElement(By.xpath(".//*[@id='applicationHost']/div/div[1]/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + breachMessageSubject + "\"]/td[1]")).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);
    
    //Verify that message details (subject, status, date) match the message
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewSubject"))).getText(), breachMessageSubject, "Message subjects do not match for breachMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewStatus"))).getText(), "reviewed", "Message status does not match for breachMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewResolved"))).getText(), "true", "Message status does not match for breachMessage");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewDate"))).getText(), breachMessageDate, "Message Date does not match for breachMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for breachMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for breachMessage");

    //Add Quotes to breachMessageSubject so that it can be used to search
    String breachMessageSubject2_Search = "\"" + breachMessageSubject2 + "\"";

    //Shorten message timestamp to date for search
    String breachMessageDate2_Search = breachMessageDate2.substring(0, 10);

    //Click Show Filters
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);

    //Search for message by subject, status (Reviewed), and Date - using SearchMessage method
    SearchMessage(driver, breachMessageSubject2_Search, breachMessageDate2_Search, searchReviewedXpath);

    //Click on the message to verify the details
    driver.findElement(By.xpath(".//*[@id='applicationHost']/div/div[1]/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + breachMessageSubject2 + "\"]/td[1]")).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);

    //Verify that message details (subject, status, date) match the message
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewSubject"))).getText(), breachMessageSubject2, "Message subjects do not match for breachMessage2");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewStatus"))).getText(), "reviewed", "Message status does not match for breachMessage2");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewResolved"))).getText(), "true", "Message status does not match for breachMessage2");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewDate"))).getText(), breachMessageDate2, "Message Date does not match for breachMessage2");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for breachMessage2");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for breachMessage2");

    //Go to the Action Pending Queue tab
    driver.findElement(By.linkText(prop.getProperty("Lnk_Monitoring_ActionPendingQueueTab"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RefreshButton"))));
    Thread.sleep(2000);
    
    //Go to the escalate tab, click on the first message, verify the message details, and mark it as Spam (also check if anything is in the breach column - changes the xpath query if so)
    driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_StatusColumnEscalate"))).click();
    Thread.sleep(1000);
    
    String firstButtonText = driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_StatusColumnFirstButton"))).getText();
    
    if(firstButtonText.equals("breach")) {
      driver.findElement(By.xpath(".//*[@id='status-1']/div/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + escalateMessageSubject + "\"]/td[1]")).click();

      Thread.sleep(5000);
      Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_MsgViewSubject"))).getText(), escalateMessageSubject, "Message subjects do not match for escalateMessage");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewStatus"))).getText(), "escalate", "Message status does not match for escalateMessage");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewResolved"))).getText(), "false", "Message status does not match for escalateMessage");
      Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_MsgViewDate"))).getText(), escalateMessageDate, "Message Date does not match for escalateMessage");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for escalateMessage");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for escalateMessage");

      driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_SpamButton1"))).click();
    }
    else {
      driver.findElement(By.xpath(".//*[@id='status-0']/div/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + escalateMessageSubject + "\"]/td[1]")).click();

      Thread.sleep(5000);
      Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_MsgViewSubject"))).getText(), escalateMessageSubject, "Message subjects do not match for escalateMessage");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewStatus"))).getText(), "escalate", "Message status does not match for escalateMessage");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewResolved"))).getText(), "false", "Message status does not match for escalateMessage");
      Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_MsgViewDate"))).getText(), escalateMessageDate, "Message Date does not match for escalateMessage");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for escalateMessage");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for escalateMessage");

      driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_SpamButton0"))).click();
    }

    System.out.println("escalateMessage marked as Spam");
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
    Thread.sleep(2000);

    //Go to the escalate tab, click on the second message and mark it as Spam through the take action workflow (also check if anything is in the breach column - changes the xpath query if so) - uses APQ_TakeActionSpam method
    driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_StatusColumnEscalate"))).click();
    Thread.sleep(1000);

    firstButtonText = driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_StatusColumnFirstButton"))).getText();

    if(firstButtonText.equals("breach")) {
      driver.findElement(By.xpath(".//*[@id='status-1']/div/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + escalateMessageSubject2 + "\"]/td[1]")).click();

      Thread.sleep(5000);
      Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_MsgViewSubject"))).getText(), escalateMessageSubject2, "Message subjects do not match for escalateMessage2");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewStatus"))).getText(), "escalate", "Message status does not match for escalateMessage2");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewResolved"))).getText(), "false", "Message status does not match for escalateMessage2");
      Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_MsgViewDate"))).getText(), escalateMessageDate2, "Message Date does not match for escalateMessage2");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for escalateMessage2");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for escalateMessage2");

      APQ_TakeActionSpam(driver,1);
    }
    else {
      driver.findElement(By.xpath(".//*[@id='status-0']/div/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + escalateMessageSubject2 + "\"]/td[1]")).click();

      Thread.sleep(5000);
      Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_MsgViewSubject"))).getText(), escalateMessageSubject2, "Message subjects do not match for escalateMessage2");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewStatus"))).getText(), "escalate", "Message status does not match for escalateMessage2");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewResolved"))).getText(), "false", "Message status does not match for escalateMessage2");
      Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_MsgViewDate"))).getText(), escalateMessageDate2, "Message Date does not match for escalateMessage2");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for escalateMessage2");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for escalateMessage2");

      APQ_TakeActionSpam(driver,0);
    }

    System.out.println("escalateMessage2 marked as Spam");
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

    //Add Quotes to escalateMessageSubject so that it can be used to search
    String escalateMessageSubject_Search = "\"" + escalateMessageSubject + "\"";
    
  //Shorten message timestamp to date for search
    String escalateMessageDate_Search = escalateMessageDate.substring(0, 10);
    
    //Search for message by subject, status (Spam), and Date
    String searchSpamXpath = "Lst_Search_StatusListSpam";
    SearchMessage(driver, escalateMessageSubject_Search, escalateMessageDate_Search, searchSpamXpath);
    
    //Set list to sort by Sent - Newest First
    new Select(driver.findElement(By.xpath(prop.getProperty("Btn_Search_SortListDropdown")))).selectByVisibleText("Sent - Newest First");
    driver.findElement(By.xpath(prop.getProperty("Lst_Search_SortListNewestFirst"))).click();
    Thread.sleep(2000);

    //Click on the message to verify the details
    driver.findElement(By.xpath(".//*[@id='applicationHost']/div/div[1]/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + escalateMessageSubject + "\"]/td[1]")).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);
    
    //Verify that message details (subject, status, date) match the message
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewSubject"))).getText(), escalateMessageSubject, "Message subjects do not match for escalateMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewStatus"))).getText(), "spam", "Message status does not match for escalateMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewResolved"))).getText(), "true", "Message status does not match for escalateMessage");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewDate"))).getText(), escalateMessageDate, "Message Date does not match for escalateMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for escalateMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for escalateMessage");

    //Add Quotes to escalateMessageSubject2 so that it can be used to search
    String escalateMessageSubject2_Search = "\"" + escalateMessageSubject2 + "\"";

    //Shorten message timestamp to date for search
    String escalateMessageDate2_Search = escalateMessageDate2.substring(0, 10);

    //Click Show Filters
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);

    //Search for message by subject, status (Spam), and Date
    SearchMessage(driver, escalateMessageSubject2_Search, escalateMessageDate2_Search, searchSpamXpath);

    //Set list to sort by Sent - Newest First
    new Select(driver.findElement(By.xpath(prop.getProperty("Btn_Search_SortListDropdown")))).selectByVisibleText("Sent - Newest First");
    driver.findElement(By.xpath(prop.getProperty("Lst_Search_SortListNewestFirst"))).click();
    Thread.sleep(2000);

    //Click on the message to verify the details
    driver.findElement(By.xpath(".//*[@id='applicationHost']/div/div[1]/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + escalateMessageSubject2 + "\"]/td[1]")).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);

    //Verify that message details (subject, status, date) match the message
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewSubject"))).getText(), escalateMessageSubject2, "Message subjects do not match for escalateMessage2");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewStatus"))).getText(), "spam", "Message status does not match for escalateMessage2");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewResolved"))).getText(), "true", "Message status does not match for escalateMessage2");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewDate"))).getText(), escalateMessageDate2, "Message Date does not match for escalateMessage2");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for escalateMessage2");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for escalateMessage2");
    
    //Go to the Action Pending Queue tab
    driver.findElement(By.linkText(prop.getProperty("Lnk_Monitoring_ActionPendingQueueTab"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RefreshButton"))));
    Thread.sleep(2000);
    
  //Go to the follow-up tab, click on the first message, verify the message details, and mark it as News (also check if anything is in the breach or escalate column - changes the xpath query if so)
    driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_StatusColumnFollowUp"))).click();
    Thread.sleep(1000);
    
    firstButtonText = driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_StatusColumnFirstButton"))).getText();

    if (driver.findElements(By.xpath(prop.getProperty("Btn_ActionPendingQueue_StatusColumnSecondButton"))).size() != 0) {
      String secondButtonText = driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_StatusColumnSecondButton"))).getText();

      if(firstButtonText.equals("breach") && secondButtonText.equals("escalate")) {
        driver.findElement(By.xpath(".//*[@id='status-2']/div/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + followUpMessageSubject + "\"]/td[1]")).click();

        Thread.sleep(5000);
        Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_MsgViewSubject"))).getText(), followUpMessageSubject, "Message subjects do not match for followUpMessage");
        Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewStatus"))).getText(), "follow-up", "Message status does not match for followUpMessage");
        Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewResolved"))).getText(), "false", "Message status does not match for followUpMessage");
        Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_MsgViewDate"))).getText(), followUpMessageDate, "Message Date does not match for followUpMessage");
        Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for followUpMessage");
        Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for followUpMessage");

        driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_NewsButton2"))).click();
      }
    }
    else if (firstButtonText.equals("breach") || firstButtonText.equals("escalate")){
      driver.findElement(By.xpath(".//*[@id='status-1']/div/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + followUpMessageSubject + "\"]/td[1]")).click();

      Thread.sleep(5000);
      Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_MsgViewSubject"))).getText(), followUpMessageSubject, "Message subjects do not match for followUpMessage");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewStatus"))).getText(), "follow-up", "Message status does not match for followUpMessage");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewResolved"))).getText(), "false", "Message status does not match for followUpMessage");
      Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_MsgViewDate"))).getText(), followUpMessageDate, "Message Date does not match for followUpMessage");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for followUpMessage");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for followUpMessage");

      driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_NewsButton1"))).click();
    }
    else {
      driver.findElement(By.xpath(".//*[@id='status-0']/div/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + followUpMessageSubject + "\"]/td[1]")).click();

      Thread.sleep(5000);
      Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_MsgViewSubject"))).getText(), followUpMessageSubject, "Message subjects do not match for followUpMessage");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewStatus"))).getText(), "follow-up", "Message status does not match for followUpMessage");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewResolved"))).getText(), "false", "Message status does not match for followUpMessage");
      Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_MsgViewDate"))).getText(), followUpMessageDate, "Message Date does not match for followUpMessage");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for followUpMessage");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for followUpMessage");

      driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_NewsButton0"))).click();
    }

    System.out.println("followUpMessage marked as News");
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
    Thread.sleep(2000);

    //Go to the follow-up tab, click on the second message, verify the message details, and mark it as News through Take Action (also check if anything is in the breach or escalate column - changes the xpath query if so) - using APQ_TakeActionNews method
    driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_StatusColumnFollowUp"))).click();
    Thread.sleep(1000);

    firstButtonText = driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_StatusColumnFirstButton"))).getText();

    if (driver.findElements(By.xpath(prop.getProperty("Btn_ActionPendingQueue_StatusColumnSecondButton"))).size() != 0) {
      String secondButtonText = driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_StatusColumnSecondButton"))).getText();

      if(firstButtonText.equals("breach") && secondButtonText.equals("escalate")) {
        driver.findElement(By.xpath(".//*[@id='status-2']/div/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + followUpMessageSubject2 + "\"]/td[1]")).click();

        Thread.sleep(5000);
        Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_MsgViewSubject"))).getText(), followUpMessageSubject2, "Message subjects do not match for followUpMessage2");
        Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewStatus"))).getText(), "follow-up", "Message status does not match for followUpMessage2");
        Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewResolved"))).getText(), "false", "Message status does not match for followUpMessage2");
        Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_MsgViewDate"))).getText(), followUpMessageDate2, "Message Date does not match for followUpMessage2");
        Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for followUpMessage2");
        Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for followUpMessage2");

        APQ_TakeActionNews(driver,2);
      }
    }

    else if (firstButtonText.equals("breach") || firstButtonText.equals("escalate")){
      driver.findElement(By.xpath(".//*[@id='status-1']/div/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + followUpMessageSubject2 + "\"]/td[1]")).click();

      Thread.sleep(5000);
      Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_MsgViewSubject"))).getText(), followUpMessageSubject2, "Message subjects do not match for followUpMessage2");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewStatus"))).getText(), "follow-up", "Message status does not match for followUpMessage2");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewResolved"))).getText(), "false", "Message status does not match for followUpMessage2");
      Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_MsgViewDate"))).getText(), followUpMessageDate2, "Message Date does not match for followUpMessage2");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for followUpMessage2");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for followUpMessage2");

      APQ_TakeActionNews(driver,1);
    }
    else {
      driver.findElement(By.xpath(".//*[@id='status-0']/div/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + followUpMessageSubject2 + "\"]/td[1]")).click();

      Thread.sleep(5000);
      Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_MsgViewSubject"))).getText(), followUpMessageSubject2, "Message subjects do not match for followUpMessage2");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewStatus"))).getText(), "follow-up", "Message status does not match for followUpMessage2");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewResolved"))).getText(), "false", "Message status does not match for followUpMessage2");
      Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_MsgViewDate"))).getText(), followUpMessageDate2, "Message Date does not match for followUpMessage2");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for followUpMessage2");
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for followUpMessage2");

      APQ_TakeActionNews(driver,0);
    }

    System.out.println("followUpMessage2 marked as News");
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RefreshButton"))));
    Thread.sleep(2000);


    //Go to the Search tab
    driver.findElement(By.linkText(prop.getProperty("Lnk_Monitoring_SearchTab"))).click();
    Thread.sleep(2000);
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);

    //Add Quotes to followUpMessageSubject so that it can be used to search
    String followUpMessageSubject_Search = "\"" + followUpMessageSubject + "\"";
    
    //Shorten message timestamp to date for search
    String followUpMessageDate_Search = followUpMessageDate.substring(0, 10);
    
    //Search for message by subject, status (News), and Date
    String searchNewsXpath = "Lst_Search_StatusListNews";
    SearchMessage(driver, followUpMessageSubject_Search, followUpMessageDate_Search, searchNewsXpath);
    
    //Set list to sort by Sent - Newest First
    new Select(driver.findElement(By.xpath(prop.getProperty("Btn_Search_SortListDropdown")))).selectByVisibleText("Sent - Newest First");
    driver.findElement(By.xpath(prop.getProperty("Lst_Search_SortListNewestFirst"))).click();
    Thread.sleep(2000);

    //Click on the message to verify the details
    driver.findElement(By.xpath(".//*[@id='applicationHost']/div/div[1]/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + followUpMessageSubject + "\"]/td[1]")).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);
    
    //Verify that message details (subject, status, date) match the message
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewSubject"))).getText(), followUpMessageSubject, "Message subjects do not match for followUpMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewStatus"))).getText(), "news", "Message status does not match for followUpMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewResolved"))).getText(), "true", "Message status does not match for followUpMessage");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewDate"))).getText(), followUpMessageDate, "Message Date does not match for followUpMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for followUpMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for followUpMessage");

    //Add Quotes to followUpMessageSubject so that it can be used to search
    String followUpMessageSubject2_Search = "\"" + followUpMessageSubject2 + "\"";

    //Shorten message timestamp to date for search
    String followUpMessageDate2_Search = followUpMessageDate2 .substring(0, 10);

    //Click Show Filters
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);

    //Search for message by subject, status (News), and Date
    SearchMessage(driver, followUpMessageSubject2_Search, followUpMessageDate2_Search, searchNewsXpath);

    //Set list to sort by Sent - Newest First
    new Select(driver.findElement(By.xpath(prop.getProperty("Btn_Search_SortListDropdown")))).selectByVisibleText("Sent - Newest First");
    driver.findElement(By.xpath(prop.getProperty("Lst_Search_SortListNewestFirst"))).click();
    Thread.sleep(2000);

    //Click on the message to verify the details
    driver.findElement(By.xpath(".//*[@id='applicationHost']/div/div[1]/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + followUpMessageSubject2 + "\"]/td[1]")).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);

    //Verify that message details (subject, status, date) match the message
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewSubject"))).getText(), followUpMessageSubject2, "Message subjects do not match for followUpMessage2");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewStatus"))).getText(), "news", "Message status does not match for followUpMessage2");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewResolved"))).getText(), "true", "Message status does not match for followUpMessage2");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewDate"))).getText(), followUpMessageDate2, "Message Date does not match for followUpMessage2");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for followUpMessage2");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for followUpMessage2");
    
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

