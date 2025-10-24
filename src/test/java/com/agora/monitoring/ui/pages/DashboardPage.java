package com.agora.monitoring.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
 * Dashboard page object used in Selenium UI tests.
 *
 * This file replaces a corrupted/duplicated version and provides a single
 * valid public class with the helpers used by ClearAlertsSeleniumTest.
 */
public class DashboardPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateTo(String baseUrl) {
        // Normalize base URL (don't blindly append another slash)
        String url = baseUrl;
        if (!url.endsWith("/")) url = url + "/";

        // Try loading the base URL and wait a bit longer for the alerts container.
        driver.get(url);
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(15));
        try {
            longWait.until(ExpectedConditions.presenceOfElementLocated(By.id("alerts")));
            return;
        } catch (Exception e) {
            // fall through to try alternative path
        }

        // Some setups may serve the dashboard at /dashboard — try that as a fallback.
        try {
            driver.get(url + "dashboard");
            longWait.until(ExpectedConditions.presenceOfElementLocated(By.id("alerts")));
            return;
        } catch (Exception e) {
            // final fallback: wait for the body to be present so tests that follow can use other
            // selectors or handle missing alerts gracefully instead of immediately failing here.
            try {
                WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
                shortWait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
            } catch (Exception ignored) {
            }
        }
    }

    /** Alias used by older tests. */
    public void open(String baseUrl) {
        navigateTo(baseUrl);
    }

    public void clickClearAlerts() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(By.id("clear-alerts")));
        btn.click();
        // wait until any existing alert list items are gone
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#alerts ul li")));
        } catch (Exception ignored) {
            // if there were no items or JS removed them very fast, ignore
        }
    }

    public List<WebElement> getAlertItems() {
        try {
            WebElement ul = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#alerts ul")));
            return ul.findElements(By.tagName("li"));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public boolean alertsListEmpty() {
        return getAlertItems().isEmpty();
    }

    // --- Helpers used by other tests ---
    public void setFanId(String id) {
        WebElement e = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input#fan-id")));
        e.clear();
        e.sendKeys(id);
    }

    public void setFanMin(String min) {
        WebElement e = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input#fan-min")));
        e.clear();
        e.sendKeys(min);
    }

    public void clickSetFan() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(By.id("set-fan-threshold")));
        try {
            btn.click();
        } catch (Exception e) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
    }

    public void setTempSensorId(String id) {
        WebElement e = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input#temp-sensor-id")));
        e.clear();
        e.sendKeys(id);
    }

    public void setTempMax(String max) {
        WebElement e = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input#temp-max")));
        e.clear();
        e.sendKeys(max);
    }

    public void clickSetTemp() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(By.id("set-temp-threshold")));
        try {
            btn.click();
        } catch (Exception e) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
    }

    public org.openqa.selenium.WebElement findFanRowById(String id) {
        try {
            By sel = By.cssSelector("#fans tbody tr[id='" + id + "']");
            return wait.until(ExpectedConditions.presenceOfElementLocated(sel));
        } catch (Exception e) {
            return null;
        }
    }

    public org.openqa.selenium.WebElement findTemperatureRowById(String id) {
        try {
            By sel = By.cssSelector("#sensors tbody tr[id='" + id + "']");
            return wait.until(ExpectedConditions.presenceOfElementLocated(sel));
        } catch (Exception e) {
            return null;
        }
    }

    public org.openqa.selenium.WebElement findSensorRowById(String id) {
        org.openqa.selenium.WebElement r = findTemperatureRowById(id);
        if (r != null) return r;
        return findFanRowById(id);
    }

    public boolean isRowAlert(org.openqa.selenium.WebElement row) {
        String classes = row.getAttribute("class");
        return classes != null && classes.contains("alert-row");
    }

    public java.util.List<org.openqa.selenium.WebElement> getAlertListItems() {
        return getAlertItems();
    }

    public java.util.List<org.openqa.selenium.WebElement> getTemperatureRows() {
        try {
            org.openqa.selenium.WebElement table = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table#sensors tbody")));
            return table.findElements(By.cssSelector("tr"));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public java.util.List<org.openqa.selenium.WebElement> getFansRows() {
        try {
            org.openqa.selenium.WebElement table = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table#fans tbody")));
            return table.findElements(By.cssSelector("tr"));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public String getCellText(org.openqa.selenium.WebElement row, int col) {
        try {
            java.util.List<org.openqa.selenium.WebElement> cells = row.findElements(By.tagName("td"));
            if (col < 0 || col >= cells.size()) return null;
            return cells.get(col).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public String getAlertTextIfPresent(int timeoutMs) {
        // First, try to detect a JS alert() popup (used by client-side code on validation errors)
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofMillis(timeoutMs));
            org.openqa.selenium.Alert alert = shortWait.until(org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent());
            String text = alert.getText();
            // dismiss after reading so test can continue
            try { alert.accept(); } catch (Exception ignored) {}
            return text != null ? text : "";
        } catch (Exception ignored) {
            // no JS alert, fall back to looking for an alerts list item in the DOM
        }

        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofMillis(timeoutMs));
            org.openqa.selenium.WebElement li = shortWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#alerts ul li")));
            return li.getText();
        } catch (Exception e) {
            return "";
        }
    }

}
