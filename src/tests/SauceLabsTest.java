import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;


public class SauceLabsTest {

  public static final String USERNAME = "clark_modesitt";
  public static final String ACCESS_KEY = "9d7dbbcb-9b96-417b-b24d-5d5d0f80c8d5";
  public static final String URL = "http://" + USERNAME + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:80/wd/hub";

  public static void main(String[] args) throws Exception {

    DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
    caps.setCapability("platform", "Windows 7");
    caps.setCapability("version", "11.0");

    WebDriver driver = new RemoteWebDriver(new URL(URL), caps);

    //Go to Naomi's cluster on IE 11
    driver.get("http://naomi-nn.qa.digitalreasoning.com:8666/apps/login");

    //Log in
    driver.findElement(By.id("username")).clear();
    driver.findElement(By.id("username")).sendKeys("admin");
    driver.findElement(By.id("password")).clear();
    driver.findElement(By.id("password")).sendKeys("1234");
    driver.findElement(By.xpath("//button[@type='submit']")).click();

    //Wait for page to load
    WebDriverWait wait = new WebDriverWait(driver, 10);
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='applicationHost']/div/div[3]/div/div[1]/div/div/form/div/label")));
    Thread.sleep(500);

    //Go to the Monitoring tab and Print Analyst Queue link
    driver.findElement(By.linkText("Monitoring")).click();
    Thread.sleep(3000);
    System.out.println(driver.findElement(By.linkText("Analyst Queue")).getText());

    //End of test
    System.out.println("Test PASSED!");

    driver.quit();
  }
}