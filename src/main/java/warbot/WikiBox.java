package warbot;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

class WikiBox {

    private static final String PATH = "./screenshot.jpg";//path to screenshot
    private static final String INPUTS = "./inputs.txt";//stores user inputs


    static void scrapeWikiPic(String input) throws Exception {
        System.setProperty("webdriver.gecko.driver", "./geckodriver"); //path to geckdriver
        //save user inputs in text file
        BufferedWriter out = new BufferedWriter(new FileWriter(INPUTS, true));
        out.write("pic " + input);
        out.newLine();
        out.close();
        //get the driver from the getPage method
        FirefoxDriver driver = getPage(input);
        //set wait time for element to load to 2 seconds
        WebDriverWait wait = new WebDriverWait(driver, 5);
        try {
            WebElement infobox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("infobox")));
            Screenshot myScreenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(driver, infobox);
            ImageIO.write(myScreenshot.getImage(), "jpg", new File(PATH));
            driver.close();
        } catch (NoSuchElementException e) {
            driver.close();
            e.printStackTrace();
        }
    }

    private static FirefoxDriver getPage(String input) throws InterruptedException {
        //start firefox in headless mode
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);
        FirefoxDriver driver = new FirefoxDriver(options);
        //opens the wikipedia main page and finds the search box
        driver.get("http://en.wikipedia.org/");
        WebElement searchBox = driver.findElement(By.id("searchInput"));
        //sends input
        searchBox.sendKeys(input);
        //waits for search results to load and loads first result
        Thread.sleep(600);
        searchBox.sendKeys(Keys.ARROW_DOWN);
        searchBox.sendKeys(Keys.RETURN);

        return driver;
    }
}