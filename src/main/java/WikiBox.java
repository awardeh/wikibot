import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.awt.*;
import java.io.IOException;

public class WikiBox {
    public String title;
    Image sc;
    private String result;
    private String sides;
    private String years;
    private String location;
    private String strength;

    public WikiBox(String input) throws IOException {
        scrapeWiki(input);
    }

    private void scrapeWiki(String input) throws IOException {
        String output = "";
        FirefoxDriver driver = new FirefoxDriver();
        driver.get("http://en.wikipedia.org/");
        WebElement searchBox = driver.findElement(By.id("searchInput"));
        searchBox.sendKeys(input);
        searchBox.sendKeys(Keys.RETURN);
        setTitle(driver.findElement(By.className("summary")).toString());


    }

    void setTitle(String s) {
        this.title = s;
    }

    String getTitle(String s) {
        return this.title;
    }
}

