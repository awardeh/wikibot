package warbot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

class WikiBox implements Logger {

    private static final String PATH = "./screenshot.jpg";//path to screenshot

    static void screenshotInfobox(String input) throws Exception {
        //save user inputs in text file
        Logger.logInput(("pic " + input));
        //get the driver from the getPage method
        ChromeDriver driver = getPage(input);
        //set wait time for element to load to 2 seconds
        WebDriverWait wait = new WebDriverWait(driver, 3);
        try {
            WebElement infobox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("infobox")));
            Screenshot myScreenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(driver, infobox);
            ImageIO.write(myScreenshot.getImage(), "jpg", new File(PATH));
        } catch (TimeoutException e) {
            e.printStackTrace();
            throw new NoSuchElementException("bruh not found");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("dummythicc");
        } finally {
            driver.quit();
        }
    }

    public static String scrapeInfobox(String input) throws IOException {
        //save user inputs in text file
        Logger.logInput(("wiki " + input));
        //get the driver from the getPage method
        ChromeDriver driver = getPage(input);
        //set wait time for element to load to 500 ms
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        if (driver.findElements(By.className("infobox")).size() > 0) {
            Document document = Jsoup.connect(driver.getCurrentUrl()).get();
            Document.OutputSettings settings = new Document.OutputSettings();
            settings.prettyPrint(true);
            driver.quit();
            Elements infobox = document.getElementsByClass("infobox");
            Elements infoboxText = infobox.get(0).getElementsByTag("tr");
            StringBuilder myString = new StringBuilder();
            for (int i = 0; i < infoboxText.size() / 2; i++) {
                if (!Jsoup.parse(infoboxText.get(i).toString()).text().isBlank()) {
                    myString.append(Jsoup.parse(infoboxText.get(i).toString()).text()).append("\n\n");
                }
            }
            return myString.toString();
        } else {
            driver.quit();
            throw new NoSuchElementException("not found boss");
        }
    }

    public static String getImage(String input) throws IOException {
        //save user inputs in text file
        Logger.logInput(("image " + input));
        //get the driver from the getPage method
        ChromeDriver driver = getPage(input);
        //set wait time for element to load to 500 ms
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        List<WebElement> images = driver.findElementByClassName("mw-parser-output").findElements(By.tagName("img"));
        if (images.size() > 0) {
            String imgURL = images.get(0).getAttribute("src");
            driver.quit();
            return imgURL;
        } else {
            driver.quit();
            throw new NoSuchElementException("not found boss");
        }
    }


    private static ChromeDriver getPage(String input) {
        //start firefox in headless mode
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);
        ChromeDriver driver = new ChromeDriver(options);
        //opens the wikipedia main page and finds the search box
        driver.get("http://en.wikipedia.org/");
        WebElement searchBox = driver.findElement(By.id("searchInput"));
        //sends input
        searchBox.sendKeys(input);
        //waits for search results to load and loads first result
        driver.manage().timeouts().implicitlyWait(700, TimeUnit.MILLISECONDS);
        searchBox.sendKeys(Keys.ARROW_DOWN);
        searchBox.sendKeys(Keys.RETURN);

        return driver;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(getImage("Battle of the Bulge"));
    }
}