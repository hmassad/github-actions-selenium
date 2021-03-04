package com.canix.tests;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
public class LoginTest extends BaseSeleniumTest {

    @Test
    public void login() throws InterruptedException{
        this.driver.manage().deleteAllCookies();

        WebDriverWait wait = new WebDriverWait(driver, 20);

        driver.manage().window().maximize();

        Map<String,String> params = new HashMap<>();
        params.put("base.url", "https://app.staging.canix.com");
        params.put("login.url", "/login");
        params.put("login.username","melody.xu@t2vsoft.com");
        params.put("login.password","!t2vsoft!canix!");

        try {
            // open url
            driver.get(params.get("base.url") + params.get("login.url") );

            driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[1]/div/main/div/div[1]/div[2]/div/div/form/div[1]/label/label/div/div/div/input")).sendKeys( params.get("login.username") );
            Print.setpLog("fill email field::parameter[" + params.get("login.username") + "]" );
            driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[1]/div/main/div/div[1]/div[2]/div/div/form/div[2]/label/label/div/div/div/input")).sendKeys( params.get("login.password") );
            Print.setpLog("fill password field::parameter[" + params.get("login.password") + "]");

            // click login button
            driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[1]/div/main/div/div[1]/div[2]/div/div/form/div[3]/div/button/h5")).click();
            Print.setpLog("click login button");

            checkLogin(0, params);

            // switch to OK
            driver.findElement(By.xpath("//div[@label='Facility']//p[text()='Facility']")).click();
            //*[@id="tippy-1"]/div/ul/li[1]
          //  Browser.wait_for_response();
            //*[@id="tippy-11"]/div/ul/li[1]
            List<WebElement> customerList = driver.findElements(By.xpath("//*[contains(@id,'tippy-')]/div/ul/li"));
            for (WebElement webElement : customerList) {
                if( webElement.getText().contains("Canix CA") ) {
                    webElement.click();
                    Print.setpLog("switch to Canix CA."  );
                    break;
                }
            }
//            Browser.wait_for_response();
//            Browser.wait_for_response();
//            Browser.wait_for_response();
//            Browser.wait_for_response();


        }catch(NoSuchElementException e) {
            e.printStackTrace();
            fail( "log in failed." );
        }

    }

    /**
     * Test every 10 seconds for success
     */
    private void checkLogin(int checkTimes, Map<String,String> params) throws InterruptedException {
        try {
            checkTimes++;
            Print.setpLog("Thread.sleep(10000);");
            Thread.sleep( 1000*10*1 );
            String currentUrl = driver.getCurrentUrl();
            if( currentUrl.equals(params.get("login.url")) ) {
                String tagName = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[1]/div/main/div/div[1]/div[2]/div[2]/p")).getTagName();
                if( "p".equals(tagName) ) {
                    Print.setpLog("Invalid login, please try again.");
                    fail( "log in failed." );
                }

                if(checkTimes >= 10 ) {
                    Print.setpLog("Wait for 100 seconds, but the login is not successful.");
                    fail( "log in failed." );
                }
            }else {
                Print.setpLog("log in successful.");
            }
        }catch(NoSuchElementException e) {
            e.printStackTrace();
            checkLogin(checkTimes, params);
        }
    }
}
