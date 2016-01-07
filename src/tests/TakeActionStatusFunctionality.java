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
  public void testTakeActionStatusFunctionality() throws Exception {
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
    System.out.println("Follow-up message Subject: "+ followUpMessageSubject);
    
    String followUpMessageDate = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewDateLabel"))).getText();
    System.out.println("Follow-up message Date: " + followUpMessageDate);
    
	String followUpMessageSourceId = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewSourceId"))).getText();
	System.out.println("Follow-up message Source ID: " + followUpMessageSourceId);
    
    //Set status to follow-up and test cancel functionality (with X icon)
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))).click();
    Thread.sleep(1000);
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_StatusDropdownButton"))).click();
    driver.findElement(By.xpath(prop.getProperty("Lst_AnalystQueue_StatusListFollowUp"))).click();
    Thread.sleep(1000);
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_TakeActionCancelXButton"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);
    
    //Verify that message is not changed/moved from Analyst queue
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewSubjectLabel"))).getText(), followUpMessageSubject, "Did not cancel properly.");
    
    // Set status to follow-up and test cancel functionality (with Cancel button)
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))).click();
    Thread.sleep(1000);
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_StatusDropdownButton"))).click();
    driver.findElement(By.xpath(prop.getProperty("Lst_AnalystQueue_StatusListFollowUp"))).click();
    Thread.sleep(1000);
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_TakeActionLowerCancelButton"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);
    
    //Verify that message is not changed/moved from Analyst queue
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewSubjectLabel"))).getText(), followUpMessageSubject, "Did not cancel properly.");
    
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
    System.out.println("Escalate message Subject: " + escalateMessageSubject);
    
    String escalateMessageDate = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewDateLabel"))).getText();
    System.out.println("Escalate message Date: " + escalateMessageDate);
    
	System.out.println("Escalate message Source ID: " + escalateMessageSourceId);
    
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
    System.out.println("Breach message Subject: " + breachMessageSubject);
    
    String breachMessageDate = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewDateLabel"))).getText();
    System.out.println("Breach message Date: " + breachMessageDate);
    
	System.out.println("Breach message Source ID: " + breachMessageSourceId);
    
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
    String reviewedMessageSourceId = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewSourceId"))).getText();
    
    if(reviewedMessageSourceId.equals(breachMessageSourceId)) {
    	String newReviewedMessageSourceId = AQ_WaitForNewSourceId(driver, breachMessageSourceId, reviewedMessageSourceId);
    	reviewedMessageSourceId = newReviewedMessageSourceId;
    }
    
    String reviewedMessageSubject = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewSubjectLabel"))).getText();
    System.out.println("Reviewed message 1 Subject: " + reviewedMessageSubject);
    
    String reviewedMessageDate = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewDateLabel"))).getText();
    System.out.println("Reviewed message 1 Date: " + reviewedMessageDate);
    
	System.out.println("Reviewed message 1 Source ID: " + reviewedMessageSourceId);
    
    //Set status to reviewed and click green Take Action button
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))).click();
    Thread.sleep(1000);
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_StatusDropdownButton"))).click();
    driver.findElement(By.xpath(prop.getProperty("Lst_AnalystQueue_StatusListReviewed"))).click();
    Thread.sleep(1000);
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_TakeActionGreenTakeActionButton"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);
    
    //Check that message has been moved from the Analyst Queue
    String spamMessageSourceId = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewSourceId"))).getText();
    
    if(spamMessageSourceId.equals(reviewedMessageSourceId)) {
    	String newSpamMessageSourceId = AQ_WaitForNewSourceId(driver, reviewedMessageSourceId, spamMessageSourceId);
    	spamMessageSourceId = newSpamMessageSourceId;
    }
    
    String spamMessageSubject = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewSubjectLabel"))).getText();
    System.out.println("Spam message 1 Subject: " + spamMessageSubject);
    
    String spamMessageDate = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewDateLabel"))).getText();
    System.out.println("Spam message 1 Date: " + spamMessageDate);
    
	System.out.println("Spam message 1 Source ID: " + spamMessageSourceId);
    
    //Set status to spam and click green Take Action button
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))).click();
    Thread.sleep(1000);
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_StatusDropdownButton"))).click();
    driver.findElement(By.xpath(prop.getProperty("Lst_AnalystQueue_StatusListSpam"))).click();
    Thread.sleep(1000);
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_TakeActionGreenTakeActionButton"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);
    
    //Check that message has been moved from the Analyst Queue
    String newsMessageSourceId = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewSourceId"))).getText();
    
    if(newsMessageSourceId.equals(spamMessageSourceId)) {
    	String newNewsMessageSourceId = AQ_WaitForNewSourceId(driver, spamMessageSourceId, newsMessageSourceId);
    	newsMessageSourceId = newNewsMessageSourceId;
    }
    
    String newsMessageSubject = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewSubjectLabel"))).getText();
    System.out.println("News message 1 Subject: " + newsMessageSubject);
    
    String newsMessageDate = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewDateLabel"))).getText();
    System.out.println("News message 1 Date: " + newsMessageDate);
    
	System.out.println("News message 1 Source ID: " + newsMessageSourceId);
    
    //Set status to news and click green Take Action button
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))).click();
    Thread.sleep(1000);
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_StatusDropdownButton"))).click();
    driver.findElement(By.xpath(prop.getProperty("Lst_AnalystQueue_StatusListNews"))).click();
    Thread.sleep(1000);
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_TakeActionGreenTakeActionButton"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);
    
    //Check that message has been moved from the Analyst Queue
    String reviewedMessageSourceId2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewSourceId"))).getText();
    
    if(reviewedMessageSourceId2.equals(newsMessageSourceId)) {
    	String newReviewedMessageSourceId2 = AQ_WaitForNewSourceId(driver, newsMessageSourceId, reviewedMessageSourceId2);
    	reviewedMessageSourceId2 = newReviewedMessageSourceId2;
    }
    
    String reviewedMessageSubject2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewSubjectLabel"))).getText();
    System.out.println("Reviewed message 2 Subject: " + reviewedMessageSubject2);
    
    String reviewedMessageDate2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewDateLabel"))).getText();
    System.out.println("Reviewed message 2 Date: " + reviewedMessageDate2);
    
	System.out.println("Reviewed message 2 Source ID: " + reviewedMessageSourceId2);
    
    //Click the Reviewed button
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewReviewedButton"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);
    
    //Check that message has been moved from the Analyst Queue
    String spamMessageSourceId2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewSourceId"))).getText();
    
    if(spamMessageSourceId2.equals(reviewedMessageSourceId2)) {
    	String newSpamMessageSourceId2 = AQ_WaitForNewSourceId(driver, reviewedMessageSourceId2, spamMessageSourceId2);
    	spamMessageSourceId2 = newSpamMessageSourceId2;
    }
    
    String spamMessageSubject2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewSubjectLabel"))).getText();
    System.out.println("Spam message 2 Subject: " + spamMessageSubject2);
    
    String spamMessageDate2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewDateLabel"))).getText();
    System.out.println("Spam message 2 Date: " + spamMessageDate2);
    
	System.out.println("Spam message 2 Source ID: " + spamMessageSourceId2);
    
    //Click the Spam button
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewSpamButton"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);
    
    //Check that message has been moved from the Analyst Queue
    String newsMessageSourceId2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewSourceId"))).getText();
    
    if(newsMessageSourceId2.equals(spamMessageSourceId2)) {
    	String newNewsMessageSourceId2 = AQ_WaitForNewSourceId(driver, spamMessageSourceId2, newsMessageSourceId2);
    	newsMessageSourceId2 = newNewsMessageSourceId2;
    }
    
    String newsMessageSubject2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewSubjectLabel"))).getText();
    System.out.println("News message 2 Subject: " + newsMessageSubject2);
    
    String newsMessageDate2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewDateLabel"))).getText();
    System.out.println("News message 2 Date: " + newsMessageDate2);
    
	System.out.println("News message 2 Source ID: " + newsMessageSourceId2);
    
    //Click the News button
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewNewsButton"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);
    
    //Check that message has been moved from the Analyst Queue
    String testMessageSourceId = driver.findElement(By.cssSelector(prop.getProperty("Lbl_MessageQueue_MsgViewSourceId"))).getText();
    
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
    
    //Search for message by subject, status (Follow-up), and Date
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ClearAllFiltersButton"))).click();
    driver.findElement(By.id(prop.getProperty("Txt_Search_KeywordsInSubject"))).sendKeys(followUpMessageSubject_Search);
    driver.findElement(By.xpath(prop.getProperty("Lst_Search_StatusListFollowUp"))).click();
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
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SearchResultMessageStatus"))).getText(), "Follow-up", "Message status does not match");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SearchResultMessageDate"))).getText(), followUpMessageDate, "Message Date does not match");
    
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);
    
  //Add Quotes to escalateMessageSubject so that it can be used to search
    String escalateMessageSubject_Search = "\"" + escalateMessageSubject + "\"";
    
    //Shorten message timestamp to date for search
    String escalateMessageDate_Search = escalateMessageDate.substring(0, 10);
    
    //Search for message by subject, status (Escalate), and Date
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ClearAllFiltersButton"))).click();
    driver.findElement(By.id(prop.getProperty("Txt_Search_KeywordsInSubject"))).sendKeys(escalateMessageSubject_Search);
    driver.findElement(By.xpath(prop.getProperty("Lst_Search_StatusListEscalate"))).click();
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SentDateDropdownButton"))).click();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateFromField"))).clear();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateFromField"))).sendKeys(escalateMessageDate_Search);
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateToField"))).clear();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateToField"))).sendKeys(escalateMessageDate_Search);
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SentDateApplyButton"))).click();
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SearchButton"))).click();
    Thread.sleep(2000);
    
    //Verify that message details (subject, status, date) match the message
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_SearchResultMessageSubject"))).getText(), escalateMessageSubject, "Message subjects do not match");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SearchResultMessageStatus"))).getText(), "Escalate", "Message status does not match");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SearchResultMessageDate"))).getText(), escalateMessageDate, "Message Date does not match");
    
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);
    
    //Add Quotes to breachMessageSubject so that it can be used to search
    String breachMessageSubject_Search = "\"" + breachMessageSubject + "\"";
    
    //Shorten message timestamp to date for search
    String breachMessageDate_Search = breachMessageDate.substring(0, 10);
    
    //Search for message by subject, status (Breach), and Date
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ClearAllFiltersButton"))).click();
    driver.findElement(By.id(prop.getProperty("Txt_Search_KeywordsInSubject"))).sendKeys(breachMessageSubject_Search);
    driver.findElement(By.xpath(prop.getProperty("Lst_Search_StatusListBreach"))).click();
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SentDateDropdownButton"))).click();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateFromField"))).clear();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateFromField"))).sendKeys(breachMessageDate_Search);
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateToField"))).clear();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateToField"))).sendKeys(breachMessageDate_Search);
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SentDateApplyButton"))).click();
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SearchButton"))).click();
    Thread.sleep(2000);
    
    //Verify that message details (subject, status, date) match the message
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_SearchResultMessageSubject"))).getText(), breachMessageSubject, "Message subjects do not match");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SearchResultMessageStatus"))).getText(), "Breach", "Message status does not match");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SearchResultMessageDate"))).getText(), breachMessageDate, "Message Date does not match");
    
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);
    
    //Add Quotes to reviewedMessageSubject so that it can be used to search
    String reviewedMessageSubject_Search = "\"" + reviewedMessageSubject + "\"";
    
    //Shorten message timestamp to date for search
    String reviewedMessageDate_Search = reviewedMessageDate.substring(0, 10);
    
    //Search for message by subject, status (Reviewed), and Date
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ClearAllFiltersButton"))).click();
    driver.findElement(By.id(prop.getProperty("Txt_Search_KeywordsInSubject"))).sendKeys(reviewedMessageSubject_Search);
    driver.findElement(By.xpath(prop.getProperty("Lst_Search_StatusListReviewed"))).click();
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SentDateDropdownButton"))).click();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateFromField"))).clear();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateFromField"))).sendKeys(reviewedMessageDate_Search);
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateToField"))).clear();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateToField"))).sendKeys(reviewedMessageDate_Search);
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SentDateApplyButton"))).click();
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SearchButton"))).click();
    Thread.sleep(2000);
    
    //Verify that message details (subject, status, date) match the message
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_SearchResultMessageSubject"))).getText(), reviewedMessageSubject, "Message subjects do not match");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SearchResultMessageStatus"))).getText(), "Reviewed", "Message status does not match");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SearchResultMessageDate"))).getText(), reviewedMessageDate, "Message Date does not match");
    
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);
    
    //Add Quotes to spamMessageSubject so that it can be used to search
    String spamMessageSubject_Search = "\"" + spamMessageSubject + "\"";
    
    //Shorten message timestamp to date for search
    String spamMessageDate_Search = spamMessageDate.substring(0, 10);
    
    //Search for message by subject, status (Spam), and Date
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ClearAllFiltersButton"))).click();
    driver.findElement(By.id(prop.getProperty("Txt_Search_KeywordsInSubject"))).sendKeys(spamMessageSubject_Search);
    driver.findElement(By.xpath(prop.getProperty("Lst_Search_StatusListSpam"))).click();
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SentDateDropdownButton"))).click();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateFromField"))).clear();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateFromField"))).sendKeys(spamMessageDate_Search);
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateToField"))).clear();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateToField"))).sendKeys(spamMessageDate_Search);
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SentDateApplyButton"))).click();
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SearchButton"))).click();
    Thread.sleep(2000);
    
    //Verify that message details (subject, status, date) match the message
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_SearchResultMessageSubject"))).getText(), spamMessageSubject, "Message subjects do not match");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SearchResultMessageStatus"))).getText(), "Spam", "Message status does not match");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SearchResultMessageDate"))).getText(), spamMessageDate, "Message Date does not match");
    
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);
    
    //Add Quotes to newsMessageSubject so that it can be used to search
    String newsMessageSubject_Search = "\"" + newsMessageSubject + "\"";
    
    //Shorten message timestamp to date for search
    String newsMessageDate_Search = newsMessageDate.substring(0, 10);
    
    //Search for message by subject, status (News), and Date
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ClearAllFiltersButton"))).click();
    driver.findElement(By.id(prop.getProperty("Txt_Search_KeywordsInSubject"))).sendKeys(newsMessageSubject_Search);
    driver.findElement(By.xpath(prop.getProperty("Lst_Search_StatusListNews"))).click();
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SentDateDropdownButton"))).click();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateFromField"))).clear();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateFromField"))).sendKeys(newsMessageDate_Search);
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateToField"))).clear();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateToField"))).sendKeys(newsMessageDate_Search);
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SentDateApplyButton"))).click();
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SearchButton"))).click();
    Thread.sleep(2000);
    
    //Verify that message details (subject, status, date) match the message
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_SearchResultMessageSubject"))).getText(), newsMessageSubject, "Message subjects do not match");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SearchResultMessageStatus"))).getText(), "News", "Message status does not match");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SearchResultMessageDate"))).getText(), newsMessageDate, "Message Date does not match");
    
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);
    
    //Add Quotes to reviewedMessageSubject2 so that it can be used to search
    String reviewedMessageSubject2_Search = "\"" + reviewedMessageSubject2 + "\"";
    
    //Shorten message timestamp to date for search
    String reviewedMessageDate2_Search = reviewedMessageDate2.substring(0, 10);
    
    //Search for message by subject, status (Reviewed), and Date
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ClearAllFiltersButton"))).click();
    driver.findElement(By.id(prop.getProperty("Txt_Search_KeywordsInSubject"))).sendKeys(reviewedMessageSubject2_Search);
    driver.findElement(By.xpath(prop.getProperty("Lst_Search_StatusListReviewed"))).click();
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SentDateDropdownButton"))).click();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateFromField"))).clear();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateFromField"))).sendKeys(reviewedMessageDate2_Search);
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateToField"))).clear();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateToField"))).sendKeys(reviewedMessageDate2_Search);
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SentDateApplyButton"))).click();
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SearchButton"))).click();
    Thread.sleep(2000);
    
    //Verify that message details (subject, status, date) match the message
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_SearchResultMessageSubject"))).getText(), reviewedMessageSubject2, "Message subjects do not match");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SearchResultMessageStatus"))).getText(), "Reviewed", "Message status does not match");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SearchResultMessageDate"))).getText(), reviewedMessageDate2, "Message Date does not match");
    
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);
    
    //Add Quotes to spamMessageSubject2 so that it can be used to search
    String spamMessageSubject_Search2 = "\"" + spamMessageSubject2 + "\"";
    
    //Shorten message timestamp to date for search
    String spamMessageDate_Search2 = spamMessageDate2.substring(0, 10);
    
    //Search for message by subject, status (Spam), and Date
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ClearAllFiltersButton"))).click();
    driver.findElement(By.id(prop.getProperty("Txt_Search_KeywordsInSubject"))).sendKeys(spamMessageSubject_Search2);
    driver.findElement(By.xpath(prop.getProperty("Lst_Search_StatusListSpam"))).click();
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SentDateDropdownButton"))).click();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateFromField"))).clear();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateFromField"))).sendKeys(spamMessageDate_Search2);
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateToField"))).clear();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateToField"))).sendKeys(spamMessageDate_Search2);
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SentDateApplyButton"))).click();
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SearchButton"))).click();
    Thread.sleep(2000);
    
    //Verify that message details (subject, status, date) match the message
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_SearchResultMessageSubject"))).getText(), spamMessageSubject2, "Message subjects do not match");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SearchResultMessageStatus"))).getText(), "Spam", "Message status does not match");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SearchResultMessageDate"))).getText(), spamMessageDate2, "Message Date does not match");
    
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);
    
    //Add Quotes to newsMessageSubject2 so that it can be used to search
    String newsMessageSubject_Search2 = "\"" + newsMessageSubject2 + "\"";
    
    //Shorten message timestamp to date for search
    String newsMessageDate_Search2 = newsMessageDate2.substring(0, 10);
    
    //Search for message by subject, status (News), and Date
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ClearAllFiltersButton"))).click();
    driver.findElement(By.id(prop.getProperty("Txt_Search_KeywordsInSubject"))).sendKeys(newsMessageSubject_Search2);
    driver.findElement(By.xpath(prop.getProperty("Lst_Search_StatusListNews"))).click();
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SentDateDropdownButton"))).click();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateFromField"))).clear();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateFromField"))).sendKeys(newsMessageDate_Search2);
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateToField"))).clear();
    driver.findElement(By.xpath(prop.getProperty("Txt_Search_SentDateToField"))).sendKeys(newsMessageDate_Search2);
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SentDateApplyButton"))).click();
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SearchButton"))).click();
    Thread.sleep(2000);
    
    //Verify that message details (subject, status, date) match the message
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_SearchResultMessageSubject"))).getText(), newsMessageSubject2, "Message subjects do not match");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SearchResultMessageStatus"))).getText(), "News", "Message status does not match");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SearchResultMessageDate"))).getText(), newsMessageDate2, "Message Date does not match");
    
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

