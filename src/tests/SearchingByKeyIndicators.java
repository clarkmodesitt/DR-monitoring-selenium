package tests;

import functions.MonitoringPage_Functions;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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

    AQ_SelectKGForTesting(driver, prop.getProperty("Bush52KG"));

    //Go to the Search tab
    driver.findElement(By.linkText(prop.getProperty("Lnk_Monitoring_SearchTab"))).click();
    Thread.sleep(2000);

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

