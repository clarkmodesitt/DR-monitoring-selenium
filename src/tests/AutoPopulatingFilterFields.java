package tests;

import functions.MonitoringPage_Functions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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
  public void testAutoPopulatingFilterFields() throws Exception {
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

    AQ_SelectKGForTesting(driver, prop.getProperty("Bush52KG"));

    //Go to the Search tab
    driver.findElement(By.linkText(prop.getProperty("Lnk_Monitoring_SearchTab"))).click();
    Thread.sleep(2000);

    //Uncheck the auto-hide filters check box
    driver.findElement(By.xpath(prop.getProperty("Box_Search_AutoHideFiltersCheckBox"))).click();
    Thread.sleep(1000);

    //Enter jeb into the Sender field and click Add jeb
    String name1 = "jeb";
    String nameArray[] = name1.split("(?!^)");

    for(int i = 0; i < nameArray.length; i++) {
      driver.findElement(By.xpath(prop.getProperty("Txt_Search_Sender"))).sendKeys(nameArray[i]);
      Thread.sleep(300);
    }

    driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SenderOption1"))).click();

    //Verify the button displays correctly in the Sender field
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SenderLabelButton"))).getText(), "jeb", "Button in Sender field isn't displayed correctly.");

    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SearchButton"))).click();
    Thread.sleep(2000);

    //Verify that no results return
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_NoMessagesLabel"))).getText(), "There are no messages for your query.", "No Messages Label isn't displayed.");

    //Clear the Sender field, Enter jeb* and click Add jeb*
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SenderXButton"))).click();

    name1 = "jeb*";
    nameArray = name1.split("(?!^)");

    for(int i = 0; i < nameArray.length; i++) {
      driver.findElement(By.xpath(prop.getProperty("Txt_Search_Sender"))).sendKeys(nameArray[i]);
      Thread.sleep(300);
    }

    driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SenderOption1"))).click();

    //Verify the button displays correctly in the Sender field
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SenderLabelButton"))).getText(), "jeb*", "Button in Sender field isn't displayed correctly.");

    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SearchButton"))).click();
    Thread.sleep(2000);

    //Verify the number of messages that return
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MessageCount"))).getText(), "Showing 1 - 12 of 12", "Message count for jeb* is not what was expected.");

    //Clear the Sender field, Enter jeb and click Jeb Bush in the dropdown
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SenderXButton"))).click();

    name1 = "jeb";
    nameArray = name1.split("(?!^)");

    for(int i = 0; i < nameArray.length; i++) {
      driver.findElement(By.xpath(prop.getProperty("Txt_Search_Sender"))).sendKeys(nameArray[i]);
      Thread.sleep(300);
    }

    driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SenderOption2"))).click();

    //Verify the button displays correctly in the Sender field
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SenderLabelButton"))).getText(), "Jeb Bush", "Button in Sender field isn't displayed correctly.");

    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SearchButton"))).click();
    Thread.sleep(2000);

    //Verify the number of messages that return
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MessageCount"))).getText(), "Showing 1 - 12 of 12", "Message count for Jeb Bush is not what was expected.");

    //Clear the Sender field, Enter jeb and click jeb@jeb.com in the dropdown
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SenderXButton"))).click();

    for(int i = 0; i < nameArray.length; i++) {
      driver.findElement(By.xpath(prop.getProperty("Txt_Search_Sender"))).sendKeys(nameArray[i]);
      Thread.sleep(300);
    }

    driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SenderOption4"))).click();

    //Verify the button displays correctly in the Sender field
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SenderLabelButton"))).getText(), "jeb@jeb.com", "Button in Sender field isn't displayed correctly.");

    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SearchButton"))).click();
    Thread.sleep(2000);

    //Verify the number of messages that return
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MessageCount"))).getText(), "Showing 1 - 11 of 11", "Message count for jeb@jeb.com is not what was expected.");

    //Clear the Sender field, Enter *bu* and click Add *bu* in the dropdown - Change to *bu*
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SenderXButton"))).click();

    name1 = "*bu*";
    nameArray = name1.split("(?!^)");

    for(int i = 0; i < nameArray.length; i++) {
      driver.findElement(By.xpath(prop.getProperty("Txt_Search_Sender"))).sendKeys(nameArray[i]);
      Thread.sleep(300);
    }

    driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SenderOption1"))).click();

    //Verify the button displays correctly in the Sender field
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SenderLabelButton"))).getText(), "*bu*", "Button in Sender field isn't displayed correctly.");

    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SearchButton"))).click();
    Thread.sleep(2000);

    //Verify the number of messages that return
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MessageCount"))).getText(), "Showing 1 - 13 of 13", "Message count for *bu* is not what was expected.");

    //Click Clear All Filters
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ClearAllFiltersButton"))).click();

    //Enter jeb into the Recipient field and click Add jeb
    name1 = "jeb";
    nameArray = name1.split("(?!^)");

    for(int i = 0; i < nameArray.length; i++) {
      driver.findElement(By.xpath(prop.getProperty("Txt_Search_Recipient"))).sendKeys(nameArray[i]);
      Thread.sleep(300);
    }

    driver.findElement(By.xpath(prop.getProperty("Lbl_Search_RecipientOption1"))).click();

    //Verify the button displays correctly in the Recipient field
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_RecipientLabelButton"))).getText(), "jeb", "Button in Recipient field isn't displayed correctly.");

    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SearchButton"))).click();
    Thread.sleep(2000);

    //Verify that no results return
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_NoMessagesLabel"))).getText(), "There are no messages for your query.", "No Messages Label isn't displayed.");

    //Clear the Recipient field, Enter jeb* and click Add jeb*
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_RecipientXButton"))).click();

    name1 = "jeb*";
    nameArray = name1.split("(?!^)");

    for(int i = 0; i < nameArray.length; i++) {
      driver.findElement(By.xpath(prop.getProperty("Txt_Search_Recipient"))).sendKeys(nameArray[i]);
      Thread.sleep(300);
    }

    driver.findElement(By.xpath(prop.getProperty("Lbl_Search_RecipientOption1"))).click();

    //Verify the button displays correctly in the Recipient field
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_RecipientLabelButton"))).getText(), "jeb*", "Button in Recipient field isn't displayed correctly.");

    //Check the Bidirectional sender/recipient box and click Search
    driver.findElement(By.id(prop.getProperty("Box_Search_BidirectionalSenderRecipient"))).click();
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SearchButton"))).click();
    Thread.sleep(2000);

    //Verify the number of messages that return
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MessageCount"))).getText(), "Showing 1 - 25 of 52", "Message count for jeb* is not what was expected.");

    //Uncheck the Bidirectional box and clear out the Recipient field.
    driver.findElement(By.id(prop.getProperty("Box_Search_BidirectionalSenderRecipient"))).click();
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_RecipientXButton"))).click();

    //Enter jeb in the Recipient field and click Jeb Bush in the dropdown
    name1 = "jeb";
    nameArray = name1.split("(?!^)");

    for(int i = 0; i < nameArray.length; i++) {
      driver.findElement(By.xpath(prop.getProperty("Txt_Search_Recipient"))).sendKeys(nameArray[i]);
      Thread.sleep(300);
    }

    driver.findElement(By.xpath(prop.getProperty("Lbl_Search_RecipientOption2"))).click();

    //Verify the button displays correctly in the Recipient field
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_RecipientLabelButton"))).getText(), "Jeb Bush", "Button in Recipient field isn't displayed correctly.");

    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SearchButton"))).click();
    Thread.sleep(2000);

    //Verify the number of messages that return
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MessageCount"))).getText(), "Showing 1 - 25 of 40", "Message count for Jeb Bush is not what was expected.");

    //Clear the Recipient field, Enter jeb and click toni@jeb.com in the dropdown
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_RecipientXButton"))).click();

    for(int i = 0; i < nameArray.length; i++) {
      driver.findElement(By.xpath(prop.getProperty("Txt_Search_Recipient"))).sendKeys(nameArray[i]);
      Thread.sleep(300);
    }

    driver.findElement(By.xpath(prop.getProperty("Lbl_Search_RecipientOption5"))).click();

    //Verify the button displays correctly in the Recipient field
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_RecipientLabelButton"))).getText(), "toni@jeb.com", "Button in Recipient field isn't displayed correctly.");

    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SearchButton"))).click();
    Thread.sleep(2000);

    //Verify the number of messages that return
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MessageCount"))).getText(), "Showing 1 - 1 of 1", "Message count for toni@jeb.com is not what was expected.");

    //Clear the Recipient field, Enter myflorida.com and click Add myflorida.com in the dropdown
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_RecipientXButton"))).click();

    name1 = "myflorida.com";
    nameArray = name1.split("(?!^)");

    for(int i = 0; i < nameArray.length; i++) {
      driver.findElement(By.xpath(prop.getProperty("Txt_Search_Recipient"))).sendKeys(nameArray[i]);
      Thread.sleep(300);
    }

    driver.findElement(By.xpath(prop.getProperty("Lbl_Search_RecipientOption1"))).click();

    //Verify the button displays correctly in the Recipient field
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_RecipientLabelButton"))).getText(), "myflorida.com", "Button in Recipient field isn't displayed correctly.");

    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SearchButton"))).click();
    Thread.sleep(2000);

    //Verify the number of messages that return
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_NoMessagesLabel"))).getText(), "There are no messages for your query.", "No Messages Label isn't displayed.");

    //Clear the Recipient field, Enter *myflorida.com and click Add *myflorida.com in the dropdown
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_RecipientXButton"))).click();

    name1 = "*myflorida.com";
    nameArray = name1.split("(?!^)");

    for(int i = 0; i < nameArray.length; i++) {
      driver.findElement(By.xpath(prop.getProperty("Txt_Search_Recipient"))).sendKeys(nameArray[i]);
      Thread.sleep(300);
    }

    driver.findElement(By.xpath(prop.getProperty("Lbl_Search_RecipientOption1"))).click();

    //Verify the button displays correctly in the Recipient field
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_RecipientLabelButton"))).getText(), "*myflorida.com", "Button in Recipient field isn't displayed correctly.");

    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SearchButton"))).click();
    Thread.sleep(2000);

    //Verify the number of messages that return
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MessageCount"))).getText(), "Showing 1 - 25 of 25", "Message count for toni@jeb.com is not what was expected.");

    //Clear the Recipient field and paste Ken Darby into the Sender field
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_RecipientXButton"))).click();

    driver.findElement(By.xpath(prop.getProperty("Txt_Search_Sender"))).sendKeys("Ken Darby" + Keys.RETURN);

    //Verify the button displays correctly in the Recipient field
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SenderLabelButton"))).getText(), "Ken Darby", "Button in Sender field isn't displayed correctly.");

    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SearchButton"))).click();
    Thread.sleep(2000);

    //Verify the number of messages that return
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MessageCount"))).getText(), "Showing 1 - 1 of 1", "Message count for Ken Darby is not what was expected.");

    //Clear the Sender field and paste Pam Dana into the Recipient field
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SenderXButton"))).click();

    driver.findElement(By.xpath(prop.getProperty("Txt_Search_Recipient"))).sendKeys("Pam Dana" + Keys.RETURN);

    //Verify the button displays correctly in the Recipient field
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_RecipientLabelButton"))).getText(), "Pam Dana", "Button in Recipient field isn't displayed correctly.");

    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SearchButton"))).click();
    Thread.sleep(2000);

    //Verify the number of messages that return
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MessageCount"))).getText(), "Showing 1 - 2 of 2", "Message count for Pam Dana is not what was expected.");

    //Change to the five_users KG
    AQ_SelectKGForTesting(driver, prop.getProperty("FiveUsersKG"));

    //Click Clear All Filters button and enter bu in the Sender BU text box
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ClearAllFiltersButton"))).click();

    name1 = "bu";
    nameArray = name1.split("(?!^)");

    for(int i = 0; i < nameArray.length; i++) {
      driver.findElement(By.xpath(prop.getProperty("Txt_Search_SenderBU"))).sendKeys(nameArray[i]);
      Thread.sleep(300);
    }

    driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SenderBUOption2"))).click();

    //Verify the button displays correctly in the Sender BU field
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SenderBULabelButton"))).getText(), "a business unit", "Button in Sender BU field isn't displayed correctly.");

    //Enter bu in the Recipient BU text box
    for(int i = 0; i < nameArray.length; i++) {
      driver.findElement(By.xpath(prop.getProperty("Txt_Search_RecipientBU"))).sendKeys(nameArray[i]);
      Thread.sleep(300);
    }

    driver.findElement(By.xpath(prop.getProperty("Lbl_Search_RecipientBUOption2"))).click();

    //Verify the button displays correctly in the Recipient BU field
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_RecipientBULabelButton"))).getText(), "a business unit", "Button in Recipient BU field isn't displayed correctly.");

    //Enter bu in the Sender Dept text box
    name1 = "de";
    nameArray = name1.split("(?!^)");

    for(int i = 0; i < nameArray.length; i++) {
      driver.findElement(By.xpath(prop.getProperty("Txt_Search_SenderDept"))).sendKeys(nameArray[i]);
      Thread.sleep(300);
    }

    driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SenderDeptOption2"))).click();

    //Verify the button displays correctly in the Sender Dept field
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SenderDeptLabelButton"))).getText(), "a department", "Button in Sender Dept field isn't displayed correctly.");

    //Enter bu in the Recipient Dept text box
    for(int i = 0; i < nameArray.length; i++) {
      driver.findElement(By.xpath(prop.getProperty("Txt_Search_RecipientDept"))).sendKeys(nameArray[i]);
      Thread.sleep(300);
    }

    driver.findElement(By.xpath(prop.getProperty("Lbl_Search_RecipientDeptOption2"))).click();

    //Verify the button displays correctly in the Recipient Dept field
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_RecipientDeptLabelButton"))).getText(), "a department", "Button in Recipient Dept field isn't displayed correctly.");

    //Run the search
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SearchButton"))).click();
    Thread.sleep(2000);

    //Verify the number of messages that return
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MessageCount"))).getText(), "Showing 1 - 25 of 124", "Message count for BU and Dept fields is not what was expected.");

    //Click Clear All Filters button and enter *b* in the Sender BU text box
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_ClearAllFiltersButton"))).click();

    name1 = "*b*";
    nameArray = name1.split("(?!^)");

    for(int i = 0; i < nameArray.length; i++) {
      driver.findElement(By.xpath(prop.getProperty("Txt_Search_SenderBU"))).sendKeys(nameArray[i]);
      Thread.sleep(300);
    }

    driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SenderBUOption1"))).click();

    //Verify the button displays correctly in the Sender BU field
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SenderBULabelButton"))).getText(), "*b*", "Button in Sender BU field isn't displayed correctly.");

    //Enter *b* in the Recipient BU text box
    for(int i = 0; i < nameArray.length; i++) {
      driver.findElement(By.xpath(prop.getProperty("Txt_Search_RecipientBU"))).sendKeys(nameArray[i]);
      Thread.sleep(300);
    }

    driver.findElement(By.xpath(prop.getProperty("Lbl_Search_RecipientBUOption1"))).click();

    //Verify the button displays correctly in the Recipient BU field
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_RecipientBULabelButton"))).getText(), "*b*", "Button in Recipient BU field isn't displayed correctly.");

    //Enter *d* in the Sender Dept text box
    name1 = "*d*";
    nameArray = name1.split("(?!^)");

    for(int i = 0; i < nameArray.length; i++) {
      driver.findElement(By.xpath(prop.getProperty("Txt_Search_SenderDept"))).sendKeys(nameArray[i]);
      Thread.sleep(300);
    }

    driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SenderDeptOption1"))).click();

    //Verify the button displays correctly in the Sender Dept field
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_SenderDeptLabelButton"))).getText(), "*d*", "Button in Sender Dept field isn't displayed correctly.");

    //Enter *d* in the Recipient Dept text box
    for(int i = 0; i < nameArray.length; i++) {
      driver.findElement(By.xpath(prop.getProperty("Txt_Search_RecipientDept"))).sendKeys(nameArray[i]);
      Thread.sleep(300);
    }

    driver.findElement(By.xpath(prop.getProperty("Lbl_Search_RecipientDeptOption1"))).click();

    //Verify the button displays correctly in the Recipient Dept field
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_RecipientDeptLabelButton"))).getText(), "*d*", "Button in Recipient Dept field isn't displayed correctly.");

    //Run the search
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SearchButton"))).click();
    Thread.sleep(2000);

    //Verify the number of messages that return
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MessageCount"))).getText(), "Showing 1 - 25 of 124", "Message count for BU and Dept fields is not what was expected.");

    //Go to the Analyst Queue
    driver.findElement(By.linkText(prop.getProperty("Lnk_Monitoring_AnalystQueue"))).click();
    Thread.sleep(2000);

    //Change to the bush_52 KG
    AQ_SelectKGForTesting(driver, prop.getProperty("Bush52KG"));










    //Go to the Analyst Queue
    driver.findElement(By.linkText(prop.getProperty("Lnk_Monitoring_AnalystQueue"))).click();
    Thread.sleep(2000);

    //Set the Sort to Sent - Newest First
    new Select(driver.findElement(By.xpath(prop.getProperty("Btn_AnalystQueue_SortListDropdown")))).selectByVisibleText("Sent - Newest First");
    Thread.sleep(2000);


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

