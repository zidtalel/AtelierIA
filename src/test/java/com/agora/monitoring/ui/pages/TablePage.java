package com.agora.monitoring.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object représentant une page contenant un tableau (Températures ou Fans)
 */
public class TablePage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final String baseUrl;

    public TablePage(WebDriver driver, String baseUrl) {
        this.driver = driver;
        this.baseUrl = baseUrl;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void openTemperaturesPage() {
        // The dashboard contains the temperatures table with id 'sensors'
        driver.get(baseUrl + "/");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table#sensors")));
    }

    public void openFansPage() {
        // The dashboard contains the fans table with id 'fans'
        driver.get(baseUrl + "/");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table#fans")));
    }

    /**
     * Clique sur la ligne du tableau identifiée par l'attribut data-id égal à l'id fourni.
     */
    public void clickRowByDataId(String tableSelector, String dataId) {
        // The templates set the tr id attribute to the sensor/fan id (th:id="${s.id}"), so use attribute selector
        By rowSelector = By.cssSelector(tableSelector + " tbody tr[id='" + dataId + "']");
        WebElement row = wait.until(ExpectedConditions.presenceOfElementLocated(rowSelector));
        try {
            wait.until(ExpectedConditions.elementToBeClickable(rowSelector));
            row.click();
        } catch (Exception e) {
            // fallback: perform JS click if normal click not possible in headless environment
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", row);
        }
    }

    public String getInputValue(String inputCssSelector) {
        By sel = By.cssSelector(inputCssSelector);
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(sel));
        return input.getAttribute("value");
    }
}
