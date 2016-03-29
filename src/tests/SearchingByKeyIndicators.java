package tests;

import functions.MonitoringPage_Functions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
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

public class SearchingByKeyIndicators extends MonitoringPage_Functions {
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
  public void testSearchingByKeyIndicators() throws Exception {
    JavascriptExecutor jse = (JavascriptExecutor)driver;

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

    AQ_SelectKGForTesting(driver, prop.getProperty("Bush52KG"));

    //Go to the Search tab
    driver.findElement(By.linkText(prop.getProperty("Lnk_Monitoring_SearchTab"))).click();
    Thread.sleep(2000);

    //Select Messages with alerts and All Key Indicators and click Search
    driver.findElement(By.xpath(prop.getProperty("Box_Search_AutoHideFiltersCheckBox"))).click();
    Thread.sleep(1000);
    new Select(driver.findElement(By.id(prop.getProperty("Btn_Search_AlertsListDropdown")))).selectByVisibleText("Messages with alerts");
    new Select(driver.findElement(By.id(prop.getProperty("Lbl_Search_KeyIndicatorsList")))).selectByVisibleText("All Key Indicators");
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SearchButton"))).click();
    Thread.sleep(2000);

    //Set the Showing dropdown to 100
    new Select(driver.findElement(By.cssSelector(prop.getProperty("Btn_Search_ShowingDropdown")))).selectByVisibleText("100");
    Thread.sleep(1000);

    //Verify that all of the messages in the KG are present in the list
    Assert.assertEquals(driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MessageCount"))).getText(), "Showing 1 - 52 of 52", "Message count does not match");

    //Select the Financial KI and click Search
    new Select(driver.findElement(By.id(prop.getProperty("Lbl_Search_KeyIndicatorsList")))).selectByVisibleText("Financial");
    driver.findElement(By.xpath(prop.getProperty("Btn_Search_SearchButton"))).click();
    Thread.sleep(2000);

    //Get the message count for the Financial messages
    String messagesDisplayed = driver.findElement(By.cssSelector(prop.getProperty("Lbl_Search_MessageCount"))).getText();
    String numberOnly = messagesDisplayed.replaceAll("[^0-9]", "");
    int numMessagesDisplayed = 0;

    if(numberOnly.length() == 3) {
      String cutNumber = numberOnly.substring(2);
      numMessagesDisplayed = Integer.parseInt(cutNumber);
    }

    else {
      String cutNumber = numberOnly.substring(3);
      numMessagesDisplayed = Integer.parseInt(cutNumber);
    }

    //Verify that Financial KI is found in each of the displayed messages
    int numForXpath = 3;

    for(int i = 0; i < numMessagesDisplayed; i++) {
      Assert.assertEquals(driver.findElement(By.xpath("//tr[" + numForXpath + "]/td[1]/div/span[span = \"Financial\"]/span")).getText(), "Financial", "Financial KI not found or displaying correctly.");
      numForXpath = numForXpath + 3;
    }

    /*

    //Open up a message and verify that the Financial KI hit is correctly displayed in the message
    driver.findElement(By.xpath(prop.getProperty("Lbl_Search_FinancialKIMessageToOpen"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("Btn_Search_MsgViewRedTakeActionButton"))));
    Thread.sleep(3000);

    //Actions action = new Actions(driver);

    driver.findElement(By.xpath(prop.getProperty("Txt_Search_FirstMessageLine"))).click();

    for (int i = 0; i < 68; i++) {
      //action.moveToElement(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewLastLineCounter")))).perform();
      //action.release();
      driver.findElement(By.xpath(prop.getProperty("Txt_Search_FirstMessageLine"))).sendKeys(Keys.ARROW_DOWN);
      Thread.sleep(100);
    }

    Thread.sleep(5000);

    System.out.println(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewFinanceKIHit"))).getText());
    System.out.println(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewFinanceKIHit"))).getAttribute("class"));
    Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("Lbl_Search_MsgViewFinanceKIHit"))).getText(), "Finance", "Financial KI Highlight not displaying correctly.");

    */

    //

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

