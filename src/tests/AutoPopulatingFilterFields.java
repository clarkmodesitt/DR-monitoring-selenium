package tests;

import functions.MonitoringPage_Functions;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class AutoPopulatingFilterFields extends MonitoringPage_Functions {
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
  public void testAssigningMessagesAndMessageActivity() throws Exception {
    // Log in - using Synthesys_Login method
    driver.get(baseUrl);
    driver.manage().window().maximize();
    Synthesys_Login(driver, prop.getProperty("Username2"), prop.getProperty("Password"));

	//Wait for page to load
    WebDriverWait wait = new WebDriverWait(driver, 10);
    //wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Lbl_Synthesys_KnowledgeGraphToQuery"))));
    Thread.sleep(500);
    
    // Go to monitoring and wait for page to load selected KG - using AQ_SelectKGForTesting method
    driver.findElement(By.linkText(prop.getProperty("Lnk_Synthesys_MonitoringTab"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_Monitoring_KGDropdownButton"))));

    AQ_SelectKGForTesting(driver, prop.getProperty("SelectedKG"));

    //Analyst Queue - Setting message statuses

    //Click Show Filters and filter by messages with attachments
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_ShowFiltersButton"))).click();
    Thread.sleep(2000);
    driver.findElement(By.xpath(prop.getProperty("Box_AnalystQueue_MessagesWithAttachmentsCheckbox"))).click();
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_ApplyFiltersButton"))).click();
    Thread.sleep(2000);

    // Select first message and store the message subject, date, source ID, and Activity count
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_ClickFirstMessage"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);
    String messageSubject = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSubjectLabel"))).getText();
    System.out.println("Message Subject: "+ messageSubject);
    
    String messageDate = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewDateLabel"))).getText();
    System.out.println("Message Date: " + messageDate);
    
	String messageSourceId = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSourceId"))).getText();
	System.out.println("Message Source ID: " + messageSourceId);

    String activityLabel = driver.findElement(By.xpath(prop.getProperty("Lnk_AnalystQueue_MsgViewActivityLink"))).getText();
    
    //Set status to follow-up and click green Take Action button - using AQ_TakeActionFollowUp_AssigneeChange method
    AQ_TakeActionFollowUp_AssigneeChange(driver,"Analyst U","Test Comment Here");

    //Log out and log back in using the Analyst account - using Synthesys_Login method
    driver.findElement(By.linkText(prop.getProperty("Lnk_Synthesys_UsernameDropdownAdmin"))).click();
    driver.findElement(By.linkText(prop.getProperty("Lnk_Synthesys_SignOut"))).click();
    Synthesys_Login(driver, prop.getProperty("Username"), prop.getProperty("Password"));

    // Go to monitoring and wait for page to load selected KG - using AQ_SelectKGForTesting method
    Thread.sleep(500);
    driver.findElement(By.linkText(prop.getProperty("Lnk_Synthesys_MonitoringTab"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_Monitoring_KGDropdownButton"))));

    AQ_SelectKGForTesting(driver, prop.getProperty("SelectedKG"));

    //Go to the Action Pending Queue, go to the follow-up messages, open the message, and verify the message details
    driver.findElement(By.linkText(prop.getProperty("Lnk_Monitoring_ActionPendingQueueTab"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RefreshButton"))));
    Thread.sleep(2000);

    driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_StatusColumnFollowUp"))).click();
    Thread.sleep(1000);

    String firstButtonText = driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_StatusColumnFirstButton"))).getText();
    int statusOrder = 0;

    if (driver.findElements(By.xpath(prop.getProperty("Btn_ActionPendingQueue_StatusColumnSecondButton"))).size() != 0) {
      String secondButtonText = driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_StatusColumnSecondButton"))).getText();

      if(firstButtonText.equals("breach") && secondButtonText.equals("escalate")) {
        statusOrder = 2;
        driver.findElement(By.xpath(".//*[@id='status-2']/div/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + messageSubject + "\"]/td[1]")).click();
      }
    }

    else if (firstButtonText.equals("breach") || firstButtonText.equals("escalate")){
      statusOrder = 1;
      driver.findElement(By.xpath(".//*[@id='status-1']/div/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + messageSubject + "\"]/td[1]")).click();
    }
    else {
      driver.findElement(By.xpath(".//*[@id='status-0']/div/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + messageSubject + "\"]/td[1]")).click();
    }

    Thread.sleep(5000);
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_MsgViewSubject"))).getText(), messageSubject, "Message subjects do not match for the message");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewStatus"))).getText(), "follow-up", "Message status does not match for the message");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewResolved"))).getText(), "false", "Message status does not match for the message");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_MsgViewDate"))).getText(), messageDate, "Message Date does not match for the message");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewOwner"))).getText(), "Default Administrator", "Message Owners do not match for the message");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for the message");

    //Update the clickable link for the Activity tab
    char activityChar = activityLabel.charAt(10);
    int activityNum = Character.getNumericValue(activityChar);
    activityNum += 2;

    String updatedActivityLabel = activityLabel.substring(0,10) + activityNum + activityLabel.substring(11);
    activityLabel = updatedActivityLabel;
    System.out.println(activityLabel);

    //Go to the Activity tab and verify the last 3 messages
    driver.findElement(By.linkText(activityLabel)).click();

    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li/div[2]/span[2]")).getText(), "Analyst User viewed this message.", "Line 1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[2]/span[2]")).getText(), "Default Administrator took ownership of the message", "Line 2.1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[3]/span[2]")).getText(), "Default Administrator changed the status to Follow-up", "Line 2.2 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[4]/span[2]")).getText(), "Default Administrator assigned message to Analyst User", "Line 2.3 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[5]/span[2]")).getText(), "Default Administrator added a comment:", "Line 2.4 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[5]/blockquote")).getText(), "\"Test Comment Here\"", "Line 2.5 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[3]/div[2]/span[2]")).getText(), "Default Administrator viewed this message.", "Line 3 of the activity tab doesn't match");

    //Mark the message as escalate and assign it back to the admin account
    APQ_TakeActionEscalate_AssigneeChange(driver, statusOrder, "Default Ad");

    System.out.println("message marked as escalate");
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RefreshButton"))));
    Thread.sleep(2000);

    //Log out and log back in using the admin account - using the Synthesys_Login method
    driver.findElement(By.linkText(prop.getProperty("Lnk_Synthesys_UsernameDropdownAnalyst"))).click();
    driver.findElement(By.linkText(prop.getProperty("Lnk_Synthesys_SignOut"))).click();
    Synthesys_Login(driver, prop.getProperty("Username2"), prop.getProperty("Password"));

    // Go to monitoring and wait for page to load selected KG - using AQ_SelectKGForTesting method
    Thread.sleep(500);
    driver.findElement(By.linkText(prop.getProperty("Lnk_Synthesys_MonitoringTab"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_Monitoring_KGDropdownButton"))));

    AQ_SelectKGForTesting(driver, prop.getProperty("SelectedKG"));

    //Go to the Action Pending Queue, go to the escalate messages, open the message, and verify the message details
    driver.findElement(By.linkText(prop.getProperty("Lnk_Monitoring_ActionPendingQueueTab"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RefreshButton"))));
    Thread.sleep(2000);

    driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_StatusColumnEscalate"))).click();
    Thread.sleep(1000);

    firstButtonText = driver.findElement(By.xpath(prop.getProperty("Btn_ActionPendingQueue_StatusColumnFirstButton"))).getText();
    statusOrder = 0;

    if (firstButtonText.equals("breach")) {
      statusOrder = 1;
      driver.findElement(By.xpath(".//*[@id='status-1']/div/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + messageSubject + "\"]/td[1]")).click();
    }
    else {
      driver.findElement(By.xpath(".//*[@id='status-0']/div/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + messageSubject + "\"]/td[1]")).click();
    }

    Thread.sleep(5000);
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_MsgViewSubject"))).getText(), messageSubject, "Message subjects do not match for the message");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewStatus"))).getText(), "escalate", "Message status does not match for the message");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewResolved"))).getText(), "false", "Message status does not match for the message");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_ActionPendingQueue_MsgViewDate"))).getText(), messageDate, "Message Date does not match for the message");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewOwner"))).getText(), "Default Administrator", "Message Owners do not match for the message");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_ActionPendingQueue_MsgViewAssignee"))).getText(), "Default Administrator", "Message Assignees do not match for the message");

    //Update the clickable link for the Activity tab
    activityNum += 2;

    updatedActivityLabel = activityLabel.substring(0,10) + activityNum + activityLabel.substring(11);
    activityLabel = updatedActivityLabel;
    System.out.println(activityLabel);

    //Go to the Activity tab and verify the last 5 messages
    driver.findElement(By.linkText(activityLabel)).click();

    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li/div[2]/span[2]")).getText(), "Default Administrator viewed this message.", "Line 1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[2]/span[2]")).getText(), "Analyst User changed the status to Escalate", "Line 2.1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[3]/span[2]")).getText(), "Analyst User assigned message to Default Administrator", "Line 2.2 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[3]/div[2]/span[2]")).getText(), "Analyst User viewed this message.", "Line 3 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[4]/div[2]/span[2]")).getText(), "Default Administrator took ownership of the message", "Line 4.1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[4]/div[3]/span[2]")).getText(), "Default Administrator changed the status to Follow-up", "Line 4.2 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[4]/div[4]/span[2]")).getText(), "Default Administrator assigned message to Analyst User", "Line 4.3 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[4]/div[5]/span[2]")).getText(), "Default Administrator added a comment:", "Line 4.4 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[4]/div[5]/blockquote")).getText(), "\"Test Comment Here\"", "Line 4.5 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[5]/div[2]/span[2]")).getText(), "Default Administrator viewed this message.", "Line 5 of the activity tab doesn't match");

    //Mark the message as reviewed and assign it back to the admin account
    APQ_TakeActionReviewed_AssigneeChange(driver, statusOrder, "Analyst U", "Marked as Reviewed");

    System.out.println("message marked as reviewed");
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_ActionPendingQueue_RefreshButton"))));
    Thread.sleep(2000);

    //Log out and log back in using the analyst account - using the Synthesys_Login method
    driver.findElement(By.linkText(prop.getProperty("Lnk_Synthesys_UsernameDropdownAdmin"))).click();
    driver.findElement(By.linkText(prop.getProperty("Lnk_Synthesys_SignOut"))).click();
    Synthesys_Login(driver, prop.getProperty("Username"), prop.getProperty("Password"));

    // Go to monitoring and wait for page to load selected KG - using AQ_SelectKGForTesting method
    Thread.sleep(500);
    driver.findElement(By.linkText(prop.getProperty("Lnk_Synthesys_MonitoringTab"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_Monitoring_KGDropdownButton"))));

    AQ_SelectKGForTesting(driver, prop.getProperty("SelectedKG"));

    //Go to the Search tab
    driver.findElement(By.linkText(prop.getProperty("Lnk_Monitoring_SearchTab"))).click();
    Thread.sleep(2000);

    //Add Quotes to messageSubject so that it can be used to search
    String messageSubject_Search = "\"" + messageSubject + "\"";

    //Shorten message timestamp to date for search
    String messageDate_Search = messageDate.substring(0, 10);

    //Search for message by subject, status (Reviewed), and Date
    String searchReviewedXpath = "Lst_Search_StatusListReviewed";
    SearchMessage(driver, messageSubject_Search, messageDate_Search, searchReviewedXpath);

    //Set list to sort by Sent - Newest First
    new Select(driver.findElement(By.xpath(prop.getProperty("Btn_Search_SortListDropdown")))).selectByVisibleText("Sent - Newest First");
    driver.findElement(By.xpath(prop.getProperty("Lst_Search_SortListNewestFirst"))).click();
    Thread.sleep(2000);

    //Click on the message to verify the details
    driver.findElement(By.xpath(".//*[@id='applicationHost']/div/div[1]/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + messageSubject + "\"]/td[1]")).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);

    //Verify that message details (subject, status, date) match the message
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewSubject"))).getText(), messageSubject, "Message subjects do not match for the message");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewStatus"))).getText(), "reviewed", "Message status does not match for the message");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewResolved"))).getText(), "true", "Message status does not match for the message");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewDate"))).getText(), messageDate, "Message Date does not match for the message");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewOwner"))).getText(), "Default Administrator", "Message Owners do not match for the message");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for the message");

    //Update the clickable link for the Activity tab
    activityNum += 2;

    updatedActivityLabel = activityLabel.substring(0,10) + activityNum + activityLabel.substring(11);
    activityLabel = updatedActivityLabel;
    System.out.println(activityLabel);

    //Go to the Activity tab and verify the last 7 messages
    driver.findElement(By.linkText(activityLabel)).click();

    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li/div[2]/span[2]")).getText(), "Analyst User viewed this message.", "Line 1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[2]/span[2]")).getText(), "Default Administrator changed the status to Reviewed", "Line 2.1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[3]/span[2]")).getText(), "Default Administrator assigned message to Analyst User", "Line 2.2 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[4]/span[2]")).getText(), "Default Administrator marked this message as Resolved", "Line 2.3 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[5]/span[2]")).getText(), "Default Administrator added a comment:", "Line 2.4 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[5]/blockquote")).getText(), "\"Marked as Reviewed\"", "Line 2.5 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[3]/div[2]/span[2]")).getText(), "Default Administrator viewed this message.", "Line 3 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[4]/div[2]/span[2]")).getText(), "Analyst User changed the status to Escalate", "Line 4.1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[4]/div[3]/span[2]")).getText(), "Analyst User assigned message to Default Administrator", "Line 4.2 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[5]/div[2]/span[2]")).getText(), "Analyst User viewed this message.", "Line 5 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[6]/div[2]/span[2]")).getText(), "Default Administrator took ownership of the message", "Line 6.1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[6]/div[3]/span[2]")).getText(), "Default Administrator changed the status to Follow-up", "Line 6.2 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[6]/div[4]/span[2]")).getText(), "Default Administrator assigned message to Analyst User", "Line 6.3 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[6]/div[5]/span[2]")).getText(), "Default Administrator added a comment:", "Line 6.4 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[6]/div[5]/blockquote")).getText(), "\"Test Comment Here\"", "Line 6.5 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[7]/div[2]/span[2]")).getText(), "Default Administrator viewed this message.", "Line 7 of the activity tab doesn't match");

    //Go to the Attachments tab and open an attachment - Save the attachment name
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_MsgViewAttachmentsTab"))).click();
    Thread.sleep(3000);
    String attachmentFileName = driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewAttachmentFile"))).getText();
    driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewAttachmentFile"))).click();
    System.out.println("Viewed attachment: " + attachmentFileName);
    Thread.sleep(3000);
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_MsgViewAttachmentCloseButton"))).click();
    Thread.sleep(1000);
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_MsgViewXButton"))).click();
    Thread.sleep(1000);



    //Click on the message again
    driver.findElement(By.xpath(".//*[@id='applicationHost']/div/div[1]/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + messageSubject + "\"]/td[1]")).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);

    //Update the clickable link for the Activity tab
    activityNum += 2;

    updatedActivityLabel = activityLabel.substring(0,10) + activityNum + activityLabel.substring(11);
    activityLabel = updatedActivityLabel;
    System.out.println(activityLabel);

    //Go to the Activity tab and verify the last 9 messages
    driver.findElement(By.linkText(activityLabel)).click();

    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li/div[2]/span[2]")).getText(), "Analyst User viewed this message.", "Line 1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[2]/span[2]")).getText(), "Analyst User viewed attachment", "Line 2 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[3]/div[2]/span[2]")).getText(), "Analyst User viewed this message.", "Line 3 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[4]/div[2]/span[2]")).getText(), "Default Administrator changed the status to Reviewed", "Line 4.1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[4]/div[3]/span[2]")).getText(), "Default Administrator assigned message to Analyst User", "Line 4.2 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[4]/div[4]/span[2]")).getText(), "Default Administrator marked this message as Resolved", "Line 4.3 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[4]/div[5]/span[2]")).getText(), "Default Administrator added a comment:", "Line 4.4 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[4]/div[5]/blockquote")).getText(), "\"Marked as Reviewed\"", "Line 4.5 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[5]/div[2]/span[2]")).getText(), "Default Administrator viewed this message.", "Line 5 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[6]/div[2]/span[2]")).getText(), "Analyst User changed the status to Escalate", "Line 6.1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[6]/div[3]/span[2]")).getText(), "Analyst User assigned message to Default Administrator", "Line 6.2 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[7]/div[2]/span[2]")).getText(), "Analyst User viewed this message.", "Line 7 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[8]/div[2]/span[2]")).getText(), "Default Administrator took ownership of the message", "Line 8.1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[8]/div[3]/span[2]")).getText(), "Default Administrator changed the status to Follow-up", "Line 8.2 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[8]/div[4]/span[2]")).getText(), "Default Administrator assigned message to Analyst User", "Line 8.3 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[8]/div[5]/span[2]")).getText(), "Default Administrator added a comment:", "Line 8.4 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[8]/div[5]/blockquote")).getText(), "\"Test Comment Here\"", "Line 8.5 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[9]/div[2]/span[2]")).getText(), "Default Administrator viewed this message.", "Line 9 of the activity tab doesn't match");



    // Go back to the Analyst Queue
    driver.findElement(By.linkText(prop.getProperty("Lnk_Monitoring_AnalystQueue"))).click();
    Thread.sleep(2000);

    // Select first message and store the message subject, date, source ID, and Activity count
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_ClickFirstMessage"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);
    String messageSubject2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSubjectLabel"))).getText();
    System.out.println("Message Subject 2: "+ messageSubject2);

    String messageDate2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewDateLabel"))).getText();
    System.out.println("Message Date 2: " + messageDate2);

    String messageSourceId2 = driver.findElement(By.cssSelector(prop.getProperty("Lbl_AnalystQueue_MsgViewSourceId"))).getText();
    System.out.println("Message Source ID 2: " + messageSourceId2);

    //Mark the message as Spam using the quick action button
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewSpamButton"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);

    //Go to the Search tab
    driver.findElement(By.linkText(prop.getProperty("Lnk_Monitoring_SearchTab"))).click();
    Thread.sleep(2000);
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);

    //Add Quotes to messageSubject so that it can be used to search
    String messageSubject2_Search = "\"" + messageSubject2 + "\"";

    //Shorten message timestamp to date for search
    String messageDate2_Search = messageDate2.substring(0, 10);

    //Search for message by subject, status (Reviewed), and Date
    String searchSpamXpath = "Lst_Search_StatusListSpam";
    SearchMessage(driver, messageSubject2_Search, messageDate2_Search, searchSpamXpath);

    //Set list to sort by Sent - Newest First
    new Select(driver.findElement(By.xpath(prop.getProperty("Btn_Search_SortListDropdown")))).selectByVisibleText("Sent - Newest First");
    driver.findElement(By.xpath(prop.getProperty("Lst_Search_SortListNewestFirst"))).click();
    Thread.sleep(2000);

    //Click on the message to verify the details
    driver.findElement(By.xpath(".//*[@id='applicationHost']/div/div[1]/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + messageSubject2 + "\"]/td[1]")).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);

    //Verify that message details (subject, status, date) match the message
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewSubject"))).getText(), messageSubject2, "Message subjects do not match for the message");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewStatus"))).getText(), "spam", "Message status does not match for the message");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewResolved"))).getText(), "true", "Message status does not match for the message");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewDate"))).getText(), messageDate2, "Message Date does not match for the message");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for the message");
    //Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for the message");

    //Click the Take Action button and set the status to Escalate
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_MsgViewRedTakeActionButton"))).click();
    Thread.sleep(1000);
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_TakeActionStatusDropdown"))).click();
    driver.findElement(By.linkText(prop.getProperty("Lnk_Search_StatusListEscalate"))).click();
    Thread.sleep(1000);
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_TakeActionGreenTakeActionButton"))).click();
    Thread.sleep(2000);

    //Click on the message again to verify the details
    driver.findElement(By.xpath(".//*[@id='applicationHost']/div/div[1]/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + messageSubject2 + "\"]/td[1]")).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));
    Thread.sleep(2000);

    //Verify that message details (subject, status, date) match the message
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewSubject"))).getText(), messageSubject2, "Message subjects do not match for the message");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewStatus"))).getText(), "escalate", "Message status does not match for the message");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewResolved"))).getText(), "true", "Message status does not match for the message");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewDate"))).getText(), messageDate2, "Message Date does not match for the message");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for the message");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for the message");


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
}

