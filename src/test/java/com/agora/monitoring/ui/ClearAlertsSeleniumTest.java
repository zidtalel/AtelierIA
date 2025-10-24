package com.agora.monitoring.ui;

import com.agora.monitoring.alert.AlertService;
import com.agora.monitoring.web.WebApplication;
import com.agora.monitoring.ui.pages.DashboardPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = WebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClearAlertsSeleniumTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @Autowired
    private AlertService alertService;

    private DashboardPage dashboard;

    @BeforeEach
    public void setUp() {
        // Setup headless Chrome
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        dashboard = new DashboardPage(driver);

        // ensure some alerts exist server-side
        alertService.clearRecent();
        alertService.alert("sensor-1", 99.0, 75.0);
        alertService.alert("sensor-2", 88.0, 70.0);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        // cleanup server-side
        alertService.clearRecent();
    }

    @Test
    public void ClearAlerts_RemovesAll() throws Exception {
        String baseUrl = "http://localhost:" + port;
        // navigate to dashboard
        dashboard.navigateTo(baseUrl);

        // Sanity: server has alerts
        assertTrue(alertService.getRecentAlerts().size() >= 2, "Precondition: server should have at least 2 alerts");

        // Click clear alerts in UI
        dashboard.clickClearAlerts();

        // Verify UI list is empty
        assertTrue(dashboard.alertsListEmpty(), "Alerts list in UI should be empty after clicking Clear Alerts");

        // Verify server-side persistence cleared
        assertTrue(alertService.getRecentAlerts().isEmpty(), "Server-side alerts should be cleared after Clear Alerts");
    }
}
