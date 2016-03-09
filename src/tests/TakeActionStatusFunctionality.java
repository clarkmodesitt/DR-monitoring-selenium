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

public class TakeActionStatusFunctionality extends MonitoringPage_Functions {
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
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

  }

  @Test
  public void testTakeActionStatusFunctionality() throws Exception {
    // Log in - using Synthesys_Login method
    driver.get(baseUrl);
    driver.manage().window().maximize();
    Synthesys_Login(driver, prop.getProperty("Username"), prop.getProperty("Password"));

    //Wait for page to load
    WebDriverWait wait = new WebDriverWait(driver, 15);
    //wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Lbl_Synthesys_KnowledgeGraphToQuery"))));
    Thread.sleep(500);

    // Go to monitoring and wait for page to load selected KG - using AQ_SelectKGForTesting method
    driver.findElement(By.linkText(prop.getProperty("Lnk_Synthesys_MonitoringTab"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_Monitoring_KGDropdownButton"))));

    AQ_SelectKGForTesting(driver, prop.getProperty("SelectedKG"));
    
    // Select first message and store the message subject, date, and source ID
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_ClickFirstMessage"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);
    String followUpMessageSubject = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSubjectLabel"))).getText();
    System.out.println("Follow-up message Subject: "+ followUpMessageSubject);
    
    String followUpMessageDate = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewDateLabel"))).getText();
    System.out.println("Follow-up message Date: " + followUpMessageDate);
    
	String followUpMessageSourceId = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSourceId"))).getText();
	System.out.println("Follow-up message Source ID: " + followUpMessageSourceId);
    
    //Set status to follow-up and test cancel functionality (with X icon) - using AQ_TakeActionCancel1 method
    AQ_TakeActionCancel1(driver);

    //Verify that message is not changed/moved from Analyst queue
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSubjectLabel"))).getText(), followUpMessageSubject, "Did not cancel properly.");
    
    // Set status to follow-up and test cancel functionality (with Cancel button) - using AQ_TakeActionCancel2 method
    AQ_TakeActionCancel2(driver);
    
    //Verify that message is not changed/moved from Analyst queue
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSubjectLabel"))).getText(), followUpMessageSubject, "Did not cancel properly.");
    
    //Set status to follow-up and click green Take Action button - using AQ_TakeActionFollowUp method
    AQ_TakeActionFollowUp(driver);
    
    //Check that message has been moved from the Analyst Queue
    String escalateMessageSourceId = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSourceId"))).getText();
    
    if(escalateMessageSourceId.equals(followUpMessageSourceId)) {
    	String newEscalateMessageSourceId = AQ_WaitForNewSourceId(driver, followUpMessageSourceId, escalateMessageSourceId);
    	escalateMessageSourceId = newEscalateMessageSourceId;
    }
    
    String escalateMessageSubject = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSubjectLabel"))).getText();
    System.out.println("Escalate message Subject: " + escalateMessageSubject);
    
    String escalateMessageDate = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewDateLabel"))).getText();
    System.out.println("Escalate message Date: " + escalateMessageDate);
    
	System.out.println("Escalate message Source ID: " + escalateMessageSourceId);
    
    //Set status to escalate and click green Take Action button - using AQ_TakeActionEscalate method
    AQ_TakeActionEscalate(driver);
    
    //Check that message has been moved from the Analyst Queue
    String breachMessageSourceId = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSourceId"))).getText();
    
    if(breachMessageSourceId.equals(escalateMessageSourceId)) {
    	String newBreachMessageSourceId = AQ_WaitForNewSourceId(driver, escalateMessageSourceId, breachMessageSourceId);
    	breachMessageSourceId = newBreachMessageSourceId;
    }
    
    String breachMessageSubject = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSubjectLabel"))).getText();
    System.out.println("Breach message Subject: " + breachMessageSubject);
    
    String breachMessageDate = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewDateLabel"))).getText();
    System.out.println("Breach message Date: " + breachMessageDate);
    
	System.out.println("Breach message Source ID: " + breachMessageSourceId);
    
    //Set status to breach and click green Take Action button - using AQ_TakeActionBreach method
    AQ_TakeActionBreach(driver);
    
    //Check that message has been moved from the Analyst Queue
    String reviewedMessageSourceId = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSourceId"))).getText();
    
    if(reviewedMessageSourceId.equals(breachMessageSourceId)) {
    	String newReviewedMessageSourceId = AQ_WaitForNewSourceId(driver, breachMessageSourceId, reviewedMessageSourceId);
    	reviewedMessageSourceId = newReviewedMessageSourceId;
    }
    
    String reviewedMessageSubject = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSubjectLabel"))).getText();
    System.out.println("Reviewed message 1 Subject: " + reviewedMessageSubject);
    
    String reviewedMessageDate = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewDateLabel"))).getText();
    System.out.println("Reviewed message 1 Date: " + reviewedMessageDate);
    
	System.out.println("Reviewed message 1 Source ID: " + reviewedMessageSourceId);
    
    //Set status to reviewed and click green Take Action button - using AQ_TakeActionReviewed method
    AQ_TakeActionReviewed(driver);
    
    //Check that message has been moved from the Analyst Queue
    String spamMessageSourceId = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSourceId"))).getText();
    
    if(spamMessageSourceId.equals(reviewedMessageSourceId)) {
    	String newSpamMessageSourceId = AQ_WaitForNewSourceId(driver, reviewedMessageSourceId, spamMessageSourceId);
    	spamMessageSourceId = newSpamMessageSourceId;
    }
    
    String spamMessageSubject = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSubjectLabel"))).getText();
    System.out.println("Spam message 1 Subject: " + spamMessageSubject);
    
    String spamMessageDate = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewDateLabel"))).getText();
    System.out.println("Spam message 1 Date: " + spamMessageDate);
    
	System.out.println("Spam message 1 Source ID: " + spamMessageSourceId);
    
    //Set status to spam and click green Take Action button - using AQ_TakeActionSpam method
    AQ_TakeActionSpam(driver);
    
    //Check that message has been moved from the Analyst Queue
    String newsMessageSourceId = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSourceId"))).getText();
    
    if(newsMessageSourceId.equals(spamMessageSourceId)) {
    	String newNewsMessageSourceId = AQ_WaitForNewSourceId(driver, spamMessageSourceId, newsMessageSourceId);
    	newsMessageSourceId = newNewsMessageSourceId;
    }
    
    String newsMessageSubject = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSubjectLabel"))).getText();
    System.out.println("News message 1 Subject: " + newsMessageSubject);
    
    String newsMessageDate = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewDateLabel"))).getText();
    System.out.println("News message 1 Date: " + newsMessageDate);
    
	System.out.println("News message 1 Source ID: " + newsMessageSourceId);
    
    //Set status to news and click green Take Action button - using AQ_TakeActionNews method
    AQ_TakeActionNews(driver);
    
    //Check that message has been moved from the Analyst Queue
    String reviewedMessageSourceId2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSourceId"))).getText();
    
    if(reviewedMessageSourceId2.equals(newsMessageSourceId)) {
    	String newReviewedMessageSourceId2 = AQ_WaitForNewSourceId(driver, newsMessageSourceId, reviewedMessageSourceId2);
    	reviewedMessageSourceId2 = newReviewedMessageSourceId2;
    }
    
    String reviewedMessageSubject2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSubjectLabel"))).getText();
    System.out.println("Reviewed message 2 Subject: " + reviewedMessageSubject2);
    
    String reviewedMessageDate2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewDateLabel"))).getText();
    System.out.println("Reviewed message 2 Date: " + reviewedMessageDate2);
    
	System.out.println("Reviewed message 2 Source ID: " + reviewedMessageSourceId2);
    
    //Click the Reviewed button
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewReviewedButton"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);
    
    //Check that message has been moved from the Analyst Queue
    String spamMessageSourceId2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSourceId"))).getText();
    
    if(spamMessageSourceId2.equals(reviewedMessageSourceId2)) {
    	String newSpamMessageSourceId2 = AQ_WaitForNewSourceId(driver, reviewedMessageSourceId2, spamMessageSourceId2);
    	spamMessageSourceId2 = newSpamMessageSourceId2;
    }
    
    String spamMessageSubject2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSubjectLabel"))).getText();
    System.out.println("Spam message 2 Subject: " + spamMessageSubject2);
    
    String spamMessageDate2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewDateLabel"))).getText();
    System.out.println("Spam message 2 Date: " + spamMessageDate2);
    
	System.out.println("Spam message 2 Source ID: " + spamMessageSourceId2);
    
    //Click the Spam button
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewSpamButton"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);
    
    //Check that message has been moved from the Analyst Queue
    String newsMessageSourceId2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSourceId"))).getText();
    
    if(newsMessageSourceId2.equals(spamMessageSourceId2)) {
    	String newNewsMessageSourceId2 = AQ_WaitForNewSourceId(driver, spamMessageSourceId2, newsMessageSourceId2);
    	newsMessageSourceId2 = newNewsMessageSourceId2;
    }
    
    String newsMessageSubject2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSubjectLabel"))).getText();
    System.out.println("News message 2 Subject: " + newsMessageSubject2);
    
    String newsMessageDate2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewDateLabel"))).getText();
    System.out.println("News message 2 Date: " + newsMessageDate2);
    
	System.out.println("News message 2 Source ID: " + newsMessageSourceId2);
    
    //Click the News button
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewNewsButton"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);
    
    //Check that message has been moved from the Analyst Queue
    String testMessageSourceId = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSourceId"))).getText();
    
    if(testMessageSourceId.equals(newsMessageSourceId2)) {
    	String newTestMessageSourceId = AQ_WaitForNewSourceId(driver, newsMessageSourceId2, testMessageSourceId);
    	testMessageSourceId = newTestMessageSourceId;
    }
    


    
    //Go to the Search tab
    driver.findElement(By.linkText(prop.getProperty("Lnk_Monitoring_SearchTab"))).click();
    Thread.sleep(2000);

    //Add Quotes to followUpMessageSubject so that it can be used to search
    String followUpMessageSubject_Search = "\"" + followUpMessageSubject + "\"";
    
    //Shorten message timestamp to date for search
    String followUpMessageDate_Search = followUpMessageDate.substring(0, 10);
    
    //Search for message by subject, status (Follow-up), and Date - using SearchMessage method
    String searchReviewedXpath = "Lst_Search_StatusListFollowUp";
    SearchMessage(driver, followUpMessageSubject_Search, followUpMessageDate_Search, searchReviewedXpath);
    
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
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewStatus"))).getText(), "follow-up", "Message status does not match for followUpMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewResolved"))).getText(), "false", "Message status does not match for followUpMessage");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewDate"))).getText(), followUpMessageDate, "Message Date does not match for followUpMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for followUpMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for followUpMessage");
    
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);
    
  //Add Quotes to escalateMessageSubject so that it can be used to search
    String escalateMessageSubject_Search = "\"" + escalateMessageSubject + "\"";
    
    //Shorten message timestamp to date for search
    String escalateMessageDate_Search = escalateMessageDate.substring(0, 10);
    
    //Search for message by subject, status (Escalate), and Date - using SearchMessage method
    searchReviewedXpath = "Lst_Search_StatusListEscalate";
    SearchMessage(driver, escalateMessageSubject_Search, escalateMessageDate_Search, searchReviewedXpath);

    //Click on the message to verify the details
    driver.findElement(By.xpath(".//*[@id='applicationHost']/div/div[1]/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + escalateMessageSubject + "\"]/td[1]")).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);
    
    //Verify that message details (subject, status, date) match the message
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewSubject"))).getText(), escalateMessageSubject, "Message subjects do not match for escalateMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewStatus"))).getText(), "escalate", "Message status does not match for escalateMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewResolved"))).getText(), "false", "Message status does not match for escalateMessage");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewDate"))).getText(), escalateMessageDate, "Message Date does not match for escalateMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for escalateMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for escalateMessage");
    
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);
    
    //Add Quotes to breachMessageSubject so that it can be used to search
    String breachMessageSubject_Search = "\"" + breachMessageSubject + "\"";
    
    //Shorten message timestamp to date for search
    String breachMessageDate_Search = breachMessageDate.substring(0, 10);
    
    //Search for message by subject, status (Breach), and Date - using SearchMessage method
    searchReviewedXpath = "Lst_Search_StatusListBreach";
    SearchMessage(driver, breachMessageSubject_Search, breachMessageDate_Search, searchReviewedXpath);

    //Click on the message to verify the details
    driver.findElement(By.xpath(".//*[@id='applicationHost']/div/div[1]/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + breachMessageSubject + "\"]/td[1]")).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);
    
    //Verify that message details (subject, status, date) match the message
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewSubject"))).getText(), breachMessageSubject, "Message subjects do not match for breachMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewStatus"))).getText(), "breach", "Message status does not match for breachMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewResolved"))).getText(), "false", "Message status does not match for breachMessage");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewDate"))).getText(), breachMessageDate, "Message Date does not match for breachMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for breachMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for breachMessage");
    
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);
    
    //Add Quotes to reviewedMessageSubject so that it can be used to search
    String reviewedMessageSubject_Search = "\"" + reviewedMessageSubject + "\"";
    
    //Shorten message timestamp to date for search
    String reviewedMessageDate_Search = reviewedMessageDate.substring(0, 10);
    
    //Search for message by subject, status (Reviewed), and Date - using SearchMessage method
    searchReviewedXpath = "Lst_Search_StatusListReviewed";
    SearchMessage(driver, reviewedMessageSubject_Search, reviewedMessageDate_Search, searchReviewedXpath);

    //Click on the message to verify the details
    driver.findElement(By.xpath(".//*[@id='applicationHost']/div/div[1]/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + reviewedMessageSubject + "\"]/td[1]")).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);
    
    //Verify that message details (subject, status, date) match the message
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewSubject"))).getText(), reviewedMessageSubject, "Message subjects do not match for reviewedMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewStatus"))).getText(), "reviewed", "Message status does not match for reviewedMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewResolved"))).getText(), "true", "Message status does not match for reviewedMessage");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewDate"))).getText(), reviewedMessageDate, "Message Date does not match for reviewedMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for reviewedMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for reviewedMessage");
    
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);
    
    //Add Quotes to spamMessageSubject so that it can be used to search
    String spamMessageSubject_Search = "\"" + spamMessageSubject + "\"";
    
    //Shorten message timestamp to date for search
    String spamMessageDate_Search = spamMessageDate.substring(0, 10);
    
    //Search for message by subject, status (Spam), and Date - using SearchMessage method
    searchReviewedXpath = "Lst_Search_StatusListSpam";
    SearchMessage(driver, spamMessageSubject_Search, spamMessageDate_Search, searchReviewedXpath);

    //Click on the message to verify the details
    driver.findElement(By.xpath(".//*[@id='applicationHost']/div/div[1]/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + spamMessageSubject + "\"]/td[1]")).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);
    
    //Verify that message details (subject, status, date) match the message
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewSubject"))).getText(), spamMessageSubject, "Message subjects do not match for spamMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewStatus"))).getText(), "spam", "Message status does not match for spamMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewResolved"))).getText(), "true", "Message status does not match for spamMessage");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewDate"))).getText(), spamMessageDate, "Message Date does not match for spamMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for spamMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for spamMessage");
    
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);
    
    //Add Quotes to newsMessageSubject so that it can be used to search
    String newsMessageSubject_Search = "\"" + newsMessageSubject + "\"";
    
    //Shorten message timestamp to date for search
    String newsMessageDate_Search = newsMessageDate.substring(0, 10);
    
    //Search for message by subject, status (News), and Date - using SearchMessage method
    searchReviewedXpath = "Lst_Search_StatusListNews";
    SearchMessage(driver, newsMessageSubject_Search, newsMessageDate_Search, searchReviewedXpath);

    //Click on the message to verify the details
    driver.findElement(By.xpath(".//*[@id='applicationHost']/div/div[1]/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + newsMessageSubject + "\"]/td[1]")).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);
    
    //Verify that message details (subject, status, date) match the message
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewSubject"))).getText(), newsMessageSubject, "Message subjects do not match for newsMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewStatus"))).getText(), "news", "Message status does not match for newsMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewResolved"))).getText(), "true", "Message status does not match for newsMessage");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewDate"))).getText(), newsMessageDate, "Message Date does not match for newsMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for newsMessage");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for newsMessage");
    
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);
    
    //Add Quotes to reviewedMessageSubject2 so that it can be used to search
    String reviewedMessageSubject2_Search = "\"" + reviewedMessageSubject2 + "\"";
    
    //Shorten message timestamp to date for search
    String reviewedMessageDate2_Search = reviewedMessageDate2.substring(0, 10);
    
    //Search for message by subject, status (Reviewed), and Date - using SearchMessage method
    searchReviewedXpath = "Lst_Search_StatusListReviewed";
    SearchMessage(driver, reviewedMessageSubject2_Search, reviewedMessageDate2_Search, searchReviewedXpath);

    //Click on the message to verify the details
    driver.findElement(By.xpath(".//*[@id='applicationHost']/div/div[1]/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + reviewedMessageSubject2 + "\"]/td[1]")).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);
    
    //Verify that message details (subject, status, date) match the message
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewSubject"))).getText(), reviewedMessageSubject2, "Message subjects do not match for reviewedMessage2");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewStatus"))).getText(), "reviewed", "Message status does not match for reviewedMessage2");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewResolved"))).getText(), "true", "Message status does not match for reviewedMessage2");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewDate"))).getText(), reviewedMessageDate2, "Message Date does not match for reviewedMessage2");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for reviewedMessage2");
    //Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for reviewedMessage2");
    
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);
    
    //Add Quotes to spamMessageSubject2 so that it can be used to search
    String spamMessageSubject2_Search = "\"" + spamMessageSubject2 + "\"";
    
    //Shorten message timestamp to date for search
    String spamMessageDate2_Search = spamMessageDate2.substring(0, 10);
    
    //Search for message by subject, status (Spam), and Date - using SearchMessage method
    searchReviewedXpath = "Lst_Search_StatusListSpam";
    SearchMessage(driver, spamMessageSubject2_Search, spamMessageDate2_Search, searchReviewedXpath);

    //Click on the message to verify the details
    driver.findElement(By.xpath(".//*[@id='applicationHost']/div/div[1]/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + spamMessageSubject2 + "\"]/td[1]")).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);
    
    //Verify that message details (subject, status, date) match the message
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewSubject"))).getText(), spamMessageSubject2, "Message subjects do not match for spamMessage2");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewStatus"))).getText(), "spam", "Message status does not match for spamMessage2");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewResolved"))).getText(), "true", "Message status does not match for spamMessage2");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewDate"))).getText(), spamMessageDate2, "Message Date does not match for spamMessage2");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for spamMessage2");
    //Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for spamMessage2");
    
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);
    
    //Add Quotes to newsMessageSubject2 so that it can be used to search
    String newsMessageSubject2_Search = "\"" + newsMessageSubject2 + "\"";
    
    //Shorten message timestamp to date for search
    String newsMessageDate2_Search = newsMessageDate2.substring(0, 10);
    
    //Search for message by subject, status (News), and Date - using SearchMessage method
    searchReviewedXpath = "Lst_Search_StatusListNews";
    SearchMessage(driver, newsMessageSubject2_Search, newsMessageDate2_Search, searchReviewedXpath);

    //Click on the message to verify the details
    driver.findElement(By.xpath(".//*[@id='applicationHost']/div/div[1]/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + newsMessageSubject2 + "\"]/td[1]")).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);
    
    //Verify that message details (subject, status, date) match the message
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewSubject"))).getText(), newsMessageSubject2, "Message subjects do not match for newsMessage2");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewStatus"))).getText(), "news", "Message status does not match for newsMessage2");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewResolved"))).getText(), "true", "Message status does not match for newsMessage2");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewDate"))).getText(), newsMessageDate2, "Message Date does not match for newsMessage2");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for newsMessage2");
    //Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for newsMessage2");
    
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

