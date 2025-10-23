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

    public String getCellText(WebElement row, int colIndex) {
        return row.findElements(By.cssSelector("td")).get(colIndex).getText();
    }
}
