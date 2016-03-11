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

public class BulkUpdate extends MonitoringPage_Functions {
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
    Synthesys_Login(driver, prop.getProperty("Username"), prop.getProperty("Password"));

	//Wait for page to load
    WebDriverWait wait = new WebDriverWait(driver, 10);
    //wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Lbl_Synthesys_KnowledgeGraphToQuery"))));
    Thread.sleep(500);
    
    // Go to monitoring and wait for page to load selected KG - using AQ_SelectKGForTesting method
    driver.findElement(By.linkText(prop.getProperty("Lnk_Synthesys_MonitoringTab"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_Monitoring_KGDropdownButton"))));

    AQ_SelectKGForTesting(driver, prop.getProperty("SelectedKG"));

    //Set the Sort to Sent - Newest First
    new Select(driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_SortListDropdown")))).selectByVisibleText("Sent - Newest First");
    Thread.sleep(2000);

    //Get the first 10 message subjects
    String messageSubject1 = driver.findElement(By.xpath(prop.getProperty("Lbl_AnalystQueue_QueueMessageSubject1"))).getText();
    String messageDate1 = driver.findElement(By.xpath(prop.getProperty("Lbl_AnalystQueue_QueueMessageDate1"))).getText();

    String messageSubject2 = driver.findElement(By.xpath(prop.getProperty("Lbl_AnalystQueue_QueueMessageSubject2"))).getText();
    String messageDate2 = driver.findElement(By.xpath(prop.getProperty("Lbl_AnalystQueue_QueueMessageDate2"))).getText();

    String messageSubject3 = driver.findElement(By.xpath(prop.getProperty("Lbl_AnalystQueue_QueueMessageSubject3"))).getText();
    String messageDate3 = driver.findElement(By.xpath(prop.getProperty("Lbl_AnalystQueue_QueueMessageDate3"))).getText();

    String messageSubject4 = driver.findElement(By.xpath(prop.getProperty("Lbl_AnalystQueue_QueueMessageSubject4"))).getText();
    String messageDate4 = driver.findElement(By.xpath(prop.getProperty("Lbl_AnalystQueue_QueueMessageDate4"))).getText();

    String messageSubject5 = driver.findElement(By.xpath(prop.getProperty("Lbl_AnalystQueue_QueueMessageSubject5"))).getText();
    String messageDate5 = driver.findElement(By.xpath(prop.getProperty("Lbl_AnalystQueue_QueueMessageDate5"))).getText();

    String messageSubject6 = driver.findElement(By.xpath(prop.getProperty("Lbl_AnalystQueue_QueueMessageSubject6"))).getText();
    String messageDate6 = driver.findElement(By.xpath(prop.getProperty("Lbl_AnalystQueue_QueueMessageDate6"))).getText();

    String messageSubject7 = driver.findElement(By.xpath(prop.getProperty("Lbl_AnalystQueue_QueueMessageSubject7"))).getText();
    String messageDate7 = driver.findElement(By.xpath(prop.getProperty("Lbl_AnalystQueue_QueueMessageDate7"))).getText();

    String messageSubject8 = driver.findElement(By.xpath(prop.getProperty("Lbl_AnalystQueue_QueueMessageSubject8"))).getText();
    String messageDate8 = driver.findElement(By.xpath(prop.getProperty("Lbl_AnalystQueue_QueueMessageDate8"))).getText();

    String messageSubject9 = driver.findElement(By.xpath(prop.getProperty("Lbl_AnalystQueue_QueueMessageSubject9"))).getText();
    String messageDate9 = driver.findElement(By.xpath(prop.getProperty("Lbl_AnalystQueue_QueueMessageDate9"))).getText();

    String messageSubject10 = driver.findElement(By.xpath(prop.getProperty("Lbl_AnalystQueue_QueueMessageSubject10"))).getText();
    String messageDate10 = driver.findElement(By.xpath(prop.getProperty("Lbl_AnalystQueue_QueueMessageDate10"))).getText();

    //Toggle the checkboxes for the first 10 messages
    driver.findElement(By.xpath(prop.getProperty("Box_AnalystQueue_QueueCheckBox1"))).click();
    driver.findElement(By.xpath(prop.getProperty("Box_AnalystQueue_QueueCheckBox2"))).click();
    driver.findElement(By.xpath(prop.getProperty("Box_AnalystQueue_QueueCheckBox3"))).click();
    driver.findElement(By.xpath(prop.getProperty("Box_AnalystQueue_QueueCheckBox4"))).click();
    driver.findElement(By.xpath(prop.getProperty("Box_AnalystQueue_QueueCheckBox5"))).click();
    driver.findElement(By.xpath(prop.getProperty("Box_AnalystQueue_QueueCheckBox6"))).click();
    driver.findElement(By.xpath(prop.getProperty("Box_AnalystQueue_QueueCheckBox7"))).click();
    driver.findElement(By.xpath(prop.getProperty("Box_AnalystQueue_QueueCheckBox8"))).click();
    driver.findElement(By.xpath(prop.getProperty("Box_AnalystQueue_QueueCheckBox9"))).click();
    driver.findElement(By.xpath(prop.getProperty("Box_AnalystQueue_QueueCheckBox10"))).click();
    Thread.sleep(1000);

    //Click the Mark selection as dropdown and select Reviewed
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_MarkSelectionAsDropdown"))).click();
    driver.findElement(By.xpath(prop.getProperty("Lst_AnalystQueue_MarkSelectionListReviewed"))).click();
    Thread.sleep(2000);

    //Verify the message in the Bulk Action modal and click Cancel
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_AnalystQueue_BulkActionMessage"))).getText(), "You are setting 10 messages to reviewed.", "Bulk Action reviewed message doesn't match.");
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_BulkActionCancelButton"))).click();
    Thread.sleep(2000);

    //Click the Mark selection as dropdown and select Spam
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_MarkSelectionAsDropdown"))).click();
    driver.findElement(By.xpath(prop.getProperty("Lst_AnalystQueue_MarkSelectionListSpam"))).click();
    Thread.sleep(2000);

    //Verify the message in the Bulk Action modal and click X button
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_AnalystQueue_BulkActionMessage"))).getText(), "You are setting 10 messages to spam.", "Bulk Action spam message doesn't match.");
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_BulkActionXButton"))).click();
    Thread.sleep(2000);

    //Click the Mark selection as dropdown and select News
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_MarkSelectionAsDropdown"))).click();
    driver.findElement(By.xpath(prop.getProperty("Lst_AnalystQueue_MarkSelectionListNews"))).click();
    Thread.sleep(2000);

    //Verify the message in the Bulk Action modal and click out of the Bulk Action modal
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_AnalystQueue_BulkActionMessage"))).getText(), "You are setting 10 messages to news.", "Bulk Action news message doesn't match.");
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_ClickOffBulkActionModal"))).click();
    Thread.sleep(2000);

    //Click the Mark selection as dropdown and select News
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_MarkSelectionAsDropdown"))).click();
    driver.findElement(By.xpath(prop.getProperty("Lst_AnalystQueue_MarkSelectionListNews"))).click();
    Thread.sleep(2000);

    //Verify the message in the Bulk Action modal and click Confirm
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_AnalystQueue_BulkActionMessage"))).getText(), "You are setting 10 messages to news.", "Bulk Action news message doesn't match.");
    driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_BulkActionConfirmButton"))).click();
    Thread.sleep(2000);

    //Verify that the messages were moved from the Analyst Queue
    Assert.assertNotEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_AnalystQueue_QueueMessageSubject1"))).getText(), messageSubject1, "First message was not removed from the Analyst Queue.");
    Assert.assertNotEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_AnalystQueue_QueueMessageSubject10"))).getText(), messageSubject10, "Last message was not removed from the Analyst Queue.");

    //Make all of the messageSubjects and messageDates searchable
    String messageSubject1_Search = "\"" + messageSubject1 + "\"";
    String messageDate1_Search = messageDate1.substring(0, 10);

    String messageSubject2_Search = "\"" + messageSubject2 + "\"";
    String messageDate2_Search = messageDate2.substring(0, 10);

    String messageSubject3_Search = "\"" + messageSubject3 + "\"";
    String messageDate3_Search = messageDate3.substring(0, 10);

    String messageSubject4_Search = "\"" + messageSubject4 + "\"";
    String messageDate4_Search = messageDate4.substring(0, 10);

    String messageSubject5_Search = "\"" + messageSubject5 + "\"";
    String messageDate5_Search = messageDate5.substring(0, 10);

    String messageSubject6_Search = "\"" + messageSubject6 + "\"";
    String messageDate6_Search = messageDate6.substring(0, 10);

    String messageSubject7_Search = "\"" + messageSubject7 + "\"";
    String messageDate7_Search = messageDate7.substring(0, 10);

    String messageSubject8_Search = "\"" + messageSubject8 + "\"";
    String messageDate8_Search = messageDate8.substring(0, 10);

    String messageSubject9_Search = "\"" + messageSubject9 + "\"";
    String messageDate9_Search = messageDate9.substring(0, 10);

    String messageSubject10_Search = "\"" + messageSubject10 + "\"";
    String messageDate10_Search = messageDate10.substring(0, 10);



    //Go to the Search tab
    driver.findElement(By.linkText(prop.getProperty("Lnk_Monitoring_SearchTab"))).click();
    Thread.sleep(2000);

    //Search for message1 and verify the message activity
    SearchMessage(driver, messageSubject1_Search, messageDate1_Search, "Lst_Search_StatusListNews");

    //Click on the message, verify the message details and go to the Activity tab
    driver.findElement(By.xpath(".//*[@id='applicationHost']/div/div[1]/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + messageSubject1 + "\"]/td[1]")).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));

    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewSubject"))).getText(), messageSubject1, "Message subjects do not match for message1");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewStatus"))).getText(), "news", "Message status does not match for message1");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewResolved"))).getText(), "true", "Message status does not match for message1");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewDate"))).getText(), messageDate1, "Message Date does not match for message1");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for message1");
    //Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for message");

    Thread.sleep(2000);
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_MsgViewActivityTab"))).click();
    Thread.sleep(1000);

    //Verify the last 2 lines on the Activity tab
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li/div[2]/span[2]")).getText(), "Analyst User viewed this message.", "Line 1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[2]/span[2]")).getText(), "Analyst User took ownership of the message", "Line 2.1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[3]/span[2]")).getText(), "Analyst User changed the status to News", "Line 2.2 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[4]/span[2]")).getText(), "Analyst User marked this message as Resolved", "Line 2.3 of the activity tab doesn't match");

    //Search for message2 and verify the message activity
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);
    SearchMessage(driver, messageSubject2_Search, messageDate2_Search, "Lst_Search_StatusListNews");

    //Click on the message, verify the message details and go to the Activity tab
    driver.findElement(By.xpath(".//*[@id='applicationHost']/div/div[1]/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + messageSubject2 + "\"]/td[1]")).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));

    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewSubject"))).getText(), messageSubject2, "Message subjects do not match for message2");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewStatus"))).getText(), "news", "Message status does not match for message2");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewResolved"))).getText(), "true", "Message status does not match for message2");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewDate"))).getText(), messageDate2, "Message Date does not match for message2");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for message2");
    //Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for message");

    Thread.sleep(2000);
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_MsgViewActivityTab"))).click();
    Thread.sleep(1000);

    //Verify the last 2 lines on the Activity tab
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li/div[2]/span[2]")).getText(), "Analyst User viewed this message.", "Line 1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[2]/span[2]")).getText(), "Analyst User took ownership of the message", "Line 2.1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[3]/span[2]")).getText(), "Analyst User changed the status to News", "Line 2.2 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[4]/span[2]")).getText(), "Analyst User marked this message as Resolved", "Line 2.3 of the activity tab doesn't match");

    //Search for message3 and verify the message activity
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);
    SearchMessage(driver, messageSubject3_Search, messageDate3_Search, "Lst_Search_StatusListNews");

    //Click on the message, verify the message details and go to the Activity tab
    driver.findElement(By.xpath(".//*[@id='applicationHost']/div/div[1]/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + messageSubject3 + "\"]/td[1]")).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));

    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewSubject"))).getText(), messageSubject3, "Message subjects do not match for message3");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewStatus"))).getText(), "news", "Message status does not match for message3");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewResolved"))).getText(), "true", "Message status does not match for message3");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewDate"))).getText(), messageDate3, "Message Date does not match for message3");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for message3");
    //Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for message");

    Thread.sleep(2000);
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_MsgViewActivityTab"))).click();
    Thread.sleep(1000);

    //Verify the last 2 lines on the Activity tab
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li/div[2]/span[2]")).getText(), "Analyst User viewed this message.", "Line 1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[2]/span[2]")).getText(), "Analyst User took ownership of the message", "Line 2.1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[3]/span[2]")).getText(), "Analyst User changed the status to News", "Line 2.2 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[4]/span[2]")).getText(), "Analyst User marked this message as Resolved", "Line 2.3 of the activity tab doesn't match");

    //Search for message4 and verify the message activity
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);
    SearchMessage(driver, messageSubject4_Search, messageDate4_Search, "Lst_Search_StatusListNews");

    //Click on the message, verify the message details and go to the Activity tab
    driver.findElement(By.xpath(".//*[@id='applicationHost']/div/div[1]/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + messageSubject4 + "\"]/td[1]")).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));

    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewSubject"))).getText(), messageSubject4, "Message subjects do not match for message4");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewStatus"))).getText(), "news", "Message status does not match for message4");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewResolved"))).getText(), "true", "Message status does not match for message4");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewDate"))).getText(), messageDate4, "Message Date does not match for message4");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for message4");
    //Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for message");

    Thread.sleep(2000);
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_MsgViewActivityTab"))).click();
    Thread.sleep(1000);

    //Verify the last 2 lines on the Activity tab
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li/div[2]/span[2]")).getText(), "Analyst User viewed this message.", "Line 1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[2]/span[2]")).getText(), "Analyst User took ownership of the message", "Line 2.1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[3]/span[2]")).getText(), "Analyst User changed the status to News", "Line 2.2 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[4]/span[2]")).getText(), "Analyst User marked this message as Resolved", "Line 2.3 of the activity tab doesn't match");

    //Search for message5 and verify the message activity
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);
    SearchMessage(driver, messageSubject5_Search, messageDate5_Search, "Lst_Search_StatusListNews");

    //Click on the message, verify the message details and go to the Activity tab
    driver.findElement(By.xpath(".//*[@id='applicationHost']/div/div[1]/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + messageSubject5 + "\"]/td[1]")).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));

    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewSubject"))).getText(), messageSubject5, "Message subjects do not match for message5");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewStatus"))).getText(), "news", "Message status does not match for message5");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewResolved"))).getText(), "true", "Message status does not match for message5");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewDate"))).getText(), messageDate5, "Message Date does not match for message5");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for message5");
    //Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for message");

    Thread.sleep(2000);
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_MsgViewActivityTab"))).click();
    Thread.sleep(1000);

    //Verify the last 2 lines on the Activity tab
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li/div[2]/span[2]")).getText(), "Analyst User viewed this message.", "Line 1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[2]/span[2]")).getText(), "Analyst User took ownership of the message", "Line 2.1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[3]/span[2]")).getText(), "Analyst User changed the status to News", "Line 2.2 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[4]/span[2]")).getText(), "Analyst User marked this message as Resolved", "Line 2.3 of the activity tab doesn't match");

    //Search for message6 and verify the message activity
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);
    SearchMessage(driver, messageSubject6_Search, messageDate6_Search, "Lst_Search_StatusListNews");

    //Click on the message, verify the message details and go to the Activity tab
    driver.findElement(By.xpath(".//*[@id='applicationHost']/div/div[1]/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + messageSubject6 + "\"]/td[1]")).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));

    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewSubject"))).getText(), messageSubject6, "Message subjects do not match for message6");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewStatus"))).getText(), "news", "Message status does not match for message6");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewResolved"))).getText(), "true", "Message status does not match for message6");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewDate"))).getText(), messageDate6, "Message Date does not match for message6");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for message6");
    //Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for message");

    Thread.sleep(2000);
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_MsgViewActivityTab"))).click();
    Thread.sleep(1000);

    //Verify the last 2 lines on the Activity tab
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li/div[2]/span[2]")).getText(), "Analyst User viewed this message.", "Line 1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[2]/span[2]")).getText(), "Analyst User took ownership of the message", "Line 2.1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[3]/span[2]")).getText(), "Analyst User changed the status to News", "Line 2.2 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[4]/span[2]")).getText(), "Analyst User marked this message as Resolved", "Line 2.3 of the activity tab doesn't match");

    //Search for message7 and verify the message activity
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);
    SearchMessage(driver, messageSubject7_Search, messageDate7_Search, "Lst_Search_StatusListNews");

    //Click on the message, verify the message details and go to the Activity tab
    driver.findElement(By.xpath(".//*[@id='applicationHost']/div/div[1]/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + messageSubject7 + "\"]/td[1]")).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));

    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewSubject"))).getText(), messageSubject7, "Message subjects do not match for message7");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewStatus"))).getText(), "news", "Message status does not match for message7");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewResolved"))).getText(), "true", "Message status does not match for message7");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewDate"))).getText(), messageDate7, "Message Date does not match for message7");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for message7");
    //Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for message");

    Thread.sleep(2000);
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_MsgViewActivityTab"))).click();
    Thread.sleep(1000);

    //Verify the last 2 lines on the Activity tab
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li/div[2]/span[2]")).getText(), "Analyst User viewed this message.", "Line 1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[2]/span[2]")).getText(), "Analyst User took ownership of the message", "Line 2.1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[3]/span[2]")).getText(), "Analyst User changed the status to News", "Line 2.2 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[4]/span[2]")).getText(), "Analyst User marked this message as Resolved", "Line 2.3 of the activity tab doesn't match");

    //Search for message8 and verify the message activity
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);
    SearchMessage(driver, messageSubject8_Search, messageDate8_Search, "Lst_Search_StatusListNews");

    //Click on the message, verify the message details and go to the Activity tab
    driver.findElement(By.xpath(".//*[@id='applicationHost']/div/div[1]/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + messageSubject8 + "\"]/td[1]")).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));

    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewSubject"))).getText(), messageSubject8, "Message subjects do not match for message8");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewStatus"))).getText(), "news", "Message status does not match for message8");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewResolved"))).getText(), "true", "Message status does not match for message8");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewDate"))).getText(), messageDate8, "Message Date does not match for message8");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for message8");
    //Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for message");

    Thread.sleep(2000);
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_MsgViewActivityTab"))).click();
    Thread.sleep(1000);

    //Verify the last 2 lines on the Activity tab
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li/div[2]/span[2]")).getText(), "Analyst User viewed this message.", "Line 1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[2]/span[2]")).getText(), "Analyst User took ownership of the message", "Line 2.1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[3]/span[2]")).getText(), "Analyst User changed the status to News", "Line 2.2 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[4]/span[2]")).getText(), "Analyst User marked this message as Resolved", "Line 2.3 of the activity tab doesn't match");

    //Search for message9 and verify the message activity
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);
    SearchMessage(driver, messageSubject9_Search, messageDate9_Search, "Lst_Search_StatusListNews");

    //Click on the message, verify the message details and go to the Activity tab
    driver.findElement(By.xpath(".//*[@id='applicationHost']/div/div[1]/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + messageSubject9 + "\"]/td[1]")).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));

    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewSubject"))).getText(), messageSubject9, "Message subjects do not match for message9");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewStatus"))).getText(), "news", "Message status does not match for message9");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewResolved"))).getText(), "true", "Message status does not match for message9");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewDate"))).getText(), messageDate9, "Message Date does not match for message9");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for message9");
    //Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for message");

    Thread.sleep(2000);
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_MsgViewActivityTab"))).click();
    Thread.sleep(1000);

    //Verify the last 2 lines on the Activity tab
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li/div[2]/span[2]")).getText(), "Analyst User viewed this message.", "Line 1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[2]/span[2]")).getText(), "Analyst User took ownership of the message", "Line 2.1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[3]/span[2]")).getText(), "Analyst User changed the status to News", "Line 2.2 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[4]/span[2]")).getText(), "Analyst User marked this message as Resolved", "Line 2.3 of the activity tab doesn't match");

    //Search for message10 and verify the message activity
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ShowFiltersButton"))).click();
    Thread.sleep(2000);
    SearchMessage(driver, messageSubject10_Search, messageDate10_Search, "Lst_Search_StatusListNews");

    //Click on the message, verify the message details and go to the Activity tab
    driver.findElement(By.xpath(".//*[@id='applicationHost']/div/div[1]/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr[td[1]=\"" + messageSubject10 + "\"]/td[1]")).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_AnalystQueue_MsgViewRedTakeActionButton"))));

    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewSubject"))).getText(), messageSubject10, "Message subjects do not match for message10");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewStatus"))).getText(), "news", "Message status does not match for message10");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewResolved"))).getText(), "true", "Message status does not match for message10");
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MsgViewDate"))).getText(), messageDate10, "Message Date does not match for message10");
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewOwner"))).getText(), "Analyst User", "Message Owners do not match for message10");
    //Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewAssignee"))).getText(), "Analyst User", "Message Assignees do not match for message");

    Thread.sleep(2000);
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_MsgViewActivityTab"))).click();
    Thread.sleep(1000);

    //Verify the last 2 lines on the Activity tab
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li/div[2]/span[2]")).getText(), "Analyst User viewed this message.", "Line 1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[2]/span[2]")).getText(), "Analyst User took ownership of the message", "Line 2.1 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[3]/span[2]")).getText(), "Analyst User changed the status to News", "Line 2.2 of the activity tab doesn't match");
    Assert.assertEquals(driver.findElement(By.xpath("//div/ul/li[2]/div[4]/span[2]")).getText(), "Analyst User marked this message as Resolved", "Line 2.3 of the activity tab doesn't match");

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

