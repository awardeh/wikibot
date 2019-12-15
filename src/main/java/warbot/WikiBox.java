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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

class WikiBox {

    private static final String PATH = "./screenshot.jpg";//path to screenshot
    private static final String INPUTS = "./inputs.txt";//stores user inputs


    static void scrapeWikiPic(String input) throws IOException, InterruptedException {
        //save user inputs in text file
        logInputPic((input));
        //get the driver from the getPage method
        ChromeDriver driver = getPage(input);
        //set wait time for element to load to 2 seconds
        WebDriverWait wait = new WebDriverWait(driver, 5);
        try {
            WebElement infobox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("infobox")));
            Screenshot myScreenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(driver, infobox);
            ImageIO.write(myScreenshot.getImage(), "jpg", new File(PATH));
            driver.quit();

        } catch (TimeoutException e) {
            e.printStackTrace();
            driver.quit();
            throw new NoSuchElementException("bruh not found");
        } catch (Exception e) {
            e.printStackTrace();
            driver.quit();
            throw new NoSuchElementException("dummythicc");
        }
    }

    public static String scrapeWikiText(String input) throws IOException, InterruptedException {
        System.setProperty("webdriver.gecko.driver", "./geckodriver"); //path to geckdriver
        //save user inputs in text file
        logInputText(input);
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
            Elements date = infobox.get(0).getElementsByTag("tr");
            return Jsoup.parse(date.get(0).toString()).text() + "\n\n" +
                    Jsoup.parse(date.get(1).toString()).text() + "\n\n" +
                    Jsoup.parse(date.get(2).toString()).text() + "\n\n" +
                    Jsoup.parse(date.get(4).toString()).text() + "\n\n" +
                    Jsoup.parse(date.get(5).toString()).text() + "\n\n" +
                    Jsoup.parse(date.get(6).toString()).text() + "\n\n" +
                    Jsoup.parse(date.get(8).toString()).text();
        } else {
            driver.quit();
            throw new NoSuchElementException("not found boss");
        }
    }


    private static ChromeDriver getPage(String input) throws InterruptedException {
        //start firefox in headless mode
        ChromeOptions options = new ChromeOptions();
        options.setBinary("/usr/bin/chromium-browser");
        options.setHeadless(true);
        ChromeDriver driver = new ChromeDriver(options);
        //opens the wikipedia main page and finds the search box
        driver.get("http://en.wikipedia.org/");
        WebElement searchBox = driver.findElement(By.id("searchInput"));
        //sends input
        searchBox.sendKeys(input);
        //waits for search results to load and loads first result
        Thread.sleep(700);
        searchBox.sendKeys(Keys.ARROW_DOWN);
        searchBox.sendKeys(Keys.RETURN);

        return driver;
    }

    private static void logInputPic(String input) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(INPUTS, true));
        out.write("pic " + input);
        out.newLine();
        out.close();
    }

    private static void logInputText(String input) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(INPUTS, true));
        out.write("pic " + input);
        out.newLine();
        out.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println(scrapeWikiText("Battle of the Bulge"));
    }
}