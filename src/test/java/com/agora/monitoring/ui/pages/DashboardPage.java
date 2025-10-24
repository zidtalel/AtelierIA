package com.agora.monitoring.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class DashboardPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open(String baseUrl) {
        driver.get(baseUrl);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sensors")));
    }

    public WebElement findSensorRowById(String id) {
        // try sensors table first
        By selector = By.cssSelector("#sensors tbody tr[id='" + id + "']");
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(selector));
        } catch (Exception e) {
            // try fans table
            selector = By.cssSelector("#fans tbody tr[id='" + id + "']");
            return wait.until(ExpectedConditions.presenceOfElementLocated(selector));
        }
    }

    public boolean isRowAlert(WebElement row) {
        String classes = row.getAttribute("class");
        return classes != null && classes.contains("alert-row");
    }

    public List<WebElement> getAlertListItems() {
        return driver.findElements(By.cssSelector("#alerts ul li"));
    }

    // additional helpers
    public WebElement getTemperatureTable() { return driver.findElement(By.cssSelector("table#sensors")); }
    public List<WebElement> getTemperatureRows() { return getTemperatureTable().findElements(By.cssSelector("tbody tr")); }
    public WebElement getFansTable() { return driver.findElement(By.cssSelector("table#fans")); }
    public List<WebElement> getFansRows() { return getFansTable().findElements(By.cssSelector("tbody tr")); }

    public void setFanId(String id) {
        var e = driver.findElement(By.id("fan-id"));
        e.clear();
        e.sendKeys(id);
    }

    public void setFanMin(String min) {
        var e = driver.findElement(By.id("fan-min"));
        e.clear();
        e.sendKeys(min);
    }

    public void clickSetFan() { driver.findElement(By.id("set-fan-threshold")).click(); }

    public WebElement findFanRowById(String id) {
        for (WebElement r : getFansRows()) {
            String cell = getCellText(r, 0);
            if (cell != null && cell.equals(id)) return r;
        }
        return null;
    }

    public String getCellText(WebElement row, int colIndex) {
        return row.findElements(By.cssSelector("td")).get(colIndex).getText();
    }

    public void setTempSensorId(String id) {
        var e = driver.findElement(By.id("temp-sensor-id"));
        e.clear();
        e.sendKeys(id);
    }

    public void setTempMax(String max) {
        var e = driver.findElement(By.id("temp-max"));
        e.clear();
        e.sendKeys(max);
    }

    public void clickSetTemp() { driver.findElement(By.id("set-temp-threshold")).click(); }

    public String getAlertTextIfPresent(long timeoutMs) {
        try {
            WebDriverWait w = new WebDriverWait(driver, Duration.ofMillis(timeoutMs));
            w.until(ExpectedConditions.alertIsPresent());
            var a = driver.switchTo().alert();
            String txt = a.getText();
            a.accept();
            return txt;
        } catch (Exception e) {
            return null;
        }
    }

    public WebElement findTemperatureRowById(String id) {
        for (WebElement r : getTemperatureRows()) {
            String cell = getCellText(r, 0);
            if (cell != null && cell.equals(id)) return r;
        }
        return null;
    }
}
