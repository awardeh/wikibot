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

    //    private static final String path = "C:\\Users\\aw\\IdeaProjects\\warbot\\test.jpg";
    private static final String PATH = "./test.jpg";
    private static final String INPUTS = "./inputs.txt";


    static void scrapeWikiPic(String input) throws Exception {
        //save user inputs in text file
        BufferedWriter out = new BufferedWriter(new FileWriter(INPUTS, true));
        out.write("pic " + input);
        out.newLine();
        out.close();

        FirefoxDriver driver = getPage(input);
        WebDriverWait wait = new WebDriverWait(driver, 5);
        try {
            Thread.sleep(600);
            WebElement infobox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("infobox")));
            Screenshot myScreenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(driver, infobox);
            ImageIO.write(myScreenshot.getImage(), "jpg", new File(PATH));
            driver.close();
        } catch (Exception NoSuchElementException) {
            driver.close();
            throw new NoSuchElementException("can't find element boss");
        }
    }

    static void scrapeWikiText(String input) throws Exception {
        FirefoxDriver driver = getPage(input);
        try {
            Thread.sleep(1600);//wait for page load
            WebElement infobox = driver.findElementByClassName("infobox");
            WebElement battleName = infobox.findElement(By.className("summary"));
            WebElement partOf = infobox.findElement(By.xpath("//*[@id=\"mw-content-text\"]/div/div[4]/div[1]/table/tbody/tr[2]/td"));
            WebElement results = infobox.findElement(By.xpath("//*[@id=\"mw-content-text\"]/div/div[4]/div[1]/table/tbody/tr[4]/td/table/tbody"));
        } catch (Exception NoSuchElementException) {
            driver.close();
            throw new NoSuchElementException("can't find element boss");
        }
    }

    private static FirefoxDriver getPage(String input) throws InterruptedException {
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);
        FirefoxDriver driver = new FirefoxDriver(options);
        driver.get("http://en.wikipedia.org/");
        WebElement searchBox = driver.findElement(By.id("searchInput"));
        searchBox.sendKeys(input);
        Thread.sleep(600);
        searchBox.sendKeys(Keys.ARROW_DOWN);
        searchBox.sendKeys(Keys.RETURN);

        return driver;
    }
}

//    public static void main(String[] args) throws Exception {
//        scrapeWikiPic("Battle of the bulge");
//    }
//}