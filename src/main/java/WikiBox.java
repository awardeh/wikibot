import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.File;

class WikiBox {

    private static final String path = "C:\\Users\\aw\\IdeaProjects\\warbot\\test.jpg";

    static void scrapeWikiPic(String input) throws Exception {
        FirefoxDriver driver = new FirefoxDriver();
        driver.get("http://en.wikipedia.org/");
        WebElement searchBox = driver.findElement(By.id("searchInput"));
        searchBox.sendKeys(input);
        Thread.sleep(500);
        searchBox.sendKeys(Keys.ARROW_DOWN);
        searchBox.sendKeys(Keys.RETURN);
        Thread.sleep(500);
        try {
            WebElement infobox = driver.findElementByClassName( "infobox");
            Screenshot myScreenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(driver, infobox);
            ImageIO.write(myScreenshot.getImage(), "jpg", new File(path));
            driver.close();
        } catch (Exception NoSuchElementException) {
            driver.close();
            throw new NoSuchElementException("can't find element boss");
        }
    }
}