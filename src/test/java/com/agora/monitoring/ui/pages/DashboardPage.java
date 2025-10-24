package com.agora.monitoring.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class DashboardPage {
    private final WebDriver driver;

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement getTemperatureTable() {
        return driver.findElement(By.cssSelector("table#sensors"));
    }

    public List<WebElement> getTemperatureRows() {
        return getTemperatureTable().findElements(By.cssSelector("tbody tr"));
    }

    public WebElement getFansTable() {
        return driver.findElement(By.cssSelector("table#fans"));
    }

    public List<WebElement> getFansRows() {
        return getFansTable().findElements(By.cssSelector("tbody tr"));
    }

    public void setFanId(String id) {
        WebElement e = driver.findElement(By.id("fan-id"));
        e.clear();
        e.sendKeys(id);
    }

    public void setFanMin(String min) {
        WebElement e = driver.findElement(By.id("fan-min"));
        e.clear();
        e.sendKeys(min);
    }

    public void clickSetFan() {
        driver.findElement(By.id("set-fan-threshold")).click();
    }

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
        WebElement e = driver.findElement(By.id("temp-sensor-id"));
        e.clear();
        e.sendKeys(id);
    }

    public void setTempMax(String max) {
        WebElement e = driver.findElement(By.id("temp-max"));
        e.clear();
        e.sendKeys(max);
    }

    public void clickSetTemp() {
        driver.findElement(By.id("set-temp-threshold")).click();
    }

    public String getAlertTextIfPresent(long timeoutMs) {
        try {
            org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofMillis(timeoutMs));
            wait.until(org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent());
            org.openqa.selenium.Alert a = driver.switchTo().alert();
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
